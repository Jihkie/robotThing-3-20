package org.usfirst.frc.team2555.robot;

import com.ctre.CANTalon;
//import edu.wpi.first.wpilibj.CameraServer;
/*import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.PWM;*/
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.vision.CameraServer;
import org.usfirst.frc.team2555.robot.RobotDrive;
import org.usfirst.frc.team2555.robot.RobotDrive.MotorType;
//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import edu.wpi.first.wpilibj.CANSpeedController;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team2555.robot.GearArm;
import org.usfirst.frc.team2555.robot.LightControl;

//import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.Encoder;
//import org.usfirst.frc.team2555.robot.MakeSound;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends SampleRobot {
	RobotDrive robotDrive;

	// Channels for the wheels
	final int kFrontLeftChannel = 3;
	final int kRearLeftChannel = 4;
	final int kFrontRightChannel = 2;
	final int kRearRightChannel = 1;
	Spark throwLeft = new Spark(4);
	Spark throwRight = new Spark(5);
	Talon ballPaddle = new Talon(6);
	Talon climbRight = new Talon(7);
	Talon sweeper = new Talon(8);
	int cameraNum = 0;
	int camFront;
	int camRight;
	int camBack;
	int camLeft;
	int currentCam;
	boolean runningSweeper = true;
	double valueOut;
	Image frame;
	CameraServer server;
	StringBuilder encOut = new StringBuilder();
	/*PWM buzzer = new PWM(9);
	Encoder encBR = new Encoder(0,1);
	Encoder encFR = new Encoder(2,3);
	Encoder encFL = new Encoder(4,5);
	Encoder encBL = new Encoder(6,7);*/
	/*bRightPID = new PIDSpeedController(encBR);
	fRightPID = new PIDSpeedController(encFR);
	fLeftPID = new PIDSpeedController(encFL);
	bLeftPID = new PIDSpeedController(encBL);*/
	// Obsoleted by GearArm class    Solenoid gearGripper = new Solenoid(2);
	//DoubleSolenoid gearArm = new DoubleSolenoid(0,1);
	GearArm gearArm = new GearArm(0, 1, 2); 
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	double goalAngle = 0.0;
	boolean cameraToggle = false;
	boolean sweeperToggle = false;
	LightControl LED = new LightControl(0, 1, 2);
	
	
	// The channel on the driver station that the joystick is connected to
	final int kJoystick1Channel = 0;
	final int kJoystick2Channel = 1;

	Joystick stick1 = new Joystick(kJoystick1Channel);
	//Joystick stick2 = new Joystick(kJoystick2Channel);

	public Robot() {
		robotDrive = new RobotDrive(kFrontLeftChannel, kRearLeftChannel, kFrontRightChannel, kRearRightChannel);
		robotDrive.setInvertedMotor(MotorType.kFrontLeft, true); // invert the
																	// left side
																	// motors
		robotDrive.setInvertedMotor(MotorType.kRearLeft, true); // you may need
																// to change or
																// remove this
																// to match your
																// robot
		robotDrive.setExpiration(0.1);
	}
	
	/*public void MakeSomeSound(double frequency, double volume) {
		buzzer.setBounds(200, deadbandMax, center, deadbandMin, min);setBounds(1, 1, 1, 1, 1);
	}*/
	
	public void Throwing(boolean isThrowing) {
		if (isThrowing){
			throwLeft.set(-0.6);
			throwRight.set(0.6);
		} else if (!isThrowing){
			throwLeft.set(0.0);
			throwRight.set(0.0);
		}
	}
	
	public void BallPaddle(boolean runPaddle, boolean runPaddleReverse) {
		if (runPaddle) {
			ballPaddle.set(-0.6);
		} else if (runPaddleReverse) {
			ballPaddle.set(0.6);
		} else {
			ballPaddle.set(0);
		}
	}
	
	public void Climbing(boolean upState, boolean downState) {
		if (upState && !downState) {
			climbRight.set(1.0);
		} else if (!upState && downState) {
			climbRight.set(-1.0);
		} else {
			climbRight.set(0.0);
		}
		
	}
	
	//obsoleted by CameraAbsolute
	public void ChangeCamCW() {
		cameraNum = ( cameraNum + 1 ) % 4;
		CameraRefresh();
	}
	
	public void ChangeCamCCW() {
		cameraNum = ( cameraNum - 1 ) % 4;
		CameraRefresh();
	}
	
	public void CameraRefresh() {
		switch(cameraNum){
		case 0 :
			NIVision.IMAQdxStopAcquisition(currentCam);
	        currentCam = camFront;
	        NIVision.IMAQdxConfigureGrab(currentCam);
	        NIVision.IMAQdxStartAcquisition(currentCam);
			break;
		case 1 :
			NIVision.IMAQdxStopAcquisition(currentCam);
	        currentCam = camRight;
	        NIVision.IMAQdxConfigureGrab(currentCam);
	        NIVision.IMAQdxStartAcquisition(currentCam);
			break;
		case 2 :
			NIVision.IMAQdxStopAcquisition(currentCam);
	        currentCam = camBack;
	        NIVision.IMAQdxConfigureGrab(currentCam);
	        NIVision.IMAQdxStartAcquisition(currentCam);
			break;
		case 3 :
			NIVision.IMAQdxStopAcquisition(currentCam);
	        currentCam = camLeft;
	        NIVision.IMAQdxConfigureGrab(currentCam);
	        NIVision.IMAQdxStartAcquisition(currentCam);
			break;
		}
	}
	
	//obsoleted by CameraAbsolute
	public void CameraRotational() {
		/*if (stick1.getRawButton(4) && !button4){
			ChangeCamCCW();
			//cams.startAutomaticCapture(cameraNum);
		}
		if (stick1.getRawButton(5) && !button5){
			ChangeCamCW();
			//cams.startAutomaticCapture(cameraNum);
		}*/
		if (cameraToggle && (stick1.getRawButton(5) && !stick1.getRawButton(6))) {  // Only execute once per Button push
			cameraToggle = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
			ChangeCamCCW();
		} else if (cameraToggle && (!stick1.getRawButton(5) && stick1.getRawButton(6))){
			cameraToggle = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
			ChangeCamCW();
		} else if((!stick1.getRawButton(5) && !stick1.getRawButton(6))) { 
			cameraToggle = true; // Button has been released, so this allows a re-press to activate the code above.
		}
	}
	
	public void CameraAbsolute(int whichCamera) {
		/*if (toCamFront && !toCamLeft && !toCamBack && !toCamRight){
			cameraNum = 0;
			CameraRefresh();
		} else if (!toCamFront && toCamLeft && !toCamBack && !toCamRight){
			cameraNum = 1;
			CameraRefresh();
		} else if (!toCamFront && !toCamLeft && toCamBack && !toCamRight){
			cameraNum = 2;
			CameraRefresh();
		} else if (!toCamFront && !toCamLeft && !toCamBack && toCamRight){
			cameraNum = 3;
			CameraRefresh();
		}*/
		
		switch (whichCamera){
		case 0 :
			cameraNum = 0;
			CameraRefresh();
			break;
		case 90 :
			cameraNum = 1;
			CameraRefresh();
			break;
		case 180 :
			cameraNum = 2;
			CameraRefresh();
			break;
		case 270 :
			cameraNum = 3;
			CameraRefresh();
			break;
		}
		
		//CameraRefresh();
	}
	
	public double ReturnSomePower(double valueIn) {
		if (valueIn > 0.2) {
			valueOut = 0.4;
		} else if (valueIn < -0.2) {
			valueOut = -0.4;
		} else {
			valueOut = 0.0;
		}
		return valueOut;
	}
	
	//obsoleted by GearArm class
	/*public void GripGears(boolean gripTheGear) {
		gearGripper.set(gripTheGear);
	}*/
	
	/*public void MoveGearArm(boolean armDown, boolean armUp){
		if (armDown && !armUp){
			gearArm.set(DoubleSolenoid.Value.kForward);
		} else if (armUp && !armDown){
			gearArm.set(DoubleSolenoid.Value.kReverse);
		} else {
			gearArm.set(DoubleSolenoid.Value.kOff);
		}
	}*/
	
	public void DoTheToggleSweeper() {
		if (sweeperToggle && stick1.getRawButton(11)) {  // Only execute once per Button push
			  sweeperToggle = false;  // Prevents this section of code from being called again until the Button is released and re-pressed
			  if (runningSweeper) {  // Decide which way to set the motor this time through (or use this as a motor value instead)
			    runningSweeper = false;
			    
			  } else {
			    runningSweeper = true;
			    
			  }
			} else if(!stick1.getRawButton(11)) { 
			    sweeperToggle = true; // Button has been released, so this allows a re-press to activate the code above.
		}
	}
	
	public void ToggleSweeper(boolean sweepOn, boolean sweepOff) {
		if (sweepOn && !sweepOff) {
			runningSweeper = true;
		} else if (sweepOff && !sweepOn) {
			runningSweeper = false;
		}
	}
	
	public void SweepBalls() {
		if (runningSweeper) {
			sweeper.set(1.0);
		} else {
			sweeper.set(0.0);
		}
	}
	
	public int BoolToInt(boolean input) {
		if (input) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public double BoolToDouble(boolean input) {
		if (input) {
			return 1;
		} else {
			return 0;
		}
	}
	
	//public double BoolToDouble(boolean convertThis) {}
	
	public double RotateRobot(double joyRotate, double gyroAngle) {
		goalAngle += 5 * SpeedPaddle(joyRotate);
		double adjustment = (goalAngle - gyroAngle) * 0.05;
		if (-1 <= adjustment && adjustment <= 1) {
			return adjustment;
		} else if (adjustment < -1) {
			return -1.0;
		} else if (adjustment > 1) {
			return 1.0;
		} else {
			return 0.0;
		}
	}
	
	public double DeadzoneAdjustment(double joyInput, double deadzoneRadius) {
		return Math.copySign(Math.max(Math.abs(joyInput)-deadzoneRadius,0), joyInput) / (1-deadzoneRadius);
	}
	
	public double SpeedPaddle(double whatYouArePuttingIn) {
		return DeadzoneAdjustment(whatYouArePuttingIn, 0.1) * (1 - 0.75 * stick1.getRawAxis(3));
	}
	
	/*public void AddEncoderInfo(CANTalon thisMotor, StringBuilder stringIn){
		stringIn.append("\tout:");
		stringIn.append(thisMotor.getOutputVoltage() / thisMotor.getBusVoltage());
		stringIn.append("\tspd:");
		stringIn.append(thisMotor.getSpeed());
		stringIn.append("\terr:");
		stringIn.append(thisMotor.getClosedLoopError());
		System.out.println(stringIn.toString());
		stringIn.setLength(0);
	}*/
	
	@Override
	public void robotInit() {
		server = CameraServer.getInstance();
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		camFront = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		camRight = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		camBack = NIVision.IMAQdxOpenCamera("cam2", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		camLeft = NIVision.IMAQdxOpenCamera("cam3", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		currentCam = camFront;
		NIVision.IMAQdxConfigureGrab(currentCam);
		NIVision.IMAQdxStartAcquisition(currentCam);
		//CameraServer.getInstance().startAutomaticCapture("cam0");;
		
		
		
	}
	
	/**
	 * Runs the motors with Mecanum drive.
	 */
	@Override
	public void operatorControl() {
		robotDrive.setSafetyEnabled(false);
		gyro.reset();
		//final CameraServer cams = CameraServer.getInstance();
		//cams.startAutomaticCapture("cam0");
		//================we changed this from true because some people on the internet told us to
		//buzzer.setBounds(2.037, 1.539, 1.513, 1.487, .989);
		while (isOperatorControl() && isEnabled()) {

			// Use the joystick X axis for lateral movement, Y axis for forward
			// movement, and Z axis for rotation.
			// This sample does not use field-oriented drive, so the gyro input
			// is set to zero.
			
			switch(cameraNum){
			case 0 :
				//robotDrive.mecanumDrive_Cartesian(SpeedPaddle(stick1.getX()) * 0.9, - SpeedPaddle(stick1.getY()) * 0.9, - RotateRobot(stick1.getZ(), gyro.getAngle()) * 0.9, 0, !stick1.getRawButton(6));
				robotDrive.mecanumDrive_Cartesian(SpeedPaddle(stick1.getX()) * 0.9, - SpeedPaddle(stick1.getY()) * 0.9, - SpeedPaddle(stick1.getZ()) * 0.9, 0, !stick1.getRawButton(7));
				//robotDrive.mecanumDrive_Cartesian(ReturnSomePower(stick.getX()), ReturnSomePower(-stick.getY()), ReturnSomePower(stick.getZ()), 0);
				break;
			case 1 :
				robotDrive.mecanumDrive_Cartesian(- SpeedPaddle(stick1.getX()) * 0.9, SpeedPaddle(stick1.getY()) * 0.9, - SpeedPaddle(stick1.getZ()) * 0.9, 0, !stick1.getRawButton(7));
				//robotDrive.mecanumDrive_Cartesian(ReturnSomePower(-stick.getY()), ReturnSomePower(-stick.getX()), ReturnSomePower(stick.getZ()), 0);
				break;
			case 2 :
				robotDrive.mecanumDrive_Cartesian(- SpeedPaddle(stick1.getX()) * 0.9, SpeedPaddle(stick1.getY()) * 0.9, - SpeedPaddle(stick1.getZ()) * 0.9, 0, !stick1.getRawButton(7));
				//robotDrive.mecanumDrive_Cartesian(ReturnSomePower(-stick.getX()), ReturnSomePower(stick.getY()), ReturnSomePower(stick.getZ()), 0);
				break;
			case 3 :
				robotDrive.mecanumDrive_Cartesian(- SpeedPaddle(stick1.getX()) * 0.9, SpeedPaddle(stick1.getY()) * 0.9, - SpeedPaddle(stick1.getZ()) * 0.9, 0, !stick1.getRawButton(7));
				//robotDrive.mecanumDrive_Cartesian(ReturnSomePower(stick.getY()), ReturnSomePower(stick.getX()), ReturnSomePower(stick.getZ()), 0);
				break;
			}
			
			robotDrive.AddEncoderInfo(robotDrive.m_frontLeftMotor, encOut);
			//System.out.println(robotDrive.m_frontLeftMotor.getClosedLoopError());
			//robotDrive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), stick.getZ(), 0);
			
			//CameraRotational();
			CameraAbsolute(stick1.getPOV(0));
			
			NIVision.IMAQdxGrab(currentCam, frame, 1);
			server.setImage(frame);
			/*Timer.delay(0.01);
			button4 = stick.getRawButton(4);
			button5 = stick.getRawButton(5);
			*/
			//Double toneValueThing1 = Math.floor((stick1.getZ() + 1) * 128);
			//Double toneValueThing2 = Math.floor((stick2.getZ() + 1) * 128);
			//Double toneValueThing3 = Math.floor((stick1.getX() + 1) * 128);
			//Double toneValueThing4 = Math.floor((stick2.getX() + 1) * 128);
			//Double toneValueThing5 = Math.floor((stick1.getY() + 1) * 128);
			//buzzer.setRaw(toneValueThing1.intValue());
			//buzzer.setBounds(2.037*0.2, 1.539*0.2, 1.513*0.2, 1.487*0.2, .989*0.2);
			
			Climbing(false, stick1.getRawButton(11));
			Throwing(stick1.getRawButton(1));
			BallPaddle(stick1.getRawButton(9),stick1.getRawButton(10));
			if (stick1.getRawButton(2)) {
				sweeper.set(0.5);
			} else {
				sweeper.set(0.0);
			}
			
			/*if(stick1.getRawButton(3)) {
				gearArm.FullGearGrab();
			}
			if(stick1.getRawButton(5)) {
				gearArm.GearRelease();
			}*/
			
			if(stick1.getRawButton(5)) {
				gearArm.GearArmDown();
			}
			if(stick1.getRawButton(4)) {
				gearArm.GearArmUp();
			}
			if(stick1.getRawButton(3)) {
				gearArm.GearGrab();
			}
			if(stick1.getRawButton(6)) {
				gearArm.GearRelease();
			}
			//LED.SetLights(BoolToInt(stick1.getRawButton(7))*255, BoolToInt(stick1.getRawButton(9))*255, BoolToInt(stick1.getRawButton(11))*255);
			
			/*if(stick1.getRawButton(7)){
				//LED.SetLights(0.0, 0.0, 0.0);
				//LED.SetLights(0, 0, 0);
				//LED.SetRed(0);
				LED.ScaleDownRed();
			}
			if(stick1.getRawButton(8)){
				//LED.SetLights(-1.0, -1.0, -1.0);
				//LED.SetLights(4095, 4095, 4095);
				//LED.SetRed(4095);
				LED.ScaleUpRed();
			}
			if(stick1.getRawButton(9)){
				//LED.SetLights(-1.0, -1.0, -1.0);
				//LED.SetLights(4096, 4096, 4096);
				//LED.SetGreen(0);
				LED.ScaleDownGreen();
			}
			if(stick1.getRawButton(10)){
				//LED.SetLights(-1.0, -1.0, -1.0);
				//LED.SetLights(4250, 4250, 4250);
				//LED.SetGreen(4095);
				LED.ScaleUpGreen();
			}
			if(stick1.getRawButton(11)){
				//LED.SetLights(-1.0, -1.0, -1.0);
				//LED.SetLights(4500, 4500, 4500);
				//LED.SetBlue(0);
				LED.ScaleDownBlue();
			}
			if(stick1.getRawButton(12)){
				//LED.SetLights(-1.0, -1.0, -1.0);
				//LED.SetLights(10000, 10000, 10000);
				//LED.SetBlue(4095);
				LED.ScaleUpBlue();
			}
			LED.RefreshLights();*/
			//System.out.println(LED.redLight.getRawBounds().toString());
			
			Timer.delay(0.005); // wait 5ms to avoid hogging CPU cycles
		}
	}
	@Override
	public void autonomous() {
		//This is the Autonomous code. write something here when we get to that point.
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 3000) {
			robotDrive.mecanumDrive_Cartesian(0.0, 0.5, 0, 0, false);
		}
		//robotDrive.mecanumDrive_Cartesian(0, 0, 0, 0, false);
	}
}
