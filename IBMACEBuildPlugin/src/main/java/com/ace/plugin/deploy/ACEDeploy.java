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

@Mojo (name = "deploybar", defaultPhase = LifecyclePhase.DEPLOY,requiresProject=false )
public class ACEDeploy extends AbstractMojo
{
	 @Parameter(required=true)
	 private String barFile;
	 
	 @Parameter
	 private String propertyFile;
	 
	 @Parameter
	 private String integrationNode;
	 
	 @Parameter
	 private String integrationServer;
	 
   public void execute() throws MojoExecutionException
    {
	ACEDeploy  aceDeploy=new ACEDeploy();

	System.out.println("deploy phase started...");
	String cmd="mqsideploy ";
	Process deployBar=null;
	try{
		//adding integration node 
		cmd=cmd + integrationNode;
		//adding integraiton server
		cmd=cmd + " -e " + 	integrationServer;
		
		//adding bar
		cmd=cmd + " -a " + barFile;
		
		deployBar= Runtime.getRuntime().exec(cmd);
        while(deployBar.isAlive()){}	
	}catch(Exception e)
	{
		throw new MojoExecutionException("Exception :"+ e);
	}
   }
}
    
