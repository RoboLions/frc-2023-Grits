// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.Elevator.GroundIntakeState;
import frc.robot.subsystems.LED.LEDStateMachine;

/** Add your docs here. */
public class OuttakingState extends State {
    
    @Override
    public void build() {

        transitions.add(new Transition(() -> {
            return !RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.OUTTAKE_BUTTON) && 
                RobotMap.driverController.getRawAxis(Constants.DriverControls.SCORING_AXIS) < 0.25;
        }, IntakeStateMachine.idleState));
    }

    @Override
    public void init(State prevState) {
        if(RobotMap.elevatorStateMachine.getCurrentState() != ElevatorStateMachine.scoreLowState){
            if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState) {
                RobotMap.intake.io.setPercentOutput(Constants.INTAKE.INTAKE_POWER);
            } else {
             RobotMap.intake.io.setPercentOutput(-1 * Constants.INTAKE.INTAKE_POWER);
            }
        }
        else{
            if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState) {
                RobotMap.intake.io.setPercentOutput(Constants.INTAKE.INTAKE_POWER * 0.5);
            } else {
             RobotMap.intake.io.setPercentOutput(-1 * Constants.INTAKE.INTAKE_POWER * 0.5);
            }
        }
    }

    @Override
    public void execute() {
  
    }

    @Override
    public void exit(State nextState) {

    }
}
