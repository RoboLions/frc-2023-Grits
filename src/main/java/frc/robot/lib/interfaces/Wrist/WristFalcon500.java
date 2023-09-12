// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Wrist;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

/** Add your docs here. */
public class WristFalcon500 implements WristIO{
    public TalonFX wristMotor;
    public WristFalcon500(int WristMotorID){
        wristMotor = new TalonFX(WristMotorID);
    }

    public void setMotorPercentOutput(double output){
        wristMotor.set(TalonFXControlMode.PercentOutput, output);
    }
    public void setMotorPositionOutput(double position){
        wristMotor.set(TalonFXControlMode.Position, position);
    }

    public void resetEncoder(){
        wristMotor.setSelectedSensorPosition(0);
    }

    public void updateInputs(WristIOInputs input){
        input.WristPosition = wristMotor.getSelectedSensorPosition();
        input.WristVelocity = wristMotor.getSelectedSensorVelocity();
        input.WristOutput = wristMotor.getMotorOutputPercent();
    }
}
