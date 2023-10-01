package frc.robot.subsystems.LED;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotMap;
import frc.robot.lib.interfaces.LED;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;
import frc.robot.subsystems.intake.IntakeStateMachine;

public class ConeLEDState extends State {
    public int green = 0;
    public Timer time = new Timer();

    public void build(){
        transitions.add(new Transition(() -> {
            return LED.backButton;
        }, LEDStateMachine.cubeLEDState));   
    }

    @Override
    public void init(State prevState) {
       
    }

    @Override
    public void execute() {
        LED.m_candle.setLEDs(255, 228, 0);

        if(RobotMap.intakeStateMachine.getCurrentState() == IntakeStateMachine.idleState && green == 0){
            LED.m_candle.setLEDs(0, 255, 0);
            time.start();
            if (time.hasElapsed(2)){
                green +=1;
            }
        }
        
        if(RobotMap.intakeStateMachine.getCurrentState() != IntakeStateMachine.idleState){
            green = 0;
        }
    }

    @Override
    public void exit(State nextState) {
        LED.m_toAnimate = null;
        LED.m_candle.animate(LED.m_toAnimate);
    }
}
