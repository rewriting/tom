if [ "$1" = "class" ]; then
  rm -f *.class
  rm -f definitions/*.class
  rm -f sort/*.class
  rm -f sort/types/*.class
  rm -f sort/types/expr/*.class
  rm -f sort/types/surexpr/*.class
  rm -f sort/strategy/*.class
  rm -f sort/strategy/expr/*.class
  rm -f sort/strategy/surexpr/*.class
fi

if [ "$1" = "" ]; then
  rm -f *.class
  rm -f *.java
  
  rm -f -r randomtermgeneration
  rm -f -r sort
  rm -f -r test
  rm -f *.dot
  rm -f definitions/*.class
fi

if [ "$1" = "all" ]; then
  rm -f *.class
  rm -f *.java

  rm -f -r randomtermgeneration
  rm -f -r sort
  rm -f -r test
  rm -f *.dot
  rm -f definitions/*.class
fi