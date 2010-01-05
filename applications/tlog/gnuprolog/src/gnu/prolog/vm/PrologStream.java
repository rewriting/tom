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
import java.util.*;
public abstract class PrologStream
{
  public static class OpenOptions
  {
    public AtomTerm mode;
    public AtomTerm type = PrologStream.textAtom;
    public AtomTerm eofAction = PrologStream.eofCodeAtom;
    public AtomTerm reposition = PrologStream.falseAtom;
    public HashSet  aliases = new HashSet();
    public AtomTerm filename;
    public Environment environment;
  }

  
  // tags used to supply stream options
  public static final CompoundTermTag filenameTag=CompoundTermTag.get("file_name",1);
  public static final CompoundTermTag modeTag=CompoundTermTag.get("mode",1);
  public static final CompoundTermTag aliasTag=CompoundTermTag.get("alias",1);
  public static final CompoundTermTag positionTag=CompoundTermTag.get("position",1);
  public static final CompoundTermTag endOfStreamTag=CompoundTermTag.get("end_of_stream",1);
  public static final CompoundTermTag eofActionTag=CompoundTermTag.get("eof_action",1);
  public static final CompoundTermTag repositionTag=CompoundTermTag.get("reposition",1);
  public static final CompoundTermTag typeTag=CompoundTermTag.get("type",1);
  // atoms used for stream options
  public static final AtomTerm falseAtom = AtomTerm.get("false");
  public static final AtomTerm trueAtom = AtomTerm.get("true");
  public static final AtomTerm atAtom = AtomTerm.get("at");
  public static final AtomTerm pastAtom = AtomTerm.get("past");
  public static final AtomTerm notAtom = AtomTerm.get("not");
  public static final AtomTerm inputAtom = AtomTerm.get("input");
  public static final AtomTerm outputAtom = AtomTerm.get("output");
  public static final AtomTerm errorAtom = AtomTerm.get("error");
  public static final AtomTerm eofCodeAtom = AtomTerm.get("eof_code");
  public static final AtomTerm resetAtom = AtomTerm.get("reset");
  public static final AtomTerm repositionAtom = AtomTerm.get("reposition");
  public static final AtomTerm streamAtom = AtomTerm.get("stream");
  public static final AtomTerm textStreamAtom = AtomTerm.get("text_stream");
  public static final AtomTerm binaryStreamAtom = AtomTerm.get("binary_stream");
  public static final AtomTerm readAtom = AtomTerm.get("read");
  public static final AtomTerm userInputAtom = AtomTerm.get("user_input");
  public static final AtomTerm userOutputAtom = AtomTerm.get("user_output");
  public static final AtomTerm textAtom = AtomTerm.get("text");
  public static final AtomTerm binaryAtom = AtomTerm.get("binary");
  public static final AtomTerm appendAtom = AtomTerm.get("append");
  public static final AtomTerm streamOrAliasAtom = AtomTerm.get("stream_or_alias");
  public static final AtomTerm openAtom = AtomTerm.get("open");
  public static final AtomTerm sourceSinkAtom = AtomTerm.get("source_sink");
  public static final AtomTerm pastEndOfStreamAtom = AtomTerm.get("past_end_of_stream");
  public static final AtomTerm listAtom = AtomTerm.get("list");
  public static final AtomTerm atomAtom = AtomTerm.get("atom");
  public static final AtomTerm variableAtom = AtomTerm.get("variable");
  public static final AtomTerm writeAtom = AtomTerm.get("write");
  public static final AtomTerm endOfFileAtom = AtomTerm.get("end_of_file");

  protected AtomTerm filename;
  protected AtomTerm mode;
  protected AtomTerm reposition;
  protected AtomTerm eofAction;
  protected AtomTerm endOfStream = notAtom;
  protected AtomTerm type;
  protected HashSet  aliases;
  protected Term streamTerm = new JavaObjectTerm(this);
  protected boolean closed = false;
  protected Environment environment;

  protected PrologStream(OpenOptions options)
  {
    filename = options.filename;
    mode = options.mode == readAtom?inputAtom:outputAtom;
    reposition = options.reposition;
    eofAction = options.eofAction;
    type = options.type;
    aliases = new HashSet(options.aliases);
    environment = options.environment;
  }

  public void checkExists() throws PrologException
  {
    if (closed)
    {
      PrologException.existenceError(streamAtom, streamTerm);
    }
  }

  public Term getStreamTerm()
  {
    return streamTerm;
  }

  public AtomTerm getMode()
  {
    return mode;
  }


  public void getProperties(List list) throws PrologException
  {
    list.add(new CompoundTerm(filenameTag, filename));
    //list.add(new CompoundTerm(modeTag, mode));
    list.add(mode);
    for (Iterator i = aliases.iterator();i.hasNext();)
    {
      list.add(new CompoundTerm(aliasTag, (Term)i.next()));
    }
    if (reposition == trueAtom)
    {
      list.add(new CompoundTerm(positionTag, getPosition(streamTerm,null)));
    }
    list.add(new CompoundTerm(endOfStreamTag, getEndOfStreamState()));
    list.add(new CompoundTerm(eofActionTag, eofAction));
    list.add(new CompoundTerm(repositionTag, reposition));
    list.add(new CompoundTerm(typeTag, type));
  }

  public abstract int getByte(Term streamTerm, Interpreter interptreter) throws PrologException;
  public abstract int peekByte(Term streamTerm, Interpreter interptreter) throws PrologException;
  public abstract void putByte(Term streamTerm, Interpreter interptreter, int _byte) throws PrologException;
  
  public Term getEndOfStreamState() throws PrologException
  {
    checkExists();
    return endOfStream;
  }
  public abstract Term getPosition(Term streamTerm, Interpreter interptreter) throws PrologException;
  public abstract void setPosition(Term streamTerm, Interpreter interptreter, Term pos) throws PrologException;
  public abstract int getCode(Term streamTerm, Interpreter interptreter) throws PrologException;
  public abstract int peekCode(Term streamTerm, Interpreter interptreter) throws PrologException;
  public abstract void putCode(Term streamTerm, Interpreter interptreter, int code) throws PrologException;

  public abstract Term readTerm(Term streamTerm, Interpreter interptreter, ReadOptions options) throws PrologException;
  public abstract void writeTerm(Term streamTerm, Interpreter interptreter, WriteOptions options, Term term) throws PrologException;

  public abstract void flushOutput(Term streamTerm) throws PrologException;
  
  public void close(boolean force) throws PrologException
  {
    environment.close(this);
  }
}
