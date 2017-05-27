package org.eclipse.om2m.home.alarme;

import mraa.Dir;

import mraa.Gpio;

public class Buzzer {
	
	private static Gpio gpioBuzzer;
	
	public void init_Buzzer(int pin){
		gpioBuzzer = new Gpio(pin);
		gpioBuzzer.dir(Dir.DIR_IN);
	}
	
	public void sonner_Buzzer(){
		gpioBuzzer.write(1);
	}
	
	public void stopper_Buzzer(){
		gpioBuzzer.write(0);
	}
}

