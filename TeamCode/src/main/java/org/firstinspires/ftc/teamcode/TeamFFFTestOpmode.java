package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;

@TeleOp(name="TeamFFFTestOpMode", group="FFF")
public class TeamFFFTestOpmode extends OpMode {
    ShootingModuleImpl shootingModule;
    FtcControllerImpl controller;



    @Override
    public void init() {
        shootingModule = new ShootingModuleImpl(hardwareMap);
        try {
            controller = new FtcControllerImpl(null, shootingModule, null, null, gamepad1);
        }catch(RobotCoreException e){

        }

    }

    @Override
    public void loop() {
        controller.gamepadStartListen();
    }

    @Override
    public void stop(){
        controller.gamepadStopListen();
    }

}
