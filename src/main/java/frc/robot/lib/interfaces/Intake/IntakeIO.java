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
    public default void setPositionOutput(double position){}
    public default void setPercentOutput(double output){}
    public default void resetEncoder(){}
}
