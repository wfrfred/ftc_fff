package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.fff.controller.FtcController;

@TeleOp(name = "TeamFFFTestOpMode", group = "FFF")
public class TeamFFFTestOpmode extends OpMode {
    ShootingModule shootingModule;
    MotionModule motionModule;
    TransportModule transportModule;
    //CVModuleImpl cvModule;
    RoboticArmModule roboticArmModule;
    FtcController controller;

    @Override
    public void init() {
        //cvModule = new CVModuleImpl(hardwareMap,telemetry);
        motionModule = new MotionModuleImpl(hardwareMap);
        transportModule = new TransportModuleImpl(hardwareMap);
        shootingModule = new ShootingModuleImpl(hardwareMap);
        roboticArmModule = new RoboticArmModuleImpl(hardwareMap,gamepad1);
        controller = new FtcControllerImpl(telemetry,motionModule, shootingModule, transportModule , roboticArmModule, null, gamepad1);
        controller.startGamepadListening();
    }

    @Override
    public void loop() {
        telemetry.addData("positon:","%d",hardwareMap.dcMotor.get("armMotor").getCurrentPosition());
        controller.setIsListening(true);
    }

    @Override
    public void stop() {
        controller.setIsListening(false);
    }

}
