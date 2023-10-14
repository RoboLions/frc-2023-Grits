// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Elevator;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.Constants;
import frc.robot.RobotMap;

/** Add your docs here. */
public class Elevator {
    private TrapezoidProfile profile;
    private TrapezoidProfile.Constraints constraints = Constants.Elevator.constraints;
    private TrapezoidProfile.State pid_setpoint = new TrapezoidProfile.State();
    private TrapezoidProfile.State m_goalpoint = new TrapezoidProfile.State(0, 0);

    public ElevatorModule firstStageElevatorMotor;
    public ElevatorModule secondStageElevatorMotor;


    public Elevator(ElevatorIO firstStage, ElevatorIO secondStage){
        firstStageElevatorMotor = new ElevatorModule(firstStage, "first");
        secondStageElevatorMotor = new ElevatorModule(secondStage, "second");
        firstStageElevatorMotor.io.setInverted(true);
        secondStageElevatorMotor.io.setInverted(true);
        secondStage.set(ControlMode.Follower, Constants.Elevator.elevatorFirstStageMotorID);
    }

    public void setPointDrive(double Goal){
        Logger.getInstance().recordOutput("ElevatorGoal", (Goal));
        m_goalpoint = new TrapezoidProfile.State(Goal, 0);
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

    public Boolean getArrived(double target) {
        return pid_setpoint.position == target;
        
    }

    public void periodic(){
        firstStageElevatorMotor.periodic();
        secondStageElevatorMotor.periodic();
        profile = new TrapezoidProfile(constraints, m_goalpoint, pid_setpoint);
        pid_setpoint = profile.calculate(0.2);
        Logger.getInstance().recordOutput("ElevatorPIDSetPoint", pid_setpoint.position);
        double elevatorSensorPositionMeters = firstStageElevatorMotor.inputs.elevatorSensorPosition / Constants.Elevator.Encoders_per_Meter;
        Logger.getInstance().recordOutput("ElevatorPIDError", pid_setpoint.position - elevatorSensorPositionMeters);
        Logger.getInstance().recordOutput("ElevatorPosition", elevatorSensorPositionMeters);

        //convert Meters to Ticks
        double target = pid_setpoint.position * Constants.Elevator.Encoders_per_Meter;
        firstStageElevatorMotor.io.setMotorPositionOutput(target);
        
    }
}
