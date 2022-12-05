package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearSlide extends Motion{

    private DcMotor linearSlideMotor = null;

    public void myInit(){

        linearSlideMotor = hardwareMap.get(DcMotor.class, "LS");

        linearSlideMotor.setDirection(DcMotor.Direction.FORWARD);

    }

    public void myStart(){

        linearSlideMotor.setPower(0);

    }

    public  void myLoop(){

        LinearSlideMotor();

    }

    private int GetLinearSlideAxis(){

        int output = 0;

        if(gamepad2.b){
            output--;
        }
        if(gamepad2.a) {
            output++;
        }

        return output;
    }

    private void LinearSlideMotor(){

        double power = GetLinearSlideAxis();

        //  Slow linear slide
        power *= 0.4;

        //  SLOW FALL, not tested
        if(gamepad2.left_bumper){
            power = -0.0001;
        }
        //  Stall linear slide
        if(gamepad2.right_bumper){
            power = -0.1;
        }

        linearSlideMotor.setPower(power);

        telemetry.addData("LinearSlide", "Linear slide is: " + (power == 0 ? "Resting" : power > 1 ? "Raising" : "Lowering"));

    }
}
