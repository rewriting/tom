import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import tom.engine.TomStreamManager;
import tom.engine.backend.BackendPlugin;
import tom.engine.tools.SymbolTable;
import tom.engine.adt.code.types.Code;
import tom.engine.adt.tomsignature.types.KeyEntry;
import tom.engine.adt.tomsignature.types.TomSymbolTable;
import tom.engine.adt.tomtype.types.TomType;

/**
 * The state the Collector of BackendPlugin leaves (J4.1b,
 * test/tools/j4/collector_test.go; backend.CollectorDump writes the same
 * lines in Go): the used marks (getUsedKeyEntries), then the inliners
 * (mapInliner sorted by key). The private markUsedConstructorDestructor
 * and SymbolTable.mapInliner are reached by reflection.
 * Args: <outDir> <bundle>...; the k-th bundle (from 0) gives
 * <outDir>/<k>.txt in UTF-8.
 */
public class CollectorState {
  public static void main(String[] args) throws Exception {
    // the Collector prints "No moduleName in stack" on System.out: keep it out
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    for(int k = 1; k < args.length; k++) {
      String res = run(args[k]);
      Writer w = new OutputStreamWriter(new FileOutputStream(new File(args[0], (k-1) + ".txt")), "UTF-8");
      w.write(res);
      w.close();
    }
  }

  @SuppressWarnings("unchecked")
  static String run(String bundle) throws Exception {
    TomStreamManager sm = new TomStreamManager();
    SymbolTable st = sm.getSymbolTable();
    st.init(new StubOptionManager().set("goCode", Boolean.TRUE));
    Code code = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(bundle), "UTF-8"));
    for(String line; (line = in.readLine()) != null; ) {
      int sp = line.indexOf(' ');
      String key = sp < 0 ? line : line.substring(0, sp);
      String rest = sp < 0 ? "" : line.substring(sp + 1);
      if(key.equals("used")) {
        KeyEntry ke = KeyEntry.fromString(rest);
        if(ke.isUsedSymbolConstructor()) {
          st.setUsedSymbolConstructor(ke.getSymbol());
        } else if(ke.isUsedSymbolDestructor()) {
          st.setUsedSymbolDestructor(ke.getSymbol());
        } else if(ke.isUsedSymbolAC()) {
          st.setUsedSymbolAC(ke.getSymbol());
        } else {
          st.setUsedType(ke.getTypedef());
        }
      } else if(key.equals("type")) {
        TomType t = TomType.fromString(rest);
        st.putType(t.getTomType(), t);
      } else if(key.equals("table")) {
        st.fromTerm(TomSymbolTable.fromString(rest));
      } else if(key.equals("code")) {
        code = Code.fromString(rest);
      }
    }
    in.close();
    BackendPlugin bp = new BackendPlugin();
    bp.setStreamManager(sm);
    Method mark = BackendPlugin.class.getDeclaredMethod("markUsedConstructorDestructor", Code.class);
    mark.setAccessible(true);
    mark.invoke(bp, code);
    StringBuilder out = new StringBuilder();
    for(KeyEntry ke : st.getUsedKeyEntries()) {
      out.append("used ").append(ke).append('\n');
    }
    Field f = SymbolTable.class.getDeclaredField("mapInliner");
    f.setAccessible(true);
    Map<String,String> inliner = new TreeMap<String,String>((Map<String,String>) f.get(st));
    for(Map.Entry<String,String> e : inliner.entrySet()) {
      out.append("inliner ").append(Quote.quote(e.getKey())).append(' ')
         .append(Quote.quote(e.getValue())).append('\n');
    }
    return out.toString();
  }
}
