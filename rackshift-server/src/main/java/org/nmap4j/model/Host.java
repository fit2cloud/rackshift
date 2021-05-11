package org.nmap4j.model;

import java.util.ArrayList;

public class Host {

	
	private ArrayList<Port> portData = new ArrayList<Port>() ;

	public ArrayList<Port> getPortData() {
		return portData;
	}

	public void setPortData(ArrayList<Port> portData) {
		this.portData = portData;
	}
	
	public void addPortData( Port port ) {
		portData.add( port ) ;
	}
}
