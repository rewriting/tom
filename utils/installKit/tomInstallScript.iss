; when buiding a new install file:
; 1. make sure you have run a stable.dist
; 2. change AppVerName
; 3. change OutputBaseFilename

[Setup]
AppName=Tom
AppVerName=Tom version 2.9-r3
DefaultDirName={pf}\INRIA\Tom
DefaultGroupName=Tom
UninstallDisplayIcon={app}\Tom.exe
SourceDir=../../stable/dist
OutputBaseFilename=tom-2.9-r3_setup
OutputDir=../../utils/installKit
LicenseFile=LICENCE
ChangesEnvironment=yes
PrivilegesRequired=none

[Languages]
Name: "en"; MessagesFile: "compiler:Default.isl"
Name: "fr"; MessagesFile: "compiler:Languages\French.isl"
Name: "nl"; MessagesFile: "compiler:Languages\Dutch.isl"
Name: "de"; MessagesFile: "compiler:Languages\German.isl"
Name: "sp"; MessagesFile: "compiler:Languages\Spanish.isl"
Name: "it"; MessagesFile: "compiler:Languages\Italian.isl"
Name: "pt"; MessagesFile: "compiler:Languages\Portuguese.isl"
Name: "ru"; MessagesFile: "compiler:Languages\Russian.isl"
Name: "pl"; MessagesFile: "compiler:Languages\Polish.isl"
Name: "sk"; MessagesFile: "compiler:Languages\Slovak.isl"
Name: "sl"; MessagesFile: "compiler:Languages\Slovenian.isl"
Name: "no"; MessagesFile: "compiler:Languages\Norwegian.isl"
Name: "hu"; MessagesFile: "compiler:Languages\Hungarian.isl"
Name: "he"; MessagesFile: "compiler:Languages\Hebrew.isl"
Name: "fi"; MessagesFile: "compiler:Languages\Finnish.isl"
Name: "da"; MessagesFile: "compiler:Languages\Danish.isl"
Name: "cs"; MessagesFile: "compiler:Languages\Czech.isl"
Name: "ca"; MessagesFile: "compiler:Languages\Catalan.isl"
Name: "br"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"
Name: "eu"; MessagesFile: "compiler:Languages\Basque.isl"

[Files]
Source: "AUTHORS";            DestDir: "{app}"
Source: "Gom.xml";            DestDir: "{app}"
Source: "Tom.xml";            DestDir: "{app}"
Source: "GomAntlrAdaptor.xml"; DestDir: "{app}"
Source: "INSTALL";            DestDir: "{app}"
Source: "LICENCE";            DestDir: "{app}"
Source: "NEWS";               DestDir: "{app}"
Source: "README";             DestDir: "{app}"; DestName: "README.wri"; Flags: isreadme
Source: "bin\*";              DestDir: "{app}\bin"
Source: "lib\*.jar";          DestDir: "{app}\lib"
Source: "lib\tom-common.xml"; DestDir: "{app}\lib"
Source: "lib\tools\org.eclipse.emf.*.jar";          DestDir: "{app}\lib\tools"
Source: "share\*";            DestDir: "{app}\share"; Flags: recursesubdirs createallsubdirs

[Registry]
; add TOM_HOME to environmental variables
;Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: string; ValueName: "TOM_HOME"; ValueData: "{app}"; Flags: deletevalue
Root: HKCU; Subkey:"Environment"; ValueType: string; ValueName: "TOM_HOME"; ValueData: "{app}"; Flags: deletevalue
; add %TOM_HOME%\bin to PATH variable
; Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};%TOM_HOME%\bin"
Root: HKCU; Subkey:"Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};%TOM_HOME%\bin"
; add %JAVA_HOME%\bin to PATH variable
;Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};%JAVA_HOME%\bin"
Root: HKCU; Subkey:"Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};%JAVA_HOME%;%JAVA_HOME%\bin"

[Code]
var
  RegistryRoot: Integer;
  RegistryEnvPath: String;
  
// check that from the JAVA_HOME defined, we can find "javac.exe" and "java.exe"
function IsJAVAHOMEProperlyDefined() : Boolean;
var
  javaHome : String;
  path : String;
begin
    Result := False
		javaHome := Trim(GetEnv('JAVA_HOME'));
		if Length(javaHome) > 0 then begin
			path := javaHome + '\bin\' + 'javac.exe';
			if FileExists(path) then begin
				Log('(JAVA_HOME) found javac.exe: ' + path);
				path := javaHome + '\bin\' + 'java.exe';
			  if FileExists(path) then begin
				  Log('(JAVA_HOME) found java.exe: ' + path);
				  Result := True;
				end;
			end;
    end
end;

function getJDKVersion(): String;
var
	jdkVersion: String;
begin
	jdkVersion := '';
	RegQueryStringValue(HKLM, 'SOFTWARE\JavaSoft\Java Development Kit', 'CurrentVersion', jdkVersion);
	GetVersionNumbersString(jdkVersion, jdkVersion);
	Result := jdkVersion;
end;

// Finds path to "java.exe" and "javaw.exe" by looking up JDK or location
// in the registry.  Ensures the file actually exists.  If none
// is found, an empty string is returned. 						
function GetJavaPath(): String;
var
	javaVersion: String;
	javaHome: String;
	path: String;
begin
	path := '';
	javaHome := '';
  Result := '';	
	// try to find JDK "javac.exe" and "java.exe"
	javaVersion := getJDKVersion();
	if ((javaVersion) >= '1.3.0') then begin
		RegQueryStringValue(HKLM, 'SOFTWARE\JavaSoft\Java Development Kit\' + javaVersion, 'JavaHome', javaHome);
		path := javaHome + '\bin\' + 'javac.exe';
		if FileExists(path) then begin
			Log('(JDK) found javac.exe: ' + path);
			path := javaHome + '\bin\' + 'java.exe';
  	  if FileExists(path) then begin
			 Log('(JDK) found java.exe: ' + path);
			 Result := javaHome;
		  end;
		end;		
	end;
	
	// no javaHome found
	// let user browse for Java
	if Length(Result) = 0 then begin
		MsgBox('Setup was unable to automatically find "javac.exe" and "java.exe".' + #13
			    + #13 +
			   'If you have a JDK, version 1.3 or greater, installed' + #13
			   'please locate "javac.exe".  If you don''t have a JDK' + #13
			   'installed, download and install from http://java.sun.com/ and' + #13
			   'restart setup.', mbError, MB_OK);
		Log('unable to find javac.exe, prompting for path');
		if GetOpenFileName('Browse for javac.exe', path, '.', 'EXE files (*.exe)|*.exe', '.exe') then begin
			Log('(BROWSE): user selected: ' + path);
			javaHome := ExtractFilePath(path);
			Result := javaHome;
		end;
	end;
	
end;

// sets the JAVA_HOME property
function SetJavaHOME(): Boolean;
var
  javaHome : String;
begin
  javaHome := GetJavaPath()
  Log('Writing JAVA_HOME:' + javaHome);
  RegWriteExpandStringValue(RegistryRoot, RegistryEnvPath, 'JAVA_HOME', javaHome);
  Result:=True;
end;

// check that java is installed
function InitializeSetup(): Boolean;
var
  success:Boolean;
begin
  success:=False;
  RegistryRoot := HKEY_CURRENT_USER;
  // HKEY_LOCAL_MACHINE
  RegistryEnvPath := 'Environment';
  // 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
  
  // check that JAVA_HOME exists
  // if yes, check that is correctly defined
  if Length(Trim(GetEnv('JAVA_HOME'))) > 0 then begin
    if not IsJAVAHOMEProperlyDefined() then begin
      //MsgBox('JAVA_HOME is not correctly defined', mbInformation, MB_OK);
      // try to overwrite it with a good value
      success := SetJavaHOME();
    end
    else success:=True;
  end
  else // if it is not defined at all
  begin
    success := SetJavaHOME();
  end
  
  if success = True then begin
    Result:=True;
  end
  else begin
   MsgBox('Unable to create JAVA_HOME. Setup will not continue', mbInformation, MB_OK);
  end
  Result := success;
end;

// updates the classpath with all the jars from {src}\lib
procedure UpdateClasspath();
var
  FindRec: TFindRec;
  OldClasspath: String;
begin
  Log('Updating classpath...');
      // if we can get the old classpath
      if RegQueryStringValue(RegistryRoot, RegistryEnvPath, 'CLASSPATH', OldClasspath) then
        begin
          RegWriteExpandStringValue(RegistryRoot, RegistryEnvPath, 'CLASSPATH',
	  OldClasspath + ';.;%TOM_HOME%\lib\tom-runtime-full.jar;%TOM_HOME%\lib\tools\org.eclipse.emf.ecore_2.5.0.v200906151043.jar;%TOM_HOME%\lib\tools\org.eclipse.emf.common_2.5.0.v200906151043.jar');
        end
      else
        begin
          RegWriteExpandStringValue(RegistryRoot, RegistryEnvPath, 'CLASSPATH', '.;%TOM_HOME%\lib\tom-runtime-full.jar;%TOM_HOME%\lib\tools\org.eclipse.emf.ecore_2.5.0.v200906151043.jar;%TOM_HOME%\lib\tools\org.eclipse.emf.common_2.5.0.v200906151043.jar');
        end
end;

// updates current setup state
procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then begin
    //FinishedInstall := True;
    UpdateClasspath();
  end
end;



