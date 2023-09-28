// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Elevator;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/** Add your docs here. */
public interface ElevatorIO {
    @AutoLog
    public static class ElevatorIOInputs{
        public double elevatorSensorPosition;
        public double elevatorSensorvelocity;
        public double elevetorPercentOutput;
    }
    public default void setNeutralMode(NeutralMode mode){}
    public default void setMotorPositionOutput(double position){}
    public default void resetEncoder(){}
    public default void setMotorPercentOutput(double output){}
    public default void updateInputs(ElevatorIOInputs inputs){}
    public default void setInverted(boolean invert) {}
    public default void set(ControlMode mode, double outputValue) {}
}
