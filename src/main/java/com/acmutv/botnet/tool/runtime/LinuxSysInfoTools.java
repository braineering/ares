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

public class LinuxSysInfoTools {
	
	//to do
	
	/**
	 * Returns the kernel version of the system
	 * @return kernel Version
	 */
	public String getKernelVersion(){
		return RuntimeManager.runCmd("");
	}
	
	/**
	 * Returns the name of the host
	 * @return hostname
	 */
	public String getHostName(){
		return RuntimeManager.runCmd("");
	}
	
	/**
	 * Returns the username of the active user on the system
	 * @return username
	 */
	public String getUserName(){
		return RuntimeManager.runCmd("");
	}
	
	/**
	 * Returns info of the network it is connected to the system
	 * @return network info
	 */
	public String getNetworkData(){
		return RuntimeManager.runCmd("");
	}
	
	/**
	 * Returns the list of applications installed on the system
	 * @return applications list
	 */
	public String getApplications(){
		return RuntimeManager.runCmd("");
	}
	
	/**
	 * Returns the local connections of the system
	 * @return local connections list
	 */
	public String getNetworkLocations(){
		return RuntimeManager.runCmd("");
	}
	
	/**
	 * Returns the list of browsers installed on the system
	 * @return browsers list
	 */
	public String getBrowsers(){
		return RuntimeManager.runCmd("");
	}	
}