package sa;

import org.kohsuke.args4j.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Options {
  @Option(name="--withAP",usage="generate rules with anti-patterns")
    public boolean withAP;

  @Option(name="--aprove",usage="generate rules for Aprove")
    public boolean aprove;

  @Option(name="-o",usage="output to this file",metaVar="OUTPUT")
    public File out = new File(".");

  @Option(name="-i",usage="input this file",metaVar="INPUT")
    public File in = new File(".");

  // receives other command line parameters than options
  @Argument
    public List<String> arguments = new ArrayList<String>();
}
