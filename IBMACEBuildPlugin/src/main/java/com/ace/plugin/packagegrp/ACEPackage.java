package com.ace.plugin.packagegrp;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import java.io.BufferedReader;
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

@Mojo(name = "packagebar", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = false)
public class ACEPackage extends AbstractMojo {
	private static final int BUFFER_SIZE = 4096;

	/*
	 * @Parameter private String m2RepoDir;
	 * 
	 * @Parameter private String dependencyList;
	 * 
	 * @Parameter(required = true) private String barPath;
	 * 
	 * @Parameter private String rootLocation;
	 * 
	 * @Parameter private String filePath;
	 * 
	 * @Parameter private String appName;
	 * 
	 * @Parameter private String libName;
	 * 
	 * @Parameter private String updateResource;//-u field
	 * 
	 * @Parameter private String addDepandNonDepFiles;
	 * 
	 * @Parameter private String traceFilePath;
	 */
	@Parameter
	private String artifactoryUserName;

	@Parameter
	private String artifactoryPassword;

	@Parameter
	private String artifactoryUrl;

	@Parameter
	private String artifactoryBarDownloadPath;
	
	@Parameter
	private String artifactoryBarVersion;

	public void execute() throws MojoExecutionException {
		ACEPackage acePackage = new ACEPackage();
		try {
			System.out.println("Executing package phase");
			/*
			 * String cmd="mqsipackagebar ";
			 * 
			 * /// adding bar file in command cmd=cmd + " -a "+barPath;
			 * 
			 * // root location in command cmd=cmd + " -w "+rootLocation;
			 * 
			 * // file location in command if (m2RepoDir!=null) { //extract and add one by
			 * one } else { // give simple as it is }
			 * 
			 * // application name in command cmd=cmd+ " -k "+ appName;
			 * 
			 * //library name in command cmd=cmd + " -y "+ libName;
			 * 
			 * //update resources in command cmd=cmd + " -u ";
			 * 
			 * //insert non dep files cmd=cmd + " -i ";
			 * 
			 * //trace file cmd=cmd + " -v " + traceFilePath;
			 * 
			 * cmd=cmd.trim();
			 * 
			 * Process procExec=Runtime.getRuntime().exec(cmd);
			 */
			// while(procExec.isAlive()){}

			HttpURLConnection conn;
			OutputStream out;
			String output;
			BufferedReader br;

			//Setting authentication credentials for JFrog artifactory
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(artifactoryUserName,artifactoryPassword.toCharArray());
				}
			});

			//Creating artifactory url to upload pom file. 
			//<groupId>com.ibm.ace</groupId>,
			//<artifactId>IBMACEBar</artifactId>,
			//<version>0.0.1-SNAPSHOT</version>
			String artifactoryBarUrl;
			if (artifactoryBarVersion != null) {
				artifactoryBarUrl= artifactoryUrl+artifactoryBarVersion;
			}else {
			artifactoryBarUrl = artifactoryUrl+"com/ibm/ace/IBMACEBar/0.0.1-SNAPSHOT/IBMACEBar-0.0.1-SNAPSHOT.bar";
			}
			URL url = new URL(artifactoryBarUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			// conn.setDoInput(true);
			conn.setDoOutput(true);
			int responseCode = conn.getResponseCode();

			
			// Check for success response and save bar file received in response.
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = conn.getHeaderField("Content-Disposition");
				String contentType = conn.getContentType();
				int contentLength = conn.getContentLength();
				System.out.println("Content-Type = " + contentType);
				System.out.println("Content-Disposition = " + disposition);
				System.out.println("Content-Length = " + contentLength);
				
				if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					String temp = disposition.substring(index);
					System.out.println("temp string" + temp);
					if (index > 0) {
						fileName = temp.substring(10, temp.indexOf(";") - 1);
					}

					System.out.println("fileName = " + fileName);
					InputStream inputStream = conn.getInputStream();
					String saveFilePath = artifactoryBarDownloadPath + File.separator + fileName;

					// opens an output stream to save into bar file
					ReadableByteChannel readableByteChannel = Channels.newChannel(conn.getInputStream());
					FileOutputStream outputStream = new FileOutputStream(saveFilePath);
					outputStream.getChannel()
					  .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

					//Closing connections
					outputStream.close();
					inputStream.close();
					conn.disconnect();
					
				}
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Exception " + e);
		} finally {
			acePackage = null;
		}

	}
}
