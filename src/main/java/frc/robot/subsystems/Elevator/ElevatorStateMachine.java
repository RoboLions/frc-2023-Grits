// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Elevator;

import frc.robot.lib.statemachine.StateMachine;

/** Add your docs here. */
public class ElevatorStateMachine extends StateMachine{
    public static ManualState manualState = new ManualState();
    public static IdleState idleState = new IdleState();
    public static ScoreHighState scoreHighState = new ScoreHighState();
    public static ScoreMidState scoreMidState = new ScoreMidState();
    public static ScoreLowState scoreLowState = new ScoreLowState();
    public static SubstationIntakeState substationIntakeState = new SubstationIntakeState();
    public static GroundIntakeState groundIntakeState = new GroundIntakeState();

    public ElevatorStateMachine(){
        manualState.build();
        idleState.build();
        scoreHighState.build();
        scoreMidState.build();
        scoreLowState.build();
        groundIntakeState.build();
        substationIntakeState.build();

        setCurrentState(manualState);
    }
}
