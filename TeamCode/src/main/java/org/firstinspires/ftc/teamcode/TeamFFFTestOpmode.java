package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name="TeamFFFTestOpMode", group="FFF")
public class TeamFFFTestOpmode extends OpMode {
    ShootingModule shootingModule;
    MotionModule motionModule;
    CVModuleImpl cvModule;
    FtcController controller;

    @Override
    public void init() {
        cvModule = new CVModuleImpl();

        /*
        shootingModule = new ShootingModuleImpl(hardwareMap);
        //motionModule = new MotionModuleImpl(hardwareMap);
        controller = new FtcControllerImpl(null, shootingModule, null, null, gamepad1);
        //controller.startMotionModuleManualThread();
        try {
            controller.startGamepadListening();
        }catch (NullPointerException e){

        }

         */
    }

    @Override
    public void loop() {
        telemetry.addData("angle","%f",cvModule.getAngle());

        //controller.setIsListening(true);

    }

    @Override
    public void stop(){
        //controller.setIsListening(false);

    }

}
