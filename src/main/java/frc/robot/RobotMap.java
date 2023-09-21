package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.networktables.NetworkTableInstance.NetworkMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.lib.interfaces.LED;
import frc.robot.lib.interfaces.Elevator.Elevator;
import frc.robot.lib.interfaces.Elevator.ElevatorFalcon500;
import frc.robot.lib.interfaces.Elevator.ElevatorIO;
import frc.robot.lib.interfaces.Intake.Intake;
import frc.robot.lib.interfaces.Intake.IntakeFalcon500;
import frc.robot.lib.interfaces.Swerve.GyroIO;
import frc.robot.lib.interfaces.Swerve.GyroPigeon2;
import frc.robot.lib.interfaces.Swerve.Swerve;
import frc.robot.lib.interfaces.Swerve.SwerveModuleFalcon500;
import frc.robot.lib.interfaces.Swerve.SwerveModuleIO;
import frc.robot.lib.interfaces.Wrist.Wrist;
import frc.robot.lib.interfaces.Wrist.WristFalcon500;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;
import frc.robot.subsystems.Wrist.WristStateMachine;

import frc.robot.subsystems.drive.DrivetrainStateMachine;
import frc.robot.subsystems.intake.IntakeStateMachine;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

public class RobotMap {

    /* state machine instances */
    public static DrivetrainStateMachine drivetrainStateMachine;
    
    public static IntakeStateMachine intakeStateMachine;
    public static LEDStateMachine ledStateMachine;
    public static ElevatorStateMachine elevatorStateMachine;
    public static WristStateMachine wristStateMachine;

    /* Motor instances */
    public static WPI_Pigeon2 gyro;
    // public static WPI_TalonFX leftElbowMotor;
    // public static WPI_TalonFX rightElbowMotor;
    // public static WPI_TalonFX leftShoulderMotor;
    // public static WPI_TalonFX rightShoulderMotor;
    // public static VictorSPX intakeMotor;

    /* Smart Dashboard Instances */
    public static Field2d Field2d;

    /* Interface instances */
    public static Elevator elevator;
    public static Wrist wrist;
    public static Swerve swerve; 
    // public static Arm arm;
    public static LED led;
    public static Intake intake;

    /* Xbox controllers */
    public static XboxController manipulatorController;
    public static XboxController driverController;

    public static void init() {
        
        gyro = new WPI_Pigeon2(Constants.CAN_IDS.PIDGEON, "Swerve");

        // leftShoulderMotor = new WPI_TalonFX(Constants.CAN_IDS.LEFT_SHOULDER_MOTOR);
        // rightShoulderMotor = new WPI_TalonFX(Constants.CAN_IDS.RIGHT_SHOULDER_MOTOR);
        // leftElbowMotor = new WPI_TalonFX(Constants.CAN_IDS.LEFT_ELBOW_MOTOR);
        // rightElbowMotor = new WPI_TalonFX(Constants.CAN_IDS.RIGHT_ELBOW_MOTOR);
        // intakeMotor = new VictorSPX(Constants.CAN_IDS.INTAKE_MOTOR);

        gyro.configFactoryDefault();
        // leftShoulderMotor.configFactoryDefault();
        // rightShoulderMotor.configFactoryDefault();
        // leftElbowMotor.configFactoryDefault();
        // rightElbowMotor.configFactoryDefault();
        // intakeMotor.configFactoryDefault();
        
        switch(Constants.currentMode){
            case REAL:
                intake = new Intake(new IntakeFalcon500(Constants.INTAKE.INTAKE_MOTOR));
                swerve = new Swerve(
                    new GyroPigeon2(Constants.CAN_IDS.PIDGEON),
                    new SwerveModuleFalcon500(Constants.SWERVE.Mod0.constants),
                    new SwerveModuleFalcon500(Constants.SWERVE.Mod1.constants),
                    new SwerveModuleFalcon500(Constants.SWERVE.Mod2.constants),
                    new SwerveModuleFalcon500(Constants.SWERVE.Mod3.constants));

                elevator = new Elevator(
                    new ElevatorFalcon500(Constants.Elevator.elevatorFirstStageMotorID),
                    new ElevatorFalcon500(Constants.Elevator.elevatorSecondStageMotorID)
                );

                wrist = new Wrist(
                    new WristFalcon500(Constants.Wrist.wristMotorID)
                );

                break;
            case REPLAY:
                elevator = new Elevator(
                    new ElevatorIO(){}, 
                    new ElevatorIO(){});
                    
                swerve = new Swerve(
                    new GyroIO(){},
                    new SwerveModuleIO(){},
                    new SwerveModuleIO(){},
                    new SwerveModuleIO(){},
                    new SwerveModuleIO(){});
                    break;
            default:
                break;
    
    }        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
        * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info. */
        Timer.delay(1.0);
        // swerve.periodic();
        // elevator.periodic();
        // wrist.periodic();
        // intake.periodic();
        
        swerve.resetModulesToAbsolute();
        swerve.zeroGyro();
        elevator.resetEncoder();
        wrist.resetEncoder();
        elevator.setNeutralMode(NeutralMode.Brake);

        manipulatorController = new XboxController(1);
        driverController = new XboxController(0);
        
        // arm = new Arm();
        Field2d = new Field2d();
        led = new LED();

        ledStateMachine = new LEDStateMachine();
        drivetrainStateMachine = new DrivetrainStateMachine();
        elevatorStateMachine = new ElevatorStateMachine();
        wristStateMachine = new WristStateMachine();
        // armStateMachine = new ArmStateMachine();
        intakeStateMachine = new IntakeStateMachine();
    }
}
// initiate bomb sequence