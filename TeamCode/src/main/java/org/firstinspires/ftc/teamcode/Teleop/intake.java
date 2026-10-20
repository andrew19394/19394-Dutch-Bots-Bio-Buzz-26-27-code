package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake {

    private final DcMotor front_intake;

    public intake(HardwareMap hwMap) {
        front_intake = hwMap.get(DcMotor.class, "intake");
        front_intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intakePower(double intake1) {
        front_intake.setPower(intake1);
    }
}
