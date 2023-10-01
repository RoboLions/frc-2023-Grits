// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive.autos;

import java.util.ArrayList;
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
import frc.robot.lib.auto.actions.TrajectoryAction;
import frc.robot.lib.auto.actions.WaitAction;
import frc.robot.lib.interfaces.Intake.Intake;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;
import frc.robot.subsystems.Wrist.WristStateMachine;
import frc.robot.subsystems.intake.IntakeStateMachine;

/** 3 piece auto on the top side of grids */
public class TopSideLoadingStation extends AutoModeBase {
    
    // trajectory action
    TrajectoryAction driveToFirstPiece;
    TrajectoryAction driveToScoreFirstPiece;
    TrajectoryAction driveToLoadingStation;

    Pose2d initialHolonomicPose;

    public TopSideLoadingStation() {

        SmartDashboard.putBoolean("Auto Finished", false);

        // define theta controller for robot heading
        var thetaController = Constants.SWERVE.Profile.THETA_CONTROLLER;
        thetaController.enableContinuousInput(-180.0, 180.0);
        
        // transform trajectory depending on alliance we are on
        ArrayList<PathPlannerTrajectory> topSideLoadingStation = (ArrayList<PathPlannerTrajectory>) PathPlanner.loadPathGroup(
            "Top Side Loading Station", 
            new PathConstraints(2.5, 1.0)
        );

        for(int i = 0; i < topSideLoadingStation.size(); i++) {
            topSideLoadingStation.set(
                i, 
                PathPlannerTrajectory.transformTrajectoryForAlliance(topSideLoadingStation.get(i), DriverStation.getAlliance())
            );
        }
        
        initialHolonomicPose = topSideLoadingStation.get(0).getInitialHolonomicPose();
        
        driveToFirstPiece = new TrajectoryAction(
            topSideLoadingStation.get(0), 
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToScoreFirstPiece = new TrajectoryAction(
            topSideLoadingStation.get(1), 
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToLoadingStation = new TrajectoryAction(
            topSideLoadingStation.get(2), 
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

        System.out.println("Running Top Side Loading Station auto!");
        SmartDashboard.putBoolean("Auto Finished", false);

        // close the claw
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.idleState)));
        
        // position arm to score high
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)));

        // wait for arm to arrive in position
        runAction(new ConditionAction(() -> {
            return RobotMap.elevator.getArrived(Constants.Elevator.ScoreHighCone);
        }));

        // then, score the piece
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.outtakingState)));
        

        // wait for the piece to be scored which means the arm is in idle
        runAction(new WaitAction(2.0));

        // position arm to pick up
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.groundIntakeState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.groundIntakeState)));
        runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.cubeLEDState)));
        // drive out of the community to get cube
        runAction(driveToFirstPiece);

        // wait for arm to arrive in position
        runAction(new ConditionAction(() -> {
            return RobotMap.elevator.getArrived(Constants.Elevator.GroundIntakeCube);
        }));

        // // then, close on the cube
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.intakingState)));

        // // wait for the claw to grab onto the cube
        runAction(new WaitAction(2.0));
        // //drive towards grid to score piece
        runAction(driveToScoreFirstPiece);

        // // position arm to score high
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.scoreHighState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.scoreHighState)));

        // // wait for arm to arrive in position
        runAction(new ConditionAction(() -> {
            return RobotMap.elevator.getArrived(Constants.Elevator.ScoreHighCone);
        }));

        // // then, score the piece
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.setCurrentState(IntakeStateMachine.outtakingState)));

        // // wait for the piece to be scored which means the arm is in idle
        runAction(new WaitAction(2.0));

        // //drive outside community to face loading station
        runAction(driveToLoadingStation);

        System.out.println("Finished auto!");
        SmartDashboard.putBoolean("Auto Finished", true);
    }

    @Override
    public Pose2d getStartingPose() {
        return initialHolonomicPose;
    }
}
