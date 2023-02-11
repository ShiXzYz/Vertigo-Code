package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.ArrayList;

@TeleOp(name = "Main", group = "Mecanum Drive")
public class Motion extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private  MyDrivetrain drivetrain = null;
    private MyLinearSlideClass linearSlide = null;
    private  MyClawClass claw = null;

   public static HardwareMap hm = null;

    public static double closePos = 0.6, openPos = 0.4;

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

        private StateMachine stateMachine;

        public void myInit(){

            linearSlideMotor = Motion.hm.get(DcMotor.class, "LS");

//            //  THIS NEEDS TO BE BEFORE SETMODE!!!
//            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
            linearSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



            linearSlideMotor.setDirection(DcMotor.Direction.REVERSE);

            // reset encoder counts kept by motors.
            linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linearSlideMotor.setTargetPosition(10);


            linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);




            stateMachine = new StateMachine();

            //  STATES:
            //  0: Resting
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {

                    linearSlideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    linearSlideMotor.setPower(0.01);

                }

                @Override
                public void OnLoop() {

                }
            });
            // 1: Fine movement
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {
                    linearSlideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }

                @Override
                public void OnLoop() {
                        int axis = GetLinearSlideAxis();

                    if(axis == 1){
                        linearSlideMotor.setPower(0.75);
                    }
                    if(axis == -1){
                        linearSlideMotor.setPower(-0.75);
                    }
                    if(axis == 0){
                        stateMachine.SwitchState(0);
                    }
                }
            });
            //  2: Going to position
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {
                    linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }

                @Override
                public void OnLoop() {

                    int dpad = GetDPadInput();

                    int position = 0;

                    if(dpad != -1){

                        position = positions[dpad];

                    }

                    if(dpad != -1) {

                        GoToPosition(position);

                    }

                    //  Has reached the desired position (within 50 units)

                    if(Math.abs(desiredPosition - linearSlideMotor.getCurrentPosition()) < 15){

                        linearSlideMotor.setPower(0);
                        stateMachine.SwitchState(0);

                    }

                    telemetry.addData("LinearSlide", "Position: " + linearSlideMotor.getCurrentPosition() + ", target position: " + desiredPosition);

                }
            });

            stateMachine.SwitchState(0);

        }

        public void myStart(){

            linearSlideMotor.setPower(0);

        }

        public void myLoop(){

            if(GetLinearSlideAxis() != 0){

                stateMachine.SwitchState(1);

            } else if(GetDPadInput() != -1){

                stateMachine.SwitchState(2);

            }

            stateMachine.OnLoop();

            telemetry.addData("State", stateMachine.state);

        }

        private int GetLinearSlideAxis(){

            int output = 0;

            if(gamepad2.b){
                output++;
            }
            if(gamepad2.a) {
                output--;
            }

            return output;
        }

        private int GetDPadInput(){

            int output = -1;

            if(gamepad2.dpad_right){
                output = 3;
            }
            if(gamepad2.dpad_up) {
                output = 2;
            }
            if(gamepad2.dpad_left){
                output = 1;
            }
            if(gamepad2.dpad_down) {
                output = 0;
            }

            return output;

        }

        private int desiredPosition = 0;

        //  PACEHOLDER!!!!!!!!!!!
        private int[] positions = {10, 2000, 3000, 4000};

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

            claw = Motion.hm.get(Servo.class, "CM");

        }

        public void myStart(){

            claw.setPosition(openPos);

        }

        public void myLoop(){

            ClawMotor();

        }

        private double clawPosition = 0;

        private void ClawMotor(){

            double clawAxis = GetClawAxis();

            if(clawAxis == 1){
                clawPosition = openPos;
            } else if(clawAxis == -1){
                clawPosition = closePos;
            }

            clawPosition = Range.clip(clawPosition, openPos, closePos);

            //  Only change servo position if the button was pressed this frame
            if(clawAxis != 0) {
                claw.setPosition(clawPosition);
            }

            telemetry.addData("Claw", "Claw is: " + (clawAxis == 0 ? "Resting" : clawAxis > 0 ? "Opening" : "Closing") + " Position: " + clawPosition);

        }

        private int GetClawAxis(){

            int output = 0;
            if(gamepad2.y){
                output++;
            }
            if(gamepad2.x) {
                output--;
            }

            return -output;

        }
    }

    private class MyDrivetrain{

        private DcMotor frontLeft = null;
        private DcMotor frontRight = null;
        private DcMotor backLeft = null;
        private DcMotor backRight = null;

        private double strafePower = 0.3;
        private double forwardBackwardPower = 0.5;
        private double turnPower = 0.5;

        private  StateMachine stateMachine;
        private int currentState = -1;

        public void myInit(){

            backLeft = Motion.hm.get(DcMotor.class, "BL");
            backRight = Motion.hm.get(DcMotor.class, "BR");
            frontLeft = Motion.hm.get(DcMotor.class, "FL");
            frontRight = Motion.hm.get(DcMotor.class, "FR");

            backLeft.setDirection(DcMotor.Direction.FORWARD);
            backRight.setDirection(DcMotor.Direction.REVERSE);
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
            frontRight.setDirection(DcMotor.Direction.REVERSE);

            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

            stateMachine = new StateMachine();

            // STATES:
            // 0: Resting
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
            // 1: Forward backward
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {

                }

                @Override
                public void OnLoop() {

                    double moveAxis = gamepad1.left_stick_y;

                    frontLeft.setPower(moveAxis * forwardBackwardPower);
                    frontRight.setPower(moveAxis * forwardBackwardPower);
                    backLeft.setPower(moveAxis * forwardBackwardPower);
                    backRight.setPower(moveAxis * forwardBackwardPower);

                }
            });
            // 2: Turning
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {

                }

                @Override
                public void OnLoop() {

                    double turnAxis = -gamepad1.right_stick_x;

                    frontLeft.setPower(turnAxis * turnPower);
                    frontRight.setPower(-turnAxis * turnPower);
                    backLeft.setPower(turnAxis * turnPower);
                    backRight.setPower(-turnAxis * turnPower);

                }
            });
            // 3: Strafing
            stateMachine.AddState(new CallbackThing() {
                @Override
                public void OnInit() {

                }

                @Override
                public void OnLoop() {

                    double backLeftPower;
                    double backRightPower;
                    double frontLeftPower;
                    double frontRightPower;

                    if(GetStrafeAxis() == 1){
                        // Strafe right

                        frontLeftPower = -strafePower;
                        frontRightPower = strafePower;
                        backLeftPower = strafePower;
                        backRightPower = -strafePower;

                    } else{
                        // Strafe left

                        frontLeftPower = strafePower;
                        frontRightPower = -strafePower;
                        backLeftPower = -strafePower;
                        backRightPower = strafePower;

                    }

                    frontLeft.setPower(frontLeftPower);
                    frontRight.setPower(frontRightPower);
                    backLeft.setPower(backLeftPower);
                    backRight.setPower(backRightPower);

                }
            });

            stateMachine.SwitchState(0);

        }

        public void myStart(){

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

        }

        public void myLoop(){

            double strafeAxis = GetStrafeAxis();

            //  If we're getting forward-backward input
            if(gamepad1.left_stick_y != 0){
                //  Forward-backward movement

                if(currentState != 1) {
                    stateMachine.SwitchState(1);
                    currentState = 1;
                }

                telemetry.addData("Mode", "Mode: forward-backward");

            }else if(gamepad1.right_stick_x != 0){
                //  Turning/rotating movement

                if(currentState != 2) {
                    stateMachine.SwitchState(2);
                    currentState = 2;
                }

                telemetry.addData("Mode", "Mode: Turning");

            }else if(strafeAxis != 0){
                //  Strafing movement

                if(currentState != 3) {
                    stateMachine.SwitchState(3);
                    currentState = 3;
                }

                telemetry.addData("Mode", "Mode: Strafing");

            }else{
               if(currentState != 0) {
                   stateMachine.SwitchState(0);
                   currentState = 0;
               }

                telemetry.addData("Mode", "Mode: Stopped");

            }

            stateMachine.OnLoop();

            telemetry.addData("Position", "Back left: " + backRight.getCurrentPosition());
            telemetry.addData("State:", currentState);

        }

        private int GetStrafeAxis(){

            int output = 0;

            if(gamepad1.right_bumper){
                output++;
            }
            if(gamepad1.left_bumper){
                output--;
            }

            return output;

        }
    }
}