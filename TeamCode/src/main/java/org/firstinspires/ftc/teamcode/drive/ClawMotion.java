package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class ClawMotion extends Motion{

    private Servo claw = null;

    public void myInit(){

        claw = hardwareMap.get(Servo.class, "CM");

    }

    public void myStart(){

        claw.setPosition(0);

    }

    public  void myLoop() {

        ClawMotor();

    }

    private double clawPosition = 0;
    private void ClawMotor(){

        double clawAxis = GetClawAxis();

        clawPosition += clawAxis * 0.001;

        clawPosition = Range.clip(clawPosition, 0, 1);

        //  Only change servo position if the button was pressed this frame
        if(clawAxis != 0) {
            claw.setPosition(clawPosition);
        }

        telemetry.addData("Claw", "Claw is: " + (clawAxis == 0 ? "Resting" : clawAxis == 1 ? "Opening" : "Closing") + " Position: " + clawPosition);

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
