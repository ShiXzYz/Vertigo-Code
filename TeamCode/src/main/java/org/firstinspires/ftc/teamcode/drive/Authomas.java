package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;




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

        private DcMotor linearSlideMotor = null;

//        private StateMachine stateMachine = new StateMachine();

        public void myInit(){

            linearSlideMotor = Authomas.hm.get(DcMotor.class, "LS");

//            //  THIS NEEDS TO BE BEFORE SETMODE!!!
//            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
            linearSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            linearSlideMotor.setDirection(DcMotor.Direction.REVERSE);

            // reset encoder counts kept by motors.
            linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linearSlideMotor.setTargetPosition(10);


            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        private DcMotor frontLeft = null;
        private DcMotor frontRight = null;
        private DcMotor backLeft = null;
        private DcMotor backRight = null;

        //  0: nothing
        //  1: go forward
        private int state = 0;

        public void myInit(){

            backLeft = Authomas.hm.get(DcMotor.class, "BL");
            backRight = Authomas.hm.get(DcMotor.class, "BR");
            frontLeft = Authomas.hm.get(DcMotor.class, "FL");
            frontRight = Authomas.hm.get(DcMotor.class, "FR");

            backLeft.setDirection(DcMotor.Direction.REVERSE);
            backRight.setDirection(DcMotor.Direction.FORWARD);
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            frontRight.setDirection(DcMotor.Direction.FORWARD);


//            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//            backLeft.setDirection(DcMotor.Direction.REVERSE);
//
//            // reset encoder counts kept by motors.
//            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            backLeft.setTargetPosition(10);
//
//
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            backLeft.setTargetPosition(targetPosition);

        }

        public void myStart(){

            frontLeft.setPower(0);

            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);



        }

        public void myLoop(){

            WheelMotors();

        }


        private void WheelMotors(){

            GoForward();

        }

        private int targetPosition = 500;
        private double power = 0.25;

        private void GoForward(){

            if(Math.abs(backLeft.getCurrentPosition()) < targetPosition){

                frontLeft.setPower(power);
                frontRight.setPower(power);
                backLeft.setPower(power);
                backRight.setPower(power);

            }else{

                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);

            }

            telemetry.addData("BackLeft", "Position: " + frontLeft.getCurrentPosition() + ", target position: " + targetPosition);

        }
    }
}