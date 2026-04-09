package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Flywheel {

    private DcMotorEx motor;
    private final double encoderTPR = 28;
    private double gearRatio;
    private double kV, kS, kP;

    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.get(DcMotorEx.class, "");

        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setMotorPower(double power){
        motor.setPower(power);

    }

    public double getTicksPerSec(){
        return motor.getVelocity();
    }

    public double getRPM(){
        return ((getTicksPerSec()/encoderTPR) * 60)/gearRatio;
    }
}
