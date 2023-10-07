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

        double elevator_goal = 0.0;

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
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.HIGH_SCORE_BUTTON);
            }, ElevatorStateMachine.scoreHighState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawAxis(Constants.ManipulatorControls.GROUND_INTAKE_FRONT) > 0.1;
            }, ElevatorStateMachine.groundIntakeState));

            transitions.add(new Transition(() -> {
                return RobotMap.manipulatorController.getRawButton(Constants.ManipulatorControls.SUBSTATION_INTAKE_BUTTON);
            }, ElevatorStateMachine.substationIntakeState));
        }
    
        @Override
        public void init(State prevState) {
            elevator_goal = RobotMap.elevator.firstStageElevatorMotor.inputs.elevatorSensorPosition / Constants.Elevator.Encoders_per_Meter;  
        }
    
        @Override
        public void execute() {
            double joystick_input = RobotMap.elevator.applyDeadband(RobotMap.manipulatorController.getLeftY());
            elevator_goal += joystick_input * -0.005;
            RobotMap.elevator.setPointDrive(elevator_goal);
        }
    
        @Override
        public void exit(State nextState) {
        }
    }   
