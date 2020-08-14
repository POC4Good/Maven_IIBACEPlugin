package com.ace.plugin.deploy;

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
import org.apache.maven.project.MavenProject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Goal which touches a timestamp file.
 *
 * 
 * @phase deploy
 */

@Mojo (name = "deploybar", defaultPhase = LifecyclePhase.INSTALL,requiresProject=false )
public class ACEDeploy extends AbstractMojo
{
	 @Parameter(required=true)
	 private String barFile;

	 
	 @Parameter
	 private String isIndependentIntegrationServerPresent;
	 
	 @Parameter
	 private String integrationNode;
	 
	 @Parameter
	 private String integrationNodeFileName;
	 
	 @Parameter
	 private String ipAddress;
	 
	 @Parameter
	 private String port;
	 
	 @Parameter
	 private String integrationServer;
	 
	 @Parameter
	 private String deployedObjects;
	 
	 @Parameter
	 private String overeridePreviousDeployment;
	 
	 @Parameter
	 private String traceFileName;
	 
	 @Parameter
	 private String timeoutSecs;
	 
	 
   public void execute() throws MojoExecutionException
    {
	ACEDeploy  aceDeploy=new ACEDeploy();

	System.out.println("deploy phase started...");
	String cmd="mqsideploy ";
	Process deployBar=null;
	try{
		//If deployment is to be done on Integration server connected to Integration Node then, it is necessary to provide both integration server and node properties.
		//If deployment is to be done on Independent Integration server then it is necessary to provide only properties related to that integration server.
		if (isIndependentIntegrationServerPresent != null && !(isIndependentIntegrationServerPresent.equalsIgnoreCase("yes"))) {
			
			if (!integrationNode.equalsIgnoreCase("Not Valid")) {
				cmd=cmd + integrationNode;
			}
			if (!integrationServer.equalsIgnoreCase("Not Valid")) {
				cmd=cmd + " -e " + 	integrationServer;
			}
			
		}
		// Need to provide value for at least one property between - integrationNodeFileName or ipAddress-port 
		if(!integrationNodeFileName.equalsIgnoreCase("Not Valid")){
			cmd=cmd + " -n " + integrationNodeFileName;
		}
		// Adding ipAddress and port
		if (!ipAddress.equalsIgnoreCase("Not Valid")) {
			cmd=cmd + " -i " + ipAddress;
		}
		if (!port.equalsIgnoreCase("Not Valid")) {
			cmd=cmd + " -p " + port;
		}
		//Adding bar file - mandatory property for deployment
		if (!barFile.equalsIgnoreCase("Not Valid")) {
			cmd=cmd + " -a " + barFile;
		}
		// Adding -m to specify complete deployment
		if (overeridePreviousDeployment != null && overeridePreviousDeployment.equalsIgnoreCase("yes")) {
			cmd=cmd + " -m ";
		}
		// Adding -d - This parameter describes the set of objects that you want to remove from the integration server
		if (!deployedObjects.equalsIgnoreCase("Not Valid")) {
			cmd=cmd + " -d " + deployedObjects;
		}
		//This parameter specifies the maximum time in seconds that the command waits for the integration node to complete the request before returning.
		if (!timeoutSecs.equalsIgnoreCase("Not Valid")) {
			cmd=cmd + " -w " + timeoutSecs;
		}
		
		//This parameter sends internal debug trace information about a command to the specified file
		if (!traceFileName.equalsIgnoreCase("Not Valid")) {
			cmd=cmd + " -v " + traceFileName;
		}
		System.out.println("Executing mqsideploy command: "+cmd);
		//Executing command
		deployBar= Runtime.getRuntime().exec(cmd);
        while(deployBar.isAlive()){}	
        System.out.println(deployBar.exitValue());
	}catch(Exception e)
	{
		throw new MojoExecutionException("Exception :"+ e);
	}
   }
}
    
