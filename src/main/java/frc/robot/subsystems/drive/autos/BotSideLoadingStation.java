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
import frc.robot.lib.interfaces.Intake.Intake;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;
import frc.robot.subsystems.Wrist.WristStateMachine;
import frc.robot.subsystems.intake.IntakeStateMachine;
/** 3 piece auto on the top side of grids */
public class BotSideLoadingStation extends AutoModeBase {
    
    // trajectory action
    TrajectoryAction driveToFirstPiece;
    TrajectoryAction driveToScoreFirstPiece;
    TrajectoryAction driveToLoadingStation;

    Pose2d initialHolonomicPose;

    public BotSideLoadingStation() {

        SmartDashboard.putBoolean("Auto Finished", false);

        // define theta controller for robot heading
        var thetaController = Constants.SWERVE.Profile.THETA_CONTROLLER;
        
        // transform trajectory depending on alliance we are on
        ArrayList<PathPlannerTrajectory> botSideLoadingStation = (ArrayList<PathPlannerTrajectory>) PathPlanner.loadPathGroup(
            "Bot Side Loading Station", 
            new PathConstraints(1.5, 0.5)
        );

        for(int i = 0; i < botSideLoadingStation.size(); i++) {
            botSideLoadingStation.set(
                i, 
                PathPlannerTrajectory.transformTrajectoryForAlliance(botSideLoadingStation.get(i), DriverStation.getAlliance())
            );
        }

        initialHolonomicPose = botSideLoadingStation.get(0).getInitialHolonomicPose();
        
        driveToFirstPiece = new TrajectoryAction(
            botSideLoadingStation.get(0), 
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToScoreFirstPiece = new TrajectoryAction(
            botSideLoadingStation.get(1), 
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToLoadingStation = new TrajectoryAction(
            botSideLoadingStation.get(2), 
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

        System.out.println("Running Bot Side Loading Station auto!");
        SmartDashboard.putBoolean("Auto Finished", false);

        // close the claw
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.idleState)));

        // // position arm to score high
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)));
        // // wait for arm to arrive in position
        runAction(new ConditionAction(() -> {
            return RobotMap.elevator.getArrived(Constants.Elevator.ScoreHighCone);
        }));

        // // then, score the piece
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.outtakingState)));
        // runAction(new LambdaAction(() -> RobotMap.armStateMachine.setCurrentState(ArmStateMachine.scoringState)));
        
        // wait for the piece to be scored which means the arm is in idle
        runAction(new WaitAction(2.0));



        //Arm and Intake to Idle
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState)));
        
        runAction(driveToLoadingStation);

        
        // drive out of the community to get cube
        // runAction(driveToFirstPiece);
        // // position arm to pick up
        // runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.cubeLEDState)));
        
        // runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.groundIntakeState)));
        // runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.groundIntakeState)));

    //     runAction(new ParallelAction(List.of(
    //             driveToFirstPiece,
    //             new SeriesAction(List.of(
    //                     new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.groundIntakeState)),
    //                     new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.groundIntakeState)),
    //                     new ConditionAction(() -> {
    //                         return RobotMap.elevator.getArrived(Constants.Elevator.GroundIntakeCube);
    //                     }),
    //                     new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.intakingState))
    //         ))
    //     )));

    //     // // wait for arm to arrive in position
        

    //     // // then, close on the cube
    //     // runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.intakingState)));

    //     // // // wait for the claw to grab onto the cube
    //     runAction(new WaitAction(2.0));


    //     runAction(new ParallelAction(List.of(
    //         driveToScoreFirstPiece,
    //         new SeriesAction(List.of(
    //             new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)),
    //             new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)),
    //             new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.idleState))
    //     ))
    // )));
    //     // // position arm to idle
    //     // runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
    //     // runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));

    //     // //drive towards grid to score piece
    //     // runAction(driveToScoreFirstPiece);

    //     // // position arm to score high
    //     // runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)));
    //     // runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)));

    //     // // wait for arm to arrive in position
    //     runAction(new ConditionAction(() -> {
    //         return RobotMap.elevator.getArrived(Constants.Elevator.ScoreHighCube);
    //     }));

    //     // // then, score the piece
    //     runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.outtakingState)));

    //     // // wait for the piece to be scored which means the arm is in idle
    //     runAction(new WaitAction(2.0));

    //     runAction(new ParallelAction(List.of(
    //         driveToLoadingStation,
    //             new SeriesAction(List.of(
    //                 new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)),
    //                 new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)),
    //                 new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState))
    //         ))
    //     )));

    //     // new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)),
    //     // new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)),
    //     // new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.idleState))

        
        // //drive outside community to face loading station
  

        System.out.println("Finished auto!");
        SmartDashboard.putBoolean("Auto Finished", true);
    }

    @Override
    public Pose2d getStartingPose() {
        return initialHolonomicPose;
    }
}
