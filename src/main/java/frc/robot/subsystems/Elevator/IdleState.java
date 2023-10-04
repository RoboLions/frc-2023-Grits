// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Elevator;

import org.littletonrobotics.junction.Logger;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;

/** Add your docs here. */
public class IdleState extends State{
    
    @Override
    public void build() {
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.HIGH_SCORE_BUTTON);
        }, ElevatorStateMachine.scoreHighState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.LOW_SCORE_BUTTON);
        }, ElevatorStateMachine.scoreLowState));
        
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MID_SCORE_BUTTON);
        }, ElevatorStateMachine.scoreMidState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MANUAL_MODE_BUTTON);
        }, ElevatorStateMachine.manualState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawAxis(Constants.ManipulatorControls.GROUND_INTAKE_FRONT) > 0.1;
        }, ElevatorStateMachine.groundIntakeState));
    }
    
    @Override
    public void init(State prevState) {
        
    
    }

    @Override
    public void execute() {
        RobotMap.elevator.setPointDrive(Constants.Elevator.IdleState);
    }

    @Override
    public void exit(State nextState) {
    RobotMap.elevator.resetEncoder();
    }
}
