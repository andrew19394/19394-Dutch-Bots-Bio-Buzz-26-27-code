package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
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
    private Command followCmd;

    @Override
    public void init() {
        drive = new DriveCode(hardwareMap);
        intake = new intake(hardwareMap);
        shooter = new flywheel(hardwareMap);
        follower = Constants.create(hardwareMap);
    }



    @Override
    public void loop() {

        follower.update();
        Pose robotPose = follower.pose();

        // intilzes the triggers and because they are floats
        double triggerValue = gamepad2.right_trigger;
        double triggerValue2 = gamepad2.left_trigger;

        // Drive code that uses the DriveCode subclass
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        // Driver Override: Cancel pathing if joysticks are moved
        if (isAutoDriving && (Math.abs(forward) > 0.1 || Math.abs(strafe) > 0.1 || Math.abs(rotate) > 0.1)) {
            isAutoDriving = false;
            if (followCmd != null) {
                Scheduler.cancel(followCmd);
            }
            follower.stop();
        }

        // Trigger Auto Drive to BlueRight Pose using Button A
        if (gamepad1.a && !isAutoDriving) {
            Path pathToScore = Paths.line(robotPose, BlueRight)
                    .linear(robotPose, BlueRight);

            followCmd = follow(follower, pathToScore);
            schedule(followCmd);
            isAutoDriving = true;
        }

        // Control Switching & Safe Handover
        if (isAutoDriving) {
            // Check if the Pedro path command has finished executing
            if (followCmd != null && !Scheduler.isRunning(followCmd)) {
                isAutoDriving = false;
                follower.stop();
            }
        } else {
            // Only allow your custom DriveCode when not auto driving
            drive.drive(forward, strafe, rotate);
        }

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

        // Pose override
        if (gamepad1.startWasPressed()) {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            follower.setPose(cornerPose);
        }


        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}

