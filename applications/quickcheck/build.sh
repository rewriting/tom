bash clean.sh class

mkdir src/gen
mkdir build

echo ""
echo "GOM"
echo ""

if [ "$1" != "nogom" ]; then
  gom src/gom/sort.gom --withCongruenceStrategies -d src
  mkdir src/tom/sort
  mv src/sort/Sort.tom src/tom/sort/Sort.tom
fi

echo ""
echo "TOM"
echo ""

tom src/tom/RandomizerGenerator.t -o src/gen/RandomizerGenerator.java
tom src/tom/Representation.t -o src/gen/Representation.java
tom src/tom/Bug.t -o src/gen/Bug.java
tom src/tom/BugLight.t -o src/gen/BugLight.java
tom src/tom/Main.t -o src/gen/Main.java
#cd src

echo ""
echo "JAVAC"
echo ""

javac src/gen/Main.java -d build -sourcepath src
javac src/gen/Bug.java -d build -sourcepath src
javac src/gen/BugLight.java -d build -sourcepath src
