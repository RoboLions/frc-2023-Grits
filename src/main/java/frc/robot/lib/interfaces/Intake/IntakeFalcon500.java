package frc.robot.lib.interfaces.Intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class IntakeFalcon500 implements IntakeIO{
    TalonFX IntakeMotor;
    public IntakeFalcon500(int IntakeMotorID){
        IntakeMotor = new TalonFX(IntakeMotorID);
        
        IntakeMotor.configPeakOutputForward(1.0);
        IntakeMotor.configPeakOutputReverse(-1.0);
        IntakeMotor.setInverted(true);
    }
    public void updateInputs(IntakeIOInputsAutoLogged inputs){
        inputs.IntakePosition = IntakeMotor.getSelectedSensorPosition();
        inputs.IntakeVelocity = IntakeMotor.getSelectedSensorVelocity();
        inputs.IntakeOutput = IntakeMotor.getMotorOutputPercent();
    }
    public void setPercentOutput(double output) {
        IntakeMotor.set(TalonFXControlMode.PercentOutput, output);
    }
    public void setPositionOutput(double position){
        IntakeMotor.set(TalonFXControlMode.Position, position);
    }
}
