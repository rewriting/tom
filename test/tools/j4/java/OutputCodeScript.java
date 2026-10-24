import java.io.*;

import tom.engine.tools.OutputCode;

/**
 * Replays OutputCode scripts (J4.1a, test/tools/j4/outputcode_test.go).
 * Args: <scripts file> <results file>, both UTF-8. A script starts with
 * "script go|string <pretty>" and ends with "end"; the operations are
 * write <q>, int <n>, deep <d> <q>, tl <d> <line> <length> <q>, writeln,
 * open, closebrace, coma, single, unsingle (q: a string quoted by
 * Quote.quote). For each script, one line "result <q>" is written.
 */
public class OutputCodeScript {
  public static void main(String[] args) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
    Writer out = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8");
    OutputCode oc = null;
    for(String line; (line = in.readLine()) != null; ) {
      String[] f = line.split(" ", 2);
      String op = f[0];
      String rest = f.length > 1 ? f[1] : "";
      if(op.equals("script")) {
        String[] g = rest.split(" ");
        if(g[0].equals("go")) {
          StubOptionManager om = new StubOptionManager()
            .set("cCode", Boolean.FALSE).set("aCode", Boolean.FALSE).set("pCode", Boolean.FALSE)
            .set("pretty", Boolean.valueOf(g[1].equals("true"))).set("goCode", Boolean.TRUE);
          oc = new OutputCode(new StringWriter(), om);
        } else {
          oc = new OutputCode(new StringWriter());
        }
      } else if(op.equals("write")) {
        oc.write(Quote.unquote(rest));
      } else if(op.equals("int")) {
        oc.write(Integer.parseInt(rest));
      } else if(op.equals("deep")) {
        String[] g = rest.split(" ", 2);
        oc.write(Integer.parseInt(g[0]), Quote.unquote(g[1]));
      } else if(op.equals("tl")) {
        String[] g = rest.split(" ", 4);
        oc.write(Integer.parseInt(g[0]), Quote.unquote(g[3]), Integer.parseInt(g[1]), Integer.parseInt(g[2]));
      } else if(op.equals("writeln")) {
        oc.writeln();
      } else if(op.equals("open")) {
        oc.writeOpenBrace();
      } else if(op.equals("closebrace")) {
        oc.writeCloseBrace();
      } else if(op.equals("coma")) {
        oc.writeComa();
      } else if(op.equals("single")) {
        oc.setSingleLine();
      } else if(op.equals("unsingle")) {
        oc.unsetSingleLine();
      } else if(op.equals("end")) {
        out.write("result " + Quote.quote(oc.stringDump()) + "\n");
        oc = null;
      } else {
        throw new RuntimeException("bad operation: " + line);
      }
    }
    out.close();
  }
}
