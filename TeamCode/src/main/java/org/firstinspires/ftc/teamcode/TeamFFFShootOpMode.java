package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "TeamFFFShootOpMode", group = "FFF")
public class TeamFFFShootOpMode extends OpMode {
    private DcMotor s;
    private Servo p;
    public double position;
    public boolean i;

    @Override
    public void init() {
        s = hardwareMap.dcMotor.get("shootMotor");
        p = hardwareMap.servo.get("pushServo");
        position = 0;
        i = false;
    }

    @Override
    public void loop() {
        if (gamepad1.a) {

        }
        if (gamepad1.right_bumper) {

        }

        /*
        int j = 1;
        if(gamepad1.a){
            i = !i;
            sleep(100);
        }
        if(gamepad1.b){
            j = -j;
        }
        if(i){
            s.setPower(j);
        }else {
            s.setPower(0);
        }
        if(gamepad1.right_bumper) position += 0.05;
        if(gamepad1.left_bumper) position -= 0.05;
        p.setPosition(position);
        telemetry.addData("Position",   "%5.2f",  position);
        sleep(50);

         */
    }

    @Override
    public void stop() {
    }

    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
