package org.eclipse.om2m.home.environment;

import mraa.Pwm;

public class ActuatorFan {
	
	
	private double m_duty ;
	private int m_port ;
	private int m_period_us ;
	private Pwm m_pwm ;
	private double m_tempTh ;
	
	public ActuatorFan(int port) {
		m_port = port ;
		m_duty = 0.0 ;
		m_period_us = 200 ;
		m_tempTh = 25 ;
		m_pwm = new Pwm(m_port) ;
		m_pwm.period_us(m_period_us) ;
	}
	
	public ActuatorFan(int port, int duty, int period) {
		m_port = port ;
		m_duty = duty ;
		m_period_us = period ;
		m_tempTh = 25 ;
		m_pwm = new Pwm(port) ;
		m_pwm.period_us(period) ;
	}
	
	/*public void init_ventilo() {
		
	}*/
	
	public void enable_ventilo() {
		m_pwm.enable(true) ;
	}

	public void setThreshold(double seuil) {
		m_tempTh = seuil ;
	}
	
	public double getThreshold() {
		return m_tempTh ;
	}
	
	public void testTemp() {
		if (Temperature.tempValue < this.m_tempTh) {
			Ventilateur.fanLevel = 0 ;
		}
		else if (Temperature.tempValue - this.m_tempTh > 5) {
			Ventilateur.fanLevel = 3 ;
		}
		else if (Temperature.tempValue - this.m_tempTh > 3) {
			Ventilateur.fanLevel = 2 ;
		}
		else if (Temperature.tempValue - this.m_tempTh > 0) {
			Ventilateur.fanLevel = 1 ;
		}
	}
	
	// Choisit le duty cycle sachant la température captée dans le sensor
	public void chooseDuty() {
		switch (Ventilateur.fanLevel) {
			case 0 : 
				m_duty = 0.0 ; 
				break ;
			case 1 : 
				m_duty = 0.65 ; 
				break ;
			case 2 : 
				m_duty = 0.80 ; 
				break ;
			case 3 : 
				m_duty = 0.95 ; 
				break ;
		    default : 
		    	m_duty = 0.0 ; 
		    	Ventilateur.fanLevel = 0 ; 
		    	break ;
		}
	}

	public void set_duty() {
		m_pwm.write((float) m_duty) ;
	}
	
}
