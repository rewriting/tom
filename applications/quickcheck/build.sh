bash clean.sh class

mkdir src/gen
mkdir build

echo ""
echo "GOM"
echo ""

if [ "$1" != "nogom" ]; then
  gom src/gom/sort.gom -d src
  gom src/gom/system.gom -d src
	mkdir src/tom/sort
  mkdir src/tom/system
	mv src/sort/Sort.tom src/tom/sort/Sort.tom
	mv src/system/System.tom src/tom/system/System.tom
fi

echo ""
echo "TOM"
echo ""

#tom src/tom/RandomizerGenerator.t -o src/gen/RandomizerGenerator.java
#tom src/tom/Representation.t -o src/gen/Representation.java
#tom src/tom/Bug.t -o src/gen/Bug.java
#tom src/tom/BugLight.t -o src/gen/BugLight.java
tom src/tom/TestGen.t -o src/gen/TestGen.java
#tom src/tom/Main.t -o src/gen/Main.java
tom src/tom/Interpretation.t -o src/gen/Interpretation.java
#tom src/tom/Shrink.t -o src/gen/Shrink.java
tom src/tom/ExamplesFormula.t -o src/gen/ExamplesFormula.java


echo ""
echo "JAVAC"
echo ""

#javac src/gen/Main.java -d build -sourcepath src
#javac src/gen/Bug.java -d build -sourcepath src
#javac src/gen/BugLight.java -d build -sourcepath src
#javac src/gen/TestGen.java -d build -sourcepath src
#javac src/gen/Interpretation.java -d build -sourcepath src
#javac src/gen/Shrink.java -d build -sourcepath src
#javac src/gen/ExamplesFormula.java -d build -sourcepath src
