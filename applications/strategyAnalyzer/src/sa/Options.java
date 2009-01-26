package sa;

import org.kohsuke.args4j.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Options {
  @Option(name="-help",usage="print usage")
    public boolean help;

  @Option(name="-h",usage="print usage")
    public boolean h;

  @Option(name="-withAP",usage="generate rules with anti-patterns")
    public boolean withAP = false;

  @Option(name="-withAT",usage="generate rules with at-annotations")
    public boolean withAT = false;

  @Option(name="-aprove",usage="generate rules for Aprove")
    public boolean aprove = false;
  
  @Option(name="-tom",usage="generate a Tom program")
    public String classname;

  @Option(name="-o",usage="output to this file",metaVar="OUTPUT")
    public File out = null;

  @Option(name="-i",usage="input this file",metaVar="INPUT")
    public File in = null;

  // receives other command line parameters than options
  @Argument
    public List<String> arguments = new ArrayList<String>();
}
