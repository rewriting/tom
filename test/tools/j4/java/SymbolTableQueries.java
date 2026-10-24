import java.io.*;
import java.util.*;

import tom.engine.TomBase;
import tom.engine.tools.SymbolTable;
import tom.engine.adt.code.types.BQTerm;
import tom.engine.adt.code.types.Code;
import tom.engine.adt.tomexpression.types.Expression;
import tom.engine.adt.tomname.types.TomNumberList;
import tom.engine.adt.tomoption.types.OptionList;
import tom.engine.adt.tomsignature.types.KeyEntry;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.tomsignature.types.TomSymbolTable;
import tom.engine.adt.tomslot.types.PairNameDecl;
import tom.engine.adt.tomslot.types.PairNameDeclList;
import tom.engine.adt.tomterm.types.TomTerm;
import tom.engine.adt.tomtype.types.TomType;
import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;

/**
 * Answers the queries of the backend on SymbolTable and TomBase (J4.1a,
 * test/tools/j4/queries_test.go; goQueries writes the same lines in Go).
 * Args: <outDir> <bundle>...; for the k-th bundle (from 0) it writes
 * <outDir>/<k>.txt in UTF-8.
 */
public class SymbolTableQueries {
  interface Query { Object get(); }

  private static String q(Query f) {
    try {
      Object o = f.get();
      return o == null ? "null" : o.toString();
    } catch(RuntimeException e) {
      return "!error";
    }
  }

  private static final String[] NAMES = {"int", "long", "float", "char", "double", "boolean", "String",
    "universal", "void", "intarray", "unknown type", "nosuchtype"};

  private final SymbolTable st = new SymbolTable();
  private final StringBuilder out = new StringBuilder();
  private int index = 0;

  private void emit(String s) { out.append(s).append('\n'); }

  public static void main(String[] args) throws Exception {
    // TomBase and SymbolTable print some errors on System.out: keep them out
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    for(int k = 1; k < args.length; k++) {
      String res = new SymbolTableQueries().run(args[k]);
      Writer w = new OutputStreamWriter(new FileOutputStream(new File(args[0], (k-1) + ".txt")), "UTF-8");
      w.write(res);
      w.close();
    }
  }

  private String run(String bundle) throws IOException {
    st.init(new StubOptionManager().set("goCode", Boolean.TRUE));
    String input = "", inputName = "";
    Code code = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(bundle), "UTF-8"));
    for(String line; (line = in.readLine()) != null; ) {
      int sp = line.indexOf(' ');
      String key = sp < 0 ? line : line.substring(0, sp);
      String rest = sp < 0 ? "" : line.substring(sp + 1);
      if(key.equals("input")) {
        input = rest;
      } else if(key.equals("inputname")) {
        inputName = Quote.unquote(rest);
      } else if(key.equals("used")) {
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
    symbols();
    types();
    emit("builtins " + st.getIntType() + " " + st.getIntArrayType() + " " + st.getLongType() + " " + st.getFloatType()
        + " " + st.getCharType() + " " + st.getDoubleType() + " " + st.getBooleanType() + " " + st.getStringType()
        + " " + st.getUniversalType() + " " + st.getVoidType() + " " + st.getIntArrayOp());
    for(KeyEntry ke : st.getUsedKeyEntries()) {
      emit("usedEntry " + ke);
    }
    for(TomType t : st.getTypeEntries()) {
      emit("typeEntry " + t);
    }
    emit("table " + st.toTerm());
    walk(code);
    emit("prefix " + TomBase.goDeclPrefix(input) + " " + TomBase.goDeclPrefix(inputName));
    return out.toString();
  }

  private void symbols() {
    for(final String name : st.keySymbolIterable()) {
      final TomSymbol s = st.getSymbolFromName(name);
      final TomType codomain = TomBase.getSymbolCodomain(s);
      emit("symbol " + name);
      emit("  codomain " + codomain);
      emit("  domain " + TomBase.getSymbolDomain(s));
      emit("  tomType " + q(() -> TomBase.getTomType(codomain)));
      emit("  tlType " + q(() -> TomBase.getTLType(codomain)));
      emit("  used " + st.isUsedSymbolConstructor(s) + " " + st.isUsedSymbolDestructor(s) + " " + st.isUsedSymbolAC(s)
          + " " + st.isUsedSymbolConstructor(name) + " " + st.isUsedSymbolDestructor(name));
      int n = s.getPairNameDeclList().length();
      for(int i = 0; i <= n; i++) {
        final int k = i;
        emit("  slotName " + i + " " + q(() -> TomBase.getSlotName(s, k)));
      }
      for(PairNameDeclList l = s.getPairNameDeclList(); !l.isEmptyconcPairNameDecl(); l = l.getTailconcPairNameDecl()) {
        final PairNameDecl p = l.getHeadconcPairNameDecl();
        emit("  slot " + p.getSlotName() + " index " + TomBase.getSlotIndex(s, p.getSlotName())
            + " type " + q(() -> TomBase.getSlotType(s, p.getSlotName())));
      }
    }
  }

  private void types() {
    TreeSet<String> names = new TreeSet<String>(Arrays.asList(NAMES));
    for(TomType t : st.getTypeEntries()) {
      if(t.isType()) {
        names.add(t.getTomType());
      }
    }
    for(String name : st.keySymbolIterable()) {
      TomType c = TomBase.getSymbolCodomain(st.getSymbolFromName(name));
      if(c.isType()) {
        names.add(c.getTomType());
      }
    }
    for(final String n : names) {
      emit("type " + n + " get " + q(() -> st.getType(n)) + " used " + st.isUsedType(n)
          + " builtin " + st.isBuiltinType(n) + " unknown " + st.isUnknownType(n)
          + " builtinType " + q(() -> st.getBuiltinType(n))
          + " is " + st.isIntType(n) + " " + st.isLongType(n) + " " + st.isFloatType(n) + " " + st.isCharType(n)
          + " " + st.isStringType(n) + " " + st.isBooleanType(n) + " " + st.isDoubleType(n));
    }
  }

  private void walk(final Object node) {
    if(!(node instanceof Visitable) || node instanceof VisitableBuiltin) {
      return;
    }
    int i = index++;
    if(node instanceof TomTerm) {
      emit("termType T " + i + " " + q(() -> TomBase.getTermType((TomTerm)node, st)));
    }
    if(node instanceof BQTerm) {
      emit("termType B " + i + " " + q(() -> TomBase.getTermType((BQTerm)node, st)));
    }
    if(node instanceof Expression) {
      emit("termType E " + i + " " + q(() -> TomBase.getTermType((Expression)node, st)));
    }
    if(node instanceof TomNumberList) {
      emit("numberList " + i + " " + q(() -> TomBase.tomNumberListToString((TomNumberList)node)));
    }
    if(node instanceof OptionList) {
      emit("moduleName " + i + " " + q(() -> TomBase.getModuleName((OptionList)node)));
    }
    Visitable v = (Visitable)node;
    for(int k = 0; k < v.getChildCount(); k++) {
      walk(v.getChildAt(k));
    }
  }
}
