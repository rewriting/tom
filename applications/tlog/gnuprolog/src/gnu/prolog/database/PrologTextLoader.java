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
package gnu.prolog.database;
import  gnu.prolog.io.*;
import  java.io.*;
import  gnu.prolog.term.*;
import  java.util.Stack;

public class PrologTextLoader
{
  /** root file */
  protected String rootFile;
  /** current file */
  protected String currentFile;
  /** current term reader */
  protected TermReader  currentReader;
  /** stack of previous readers */
  protected Stack       readerStack = new Stack();
  /** stack of previous files */
  protected Stack       fileStack = new Stack();
  /** operator set */
  protected OperatorSet operatorSet =  new OperatorSet();
  /** prolog text loader state */
  protected PrologTextLoaderState prologTextLoaderState;

  AtomTerm  opFX  = AtomTerm.get( "fx");
  AtomTerm  opFY  = AtomTerm.get( "fy");
  AtomTerm opXFX  = AtomTerm.get("xfx");
  AtomTerm opXFY  = AtomTerm.get("xfy");
  AtomTerm opYFX  = AtomTerm.get("yfx");
  AtomTerm opXF   = AtomTerm.get("xf" );
  AtomTerm opYF   = AtomTerm.get("yf" );

  // tags used in loader
  CompoundTermTag directiveTag       = CompoundTermTag.directive;
  CompoundTermTag clauseTag          = CompoundTermTag.clause;
  CompoundTermTag includeTag         = CompoundTermTag.get("include",1);
  CompoundTermTag multifileTag       = CompoundTermTag.get("multifile",1);
  CompoundTermTag dynamicTag         = CompoundTermTag.get("dynamic",1);
  CompoundTermTag discontiguousTag   = CompoundTermTag.get("discontiguous",1);
  CompoundTermTag opTag              = CompoundTermTag.get("op",3);
  CompoundTermTag char_conversionTag = CompoundTermTag.get("char_conversion",2);
  CompoundTermTag initializationTag  = CompoundTermTag.get("initialization",1);
  CompoundTermTag ensure_loadedTag   = CompoundTermTag.get("ensure_loaded",1);
  CompoundTermTag set_prolog_flagTag = CompoundTermTag.get("set_prolog_flag",2);

  // my extension directives
  CompoundTermTag externalTag = CompoundTermTag.get("external",2);
  CompoundTermTag build_inTag = CompoundTermTag.get("build_in",2);
  CompoundTermTag controlTag  = CompoundTermTag.get("control",2);


  // include/ensure loaded argument terms
  CompoundTermTag url1Tag = CompoundTermTag.get("url",1);
  CompoundTermTag resource1Tag = CompoundTermTag.get("resource",1);
  CompoundTermTag file1Tag = CompoundTermTag.get("file",1);

  public PrologTextLoader(PrologTextLoaderState prologTextLoaderState, Term root)
  {
    this.prologTextLoaderState = prologTextLoaderState;
    this.rootFile = prologTextLoaderState.getInputName(root);
    currentFile = rootFile;
    try
    {
      currentReader = new TermReader(new InputStreamReader(prologTextLoaderState.getInputStream(root)));
    }
    catch(Exception ex)
    {
      logError("could not open file \'"+currentFile+"\': "+ex.getMessage());
      return;
    }
    processFile();
  }
  
  public PrologTextLoader(PrologTextLoaderState prologTextLoaderState, String term)
  {
    this.prologTextLoaderState = prologTextLoaderState;
    StringReader sReader = new StringReader(term);
    currentReader = new TermReader(sReader);
    processFile();
  }

  public String getCurrentFile()
  {
    return currentFile;
  }

  public int getCurrentLine()
  {
    if (currentReader == null)
      return 0;
    return currentReader.getCurrentLine();
  }

  public int getCurrentColumn()
  {
    if (currentReader == null)
      return 0;
    return currentReader.getCurrentColumn();
  }

  protected void processFile()
  {
    while (currentReader != null)
    {
      Term term;
      try
      {
        term = currentReader.readTerm(operatorSet);
      }
      catch(ParseException ex)
      {
        //ex.printStackTrace();
        logError(ex);
        continue;
      }
      if (term == null) // if eof
      {
        processEof();
      }
      else if (term instanceof AtomTerm)
      {
        processClause(term);
      }
      else if (term instanceof CompoundTerm)
      {
        CompoundTerm cterm = (CompoundTerm)term;
        if (cterm.tag != directiveTag)
        {
          processClause(term);
        }
        else
        {
          if(!(cterm.args[0] instanceof CompoundTerm))
          {
            logError("invalid directive term");
            continue;
          }
          CompoundTerm dirTerm = (CompoundTerm)cterm.args[0];
          CompoundTermTag dirTag = dirTerm.tag;
          if (dirTag == includeTag)
          {
            processIncludeDirective(dirTerm.args[0]);
          }
          else if (dirTag == multifileTag       )
          {
            processMultifileDirective(dirTerm.args[0]);
          }
          else if (dirTag == dynamicTag         )
          {
            processDynamicDirective(dirTerm.args[0]);
          }
          else if (dirTag == discontiguousTag   )
          {
            processDiscontiguousDirective(dirTerm.args[0]);
          }
          else if (dirTag == opTag              )
          {
            processOpDirective(dirTerm.args[0],dirTerm.args[1],dirTerm.args[2]);
          }
          else if (dirTag == char_conversionTag )
          {
            processCharConversionDirective(dirTerm.args[0],dirTerm.args[1]);
          }
          else if (dirTag == initializationTag  )
          {
            processInitializationDirective(dirTerm.args[0]);
          }
          else if (dirTag == ensure_loadedTag   )
          {
            processEnsureLoadedDirective(dirTerm.args[0]);
          }
          else if (dirTag == set_prolog_flagTag )
          {
            processSetPrologFlagDirective(dirTerm.args[0],dirTerm.args[1]);
          }
          else if (dirTag == externalTag )
          {
            processExternalDirective(dirTerm.args[0],dirTerm.args[1]);
          }
          else if (dirTag == controlTag )
          {
            processControlDirective(dirTerm.args[0],dirTerm.args[1]);
          }
          else if (dirTag == build_inTag )
          {
            processBuildInDirective(dirTerm.args[0],dirTerm.args[1]);
          }
          else
          {
            logError("invalid directive");
          }
        }
      }
      else
      {
        logError("term is not a clause or directive");
      }
    }
  }

  protected void processSetPrologFlagDirective(Term arg0, Term arg1)
  {
    //logError("set_prolog_flag/2 directive was ignored");
    prologTextLoaderState.addInitialization(this, new CompoundTerm(set_prolog_flagTag,arg0, arg1));
  }


  protected void processBuildInDirective(Term pi,Term className)
  {
    if (! (className instanceof AtomTerm))
    {
      logError("class name should be atom term");
      return;
    }
    prologTextLoaderState.defineExternal(this, pi, ((AtomTerm)className).value, Predicate.BUILD_IN);
  }
  
  protected void processControlDirective(Term pi,Term className)
  {
    if (! (className instanceof AtomTerm))
    {
      logError("class name should be atom term");
      return;
    }
    prologTextLoaderState.defineExternal(this, pi, ((AtomTerm)className).value, Predicate.CONTROL);
  }

  protected void processExternalDirective(Term pi,Term className)
  {
    if (! (className instanceof AtomTerm))
    {
      logError("class name should be atom term");
      return;
    }
    prologTextLoaderState.defineExternal(this, pi, ((AtomTerm)className).value, Predicate.EXTERNAL);
  }

  protected void processInitializationDirective(Term term1)
  {
    prologTextLoaderState.addInitialization(this, term1);
  }

  protected void processCharConversionDirective(Term from,Term to)
  {
    logError("char_conversion/2 directive was ignored");
  }

  protected void processOpDirective(Term priority,Term specifier,Term operatorAtom)
  {
    if (! (specifier instanceof AtomTerm))
    {
      logError("the specifier should be an atom term");
      return;
    }
    if (! (priority instanceof IntegerTerm))
    {
      logError("the priority should be an integer term");
      return;
    }

    if (! (operatorAtom instanceof AtomTerm))
    {
      logError("the functor should be an atom term");
      return;
    }

    int spec = -1;
         if (specifier ==  opFX) spec = Operator. FX;
    else if (specifier ==  opFY) spec = Operator. FY;
    else if (specifier == opXFX) spec = Operator.XFX;
    else if (specifier == opXFY) spec = Operator.XFY;
    else if (specifier == opYFX) spec = Operator.YFX;
    else if (specifier == opXF ) spec = Operator.XF ;
    else if (specifier == opYF ) spec = Operator.YF ;
    else
    {
      logError("invalid operator specifier");
    }
    operatorSet.add(((IntegerTerm)priority).value, spec, ((AtomTerm)operatorAtom).value);
    prologTextLoaderState.addInitialization(this, new CompoundTerm(opTag,new Term[]{priority, specifier, operatorAtom}));
  }

  protected void processDiscontiguousDirective(Term pi)
  {
    if (!CompoundTermTag.isPredicateIndicator(pi))
    {
      logError("the predicate indicator is not valid.");
      return;
    }
    CompoundTermTag tag = CompoundTermTag.get(pi);
    prologTextLoaderState.declareDiscontiguous(this, tag);
  }

  protected void processMultifileDirective(Term pi)
  {
    if (!CompoundTermTag.isPredicateIndicator(pi))
    {
      logError("the predicate indicator is not valid.");
      return;
    }
    CompoundTermTag tag = CompoundTermTag.get(pi);
    prologTextLoaderState.declareMultifile(this, tag);
  }

  protected void processDynamicDirective(Term pi)
  {
    if (!CompoundTermTag.isPredicateIndicator(pi))
    {
      logError("the predicate indicator is not valid.");
      return;
    }
    CompoundTermTag tag = CompoundTermTag.get(pi);
    prologTextLoaderState.declareDynamic(this, tag);
  }

  protected void processClause(Term argument)
  {
    prologTextLoaderState.addClause(this, argument);
  }

  protected void processIncludeDirective(Term argument)
  {
    try
    {
      TermReader reader = new TermReader(new InputStreamReader(prologTextLoaderState.getInputStream(argument)));
      readerStack.push(currentReader);
      fileStack.push(currentFile);
      currentReader = reader;
      currentFile = prologTextLoaderState.getInputName(argument);
    }
    catch(Exception ex)
    {
      logError("could not open datasource \'"+prologTextLoaderState.getInputName(argument)+"\': "+ex.getMessage());
      return;
    }
  }


  protected void processEnsureLoadedDirective(Term argument)
  {
    prologTextLoaderState.ensureLoaded(argument);
  }

  protected void processEof()
  {
    if (!fileStack.isEmpty())
    {
      currentFile   = null;
      try
      {
        currentReader.close();
      }
      catch(IOException ex)
      {
        logError("error during closing file: "+ex.getMessage());
      }
      currentFile   = (String)fileStack.pop();
      currentReader = (TermReader)readerStack.pop();
    }
    else
    {
      currentFile   = null;
      currentReader = null;
    }
  }

  public void logError(String message)
  {
    prologTextLoaderState.logError(this, message);
  }

  public void logError(ParseException ex)
  {
    prologTextLoaderState.logError(this, ex);
  }
}


