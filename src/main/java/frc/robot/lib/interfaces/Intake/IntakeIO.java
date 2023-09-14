package frc.robot.lib.interfaces.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
    @AutoLog
    public class IntakeIOInputs {
        public double IntakePosition;
        public double IntakeVelocity;
        public double IntakeOutput;
    }
    public default void updateInputs(IntakeIOInputs inputs){}
    public default void setMotorPositionOutput(double position){}
    public default void setMotorPercentOutput(double output){}
    public default void resetEncoder(){}
    class IntakeAutoLogInputs{
        public double IntakePosition  = 0.0;
        public double IntakeVelocity  = 0.0;
        public double IntakeOutput = 0.0;
    }
    public default void updateInputs(){}
    public default void setPercentOutput(double output){}
    public default void setPositionOutput(double position){}
}
