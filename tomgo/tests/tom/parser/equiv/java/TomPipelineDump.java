// TomPipelineDump runs the TOM compiler pipeline up to a chosen phase and
// prints the resulting `Code` AST to stdout via `code.toString()` — same
// byte-stable, aterm.jar-free format as TomParseDump.
//
// Unlike `tom.engine.Tom`, this runner does NOT load Tom.config (which fails
// on JDK 11+ with aterm's BAF format bug). It hand-wires the plugins:
// TomStreamManager + parser + the requested suffix of the chain. Each plugin
// in the chain has the `[Code, TomStreamManager]` arg-shape (per
// TomGenericPlugin.setArgs), apart from TomParserPlugin which only takes
// the stream manager.
//
// Usage: java TomPipelineDump <input.t> <phase>
//   where <phase> is one of: parsed | transformed | synchecked | desugared | typed
//
// Exits 0 on success, 1 on plugin error, 2 on usage error.

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import tom.engine.TomStreamManager;
import tom.engine.parser.TomParserPlugin;
import tom.engine.transformer.TransformerPlugin;
import tom.engine.checker.SyntaxCheckerPlugin;
import tom.engine.desugarer.DesugarerPlugin;
import tom.engine.typer.TyperPlugin;
import tom.engine.adt.code.types.Code;
import tom.platform.ConfigurationManager;
import tom.platform.OptionManager;
import tom.platform.adt.platformoption.types.PlatformOptionList;

public class TomPipelineDump {

  /** Same minimal HashMap-backed OptionManager as TomParseDump — no plugin
   * discovery, no dependency resolution, just key/value storage. Booleans
   * default to FALSE for any unset key. */
  static class MapOptionManager implements OptionManager {
    private final Map<String, Object> values = new HashMap<>();
    private final List<String> inputs = new ArrayList<>();

    @Override public int initialize(ConfigurationManager cm, String[] cli) { return 0; }
    @Override public void setGlobalOptionList(PlatformOptionList list) {}
    @Override public List<String> getInputToCompileList() { return inputs; }
    @Override public void setOptionValue(String name, Object value) { values.put(name, value); }
    @Override public Object getOptionValue(String name) {
      Object v = values.get(name);
      if (v == null) return Boolean.FALSE;
      return v;
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("usage: TomPipelineDump <input.t> <phase>");
      System.err.println("  <phase>: parsed | transformed | synchecked | desugared | typed");
      System.exit(2);
    }
    String inputFile = args[0];
    String phase     = args[1];

    MapOptionManager om = new MapOptionManager();
    // Mirror TomParseDump's seeded option set; add newtyper=TRUE for the typer.
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
    om.setOptionValue("jCode", Boolean.TRUE);
    om.setOptionValue("cCode", Boolean.FALSE);
    om.setOptionValue("camlCode", Boolean.FALSE);
    om.setOptionValue("pCode", Boolean.FALSE);
    om.setOptionValue("aCode", Boolean.FALSE);
    om.setOptionValue("parse", Boolean.FALSE);
    om.setOptionValue("newparser", Boolean.TRUE);
    om.setOptionValue("tomjava", Boolean.FALSE);
    om.setOptionValue("printcst", Boolean.FALSE);
    om.setOptionValue("printast", Boolean.FALSE);
    // Typer: pick the modern path (TyperPlugin falls through with a
    // "Sorry no typer!!!" message otherwise).
    om.setOptionValue("oldtyper", Boolean.FALSE);
    om.setOptionValue("newtyper", Boolean.TRUE);
    // SyntaxChecker honours noSyntaxCheck; leave default (FALSE) to run.

    // TomParserTool.parseGomFile (the path taken when a `%gom { ... }`
    // block appears) reads option "X" expecting a string. With our
    // MapOptionManager's Boolean.FALSE default this would crash with
    // a ClassCastException. Resolve the bundled Tom.xml under
    // utils/eclipse-plugin/plugin/config/ if present so the inline-gom
    // path stays happy.
    String repoRoot = System.getProperty("tomgo.repoRoot");
    if (repoRoot == null || repoRoot.isEmpty()) {
      repoRoot = guessRepoRoot();
    }
    if (repoRoot != null) {
      String tomXml = repoRoot + "/utils/eclipse-plugin/plugin/config/Tom.xml";
      if (new File(tomXml).isFile()) {
        om.setOptionValue("X", tomXml);
      }
      // NOTE: `tom.home` is intentionally NOT set. Setting it would
      // make TomParserTool successfully invoke the embedded Gom
      // platform on `%gom { ... }` blocks and emit a TomInclude
      // carrying the generated type declarations — useful for
      // fixtures like TestSublists, but disruptive for the simpler
      // fixtures (Test.t etc.) where the empty AbstractBlock is the
      // expected shape. Setting tom.home is a per-fixture opt-in.
    }

    // The parser prints a one-line "antlr4: <file> parsing + building cst:…"
    // stat onto stdout (CstConverter.convert in stable Tom). Redirect stdout
    // to /dev/null while the plugins run so only the final term.toString()
    // reaches the caller. Stderr (logger output) is left untouched.
    PrintStream realOut = System.out;
    PrintStream nullOut = new PrintStream(new OutputStream() {
      @Override public void write(int b) {}
      @Override public void write(byte[] b, int off, int len) {}
    });
    System.setOut(nullOut);
    try {
      TomStreamManager sm = new TomStreamManager();
      sm.initializeFromOptionManager(om);
      sm.prepareForInputFile(inputFile);
      // Mirror the JVM-property convention Java's TomStreamManager
      // uses: when `tom.home` is set, getImportList appends
      // $TOM_HOME/share/tom and $TOM_HOME/share/tom/<lang>. We do the
      // same manually by extending the userImportList with the
      // repo's bundled mappings.
      if (repoRoot != null) {
        List<File> imports = new ArrayList<>(sm.getUserImportList());
        File langDir = new File(repoRoot, "utils/eclipse-plugin/plugin/include/java");
        File rootDir = new File(repoRoot, "utils/eclipse-plugin/plugin/include");
        if (langDir.isDirectory()) imports.add(langDir);
        if (rootDir.isDirectory()) imports.add(rootDir);
        sm.setUserImportList(imports);
      }

      // Parser: only plugin with the non-standard [TomStreamManager]
      // setArgs shape.
      TomParserPlugin parser = new TomParserPlugin();
      parser.setOptionManager(om);
      parser.setArgs(new Object[] { sm });
      parser.run(new HashMap<>());
      Code term = (Code) parser.getWorkingTerm();
      if (term == null) {
        System.err.println("TomPipelineDump: parser produced null term");
        System.exit(1);
      }
      if ("parsed".equals(phase))      { System.setOut(realOut); realOut.print(term.toString()); return; }

      term = runStandard(new TransformerPlugin(),  om, term, sm);
      if ("transformed".equals(phase)) { System.setOut(realOut); realOut.print(term.toString()); return; }

      term = runStandard(new SyntaxCheckerPlugin(), om, term, sm);
      if ("synchecked".equals(phase)) { System.setOut(realOut); realOut.print(term.toString()); return; }

      term = runStandard(new DesugarerPlugin(),    om, term, sm);
      if ("desugared".equals(phase))   { System.setOut(realOut); realOut.print(term.toString()); return; }

      term = runStandard(new TyperPlugin(),        om, term, sm);
      if ("typed".equals(phase))       { System.setOut(realOut); realOut.print(term.toString()); return; }

      System.setOut(realOut);
      System.err.println("TomPipelineDump: unknown phase: " + phase);
      System.exit(2);
    } catch (Throwable t) {
      System.setOut(realOut);
      System.err.println("TomPipelineDump: pipeline failed at or before phase '" + phase + "': " + t);
      t.printStackTrace(System.err);
      System.exit(1);
    }
  }

  /** Run a downstream plugin (anything past Parser): the canonical
   * [Code, TomStreamManager] arg-shape from TomGenericPlugin.setArgs. */
  private static Code runStandard(Object plugin, MapOptionManager om, Code in, TomStreamManager sm) throws Exception {
    // All engine plugins extend TomGenericPlugin; call via reflection so
    // we don't need a common interface beyond what's already public.
    plugin.getClass().getMethod("setOptionManager", OptionManager.class).invoke(plugin, om);
    plugin.getClass().getMethod("setArgs", Object[].class).invoke(plugin, (Object) new Object[] { in, sm });
    plugin.getClass().getMethod("run", Map.class).invoke(plugin, new HashMap<>());
    return (Code) plugin.getClass().getMethod("getWorkingTerm").invoke(plugin);
  }

  /** Walk up from the JVM's working directory until we find the
   * marker file `tomgo/go.mod` — same trick the Go side uses to
   * locate the repo root from arbitrary cwd. Returns null if not
   * found (the configuration knobs above then silently degrade). */
  private static String guessRepoRoot() {
    File cur = new File(System.getProperty("user.dir"));
    while (cur != null) {
      if (new File(cur, "tomgo/go.mod").isFile()) {
        return cur.getAbsolutePath();
      }
      cur = cur.getParentFile();
    }
    return null;
  }
}
