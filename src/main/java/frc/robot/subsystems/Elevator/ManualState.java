// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Elevator;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;

/** Add your docs here. */
public class ManualState extends State{
        State prevState;
        @Override
        public void build() {
       //Transitions
    //    transitions.add(new Transition(() -> {
    //     return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.IDLE_BUTTON);
    // }, ElevatorStateMachine.idleState));

    // transitions.add(new Transition(() -> {
    //     return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.LOW_SCORE_BUTTON);
    // }, ElevatorStateMachine.scoreLowState));
    
    // transitions.add(new Transition(() -> {
    //     return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MID_SCORE_BUTTON);
    // }, ElevatorStateMachine.scoreMidState));

    // transitions.add(new Transition(() -> {
    //     return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.HIGH_SCORE_BUTTON);
    // }, ElevatorStateMachine.scoreHighState));
        }
    
        @Override
        public void init(State prevState) {
            this.prevState = prevState;
        }
    
        @Override
        public void execute() {
            double translationVal = RobotMap.manipulatorController.getLeftY();
            RobotMap.elevator.manualDrive(RobotMap.elevator.applyDeadband(translationVal));

        }
    
        @Override
        public void exit(State nextState) {
        }
    }   
