package org.firstinspires.ftc.teamcode.Systems;

import static com.qualcomm.robotcore.util.Range.clip;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Flywheel {

    private DcMotorEx flywheel;
    private final double encoderTPR = 28;//Configurar
    private final double gearRatio = 0;//Configurar
    private double kV, kS, kP;
    private static Flywheel instance = null;

    private final double MAXPOWER = 0.9;

    Flywheel(HardwareMap hardwareMap){
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    public static Flywheel getInstance(HardwareMap hardwareMap) {
        if (instance == null) {
            instance = new Flywheel(hardwareMap);
        }
        return instance;
    }

    public void setMotorPower(double power){
        flywheel.setPower(power);
    }

    public double getRPM(){
        return ((flywheel.getVelocity()/encoderTPR) * 60)/gearRatio;
    }

    public void setkV(double value){
        kV = value;
    }

    public void setkS(double value){
        kS = value;
    }

    public void setkP(double value){
        kP = value;
    }

    public double getkV(){
        return kV;
    }

    public double getkS(){
        return kS;
    }

    public double getkP(){
        return kP;
    }

    public void PVS(double target, double current){
        double power = ((target - current) * kP + (target * kV) + kS);
        power = clip(power, -MAXPOWER, MAXPOWER);
        setMotorPower(power);
    }


}
