package com.ace.plugin.install;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
 * Goal which installs bar in local .m2 directory or on JFrog artifactory
 *
 * 
 * @phase compile
 */

@Mojo(name = "installbar", defaultPhase = LifecyclePhase.INSTALL)
public class ACEInstall extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	

	@Parameter(required = true)
	private String barPath;

	@Parameter
	private String localm2Dir;
	
	@Parameter
	private String artifactoryUpload;
	
	@Parameter
	private String artifactoryUserName;

	@Parameter
	private String artifactoryPassword;

	@Parameter
	private String artifactoryUrl;
	
	@Parameter
	private String artifactoryPOM;
	

	public void execute() throws MojoExecutionException {
		ACEInstall cMaven = new ACEInstall();
		try {
			String filename = null;
			if(!localm2Dir.equalsIgnoreCase("Not Valid")){
				if (barPath.contains(File.separator)) {
					int index = barPath.lastIndexOf(File.separator);
					filename = barPath.substring(index+1);
					System.out.println("bar file name: "+filename);
				}
				Path FROM = Paths.get(barPath);
				Path TO = Paths.get(localm2Dir+File.separator+filename);
				CopyOption[] options = new CopyOption[]{
				  StandardCopyOption.REPLACE_EXISTING
				}; 
				java.nio.file.Files.copy(FROM, TO, options);
				System.out.println("Bar file copied successfully to: "+localm2Dir);
			}
			
			if (artifactoryUpload.equalsIgnoreCase("yes")) {
				HttpURLConnection conn;
				OutputStream out;
				String output; 
				BufferedReader br;
				MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
				
				//Setting authentication credentials for JFrog artifactory
				Authenticator.setDefault (new Authenticator() {
				    protected PasswordAuthentication getPasswordAuthentication() {
				        return new PasswordAuthentication (artifactoryUserName, artifactoryPassword.toCharArray());
				    }
				});
				//Creating artifactory url to upload pom file. 
				//<groupId>com.ibm.ace</groupId>,
				//<artifactId>IBMACEBar</artifactId>,
				//<version>0.0.1-SNAPSHOT</version>
				
				String artifactoryPomUrl = artifactoryUrl+"com/ibm/ace/IBMACEBar/0.0.1-SNAPSHOT/IBMACEBar-0.0.1-SNAPSHOT.pom";
				URL pomUrl = new URL(artifactoryPomUrl);
				conn = (HttpURLConnection) pomUrl.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT");
				String pomName = artifactoryPOM;
				FileBody filePom = new FileBody(new File(pomName));
				multipartEntity.addPart("file", filePom);
				conn.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
				out = conn.getOutputStream();
				try {
				    multipartEntity.writeTo(out);
				} finally {
				    
				}
				//printing output from artifactory pom upload
				br = new BufferedReader(new InputStreamReader(
						  (conn.getInputStream())));  
				System.out.println("Output from Server .... \n"); 
				while((output = br.readLine()) != null) 
				{ System.out.println(output); 
				}
				//closing output stream and disconnecting connection
				out.close();
				conn.disconnect();
						
				//Creating artifactory url to upload bar file. 
				//<groupId>com.ibm.ace</groupId>,
				//<artifactId>IBMACEBar</artifactId>,
				//<version>0.0.1-SNAPSHOT</version>
				
				String artifactoryBarUrl = artifactoryUrl+"com/ibm/ace/IBMACEBar/0.0.1-SNAPSHOT/IBMACEBar-0.0.1-SNAPSHOT.bar";
				URL url = new URL(artifactoryBarUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT");
				String fileName = barPath;
				FileBody fileBody = new FileBody(new File(fileName));
				multipartEntity.addPart("file", fileBody);

				conn.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
				out = conn.getOutputStream();
				try {
				    multipartEntity.writeTo(out);
				} finally {
				    
				}
				
				//printing output from artifactory bar upload
				
				br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream()))); 
				System.out.println("Output from Server .... \n"); 
				while((output = br.readLine()) != null) 
				{ System.out.println(output); 
				}
				
				//closing output stream and disconnecting connection
				out.close();
				conn.disconnect();
			}

		} catch (Exception e) {
			throw new MojoExecutionException(" Exception : ", e);
		} finally {
			cMaven = null;
		}
	}
	
	
}

