package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("front_left_motor");
        c.frontRightName.set("front_right_motor");
        c.backLeftName.set("back_left_motor");
        c.backRightName.set("back_right_motor");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-2.368552590918353);
        c.yPodOffset.set(-5.188557782511073);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.21911626844185741);
                Controller secondaryTranslationalForward = Controller.proportional(0.08095760629474241);
                Controller primaryTranslationalLateral = Controller.proportional(0.23382196549366188);
                Controller secondaryTranslationalLateral = Controller.proportional(0.086390968411922);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016161292863655634));
                c.brake.set(Controller.proportionalFeedforward(0.013737098934107288));

                c.headingFeedback.set(Controller.proportional(2.6452853903244162));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04089342566388585, 0.0057871396197144405));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06392931912711514, 0.03122990938957138));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.435721829308332E-4, 0.001995939259636485));

                c.maxAchievableForwardVelocity.set(63.028114750311225);
                c.maxAchievableStrafeVelocity.set(56.8506144644558);
                c.naturalForwardDeceleration.set(35.81573210101237);
                c.naturalStrafeDeceleration.set(61.889031496485366);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}