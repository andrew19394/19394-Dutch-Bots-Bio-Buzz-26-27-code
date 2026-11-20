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

    private final Pose BlueTop = poseFactory.of(121, 13, 136);

    private final Pose BlueBottom = poseFactory.of(126, 127, 216);

    private final Pose RedBottom = poseFactory.of(19, 13, 36);

    private final Pose RedTop = poseFactory.of(19, 127, 270);


    private boolean isAutoDriving = false;
    private Command followCmd;

    @Override
    public void init() {
        drive = new DriveCode(hardwareMap);
        intake = new intake(hardwareMap);
        shooter = new flywheel(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(OpModeStorage.autonomousEndPose);
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

        // Trigger Auto Drive to BlueTop Pose using Button dpad right
        if (gamepad1.dpad_right && !isAutoDriving) {
            Path pathToScore = Paths.line(robotPose, BlueTop)
                    .linear(robotPose, BlueTop);

            followCmd = follow(follower, pathToScore);
            schedule(followCmd);
            isAutoDriving = true;
        }

        // Trigger Auto Drive to BlueBottom Pose using Button dpad left
        if (gamepad1.dpad_left && !isAutoDriving) {
            Path pathToScore = Paths.line(robotPose, BlueBottom)
                    .linear(robotPose, BlueBottom);

            followCmd = follow(follower, pathToScore);
            schedule(followCmd);
            isAutoDriving = true;
        }

        // Trigger Auto Drive to RedTop Pose using Button dpad up
        if (gamepad1.dpad_up && !isAutoDriving) {
            Path pathToScore = Paths.line(robotPose, RedTop)
                    .linear(robotPose, RedTop);

            followCmd = follow(follower, pathToScore);
            schedule(followCmd);
            isAutoDriving = true;
        }

        // Trigger Auto Drive to RedBottom Pose using Button dpad down
        if (gamepad1.dpad_down && !isAutoDriving) {
            Path pathToScore = Paths.line(robotPose, RedBottom)
                    .linear(robotPose, RedBottom);

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
            intake.intakePower(0.8);
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            intake.intakePower(-0.5);
        }
        else {
            intake.intakePower(0);
        }

        // Launcher speed code
        // Max is 2120
        if (gamepad2.x) {
            shooter.setVelocity(1600);
        } else if (gamepad2.b) {
            shooter.setVelocity(1550);
        }else if (gamepad2.a) {
            shooter.setVelocity(1500);
        }
        else {
            shooter.setVelocity(0);
        }

        if (gamepad2.dpad_up) {
            shooter.shoot(.98, 0.5);
        }else {
            shooter.angle(.95);
        }

        // Pose override in red garden
        if (gamepad1.startWasPressed()) {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            follower.setPose(cornerPose);
        }

        shooter.updateFeedforward();
        shooter.updateTelemetryLaucncher(telemetry);
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}

