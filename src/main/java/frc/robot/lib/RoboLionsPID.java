package frc.robot.lib;

public class RoboLionsPID {
  //PID Template Code
  //23MARCH2019 Dustin Krack and Ava Byrd
  
  public double proportionalGain = 0.0;
  public double integralGain = 0.0;
  public double derivativeGain = 0.0;
  public double integral_charge = 0.0;

  //errors outside of the zone result of the cage
  //do not allow the integrator to run
  public double upperCageLimit = 0.0;
  public double lowerCageLimit = 0.0;

  public double upperDeadbandLimit = 0.0;
  public double lowerDeadbandLimit = 0.0;

  public double maxOutput = 0.0;
  public double minOutput = 0.0;

  public double output = 0.0;
  public double error = 0.0;

  public double derivativeCalculation = 0.0;

  public double previousError = 0.0;

  public double deltaTime = 0.02;

  public double cmd = 0.0;
  public double feed = 0.0;

  public boolean deadband_active = false;

  // These are used to turn off the limiter cage and the deadband 2/15/20
  public boolean enableCage = true;
  public boolean enableDeadBand = true;

  // Called just before this Command runs the first time to set your values
  public void initialize(double _P,double _I, double _D,
                         double Cage_Limit, double Deadband, double MaxOutput) {
    proportionalGain = _P;
    integralGain = _I;
    derivativeGain = _D;
    
    upperCageLimit = +Cage_Limit;
    lowerCageLimit = -Cage_Limit;
  
    upperDeadbandLimit = +Deadband;
    lowerDeadbandLimit = -Deadband;
  
    maxOutput = +MaxOutput;
    minOutput = -MaxOutput;
  
    output = 0.0;
    error = 0.0;
  
    derivativeCalculation = 0.0;
    integral_charge = 0.0;
    previousError = 0.0;
  
    deltaTime = 0.02;

    deadband_active = false;

    enableCage = true;
    enableDeadBand = true;
    
  }

  public void initialize(double _P,double _I, double _D,
                         double Cage_Limit, double Deadband, double MaxOutput, 
                         boolean enable_Cage, boolean enable_DeadBand) { 
    // We are implementing this function to turn off the cage function and the dead band                
    initialize(_P, _I, _D, Cage_Limit, Deadband, MaxOutput);

    enableCage = enable_Cage;
    enableDeadBand = enable_DeadBand;

  }

  // Called repeatedly when this Command is scheduled to run
  // This is the function that takes in what you told the motors to do and 
  // what the motors actually did to create what we know as PID control
  public double execute(double command, double feedback) {
    cmd = command;
    feed = feedback;

    //1. calculate the error
    error = command - feedback;

    // 2. calculate change of error
    derivativeCalculation = (error - previousError) / deltaTime;
    
    // 3. Latch internal error's state for next time
    previousError = error; 

    // 4. If in deadband, PID output = 0
    if (enableDeadBand == true) {
      if ((error <= upperDeadbandLimit) && (error >= lowerDeadbandLimit)) {
        return output;
      }
    }
    
    // 5. If in cage limit, prevent integral from increasing further
    integral_charge += error*deltaTime;
    if (enableCage == true) {
      Math.min(integral_charge, upperCageLimit);
      Math.max(integral_charge, lowerCageLimit);
    }

    // 6. Calculate PID Control Equation
    output = error*proportionalGain + integral_charge*integralGain - derivativeCalculation*derivativeGain;
    if (output > maxOutput) {
      output = maxOutput;
    } else if (output < minOutput) {
      output = minOutput;
    }

    return output;
  }

  // for use if in standby and not running any power
  public void reset() {
    output = 0.0;
    error = 0.0;
  
    derivativeCalculation = 0.0;
    integral_charge = 0.0;
    previousError = 0.0;

    deadband_active = false;
  }
}