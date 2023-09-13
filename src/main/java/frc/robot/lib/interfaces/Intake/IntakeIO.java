package frc.robot.lib.interfaces.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
    @AutoLog
    class IntakeAutoLogInputs{
        public double IntakePosition  = 0.0;
        public double IntakeVelocity  = 0.0;
        public double IntakeOutput = 0.0;
    }
    public default void updateInputs(){}
    public default void setPercentOutput(double output){}
    public default void setPositionOutput(double position){}
}
