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

public class PollenAuto extends OpMode {

    private Follower follower;
    private intake intake;
    private flywheel shooter;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(59, 9, 270);
    private final Pose starttoScore = poseFactory.of(59, 12, 270);
    private final Pose scoretoParkStart = poseFactory.of(59, 25, 270);
    private final Pose scoretoPark = poseFactory.of(59, 110, 270);
    private final Pose parkStart = poseFactory.of(59, 110, 270);
    private final Pose park = poseFactory.of(10, 120, 0);

    public Path starttoScore() {
        return Paths.line(start, starttoScore).linear(start, starttoScore);
    }

    public Path scoretoPark() {
        return Paths.line(scoretoParkStart, scoretoPark).linear(scoretoParkStart, scoretoPark);
    }

    public Path park() {
        return Paths.line(parkStart, park).linear(parkStart, park);
    }


    private Command autoRoutine() {
        return sequential(
                follow(follower, starttoScore()),

                instant(() -> shooter.setVelocity(1500)),
                waitMs(2000),
                instant(() -> shooter.shoot(0.98,0.5)),
                waitMs(3000),
                instant(() -> shooter.setVelocity(0)),
                instant(() -> shooter.shoot(0.95,0)),

                follow(follower, scoretoPark()),

                follow(follower, park())
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
        shooter.updateFeedforward();
    }

    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}
