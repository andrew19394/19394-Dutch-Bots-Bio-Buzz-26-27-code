package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Teleop.flywheel;
import org.firstinspires.ftc.teamcode.Teleop.intake;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous

public class PollenAutoBlue extends OpMode {

    private Follower follower;
    private intake intake;
    private flywheel shooter;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(84, 130, 270);
    private final Pose starttoScore = poseFactory.of(84, 110, 270);
    private final Pose scoreToParkStart = poseFactory.of(84, 110, 270);
    private final Pose scoreToPark = poseFactory.of(84, 26, 270);
    private final Pose parktoParkStart = poseFactory.of(84, 26, 270);
    private final Pose parktoPark = poseFactory.of(130, 24, 180);

    public Path starttoScore() {
        return Paths.line(start, starttoScore).linear(start, starttoScore);
    }

    public Path scoreToPark() {
        return Paths.line(scoreToParkStart, scoreToPark).linear(scoreToParkStart, scoreToPark);
    }

    public Path parktoPark() {
        return Paths.line(parktoParkStart, parktoPark).linear(parktoParkStart, parktoPark);
    }


    private Command autoRoutine() {
        return sequential(
                follow(follower, starttoScore()),

                instant(() -> shooter.flywheelPower(0.70)),
                waitMs(2000),
                instant(() -> shooter.shoot(1.0,.3)),
                waitMs(5000),
                instant(() -> shooter.flywheelPower(0.0)),
                instant(() -> shooter.shoot(0.95,0)),

                follow(follower, scoreToPark()),

                follow(follower, parktoPark())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        intake = new intake(hardwareMap);
        shooter = new flywheel(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
    }
    @Override
    public void start() {
        schedule(autoRoutine());
    }
    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}
