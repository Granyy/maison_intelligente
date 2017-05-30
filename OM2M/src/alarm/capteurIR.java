package org.eclipse.om2m.home.alarme;
import mraa.Dir;
import mraa.Gpio;

public class capteurIR {
	private int etatCapteur;
	private Gpio gpioCapteurIR;
	private boolean open; //état de la fenetre
	
	//constructeur
	public capteurIR(int pin){
		init_Capteur(pin);
		open=false;
	}
	
	public void init_Capteur(int pin){
		gpioCapteurIR = new Gpio(pin);
		gpioCapteurIR.dir(Dir.DIR_IN);
	}
	
	
	public Gpio get_Gpio(){
		return gpioCapteurIR;
	}
	
	// Le capteur n'est actif que si personne n'est à la maison/tout le monde est couché
	 

	
	
	public boolean get_Capteur(){
		
		if (gpioCapteurIR.read()==1){
			return true;
		}else{
			return false;
		}
	}
	
	//Fonction de lecture à utiliser si capteur de luminosité 
	//public boolean isOpen(){
		/*
		 * étalonnage du capteur + algo seuil
		 */
		//return 	open;
	//}
	
}
