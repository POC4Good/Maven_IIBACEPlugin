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
 * Goal which touches a timestamp file.
 *
 * 
 * @phase process-sources
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
    
    @Parameter (required = true)
    private String barPath;

    @Parameter
    private String appName;

    @Parameter
    private String libName;

    /* public String configValue(String processType,String osType) throws  MojoExecutionException
    {
    	       Properties prop = new Properties();
		String propFileName = "script_dir.properties";
             try{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new MojoExecutionException("property file '" + propFileName + "' not found in the classpath");
		}

	   // String type=processType+"_"+osType;

		// get the property value and print it out
		String path = prop.getProperty(type);
		return path;
             }
             catch(Exception e)
             {
                throw new MojoExecutionException("Exception", e);
             }
    }
*/

    public void execute()
        throws MojoExecutionException
    {
    	ACECompile cMaven=new ACECompile();
        try
        { 
              //    System.out.println("User entered:" + processType + "   " + osType);
		 String scriptsubPath="";//cMaven.configValue(processType, osType);
		 String scriptPath=null;
		 String userDir=System.getProperty("user.dir");
		  System.out.println(userDir);
		  Process p;
                  
		//  switch(processType)
		//  {
		  //	case "createbar":
		  		  scriptPath="mqsicreatebar -data "+workspace + " -b " + barPath + " -a ";
				  if (appName!=null)
                                     scriptPath=scriptPath+appName;

                                  if(libName!=null)
                                   {
                                     
                                   }
                                   
                                   scriptPath=scriptPath + " -skipWSErrorCheck";
    
                                  System.out.println("executing script");
		  		   p =Runtime.getRuntime().exec(scriptPath);
		           while(p.isAlive()){}
		           System.out.println(p.exitValue());
		     /*      break;
		  	case "packagebar":
		  		   scriptPath=userDir+scriptsubPath;
		  		   p =Runtime.getRuntime().exec(scriptPath);
		           while(p.isAlive()){}
		           System.out.println(p.exitValue());
		           break;
		  	case "deploybar":
		  		   p =Runtime.getRuntime().exec("..\\scripts\\windows\\deploybar_script.bat");
		           while(p.isAlive()){}
		           System.out.println(p.exitValue());
		           break;
		  	default:
		  		  //throw exception.
		  }*/
		   
        }
        catch ( IOException e )
        {
             throw new MojoExecutionException("IO Exception", e);
        }
        finally
        {
            cMaven=null;
        }
    }
}
