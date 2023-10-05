// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive.autos;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.auto.AutoModeBase;
import frc.robot.lib.auto.AutoModeEndedException;
import frc.robot.lib.auto.actions.ConditionAction;
import frc.robot.lib.auto.actions.LambdaAction;
import frc.robot.lib.auto.actions.ParallelAction;
import frc.robot.lib.auto.actions.SeriesAction;
import frc.robot.lib.auto.actions.TrajectoryAction;
import frc.robot.lib.auto.actions.WaitAction;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;
import frc.robot.subsystems.Wrist.WristStateMachine;
import frc.robot.subsystems.intake.IntakeStateMachine;

/** 3 piece auto on the top side of grids */
public class TopTwoPieceRed extends AutoModeBase {
    
    // trajectory action
    TrajectoryAction driveToIntake;
    TrajectoryAction driveToScore;

    private Timer timer = new Timer();
    private Timer timer2 = new Timer();
    private Timer timer3 = new Timer();
    
    Pose2d initialHolonomicPose;

    public TopTwoPieceRed() {

        SmartDashboard.putBoolean("Auto Finished", false);

        // define theta controller for robot heading
        var thetaController = Constants.SWERVE.Profile.THETA_CONTROLLER;
        thetaController.enableContinuousInput(-180.0, 180.0);
        
        ArrayList<PathPlannerTrajectory> topTwoPiece = (ArrayList<PathPlannerTrajectory>) PathPlanner.loadPathGroup(
            "Top Side Two Piece Red", 
            new PathConstraints(3.0, 2.0)
        );

        initialHolonomicPose = topTwoPiece.get(0).getInitialHolonomicPose();

        driveToIntake = new TrajectoryAction(
            topTwoPiece.get(0), 
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToScore = new TrajectoryAction(
            topTwoPiece.get(1),
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );
    }

    @Override
    protected void routine() throws AutoModeEndedException {

        System.out.println("Running Top Side Two Piece Red auto!");
        SmartDashboard.putBoolean("Auto Finished", false);

// Hold Cone
runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState)));

// // position arm to score high
runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)));
runAction(new WaitAction(0.5));
runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)));
// // wait for arm to arrive in position
runAction(new ConditionAction(() -> {
    return RobotMap.elevator.getArrived(Constants.Elevator.ScoreHighCone);
}));

// // then, score the piece
runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.outtakingState)));


// wait for the piece to be scored
runAction(new WaitAction(2.0));


 // // put LED to cube (purple)
 runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.cubeLEDState)));

//  runAction(driveToIntake);

 //Drive to Cube while prepping to intake
 runAction(new ParallelAction(List.of(
     driveToIntake,
         new SeriesAction(List.of(
                 new LambdaAction(() -> RobotMap.elevatorStateMachine.maintainState(ElevatorStateMachine.groundIntakeState)),
                 new LambdaAction(() -> RobotMap.wristStateMachine.maintainState(WristStateMachine.groundIntakeState)),
                 new ConditionAction(() -> {
                     return RobotMap.elevator.getArrived(Constants.Elevator.GroundIntakeCube);
                 }),
                 new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.intakingState))
     ))
 )));

// runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.maintainState(ElevatorStateMachine.groundIntakeState)));
// runAction(new LambdaAction(() -> RobotMap.wristStateMachine.maintainState(WristStateMachine.groundIntakeState)));

// runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.maintainState(ElevatorStateMachine.groundIntakeState)));
//  runAction(new LambdaAction(() -> RobotMap.wristStateMachine.maintainState(WristStateMachine.groundIntakeState)));
// runAction(new ConditionAction(() -> {
//                          return RobotMap.elevator.getArrived(Constants.Elevator.GroundIntakeCube);
//                      }));
// runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.intakingState)));
 
 

 //Drive to score while raising arm
 runAction(new ParallelAction(List.of(
     driveToScore,
     new SeriesAction(List.of(
         new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)),
         new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)),
         new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState))
 ))
))); 
// runAction(driveToScore);

// runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)));
// runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)));
// runAction( new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState)));
//Check if we are in High
runAction(new ConditionAction(() -> {
     return RobotMap.elevator.getArrived(Constants.Elevator.ScoreHighCube);
 }));

 // // then, score the piece
 runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.outtakingState)));

//  runAction(d)

 // wait for the piece to be scored
 runAction(new WaitAction(2.0));
 // stop intake
 runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState)));

 // put arm to idle
 runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
 runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));


        System.out.println("Finished auto!");
        SmartDashboard.putBoolean("Auto Finished", true);
    }

    @Override
    public Pose2d getStartingPose() {
        return initialHolonomicPose;
    }
}
