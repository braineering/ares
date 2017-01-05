package com.acmutv.botnet.core.analysis;

import com.acmutv.botnet.tool.runtime.RunCmdTools;

public class WindowsSysInfoTools {
	
	//to do
	
	/**
	 * Returns the kernel version of the system
	 * @return kernel Version
	 */
	public String getKernelVersion(){
		return RunCmdTools.runCmd("");
	}
	
	/**
	 * Returns the name of the host
	 * @return hostname
	 */
	public String getHostName(){
		return RunCmdTools.runCmd("");
	}
	
	/**
	 * Returns the username of the active user on the system
	 * @return username
	 */
	public String getUserName(){
		return RunCmdTools.runCmd("");
	}
	
	/**
	 * Returns info of the network it is connected to the system
	 * @return network info
	 */
	public String getNetworkData(){
		return RunCmdTools.runCmd("");
	}
	
	/**
	 * Returns the list of applications installed on the system
	 * @return applications list
	 */
	public String getApplications(){
		return RunCmdTools.runCmd("");
	}
	
	/**
	 * Returns the local connections of the system
	 * @return local connections list
	 */
	public String getNetworkLocations(){
		return RunCmdTools.runCmd("");
	}
	
	/**
	 * Returns the list of browsers installed on the system
	 * @return browsers list
	 */
	public String getBrowsers(){
		return RunCmdTools.runCmd("");
	}	
}