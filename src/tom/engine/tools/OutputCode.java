/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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

package tom.engine.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import tom.platform.OptionManager;

public class OutputCode {
  protected Writer file;
  private OptionManager optionManager;

  private int lineCounter = 1;
  private int singleLine = 0;

  private boolean pretty = false;
  private boolean indent = false;
  private boolean cCode  = false;
  private boolean aCode  = false;

  public OutputCode(Writer file, OptionManager optionManager) {
    this.file = file;
    this.optionManager = optionManager;
    this.cCode  = ((Boolean)optionManager.getOptionValue("cCode")).booleanValue();
    this.aCode  = ((Boolean)optionManager.getOptionValue("aCode")).booleanValue();
    this.indent = ((Boolean)optionManager.getOptionValue("pCode")).booleanValue() || this.aCode;
    this.pretty = ((Boolean)optionManager.getOptionValue("pretty")).booleanValue();
  }

  public OutputCode(Writer file) {
    this.file = file;
  }

  public void setSingleLine() {
    this.singleLine++;
  }

  public void unsetSingleLine() {
    this.singleLine--;
  }

  public Writer getFile() {
    return file;
  }

  public void writeSpace() throws IOException {
    file.write(' ');
  }
  public void writeOpenBrace() throws IOException {
    file.write('(');
  }
  public void writeCloseBrace() throws IOException {
    file.write(')');
  }
  public void writeComa() throws IOException {
    file.write(',');
  }
  public void writeSemiColon() throws IOException {
    file.write(';');
  }
  public void writeUnderscore() throws IOException {
    file.write('_');
  }

  public void write(String s) throws IOException {
    try {
      file.write(s);
    } catch (IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }

  public void write(int n) throws IOException {
    write(Integer.toString(n));
  }

  public void write(int deep,String s) throws IOException {
    indent(deep);
    write(s);
  }

  public void writeln() throws IOException {
    if(pretty) {
      file.write('\n');
    }
  }

  public void writeln(String s) throws IOException {
    write(s);
    writeln();
  }

  public void writeln(int deep,String s) throws IOException {
    write(deep,s);
    writeln();
  }

  public void write(StringBuilder s) throws IOException {
    write(s.toString());
  }

  public void write(int deep,StringBuilder s) throws IOException {
    write(deep,s.toString());
  }

  public void writeln(StringBuilder s) throws IOException {
    writeln(s.toString());
  }

  public void writeln(int deep,StringBuilder s) throws IOException {
    writeln(deep,s.toString());
  }

  public void write(int deep, StringBuilder s, int line, int length) throws IOException {
    write(deep, s.toString(), line, length);
  }

  private String makeSingleLine(String s) {
    s = s.replace('\n', ' ');
    s = s.replace('\r', ' ');
    s = s.replace('\t', ' ');
    return s;
  }

  public void write(int deep, String s, int line, int length) throws IOException {
    if(aCode && pretty) {
      String[] lines = s.split("\n", -1);
      if(lines.length==1) { //one line
        String s2 = s.replaceFirst("^\\s+","");
        if(!s2.equals("")) {
          write(deep, s);
        } else {
          indent = true;
        }
      } else { //several lines
        for(int i=0;i<lines.length-1;i++) {
          String ln = lines[i];
          ln = ln.replaceFirst("^\\s+",""); // removes spaces at the beginning of the line
          writeln(deep, ln);
          indent = true;
        }
        String s2 = lines[lines.length-1].replaceFirst("^\\s+","");
        if(!s2.equals("")) {
          write(deep, s2);
          indent = false;
        } else {
          indent = true;
        }
      }
    } else if(!pretty) {
      if(cCode) {
        String s1 = "\n#line "+line+"\n";
        s = s1+s;
        write(s);
        lineCounter+= length;
      } else if(singleLine>0 || lineCounter>line) {
        // put everything on a single line
        length = 0;
        write(makeSingleLine(s));
      } else if(lineCounter <= line) {
        while(lineCounter < line) {
          write("\n");
          lineCounter++;
        }
        write(s);
        lineCounter+= length;
      } 
    } else {
      String[] lines = s.split("\n");
      //System.out.println("s = '" + s + "'");
      //System.out.println("length = " + lines.length);
      if(lines.length==0) {
        write(deep, s.replaceFirst("^\\s+",""));
      } else {
        for (int i=0;i<lines.length;i++) {
          String ln = lines[i];
          ln = ln.replaceFirst("^\\s+",""); // removes spaces at the beginning of the line
          writeln(deep, ln);
        }
      }
    }
  }

  public void close() {
    try {
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("close error");
      e.printStackTrace();
    }
  }

  public String stringDump() {
    try {
      if(file instanceof StringWriter) {
        file.flush();
        return file.toString();
      } else {
        throw new InternalError("OutputCode does not contain any string");
      }
    } catch (IOException e) {
      System.out.println("stringDump error");
      e.printStackTrace();
      return null;
    }
  }

  public void indent(int deep) {
    try {
      if(indent) {
        if(pretty) {
          for(int i=0 ; i<deep ; i++) {
            file.write("    ");
          }
        } else {
          file.write(' ');
        }
      }
    } catch (IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }

}
