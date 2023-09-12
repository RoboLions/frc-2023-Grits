// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Wrist;

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
            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.IDLE_BUTTON);
            }, WristStateMachine.idleState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.LOW_SCORE_BUTTON);
            }, WristStateMachine.scoreLowState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MID_SCORE_BUTTON);
            }, WristStateMachine.scoreMidState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.HIGH_SCORE_BUTTON);
            }, WristStateMachine.scoreHighState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.SUBSTATION_INTAKE_BUTTON);
            }, WristStateMachine.substationIntakeState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.GROUND_INTAKE_FRONT);
            }, WristStateMachine.groundIntakeState));
        }
    
        @Override
        public void init(State prevState) {
            this.prevState = prevState;
        }
    
        @Override
        public void execute() {
            double rotationVal = RobotMap.manipulatorController.getRightX();
            RobotMap.wrist.manualDrive(RobotMap.wrist.applyDeadband(rotationVal) * 0.25);

        }
    
        @Override
        public void exit(State nextState) {

        }
    }   
