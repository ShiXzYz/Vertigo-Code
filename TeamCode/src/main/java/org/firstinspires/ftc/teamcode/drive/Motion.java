package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Main", group = "Mecanum Drive")
public class Motion extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private  WheelMotion wheelClass = null;
    private  LinearSlide linearSlideClass = null;
    private  ClawMotion clawClass = null;

    @Override
    public void init() {

        wheelClass = new WheelMotion();
        linearSlideClass = new LinearSlide();
        clawClass = new ClawMotion();

        wheelClass.myInit();
        linearSlideClass.myInit();
        clawClass.myInit();

        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();

        wheelClass.myStart();
        linearSlideClass.myStart();
        clawClass.myStart();

    }

    @Override
    public void loop() {

        wheelClass.myLoop();
        linearSlideClass.myLoop();
        clawClass.myLoop();

        telemetry.update();

    }

    @Override
    public void stop() {
    }
}