package org.eclipse.om2m.home.environment;

import mraa.Aio;

public class SensorTemp {
	
	private Aio m_aio ;
	private int m_port ;
	private double m_temp ;
	
	private final double R0=100000.0 ;
	private final double B=4275.0 ;
	
	
	/**
	 * Constructeur avec port par defaut
	 */
	public SensorTemp() {
		m_port = 0 ;
	}
	
	/**
	 * Constructeur avec port en argument
	 * @param port
	 */
	public SensorTemp(int port) {
		m_port = port ;
	}
	
	/**
	 * setPort
	 * @param port
	 */
	public void setPort(int port) {
		m_port = port ;
	}
	
	/**
	 * getPort
	 * @return m_port
	 */
	public int getPort() {
		return m_port ;
	}
	
	/**
	 * getTemp
	 * @return m_temp
	 */
	public double getTemp() {
		return m_temp ;
	}
	

	/**
	 * initSensor
	 */
	public void initSensor() {
		m_aio = new Aio(m_port);
	}
	
	

	/**
	 * readTemp
	 * Lit la température et la convertit en Celcius, puis la stocke dans l'attribut m_temp
	 */
	public void readTemp() {
		double R = 1024.0/((double)m_aio.read())-1.0;
		R = R0*R ;
		m_temp = 1.0/(Math.log(R/R0)/B+1/298.15)-273.15 ; 
		m_temp = (double) Math.round(m_temp * 100) / 100;
	}

}
