package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Systems.Vision;

public class VisionTest extends OpMode {
   Vision limelight;


    @Override
    public void init() {
        limelight = Vision.getInstance(hardwareMap);
        limelight.start();

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
    public void loop() {
        limelight.update();
        telemetry.addData("X","%.6f", limelight.getX());
        telemetry.addData("Y","%.6f", limelight.getY());
        telemetry.addData("Z","%.6f", limelight.getZ());
        telemetry.addLine();
        telemetry.addData("Distance3D","%.6f", limelight.getDistance());
        telemetry.addLine();
        telemetry.addData("TagID","%.6f", limelight.getID());
    }
}
