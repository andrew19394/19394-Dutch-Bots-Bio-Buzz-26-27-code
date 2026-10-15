package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.Teleop.DriveCode;


@TeleOp
public class MainOpmode extends OpMode {

    private DriveCode drive;
    double forward, strafe, rotate ;

    @Override
    public void init() {
        drive = new DriveCode(hardwareMap);
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


    }
}

