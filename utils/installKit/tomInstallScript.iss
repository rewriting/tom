[Setup]
AppName=Tom
AppVerName=Tom version 2.4
DefaultDirName={pf}\INRIA\Tom
DefaultGroupName=Tom
UninstallDisplayIcon={app}\Tom.exe
SourceDir=../../stable/dist
LicenseFile=LICENCE

[Files]
Source: "AUTHORS";      DestDir: "{app}"
Source: "Gom.xml";      DestDir: "{app}"
Source: "INSTALL";      DestDir: "{app}"
Source: "LICENCE";      DestDir: "{app}"
Source: "NEWS";         DestDir: "{app}"
Source: "README";       DestDir: "{app}\README.txt"; Flags: isreadme
Source: "Tom.xml";      DestDir: "{app}"
Source: "bin\*";        DestDir: "{app}\bin"
Source: "lib\*";        DestDir: "{app}\lib"
Source: "share\*";      DestDir: "{app}\share"; Flags: recursesubdirs createallsubdirs

; TODO - abort setup if java not installed

[Registry]
; add TOM_HOME to environmental variables
Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: string; ValueName: "TOM_HOME"; ValueData: "{app}"; Flags: deletevalue
; add %TOM_HOME%\bin to PATH variable
Root: HKLM; Subkey:"SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; ValueType: expandsz; ValueName: "Path"; ValueData: "%TOM_HOME%\bin"
