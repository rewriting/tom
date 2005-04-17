# use it with: 
# ant -find build.xml -emacs | awk -f ${TOM_SRC_HOME}/utils/ant-tom.awk

BEGIN {
   FS = ":";
   OFS = ":";
}

/[a-zA-Z][a-zA-Z0-9]+\.java/ {
  file = $1
  sub("\.java",".t",file)
  sub("gen\/","",file)
  if (!system("test -f "file)) 
    {
      $1 = file
    }
  print $0
}

(NF <= 1) {
      print $0
 }