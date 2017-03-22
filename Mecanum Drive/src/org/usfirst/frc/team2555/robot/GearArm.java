package org.usfirst.frc.team2555.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class GearArm {
	public Solenoid gearGrabber;
	public DoubleSolenoid gearArm;
	
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
	
	
}
