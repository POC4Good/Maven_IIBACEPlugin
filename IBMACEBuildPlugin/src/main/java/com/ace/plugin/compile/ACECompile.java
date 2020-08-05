package com.ace.plugin.compile;

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
 * Goal which creates bar.
 *
 * 
 * @phase compile
 */

@Mojo (name = "createbar", defaultPhase = LifecyclePhase.COMPILE)
public class ACECompile
    extends AbstractMojo
{
    /**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    @Parameter (required = true)
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

    public void execute()
        throws MojoExecutionException
    {
    	ACECompile cMaven=new ACECompile();
        try
        { 
        	 	  Process p=null;
                  
		
		  		  String scriptPath="mqsicreatebar -data "+workspace;
		  		  
		  		  if(barPath!=null)
		  			   scriptPath=scriptPath + " -b " + barPath;
		  		  
		  		  if (compileOnly!=null && compileOnly.equalsIgnoreCase("yes"))
		  				  scriptPath=scriptPath+" -compileOnly";
		  		  
		  		 
		  		  
				  if (appName!=null)
                                     scriptPath=scriptPath+" -a "+appName;

                  if(libName!=null)
                	  			scriptPath=scriptPath+" -l "+libName;  
                  
                  if (deployAsSource!=null)
	  			      scriptPath=scriptPath+" -deployAsSource";
                 //skip error in workspace
                 if(skipError!=null && skipError.equalsIgnoreCase("yes"))
                       scriptPath=scriptPath + " -skipWSErrorCheck";
    
                                  
                   //adding tracefile if not present
                   if(traceFilePath!=null)
                	   scriptPath=scriptPath + " -trace -v " + traceFilePath;
                   
                   
                   System.out.println("executing script ...");
		  		   p =Runtime.getRuntime().exec(scriptPath);
		  		   
		           while(p.isAlive()){}
		           System.out.println(p.exitValue());
		   
        }
        catch ( Exception e )
        {
             throw new MojoExecutionException(" Exception : ", e);
        }
        finally
        {
            cMaven=null;
        }
    }
}
