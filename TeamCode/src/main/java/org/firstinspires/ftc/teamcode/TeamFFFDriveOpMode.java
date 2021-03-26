package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="TeamFFFDriveOpMode", group="FFF")
public class TeamFFFDriveOpMode extends OpMode {
    private DcMotor l1,l2,r1,r2;

    @Override
    public void init() {
        l1 = hardwareMap.dcMotor.get("l1");
        l2 = hardwareMap.dcMotor.get("l2");
        r1 = hardwareMap.dcMotor.get("r1");
        r2 = hardwareMap.dcMotor.get("r2");

        //麦轮左右反转
        l1.setDirection(DcMotorSimple.Direction.REVERSE);
        l2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        //读取手柄：前进 转向 横向
        double move = -gamepad1.left_stick_y;
        double fun = gamepad1.left_stick_x;
        double turn= gamepad1.right_stick_x;

        //计算每个电机
        double first = (move + turn + fun)/4;
        double second = (move + turn - fun)/4;
        double third = (move - turn - fun)/4;
        double fourth = (move - turn + fun)/4;

        l1.setPower(first);
        l2.setPower(second);
        r1.setPower(third);
        r2.setPower(fourth);

    }

    @Override
    public void stop(){
    }
}
