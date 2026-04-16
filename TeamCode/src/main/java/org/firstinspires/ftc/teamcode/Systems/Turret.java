package org.firstinspires.ftc.teamcode.Systems;

import static com.qualcomm.robotcore.util.Range.clip;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Turret {
    private DcMotorEx turret;
    private final double encoderTPR = 28; //Configurar
    private final double gearRatio = 1; //Configurar

    private double kP = 0.0;
    private double kD = 0.0;

    private final ElapsedTime timer = new ElapsedTime();

    private static Turret instance = null;

    private final double MAXPOWER = 0.6;


    public Turret(HardwareMap hardwareMap){
        turret = hardwareMap.get(DcMotorEx.class, "turret");
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        timer.reset();
    }
    public static Turret getInstance(HardwareMap hardwareMap) {
        if (instance == null) {
            instance = new Turret(hardwareMap);
        }
        return instance;
    }

    public void setMotorPower(double power){
        turret.setPower(power);
    }

    public double getPosition(){
        return (turret.getCurrentPosition() * 360 / (encoderTPR * gearRatio));
    }

    public void resetTimer(){
        timer.reset();
    }

    public void setkP(double value){
        kP = value;
    }

    public void setkD(double value){
        kD = value;
    }

    public double getkP(){
        return kP;
    }

    public double getkD(){
        return kD;
    }


    private double D(double error, double lastError){
        double dt = timer.seconds();
        resetTimer();
        double dTerm = 0;

        if (dt > 0) {
            dTerm = ((error - lastError) / dt);
        }
        return dTerm;
    }

    public void PD(double error, double lastError){
        double power = error * kP + D(error, lastError) * kD;
        power = clip(power, -MAXPOWER, MAXPOWER);
        setMotorPower(power);
    }

}
