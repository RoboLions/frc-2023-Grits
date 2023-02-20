// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import frc.robot.lib.statemachine.State;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.Transition;

/** Add your docs here. */
public class SubstationIntakeState extends State {

    @Override
    public void build() {
        // Go to IDLE Transitions
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButtonPressed(Constants.ManipulatorControls.IDLE_BUTTON);
        }, ArmStateMachine.idleState));

        transitions.add(new Transition(() -> {
            return RobotMap.claw.getColor() != null &&
                RobotMap.claw.isClosed();
        }, ArmStateMachine.idleState));
    }
    
    @Override
    public void init() {

        double modifier = RobotMap.arm.getSubstationDirectionModifier();
        RobotMap.arm.moveArmPosition(
            modifier * Constants.SUBSTATION_INTAKE.SHOULDER_POSITION, 
            modifier * Constants.SUBSTATION_INTAKE.ELBOW_POSITION, 
            modifier * Constants.SUBSTATION_INTAKE.WRIST_POSITION
        );
    }

    @Override
    public void execute() {
    }

    @Override
    public void exit() {
        
    }

}
