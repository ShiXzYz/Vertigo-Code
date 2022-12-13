package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Main", group = "Mecanum Drive")
public class Motion extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotorEx linearSlideMotor = null;

    private Servo claw = null;

    @Override
    public void init() {
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        linearSlideMotor = hardwareMap.get(DcMotorEx.class, "LS");

        claw = hardwareMap.get(Servo.class, "CM");

        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        linearSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        linearSlideMotor.setPower(0);

        claw.setPosition(0);
    }

    @Override
    public void loop() {

        WheelMotors();
        LinearSlideMotor();
        ClawMotor();

        telemetry.update();

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

    private int lastLSPosition = 0;

    private void LinearSlideMotor(){

        double axis = GetLinearSlideAxis();
        int position = 0;
       if(axis == 1){
           position = 3000;
       }
        if(axis == -1){
            position = 10;
        }

        linearSlideMotor.setTargetPosition(position);

        telemetry.addData("LinearSlide", "Linear slide is: " + (axis == 0 ? "Resting" : axis == 1 ? "Raising" : "Lowering"));
        telemetry.addData("LinearSlide", "Position: " + linearSlideMotor.getCurrentPosition());

    }

    private double clawPosition = 0;
    private double lastFrameClawAxis = 0;

    private void ClawMotor(){

        double clawAxis = GetClawAxis();

        clawPosition += clawAxis * 0.001;

        clawPosition = Range.clip(clawPosition, 0, 0.3);

        //  Only change servo position if the button was pressed this frame
        if(clawAxis != 0) {
            claw.setPosition(clawPosition);
        }

        telemetry.addData("Claw", "Claw is: " + (clawAxis == 0 ? "Resting" : clawAxis > 0 ? "Opening" : "Closing") + " Position: " + clawPosition);

        lastFrameClawAxis = clawAxis;

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

        //backLeftPower = Range.clip(drive + turn, -1.0, 1.0);
        //backRightPower = Range.clip(drive - turn, -1.0, 1.0);

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
        //  ??
        //double x2 = x1*Math.cos(-Math.PI/4);
        //double y2 = y1*Math.sin(-Math.PI/4);

        //backLeftPower = Range.clip(drive + turn, -1.0, 1.0);
        //backRightPower = Range.clip(drive - turn, -1.0, 1.0);

        frontLeft.setPower(moveAxis);
        frontRight.setPower(moveAxis);
        backLeft.setPower(moveAxis);
        backRight.setPower(moveAxis);

    }

    @Override
    public void stop() {
    }
}