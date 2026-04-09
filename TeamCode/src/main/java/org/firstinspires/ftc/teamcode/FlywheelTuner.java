package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class FlywheelTuner extends OpMode {

    Flywheel flywheel = new Flywheel();

    public double[] k = {0, 0, 0};

    int S = 2;
    int V = 1;
    int P = 0;

    double[] increments = {0.000001, 0.00001, 0.0001, 0.001, 0.01};
    int incrementIndex = 4;
    int kIndex = S;
    String kMode;

    double targetRPM = 0;
    double idealRPM; //Por definir
    double stepRPM = 100;
    double currentRPM;
    double error;


    @Override
    public void init() {
        flywheel.init(hardwareMap);
    }

    @Override
    public void loop() {
        if (gamepad1.leftStickButtonWasPressed()){
            kIndex++;
            if (kIndex > 2) {kIndex = 0;}
        }

        if (gamepad1.dpadRightWasPressed() && incrementIndex < 4) {
            incrementIndex++;
        } else if (gamepad1.dpadLeftWasPressed() && incrementIndex > 0){
            incrementIndex--;
        }
        double step = increments[incrementIndex];

        if (gamepad1.dpadUpWasPressed()) {k[kIndex] += step;}
        if (gamepad1.dpadDownWasPressed()) {k[kIndex] -= step;}

        if (gamepad1.a) { targetRPM = idealRPM; }
        if (gamepad1.b) { targetRPM = 0;}
        if (gamepad1.leftTriggerWasPressed()) { targetRPM -= stepRPM; }
        if (gamepad1.rightTriggerWasPressed()) { targetRPM += stepRPM; }

        currentRPM = flywheel.getRPM();
        error = targetRPM - currentRPM;

        flywheel.setMotorPower(k[S] + (k[V] * targetRPM) + (k[P] * error));


        if (kIndex == S){kMode = "S";}
        else if (kIndex == V){kMode = "V";}
        else if (kIndex == P){kMode = "P";}

        telemetry.addData("Step", "%.6f", step);
        telemetry.addLine();
        telemetry.addData("Current Mode", kMode);
        telemetry.addData("kS","%.6f", k[S]);
        telemetry.addData("kV","%.6f", k[V]);
        telemetry.addData("kP","%.6f", k[P]);
        telemetry.addLine();
        telemetry.addData("RPM","%.6f", currentRPM);
        telemetry.addData("Target RPM","%.6f", targetRPM);
        telemetry.addData("Error","%.6f", error);

    }
}
