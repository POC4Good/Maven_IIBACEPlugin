package com.ace.plugin.compile;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

//import org.jfrog.artifactory.client.model.File;

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
 * Goal which creates bar.
 *
 * 
 * @phase compile
 */

@Mojo(name = "createbar", defaultPhase = LifecyclePhase.COMPILE)
public class ACECompile extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	@Parameter(required = true)
	private String workspace;

	@Parameter
	private String barPath;

	@Parameter
	private String compileOnly;

	@Parameter
	private String deployAsSource;

	@Parameter
	private String appName;

	@Parameter
	private String libName;

	@Parameter
	private String skipError;

	@Parameter
	private String traceFilePath;
	
	

	public void execute() throws MojoExecutionException {
		ACECompile cMaven = new ACECompile();
		try {
			Process p = null;
			String barFileName = null;
			String scriptPath = "mqsicreatebar -data " + workspace;

			//Adding bar file path
			if (!barPath.equalsIgnoreCase("Not Valid")) {
				scriptPath = scriptPath + " -b " + barPath;
				barFileName= barPath.substring(barPath.lastIndexOf("\\")+1);
				System.out.println("barFileName= "+barFileName);
			}
			//Adding CompileOnly option if present
			if (compileOnly != null && compileOnly.equalsIgnoreCase("yes"))
				scriptPath = scriptPath + " -compileOnly";

			//Adding appName if present
			if (!appName.equalsIgnoreCase("Not Valid"))
				scriptPath = scriptPath + " -a " + appName;

			//Adding Library name if present
			if (!libName.equalsIgnoreCase("Not Valid"))
				scriptPath = scriptPath + " -l " + libName;

			//Adding deployAsSource option if present
			if (!deployAsSource.equalsIgnoreCase("Not Valid"))
				scriptPath = scriptPath + " -deployAsSource";
			
			// skip error in workspace
			if (skipError != null && skipError.equalsIgnoreCase("yes"))
				scriptPath = scriptPath + " -skipWSErrorCheck";

			// adding tracefile if provided by user
			if (!traceFilePath.equalsIgnoreCase("Not Valid"))
				scriptPath = scriptPath + " -trace -v " + traceFilePath;

			//Running mqsicreatebar command 
			System.out.println("executing script ..." + scriptPath);
			p = Runtime.getRuntime().exec(scriptPath);
			
			
			while (p.isAlive()) {
			}
			System.out.println(p.exitValue());
			
			

		} catch (Exception e) {
			throw new MojoExecutionException(" Exception : ", e);
		} finally {
			cMaven = null;
		}
	}
	
	
}
