// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Elevator;

import frc.robot.lib.statemachine.State;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.Transition;
import frc.robot.subsystems.LED.LEDStateMachine;

/** Add your docs here. */
public class ScoreHighState extends State {

    @Override
    public void build() {
        //Transitions
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.IDLE_BUTTON);
        }, ElevatorStateMachine.idleState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.LOW_SCORE_BUTTON);
        }, ElevatorStateMachine.scoreLowState));
        
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MID_SCORE_BUTTON);
        }, ElevatorStateMachine.scoreMidState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MANUAL_MODE_BUTTON);
        }, ElevatorStateMachine.manualState));
            
    }
    
    @Override
    public void init(State prevState) {
        if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState) {
            RobotMap.elevator.setPointDrive(Constants.Elevator.ScoreHighCone);
            ;
        } else {
            RobotMap.elevator.setPointDrive(Constants.Elevator.ScoreHighCube);
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public void exit(State nextState) {
        
    }

}