; when buiding a new install file:
; 1. make sure you have run a stable.dist
; 2. change AppVerName
; 3. change OutputBaseFilename

[Setup]
AppName=Tom
AppVerName=Tom version 2.4
DefaultDirName={pf}\INRIA\Tom
DefaultGroupName=Tom
UninstallDisplayIcon={app}\Tom.exe
SourceDir=../../stable/dist
OutputBaseFilename=TOM_2_4_setup
OutputDir=../../utils/installKit
LicenseFile=LICENCE

[Files]
Source: "AUTHORS";      DestDir: "{app}"
Source: "Gom.xml";      DestDir: "{app}"
Source: "INSTALL";      DestDir: "{app}"
Source: "LICENCE";      DestDir: "{app}"
Source: "NEWS";         DestDir: "{app}"
Source: "README";       DestDir: "{app}"; DestName: "README.txt"; Flags: isreadme
Source: "Tom.xml";      DestDir: "{app}"
Source: "bin\*";        DestDir: "{app}\bin"
Source: "lib\*";        DestDir: "{app}\lib"
Source: "share\*";      DestDir: "{app}\share"; Flags: recursesubdirs createallsubdirs

[Registry]
; add TOM_HOME to environmental variables
Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: string; ValueName: "TOM_HOME"; ValueData: "{app}"; Flags: deletevalue
; add %TOM_HOME%\bin to PATH variable
Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};%TOM_HOME%\bin"
; add %JAVA_HOME%\bin to PATH variable
Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "{olddata};%JAVA_HOME%\bin"


[Code]
var
  FinishedInstall: Boolean;
// abort setup if JAVA_HOME not defined
function InitializeSetup(): Boolean;
begin
  if Length(Trim(GetEnv('JAVA_HOME'))) = 0 then begin
    MsgBox('Make sure that Java is installed on your system and that JAVA_HOME is correctly defined', mbInformation, MB_OK);
    Result:=False;
  end else Result:=True;
end;

// updates the classpath with all the jars from {src}\lib
procedure UpdateClasspath();
var
  FindRec: TFindRec;
  JarName: String;
  OldClasspath: String;
  TomLib: String;
begin
  TomLib := '';
  if FindFirst(ExpandConstant('{app}\lib\*.jar'), FindRec) then begin
    try
      repeat
        TomLib := '%TOM_HOME%\lib\' + FindRec.Name + ';' + TomLib + ';';
      until not FindNext(FindRec);
      // if we can get the old classpath
      if RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
            , 'CLASSPATH', OldClasspath) then
        begin
           RegWriteExpandStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
            , 'CLASSPATH', OldClasspath + ';%TOM_LIB%');
        end;
      RegWriteExpandStringValue(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment'
              , 'TOM_LIB', TomLib + '.;');
    finally
      FindClose(FindRec);
    end;
  end;
end;

procedure DeinitializeSetup();
begin
  // only if the setup was completed
  if FinishedInstall then begin
    UpdateClasspath();
  end;
end;

// updates current setup state
procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
    FinishedInstall := True;
end;



