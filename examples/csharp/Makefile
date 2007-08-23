all: Test.exe

clean:
	rm -r term *.dll *.exe 

Test.exe: Test.cs term.dll tom-dist.dll
	gmcs -r:tom-dist.dll -r:term.dll Test.cs

Test.cs: Test.t term
	tom -csCode -o Test.cs Test.t
	sed -i -e 's/instanceof/is/g' Test.cs
	sed -i -e 's/boolean/bool/g' Test.cs
	sed -i -e 's/equals/Equals/g' Test.cs
	sed -i -e 's/super/base/g' Test.cs  # should not exist, bad backend/compiler design

term: term.gom
	gom term.gom

term.dll: term tom-dist.dll
	javac term/*.java
	find term -name "*.class" | xargs ikvmc -target:library -out:term.dll -r:tom-dist.dll

tom-dist.dll:
	ikvmc -target:library -out:tom-dist.dll ${TOM_HOME}/lib/*.jar

