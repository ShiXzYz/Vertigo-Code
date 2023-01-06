package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.ArrayList;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

@TeleOp(name = "Main", group = "Mecanum Drive")
public class Motion extends OpMode {
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



            //  STATES:
            //  0: Resting
            //  1: Going to position
//            stateMachine.AddState(new CallbackThing() {
//                @Override
//                public void OnInit() {
//                  linearSlideMotor.setPower(0);
//                }
//
//                @Override
//                public void OnLoop() {
//
//                }
//            });

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

        }

        public void myStart(){

            linearSlideMotor.setPower(0);

        }

        public void myLoop(){

            LinearSlideMotor();

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
        private int[] positions = {10, 1000, 2000, 3000};

        private void LinearSlideMotor(){


            int axis = GetLinearSlideAxis();
            int dpad = GetDPadInput();

            int position = 0;
            if(axis == 1){
                position = desiredPosition + 10;
            }
            if(axis == -1){
                position = desiredPosition - 10;
            }

            if(dpad != -1){

                position = positions[dpad];

            }

            if(axis != 0 || dpad != -1) {

                GoToPosition(position);

            }

            //  Has reached the desired position (within 50 units)

            if(Math.abs(desiredPosition - linearSlideMotor.getCurrentPosition()) < 25){

                linearSlideMotor.setPower(0);

            }

            telemetry.addData("LinearSlide", "Linear slide input: " + (axis == 0 ? "Nothing" : axis == 1 ? "Up" : "Down"));
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

            claw = Motion.hm.get(Servo.class, "CM");

        }

        public void myStart(){

            claw.setPosition(0);

        }

        public void myLoop(){

            ClawMotor();

        }

        private double clawPosition = 0;

        private void ClawMotor(){

            double clawAxis = GetClawAxis();

            clawPosition += clawAxis * 0.001;

            clawPosition = Range.clip(clawPosition, 0, 0.3);

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

        public void myInit(){

            backLeft = Motion.hm.get(DcMotor.class, "BL");
            backRight = Motion.hm.get(DcMotor.class, "BR");
            frontLeft = Motion.hm.get(DcMotor.class, "FL");
            frontRight = Motion.hm.get(DcMotor.class, "FR");

            backLeft.setDirection(DcMotor.Direction.FORWARD);
            backRight.setDirection(DcMotor.Direction.REVERSE);
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
            frontRight.setDirection(DcMotor.Direction.REVERSE);

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

            double strafeAxis = GetStrafeAxis();

            //  If we're getting forward-backward input
            if(gamepad1.left_stick_y != 0){
                //  Forward-backward movement

                ForwardBackward();

                telemetry.addData("Mode", "Mode: forward-backward");

            }else if(gamepad1.right_stick_x != 0){
                //  Turning/rotating movement

                Turning();

                telemetry.addData("Mode", "Mode: Turning");

            }if(strafeAxis != 0){
                //  Strafing movement

                Strafe(strafeAxis);

                telemetry.addData("Mode", "Mode: Strafing");

            }else{

                StopMovement();

                telemetry.addData("Mode", "Mode: Stopped");

            }
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

        private void Strafe(double input){


            double backLeftPower;
            double backRightPower;
            double frontLeftPower;
            double frontRightPower;

            if(input == 1){
                // Strafe right

                frontLeftPower = -1;
                frontRightPower = 1;
                backLeftPower = 1;
                backRightPower = -1;

            } else{
                // Strafe left

                frontLeftPower = 1;
                frontRightPower = -1;
                backLeftPower = -1;
                backRightPower = 1;

            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

        }

        private void StopMovement(){

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

        }

        private void Turning(){

            double turnAxis = -gamepad1.right_stick_x;

            frontLeft.setPower(turnAxis);
            frontRight.setPower(-turnAxis);
            backLeft.setPower(turnAxis);
            backRight.setPower(-turnAxis);

        }

        private void ForwardBackward(){

            double moveAxis = gamepad1.left_stick_y;

            frontLeft.setPower(moveAxis);
            frontRight.setPower(moveAxis);
            backLeft.setPower(moveAxis);
            backRight.setPower(moveAxis);

        }
    }

    //  State machine

//    private class StateMachine{
//
//       private ArrayList<StateObj> states =  new ArrayList<StateObj>();
//
//        private StateObj state = null;
//
//        public  StateMachine(){
//
//        }
//
//        public void OnInitialize(){
//
//            state.OnInitialize();
//
//        }
//
//        public void OnLoop(){
//
//            state.OnLoop();
//
//        }
//
//        public void AddState(CallbackThing _stateCallbacks){
//
//            states.add(new StateObj(_stateCallbacks));
//
//        }
//
//        public void SwitchState(int _state){
//            try {
//                state = states.get(_state);
//            }catch (Exception e){
//                telemetry.addData("Fuck Me:", "FUCKFUCKFUCKFUCKFUCK");
//            }
//        }
//    }
//
//    public interface CallbackThing{
//        void OnInit();
//        void OnLoop();
//    }
//
//    private class StateObj{
//
//        public CallbackThing functions = null;
//
//       public StateObj(CallbackThing _callbacks){
//
//           functions = _callbacks;
//
//       }
//
//       public void OnInitialize(){
//          functions.OnInit();
//       }
//        public void OnLoop(){
//            functions.OnLoop();
//        }
//    }
}