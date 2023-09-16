package frc.robot.lib.interfaces.Intake;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.subsystems.Elevator.ElevatorStateMachine;
import frc.robot.subsystems.LED.LEDStateMachine;

public class Intake {
    public IntakeIO io;
    public IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

    public Intake(IntakeIO io){
        this.io = io;
    }
    public void runIntake() {
        // if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState) {
        //     io.setPercentOutput(-1 * Constants.INTAKE.INTAKE_POWER);
        // } else {
        //     io.setPercentOutput(Constants.INTAKE.INTAKE_POWER);
        // }
        io.setPercentOutput(0.5);
    }

    public void runOuttake() {
        // if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState) {
        //     io.setPercentOutput(-1 * Constants.INTAKE.INTAKE_POWER);
        // } else if (RobotMap.ledStateMachine.getCurrentState() == LEDStateMachine.coneLEDState && RobotMap.elevatorStateMachine.getCurrentState() == ElevatorStateMachine.scoreMidState) {
        //     io.setPercentOutput(-1 * Constants.INTAKE.INTAKE_POWER * 1.4);
        // } else {
        //     io.setPercentOutput(Constants.INTAKE.INTAKE_POWER);
        // }
        io.setPercentOutput(0.5);
    }
}
