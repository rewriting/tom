bash clean.sh class

if [ "$1" != "nogom" ]; then
  gom sort.gom --withCongruenceStrategies
fi

tom RandomizerGenerator.t  && tom Representation.t && tom Bug.t && tom BugLight.t && tom Main.t && javac Main.java && javac Bug.java && javac BugLight.java