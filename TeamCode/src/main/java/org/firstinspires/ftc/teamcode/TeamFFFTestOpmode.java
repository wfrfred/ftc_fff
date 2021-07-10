package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "TeamFFFTestOpMode", group = "FFF")
public class TeamFFFTestOpmode extends OpMode {
    ShootingModule shootingModule;
    MotionModule motionModule;
    CVModuleImpl cvModule;
    FtcController controller;

    @Override
    public void init() {
        cvModule = new CVModuleImpl();
        motionModule = new MotionModuleImpl(hardwareMap, cvModule);
        controller = new FtcControllerImpl(motionModule, null, null, null, cvModule, gamepad1);
        controller.startGamepadListening();
    }

    @Override
    public void loop() {
        telemetry.addData("angle", "%f", cvModule.getAngle());
        telemetry.addData("totalAngle", "%f", controller.getTotalAngle());
    }

    @Override
    public void stop() {
    }

}
