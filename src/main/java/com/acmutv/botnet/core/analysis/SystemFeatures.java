/*
  The MIT License (MIT)

  Copyright (c) 2016 Giacomo Marciani and Michele Porretta

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

package com.acmutv.botnet.core.analysis;

import com.acmutv.botnet.core.exception.BotAnalysisException;
import com.acmutv.botnet.tool.runtime.LinuxSysInfoTools;
import com.acmutv.botnet.tool.runtime.MacOsxSysInfoTools;
import com.acmutv.botnet.tool.runtime.WindowsSysInfoTools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class realizes the model of system information.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see NetworkFeatures
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemFeatures {

	/**
	 * The Operating System's name.
	 */
	private String OsName;

	/**
	 * The Architecture's name.
	 */
	private String OsArch;

	/**
	 * The Operating System's version name.
	 */
	private String OsVersion;

	/**
	 * The kernel's name.
	 */
	private String KernelVersion;

	/**
	 * The Host's name.
	 */
	private String HostName;

	/**
	 * The User's name.
	 */
	private String UserName;

	/**
	 * Installed browsers on the host
	 */
	private String Browsers;
	
	/**
	 * Returns local system features.
	 * @return local system features.
	 * @throws BotAnalysisException when some system features cannot be determined.
	 */
	public static SystemFeatures getLocal() throws BotAnalysisException {
		String osName = System.getProperty ("os.name");
		String osArch = null;
		String osVersion = null;
		String kernelVersion = null;
		String browsers = null;
		String hostName = null;
		String userName = null;
		
		if(osName.toUpperCase().equals("MAC OS X")){
			MacOsxSysInfoTools mac = new MacOsxSysInfoTools();
			osArch = System.getProperty ("os.arch"); 
			osVersion = System.getProperty ("os.version");
			kernelVersion   = mac.getKernelVersion();
			browsers 	  	  = mac.getBrowsers();
			hostName 	  	  = mac.getHostName();
			userName 	 	  = mac.getUserName();
		}else if (osName.toUpperCase().equals("LINUX")){
			LinuxSysInfoTools linux = new LinuxSysInfoTools();
			osArch = System.getProperty ("os.arch"); 
			osVersion = System.getProperty ("os.version");
			kernelVersion   = linux.getKernelVersion();
			browsers 	  	  = linux.getBrowsers();
			hostName 	  	  = linux.getHostName();
			userName 	 	  = linux.getUserName();  
		}
		else if (osName.toUpperCase().equals("WINDOWS")){
			WindowsSysInfoTools win = new WindowsSysInfoTools();
			osArch = System.getProperty ("os.arch"); 
			osVersion = System.getProperty ("os.version");
			kernelVersion   = win.getKernelVersion();
			browsers 	  	  = "";
			hostName 	  	  = win.getHostName();
			userName 	 	  = win.getUserName();
		}else{
			osArch = "Not Found"; 
			osVersion = "Not Found";
			kernelVersion   = "Not Found";
			browsers 	  	  = "Not Found";
			hostName 	  	  = "Not Found";
			userName 	 	  = "Not Found"; 
		}
		
		return new SystemFeatures(osName, osArch, osVersion, kernelVersion, hostName, userName, browsers);
	}
}