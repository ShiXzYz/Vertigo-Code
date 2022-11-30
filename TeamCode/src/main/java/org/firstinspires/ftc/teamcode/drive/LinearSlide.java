package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "Main", group = "Mecanum Drive")
public class LinearSlide extends OpMode{

   private DcMotor linearSlideMotor = null;

    @Override
   public void init(){

       linearSlideMotor = hardwareMap.get(DcMotor.class, "LS");

   }

    @Override
    public void loop() {

        double power = 0;

        if(gamepad1.y){
            power++;
        }
        if(gamepad1.a) {
            power--;
        }

       linearSlideMotor.setPower(power);

    }
}
