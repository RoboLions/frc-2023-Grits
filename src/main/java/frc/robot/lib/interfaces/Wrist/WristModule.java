package frc.robot.lib.interfaces.Wrist;

import org.littletonrobotics.junction.Logger;

/** Add your docs here. */
public class WristModule {
    public final String motorName;
    final WristIO io;
    public final WristIOInputsAutoLogged inputs = new WristIOInputsAutoLogged();

    public WristModule(WristIO io, String motorName){
        this.io = io;
        this.motorName = motorName;
    }

    public void periodic(){
        io.updateInputs(inputs);
        Logger.getInstance().processInputs(motorName + " Wrist", inputs);
    }
}