package com.ace.plugin.packagegrp;

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
 * This uses mqsipackage bar features of IBM ACE.
 *
 * @PACKAGE package
 * 
 * @phase Package
 */

@Mojo (name = "packagebar", defaultPhase = LifecyclePhase.PACKAGE,requiresProject=false )
public class ACEPackage extends AbstractMojo
{
   
    @Parameter
    private String m2RepoDir;

    @Parameter
    private String dependencyList;

    @Parameter(required = true)
    private String barPath;

    @Parameter
    private String rootLocation;

    @Parameter
    private String filePath;

    @Parameter
    private String appName;

    @Parameter
    private String libName;

    @Parameter
    private String updateResource;//-u field

    @Parameter
    private String addDepandNonDepFiles;

    @Parameter
    private String traceFilePath;

    

   public void execute() throws MojoExecutionException
    {
	ACEPackage acePackage=new ACEPackage();
	try{
	System.out.println("Executing package phase");
	String cmd="mqsipackagebar ";
	
	/// adding bar file in command
	cmd=cmd + " -a "+barPath;
	
	// root location in command
	cmd=cmd + " -w "+rootLocation;
	
	// file location in command
    if (m2RepoDir!=null)
    {
        //extract  and add one by one 
    }
    else
    {
    	// give simple as it is
    }
    
    // application name in command
    cmd=cmd+ " -k "+ appName;
    
    //library name in command
    cmd=cmd + " -y "+ libName;
    
    //update resources in command
    cmd=cmd + " -u ";
    
    //insert non dep files
    cmd=cmd + " -i ";
    
    //trace file
    cmd=cmd + " -v " + traceFilePath;
    
    cmd=cmd.trim();
    
    Process procExec=Runtime.getRuntime().exec(cmd);
    while(procExec.isAlive()){}
    }catch(Exception e)
    {
    	throw new MojoExecutionException("Exception "+e);
     }
	finally{
		acePackage=null;
	}
    }
}
    
