#Maven_IIBACEPlugin

              
Prerequisite:
            IBM ACE should be installed on the system.

1. Overview

This Maven plugin specifically created for IBM App Connect Enterprise (ACE). This internally uses mqsi commands supported by ACE environment. 
This plugin helps in –
* Creating bar file
* Packaging bar file
* Apply bar overrides to bar file
* Deploy bar file

2. Generic POM.xml

Generic pom.xml is provided with this plugin, which is used to specify different user defined properties and values. 
It contains all the properties used by Create bar, package bar, apply bar override and deploy bar. It also contains the Artifactory related properties as well. 
Plugin goals are defined in plugin execution section.
- validateEnv: This goal is used to set mqsi profile location to run all the commands
- createbar: This goal is used to create bar file. It will also upload generated bar file to JFrog artifactory.
- packagebar: this goal is used to perform package bar mqsi command. It will also download the latest bar file present in JFrog artifactory.
- verify: This goal is used to apply bar override properties to generated bar file.
- deploybar: This goal is used to deploy bar file to ACE integration server.

Attached generic pom file in reference section.
Artifactory properties -in pom file.
<artifactoryUpload>yes</artifactoryUpload>
<artifactoryDownload>yes</artifactoryDownload>
<artifactoryUserName>admin</artifactoryUserName>
<artifactoryPassword>password</artifactoryPassword>
<artifactoryUrl>http://localhost:8081/artifactory/libs-release-local/com/ibm/ace/IBMACEBar/0.0.1-SNAPSHOT/</artifactoryUrl> 
<artifactoryBarVersion>IBMACEBar-0.0.1-20200928.165340-6.bar</artifactoryBarVersion>
<artifactoryPOM>C:\ACEPlugin\bar\IBMACEBar-0.0.1-SNAPSHOT.pom</artifactoryPOM>
<artifactoryBarDownloadPath>C:\ACEPlugin\bar\</artifactoryBarDownloadPath>


These properties are used by plugin internally to connect to JFrog artifactory.

1. Create Bar file
This is used to create bar file in ACE. 
IBM Knowledge Center link for mqsicreatebar 
All possible variants of mqsicreatebar can be utilised using this plugin, by passing correct arguments to maven command. It will also upload bar bar file to JFrog artifactory, if it is specified in pom file. Th property used to specify artifactory upload is –
<artifactoryUpload>yes</artifactoryUpload>

Workspace (M): The path of the workspace in which your projects are created

barPath: The name of the BAR (compressed file format) archive file where the result is stored. This parameter is required if the -compileOnly parameter is not used.

compileOnly: Include this parameter to compile message sets and Java™ code before you run the mqsipackagebar command. All relevant files in the workspace that is specified by the -data parameter are compiled.

deployAsSource: Include this parameter to deploy applicable resource files without compiling them. When the -deployAsSource parameter is used, any applicable resources are added to the BAR file as source files, and are not compiled into message flow .cmf files.

appName: The name of an application to add to the BAR file. You can add more than one application by using the following format:ApplicationName1 ApplicationName2 .... ApplicationName'n'

libName: The name of a library to add to the BAR file. You can add more than one library by using the following format: LibraryName1 LibraryName2 .... LibraryName'n'

skipError: his parameter forces the BAR file compilation process to run, even if errors exist in the workspace.

traceFilePath: This parameter specifies the file name of the output log to which trace information is sent.

cleanBuild (O): Refreshes the projects in the workspace and then invokes a clean build before new items are added to the BAR file.

Esql21 (O): Compile ESQL for integration nodes at Version 2.1 of the product.

version (O): Appends the _ (underscore) character and the value of VersionString to the names of the compiled versions of the message flows (.cmf) files added to the BAR file, before the file extension

trace (O): This parameter displays trace information for BAR file compilation

Createbar for application without shared lib:

mvn compile -Dworkspace=<workspace-name> -DbarPath=<bar-filename> -DappName=<application-name> -DskipError=yes -DtraceFilePath=<trace-file-path>

Ex:
mvn compile -Dworkspace=C:\Workspace\SampleWS -DbarPath=C:\TESTApp.bar -DappName=TESTApp -DskipError=yes -DtraceFilePath=C:\TESTApp_Override.txt


Createbar for application with shared lib

mvn compile -Dworkspace=<workspace-name> -DbarPath=<bar-filename> -DappName=<application-name> -DskipError=yes -DtraceFilePath=<trace-file-path>
-DlibName=<library-name> -DdeployAsSource=<yes/no>

Ex:
mvn compile -Dworkspace=C:\Workspace\SampleWS -DbarPath=C:\TESTApp.bar -DappName=TESTApp -DskipError=yes -DtraceFilePath=C:\TESTApp_trace.txt
-DlibName=SampleSharedLib -DdeployAsSource=yes

Createbar for shared lib

mvn compile -Dworkspace=<workspace-name> -DbarPath=<bar-filename>  -DlibName=<Shared-library-name> -DskipError=yes -DtraceFilePath=<trace-file-path>

Ex:
mvn compile -Dworkspace=C:\Workspace\SampleWS -DbarPath=C:\SampleSharedLib.bar -DlibName=SampleSharedLib -DskipError=yes -DtraceFilePath=C:\sharedlib_trace.txt


2. Package Bar file


It is also used to download existing bar file present in JFrog artifactory if it is specified in pom file. Property mentioned in pom file is- 
<artifactoryDownload>yes</artifactoryDownload>

If artifactoryBarVersion property is set, then it will download specified version of bar file. Else it will download the latest version present in artifactory.
<artifactoryBarVersion>IBMACEBar-0.0.1-20200928.165340-6.bar</artifactoryBarVersion>



3. Apply Bar Overrides
This command is used to apply bar override properties on bar file.
IBM Knowledge Center link for mqsiapplybaroverride
All possible variants of mqsiapplybaroverride can be utilised using this plugin, by passing correct arguments to maven command.
All possible arguments for this plugin:
barFile (M): The path to the BAR file (in compressed format) to which the override values apply.

overrideFile (O): name of override file. It can be bar file, property file or deployment descriptor file.

manualUsingPropertyfile (O): this indicator used to identify if custom property file is used for override. Value can be either yes or no.

recursion (O): Specifies that all deployment descriptor files are updated recursively, including any in nested applications (.appzip files) and libraries (.libzip files).

applicationName (O): The name of an application in the BAR file to which to apply overrides

traceFileName (O): Specifies that the internal trace is to be sent to the named file

libraryName (O): The name of a library in the BAR file to which to apply overrides

outputFile (O):  The name of the output BAR file to which the BAR file changes are to be made. If an output file is not specified, the input file is overwritten.

directManualOverrides (O): A list of the property-name=override pairs, current-property-value=override pairs, or a combination of them, to be applied to the BAR file. The pairs in the list are separated by commas (,). On Windows, you must enclose the list in quotation marks (" ").

Some Examples and scenarios:
1- A BAR file that contains the deployment descriptor that is used to apply overrides to the BAR file.
2- A properties file in which each line contains a property-name=override or current-property-value=new-property-value pair.
3- A deployment descriptor that is used to apply overrides to the BAR file
4- A Custom property file (which is specifically created for this plugin)
5- Direct Manual overrides

1- Using bar file/property file/deployment descriptor (above 1,2,3 points)

When want to override properties of one bar file to another bar file.
When override properties are mentioned in property file
When deployment descriptor is used for bar override

Sample Property file snippet-


Maven Command:
mvn verify -DbarFile=<bar-file-location> -DapplicationName=<application-name> -DoverrideFile =<override-file-location>

Ex:
mvn verify -DbarFile=C:\TESTApp.bar -DapplicationName=TESTApp -DoverrideFile=C:\TESTApp.properties -DtraceFileName=C:\TestAppOverride.txt

mvn verify -DbarFile=C:\TESTApp.bar - DoverrideFile =C:\TESTAppOriginal.bar -DtraceFileName=C:\TestAppOverride.txt

mvn verify -DbarFile=C:\TESTApp.bar -DpropertyFile=C:\StaticLib_broker.xml -DtraceFileName=C:\TestAppOverride.txt -DapplicationName=TESTApp -DlibraryName=SampleLib



2- Using Custom property file (point 4 above)
When all overrides are mentioned in property file including overrides for libraries and applications.

Sample property file snippet:


In this property file-
m: message flow property
y: library name
k: application name

Maven command: 
mvn verify -DbarFile=<bar-file-location> -DapplicationName=<application-name> - DoverrideFile =<override-file-location> -DtraceFileName=<trace-filename> -DmanualUsingPropertyfile=<yes/no> -Drecursion=<yes/no>

manualUsingPropertyfile (O): this indicator used to identify if custom property file is used for override. Value will be either yes or no. This parameter is mandatory and should provide value as ‘yes’ in case of custom property file.
recursion (O): used if override needs to be done recursively.

Ex:
mvn verify -DbarFile=C:\TESTApp.bar - DoverrideFile =C:\TESTApp.properties -DtraceFileName=C:\TestAppOverride.txt -DmanualUsingPropertyfile=yes -Drecursion=yes -DapplicationName=TESTApp


3- Direct Manual overrides (Point 5 above)
This plugin can be used for direct manual overrides. Where override properties and values re directly provided in command instead of any property, bar or deployment descriptor.

Maven command-
mvn verify mvn verify -DbarFile=<bar-file-location> -DapplicationName=<application-name> -DmanualOverrides="<manual override properties>" - DtraceFileName=<trace-filename>

Ex:
mvn verify -DbarFile=C:\TESTApp.bar -D directManualOverrides ="testFlow#MQ Input.queueName=NEW_INQ,testFlow#MQ Output.queueName=NEW_OUTQ" -DtraceFileName=C:\TestAppOverride.txt -DoutputFile=C:\newBar.bar



4. Deploy Bar file
This command is used to deploy bar file to ACE Integration server or Integration Node.
IBM Knowledge Center link for mqsideploybar
All possible variants of mqsideploybar can be utilised using this plugin, by passing correct arguments to maven command.

Argument list need to pass for Maven command:
barFile (M): This parameter specifies the BAR file that you want to use to deploy a message flow and other resources.

isIndependentIntegrationServerPresent (O) : This parameter is to identify if deployment is to do on Independent integration server or Integration server which is having Integration node assigned. Pass value as yes, if deployment is on independent integration server and pass as no if deployment is on integration server which has integration node assigned.

integrationNodeName (O): This parameter identifies the name of a specific integration node

integrationNodeFileName (O):  This parameter identifies the name of a .broker file that contains the connection details for an integration node or independent integration server. Include the location (path) and file name when you specify this parameter. You must ensure that the location is accessible when you run this command.

ipAddress (O): The host name or IP address of the computer on which the integration node or server is running. If you do not specify this parameter, a value that represents the local computer is used.
These parameters identify a host and port for an integration node or independent integration server, for connections that do not require advanced connection parameter.
To connect to a specific remote integration node by name, you can additionally prefix integrationNodeName. You cannot use -n integrationNodeFileName in the same command.

port (O): The port on which the web user interface HTTP connection listener is listening. If you do not specify this parameter, the value 4414 is used.

integrationServer (O): This parameter specifies the name of the integration server on an integration node on which to perform the deploy action.

deployedObjects (O): This parameter describes the set of objects that you want to remove from the integration server. You can specify multiple files to delete by separating the filenames with a colon (:).

overeridePreviousDeployment (O): The default operation is a delta or incremental deployment. Specify this parameter. parameter to override the default operation and run a complete deployment

traceFileName (O): This parameter sends internal debug trace information about a command to the specified file

timeoutSecs (O): This parameter specifies the maximum time in seconds that the command waits for the integration node to complete the request before returning.


Sample Maven commands:

Deploy to integration server in integration node by providing ip and port of integration node:
mvn clean install -DipAddress=<ip-address> -Dport=<port> -DintegrationServer=<integration-server-name> -DbarFile=<bar-filename> -DovereridePreviousDeployment=<yes/no> -DtimeoutSecs=<timeout-in-sec> -DtraceFileName=<trace-filename>

Ex:
mvn clean install -DipAddress=localhost -Dport=4416 -DintegrationServer=DEFAULT -DbarFile=C:\newBar.bar -DovereridePreviousDeployment=yes -DtimeoutSecs=600 -DtraceFileName=C:\newBarDeploylogs_node.txt

Deploy to integration server in integration node by providing integration node name:
mvn clean install -DintegrationNodeName=<Integration-node-name> -DintegrationServer=<integration-server-name> -DbarFile=<bar-filename> -DovereridePreviousDeployment=<yes/no> -DtimeoutSecs=<timeout-in-sec> -DtraceFileName=<trace-filename>

Ex:
mvn clean install -DintegrationNodeName=ACENODE -DintegrationServer=DEFAULT -DbarFile=C:\newBar.bar -DovereridePreviousDeployment=yes -DtimeoutSecs=600 -DtraceFileName=C:\newBarDeploylogs.txt



Deploy to independent integration server
mvn clean install -DipAddress=<ip-address> -Dport=<port>  -DbarFile=<bar-filename> -DovereridePreviousDeployment=<yes/no> -DtimeoutSecs=<timeout-in-sec> -DtraceFileName=<trace-filename> -DisIndependentIntegrationServerPresent=<yes>

Ex:
mvn clean install -DipAddress=localhost -Dport=7600 -DbarFile=C:\newBar.bar -DovereridePreviousDeployment=yes -DtimeoutSecs=600 -DtraceFileName=C:\newBarDeploylogs.txt -DisIndependentIntegrationServerPresent=yes



                    

