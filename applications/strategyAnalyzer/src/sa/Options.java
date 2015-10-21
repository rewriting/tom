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

  @Option(name="-timbuk",usage="generate rules for Timbuk")
    public boolean timbuk = false;

  @Option(name="-metalevel",usage="use a metalevel encoding of strategies")
    public boolean metalevel = false;

  @Option(name="-tom",usage="generate a Tom program")
    public String classname;

  @Option(name="-o",usage="output to this file",metaVar="OUTPUT")
    public File out = null;

  @Option(name="-i",usage="input this file",metaVar="INPUT")
    public File in = null;

  @Option(name="-l",usage="anti-pattern level",metaVar="LEVEL")
    public int level = 100;

  @Option(name="-approx",usage="compilation does not respects the reduction in the original system")
    public boolean approx = false;

  @Option(name="-typed",usage="generate typed signature",metaVar="TYPE")
    public String type = null;

  @Option(name="-ordered",usage="compilation produces a TRS which should be evaluated in the specified order")
    public boolean ordered = false;
  @Option(name="-d", usage="define directory in which to place output")
    public String directory = null;

  // receives other command line parameters than options
  @Argument
    public List<String> arguments = new ArrayList<String>();
}
