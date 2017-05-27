package org.eclipse.om2m.home.environment;

import mraa.Aio;

public class SensorLumi {
	
	
	private int m_port ;
	private int m_luminosite ;
	private Aio m_aio;
	
	public SensorLumi(int port) {
		m_port = port;
		m_luminosite = -1 ;
	}
	
	public void initSensor() {
		m_aio = new Aio(m_port);
	}
	
	public void readLumi() {
		final int resolution = 1023 ;
		int convADC = (int) m_aio.read();
		float Rensor = ( (float) (resolution - convADC) * 10.0f) / (float) convADC;
		
		m_luminosite = Math.round(10000.0f / (float) Math.pow((Rensor*10.0f),(4.0f/3.0f)));
	}

	public int getLumi() {
		return m_luminosite ;
	}
}
