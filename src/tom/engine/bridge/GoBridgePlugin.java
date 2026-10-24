/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.bridge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import tom.engine.TomMessage;
import tom.engine.TomOptionManager;
import tom.engine.adt.tomsignature.types.KeyEntry;
import tom.engine.adt.tomtype.types.TomType;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.TomGenericPlugin;

/**
 * The bridge to the Go suffix of the compiler (J4, spec §4.1-4.2): run by
 * the platform before the plugin named by --goFrom, it writes the bundle
 * (options, used marks, types, symbol table, working term; one datum per
 * line) on the standard input of <tom.home>/bin/tomgo suffix (TOMGO_BIN
 * overrides it). Without --goCheck, the standard output of tomgo is the
 * generated file; with --goCheck, tomgo suffix --echo must give the bundle
 * back byte for byte and the Java chain goes on.
 */
public class GoBridgePlugin extends TomGenericPlugin {

  /** the bundle format version (first line) */
  public static final int VERSION = 1;

  /** suffix of the bundle written with --intermediate */
  public static final String BRIDGE_SUFFIX = ".tfix.bridge";

  /** the plugin before which the bridge runs, without spaces */
  private String from = "";

  /** the generated file (without --goCheck) */
  private String generatedFileName = null;

  public GoBridgePlugin() {
    super("GoBridgePlugin");
  }

  /** Set by the platform before run */
  public void setFrom(String from) {
    this.from = from;
  }

  public Object[] getArgs() {
    if(generatedFileName != null) {
      return new Object[]{generatedFileName};
    }
    return super.getArgs();
  }

  public void run(Map informationTracker) {
    String input = getStreamManager().getInputFileName();
    if(!getOptionBooleanValue("goCode")) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeNeedsGo);
      return;
    }
    String tomgo = System.getenv("TOMGO_BIN");
    if(tomgo == null || tomgo.length() == 0) {
      String home = System.getProperty("tom.home");
      tomgo = (home == null)
        ? "<tom.home unset>/bin/tomgo"
        : new File(new File(home, "bin"), "tomgo").getPath();
    }
    if(!new File(tomgo).isFile()) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeNotFound, tomgo);
      return;
    }
    boolean check = getOptionBooleanValue("goCheck");
    byte[] bundle;
    try {
      String text = bundle();
      if(text == null) {
        return;
      }
      bundle = text.getBytes("UTF-8");
      if(getOptionBooleanValue("intermediate")) {
        OutputStream out = new FileOutputStream(getStreamManager().getOutputFileName() + BRIDGE_SUFFIX);
        try {
          out.write(bundle);
        } finally {
          out.close();
        }
      }
    } catch(IOException e) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeIOException, tomgo, e.getMessage());
      return;
    }

    List<String> command = new ArrayList<String>();
    command.add(tomgo);
    command.add("suffix");
    if(check) {
      command.add("--echo");
    }
    long timeout = 300;
    String env = System.getenv("TOMGO_TIMEOUT");
    if(env != null && env.trim().length() > 0) {
      try {
        timeout = Long.parseLong(env.trim());
      } catch(NumberFormatException e) {
        timeout = 300;
      }
    }
    final long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeout);
    final byte[][] stdoutBox = new byte[1][];
    List<String> stderr = new ArrayList<String>();
    int status = -1;
    Process proc = null;
    boolean finished = false;
    boolean timedOut = false;
    try {
      final Process p = new ProcessBuilder(command).start();
      proc = p;
      final byte[] toSend = bundle;
      Thread writer = new Thread() {
        public void run() {
          OutputStream in = p.getOutputStream();
          try {
            in.write(toSend);
          } catch(IOException e) {
            // tomgo may stop reading early: its status tells why
          } finally {
            try { in.close(); } catch(IOException e) { }
          }
        }
      };
      final List<String> errLines = stderr;
      Thread reader = new Thread() {
        public void run() {
          try {
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
            String line;
            while((line = r.readLine()) != null) {
              synchronized(errLines) {
                errLines.add(line);
              }
            }
            r.close();
          } catch(IOException e) {
            synchronized(errLines) {
              errLines.add("stderr: " + e.getMessage());
            }
          }
        }
      };
      Thread outReader = new Thread() {
        public void run() {
          try {
            stdoutBox[0] = readAll(p.getInputStream());
          } catch(IOException e) {
            stdoutBox[0] = null;
          }
        }
      };
      writer.setDaemon(true);
      reader.setDaemon(true);
      outReader.setDaemon(true);
      writer.start();
      reader.start();
      outReader.start();
      if(p.waitFor(timeout, TimeUnit.SECONDS)) {
        long left = Math.max(1, TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()));
        outReader.join(left);
        reader.join(left);
        writer.join(left);
        if(outReader.isAlive() || reader.isAlive()) {
          // a grandchild kept the pipes open
          timedOut = true;
        } else {
          status = p.exitValue();
          finished = true;
        }
      } else {
        timedOut = true;
      }
    } catch(IOException e) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeIOException, tomgo, e.getMessage());
      return;
    } catch(InterruptedException e) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeIOException, tomgo, e.getMessage());
      return;
    } finally {
      if(proc != null && !finished) {
        proc.destroyForcibly();
      }
    }
    if(timedOut) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeTimeout, tomgo, String.valueOf(timeout));
      return;
    }
    byte[] stdout = stdoutBox[0];
    if(stdout == null) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeIOException, tomgo, "cannot read standard output");
      return;
    }

    // relay the diagnostics of tomgo: ERROR|WARNING <name> <file> <line> <message>
    boolean reported = false;
    StringBuilder errText = new StringBuilder();
    for(String line : stderr) {
      String[] f = line.split(" ", 5);
      boolean isError = f.length == 5 && f[0].equals("ERROR");
      boolean isWarning = f.length == 5 && f[0].equals("WARNING");
      if(isError || isWarning) {
        String file = f[2].equals("-") ? input : f[2];
        int lineNumber = 0;
        try {
          lineNumber = Integer.parseInt(f[3]);
        } catch(NumberFormatException e) {
          lineNumber = 0;
        }
        if(isError) {
          TomMessage.error(getLogger(), file, lineNumber, TomMessage.goBridgeError, f[1], f[4]);
          reported = true;
        } else {
          TomMessage.warning(getLogger(), file, lineNumber, TomMessage.goBridgeError, f[1], f[4]);
        }
      } else {
        if(errText.length() > 0) {
          errText.append('\n');
        }
        errText.append(line);
      }
    }
    if(status != 0) {
      if(!reported) {
        TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeFailure,
            String.valueOf(status), errText.toString());
      }
      return;
    }
    if(reported) {
      return;
    }

    try {
      if(check) {
        String diff = firstDifference(bundle, stdout);
        if(diff != null) {
          int sep = diff.indexOf(' ');
          TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeMismatch,
              diff.substring(0, sep), diff.substring(sep + 1));
        }
        return;
      }
      String encoding = (String)getOptionValue("encoding");
      File output = getStreamManager().getOutputFile();
      Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), encoding));
      try {
        writer.write(new String(stdout, "UTF-8"));
      } finally {
        writer.close();
      }
      generatedFileName = output.getCanonicalPath();
    } catch(IOException e) {
      TomMessage.error(getLogger(), input, 0, TomMessage.goBridgeIOException, tomgo, e.getMessage());
    }
  }

  /** The bundle (spec §4.2), or null after an error */
  private String bundle() throws IOException {
    StringBuilder b = new StringBuilder();
    b.append("tom-bridge ").append(VERSION).append('\n');
    b.append("from ").append(from).append('\n');
    b.append("input ").append(new File(getStreamManager().getInputFileName()).getCanonicalPath()).append('\n');
    b.append("output ").append(getStreamManager().getOutputFile().getCanonicalPath()).append('\n');
    TomOptionManager om = (TomOptionManager)getOptionManager();
    for(String name : om.getOptionNames()) {
      Object value = om.getOptionValue(name);
      b.append("option ").append(name).append(' ');
      if(value instanceof Boolean) {
        b.append("bool ").append(value);
      } else if(value instanceof Integer) {
        b.append("int ").append(value);
      } else {
        b.append("string ").append(quote(String.valueOf(value)));
      }
      b.append('\n');
    }
    SymbolTable table = getSymbolTable();
    // The "used" marks are computed by Java only in the backend
    // (BackendPlugin.markUsedConstructorDestructor, just before generation):
    // a bundle cut before Backend carries none, by construction. The Go
    // backend will have to recompute them from the code.
    for(KeyEntry e : table.getUsedKeyEntries()) {
      if(!line(b, "used", e)) {
        return null;
      }
    }
    for(TomType t : table.getTypeEntries()) {
      if(!line(b, "type", t)) {
        return null;
      }
    }
    if(!line(b, "table", table.toTerm()) || !line(b, "code", getWorkingTerm())) {
      return null;
    }
    b.append("end\n");
    return b.toString();
  }

  /** Appends "key term\n"; a term that does not fit on one line is an error */
  private boolean line(StringBuilder b, String key, Object term) {
    String s = term.toString();
    if(s.indexOf('\n') >= 0) {
      TomMessage.error(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.goBridgeBadTerm, key);
      return false;
    }
    b.append(key).append(' ').append(s).append('\n');
    return true;
  }

  /** A Go-readable string literal (strconv.Unquote) */
  static String quote(String s) {
    StringBuilder b = new StringBuilder("\"");
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch(c) {
        case '\\': b.append("\\\\"); break;
        case '"':  b.append("\\\""); break;
        case '\n': b.append("\\n"); break;
        case '\r': b.append("\\r"); break;
        case '\t': b.append("\\t"); break;
        default:
          if(c < 0x20) {
            b.append(String.format("\\u%04x", (int)c));
          } else {
            b.append(c);
          }
      }
    }
    return b.append('"').toString();
  }

  private static byte[] readAll(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buf = new byte[1 << 16];
    int n;
    while((n = in.read(buf)) > 0) {
      out.write(buf, 0, n);
    }
    in.close();
    return out.toByteArray();
  }

  /**
   * null when sent and received are identical, otherwise "<line> <text>":
   * the 1-based number of the first differing line and the received line
   * (truncated to 200 characters).
   */
  static String firstDifference(byte[] sent, byte[] received) throws IOException {
    if(java.util.Arrays.equals(sent, received)) {
      return null;
    }
    String[] a = new String(sent, "UTF-8").split("\n", -1);
    String[] b = new String(received, "UTF-8").split("\n", -1);
    int i = 0;
    while(i < a.length && i < b.length && a[i].equals(b[i])) {
      i++;
    }
    String got = (i < b.length) ? b[i] : "<end of output>";
    if(got.length() > 200) {
      got = got.substring(0, 200) + "...";
    }
    return (i + 1) + " " + got;
  }
}
