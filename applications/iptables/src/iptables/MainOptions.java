package iptables;

import org.kohsuke.args4j.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class MainOptions {
  @Option(name="-help",usage="print usage")
    public boolean help;

  @Option(name="-h",usage="print usage")
    public boolean h;

  @Option(name="-o",usage="output to this file",metaVar="OUTPUT")
    public File out = null;

  @Option(name="-i",usage="input this file",metaVar="INPUT")
    public File in = null;

  // receives other command line parameters than options
  @Argument
    public List<String> arguments = new ArrayList<String>();
}
