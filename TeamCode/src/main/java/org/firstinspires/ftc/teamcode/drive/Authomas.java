package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;




//  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
//  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

//  The back left wheel encoder is not returning any readings.



@TeleOp(name = "Authomas", group = "Mecanum Drive")
public class Authomas extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotorEx linearSlideMotor = null;
    private Servo claw = null;
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    private StateMachine stateMachine;


    //  TWEAKERS

    private double forwardBackwardSpeed = 0.3, strafeSpeed = 0.3;


    @Override
    public void init() {

        //  LINEAR SLIDE

        linearSlideMotor = hardwareMap.get(DcMotorEx.class, "LS");

        linearSlideMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        linearSlideMotor.setDirection(DcMotorEx.Direction.REVERSE);

        // reset encoder counts kept by motors.
        linearSlideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        linearSlideMotor.setTargetPosition(10);

        linearSlideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        //  CLAW

        claw = hardwareMap.get(Servo.class, "CM");

        //  DRIVE TRAIN

        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);

        backLeft.setTargetPosition(5);
        backLeft.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        backLeft.setPower(0);

        //  STATE MACHINE

        stateMachine = new StateMachine();

        // STATES
        //  0: close claw
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {
                //  CLOSE CLAW
                claw.setPosition(0.6);
            }

            @Override
            public void OnLoop() {
                stateMachine.SwitchState(stateMachine.state + 1);
            }
        });
        //  1: linear slide up
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                linearSlideMotor.setPower(0.75);

            }

            @Override
            public void OnLoop() {

                int targetHeight = 1000;

                linearSlideMotor.setTargetPosition(targetHeight);

                if(linearSlideMotor.getCurrentPosition() >= targetHeight){

                    linearSlideMotor.setPower(0.01);

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

            }
        });
        //  2: go forward
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                frontLeft.setPower(forwardBackwardSpeed);
                frontRight.setPower(forwardBackwardSpeed);
                backLeft.setPower(forwardBackwardSpeed);
                backRight.setPower(forwardBackwardSpeed);

            }

            @Override
            public void OnLoop() {

                int targetPos = 500;

                if(backLeft.getCurrentPosition() >= targetPos){

                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(0);

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

                telemetry.addData("Drivetrain pos", backLeft.getCurrentPosition());

            }
        });
        //  3: open claw
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {
                //  CLOSE CLAW
                claw.setPosition(0.4);
                stateMachine.SwitchState(stateMachine.state + 1);
            }

            @Override
            public void OnLoop() {

            }
        });
        //  4: linear slide down
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                backLeft.setPower(0.75);

            }

            @Override
            public void OnLoop() {

                int targetHeight = 900;

                if(linearSlideMotor.getCurrentPosition() <= targetHeight){

                    linearSlideMotor.setPower(0.01);

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

            }
        });
        //  5: open claw
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {
                //  CLOSE CLAW
                claw.setPosition(0.4);
                stateMachine.SwitchState(stateMachine.state + 1);
            }

            @Override
            public void OnLoop() {

            }
        });
        //  6: finsihed
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

            }

            @Override
            public void OnLoop() {

            }
        });

        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();

        stateMachine.SwitchState(0);

    }

    @Override
    public void loop() {

        stateMachine.OnLoop();

        telemetry.addData("State", stateMachine.state);

        telemetry.update();

    }



    @Override
    public void stop() {
    }
}