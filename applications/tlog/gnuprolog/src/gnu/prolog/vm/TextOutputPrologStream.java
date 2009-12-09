/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text ol license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.vm;
import gnu.prolog.term.*;
import gnu.prolog.io.*;
import java.io.*;

public class TextOutputPrologStream extends PrologStream
{
  TermWriter termWriter; 
  TextOutputPrologStream(OpenOptions options, Writer wr)
  {
    super(options);
    endOfStream = atAtom;
    termWriter = new TermWriter(wr);
  }

  public int getByte(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    PrologException.permissionError(inputAtom, textStreamAtom, streamTerm);
    return 0;
  }
  public int peekByte(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    PrologException.permissionError(inputAtom, textStreamAtom, streamTerm);
    return 0;
  }

  public void putByte(Term streamTerm, Interpreter interptreter, int _byte) throws PrologException
  {
    checkExists();
    PrologException.permissionError(outputAtom, textStreamAtom, streamTerm);
  }
  
  public Term getPosition(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    PrologException.permissionError(repositionAtom, textStreamAtom, streamTerm);
    return null;
  }
  public void setPosition(Term streamTerm, Interpreter interptreter, Term pos) throws PrologException
  {
    checkExists();
    PrologException.permissionError(repositionAtom, textStreamAtom, streamTerm);
  }
  
  public int getCode(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    PrologException.permissionError(inputAtom, streamAtom, streamTerm);
    return 0;
  }
  public int peekCode(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    PrologException.permissionError(inputAtom, streamAtom, streamTerm);
    return 0;
  }
  public void putCode(Term streamTerm, Interpreter interptreter, int code) throws PrologException
  {
    termWriter.print((char)code);
  }
  public Term readTerm(Term streamTerm, Interpreter interptreter, ReadOptions options) throws PrologException
  {
    PrologException.permissionError(inputAtom, streamAtom, streamTerm);
    return null;
  }
  public void writeTerm(Term streamTerm, Interpreter interptreter, WriteOptions options, Term term) throws PrologException
  {
    termWriter.print(options, term);
  }
  public void flushOutput(Term streamTerm) throws PrologException
  {
    termWriter.flush();
  }
  public void close(boolean force) throws PrologException
  {
    try
    {
      termWriter.close();
    }
    catch(Exception ex)
    {
      if (!force)
      {
        PrologException.systemError();
      }
    }
    super.close(force);
  }
}
