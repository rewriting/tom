/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.tools;

import java.io.*;

public final class OutputCode {
  private Writer file;
  private int lineCounter = 0;
  
  public OutputCode(Writer file) {
    this.file = file;
  }

  public OutputCode() {
    this.file = new StringWriter();
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
  public void writeOpenCBrace() throws IOException {
    file.write('{');
  }
  public void writeCloseCBrace() throws IOException {
    file.write('}');
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
      //try {
      file.write(s);
        //} catch (IOException e) {
        //System.out.println("write error");
        //e.printStackTrace();
        //}
  }

  public void write(int n) throws IOException {
    write(Integer.toString(n));
  }

  public void write(int deep,String s) throws IOException {
    indent(deep);
    write(s);
  }

  public void writeln() throws IOException {
    file.write('\n');
    lineCounter++;
  }

  public void writeln(String s) throws IOException {
    write(s);
    writeln();
  }

  public void writeln(int deep,String s) throws IOException {
    write(deep,s);
    writeln();
  }

  public void write(int deep,String s, String Line, Integer length) throws IOException {
    int line = Integer.valueOf(Line).intValue();
    if (line == lineCounter) {
      write(0, "/*" + "Line: "+line+" Length:"+ length.intValue()+"*/");
      write(deep,s);
      lineCounter+= length.intValue();
    } else if (line > lineCounter) {
      int diff = line - lineCounter;
      for(int i=0;i<diff;i++) {
        write("/* newline : Line: "+line+" Length:"+ length.intValue()+" LineCounter:"+ lineCounter+"*/");
        writeln();
      }
      write(deep,s);
      lineCounter+= length.intValue();
    } else{
      System.out.println("Synchronization issue:"+s);
      write(deep,s);
      writeln();
    }
//    write(deep,s);
//   writeln();
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
      for(int i=0 ; i<deep ; i++) {
        file.write(' ');
        file.write(' ');
      }
    } catch (IOException e) {
      System.out.println("write error");
      e.printStackTrace();
    }
  }

}
