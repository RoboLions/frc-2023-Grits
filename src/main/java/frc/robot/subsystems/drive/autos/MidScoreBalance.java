package frc.robot.subsystems.drive.autos;

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
import frc.robot.lib.auto.actions.TrajectoryAction;
import frc.robot.lib.auto.actions.WaitAction;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;
import frc.robot.subsystems.Wrist.WristStateMachine;
import frc.robot.subsystems.drive.DrivetrainStateMachine;
import frc.robot.subsystems.intake.IntakeStateMachine;

/** Simple mid score, 1 cone high, then balance on charging station */
public class MidScoreBalance extends AutoModeBase {
    
    // trajectory action
    TrajectoryAction driveToChargeStation;

    Pose2d initialHolonomicPose;

    private Timer timer = new Timer();

    public MidScoreBalance() {

        SmartDashboard.putBoolean("Auto Finished", false);

        // define theta controller for robot heading
        var thetaController = Constants.SWERVE.Profile.THETA_CONTROLLER;
        
        // transform trajectory depending on alliance we are on
        PathPlannerTrajectory botMidScore = PathPlanner.loadPath("Mid Score + Balance", new PathConstraints(1.25, 1.75));
        botMidScore = PathPlannerTrajectory.transformTrajectoryForAlliance(botMidScore, DriverStation.getAlliance());
        
        initialHolonomicPose = botMidScore.getInitialHolonomicPose();

        driveToChargeStation = new TrajectoryAction(
            botMidScore, 
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

        System.out.println("Running mid score with balance auto!");
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
    
        // drive onto the charge station
        runAction(driveToChargeStation);

        // switch drivetrain to balance state
        runAction(new LambdaAction(() -> RobotMap.drivetrainStateMachine.setCurrentState(DrivetrainStateMachine.balanceState)));

        System.out.println("Finished auto!");
        SmartDashboard.putBoolean("Auto Finished", true);
    }

    @Override
    public Pose2d getStartingPose() {
        return initialHolonomicPose;
    }
}
