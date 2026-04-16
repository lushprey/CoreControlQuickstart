package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Vision {
    private Limelight3A limelight;
    private double x;
    private double y;
    private double z;
    private double distance3D;
    private static Vision instance = null;

    private int TARGET_ID = 23;
    IMU imu;
    public Vision(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
    }

    public void setIMU(IMU imu){
        this.imu = imu;
    }
    public void start(){
        limelight.start();
    }

    public static Vision getInstance(HardwareMap hardwareMap) {
        if (instance == null) {
            instance = new Vision(hardwareMap);
        }
        return instance;
    }

    public void update(){
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
        Pose3D pose = getPose3D();
        if (pose != null){
           x = pose.getPosition().toUnit(DistanceUnit.MM).x;
           y = pose.getPosition().toUnit(DistanceUnit.MM).y;
           z = pose.getPosition().toUnit(DistanceUnit.MM).z;
           distance3D = Math.sqrt(x*x + y*y + z*z); //Millimeters
        }
    }

    public double getX(){
        return  x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public double getDistance(){
        return distance3D; //Millimeters
    }

    public void setID(int value){
        TARGET_ID = value;
    }
    public int getID(){
        return TARGET_ID;
    }

    private Pose3D getPose3D(){
        LLResult llResult = limelight.getLatestResult();
        if(llResult != null && llResult.isValid()) {
            for (LLResultTypes.FiducialResult tag : llResult.getFiducialResults()) {
                if (tag.getFiducialId() == TARGET_ID) {
                    return tag.getRobotPoseTargetSpace();
                }
            }
        }
        return null;
    }




}
