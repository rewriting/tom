rm -f -r build

if [ "$1" != "class" ]; then
  rm -f -r src/gen
  rm -f -r src/sort
  rm -f -r src/system
  rm -f -r src/tom/sort
  rm -f -r src/tom/system
  rm -f *.dot
  rm -f -r dist
fi
