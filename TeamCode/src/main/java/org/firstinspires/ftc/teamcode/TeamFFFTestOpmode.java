package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;

@TeleOp(name="TeamFFFTestOpMode", group="FFF")
public class TeamFFFTestOpmode extends OpMode {
    MotionModuleImpl motionModule;
    FtcControllerImpl controller;



    @Override
    public void init() {
        motionModule = new MotionModuleImpl();
        try {
            controller = new FtcControllerImpl(motionModule, null, null, null, gamepad1);
        }catch(RobotCoreException e){

        }
    }

    @Override
    public void loop() {
        controller.setMotionModuleManual();
    }
}
