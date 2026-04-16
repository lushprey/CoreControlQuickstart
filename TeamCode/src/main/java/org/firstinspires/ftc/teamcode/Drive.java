package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Systems.Drivetrain;

public class Drive extends OpMode {
    Drivetrain drivetrain;

    boolean fieldRelative = false;
    @Override
    public void init() {
        drivetrain.init(hardwareMap);

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
        drivetrain.setIMU(imu);
    }

    @Override
    public void loop() {
        if(fieldRelative){
            drivetrain.driveFieldRelative(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        } else {
            drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
    }
}
