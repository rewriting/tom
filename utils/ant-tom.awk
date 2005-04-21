# use it with: 
# ant -find build.xml -emacs | awk -f ${TOM_SRC_HOME}/utils/ant-tom.awk

BEGIN {
  # set field separator and output field separator to ':', to extract filenames
   FS = ":";
   OFS = ":";
}

$1~/.*\/[a-zA-Z][a-zA-Z0-9]*\.java/ {
  file = $1
  sub("\.java$",".t",file)
  sub("gen\/","",file)
  if (!system("test -f "file)) 
    {
      $1 = file
    }
}

(NF >= 0) {
      print $0
 }
