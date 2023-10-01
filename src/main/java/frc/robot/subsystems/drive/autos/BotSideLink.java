// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive.autos;

import java.util.ArrayList;
import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
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
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;
import frc.robot.subsystems.Wrist.WristStateMachine;
import frc.robot.subsystems.intake.IntakeStateMachine;


/** 3 piece auto on the top side of grids */
public class BotSideLink extends AutoModeBase {
    
    // trajectory action
    TrajectoryAction driveToFirstPiece;
    TrajectoryAction driveToScoreFirstPiece;
    TrajectoryAction driveToSecondPiece;
    TrajectoryAction driveToScoreSecondPiece;

    public BotSideLink() {

        SmartDashboard.putBoolean("Auto Finished", false);

        // define theta controller for robot heading
        var thetaController = Constants.SWERVE.Profile.THETA_CONTROLLER;
        
        // transform trajectory depending on alliance we are on
        ArrayList<PathPlannerTrajectory> topSideLink = (ArrayList<PathPlannerTrajectory>) PathPlanner.loadPathGroup(
            "Bot Side Link Auto", 
            new PathConstraints(0.25, 0.25)
        );
        for(int i = 0; i < topSideLink.size(); i++) {
            topSideLink.set(
                i, 
                PathPlannerTrajectory.transformTrajectoryForAlliance(topSideLink.get(i), DriverStation.getAlliance())
            );
        }
        
        driveToFirstPiece = new TrajectoryAction(
            topSideLink.get(0), 
            RobotMap.swerve::getPose,
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToScoreFirstPiece = new TrajectoryAction(
            topSideLink.get(1), 
            RobotMap.swerve::getPose,
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToSecondPiece = new TrajectoryAction(
            topSideLink.get(2), 
            RobotMap.swerve::getPose, 
            Constants.SWERVE.SWERVE_KINEMATICS, 
            Constants.SWERVE.Profile.X_CONTROLLER,
            Constants.SWERVE.Profile.Y_CONTROLLER,
            thetaController,
            RobotMap.swerve::setModuleStates
        );

        driveToScoreSecondPiece = new TrajectoryAction(
            topSideLink.get(3), 
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

        System.out.println("Running Top Side Link auto!");
        SmartDashboard.putBoolean("Auto Finished", false);

        runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.coneLEDState)));
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
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.outtakingState)));
        // runAction(new LambdaAction(() -> RobotMap.armStateMachine.setCurrentState(ArmStateMachine.scoringState)));


         // wait for the piece to be scored
        runAction(new WaitAction(2.0));

        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));
    
        
        // drive out of the community to get cube
        runAction(driveToFirstPiece);

        runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.cubeLEDState)));
        // position arm to pick up
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.groundIntakeState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.groundIntakeState)));


        // // wait for arm to arrive in position
        runAction(new ConditionAction(() -> {
            return RobotMap.elevator.getArrived(Constants.Elevator.GroundIntakeCube);
        }));

        // // then, close on the cube
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.intakingState)));

        // // // wait for the claw to grab onto the cube
        runAction(new WaitAction(2.0));

        // // position arm to idle
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));

        // //drive towards grid to score piece
        runAction(driveToScoreFirstPiece);

        runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.coneLEDState)));
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
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.outtakingState)));
        // runAction(new LambdaAction(() -> RobotMap.armStateMachine.setCurrentState(ArmStateMachine.scoringState)));


         // wait for the piece to be scored
        runAction(new WaitAction(2.0));

        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));
    
        //drive outside community to get the second piece
        runAction(driveToSecondPiece);

        runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.coneLEDState)));
        // position arm to pick up
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.groundIntakeState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.groundIntakeState)));


        // // wait for arm to arrive in position
        runAction(new ConditionAction(() -> {
            return RobotMap.elevator.getArrived(Constants.Elevator.GroundIntakeCube);
        }));

        // // then, close on the cube
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.intakingState)));

        // // // wait for the claw to grab onto the cube
        runAction(new WaitAction(2.0));

        // // position arm to idle
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));

        //drive towards the grid to score second piece
        runAction(driveToScoreSecondPiece);

        runAction(new LambdaAction(() -> RobotMap.ledStateMachine.setCurrentState(LEDStateMachine.coneLEDState)));
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
        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.outtakingState)));
        // runAction(new LambdaAction(() -> RobotMap.armStateMachine.setCurrentState(ArmStateMachine.scoringState)));


         // wait for the piece to be scored
        runAction(new WaitAction(2.0));

        runAction(new LambdaAction(() -> RobotMap.intakeStateMachine.maintainState(IntakeStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.elevatorStateMachine.setCurrentState(ElevatorStateMachine.idleState)));
        runAction(new LambdaAction(() -> RobotMap.wristStateMachine.setCurrentState(WristStateMachine.idleState)));
    

        System.out.println("Finished auto!");
        SmartDashboard.putBoolean("Auto Finished", true);
    }

    @Override
    public Pose2d getStartingPose() {
        return driveToFirstPiece.getInitialPose();
    }
}
