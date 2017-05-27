package org.eclipse.om2m.home.alarme;
import mraa.Dir;
import mraa.Gpio;

public class capteurIR {
	private int etatCapteur;
	private Gpio gpioCapteurIR;
	private boolean active; 
	private boolean open; //�tat de la fenetre
	
	//constructeur
	public capteurIR(){
	}
	
	public void init_Capteur(int pin){
		gpioCapteurIR = new Gpio(pin);
		gpioCapteurIR.dir(Dir.DIR_IN);
	}
	
	
	public Gpio get_Gpio(){
		return gpioCapteurIR;
	}
	
	// Le capteur n'est actif que si personne n'est � la maison/tout le monde est couch�
	 
	public boolean get_Active(){return active;}
	
	public void set_Active(boolean etat){
		active=etat;
	}
	
	
	public boolean get_Capteur(){
		
		if (gpioCapteurIR.read()==1){
			return true;
		}else{
			return false;
		}
	}
	
	//Fonction de lecture � utiliser si capteur de luminosit� 
	//public boolean isOpen(){
		/*
		 * �talonnage du capteur + algo seuil
		 */
		//return 	open;
	//}
	
}
