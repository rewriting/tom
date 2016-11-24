/*
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the Inria nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tom.library.bytecode;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;

public class ToolBox {

          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}         private static   tom.library.adt.bytecode.types.StringList  tom_append_list_StringList( tom.library.adt.bytecode.types.StringList l1,  tom.library.adt.bytecode.types.StringList  l2) {     if( l1.isEmptyStringList() ) {       return l2;     } else if( l2.isEmptyStringList() ) {       return l1;     } else if(  l1.getTailStringList() .isEmptyStringList() ) {       return  tom.library.adt.bytecode.types.stringlist.ConsStringList.make( l1.getHeadStringList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.stringlist.ConsStringList.make( l1.getHeadStringList() ,tom_append_list_StringList( l1.getTailStringList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.StringList  tom_get_slice_StringList( tom.library.adt.bytecode.types.StringList  begin,  tom.library.adt.bytecode.types.StringList  end, tom.library.adt.bytecode.types.StringList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyStringList()  ||  (end== tom.library.adt.bytecode.types.stringlist.EmptyStringList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.stringlist.ConsStringList.make( begin.getHeadStringList() ,( tom.library.adt.bytecode.types.StringList )tom_get_slice_StringList( begin.getTailStringList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.AccessList  tom_append_list_AccessList( tom.library.adt.bytecode.types.AccessList l1,  tom.library.adt.bytecode.types.AccessList  l2) {     if( l1.isEmptyAccessList() ) {       return l2;     } else if( l2.isEmptyAccessList() ) {       return l1;     } else if(  l1.getTailAccessList() .isEmptyAccessList() ) {       return  tom.library.adt.bytecode.types.accesslist.ConsAccessList.make( l1.getHeadAccessList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.accesslist.ConsAccessList.make( l1.getHeadAccessList() ,tom_append_list_AccessList( l1.getTailAccessList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.AccessList  tom_get_slice_AccessList( tom.library.adt.bytecode.types.AccessList  begin,  tom.library.adt.bytecode.types.AccessList  end, tom.library.adt.bytecode.types.AccessList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyAccessList()  ||  (end== tom.library.adt.bytecode.types.accesslist.EmptyAccessList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.accesslist.ConsAccessList.make( begin.getHeadAccessList() ,( tom.library.adt.bytecode.types.AccessList )tom_get_slice_AccessList( begin.getTailAccessList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.FieldDescriptorList  tom_append_list_FieldDescriptorList( tom.library.adt.bytecode.types.FieldDescriptorList l1,  tom.library.adt.bytecode.types.FieldDescriptorList  l2) {     if( l1.isEmptyFieldDescriptorList() ) {       return l2;     } else if( l2.isEmptyFieldDescriptorList() ) {       return l1;     } else if(  l1.getTailFieldDescriptorList() .isEmptyFieldDescriptorList() ) {       return  tom.library.adt.bytecode.types.fielddescriptorlist.ConsFieldDescriptorList.make( l1.getHeadFieldDescriptorList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.fielddescriptorlist.ConsFieldDescriptorList.make( l1.getHeadFieldDescriptorList() ,tom_append_list_FieldDescriptorList( l1.getTailFieldDescriptorList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.FieldDescriptorList  tom_get_slice_FieldDescriptorList( tom.library.adt.bytecode.types.FieldDescriptorList  begin,  tom.library.adt.bytecode.types.FieldDescriptorList  end, tom.library.adt.bytecode.types.FieldDescriptorList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyFieldDescriptorList()  ||  (end== tom.library.adt.bytecode.types.fielddescriptorlist.EmptyFieldDescriptorList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.fielddescriptorlist.ConsFieldDescriptorList.make( begin.getHeadFieldDescriptorList() ,( tom.library.adt.bytecode.types.FieldDescriptorList )tom_get_slice_FieldDescriptorList( begin.getTailFieldDescriptorList() ,end,tail)) ;   }      private static   tom.library.adt.bytecode.types.IntList  tom_append_list_IntList( tom.library.adt.bytecode.types.IntList l1,  tom.library.adt.bytecode.types.IntList  l2) {     if( l1.isEmptyIntList() ) {       return l2;     } else if( l2.isEmptyIntList() ) {       return l1;     } else if(  l1.getTailIntList() .isEmptyIntList() ) {       return  tom.library.adt.bytecode.types.intlist.ConsIntList.make( l1.getHeadIntList() ,l2) ;     } else {       return  tom.library.adt.bytecode.types.intlist.ConsIntList.make( l1.getHeadIntList() ,tom_append_list_IntList( l1.getTailIntList() ,l2)) ;     }   }   private static   tom.library.adt.bytecode.types.IntList  tom_get_slice_IntList( tom.library.adt.bytecode.types.IntList  begin,  tom.library.adt.bytecode.types.IntList  end, tom.library.adt.bytecode.types.IntList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyIntList()  ||  (end== tom.library.adt.bytecode.types.intlist.EmptyIntList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.bytecode.types.intlist.ConsIntList.make( begin.getHeadIntList() ,( tom.library.adt.bytecode.types.IntList )tom_get_slice_IntList( begin.getTailIntList() ,end,tail)) ;   }    


  private final static int[] accessFlags = {
    Opcodes.ACC_ABSTRACT,
    Opcodes.ACC_ANNOTATION,
    Opcodes.ACC_BRIDGE,
    Opcodes.ACC_DEPRECATED,
    Opcodes.ACC_ENUM,
    Opcodes.ACC_FINAL,
    Opcodes.ACC_INTERFACE,
    Opcodes.ACC_NATIVE,
    Opcodes.ACC_PRIVATE,
    Opcodes.ACC_PROTECTED,
    Opcodes.ACC_PUBLIC,
    Opcodes.ACC_STATIC,
    Opcodes.ACC_STRICT,
    Opcodes.ACC_SUPER,
    Opcodes.ACC_SYNCHRONIZED,
    Opcodes.ACC_SYNTHETIC,
    Opcodes.ACC_TRANSIENT,
    Opcodes.ACC_VARARGS,
    Opcodes.ACC_VOLATILE
  };

  private final static Access[] accessObj = {
     tom.library.adt.bytecode.types.access.ABSTRACT.make() ,
     tom.library.adt.bytecode.types.access.ANNOTATION.make() ,
     tom.library.adt.bytecode.types.access.BRIDGE.make() ,
     tom.library.adt.bytecode.types.access.DEPRECATED.make() ,
     tom.library.adt.bytecode.types.access.ENUM.make() ,
     tom.library.adt.bytecode.types.access.FINAL.make() ,
     tom.library.adt.bytecode.types.access.INTERFACE.make() ,
     tom.library.adt.bytecode.types.access.NATIVE.make() ,
     tom.library.adt.bytecode.types.access.PRIVATE.make() ,
     tom.library.adt.bytecode.types.access.PROTECTED.make() ,
     tom.library.adt.bytecode.types.access.PUBLIC.make() ,
     tom.library.adt.bytecode.types.access.STATIC.make() ,
     tom.library.adt.bytecode.types.access.STRICT.make() ,
     tom.library.adt.bytecode.types.access.SUPER.make() ,
     tom.library.adt.bytecode.types.access.SYNCHRONIZED.make() ,
     tom.library.adt.bytecode.types.access.SYNTHETIC.make() ,
     tom.library.adt.bytecode.types.access.TRANSIENT.make() ,
     tom.library.adt.bytecode.types.access.VARARGS.make() ,
     tom.library.adt.bytecode.types.access.VOLATILE.make() 
  };

  public static AccessList buildAccess(int access) {
    AccessList list =  tom.library.adt.bytecode.types.accesslist.EmptyAccessList.make() ;

    for(int i = 0; i < accessFlags.length; i++) {
      if((access & accessFlags[i]) != 0) {
       Access tmp = accessObj[i];
        list =  tom.library.adt.bytecode.types.accesslist.ConsAccessList.make(tmp, list) ;
      }
    }

    return list;
  }


  public static int buildAccessValue(AccessList list){
    int value =0;
    HashMap<Access,Integer> map = new HashMap<Access,Integer>();
    for(int i =0;i<accessObj.length;i++){
      map.put(accessObj[i],accessFlags[i]);
    }

    { /* unamed block */{ /* unamed block */if ( (list instanceof tom.library.adt.bytecode.types.AccessList) ) {if ( (((( tom.library.adt.bytecode.types.AccessList )list) instanceof tom.library.adt.bytecode.types.accesslist.ConsAccessList) || ((( tom.library.adt.bytecode.types.AccessList )list) instanceof tom.library.adt.bytecode.types.accesslist.EmptyAccessList)) ) { tom.library.adt.bytecode.types.AccessList  tomMatch32_end_4=(( tom.library.adt.bytecode.types.AccessList )list);do {{ /* unamed block */if (!( tomMatch32_end_4.isEmptyAccessList() )) {

        value = value | (map.get( tomMatch32_end_4.getHeadAccessList() )).intValue();
      }if ( tomMatch32_end_4.isEmptyAccessList() ) {tomMatch32_end_4=(( tom.library.adt.bytecode.types.AccessList )list);} else {tomMatch32_end_4= tomMatch32_end_4.getTailAccessList() ;}}} while(!( (tomMatch32_end_4==(( tom.library.adt.bytecode.types.AccessList )list)) ));}}}}

    return value;   
  }

  public static String buildSignature(Signature signature){
    String sig = null;
    { /* unamed block */{ /* unamed block */if ( (signature instanceof tom.library.adt.bytecode.types.Signature) ) {if ( ((( tom.library.adt.bytecode.types.Signature )signature) instanceof tom.library.adt.bytecode.types.signature.Signature) ) {
sig= (( tom.library.adt.bytecode.types.Signature )signature).getsig() ;}}}}

    return sig;
  }

  public static Value buildValue(Object v) {
    if(v instanceof String) {
      return  tom.library.adt.bytecode.types.value.StringValue.make((String)v) ;
    }
    else if(v instanceof Integer) {
      return  tom.library.adt.bytecode.types.value.IntValue.make(((Integer)v).intValue()) ;
    }
    else if(v instanceof Long) {
      return  tom.library.adt.bytecode.types.value.LongValue.make(((Long)v).longValue()) ;
    }
    else if(v instanceof Float) {
      return  tom.library.adt.bytecode.types.value.FloatValue.make(((Float)v).floatValue()) ;
    }
    else if(v instanceof Double) {
      return  tom.library.adt.bytecode.types.value.DoubleValue.make(((Double)v).doubleValue()) ;
    }
    return null;
  }

  public static Object buildConstant(Value value) {
    { /* unamed block */{ /* unamed block */if ( (value instanceof tom.library.adt.bytecode.types.Value) ) {if ( ((( tom.library.adt.bytecode.types.Value )value) instanceof tom.library.adt.bytecode.types.value.StringValue) ) {
 return  (( tom.library.adt.bytecode.types.Value )value).gets() ;}}}{ /* unamed block */if ( (value instanceof tom.library.adt.bytecode.types.Value) ) {if ( ((( tom.library.adt.bytecode.types.Value )value) instanceof tom.library.adt.bytecode.types.value.IntValue) ) {
return Integer.valueOf( (( tom.library.adt.bytecode.types.Value )value).geti() );}}}{ /* unamed block */if ( (value instanceof tom.library.adt.bytecode.types.Value) ) {if ( ((( tom.library.adt.bytecode.types.Value )value) instanceof tom.library.adt.bytecode.types.value.LongValue) ) {
return Long.valueOf( (( tom.library.adt.bytecode.types.Value )value).getl() );}}}{ /* unamed block */if ( (value instanceof tom.library.adt.bytecode.types.Value) ) {if ( ((( tom.library.adt.bytecode.types.Value )value) instanceof tom.library.adt.bytecode.types.value.FloatValue) ) {
return new Float( (( tom.library.adt.bytecode.types.Value )value).getf() );}}}{ /* unamed block */if ( (value instanceof tom.library.adt.bytecode.types.Value) ) {if ( ((( tom.library.adt.bytecode.types.Value )value) instanceof tom.library.adt.bytecode.types.value.DoubleValue) ) {
return new Double( (( tom.library.adt.bytecode.types.Value )value).getd() );}}}}

    return null;
  }

  public static StringList buildStringList(String[] array) {
    StringList list =  tom.library.adt.bytecode.types.stringlist.EmptyStringList.make() ;
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--) {
        String tmp=array[i];
        list =  tom.library.adt.bytecode.types.stringlist.ConsStringList.make(tmp, list) ;
      }
    }

    return list;
  }

  public static IntList buildIntList(int[] array) {
    IntList list =  tom.library.adt.bytecode.types.intlist.EmptyIntList.make() ;
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--) {
        int tmp=array[i];
        list =  tom.library.adt.bytecode.types.intlist.ConsIntList.make(tmp, list) ;
      }
    }

    return list;
  }

  public static TypeNode buildType(String type) {
    int t = Type.getType(type).getSort();
    switch(t) {
      case Type.ARRAY:
        return  tom.library.adt.bytecode.types.typenode.ARRAY.make() ;
      case Type.BOOLEAN:
        return  tom.library.adt.bytecode.types.typenode.BOOLEAN.make() ;
      case Type.BYTE:
        return  tom.library.adt.bytecode.types.typenode.BYTE.make() ;
      case Type.CHAR:
        return  tom.library.adt.bytecode.types.typenode.CHAR.make() ;
      case Type.DOUBLE:
        return  tom.library.adt.bytecode.types.typenode.DOUBLE.make() ;
      case Type.FLOAT:
        return  tom.library.adt.bytecode.types.typenode.FLOAT.make() ;
      case Type.INT:
        return  tom.library.adt.bytecode.types.typenode.INT.make() ;
      case Type.LONG:
        return  tom.library.adt.bytecode.types.typenode.LONG.make() ;
      case Type.OBJECT:
        return  tom.library.adt.bytecode.types.typenode.OBJECT.make() ;
      case Type.SHORT:
        return  tom.library.adt.bytecode.types.typenode.SHORT.make() ;
      case Type.VOID:
        return  tom.library.adt.bytecode.types.typenode.VOID.make() ;
      default:
        throw new RuntimeException("Unsupported Type :"+t);
    }
  }

  private static class Counter { public int count = 0; }
  public static FieldDescriptor buildFieldDescriptor(String desc) {
    Counter count = new Counter();
    FieldDescriptor fDesc = buildFieldDescriptorFrom(desc, count);
    if(count.count != desc.length()) {
      System.err.println("Malformed descriptor : " + desc);
    }
    return fDesc;
  }

  private static FieldDescriptor buildFieldDescriptorFrom(String desc, Counter count) {
    FieldDescriptor fDesc = null;
    switch(desc.charAt(count.count)) {
      case 'L':
        count.count++;
        int j = desc.indexOf(';', count.count);
        if(j == -1) {
          System.err.println("Malformed descriptor : " + desc);
        }
        String className = desc.substring(count.count, j);
        count.count += className.length() + 1;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.ObjectType.make(className) ;
        break;
      case '[':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.ArrayType.make(buildFieldDescriptorFrom(desc,count)) ;
        break;
      case 'B':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.B.make() ;
        break;
      case 'C':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.C.make() ;
        break;
      case 'D':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.D.make() ;
        break;
      case 'F':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.F.make() ;
        break;
      case 'I':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.I.make() ;
        break;
      case 'J':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.J.make() ;
        break;
      case 'S':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.S.make() ;
        break;
      case 'Z':
        count.count++;
        fDesc =  tom.library.adt.bytecode.types.fielddescriptor.Z.make() ;
        break;
    }
    if(fDesc == null) {
      System.err.println("Malformed descriptor : " + desc);
    }
    return fDesc;
  }

  public static ReturnDescriptor buildReturnDescriptor(String desc) {
    if(desc.charAt(0) == 'V' && desc.length() == 1) {
      return  tom.library.adt.bytecode.types.returndescriptor.Void.make() ;
    }
    return  tom.library.adt.bytecode.types.returndescriptor.ReturnDescriptor.make(buildFieldDescriptor(desc)) ;
  }

  public static MethodDescriptor buildMethodDescriptor(String desc) {
    int endParam = desc.indexOf(')', 1);
    if(desc.charAt(0) != '(' || endParam == -1) {
      System.err.println("Malformed descriptor : " + desc);
    }
    FieldDescriptorList fList =  tom.library.adt.bytecode.types.fielddescriptorlist.EmptyFieldDescriptorList.make() ;
    Counter count = new Counter();
    count.count++;
    while(count.count < endParam)
      fList = tom_append_list_FieldDescriptorList(fList, tom.library.adt.bytecode.types.fielddescriptorlist.ConsFieldDescriptorList.make(buildFieldDescriptorFrom(desc,count), tom.library.adt.bytecode.types.fielddescriptorlist.EmptyFieldDescriptorList.make() ) );
    if(count.count != endParam) {
      System.err.println("Malformed descriptor : " + desc);
    }
    ReturnDescriptor ret = buildReturnDescriptor(desc.substring(count.count + 1));
    return  tom.library.adt.bytecode.types.methoddescriptor.MethodDescriptor.make(fList, ret) ;
  }

  public static class BuildDescriptor extends tom.library.sl.AbstractStrategyBasic {private  StringBuilder  sb;public BuildDescriptor( StringBuilder  sb) {super(( new tom.library.sl.Identity() ));this.sb=sb;}public  StringBuilder  getsb() {return sb;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {return ((T)visit_FieldDescriptor((( tom.library.adt.bytecode.types.FieldDescriptor )v),introspector));}if ( (v instanceof tom.library.adt.bytecode.types.ReturnDescriptor) ) {return ((T)visit_ReturnDescriptor((( tom.library.adt.bytecode.types.ReturnDescriptor )v),introspector));}if ( (v instanceof tom.library.adt.bytecode.types.MethodDescriptor) ) {return ((T)visit_MethodDescriptor((( tom.library.adt.bytecode.types.MethodDescriptor )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.library.adt.bytecode.types.MethodDescriptor  _visit_MethodDescriptor( tom.library.adt.bytecode.types.MethodDescriptor  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.library.adt.bytecode.types.MethodDescriptor )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.library.adt.bytecode.types.ReturnDescriptor  _visit_ReturnDescriptor( tom.library.adt.bytecode.types.ReturnDescriptor  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.library.adt.bytecode.types.ReturnDescriptor )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.library.adt.bytecode.types.FieldDescriptor  _visit_FieldDescriptor( tom.library.adt.bytecode.types.FieldDescriptor  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.library.adt.bytecode.types.FieldDescriptor )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.library.adt.bytecode.types.ReturnDescriptor  visit_ReturnDescriptor( tom.library.adt.bytecode.types.ReturnDescriptor  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.ReturnDescriptor) ) {























 sb.append(')'); }}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.ReturnDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.ReturnDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.returndescriptor.Void) ) {
 sb.append('V'); }}}}return _visit_ReturnDescriptor(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.library.adt.bytecode.types.MethodDescriptor  visit_MethodDescriptor( tom.library.adt.bytecode.types.MethodDescriptor  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.MethodDescriptor) ) { sb.append('('); }}}return _visit_MethodDescriptor(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.library.adt.bytecode.types.FieldDescriptor  visit_FieldDescriptor( tom.library.adt.bytecode.types.FieldDescriptor  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.ObjectType) ) { sb.append("L" +  (( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg).getclassName() + ";"); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.ArrayType) ) { sb.append('['); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.B) ) { sb.append('B'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.C) ) { sb.append('C'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.D) ) { sb.append('D'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.F) ) { sb.append('F'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.I) ) { sb.append('I'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.J) ) { sb.append('J'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.S) ) { sb.append('S'); }}}{ /* unamed block */if ( (tom__arg instanceof tom.library.adt.bytecode.types.FieldDescriptor) ) {if ( ((( tom.library.adt.bytecode.types.FieldDescriptor )tom__arg) instanceof tom.library.adt.bytecode.types.fielddescriptor.Z) ) { sb.append('Z'); }}}}return _visit_FieldDescriptor(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_BuildDescriptor( StringBuilder  t0) { return new BuildDescriptor(t0);}



  public static String buildDescriptor(FieldDescriptor desc) {
    StringBuilder sb = new StringBuilder();
    try {
      tom_make_TopDown(tom_make_BuildDescriptor(sb)).visitLight(desc);
    } catch(tom.library.sl.VisitFailure e) { }
    return sb.toString();
  }

  public static String buildDescriptor(MethodDescriptor desc) {
    StringBuilder sb = new StringBuilder();
    try {
      tom_make_TopDown(tom_make_BuildDescriptor(sb)).visitLight(desc);
    } catch(tom.library.sl.VisitFailure e) { }
    return sb.toString();
  }

 }

