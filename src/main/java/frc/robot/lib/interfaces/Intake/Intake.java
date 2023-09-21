package frc.robot.lib.interfaces.Intake;

import org.littletonrobotics.junction.Logger;



public class Intake {
    public IntakeIO io;
    public IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

    public Intake(IntakeIO io){
        this.io = io;
    }
    public void periodic(){
        io.updateInputs(inputs);
        Logger.getInstance().processInputs("Intake", inputs);
    }

  
}
