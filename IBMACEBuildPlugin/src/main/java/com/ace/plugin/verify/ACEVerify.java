package com.ace.plugin.verify;

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
	private String propertyFile;

	@Parameter
	private String traceFileName;

	@Parameter
	private String applicationName;

	@Parameter
	private String libraryName;

	@Parameter
	private String outputFile;

	@Parameter
	private String manualOverrides;

	@Parameter
	private String recursion;

	public void execute() throws MojoExecutionException {
		ACEVerify aceVerify = new ACEVerify();
		try {
			String stmt = null;
			String OS = System.getProperty("os.name").toLowerCase();
			System.out.println("Operating System: " + OS);
			System.out.println("Bar override started...");

			if (OS.startsWith("windows")) {
				stmt = "mqsiapplybaroverride.bat -b " + barFile;
			} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
				stmt = "mqsiapplybaroverride -b " + barFile;
			}
			
			//reading NBS property file structure and creating commnd for manual bar override
			//to do
			
			
			
			if (!applicationName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -k " + applicationName;
			}
			if (!libraryName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -y " + libraryName;
			}

			if (!manualOverrides.equalsIgnoreCase("Not Valid")) {
				if (OS.startsWith("windows")) {
					stmt = stmt + " -m \"" + manualOverrides + "\"";
				} else if (OS.startsWith("unix") || OS.startsWith("linux")) {
					stmt = stmt + " -m " + manualOverrides;
				}
			}

			if (!propertyFile.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -p " + propertyFile;
			}
			if (!outputFile.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -o " + outputFile;
			}
			if (!traceFileName.equalsIgnoreCase("Not Valid")) {
				stmt = stmt + " -v " + traceFileName;
			}
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

}
