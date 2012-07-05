bash clean.sh class

mkdir gen
mkdir build

if [ "$1" != "nogom" ]; then
  gom gom/sort.gom --withCongruenceStrategies
  mkdir tom/sort
  mv sort/Sort.tom tom/sort/Sort.tom
fi

tom tom/RandomizerGenerator.t -o gen/RandomizerGenerator.java
tom tom/Representation.t -o gen/Representation.java
tom tom/Bug.t -o gen/Bug.java
tom tom/BugLight.t -o gen/BugLight.java
tom tom/Main.t -o gen/Main.java
javac gen/Main.java -d build
javac gen/Bug.java -d build
javac gen/BugLight.java -d build