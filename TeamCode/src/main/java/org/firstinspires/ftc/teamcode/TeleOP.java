package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Looper;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Nigga", group="Linear Opmode")

public class TeleOP extends LinearOpMode {

    DcMotor driveRight = null;
    DcMotor driveLeft = null;
    DcMotor intake = null;
    DcMotor pivot = null;
    private double driveVelocityCap = 1;
    private String state = "none";
    private boolean intaking=false;

    private int tpos = 0;

    @Override
    public void runOpMode() {


        //Inicialización de motores
        driveLeft = hardwareMap.get(DcMotor.class, "leftDrive");
        driveRight = hardwareMap.get(DcMotor.class, "rightDrive");
        intake = hardwareMap.get(DcMotor.class, "intake");


        driveLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        driveRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        driveRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pivot.setTargetPosition(0);
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setPower(0.8);

        // Invertimos un lado para que todos avancen hacia adelante
        driveLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        driveRight.setDirection(DcMotorSimple.Direction.REVERSE);


        // Espera a que empiece el match
        waitForStart();



        // Loop principal de teleop
        while (opModeIsActive()) {


            double fwd = gamepad2.left_stick_y;
            double turn = gamepad2.right_stick_x;


            double denominator = Math.max(Math.abs(fwd) + Math.abs(turn) , 1);
            double leftPwr = ((fwd + turn) / denominator) * driveVelocityCap;
            double rightPwr = ((fwd - turn) / denominator) * driveVelocityCap;

            // Normalizamos si algún valor > 1


            driveRight.setPower(rightPwr);
            driveLeft.setPower(leftPwr);

            //triggers
            if (gamepad2.right_trigger > 0.5) {
                intake.setPower(-0.9);
                intaking = true;

            } else if (gamepad2.left_trigger > 0.5) {
                intake.setPower(0.5);

            } else {
                intake.setPower(0);
            }

            if (intaking & gamepad2.b){
                pivot.setTargetPosition(1800);
                state = "outtaking";
            }
            else if (gamepad2.b & !intaking){
                pivot.setTargetPosition(0);
                state="intaking";
            }
            else if (intaking) {
                pivot.setTargetPosition(0);
                state="intaking";
            }

// SOY GAY = True
// atte.Hachita

                    //Telemetría para ver valores en Driver Station
            telemetry.addData("Right PWR", rightPwr);
            telemetry.addData("Left PWR", leftPwr);
            telemetry.addData("Intake", intake.getPower());
            telemetry.addData("pivot pos", pivot.getCurrentPosition());
            telemetry.addData("pivot target pos", pivot.getTargetPosition());
            telemetry.addData("pivot state", state);

            telemetry.update();

                }
            }
        }

