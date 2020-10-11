package com.ace.plugin.verify;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal which is used for mqsireadbar.
 *
 * 
 * @phase verify
 */

@Mojo(name = "verify", requiresProject = false)
public class ACEVerify extends AbstractMojo {
	@Parameter(required = true)
	private String barFile;

	@Parameter
	private String traceFileName;

	@Parameter
	private String recursion;
	
	@Parameter
	private String overrideFile;
	
	@Parameter
	private String readBarOutputFile;
	
	
	public void execute() throws MojoExecutionException {
		ACEVerify aceVerify = new ACEVerify();
		try {
			File readBarOutput = new File(readBarOutputFile);
			File propertyFile = new File(overrideFile);
			String stmt = null;
			String OS = System.getProperty("os.name").toLowerCase();
			System.out.println("Operating System: " + OS);
			System.out.println("Read bar started...");

			ArrayList<String> override = new ArrayList<String>();

			// Checking for OS and using command accordingly
			if (OS.startsWith("windows")) {
				stmt = "mqsireadbar.bat -b " + barFile;
			} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
				stmt = "mqsireadbar -b " + barFile;
			}

			// Adding trace file
			if (!traceFileName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -v " + traceFileName;
			}

			// Adding recursive override if provided
			if (recursion != null && recursion.equalsIgnoreCase("yes")) {
				stmt = stmt + " -r";
			}
			//writting output of mqsireadbar file to outputfile provided
			stmt =stmt +" > "+readBarOutputFile;
			System.out.println("executing script ..." + stmt);

			//executing script
			Process proctoExec = Runtime.getRuntime().exec(stmt);

			while (proctoExec.isAlive()) {

			}
			System.out.println("Process exited with value" + proctoExec.exitValue());
			
			//if process execution is successful then compare property file with mqsireadbar command output file.
			if (proctoExec.exitValue() == 0) {
				String line = null;
				String readout = null;
				String propertyToCheck = null;
				
				//reading property file
				FileReader fReader = new FileReader(overrideFile);
				BufferedReader fileBuff = new BufferedReader(fReader);
				
				//reading mqsireadbar output file in a string
				readout = FileUtils.readFileToString(readBarOutput, "UTF-8");
				
				//Reading propertyfile line by line and checking if property is present mqsireadbar output file (checking in deployment descriptor)
	            while ((line = fileBuff.readLine()) != null) {
	            	int index = line.lastIndexOf("=");
	            	String temp = line.substring(0,index);
	            	System.out.println("Output="+temp);
	            	
	            	//Check if custom property file provided
	            	if (temp.contains(",")) {
	            		String[] inputValues = temp.split(",");
	            		propertyToCheck = inputValues[inputValues.length -1];
					} else {
						propertyToCheck = temp;
					}
	            	System.out.println("propertyToCheck: "+propertyToCheck);
	            	//Check if property exists in deployment descriptor
	            	Boolean exists = readout.contains(propertyToCheck);
	            	
	            	//If property does not exists in deployment descriptor then show error message
	            	if (!exists) {
						System.out.println("Property does not exists in deployment descriptor -"+propertyToCheck);
					}
	            	propertyToCheck = null;
	            }
	            //closing FileReader and BufferedReader
	            fileBuff.close();
	            fReader.close();
			}
			

		} catch (Exception e) {
			throw new MojoExecutionException("Exception :" + e);
		} finally {
			aceVerify = null;
		}

	}
}
