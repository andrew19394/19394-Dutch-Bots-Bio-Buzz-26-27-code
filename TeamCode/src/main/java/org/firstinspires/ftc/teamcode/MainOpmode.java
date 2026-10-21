package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Teleop.DriveCode;
import org.firstinspires.ftc.teamcode.Teleop.intake;
import org.firstinspires.ftc.teamcode.Teleop.flywheel;

@TeleOp
public class MainOpmode extends OpMode {

    private DriveCode drive;

    private intake intake;
    private flywheel shooter;
    private
    double forward, strafe, rotate ;

    @Override
    public void init() {
        drive = new DriveCode(hardwareMap);
        intake = new intake(hardwareMap);
        shooter = new flywheel(hardwareMap);
    }



    @Override
    public void loop() {
        // intilzes the triggers and because they are floats
        double triggerValue = gamepad2.right_trigger;
        double triggerValue2 = gamepad2.left_trigger;

        // Drive code that uses the RobotDrive subclass
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;
        drive.drive(forward,strafe,rotate);

        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            intake.intakePower(1);
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            intake.intakePower(-0.5);
        } else {
            intake.intakePower(0);
        }

        if (gamepad2.x) {
            shooter.flywheelPower(0.7);
        } else if (gamepad2.b) {
            shooter.flywheelPower(-0.2);
        } else {
            shooter.flywheelPower(0);
        }
    }
}

