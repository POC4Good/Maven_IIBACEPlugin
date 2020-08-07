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
import org.apache.maven.project.MavenProject;

import com.ace.plugin.deploy.ACEDeploy;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Goal which applybaroverride.
 *
 * 
 * @phase verify
 */

@Mojo (name = "verify", defaultPhase = LifecyclePhase.VERIFY,requiresProject=false )
public class ACEVerify extends AbstractMojo
{
	 @Parameter(required=true)
	 private String barFile;
	 
	 @Parameter
	 private String propertyFile;
	 
	
   public void execute() throws MojoExecutionException
    {
	ACEDeploy  aceDeploy=new ACEDeploy();
	try{
	System.out.println("Bar override started...");
	String stmt="mqsiapplybaroverride -b "+ barFile ;
	if(propertyFile!=null)
	{
		stmt=stmt + " -p "+propertyFile + " -r";
	}
		 Process procExec=Runtime.getRuntime().exec(stmt);
	
	 while(procExec.isAlive()){}
	
	}catch(Exception e)
	{
		throw new MojoExecutionException("Exception :"+ e);
	}
	finally{
		aceDeploy=null;
	}
   }
   
}
    
