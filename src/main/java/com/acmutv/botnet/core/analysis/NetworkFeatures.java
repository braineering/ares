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
import com.acmutv.botnet.tool.runtime.WindowsSysInfoTools;

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
	 * Network Statistics
	 */
	private String NetworkStatistics;
	
	/**
	 * Tcp Connections
	 */
	//private String TcpConnections;
	
	/**
	 * Udp Connections
	 */
	//private String UdpConnections;
	
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
		String networkStatistics = null;
		//String tcpConnections = null;
		//String udpConnections = null;

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
			networkStatistics = osx.getAllNetworkStatistics();
			//tcpConnections = osx.getTCPCurrentConnections();
			//udpConnections = osx.getUDPCurrentConnections();
			
		}else if (osName.toUpperCase().equals("LINUX")){
			LinuxSysInfoTools linux = new LinuxSysInfoTools();
			currentNetworkInformation = linux.getCurrentNetworkInformation();
			networkStatistics = linux.getAllNetworkStatistics();
			//tcpConnections = linux.getTCPCurrentConnections();
			//udpConnections = linux.getUDPCurrentConnections();

		}
		else if (osName.toUpperCase().equals("WINDOWS")){
			WindowsSysInfoTools win = new WindowsSysInfoTools();
			currentNetworkInformation = win.getCurrentNetworkInformation();
			networkStatistics = win.getAllNetworkStatistics();
			//tcpConnections = win.getTCPCurrentConnections();
			//udpConnections = win.getUDPCurrentConnections();
		}else{
			currentNetworkInformation = "Error: Not Found";
			networkStatistics = "Error: Not Found";
			//tcpConnections = "Error: Not Found";
			//udpConnections = "Error: Not Found";
		}

		//return new NetworkFeatures(ip, mac, currentNetworkInformation, networkStatistics, tcpConnections, udpConnections);
		return new NetworkFeatures(ip, mac, currentNetworkInformation, networkStatistics);
	}
}
