// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems.Wrist;

import org.littletonrobotics.junction.Logger;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;
import frc.robot.subsystems.LED.LEDStateMachine;

/** Add your docs here. */
public class GroundIntakeState extends State {

    @Override
    public void build() {
        //Transition
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.IDLE_BUTTON)
           || RobotMap.manipulatorController.getRawAxis(Constants.ManipulatorControls.GROUND_INTAKE_FRONT) < 0.1;
        }, WristStateMachine.idleState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.LOW_SCORE_BUTTON);
        }, WristStateMachine.scoreLowState));
        
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.HIGH_SCORE_BUTTON);
        }, WristStateMachine.scoreHighState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MANUAL_MODE_BUTTON);
        }, WristStateMachine.manualState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.MID_SCORE_BUTTON);
        }, WristStateMachine.scoreMidState));

        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.SUBSTATION_INTAKE_BUTTON);
        }, WristStateMachine.substationIntakeState));

    }
    
    @Override
    public void init(State prevState) {
        Logger.getInstance().recordOutput("IS IN GROUND INTAKE", true);
        
        if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState) {
            RobotMap.wrist.setPointDrive(Constants.Wrist.GroundIntakeCone);
            
        } else {
            RobotMap.wrist.setPointDrive(Constants.Wrist.GroundIntakeCube);
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public void exit(State nextState) {
        
    }

}