bash clean.sh class

if [ "$1" != "nogom" ]; then
  gom sort.gom --withCongruenceStrategies
fi

tom RandomizerGenerator.t  && tom Representation.t && tom Main.t && javac Main.java