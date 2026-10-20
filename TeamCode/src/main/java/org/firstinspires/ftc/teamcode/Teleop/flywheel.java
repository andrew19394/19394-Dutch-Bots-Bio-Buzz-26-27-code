package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class flywheel {

    private final DcMotor flywheel;

    public flywheel(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotor.class, "flywheel");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void flywheelPower(double power) {
        flywheel.setPower(power);
    }
}