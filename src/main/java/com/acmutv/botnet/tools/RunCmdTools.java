package com.acmutv.botnet.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * This class allows the execution of system commands.
 * @author Giacomo Marciani {@literal <gmarciani@ieee.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0.0
 * @see Random
 */
public class RunCmdTools {

	/**
	 * A method that runs a command on the command line of the host and captures the result from the console
	 * @param system command
	 * @return output of the executed system command
	 */
	public static String runCmd(String command) { 
		String cmdOutput = "";
		String s = null; 

		try {
			Process p = Runtime.getRuntime().exec(command); 
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((s = stdInput.readLine()) != null)
			{ 
				cmdOutput += s+"\n";
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1); 
		}
		return cmdOutput; 
	}
}
