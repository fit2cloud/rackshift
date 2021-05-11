package org.nmap4j.data.host;

import java.util.StringTokenizer;

/**
 * A representation of the Common Platform Enumeration (CPE) in Nmap output.
 * <p>
 * The CPE data is documented here:
 * <p>
 * http://nmap.org/book/output-formats-cpe.html
 * <p>
 * But the important bits are that it is a colon delimited string. For example:
 * <p>
 * &ltcpe&gt;cpe:/o:apple:mac_os_x:10.7&lt;/cpe&gt;
 * <p>
 * This data is variable in length. This data can be a child of service or 
 * osclass. The data is formatted as follows:
 * 
 * cpe:/<part>:<vendor>:<product>:<version>:<update>:<edition>:<language>
 * 
 * The part data is as follows:
 * <ul>
 * 	<li>a - for applications,
 *  <li>h - for hardware platforms, or
 *  <li>o - for operating systems.
 *  </ul>
 * 
 * @author jon.svede
 *
 */
public class Cpe {
	
	public final static String CPE_ATTR = "cpe" ;

	private String cpeData ;
	
	private String part = null ;
	private String vendor = null ;
	private String product = null ;
	private String version = null ;
	private String update = null ;
	private String edition = null ;
	private String language = null ;
	
	public Cpe() {}
	
	public Cpe( String cpe ) {
		cpeData = cpe.substring( 5 ) ;
		parseData() ;
	}

	private void parseData() {
		String[] tokens = cpeData.split( ":" ) ;

		int tokenNumber = 0 ;
		
		//<part>:<vendor>:<product>:<version>:<update>:<edition>:<language>
		for( String token : tokens ) {
			switch( tokenNumber ) {
			case 0:
				part = token ;
				break ;
			case 1:
				vendor = token ;
				break ;
			case 2:
				product = token ;
				break ;
			case 3:
				version = token ;
				break ;
			case 4:
				update = token ;
				break ;
			case 5:
				edition = token ;
				break ;
			case 6:
				language = token ;
				break ;
			}
			tokenNumber++ ;
		}
		
	}
	public String getCpeData() {
		return cpeData;
	}

	public void setCpeData(String cpeData) {
		this.cpeData = cpeData;
	}

	public String getPart() {
		return part;
	}

	public String getVendor() {
		return vendor;
	}

	public String getProduct() {
		return product;
	}

	public String getVersion() {
		return version;
	}

	public String getUpdate() {
		return update;
	}

	public String getEdition() {
		return edition;
	}

	public String getLanguage() {
		return language;
	}
	
	
	
}
