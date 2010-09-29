cd %TOM_HOME%\..\..\applications\polygraphes\build

java gui.MainGUI "%TOM_HOME%\..\..\applications\polygraphes\examples\testprogramv2.xml" "%TOM_HOME%\..\..\applications\polygraphes\build\foo\\"
pause
@rem double slash pour escape

@rem cd foo

@rem Echo Execution du script buildGuiTest.bat
@rem call %TOM_HOME%\..\..\applications\polygraphes\build\foo\buildGuiTest.bat
@rem Echo Fin de l'appel

@rem Echo lancement du programme java
@rem java program.GuiTest "%TOM_HOME%\..\..\applications\polygraphes\examples\GuiTest.xml" "%TOM_HOME%\..\..\applications\polygraphes\build\foo\result\\"
@rem pause