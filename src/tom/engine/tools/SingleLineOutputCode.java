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

public final class SingleLineOutputCode extends OutputCode {

  public SingleLineOutputCode(Writer file) {
    super(file);
  }

  protected void internalWriteln() throws IOException {
  }

  public void writeln(String s) throws IOException {
    write(s);
    writeln();
  }
  
  public void write(int deep,String s, int line, int length) throws IOException {
    String s2 = s.replace('\n', ' ');
    s2 = s2.replace('\t', ' ');
    writeln(s2);
  }
}
