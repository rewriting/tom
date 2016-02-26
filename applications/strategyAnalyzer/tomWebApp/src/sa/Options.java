package sa;

public class Options {

    public boolean verbose = false;

    public boolean debug = false;

    public boolean withAP = false;

    public boolean withAT = false;

    public boolean aprove = true;

    public boolean timbuk = false;

    public boolean metalevel = false;

    public int level = 100;

    public boolean approx = false;

    public boolean withType = false;

    public boolean minimize = false;

    public boolean ordered = false;

    public boolean pattern = false;
    
    public boolean tom = false;

    public String classname = null;

    public void setOptions(boolean verbose, boolean debug, boolean withAP, boolean withAT, boolean aprove, boolean timbuk, boolean metalevel, boolean approx, boolean withType, boolean minimize, boolean ordered, boolean pattern, boolean tom, String classname) {
      this.verbose = verbose;

      this.debug = debug;

      this.withAP = withAP;

      this.withAT = withAT;

      this.aprove = aprove;

      this.timbuk = timbuk;

      this.metalevel = metalevel;

      this.approx = approx;

      this.withType = withType;

      this.minimize = minimize;

      this.ordered = ordered;

      this.pattern = pattern;

      this.classname = classname;

      if(tom) {
        this.classname = "tom";
      }
    }
}
