// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// THIS IS FROGBOT CODE - SMART 6932 - 2022

package frc.robot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
//import edu.wpi.first.wpilibj.Servo;
//import java.util.concurrent.TimeUnit;
//import java.util.Date; 
//import edu.wpi.first.wpilibj2.command.PrintCommand;
//import edu.wpi.first.wpilibj2.command.PrintCommand;


public class Robot extends TimedRobot {
  PWMSparkMax frontLeft = new PWMSparkMax(2);
  PWMSparkMax rearLeft = new PWMSparkMax(3);
  PWMSparkMax frontRight = new PWMSparkMax(1);
  PWMSparkMax rearRight = new PWMSparkMax(0);
  PWMSparkMax inTakeMotor = new PWMSparkMax(4);
  PWMSparkMax frontLift = new PWMSparkMax(5); //owen added
  PWMSparkMax backLift = new PWMSparkMax(6); //owen added
  PWMSparkMax shooterMotor = new PWMSparkMax(7); //owen added
  PWMSparkMax climbLeft = new PWMSparkMax(8); //owen added
  PWMSparkMax climbRight = new PWMSparkMax(9); //owen added
  DigitalInput climbLeftLimit = new DigitalInput(0); //wyatt added
  DigitalInput climbRightLimit = new DigitalInput(1); //wyatt added
  //Servo ClimbLeftStop = new Servo(0);


  

  private MecanumDrive m_robotDrive;
  private Joystick m_stick = new Joystick(2); // Driving joystick
  private Joystick s_stick = new Joystick(1); // Shooting game console

  //boolean toggleOn = false;
  boolean halfSpeedOn = false; // wyatt added
  boolean negativeSpeedOn = false; //owen added
  boolean inTakeOn = false; //owen added
  boolean LiftOn = false; //owen added
  boolean shooterOn = false; //owen added
  boolean inTakeBackwardsOn = false; //owen added
  boolean LiftBackwardsOn = false; //owen added
  boolean shooterBackwardsOn = false; //owen added
  boolean climbDownOn = false; //owen added 
  boolean climbUpOn = false; //owen added 
  double StartTime;
  


  @Override
  public void robotPeriodic() {
    
  }

  @Override
  public void robotInit() {
    // Invert the right side motors.
    // You may need to change or remove this to match your robot.
    frontRight.setInverted(true);
    rearRight.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    // Make the cameras work
    UsbCamera cam0 = CameraServer.startAutomaticCapture(0);
    UsbCamera cam1 = CameraServer.startAutomaticCapture(1);

     cam0.setResolution(500, 500);
     cam0.setFPS(30);
     cam0.setPixelFormat(PixelFormat.kMJPEG);

     cam1.setResolution(160, 120);
     cam1.setFPS(5);
     cam1.setPixelFormat(PixelFormat.kMJPEG);
    }

    


    
  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.

    // Setting wheels for drive at half speed when button 2 on m_stick is pressed (press again to go back to main speed) and default is normal speed
    if(m_stick.getRawButton(2)){
      halfSpeedOn = true;
    } 
    else {
      halfSpeedOn = false;
    }
    
    if (halfSpeedOn){
      m_robotDrive.driveCartesian(m_stick.getX()*0.5, m_stick.getY()*0.5, m_stick.getZ()*0.5, 0.0);
    }
    else {
      m_robotDrive.driveCartesian(m_stick.getX(), m_stick.getY(), m_stick.getZ(), 0.0);
    }

    //Climber code
    //Turn on motors when Up D-pad and Down D-Pad are held 
    //POV 0 = Up
    //POV 180 = Down
    
    //climb left
    //climber up
    if (s_stick.getPOV() == 0) { 
      climbLeft.set(.75);
    } 
    //climber down
    else if (s_stick.getPOV() == 180) {
      climbLeft.set(-.50);
    }
    //climber off
    else {
      climbLeft.set(0);
    }
    
   //climb right
   //climber up
    /* if (s_stick.getPOV() == 0) { 
      climbRight.set(.75);
    } 
    //climber down
    else if (s_stick.getPOV() == 180) {
        climbRight.set(-.50);
    }
    //climber down
    else {
      climbRight.set(0);
    } */
    
    // Limt switch climb
    if (s_stick.getPOV() == 0) { 
      climbLeft.set(.75);
    } 
    else if (climbLeftLimit.get() == false){
      if (s_stick.getPOV() == 180) {
          climbLeft.set(-.75);
        }
      else {
        climbLeft.set(0);
        }
      }
    else {
      climbLeft.set(0);
    }  
    
    if (s_stick.getPOV() == 0) { 
      climbRight.set(.75);
    } 
    else if (climbRightLimit.get() == false){
      if (s_stick.getPOV() == 180) {
          climbRight.set(-.75);
        }
      else {
        climbRight.set(0);
        }
      }
    else {
      climbRight.set(0);
    }  
      
  // intake only
  if (s_stick.getRawButton(1)) { 
      inTakeMotor.set(1);
    } 
  // intake reverse
  else if (s_stick.getRawButton(2)) {
      inTakeMotor.set(-1);
    }
  // shoot low
  else if (s_stick.getRawButton(3)){
    shooterMotor.set(.75);
    frontLift.set(-1);
    backLift.set(-1);
    inTakeMotor.set(1);
  }
  // shoot high
  else if (s_stick.getRawButton(4)){
    shooterMotor.set(1);
    frontLift.set(-1);
    backLift.set(-1);
    inTakeMotor.set(1);
  }
  //full reverse
  else if (s_stick.getRawButton(5)){
    shooterMotor.set(-.50);
    frontLift.set(1);
    backLift.set(1);
    inTakeMotor.set(-1);
  }
  // motors off
  else {
    shooterMotor.set(0);
    frontLift.set(0);
    backLift.set(0);
    inTakeMotor.set(0);
  }

  

  }
    // Add our periodic sensor readings, etc. here. 

  
  //Autonomous code  
  @Override
  public void autonomousPeriodic() {
    // Add out autonomous code here.
    double TimeRobot = Timer.getFPGATimestamp();
    frontRight.setInverted(true);
    rearRight.setInverted(true);
    //single ball shoot 
    //turn on intake, lift, shooter to shoot high goal
    if (TimeRobot - StartTime > 0 && TimeRobot - StartTime < .80 ) { 
      shooterMotor.set(1);
    }
    if (TimeRobot - StartTime > .80 && TimeRobot - StartTime < 6 ) { 
      frontLift.set(-1);
      backLift.set(-1);
      inTakeMotor.set(1);
    }
    //drive backward to taxi 
    if (TimeRobot - StartTime > 6.20 && TimeRobot - StartTime < 6.90) {
      m_robotDrive.driveCartesian(0, .75, 0);      
    }
    //stop
    if (TimeRobot - StartTime > 6.90 && TimeRobot - StartTime < 7.90 ) { 
      m_robotDrive.driveCartesian(0, 0, 0); 
    }
  
    
   /* //two ball shoot
    //intake preloaded ball
    if (TimeRobot - StartTime > 0 && TimeRobot - StartTime < .20) {  
      inTakeMotor.set(1);    
    }
    //drive forward out of start zone
    if (TimeRobot - StartTime > .20 && TimeRobot - StartTime < .90) {
      m_robotDrive.driveCartesian(0, -.75, 0);  
      inTakeMotor.set(1);    
    }
    //intake secound ball
    if (TimeRobot - StartTime > .90 && TimeRobot - StartTime < 1.10) {  
      inTakeMotor.set(1);    
    }
    //do a 180
    if (TimeRobot - StartTime > 1.10 && TimeRobot - StartTime < 2.40) {
      m_robotDrive.driveCartesian(0, 0, .5); 
      inTakeMotor.set(1);      
    }
    //drive to shooting position
    if (TimeRobot - StartTime > 2.55 && TimeRobot - StartTime < 2.70 ) { 
      m_robotDrive.driveCartesian(0, -.75, 0); 
    }
    //shoot
    if (TimeRobot - StartTime > 2.70 && TimeRobot - StartTime < 3.70 ) { 
      shooterMotor.set(1);
    }
    if (TimeRobot - StartTime > 3.70 && TimeRobot - StartTime < 7.70 ) { 
      shooterMotor.set(1);
      frontLift.set(-1);
      backLift.set(-1);
      inTakeMotor.set(1);
    }
    //stop
    if (TimeRobot - StartTime > 7.70 && TimeRobot - StartTime < 8.70) { 
      m_robotDrive.driveCartesian(0, 0, 0); 
    }*/
  
   
    
    
  
    


  }  


  @Override 
  public void autonomousInit() {
   StartTime = Timer.getFPGATimestamp();
  }
} // end TimedRobot
