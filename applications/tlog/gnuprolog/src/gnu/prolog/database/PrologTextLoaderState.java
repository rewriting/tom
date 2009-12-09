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
import java.util.*;
import java.io.*;
import java.net.URL;
import gnu.prolog.term.*;
import gnu.prolog.io.*;

public class PrologTextLoaderState
{
  Module module = new Module();
  HashMap predicate2options2loaders = new HashMap();
  Predicate currentPredicate = null;
  ArrayList errorList = new ArrayList();
  HashSet   loadedFiles = new HashSet();


  // arguments of ensure_loaded/1 and include/2 derectived
  final static CompoundTermTag resourceTag = CompoundTermTag.get("resource",1);
  final static CompoundTermTag urlTag      = CompoundTermTag.get("url",1);
  final static CompoundTermTag fileTag     = CompoundTermTag.get("file",1);

  public List getErrors()
  {
    return errorList;
  }

  public Module getModule()
  {
    return module;
  }

  private boolean testOption(PrologTextLoader loader, Predicate p, String option)
  {
    HashMap options2loaders = (HashMap)predicate2options2loaders.get(p);
    if (options2loaders == null)
    {
      return false;
    }

    Set loaders = (Set)options2loaders.get(option);
    if (loaders == null)
    {
      return false;
    }
    if(loader != null && !loaders.contains(loader))
    {
      return false;
    }
    return true;
  }

  private void defineOption(PrologTextLoader loader, Predicate p, String option)
  {
    HashMap options2loaders = (HashMap)predicate2options2loaders.get(p);
    if (options2loaders == null)
    {
      options2loaders = new HashMap();
      predicate2options2loaders.put(p,options2loaders);
    }
    Set loaders = (Set)options2loaders.get(option);
    if (loaders == null)
    {
      loaders = new HashSet();
      options2loaders.put(option,loaders);
    }
    if( !loaders.contains(loader))
    {
      loaders.add(loader);
    }
  }

  private void defineOptionAndDeclare(PrologTextLoader loader, Predicate p, String option)
  {
    defineOption(loader, p, option);
    defineOption(loader, p, "declared");
  }

  private boolean isDeclaredInOtherLoaders(PrologTextLoader loader, Predicate p)
  {
    HashMap options2loaders = (HashMap)predicate2options2loaders.get(p);
    if (options2loaders == null)
    {
      return false;
    }

    Set loaders = (Set)options2loaders.get("declared");
    if (loaders == null || loaders.isEmpty())
    {
      return false;
    }

    Iterator i = loaders.iterator();
    while (i.hasNext())
    {
      if (loader != i.next())
      {
        return true;
      }
    }
    return false;
  }

  public boolean declareDynamic(PrologTextLoader loader, CompoundTermTag tag)
  {
    Predicate p = findOrCreatePredicate(tag);
    if (testOption(loader, p, "dynamic"))
    {
      return true;
    }
    if (isDeclaredInOtherLoaders(loader, p))
    {
      if (!testOption(loader, p, "multifile"))
      {
        logError(loader, "non multifile predicate could not be changed in other prolog text.");
        return false;
      }
      if (!testOption(null, p, "dynamic"))
      {
        logError(loader, "predicate was not declared dynamic in other texts, dynamic option should be the same in each prolog text.");
        return false;
      }
    }
    else
    {
      if (testOption(loader, p, "defined"))
      {
        logError(loader, "predicate was already defined and could not be declared dynamic.");
        return false;
      }
    }
    if (p.getType() == Predicate.UNDEFINED)
    {
      p.setType(Predicate.USER_DEFINED);
    }
    p.setDynamic();
    defineOptionAndDeclare(loader, p, "dynamic");
    return true;
  }


  public void declareMultifile(PrologTextLoader loader, CompoundTermTag tag)
  {
    Predicate p = findOrCreatePredicate(tag);
    if (testOption(loader, p, "multifile"))
    {
      return;
    }
    if (isDeclaredInOtherLoaders(loader, p))
    {
      if (!testOption(null, p, "multifile"))
      {
        logError(loader, "non multifile predicate could not be changed in other prolog text.");
        return;
      }
    }
    else
    {
      if (testOption(loader, p, "defined"))
      {
        logError(loader, "predicate was already defined and could not be declared multifile.");
        return;
      }
    }
    if (p.getType() == Predicate.UNDEFINED)
    {
      p.setType(Predicate.USER_DEFINED);
    }
    defineOptionAndDeclare(loader, p, "multifile");
  }

  public void declareDiscontiguous(PrologTextLoader loader, CompoundTermTag tag)
  {
    Predicate p = findOrCreatePredicate(tag);
    if (testOption(loader, p, "discontiguous"))
    {
      return;
    }
    if (isDeclaredInOtherLoaders(loader, p))
    {
      if (!testOption(null, p, "multifile"))
      {
        logError(loader, "non multifile predicate could not be changed in other prolog text.");
        return;
      }
    }
    if (testOption(loader, p, "defined"))
    {
      logError(loader, "predicate was already defined and could not be declared discontiguous.");
      return;
    }
    if (p.getType() == Predicate.UNDEFINED)
    {
      p.setType(Predicate.USER_DEFINED);
    }
    defineOptionAndDeclare(loader, p, "discontiguous");
  }

  public void addClause(PrologTextLoader loader, Term term)
  {
    Term head = term;
    CompoundTermTag headTag;
    if (term instanceof CompoundTerm &&
        ((CompoundTerm)term).tag == CompoundTermTag.clause)
    {
      head = ((CompoundTerm)term).args[0];
    }
    if (head instanceof AtomTerm)
    {
      headTag = CompoundTermTag.get((AtomTerm)head, 0);
    }
    else if (head instanceof CompoundTerm)
    {
      headTag = ((CompoundTerm)head).tag;
    }
    else
    {
      logError(loader, "predicate head is not a callable term.");
      return;
    }

    if (currentPredicate == null || headTag != currentPredicate.getTag() )
    {
      currentPredicate = null;
      Predicate p = findOrCreatePredicate(headTag);
      if (testOption(loader, p, "defined") &&
          !testOption(loader, p, "discontiguous"))
      {
        logError(loader, "predicate is not discontiguous.");
        return;
      }
      if (!testOption(loader, p, "declared") &&
          testOption(null, p, "declared") &&
          !testOption(loader, p, "multifile"))
      {
        logError(loader, "predicate is not multifile.");
        return;
      }
      if (!testOption(loader, p, "dynamic") &&
          testOption(null, p, "dynamic"))
      {
        logError(loader, "predicate is not declared dynamic in this prolog text.");
        return;
      }
      currentPredicate = p;
      if (!testOption(loader, p, "defined"))
      {
        if (p.getType() == Predicate.UNDEFINED)
        {
          p.setType(Predicate.USER_DEFINED);
        }
        defineOptionAndDeclare(loader, p, "defined");
      }
    }
    try
    {
      currentPredicate.addClauseLast(Predicate.prepareClause(term));
    }
    catch(IllegalArgumentException ex)
    {
      logError(loader, ex.getMessage());
    }

  }

  public void defineExternal(PrologTextLoader loader, Term pi, String javaClassName, int type)
  {
    if (!CompoundTermTag.isPredicateIndicator(pi))
    {
      logError(loader, "predicate indicator is not valid.");
      return;
    }
    CompoundTermTag tag = CompoundTermTag.get(pi);
    Predicate p = findOrCreatePredicate(tag);
    if (p.getType() != Predicate.UNDEFINED)
    {
      logError(loader, "predicate type could not be changed.");
      return;
    }
    p.setType(type);
    p.setJavaClassName(javaClassName);
    defineOptionAndDeclare(loader, p, "defined");
  }

  Predicate findOrCreatePredicate(CompoundTermTag tag)
  {
    Predicate p = module.getDefinedPredicate(tag);
    if (p == null)
    {
      p = module.createDefinedPredicate(tag);
    }
    return p;
  }

  public void logError(PrologTextLoader loader, ParseException ex)
  {
    errorList.add(new PrologTextLoaderError(loader,ex));
  }

  public void logError(PrologTextLoader loader, String message)
  {
    errorList.add(new PrologTextLoaderError(loader, message));
  }

  public List getErrorList()
  {
    return errorList;
  }

  public void addInitialization(PrologTextLoader loader, Term term)
  {
    module.addInitialization(term);
  }

  public void ensureLoaded(Term term)
  {
    if (!loadedFiles.contains(getInputName(term)))
    {
      loadedFiles.add(getInputName(term));
      new PrologTextLoader(this, term);
    }
  }
  
  public void ensureLoaded(String term)
  {
      new PrologTextLoader(this, term);
  }

  String getInputName(Term term)
  {
    if (term instanceof AtomTerm) // argument is an atom, which is an filename
    {
      return ((AtomTerm)term).value;
    }
    else if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      if (ct.tag == fileTag)
      {
        if ((ct.args[0] instanceof AtomTerm))
        {
          return getInputName(ct.args[0]);
        }
      }
      else if (ct.tag == urlTag || ct.tag == resourceTag )
      {
        if (ct.args[0] instanceof AtomTerm)
        {
          AtomTerm arg = (AtomTerm)ct.args[0];
          if (ct.tag == urlTag)
          {
            return "url:"+arg.value;
          }
          else //resource tag
          {
            return "resource:"+arg.value;
          }
        }
      }
    }
    return "bad_input("+TermWriter.toString(term)+")";
  }

  InputStream getInputStream(Term term) throws IOException
  {
    if (term instanceof AtomTerm) // argument is an atom, which is an filename
    {
      return new FileInputStream(((AtomTerm)term).value);
    }
    else if (term instanceof CompoundTerm)
    {
      CompoundTerm ct = (CompoundTerm)term;
      if (ct.tag == fileTag)
      {
        if (!(ct.args[0] instanceof AtomTerm))
        {
          throw new IOException("unknown type of datasource");
        }
        return getInputStream(ct.args[0]);
      }
      else if (ct.tag == urlTag || ct.tag == resourceTag )
      {
        URL url;
        if (!(ct.args[0] instanceof AtomTerm))
        {
          throw new IOException("unknown type of datasource");
        }
        AtomTerm arg = (AtomTerm)ct.args[0];
        if (ct.tag == urlTag)
        {
          url = new URL(arg.value);
        }
        else //resource tag
        {
          url = getClass().getResource(arg.value);
          if (url == null)
          {
            throw new IOException("resource not found");
          }
        }
        return url.openStream();
      }
    }
    throw new IOException("unknown type of datasource");
  }

}
