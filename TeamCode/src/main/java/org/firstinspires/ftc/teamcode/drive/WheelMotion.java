package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.DcMotor;

public class WheelMotion extends Motion{

    private final double driveSpeed = 0.7;
    private final double turnSpeed = 0.7;

    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    public void myInit(){

        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");

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

    public  void myLoop(){

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

            frontLeftPower = -turnSpeed;
            frontRightPower = turnSpeed;
            backLeftPower = turnSpeed;
            backRightPower = -turnSpeed;

        } else{
            // Strafe left

            frontLeftPower = turnSpeed;
            frontRightPower = -turnSpeed;
            backLeftPower = -turnSpeed;
            backRightPower = turnSpeed;

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

        frontLeft.setPower(turnAxis * driveSpeed);
        frontRight.setPower(-turnAxis * driveSpeed);
        backLeft.setPower(turnAxis * driveSpeed);
        backRight.setPower(-turnAxis * driveSpeed);

    }

    private void ForwardBackward(){

        double moveAxis = gamepad1.left_stick_y;
        //  ??
        //double x2 = x1*Math.cos(-Math.PI/4);
        //double y2 = y1*Math.sin(-Math.PI/4);

        //backLeftPower = Range.clip(drive + turn, -1.0, 1.0);
        //backRightPower = Range.clip(drive - turn, -1.0, 1.0);

        frontLeft.setPower(moveAxis * driveSpeed);
        frontRight.setPower(moveAxis * driveSpeed);
        backLeft.setPower(moveAxis * driveSpeed);
        backRight.setPower(moveAxis * driveSpeed);

    }
}
