package frc.robot.subsystems.LED;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotMap;
import frc.robot.lib.interfaces.LED;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;
import frc.robot.subsystems.intake.IntakeStateMachine;
import frc.robot.subsystems.intake.OuttakingState;


public class CubeLEDState extends State {
    public int green = 0;
    public Timer time = new Timer();

    public void build(){
        transitions.add(new Transition(() -> {
            return LED.backButton;
        }, LEDStateMachine.coneLEDState));
    }

    @Override
    public void init(State prevState) {

    }

    @Override
    public void execute() {
        LED.m_candle.setLEDs(174, 0, 255);
         
        if(RobotMap.intakeStateMachine.getCurrentState() == IntakeStateMachine.intakingState && green == 0 && RobotMap.intake.inputs.IntakeCurrent >0.5){
            LED.m_candle.setLEDs(0, 255, 0);
            time.start();
            if (time.hasElapsed(2)){
                green +=1;
            }
        }
        
        if(RobotMap.intakeStateMachine.getCurrentState() == IntakeStateMachine.outtakingState ){
            green = 0;
        }
    }

    @Override
    public void exit(State nextState) {
        LED.m_toAnimate = null;        
    }

}