package frc.robot.lib.interfaces.Intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.lib.interfaces.Intake.IntakeIO.IntakeIOInputs;

public class IntakeFalcon500 implements IntakeIO{
    public TalonFX intakeMotor;

    // public IntakeFalcon500(double intakeMotorID){
    //     intakeMotor = new TalonFX(intakeMotorID);
    // }

    public void updateInputs(IntakeIOInputs input){
        input.IntakePosition = intakeMotor.getSelectedSensorPosition();
        input.IntakeVelocity = intakeMotor.getSelectedSensorVelocity();
        input.IntakeOutput = intakeMotor.getMotorOutputPercent();
    }

    public void setMotorPositionOutput(double position){
        intakeMotor.set(TalonFXControlMode.Position, position);
    }

    public void setMotorPercentOutput(double output){
        intakeMotor.set(ControlMode.PercentOutput, output);
    }

    public void resetEncoder(){
        intakeMotor.setSelectedSensorPosition(0);
    }

}
