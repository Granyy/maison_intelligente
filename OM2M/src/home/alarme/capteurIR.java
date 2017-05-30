package org.eclipse.om2m.home.alarme;
import mraa.Dir;
import mraa.Gpio;

public class capteurIR {
	private Gpio gpioCapteurIR;
	
	//constructeur
	public capteurIR(int pin){
		init_Capteur(pin);
	}
	
	public void init_Capteur(int pin){
		gpioCapteurIR = new Gpio(pin);
		gpioCapteurIR.dir(Dir.DIR_IN);
	}
	
	
	public Gpio get_Gpio(){
		return gpioCapteurIR;
	}
	
	// Le capteur n'est actif que si personne n'est a la maison/tout le monde est couche
	 

	
	
	public boolean get_Capteur(){
		
		if (gpioCapteurIR.read()==1){
			return true;
		}else{
			return false;
		}
	}
	
	//Fonction de lecture a utiliser si capteur de luminosite 
	//public boolean isOpen(){
		/*
		 * etalonnage du capteur + algo seuil
		 */
		//return 	open;
	//}
	
}
