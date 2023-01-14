package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;




//  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

//  The back left wheel encoder is not returning any readings.



@TeleOp(name = "Authomas", group = "Mecanum Drive")
public class Authomas extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private  MyDrivetrain drivetrain = null;
    private MyLinearSlideClass linearSlide = null;
    private  MyClawClass claw = null;

   public static HardwareMap hm = null;

    @Override
    public void init() {

        hm = hardwareMap;

        drivetrain = new MyDrivetrain();
        drivetrain.myInit();

        linearSlide = new MyLinearSlideClass();
        linearSlide.myInit();

        claw = new MyClawClass();
        claw.myInit();

        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();

        drivetrain.myStart();
        linearSlide.myStart();
        claw.myStart();

    }

    @Override
    public void loop() {

        drivetrain.myLoop();
        linearSlide.myLoop();
        claw.myLoop();

        telemetry.update();

    }



    @Override
    public void stop() {
    }

    private class MyLinearSlideClass{

        private DcMotorEx linearSlideMotor = null;

//        private StateMachine stateMachine = new StateMachine();

        public void myInit(){

            linearSlideMotor = Authomas.hm.get(DcMotorEx.class, "LS");

//            //  THIS NEEDS TO BE BEFORE SETMODE!!!
//            linearSlideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

            linearSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            linearSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);

            // reset encoder counts kept by motors.
            linearSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            linearSlideMotor.setTargetPosition(10);


            linearSlideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        }

        public void myStart(){

            linearSlideMotor.setPower(0);
            GoToPosition(positions[0]);

        }

        public void myLoop(){

            LinearSlideMotor();

        }

        private int desiredPosition = 0;

        //  PACEHOLDER!!!!!!!!!!!
        private int[] positions = {10, 1000, 2000, 3000};

        private void LinearSlideMotor(){

            //  Has reached the desired position (within 50 units)

            if(Math.abs(desiredPosition - linearSlideMotor.getCurrentPosition()) < 25){

                linearSlideMotor.setPower(0);

            }

            telemetry.addData("LinearSlide", "Position: " + linearSlideMotor.getCurrentPosition() + ", target position: " + desiredPosition);

        }

        private void GoToPosition(int position){

            linearSlideMotor.setTargetPosition(position);

            desiredPosition = position;

            // set both motors to 25% power. Movement will start. Sign of power is
            // ignored as sign of target encoder position controls direction when
            // running to position.

            linearSlideMotor.setPower(0.75);

        }
    }

    private class MyClawClass{

        private Servo claw = null;

        public void myInit(){

            claw = Authomas.hm.get(Servo.class, "CM");

        }

        public void myStart(){

            claw.setPosition(0);

        }

        public void myLoop(){

            ClawMotor();

        }

        private double clawPosition = 0;

        private void ClawMotor(){

            //  Only change servo position if the button was pressed this frame

//           claw.setPosition(clawPosition);

        }
    }

    private class MyDrivetrain{

        private DcMotorEx frontLeft = null;
        private DcMotorEx frontRight = null;
        private DcMotorEx backLeft = null;
        private DcMotorEx backRight = null;

        public StateMachine stateMachine;

        private int targetPosition = 5000;
        private double power = 0.25;

        public void myInit(){

           stateMachine = new StateMachine();

            //  STATES:
            //  0: Resting
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {

                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(0);

                }

                @Override
                public void OnLoop() {

                }
            });
            //  1: Going forward to position
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {

                }

                @Override
                public void OnLoop() {
                    if(Math.abs(backRight.getCurrentPosition()) < targetPosition) {

                        frontLeft.setPower(power);
                        frontRight.setPower(power);
                        backLeft.setPower(power);
                        backRight.setPower(power);

                    }else{

                        stateMachine.SwitchState(0);

                    }

                    telemetry.addData("BackLeft", "Position: " + backRight.getCurrentPosition() + ", velocity: " + backRight.getVelocity() + ", target position: " + targetPosition);
                }
            });

           stateMachine.SwitchState(1);

            backLeft = Authomas.hm.get(DcMotorEx.class, "BL");
            backRight = Authomas.hm.get(DcMotorEx.class, "BR");
            frontLeft = Authomas.hm.get(DcMotorEx.class, "FL");
            frontRight = Authomas.hm.get(DcMotorEx.class, "FR");

            backLeft.setDirection(DcMotorEx.Direction.REVERSE);
            backRight.setDirection(DcMotorEx.Direction.FORWARD);
            frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
            frontRight.setDirection(DcMotorEx.Direction.FORWARD);


//            backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
//
//            backLeft.setDirection(DcMotorEx.Direction.REVERSE);
//
//            // reset encoder counts kept by motors.
//            backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//            backLeft.setTargetPosition(10);
//
//


            backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

            backRight.setPower(0);

        }

        public void myStart(){

            frontLeft.setPower(0);

            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

        }

        public void myLoop(){

            stateMachine.OnLoop();

        }
    }
}