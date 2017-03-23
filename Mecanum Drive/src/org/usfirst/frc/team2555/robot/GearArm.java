package org.usfirst.frc.team2555.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class GearArm {
	public Solenoid gearGrabber;
	public DoubleSolenoid gearArm;
	boolean gearIsGrabbed = false;
	public GearArm(int armDownCh, int armUpCh, int gearGrabCh) {
		gearGrabber = new Solenoid(gearGrabCh);
		gearArm = new DoubleSolenoid(armDownCh, armUpCh);
	}
	
	public void GearArmDown() {
		gearArm.set(DoubleSolenoid.Value.kForward);
	}
	public void GearArmUp() {
		gearArm.set(DoubleSolenoid.Value.kReverse);
	}
	public void GearGrab() {
		gearGrabber.set(true);
	}
	public void GearRelease() {
		gearGrabber.set(false);
	}
	
	public void FullGearGrab() {
		if(!gearIsGrabbed) {
			GearRelease();
			Timer.delay(0.3);
			GearArmDown();
			Timer.delay(1.0);
			GearGrab();
			Timer.delay(0.25);
			GearArmUp();
			gearIsGrabbed = true;
		}
	}
}
