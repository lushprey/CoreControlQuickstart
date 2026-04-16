package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Systems.Turret;
import org.firstinspires.ftc.teamcode.Systems.Vision;

public class TurretTuner extends OpMode {

    Turret turret;
    Vision limelight;

    public double[] k = {0, 0};

    int D = 1;
    int P = 0;

    double[] increments = {0.000001, 0.00001, 0.0001, 0.001, 0.01};
    int incrementIndex = 4;
    int kIndex = P;
    String kMode; //Para telemetria

    double lastError;
    double error;

    double targetX = 0;
    double currentX;

    double tolerance = 10; //Configurar (Millimetros)

    final int ID = 20; //Configurar

    @Override
    public void init() {
        turret = Turret.getInstance(hardwareMap);
        limelight = Vision.getInstance(hardwareMap);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Esto depende de la configuración de tu robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        //Hacia que lado ve el logo

        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        //Hacia que lado ve el USB

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        limelight.setIMU(imu);
    }

    @Override
    public void start() {
        limelight.start();
        limelight.setID(ID);
    }

    @Override
    public void loop() {
        limelight.update();
        if (gamepad1.leftStickButtonWasPressed()){
            kIndex++;
            if (kIndex > 1) {kIndex = 0;}
        }

        if (gamepad1.dpadRightWasPressed() && incrementIndex < 4) {
            incrementIndex++;
        } else if (gamepad1.dpadLeftWasPressed() && incrementIndex > 0){
            incrementIndex--;
        }
        double step = increments[incrementIndex];

        if (gamepad1.dpadUpWasPressed()) {k[kIndex] += step;}
        if (gamepad1.dpadDownWasPressed()) {k[kIndex] -= step;}

        turret.setkP(k[P]);
        turret.setkD(k[D]);

        lastError = error;
        currentX = limelight.getX();
        error = targetX - currentX;

        if (error > tolerance){
            turret.PD(error, lastError);
        } else {
            turret.setMotorPower(0);
        }

        telemetry.addData("Step", "%.6f", step);
        telemetry.addLine();
        telemetry.addData("Current Mode", kMode);
        telemetry.addData("kP","%.6f", turret.getkP());
        telemetry.addData("kD","%.6f",turret.getkD());
        telemetry.addLine();
        telemetry.addData("Current X","%.6f", currentX);
        telemetry.addData("Target X","%.6f", targetX);
        telemetry.addData("Error","%.6f", error);
        telemetry.addLine();
        telemetry.addData("Position","%.6f", turret.getPosition());

    }
}
