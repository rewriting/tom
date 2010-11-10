/*
* 
* TOM - To One Matching Compiler
* 
* Copyright (c) 2000-2010, INPL, INRIA
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

package tom.engine.backend;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.*;

import tom.engine.TomMessage;
import tom.engine.TomBase;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.tools.*;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionParser;
import tom.platform.PluginPlatformMessage;
import tom.platform.PlatformException;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;
import tom.library.sl.VisitFailure;


/**
* The TomBackend "plugin".
* Has to create the generator depending on OptionManager, create the output 
* writer and generting the output code.
*/
public class BackendPlugin extends TomGenericPlugin {



  private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.Sequence) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.Choice) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.SequenceId) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.SequenceId(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin):new tom.library.sl.SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)) );
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( (l1 instanceof tom.library.sl.ChoiceId) )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {
        return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) );
      } else {
        return ( (tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) );
      }
    } else {
      return ( (l2==null)?l1:new tom.library.sl.ChoiceId(l1,l2) );
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return ( (( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin):new tom.library.sl.ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)) );
  }
  private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.Identity() )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ),( null )) )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )==null)?s2:new tom.library.sl.Choice(s2,( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?s1:new tom.library.sl.Sequence(s1,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ),( null )) )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { 
return ( 
( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?s:new tom.library.sl.Choice(s,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Choice(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.Sequence(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Choice(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ))

;
}


/** the tabulation starting value */
private final static int defaultDeep = 2;

/** the declared options string */
public static final String DECLARED_OPTIONS = 
"<options>" +
"<boolean name='noOutput' altName=''  description='Do not generate code' value='false'/>" +
"<boolean name='jCode'    altName='j' description='Generate Java code' value='true'/>" + 
"<boolean name='csCode'   altName=''  description='Generate C# code' value='false'/>" + 
"<boolean name='cCode'    altName='c' description='Generate C code' value='false'/>" +
"<boolean name='camlCode' altName=''  description='Generate Caml code' value='false'/>" + 
"<boolean name='pCode'    altName=''  description='Generate Python code' value='false'/>" + 
"<boolean name='inline'   altName=''  description='Inline mapping' value='false'/>" +
"<boolean name='inlineplus'   altName=''  description='Inline mapping' value='false'/>" +
"</options>";

/** the generated file name */
private String generatedFileName = null;

/** Constructor*/
public BackendPlugin() {
super("BackendPlugin");
}

/**
*
*/
public void run(Map informationTracker) {
//System.out.println("(debug) I'm in the Tom backend : TSM"+getStreamManager().toString());
try {
if(isActivated() == true) {
AbstractGenerator generator = null;
Writer writer;
long startChrono = System.currentTimeMillis();
try {
String encoding = getOptionStringValue("encoding");
writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getStreamManager().getOutputFile()),encoding));
OutputCode output = new OutputCode(writer, getOptionManager());
if(getOptionBooleanValue("noOutput")) {
throw new TomRuntimeException("Backend activated, but noOutput is set");
} else if(getOptionBooleanValue("cCode")) {
generator = new CGenerator(output, getOptionManager(), getSymbolTable());
} else if(getOptionBooleanValue("camlCode")) {
generator = new CamlGenerator(output, getOptionManager(), getSymbolTable());
} else if(getOptionBooleanValue("pCode")) {
generator = new PythonGenerator(output, getOptionManager(), getSymbolTable());
} else if(getOptionBooleanValue("csCode")) {
generator = new CSharpGenerator(output, getOptionManager(), getSymbolTable());
} else if(getOptionBooleanValue("jCode")) {
generator = new JavaGenerator(output, getOptionManager(), getSymbolTable());
} else {
throw new TomRuntimeException("no selected language for the Backend");
}

Code pilCode = (Code) getWorkingTerm();

markUsedConstructorDestructor(pilCode);

generator.generate(defaultDeep, pilCode,TomBase.DEFAULT_MODULE_NAME);
// verbose
TomMessage.info(getLogger(),null,0,TomMessage.tomGenerationPhase,
Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
output.close();
} catch (IOException e) {
TomMessage.error(getLogger(),
getStreamManager().getInputFileName(), 0,
TomMessage.backendIOException, e.getMessage());
return;
} catch (Exception e) {
String fileName = getStreamManager().getInputFileName();
//int line = -1;
// set line number to -1 instead of 0 in order to keep the old
// behavior. It will probably be modified soon.
TomMessage.error(getLogger(),fileName,-1,TomMessage.exceptionMessage, fileName);
e.printStackTrace();
return;
}
// set the generated File Name
try {
generatedFileName = getStreamManager().getOutputFile().getCanonicalPath();
} catch (IOException e) {
System.out.println("IO Exception when computing generatedFileName");
e.printStackTrace();
}
} else {
// backend is desactivated
TomMessage.info(getLogger(),getStreamManager().getInputFileName(),0,TomMessage.backendInactivated);
}
} catch(PlatformException e) {
TomMessage.error(getLogger(),null,0,PluginPlatformMessage.platformStopped);
return;
}
}

public void optionChanged(String optionName, Object optionValue) {
//System.out.println("optionChanged: " + optionName + " --> " + optionValue);
if(optionName.equals("camlCode") && ((Boolean)optionValue).booleanValue() ) { 
setOptionValue("jCode", Boolean.FALSE);        
setOptionValue("cCode", Boolean.FALSE);        
setOptionValue("pCode", Boolean.FALSE);        
} else if(optionName.equals("cCode") && ((Boolean)optionValue).booleanValue() ) { 
setOptionValue("jCode", Boolean.FALSE);        
setOptionValue("camlCode", Boolean.FALSE);        
setOptionValue("pCode", Boolean.FALSE);        
} else if(optionName.equals("jCode") && ((Boolean)optionValue).booleanValue() ) { 
setOptionValue("cCode", Boolean.FALSE);        
setOptionValue("camlCode", Boolean.FALSE);        
setOptionValue("pCode", Boolean.FALSE);        
} else if(optionName.equals("pCode") && ((Boolean)optionValue).booleanValue() ) { 
setOptionValue("cCode", Boolean.FALSE);        
setOptionValue("camlCode", Boolean.FALSE);        
setOptionValue("jCode", Boolean.FALSE);        
}
}

/**
* inherited from OptionOwner interface (plugin) 
*/
public PlatformOptionList getDeclaredOptionList() {
return OptionParser.xmlToOptionList(BackendPlugin.DECLARED_OPTIONS);
}

private boolean isActivated() {
return !getOptionBooleanValue("noOutput");
}

protected SymbolTable getSymbolTable(String moduleName) {
//TODO//
//Using of the moduleName
////////

//System.out.println(getSymbolTable().toTerm());

return getSymbolTable();
}
/**
* inherited from plugin interface
* returns the generated file name
*/
public Object[] getArgs() {
return new Object[]{generatedFileName};
}




private void markUsedConstructorDestructor(Code pilCode) {
Stack<String> stack = new Stack<String>();
stack.push(TomBase.DEFAULT_MODULE_NAME);
try {

( new tom.library.sl.Mu(( new tom.library.sl.MuVar("markStrategy") ),tom_make_TopDownCollect(tom_make_Collector(( new tom.library.sl.MuVar("markStrategy") ),this,stack))) ).visitLight(pilCode);
} catch(VisitFailure e) { /* Ignored */ }
}

private void setUsedSymbolConstructor(String moduleName, TomSymbol tomSymbol, Strategy markStrategy) {
SymbolTable st = getSymbolTable(moduleName);
if(!st.isUsedSymbolConstructor(tomSymbol) && !st.isUsedSymbolDestructor(tomSymbol)) {
try {
markStrategy.visitLight(tomSymbol);
} catch(VisitFailure e) { /* Ignored */ }
}
getSymbolTable(moduleName).setUsedSymbolConstructor(tomSymbol);
}

private void setUsedSymbolDestructor(String moduleName, TomSymbol tomSymbol, Strategy markStrategy) {    
SymbolTable st = getSymbolTable(moduleName);
if(!st.isUsedSymbolConstructor(tomSymbol) && !st.isUsedSymbolDestructor(tomSymbol)) {
try {
markStrategy.visitLight(tomSymbol);
} catch(VisitFailure e) { /* Ignored */ }
}
getSymbolTable(moduleName).setUsedSymbolDestructor(tomSymbol);
}

private void setUsedType(String moduleName, String tomTypeName, Strategy markStrategy) {
getSymbolTable(moduleName).setUsedType(tomTypeName);
}

/*
* the strategy Collector is used collect the part of the mapping that is really used
* this strategy also collect the declarations (IsFsymDecl, GetSlotDecl, etc)
* to fill the mapInliner used by the backend to inline calls to IsFsym, GetSlot, etc.
*/

public static class Collector extends tom.library.sl.AbstractStrategyBasic {
private  tom.library.sl.Strategy  markStrategy;
private  BackendPlugin  bp;
private  Stack<String>  stack;
public Collector( tom.library.sl.Strategy  markStrategy,  BackendPlugin  bp,  Stack<String>  stack) {
super(( new tom.library.sl.Identity() ));
this.markStrategy=markStrategy;
this.bp=bp;
this.stack=stack;
}
public  tom.library.sl.Strategy  getmarkStrategy() {
return markStrategy;
}
public  BackendPlugin  getbp() {
return bp;
}
public  Stack<String>  getstack() {
return stack;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
stratChilds[1] = getmarkStrategy();
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
markStrategy = ( tom.library.sl.Strategy ) children[1];
return this;
}
public int getChildCount() {
return 2;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
case 1: return getmarkStrategy();
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
case 1: markStrategy = ( tom.library.sl.Strategy )child; return this;
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {


String moduleName = TomBase.getModuleName(
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOptions() );
/*
* push the modulename
* or the wrapping modulename if the current one
* (nested match for example) does not have one
*/
if (moduleName==null) {
try {
moduleName = stack.peek();
stack.push(moduleName);
/*System.out.println("push2: " + moduleName);*/
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}
} else {
stack.push(moduleName);
/*System.out.println("push1: " + moduleName);*/
}
/*System.out.println("match -> moduleName = " + moduleName);*/
markStrategy.visitLight(
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAutomataInst() );
/*String pop = (String) stack.pop();
System.out.println("pop: " + pop);*/
throw new tom.library.sl.VisitFailure();



}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {

markStrategy.visitLight(
 (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() );
throw new tom.library.sl.VisitFailure();


}
}

}


}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
boolean tomMatch75_5= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch75_1= null ;
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {
{
tomMatch75_5= true ;
tomMatch75_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;

}
} else {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) {
{
tomMatch75_5= true ;
tomMatch75_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;

}
} else {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {
{
tomMatch75_5= true ;
tomMatch75_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;

}
} else {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {
{
tomMatch75_5= true ;
tomMatch75_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;

}
} else {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {
{
tomMatch75_5= true ;
tomMatch75_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;

}
} else {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {
{
tomMatch75_5= true ;
tomMatch75_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getOpname() ;

}
}
}
}
}
}
}
if (tomMatch75_5) {
if ( (tomMatch75_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

try {
/*System.out.println("list check: " + `name);*/
String moduleName = stack.peek();
/*System.out.println("moduleName: " + moduleName);*/
TomSymbol tomSymbol = TomBase.getSymbolFromName(
 tomMatch75_1.getString() ,bp.getSymbolTable(moduleName)); 
bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}

}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch75_7= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getAstName() ;
if ( (tomMatch75_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

try {
/*System.out.println("list check: " + `name);*/
String moduleName = stack.peek();
/*System.out.println("moduleName: " + moduleName);*/
TomSymbol tomSymbol = TomBase.getSymbolFromName(
 tomMatch75_7.getString() ,bp.getSymbolTable(moduleName)); 
bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}

}


}
return _visit_Expression(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

try {
String moduleName = stack.peek();
bp.setUsedType(moduleName,
 (( tom.engine.adt.tomtype.types.TomType )tom__arg).getTomType() ,markStrategy);
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}

}

}
return _visit_TomType(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch77_3= false ;
 tom.engine.adt.tomname.types.TomNameList  tomMatch77_1= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {
{
tomMatch77_3= true ;
tomMatch77_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
{
tomMatch77_3= true ;
tomMatch77_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ;

}
}
}
if (tomMatch77_3) {

TomNameList l = 
tomMatch77_1;
/*System.out.println("dest " + `l);*/
while(!l.isEmptyconcTomName()) {
try {
/*System.out.println("op: " + l.getHead());*/
String moduleName = stack.peek();
/*System.out.println("moduleName: " + moduleName);*/
TomSymbol tomSymbol = TomBase.getSymbolFromName(l.getHeadconcTomName().getString(),bp.getSymbolTable(moduleName)); 
/*System.out.println("mark: " + tomSymbol);*/
/*
* if it comes from java
*/
if (tomSymbol != null) { bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);}
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}
l = l.getTailconcTomName();
}
/*
* here we can fail because the subterms appear in isFsym tests
* therefore, they are marked when traversing the compiledAutomata
*/
throw new tom.library.sl.VisitFailure();


}

}

}

}
return _visit_TomTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch78_5= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch78_1= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
{
tomMatch78_5= true ;
tomMatch78_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
{
tomMatch78_5= true ;
tomMatch78_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
if (tomMatch78_5) {
if ( (tomMatch78_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

try {
/*System.out.println("build: " + `name);*/
String moduleName = stack.peek();
/*System.out.println("moduleName: " + moduleName);*/
TomSymbol tomSymbol = TomBase.getSymbolFromName(
 tomMatch78_1.getString() ,bp.getSymbolTable(moduleName)); 
bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}

}

}
{
if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch78_11= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch78_7= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {
{
tomMatch78_11= true ;
tomMatch78_7= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
{
tomMatch78_11= true ;
tomMatch78_7= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
{
tomMatch78_11= true ;
tomMatch78_7= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {
{
tomMatch78_11= true ;
tomMatch78_7= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {
{
tomMatch78_11= true ;
tomMatch78_7= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;

}
}
}
}
}
}
if (tomMatch78_11) {
if ( (tomMatch78_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

try {
/*System.out.println("build: " + `name);*/
String moduleName = stack.peek();
/*System.out.println("moduleName: " + moduleName);*/
TomSymbol tomSymbol = TomBase.getSymbolFromName(
 tomMatch78_7.getString() ,bp.getSymbolTable(moduleName)); 
bp.setUsedSymbolConstructor(moduleName,tomSymbol,markStrategy);
/* XXX: Also mark the destructors as used, since some generated
* functions will use them */
bp.setUsedSymbolDestructor(moduleName,tomSymbol,markStrategy);
/*
* resolve uses in the symbol declaration
*/
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}

}

}


}
return _visit_BQTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomdeclaration.types.Declaration  visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_1= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_2= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_2 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putIsFsym(
 tomMatch79_1.getString() ,
 tomMatch79_2.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch79_9= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTermArg() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_10= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_9 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch79_12= tomMatch79_9.getAstType() ;
if ( (tomMatch79_12 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (tomMatch79_10 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putIsSort(
 tomMatch79_12.getTomType() ,
 tomMatch79_10.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch79_20= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getTermArg1() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_21= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_20 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch79_23= tomMatch79_20.getAstType() ;
if ( (tomMatch79_23 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (tomMatch79_21 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putEqualTerm(
 tomMatch79_23.getTomType() ,
 tomMatch79_21.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_31= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tomname.types.TomName  tomMatch79_32= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getSlotName() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_33= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_31 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_32 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_33 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putGetSlot(
 tomMatch79_31.getString() ,
 tomMatch79_32.getString() ,
 tomMatch79_33.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_42= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_43= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_42 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_43 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putGetHead(
 tomMatch79_42.getString() ,
 tomMatch79_43.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_50= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_51= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_50 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_51 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putGetTail(
 tomMatch79_50.getString() ,
 tomMatch79_51.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_58= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tominstruction.types.Instruction  tomMatch79_59= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;
if ( (tomMatch79_58 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_59 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_63= tomMatch79_59.getExpr() ;
if ( (tomMatch79_63 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putMake(
 tomMatch79_58.getString() ,
 tomMatch79_63.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_68= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_69= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_68 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_69 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putIsEmptyList(
 tomMatch79_68.getString() ,
 tomMatch79_69.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_76= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tominstruction.types.Instruction  tomMatch79_77= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;
if ( (tomMatch79_76 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_77 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_81= tomMatch79_77.getExpr() ;
if ( (tomMatch79_81 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putMakeEmptyList(
 tomMatch79_76.getString() ,
 tomMatch79_81.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_86= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tominstruction.types.Instruction  tomMatch79_87= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;
if ( (tomMatch79_86 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_87 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_91= tomMatch79_87.getExpr() ;
if ( (tomMatch79_91 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putMakeAddList(
 tomMatch79_86.getString() ,
 tomMatch79_91.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_96= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tominstruction.types.Instruction  tomMatch79_97= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;
if ( (tomMatch79_96 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_97 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_101= tomMatch79_97.getExpr() ;
if ( (tomMatch79_101 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putMakeEmptyArray(
 tomMatch79_96.getString() ,
 tomMatch79_101.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_106= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;
 tom.engine.adt.tominstruction.types.Instruction  tomMatch79_107= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getInstr() ;
if ( (tomMatch79_106 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_107 instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_111= tomMatch79_107.getExpr() ;
if ( (tomMatch79_111 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putMakeAddArray(
 tomMatch79_106.getString() ,
 tomMatch79_111.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_116= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_117= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_116 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_117 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putGetElementArray(
 tomMatch79_116.getString() ,
 tomMatch79_117.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch79_124= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getOpname() ;
 tom.engine.adt.tomexpression.types.Expression  tomMatch79_125= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getExpr() ;
if ( (tomMatch79_124 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch79_125 instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

try {
String moduleName = stack.peek();
bp.getSymbolTable(moduleName).putGetSizeArray(
 tomMatch79_124.getString() ,
 tomMatch79_125.getCode() );
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}
}
}

}
{
if ( (tom__arg instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
boolean tomMatch79_136= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch79_132= null ;
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) {
{
tomMatch79_136= true ;
tomMatch79_132= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {
{
tomMatch79_136= true ;
tomMatch79_132= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) {
{
tomMatch79_136= true ;
tomMatch79_132= (( tom.engine.adt.tomdeclaration.types.Declaration )tom__arg).getAstName() ;

}
}
}
}
if (tomMatch79_136) {
if ( (tomMatch79_132 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

try {
String moduleName = stack.peek();
TomSymbol tomSymbol = TomBase.getSymbolFromName(
 tomMatch79_132.getString() ,bp.getSymbolTable(moduleName));
markStrategy.visitLight(tomSymbol);
} catch (EmptyStackException e) {
System.out.println("No moduleName in stack");
}


}
}

}

}


}
return _visit_Declaration(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomdeclaration.types.Declaration  _visit_Declaration( tom.engine.adt.tomdeclaration.types.Declaration  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomdeclaration.types.Declaration )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
return ((T)visit_Declaration((( tom.engine.adt.tomdeclaration.types.Declaration )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {
return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {
return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
}
private static  tom.library.sl.Strategy  tom_make_Collector( tom.library.sl.Strategy  t0,  BackendPlugin  t1,  Stack<String>  t2) { 
return new Collector(t0,t1,t2);
}

}