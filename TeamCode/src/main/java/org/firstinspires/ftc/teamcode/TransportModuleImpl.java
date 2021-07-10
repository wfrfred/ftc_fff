package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TransportModuleImpl implements TransportModule {
    private final DcMotor involuter, transporter;
    private int amount = 3;

    TransportModuleImpl(HardwareMap hardwareMap) {
        involuter = hardwareMap.dcMotor.get("involuter");
        transporter = hardwareMap.dcMotor.get("transporter");
    }

    @Override
    public void startInvoluting() {
        involuter.setPower(1.0);
    }

    @Override
    public void stopInvoluting() {
        involuter.setPower(0.0);
    }

    @Override
    public void startTransporting() {
        transporter.setPower(1.0);
    }

    @Override
    public void stopTransporting() {
        transporter.setPower(0.0);
    }
}
