package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;



import org.firstinspires.ftc.teamcode.Teleop.DriveCode;
import org.firstinspires.ftc.teamcode.Teleop.intake;
import org.firstinspires.ftc.teamcode.Teleop.flywheel;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class MainOpmode extends OpMode {

    private DriveCode drive;

    private intake intake;
    private flywheel shooter;
    private Follower follower;
    double forward, strafe, rotate ;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose BlueRight = poseFactory.of(83, 114, 270);

    private boolean isAutoDriving = false;

    @Override
    public void init() {
        drive = new DriveCode(hardwareMap);
        intake = new intake(hardwareMap);
        shooter = new flywheel(hardwareMap);
        follower = Constants.create(hardwareMap);
    }



    @Override
    public void loop() {
        // intilzes the triggers and because they are floats
        double triggerValue = gamepad2.right_trigger;
        double triggerValue2 = gamepad2.left_trigger;

        // Drive code that uses the DriveCode subclass
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
            shooter.flywheelPower(0.70);
        } else if (gamepad2.b) {
            shooter.flywheelPower(-0.5);
            shooter.angle(1);
        } else {
            shooter.flywheelPower(0);
        }

        if (gamepad2.dpad_up) {
            shooter.shoot(1,0.3);

        } else {
            shooter.shoot(0.94,0);
        }


        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        // Math.toDegrees() is a built-in java method
    }
}

