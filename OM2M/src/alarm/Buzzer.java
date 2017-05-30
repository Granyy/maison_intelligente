package org.eclipse.om2m.home.alarme;

import mraa.Dir;
import mraa.Gpio;

public class Buzzer {
	
	private static Gpio gpioBuzzer;
	private boolean active;
	
	
	public Buzzer(int pin){
		init_Buzzer(pin);
		active=false;
	}
	
	public void init_Buzzer(int pin){
		gpioBuzzer = new Gpio(pin);
		gpioBuzzer.dir(Dir.DIR_OUT);
	}
	
	public void sonner_Buzzer(){
		gpioBuzzer.write(1);
	}
	
	public void stopper_Buzzer(){
		gpioBuzzer.write(0);
	}
	
	public boolean get_Active(){return active;}
	
	public void set_Active(boolean etat){
		active=etat;
	}
}