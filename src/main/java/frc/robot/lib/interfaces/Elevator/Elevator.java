// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Elevator;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.Constants;

/** Add your docs here. */
public class Elevator {
    private TrapezoidProfile profile;
    private TrapezoidProfile.Constraints constraints = Constants.Elevator.constraints;
    private TrapezoidProfile.State m_setpoint = new TrapezoidProfile.State();
    private TrapezoidProfile.State pid_setpoint = new TrapezoidProfile.State();
  

    public ElevatorModule firstStageElevatorMotor;
    public ElevatorModule secondStageElevatorMotor;


    public Elevator(ElevatorIO firstStage, ElevatorIO secondStage){
        firstStageElevatorMotor = new ElevatorModule(firstStage, "first");
        secondStageElevatorMotor = new ElevatorModule(secondStage, "second");
    }

    public void setPointDrive(double Goal){
        profile = new TrapezoidProfile(constraints, new TrapezoidProfile.State(Goal, 0), pid_setpoint);
        pid_setpoint = profile.calculate(0.25);
        Logger.getInstance().recordOutput("ElevatorOutput", pid_setpoint.position);
        // firstStageElevatorMotor.io.setMotorPositionOutput(setpoint.position);
        // secondStageElevatorMotor.io.setMotorPositionOutput(setpoint.position);

    }

    public void manualDrive(double translationVal){
        TrapezoidProfile.State m_goal = new TrapezoidProfile.State(translationVal, 0);
        profile = new TrapezoidProfile(constraints, 
        m_goal , m_setpoint);
        m_setpoint = profile.calculate(0.75);
        // firstStageElevatorMotor.io.setMotorPercentOutput(translationVal);
        // secondStageElevatorMotor.io.setMotorPercentOutput(translationVal);
        Logger.getInstance().recordOutput("ElevatorOutput", m_setpoint.position);
    }
    public void setNeutralMode(NeutralMode mode){
        firstStageElevatorMotor.io.setNeutralMode(mode);
        secondStageElevatorMotor.io.setNeutralMode(mode);
    }

    public void resetEncoder(){
        firstStageElevatorMotor.io.resetEncoder();
        secondStageElevatorMotor.io.resetEncoder();
    }

    public double applyDeadband(double armManualInput) {
        if (armManualInput > Constants.Elevator.STICK_DEADBAND || armManualInput < -Constants.Elevator.STICK_DEADBAND) {
            return armManualInput;
        }
        return 0.0;
    }

    public void periodic(){
        firstStageElevatorMotor.periodic();
        secondStageElevatorMotor.periodic();
    }
}