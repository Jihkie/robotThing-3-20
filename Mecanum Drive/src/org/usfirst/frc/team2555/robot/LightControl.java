package org.usfirst.frc.team2555.robot;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;

public class LightControl {

	public PWM redLight;
	public PWM grnLight;
	public PWM bluLight;
	
	protected double redValue = 0;
	protected double grnValue = 0;
	protected double bluValue = 0;
	
	protected int actualRed;
	protected int actualGrn;
	protected int actualBlu;
	
	
	//========================LMAO THE RAW BOUNDS ARE 0-4095 NOT 0-255 IT UNDERFLOWS AT 4096================
	public LightControl(int redCh, int grnCh, int bluCh){
		redLight = new PWM(redCh);
		grnLight = new PWM(grnCh);
		bluLight = new PWM(bluCh);
		
		redLight.setRawBounds(255, 128, 127, 126, 0);
		grnLight.setRawBounds(255, 128, 127, 126, 0);
		bluLight.setRawBounds(255, 128, 127, 126, 0);
		
		redLight.setBounds(4.0, 2.01, 2.0, 1.99, 0.0);
		grnLight.setBounds(4.0, 2.01, 2.0, 1.99, 0.0);
		bluLight.setBounds(4.0, 2.01, 2.0, 1.99, 0.0);
		
		redLight.setPeriodMultiplier(PeriodMultiplier.k1X);
		grnLight.setPeriodMultiplier(PeriodMultiplier.k1X);
		bluLight.setPeriodMultiplier(PeriodMultiplier.k1X);
	}
	
	public void SetLights(int red, int green, int blue){
		//redLight.setRaw(red);
		redLight.setRaw(red);
		grnLight.setRaw(green);
		bluLight.setRaw(blue);
	}
	
	/*
	 * This is not necessary code and is in fact dumb.
	public void SetLights(double red, double green, double blue){
		//redLight.setRaw(red);
		redLight.setSpeed(red);
		grnLight.setSpeed(green);
		bluLight.setSpeed(blue);
		
	}*/
	
	public void RefreshLights(){
		redLight.setRaw(actualRed);
		grnLight.setRaw(actualGrn);
		bluLight.setRaw(actualBlu);
	}
	
	public void SetRed(int red){
		redLight.setRaw(red);
	}
	
	public void SetGreen(int green){
		grnLight.setRaw(green);
	}
	
	public void SetBlue(int blue){
		bluLight.setRaw(blue);
	}
	
	
	/*public void ScaleUpRed(){
		redValue*=1.05;
		if (redValue >= 4095){
			redValue = 4095;
		}
	}*/
	public void ScaleUpRed(){
		if ((redValue+1)*(redValue+1)*5 < 4095){
			++redValue;
		}
		actualRed = (int) (redValue*redValue*5);
	}
	
	public void ScaleUpGreen(){
		if ((grnValue+1)*(grnValue+1)*5 < 4095){
			++grnValue;
		}
		actualGrn = (int) (grnValue*grnValue*5);
	}
	
	public void ScaleUpBlue(){
		if ((bluValue+1)*(bluValue+1)*5 < 4095){
			++bluValue;
		}
		actualBlu = (int) (bluValue*bluValue*5);
	}
	
	
	/*public void ScaleDownRed(){
		redValue/=1.05;
		if (redValue >= 4095){
			redValue = 4095;
		}
	}*/
	public void ScaleDownRed(){
		if (Math.copySign((redValue-1)*(redValue-1)*5, redValue) >= 0){
			--redValue;
		}
		actualRed = (int) (redValue*redValue*5);
		if (actualBlu <= 0) {
			actualBlu = 0;
		}
	}
	
	public void ScaleDownGreen(){
		if (Math.copySign((grnValue-1)*(grnValue-1)*5, grnValue) >= 0){
			--grnValue;
		}
		actualGrn = (int) (grnValue*grnValue*5);
		if (actualGrn <= 0) {
			actualGrn = 0;
		}
	}
	
	public void ScaleDownBlue(){
		if (Math.copySign((bluValue-1)*(bluValue-1)*5, bluValue) >= 0){
			--bluValue;
		}
		actualBlu = (int) (bluValue*bluValue*5);
		if (actualBlu <= 0) {
			actualBlu = 0;
		}
	}
	
	
	/**
	 * Slowly fade a color on the LEDs
	 * @param start
	 * @param end
	 * @return
	 */
	/*public int FadeColor(int start, int end){
		double value = start;
		
		while(value != end){
			value += (end-start)/1000;
			return (int) value;
		}
		return (int) value;
		
		
		
	}*/
	
	public void FadeColor(int end){
		
	}
}
