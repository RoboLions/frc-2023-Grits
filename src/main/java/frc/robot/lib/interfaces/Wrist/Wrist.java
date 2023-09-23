// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Wrist;

import frc.robot.Constants;
import org.littletonrobotics.junction.Logger;

/** Add your docs here. */
public class Wrist {
    public WristIO io;
    public WristIOInputsAutoLogged inputs = new WristIOInputsAutoLogged();

    public Wrist(WristIO io){
        this.io = io;
    }

    public void setPointDrive(double wristTarget) {
        Logger.getInstance().recordOutput("WristGoal", wristTarget);
        io.setMotorPositionOutput(wristTarget);
    }

    public void manualDrive(double rotationVal) {
       io.setMotorPercentOutput(rotationVal);
    }

    public void resetEncoder(){
        io.resetEncoder();
    }

    public double applyDeadband(double armManualInput) {
        if (armManualInput > Constants.Wrist.STICK_DEADBAND || armManualInput < -Constants.Wrist.STICK_DEADBAND) {
            return armManualInput;
        }
        return 0.0;
    }
    public void periodic(){
        io.updateInputs(inputs);
        Logger.getInstance().processInputs( "Wrist Motor", inputs);
    }
}