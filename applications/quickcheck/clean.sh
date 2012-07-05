rm -f -r build

if [ "$1" != "class" ]; then
  rm -f -r gen
  rm -f -r sort
  rm -f -r tom/sort
fi