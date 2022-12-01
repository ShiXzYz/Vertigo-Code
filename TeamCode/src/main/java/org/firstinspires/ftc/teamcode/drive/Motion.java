package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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


    @Override
    public void init() {
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        linearSlideMotor = hardwareMap.get(DcMotor.class, "LS");

        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);

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
    }

    @Override
    public void loop() {

        WheelMotors();

        LinearSlideMotor();

        //telemetry.addData("Motors", "left (%.2f), right (%.2f)", backLeftPower, backRightPower);
        telemetry.update();

    }

    private void LinearSlideMotor(){

        double power = 0;

        if(gamepad1.b){
            power++;
        }
        if(gamepad1.a) {
            power--;
        }

        linearSlideMotor.setPower(power);

    }

    private void WheelMotors(){

        double backLeftPower;
        double backRightPower;
        double frontLeftPower;
        double frontRightPower;


        double x1 = gamepad1.left_stick_x;
        double y1 = -gamepad1.left_stick_y;
        double x2 = x1*Math.cos(-Math.PI/4);
        double y2 = y1*Math.sin(-Math.PI/4);

        //backLeftPower = Range.clip(drive + turn, -1.0, 1.0);
        //backRightPower = Range.clip(drive - turn, -1.0, 1.0);

        frontLeft.setPower(x2);
        frontRight.setPower(x2);
        backLeft.setPower(y2);
        backRight.setPower(y2);



        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Power", "x2: " + x2 + " y2: " + y2);

    }

    @Override
    public void stop() {
    }
}
