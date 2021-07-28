package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TransportModuleImpl implements TransportModule {
    private final DcMotor transporter;

    TransportModuleImpl(HardwareMap hardwareMap) {
        transporter = hardwareMap.dcMotor.get("transporter");
    }
    @Override
    public void startTransporting(boolean isForward) {
        if(isForward){
            transporter.setPower(1.0);
        }else{
            transporter.setPower(-1.0);
        }

    }

    @Override
    public void stopTransporting() {
        transporter.setPower(0.0);
    }
}
