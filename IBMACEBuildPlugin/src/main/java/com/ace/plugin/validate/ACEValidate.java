package com.ace.plugin.validate;

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
 * Goal which checks if mqsiprofile is set to run the 
 *
 * 
 * @phase validate
 */

@Mojo (name = "validateEnv", defaultPhase = LifecyclePhase.VALIDATE,requiresProject=false )
public class ACEValidate extends AbstractMojo
{
   @Parameter (required = true)
   private String mqsiprofileLoc;
   public void execute() throws MojoExecutionException
    {
	ACEValidate aceValid=new ACEValidate();
	System.out.println("Validating mqsiprofile set to run commandline environment...");
        Process procToExec=null;
        try{
		
		procToExec=Runtime.getRuntime().exec(mqsiprofileLoc);
		while(procToExec.isAlive()){}
        System.out.println("Process exited with value" + procToExec.exitValue());
                
         }catch(Exception e)
         {
         System.out.println("Error Occured while setting the mqsiprofile cmd");
		throw new MojoExecutionException("Exception " +e);
	     }
        finally{
        	aceValid=null;
        }
    }
}
    
