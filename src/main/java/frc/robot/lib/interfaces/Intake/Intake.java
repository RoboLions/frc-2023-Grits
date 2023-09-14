package frc.robot.lib.interfaces.Intake;

import org.littletonrobotics.junction.Logger;

import frc.robot.Constants;

public class Intake {
    public IntakeIO io;
    public IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

    public Intake(IntakeIO io){
        this.io = io;
    }
    public void setPointDrive(double IntakeTarget) {
        io.setMotorPositionOutput(IntakeTarget);
    }

    public void manualDrive(double rotationVal) {
       io.setMotorPercentOutput(rotationVal);
    }

    public void resetEncoder(){
        io.resetEncoder();
    }

    // public double applyDeadband(double armManualInput) {
    //     if (armManualInput > Constants.Intake.STICK_DEADBAND || armManualInput < -Constants.Intake.STICK_DEADBAND) {
    //         return armManualInput;
    //     }
    //     return 0.0;
    // }

    public void periodic(){
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Intake", inputs);
    }
}
