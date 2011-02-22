package tom.engine.prettyprinter;

import ppeditor.*;

public class OutputFormatter{

  static String fileName;
  static PPCursor theCursor;
  
  public OutputFormatter(String n) {

    this.fileName=n;
    this.theCursor=new PPCursor(0,0);
    this.theCursor.setInsertion(false);
  }

  public void write(String text, PPTextPosition position) {

    theCursor.setPosition(position);
    theCursor.write(text);
  }

  public StringBuffer dump(){

    return theCursor.dump("$TOM_HOME"+this.fileName);
  }

} 
