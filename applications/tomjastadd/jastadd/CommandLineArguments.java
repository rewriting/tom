package jastadd;
/*
 * Compilers and Interpreters
 *
 * GH 001106 Created
 */

/**
 * <P>A class for retrieving the command line arguments such as operands
 * and options.</P>
 *
 * <P>The intention is to follow POSIX conventions, but the implementation so
 * far is not complete, supporting only long options and operands.</P>
 *
 * <P> A long option is preceeded by two hyphens and is written
 * --name[=value] on the commandline, e.g.
 * <PRE>
 *   cmd --temperature=37 </PRE>
 *
 * <P> This example tests if the "temperature" option occurs in the commandline
 * <PRE>
 *   CommandLineArguments CLAs = new CommandLineArguments(args);
 *   if (CLAs.hasLongOption("temperature")) {
 *      ...
 *   };
 * </PRE>
 * </P>
 * <P> This example gets the value of the "temperature" option. If there is
 * no such option in the commandline, or it is not given any value,
 * the given default value "40" is returned.
 * <PRE>
 *   temp = CLAs.getLongOptionValue("temperature", "40");
 * </PRE>
 * </P>
 *
 * <P> Operands are normally used for filenames and occur after the options
 * on the command line, e.g.
 * <PRE>
 *   cmd --opt1 --opt2 file1 file2 file3
 * </PRE>
 * </P>
 * <P> The following example retrieves the number of operands and gets each
 * of them in a loop.
 * <PRE>
 *   CommandLineArguments CLAs = new CommandLineArguments(args);
 *   int n = CLAs.getNumOperands();
 *   for (int k=0; k&lt;n; k++) {
 *     ... = CLAs.getOperand(k);
 *   };
 * </PRE>
 * </P>
 */

public class CommandLineArguments {
  
  // ---------------------------------------------------- PUBLIC CONSTRUCTORS

  /**
   * @param args Use the args parameter from your main method.
   */
  public CommandLineArguments(String[] args) {
    theArgs = args;
  };
  

  // --------------------------------------------------------- PUBLIC METHODS

  /**
   * Returns true if the long option longopt occurs in the command line.
   */
  public boolean hasLongOption(String longopt) {
    String prefix = "--" + longopt;
    for (int k=0; k<theArgs.length; k++) {
      if (theArgs[k].equals(prefix) || theArgs[k].startsWith(prefix+"="))
        return true;
    };
    return false;
  };
  
  /**
   * Returns the value of the long option longopt. If there is no such option,
   * or if it has no associated value, the empty string is returned.
   */
  public String getLongOptionValue(String longopt) {
    String prefix = "--" + longopt + "=";
    for (int k=0; k<theArgs.length; k++) {
      if (theArgs[k].startsWith(prefix))
        return theArgs[k].substring(prefix.length());
    };
    return "";
  };
  
  /**
   * Returns the value of the long option longopt. If there is no such option,
   * or if it has no associated value, defaultValue is returned.
   */
  public String getLongOptionValue(String longopt, String defaultValue) {
    String val = getLongOptionValue(longopt);
    if (val=="")
      return defaultValue;
    else
      return val;
  };
  
  /**
   * Returns the number of operands at the end of the command line.
   */
  public int getNumOperands() {
    int lastOption = -1;
    for (int k=0; k<theArgs.length; k++) {
      if (!theArgs[k].startsWith("-"))
        return theArgs.length-k;
    };
    return 0;
  };
  
  /**
   * Returns the k'th operand at the end of the command line,
   * where the operands are numbered from 0 to getNumOperands()-1.
   */
  public String getOperand(int k) {
    return theArgs[theArgs.length-getNumOperands()+k];
  };
  
  // -------------------------------------------------------- PRIVATE METHODS

  
  // ----------------------------------------------------- PRIVATE ATTRIBUTES
  
  private String[] theArgs;
  
};
