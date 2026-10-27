package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class flywheel {

    private final DcMotor flywheel;
    private final DcMotor intakeShoot;

    private final Servo gate1;

    public flywheel(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotor.class, "flywheel");
        intakeShoot = hwMap.get(DcMotor.class, "intake");
        gate1 = hwMap.get(Servo.class, "gate");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeShoot.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void flywheelPower(double power) {
        flywheel.setPower(power);
    }

    public void shoot(double gateAngle,double intakePower) {
        intakeShoot.setPower(intakePower);
        gate1.setPosition(gateAngle);
    }

    public void angle(double ang) {
        gate1.setPosition(ang);
    }
}