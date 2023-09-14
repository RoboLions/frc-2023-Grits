// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Elevator;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.Constants;

/** Add your docs here. */
public class Elevator {
    private TrapezoidProfile profile;
    private TrapezoidProfile.Constraints constraints = Constants.Elevator.constraints;
  

    public ElevatorModule firstStageElevatorMotor;
    public ElevatorModule secondStageElevatorMotor;


    public Elevator(ElevatorIO firstStage, ElevatorIO secondStage){
        firstStageElevatorMotor = new ElevatorModule(firstStage, "first");
        secondStageElevatorMotor = new ElevatorModule(secondStage, "second");
    }
    public void setBrakeMode(){
        firstStageElevatorMotor.io.setBrakeMode();
        secondStageElevatorMotor.io.setBrakeMode();
    }
    public void setPointDrive(double Goal){
        profile = new TrapezoidProfile(constraints, 
        new TrapezoidProfile.State(Goal, 0), 
        new TrapezoidProfile.State(firstStageElevatorMotor.inputs.elevatorSensorPosition, firstStageElevatorMotor.inputs.elevatorSensorvelocity));
        var setpoint = profile.calculate(0.02);

        firstStageElevatorMotor.io.setMotorPositionOutput(Goal);
        secondStageElevatorMotor.io.setMotorPositionOutput(Goal);

    }
    public void manualDrive(double translationVal){
        profile = new TrapezoidProfile(constraints, 
        new TrapezoidProfile.State(translationVal, 0));
        var setpoint = profile.calculate(0.02);
        firstStageElevatorMotor.io.setMotorPercentOutput(setpoint.position);
        secondStageElevatorMotor.io.setMotorPercentOutput(setpoint.position);
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
