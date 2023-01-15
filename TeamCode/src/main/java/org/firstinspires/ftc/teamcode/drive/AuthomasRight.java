package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "AuthomasRight", group = "Mecanum Drive")
public class AuthomasRight extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotorEx linearSlideMotor = null;
    private Servo claw = null;
    private DcMotorEx frontLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx backRight = null;

    private StateMachine stateMachine;


    //  TWEAKERS

    private double forwardBackwardSpeed = 0.2, strafeSpeed = 0.3;


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

        backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        backRight = hardwareMap.get(DcMotorEx.class, "BR");
        frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);

        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        //  STATE MACHINE

        stateMachine = new StateMachine();

        // STATES
        //  0: close claw
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {
                //  CLOSE CLAW
                claw.setPosition(Motion.closePos);
            }

            @Override
            public void OnLoop() {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
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

                int targetHeight = 1600;

                linearSlideMotor.setTargetPosition(targetHeight);

                if(Math.abs(linearSlideMotor.getCurrentPosition()) >= targetHeight){

                    linearSlideMotor.setPower(0.01);

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

            }
        });
        //  2: strafe right
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

                frontLeft.setPower(forwardBackwardSpeed);
                frontRight.setPower(-forwardBackwardSpeed);
                backLeft.setPower(-forwardBackwardSpeed);
                backRight.setPower(forwardBackwardSpeed);

            }

            @Override
            public void OnLoop() {

                int targetPos = 600;

                if(Math.abs(backRight.getCurrentPosition()) >= targetPos) {

                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(0);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

                telemetry.addData("Drivetrain pos", backRight.getCurrentPosition());

            }
        });
        //  3: forward
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

                frontLeft.setPower(forwardBackwardSpeed);
                frontRight.setPower(forwardBackwardSpeed);
                backLeft.setPower(forwardBackwardSpeed);
                backRight.setPower(forwardBackwardSpeed);

            }

            @Override
            public void OnLoop() {

                int targetPos = 50;

                if(Math.abs(backRight.getCurrentPosition()) >= targetPos) {

                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(0);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

                telemetry.addData("Drivetrain pos", backRight.getCurrentPosition());

            }
        });
        //  4: open claw
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {
                //  CLOSE CLAW
                claw.setPosition(Motion.openPos);
            }

            @Override
            public void OnLoop() {

                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }

                stateMachine.SwitchState(stateMachine.state + 1);
            }
        });
        //  5: backward
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

                frontLeft.setPower(-forwardBackwardSpeed);
                frontRight.setPower(-forwardBackwardSpeed);
                backLeft.setPower(-forwardBackwardSpeed);
                backRight.setPower(-forwardBackwardSpeed);

            }

            @Override
            public void OnLoop() {

                int targetPos = 50;

                if(Math.abs(backRight.getCurrentPosition()) >= targetPos) {

                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(0);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

                telemetry.addData("Drivetrain pos", backRight.getCurrentPosition());

            }
        });
        //  6: strafe right
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

                frontLeft.setPower(forwardBackwardSpeed);
                frontRight.setPower(-forwardBackwardSpeed);
                backLeft.setPower(-forwardBackwardSpeed);
                backRight.setPower(forwardBackwardSpeed);

            }

            @Override
            public void OnLoop() {

                int targetPos = 450;

                if(Math.abs(backRight.getCurrentPosition()) >= targetPos) {

                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(0);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

                telemetry.addData("Drivetrain pos", backRight.getCurrentPosition());

            }
        });
        //  7: linear slide down
        stateMachine.AddState(new CallbackThing() {
            @Override
            public void OnInit() {

                linearSlideMotor.setPower(0.75);

            }

            @Override
            public void OnLoop() {

                int targetHeight = 10;

                linearSlideMotor.setTargetPosition(targetHeight);

                if(linearSlideMotor.getCurrentPosition() <= targetHeight){

                    linearSlideMotor.setPower(0);

                    stateMachine.SwitchState(stateMachine.state + 1);

                }

            }
        });
        //  8: finished
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