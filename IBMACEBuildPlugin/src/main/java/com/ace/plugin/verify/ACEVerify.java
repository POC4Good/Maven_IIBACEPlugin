package com.ace.plugin.verify;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
 * Goal which applybaroverride.
 *
 * 
 * @phase verify
 */

@Mojo(name = "verify", defaultPhase = LifecyclePhase.VERIFY, requiresProject = false)
public class ACEVerify extends AbstractMojo {
	@Parameter(required = true)
	private String barFile;

	@Parameter
	private String overrideFile;

	@Parameter
	private String manualUsingPropertyfile;

	@Parameter
	private String traceFileName;

	@Parameter
	private String applicationName;

	@Parameter
	private String libraryName;

	@Parameter
	private String outputFile;

	@Parameter
	private String directManualOverrides;

	@Parameter
	private String recursion;

	public void execute() throws MojoExecutionException {
		ACEVerify aceVerify = new ACEVerify();
		try {
			String stmt = null;
			String OS = System.getProperty("os.name").toLowerCase();
			System.out.println("Operating System: " + OS);
			System.out.println("Bar override started...");

			ArrayList<String> override = new ArrayList<String>();

			// Checking for OS and using command accordingly
			if (OS.startsWith("windows")) {
				stmt = "mqsiapplybaroverride.bat -b " + barFile;
			} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
				stmt = "mqsiapplybaroverride -b " + barFile;
			}

			// Checking if application name provided
			if (!applicationName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -k " + applicationName;
			}

			// Check if Library exists
			if (!libraryName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -y " + libraryName;
			}

			// reading property file structure and creating command for manual bar
			// override
			// to do

			if (!overrideFile.equalsIgnoreCase("Not Valid")) {
				if (manualUsingPropertyfile != null && manualUsingPropertyfile.equalsIgnoreCase("yes")) {
					override = manualOverrideUsingProperty(overrideFile, OS);
					// stmt = stmt + override;
					String temp = stmt;

					for (String tempOverride : override) {
						// Check if new output bar file provided
						if (!outputFile.equalsIgnoreCase("Not Valid")) {
							stmt = stmt + " -o " + outputFile;
						}

						// Adding trace file
						if (!traceFileName.equalsIgnoreCase("Not Valid")) {
							stmt = stmt + " -v " + traceFileName;
						}

						// Adding recursive override if provided
						if (recursion != null && recursion.equalsIgnoreCase("yes")) {
							stmt = stmt + " -r";
						}

						stmt = stmt + " " + tempOverride;

						System.out.println("executing script ..." + stmt);

						Process proctoExec = Runtime.getRuntime().exec(stmt);

						while (proctoExec.isAlive()) {

						}
						System.out.println("Process exited with value" + proctoExec.exitValue());
						stmt = temp;
					}
					return;
				}
				else {
					stmt = stmt + " -p " + overrideFile;
				}

			} 

			// If manual override is direct provided and without using property file
			if (!directManualOverrides.equalsIgnoreCase("Not Valid")) {
				if (OS.startsWith("windows")) {
					stmt = stmt + " -m \"" + directManualOverrides + "\"";
				} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
					stmt = stmt + " -m " + directManualOverrides;
				}
			}

			// Check if new output bar file provided
			if (!outputFile.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -o " + outputFile;
			}

			// Adding trace file
			if (!traceFileName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -v " + traceFileName;
			}

			// Adding recursive override if provided
			if (recursion != null && recursion.equalsIgnoreCase("yes")) {
				stmt = stmt + " -r";
			}

			System.out.println("executing script ..." + stmt);

			Process proctoExec = Runtime.getRuntime().exec(stmt);

			while (proctoExec.isAlive()) {

			}
			System.out.println("Process exited with value" + proctoExec.exitValue());

		} catch (Exception e) {
			throw new MojoExecutionException("Exception :" + e);
		} finally {
			aceVerify = null;
		}

	}

	private String manualOverrideUsingPropertyFile(String propertyFile, String OS) throws IOException {
		FileReader fr = new FileReader(new File(propertyFile));
		BufferedReader br = new BufferedReader(fr);
		String inputPropertyString;
		String libName = "";
		String OverrideString = "";
		while ((inputPropertyString = br.readLine()) != null) {

			String[] inputValues = inputPropertyString.split(",");
			// System.out.println("input value:" + inputValues.length);
			/*
			 * for (int i = 0; i < inputValues.length; i++) { System.out.println("inputs" +
			 * i + inputValues[i]);
			 * 
			 * }
			 */
			if (inputValues[0].startsWith("m") && inputValues.length == 2) {
				if (OverrideString.equalsIgnoreCase("")) {
					OverrideString = inputValues[1];
				} else {
					OverrideString = OverrideString + "," + inputValues[1];
				}

			}
			if (inputValues[0].startsWith("y") && inputValues.length == 4) {

				if (libName.equalsIgnoreCase("")) {
					libName = libName + " -y " + inputValues[1];
				}

				if (inputValues[2].startsWith("m")) {
					OverrideString = OverrideString + "," + inputValues[3];
				}

			}

		}

		if (OS.startsWith("windows")) {
			OverrideString = " -m \"" + OverrideString + "\"";
		} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
			OverrideString = " -m " + OverrideString;
		}

		System.out.println("Override string after LibName append: " + libName + OverrideString);
		return libName + OverrideString;

	}

	private ArrayList<String> manualOverrideUsingProperty(String propertyFile, String OS) throws IOException {
		FileReader fr = new FileReader(new File(propertyFile));
		BufferedReader br = new BufferedReader(fr);
		String inputPropertyString;
		String libName = "";
		String tempString = "";
		String OverrideString = "";
		// String[] override;
		// List<String> ar = new ArrayList<String>();
		ArrayList<String> overrideList = new ArrayList<String>();
		int icounter = 0;

		while ((inputPropertyString = br.readLine()) != null) {

			String[] inputValues = inputPropertyString.split(",");

			// override[icounter]= new String();
			// System.out.println("input value:" + inputValues.length);
			/*
			 * for (int i = 0; i < inputValues.length; i++) { System.out.println("inputs" +
			 * i + inputValues[i]);
			 * 
			 * }
			 */

			if (inputValues[0].startsWith("k")) {

				tempString = tempString + " -k " + inputValues[1];

				if (inputValues[2].startsWith("y")) {
					tempString = tempString + " -y " + inputValues[3];

					if (inputValues[4].startsWith("m")) {
						if (OverrideString.equalsIgnoreCase("")) {
							OverrideString = inputValues[5];
						} else
							OverrideString = OverrideString + "," + inputValues[5];
					}

				} else if (inputValues[2].startsWith("m")) {
					if (OverrideString.equalsIgnoreCase("")) {
						OverrideString = inputValues[3];
					} else
						OverrideString = OverrideString + "," + inputValues[3];

				}
			}
			if (inputValues[0].startsWith("m")) {

				OverrideString = inputValues[1];

			}
			if (inputValues[0].startsWith("y")) {
				tempString = tempString + " -y " + inputValues[1];

				if (inputValues[2].startsWith("m")) {
					if (OverrideString.equalsIgnoreCase("")) {
						OverrideString = inputValues[3];
					} else
						OverrideString = OverrideString + "," + inputValues[3];
				}

			}

			if (OS.startsWith("windows")) {
				OverrideString = tempString + " -m \"" + OverrideString + "\"";
			} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
				OverrideString = tempString + " -m " + OverrideString;
			}
			overrideList.add(OverrideString);
			icounter = icounter + 1;
			OverrideString = "";
			tempString = "";
		}

		// System.out.println("Override string after LibName append: "+libName +
		// OverrideString);
		return overrideList;

	}

}
