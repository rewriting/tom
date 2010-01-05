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
import java.io.*;
import gnu.prolog.io.*;

public class BinaryPrologStream extends PrologStream
{
  RandomAccessFile file;
  
  public BinaryPrologStream(AtomTerm fileAtom, AtomTerm mode, OpenOptions options) throws PrologException
  {
    super(options);
    String of = (mode == readAtom)?"r":"rw";
    try
    {
      file = new RandomAccessFile(fileAtom.value,of);
      if (mode == appendAtom)
      {
        file.seek(file.length());
      }
      else if (mode == writeAtom)
      {
        file.setLength(0); // truncate
      }
    }
    catch(Exception ex)
    {
    }
  }

  public int getByte(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    if (mode == inputAtom)
    {
      getEndOfStreamState();
      if (endOfStream == pastAtom)
      {
        PrologException.permissionError(inputAtom,
                                        pastEndOfStreamAtom,
                                        streamTerm);
      }
      else if (endOfStream == atAtom)
      {
        endOfStream = pastAtom;
        return -1;
      }
      else
      {
        try
        {
          return file.read();
        }
        catch(IOException ex)
        {
          PrologException.systemError();
        }
      }
    }
    else
    {
      PrologException.permissionError(inputAtom, streamAtom, streamTerm);
    }
    return -1; //fake return
  }
    
  public int peekByte(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    checkExists();
    if (mode == inputAtom)
    {
      getEndOfStreamState();
      if (endOfStream == pastAtom)
      {
        PrologException.permissionError(inputAtom,
                                        pastEndOfStreamAtom,
                                        streamTerm);
      }
      else if (endOfStream == atAtom)
      {
        endOfStream = pastAtom;
        return -1;
      }
      else
      {
        try
        {
          long pos = file.getFilePointer();
          int rc = file.read();
          file.seek(pos);
          return rc;
        }
        catch(IOException ex)
        {
          PrologException.systemError();
        }
      }
    }
    else
    {
      PrologException.permissionError(inputAtom, streamAtom, streamTerm);
    }
    return -1; //fake 
  }
  public void putByte(Term streamTerm, Interpreter interptreter, int _byte) throws PrologException
  {
    checkExists();
    if (mode == outputAtom)
    {
      try
      {
        file.write(_byte);
      }
      catch(IOException ex)
      {
        PrologException.systemError();
      }
    }
    else
    {
      PrologException.permissionError(outputAtom, streamAtom, streamTerm);
    }
  }
  public Term getPosition(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    try
    {
      return new JavaObjectTerm(new Long(file.getFilePointer()));
    }
    catch(IOException ex)
    {
      PrologException.systemError();
    }
    return null; //fake
  }
  public void setPosition(Term streamTerm, Interpreter interptreter, Term position) throws PrologException
  {
    try
    {
      if (reposition == falseAtom)
      {
        PrologException.permissionError(repositionAtom,streamAtom,getStreamTerm());
      }
      if (position instanceof VariableTerm)
      {
        PrologException.instantiationError();
      }
      else if (!(position instanceof JavaObjectTerm))
      {
        PrologException.domainError(TermConstants.streamPositionAtom, position);
      }
      JavaObjectTerm jt = (JavaObjectTerm)position;
      if (!(jt.value  instanceof Long))
      {
        PrologException.domainError(TermConstants.streamPositionAtom, position);
      }
      long pos =  ((Long)jt.value).longValue();
      if (pos > file.length())
      {
        PrologException.domainError(TermConstants.streamPositionAtom, position);
      }
      file.seek(pos);
    }
    catch(IOException ex)
    {
      PrologException.systemError();
    }
  }
  
  public int getCode(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    PrologException.permissionError(inputAtom, binaryStreamAtom, streamTerm);
    return -1;
  }
  public int peekCode(Term streamTerm, Interpreter interptreter) throws PrologException
  {
    PrologException.permissionError(inputAtom, binaryStreamAtom, streamTerm);
    return -1;
  }
  public void putCode(Term streamTerm, Interpreter interptreter, int code) throws PrologException
  {
    PrologException.permissionError(outputAtom, binaryStreamAtom, streamTerm);
  }

  public gnu.prolog.term.Term readTerm(Term streamTerm, gnu.prolog.vm.Interpreter i, gnu.prolog.io.ReadOptions o) throws PrologException
  {
    PrologException.permissionError(inputAtom, binaryStreamAtom, streamTerm);
    return null;
  }
  public void writeTerm(Term streamTerm, gnu.prolog.vm.Interpreter i, gnu.prolog.io.WriteOptions o, gnu.prolog.term.Term t)  throws PrologException
  {
    PrologException.permissionError(outputAtom, binaryStreamAtom, streamTerm);
  }

  public void flushOutput(Term streamTerm) throws PrologException
  {
    if (mode == inputAtom)
    {
      checkExists();
      PrologException.permissionError(outputAtom, streamAtom, streamTerm);
    }
    else
    {
      // do nothing RAF cannot be flushed
    }
  }
  
  public void close(boolean force) throws PrologException
  {
    try
    {
      file.close();
    }
    catch(IOException ex)
    {
      if (!force)
      {
        return;
      }
    }
    super.close(force);
  }

  public Term getEndOfStreamState() throws PrologException
  {
    try
    {
      long length = file.length();
      long pos = file.getFilePointer();
      if (pos < length)
      {
        endOfStream = notAtom;
      }
      else if (endOfStream == pastAtom)
      {
        if (eofAction == resetAtom || eofAction == eofCodeAtom)
        {
          endOfStream = atAtom;
        }
      }
    }
    catch(IOException ex)
    {
      PrologException.systemError();
    }
    return super.getEndOfStreamState();
  }

}
