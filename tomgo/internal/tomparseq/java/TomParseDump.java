// TomParseDump runs the TOM parser plugin (antlr4 newparser, mode `-np`) on a
// single .t file and prints the resulting `Code` AST to stdout via
// `code.toString()` — the Gom-canonical `Op(arg1,arg2,…)` format, byte-stable
// and free of any aterm.jar dependency (so the JDK 11+ `Null charset name` bug
// in aterm.stream.BufferedOutputStreamWriter never fires).
//
// It does NOT use tom.engine.Tom / Tom.config / PluginPlatform: those force a
// full pipeline with cross-plugin option dependencies (`optimize2` and friends)
// that would require shipping every plugin. Instead it provides a tiny
// `OptionManager` implementation backed by a plain HashMap, seeds every option
// the parser/streamManager/symbolTable actually consult, instantiates the
// parser plugin directly, and prints the resulting term.
//
// Usage: java TomParseDump <input.t>
//
// Exit 0 on success (term printed to stdout), 1 on parse error or exception.

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import tom.engine.TomStreamManager;
import tom.engine.parser.TomParserPlugin;
import tom.engine.adt.code.types.Code;
import tom.platform.ConfigurationManager;
import tom.platform.OptionManager;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption;

public class TomParseDump {

  /** OptionManager backed by a HashMap — no plugin discovery, no dependency
   * resolution, just key/value storage. setGlobalOptionList and
   * initialize(ConfigurationManager, String[]) are stubbed: the caller seeds
   * every option it cares about via setOptionValue before invoking the parser. */
  static class MapOptionManager implements OptionManager {
    private final Map<String, Object> values = new HashMap<>();
    private final List<String> inputs = new ArrayList<>();

    @Override public int initialize(ConfigurationManager cm, String[] cli) { return 0; }
    @Override public void setGlobalOptionList(PlatformOptionList list) {}
    @Override public List<String> getInputToCompileList() { return inputs; }
    @Override public void setOptionValue(String name, Object value) { values.put(name, value); }
    @Override public Object getOptionValue(String name) {
      Object v = values.get(name);
      // Sensible defaults for any option the parser/streamManager peeks at
      // without us having set it explicitly — Boolean.FALSE, "" or 0.
      if (v == null) return Boolean.FALSE;
      return v;
    }
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: TomParseDump <input.t>");
      System.exit(2);
    }
    String inputFile = args[0];

    MapOptionManager om = new MapOptionManager();
    // Options consulted by TomStreamManager.initializeFromOptionManager and
    // TomParserPlugin.run. Values picked so that:
    //   * jCode=true selects the Java host language (the only one we exercise)
    //   * newparser=true selects the antlr4 island parser
    //   * tomjava=false / parse=false disable the other two parser modes
    //   * intermediate=false avoids the aterm.jar serialization bug
    //   * destdir="." / encoding="UTF-8" / output="" / import="" are the defaults
    om.setOptionValue("verbose", Boolean.FALSE);
    om.setOptionValue("wall", Boolean.FALSE);
    om.setOptionValue("intermediate", Boolean.FALSE);
    om.setOptionValue("eclipse", Boolean.FALSE);
    om.setOptionValue("noDeclaration", Boolean.FALSE);
    om.setOptionValue("pretty", Boolean.FALSE);
    om.setOptionValue("noStatic", Boolean.FALSE);
    om.setOptionValue("protected", Boolean.FALSE);
    om.setOptionValue("multithread", Boolean.FALSE);
    om.setOptionValue("help", Boolean.FALSE);
    om.setOptionValue("version", Boolean.FALSE);
    om.setOptionValue("optimize", Boolean.FALSE);
    om.setOptionValue("optimize2", Boolean.FALSE);
    om.setOptionValue("inlineplus", Boolean.FALSE);
    om.setOptionValue("genIntrospector", Boolean.FALSE);
    om.setOptionValue("inline", Boolean.FALSE);
    om.setOptionValue("nodefaultlayout", Boolean.FALSE);
    om.setOptionValue("import", "");
    om.setOptionValue("output", "");
    om.setOptionValue("destdir", ".");
    om.setOptionValue("encoding", "UTF-8");
    // Host language flags consumed by TomStreamManager.
    om.setOptionValue("jCode", Boolean.TRUE);
    om.setOptionValue("cCode", Boolean.FALSE);
    om.setOptionValue("camlCode", Boolean.FALSE);
    om.setOptionValue("pCode", Boolean.FALSE);
    om.setOptionValue("aCode", Boolean.FALSE);
    // Parser-plugin mode flags.
    om.setOptionValue("parse", Boolean.FALSE);
    om.setOptionValue("newparser", Boolean.TRUE);
    om.setOptionValue("tomjava", Boolean.FALSE);
    om.setOptionValue("printcst", Boolean.FALSE);
    om.setOptionValue("printast", Boolean.FALSE);

    try {
      TomStreamManager sm = new TomStreamManager();
      sm.initializeFromOptionManager(om);
      sm.prepareForInputFile(inputFile);

      TomParserPlugin plugin = new TomParserPlugin();
      plugin.setOptionManager(om);
      // TomParserPlugin.setArgs expects arg[0] = TomStreamManager (it overrides
      // the generic [Code, TomStreamManager] signature inherited from
      // TomGenericPlugin).
      plugin.setArgs(new Object[] { sm });
      plugin.run(new HashMap<>());

      Object term = plugin.getWorkingTerm();
      if (term == null) {
        System.err.println("TomParseDump: parser produced null term");
        System.exit(1);
      }
      // Canonical Op(arg1,arg2,…) format via toStringBuilder — same shape as
      // tomast.Code.String() on the Go side.
      System.out.print(term.toString());
    } catch (Throwable t) {
      System.err.println("TomParseDump: parser failed: " + t);
      t.printStackTrace(System.err);
      System.exit(1);
    }
  }
}
