package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class flywheel {

    private final DcMotorEx flywheel;
    private final DcMotorEx intakeShoot;

    private final Servo gate1;

    private double targetVelocity = 0;
    public double kS = 0.07, kV = 0.00047169811321, kP = 0.002;
    public flywheel(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotorEx.class, "flywheel");
        intakeShoot = hwMap.get(DcMotorEx.class, "intake");
        gate1 = hwMap.get(Servo.class, "gate");
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeShoot.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    public void updateFeedforward()
    {
        flywheel.setPower((kV * targetVelocity) + (kP * (targetVelocity - flywheel.getVelocity())) + kS);
    }
    public void setVelocity(double target)
    {
        targetVelocity = target;
    }

    public void flywheelPower(double power) {
        flywheel.setPower(power);
    }

    public void shoot(double gateAngle,double intakePower) {
        intakeShoot.setPower(intakePower);
        gate1.setPosition(gateAngle);
    }

    public void updateTelemetryLaucncher(Telemetry telemetry)
    {
        telemetry.addData("Target velocity", targetVelocity);
        telemetry.addData("Actual Velocity", flywheel.getVelocity());
        telemetry.update();
    }

    public void angle(double ang) {
        gate1.setPosition(ang);
    }
}