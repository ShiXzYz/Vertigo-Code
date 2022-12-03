package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
    private DcMotor linearSlideMotor = null;

    private Servo claw = null;


    @Override
    public void init() {
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        linearSlideMotor = hardwareMap.get(DcMotor.class, "LS");

        claw = hardwareMap.get(Servo.class, "CM");

        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        linearSlideMotor.setDirection(DcMotor.Direction.FORWARD);

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

    private void LinearSlideMotor(){

        double power = 0;

        if(gamepad2.b){
            power++;
        }
        if(gamepad2.a) {
            power--;
        }

        linearSlideMotor.setPower(power);

        telemetry.addData("LinearSlide", "Linear slide is: " + (power == 0 ? "Resting" : power == 1 ? "Raising" : "Lowering"));

    }

    private double clawPosition = 0;

    private void ClawMotor(){

        double clawAxis = 0;

        if(gamepad2.y){
            clawAxis++;
        }
        if(gamepad2.x) {
            clawAxis--;
        }

        clawPosition += clawAxis;

        claw.setPosition(clawPosition);

        telemetry.addData("Claw", "Claw is: " + (clawAxis == 0 ? "Resting" : clawAxis == 1 ? "Opening" : "Closing"));

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

        telemetry.addData("Status", "Run Time: " + runtime.toString());

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

        double turnAxis = gamepad1.right_stick_x;

        frontLeft.setPower(turnAxis);
        frontRight.setPower(-turnAxis);
        backLeft.setPower(turnAxis);
        backRight.setPower(-turnAxis);

        telemetry.addData("Power", "turn axis: " + turnAxis);

    }

    private void ForwardBackward(){

        double moveAxis = -gamepad1.left_stick_y;
        //  ??
        //double x2 = x1*Math.cos(-Math.PI/4);
        //double y2 = y1*Math.sin(-Math.PI/4);

        //backLeftPower = Range.clip(drive + turn, -1.0, 1.0);
        //backRightPower = Range.clip(drive - turn, -1.0, 1.0);

        frontLeft.setPower(moveAxis);
        frontRight.setPower(moveAxis);
        backLeft.setPower(moveAxis);
        backRight.setPower(moveAxis);

        telemetry.addData("Power", "move axis: " + moveAxis);

    }

    @Override
    public void stop() {
    }
}