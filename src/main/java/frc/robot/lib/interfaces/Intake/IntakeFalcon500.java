package frc.robot.lib.interfaces.Intake;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class IntakeFalcon500 implements IntakeIO{
    TalonFX IntakeMotor;
    public IntakeFalcon500(int intakeMotor2){
        IntakeMotor = new TalonFX(intakeMotor2);
        
        IntakeMotor.configPeakOutputForward(1.0);
        IntakeMotor.configPeakOutputReverse(-1.0);
        IntakeMotor.setInverted(true);
    }
    public void updateInputs(IntakeIOInputs inputs){
        inputs.IntakePosition = IntakeMotor.getSelectedSensorPosition();
        inputs.IntakeVelocity = IntakeMotor.getSelectedSensorVelocity();
        inputs.IntakeOutput = IntakeMotor.getMotorOutputPercent();
        inputs.IntakeCurrent = IntakeMotor.getStatorCurrent();
    }
    public void setPercentOutput(double output) {
        IntakeMotor.set(TalonFXControlMode.PercentOutput, output);
    }
    public void setPositionOutput(double position){
        IntakeMotor.set(TalonFXControlMode.Position, position);
    }
}
