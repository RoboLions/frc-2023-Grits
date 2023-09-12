// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Wrist;

import frc.robot.Constants;

/** Add your docs here. */
public class Wrist {
    public WristIO io;
    public WristIOInputsAutoLogged inputs = new WristIOInputsAutoLogged();
    public WristModule wirstMotor;
    public Wrist(WristIO wrist){
        this.io = io;
        wirstMotor = new WristModule(wrist, "wrist");
    }

    public void setPointDrive(double wristTarget) {
        wirstMotor.io.setMotorPositionOutput(wristTarget);
    }

    public void manualDrive(double rotationVal) {
        wirstMotor.io.setMotorPositionOutput(rotationVal);
    }

    public void resetEncoder(){
        wirstMotor.io.resetEncoder();
    }

    public double applyDeadband(double armManualInput) {
        if (armManualInput > Constants.Wrist.STICK_DEADBAND || armManualInput < -Constants.Wrist.STICK_DEADBAND) {
            return armManualInput;
        }
        return 0.0;
    }
    public void periodic(){
        io.updateInputs(inputs);
        wirstMotor.periodic();

    }
}