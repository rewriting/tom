//TODO: rename level to depth

package tomchecker;

import org.kohsuke.args4j.*;
import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class Options {
  @Option(name="-help",usage="print usage")
  public boolean help;

  @Option(name="-h",usage="print usage")
  public boolean h;

  @Option(name="-impt",usage="import types", multiValued = true)
  public List<String> importTypes;

  @Option(name="-impp",usage="imports package", multiValued = true)
  public List<String> importPackage;

  @Option(name="-i",usage="input this file",metaVar="INPUT")
  public File in = null;

  @Option(name="-pkg",usage="package")
  public String pkg = "tested";

  @Option(name="-l",usage="test level",metaVar="LEVEL")
  public int level = 5;

  @Option(name="-v",usage="verbose")
  public int v = 0;
  
  @Option(name="-s",usage="shrink")
  public int s = 0;
  
  @Option(name="-q",usage="quotient")
  public int q = 2;

  @Option(name="-small",usage="small")
  public boolean small = false;

  @Option(name="-quick",usage="quick")
  public boolean quick = false;
  
  @Option(name="-printj",usage="print java")
  public boolean printj = false;
  
  @Option(name="-numt",usage="used to compute number of tests")
  public int numOfTest = 1000;
  
  // receives other command line parameters than options
  @Argument
  public List<String> arguments = new ArrayList<String>();
}
