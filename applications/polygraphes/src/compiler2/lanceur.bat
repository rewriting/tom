@rem : fichier original linux
@rem ant
@rem cd build
@rem java compiler/Compiler ~/workspace/jtom/applications/polygraphes/examples/testprogramv2.xml ~/workspace/jtom/applications/polygraphes/build/foo/
@rem cd foo
@rem sh ./buildTestProgramv2.sh
@rem java program/TestProgramv2 ~/workspace/jtom/applications/polygraphes/examples/XMLinput.xml ~/workspace/jtom/applications/polygraphes/build/foo/result/ 

cd "%TOM_HOME%\..\..\applications\polygraphes\build

@rem : il doit demander les deux paths
java compiler2.Compiler

@rem devrait marcher
java compiler2.Compiler "%TOM_HOME%\..\..\applications\polygraphes\examples\testprogramv2.xml" "%TOM_HOME%\..\..\applications\polygraphes\build\foo\\"
@rem double slash pour escape

cd foo

Echo Execution du script buildTestProgramv2.bat
call buildTestProgramv2.bat
Echo Fin de l'appel

Echo lancement du programme java
java program.TestProgramv2
java program.TestProgramv2 "%TOM_HOME%\..\..\applications\polygraphes\examples\XMLinput.xml" "%TOM_HOME%\..\..\applications\polygraphes\build\foo\result\\"
pause