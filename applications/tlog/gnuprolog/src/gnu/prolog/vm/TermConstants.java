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

public interface TermConstants
{
  // atoms used for errors
  // valid types 
  public static final AtomTerm atomAtom               = AtomTerm.get("atom");
  public static final AtomTerm atomicAtom             = AtomTerm.get("atomic");
  public static final AtomTerm byteAtom               = AtomTerm.get("byte");
  public static final AtomTerm callableAtom           = AtomTerm.get("callable");
  public static final AtomTerm characterAtom          = AtomTerm.get("character");
  public static final AtomTerm compoundAtom           = AtomTerm.get("compound");
  public static final AtomTerm evaluableAtom          = AtomTerm.get("evaluable");
  public static final AtomTerm inByteAtom             = AtomTerm.get("in_byte");
  public static final AtomTerm inCharacterAtom        = AtomTerm.get("in_character");
  public static final AtomTerm integerAtom            = AtomTerm.get("integer");
  public static final AtomTerm listAtom               = AtomTerm.get("list");
  public static final AtomTerm numberAtom             = AtomTerm.get("number");
  public static final AtomTerm predicateIndicatorAtom = AtomTerm.get("predicate_indicator");
  public static final AtomTerm variableAtom           = AtomTerm.get("variable");
  // valid domains
  public static final AtomTerm characterCodeListAtom = AtomTerm.get("character_code_list");
  public static final AtomTerm closeOptionAtom       = AtomTerm.get("close_option");
  public static final AtomTerm flagValueAtom         = AtomTerm.get("flag_value");
  public static final AtomTerm ioModeAtom            = AtomTerm.get("io_mode");
  public static final AtomTerm nonEmptyListAtom      = AtomTerm.get("non_empty_list");
  public static final AtomTerm notLessThenZeroAtom   = AtomTerm.get("not_less_then_zero");
  public static final AtomTerm operatorPriorityAtom  = AtomTerm.get("operator_priority");
  public static final AtomTerm operatorSpecifierAtom = AtomTerm.get("operator_specifier");
  public static final AtomTerm prologFlagAtom        = AtomTerm.get("prolog_flag");
  public static final AtomTerm readOptionAtom        = AtomTerm.get("read_option");
  public static final AtomTerm sourceSinkAtom        = AtomTerm.get("source_sink");
  public static final AtomTerm streamAtom            = AtomTerm.get("stream");
  public static final AtomTerm streamOptionAtom      = AtomTerm.get("stream_option");
  public static final AtomTerm streamOrAliasAtom     = AtomTerm.get("stream_or_alias");
  public static final AtomTerm streamPositionAtom    = AtomTerm.get("stream_position");
  public static final AtomTerm streamPropertyAtom    = AtomTerm.get("stream_property");
  public static final AtomTerm writeOptionAtom       = AtomTerm.get("write_option");
  // object types - 
  public static final AtomTerm procedureAtom = AtomTerm.get("procedure");
  //stream, source_sink

  // oprations
  public static final AtomTerm accessAtom     = AtomTerm.get("access");
  public static final AtomTerm createAtom     = AtomTerm.get("create");
  public static final AtomTerm inputAtom      = AtomTerm.get("input");
  public static final AtomTerm modifyAtom     = AtomTerm.get("modify");
  public static final AtomTerm openAtom       = AtomTerm.get("open");
  public static final AtomTerm outputAtom     = AtomTerm.get("output");
  public static final AtomTerm repositionAtom = AtomTerm.get("reposition");
  // permission types
  public static final AtomTerm binaryStreamAtom     = AtomTerm.get("binary_stream");
  public static final AtomTerm flagAtom             = AtomTerm.get("flag");
  public static final AtomTerm operatorAtom         = AtomTerm.get("operator");
  public static final AtomTerm pastEndOfStreamAtom  = AtomTerm.get("past_end_of_stream");
  public static final AtomTerm privateProcedureAtom = AtomTerm.get("private_procedure");
  public static final AtomTerm staticProcedureAtom  = AtomTerm.get("static_procedure");
  public static final AtomTerm textStreamAtom       = AtomTerm.get("text_stream");
  //source_sink, stream

  // flags for representation error
  //character
  public static final AtomTerm characterCodeAtom   = AtomTerm.get("character_code");
  public static final AtomTerm inCharacterCodeAtom = AtomTerm.get("in_character_code");
  public static final AtomTerm maxArityAtom        = AtomTerm.get("max_arity");
  public static final AtomTerm maxIntegerAtom      = AtomTerm.get("max_integer");
  public static final AtomTerm minIntegerAtom      = AtomTerm.get("min_integer");

  // evaluation errors
  public static final AtomTerm floatOverflowAtom = AtomTerm.get("float_overflow");
  public static final AtomTerm intOverflowAtom   = AtomTerm.get("int_overflow");
  public static final AtomTerm undefinedAtom     = AtomTerm.get("undefined");      
  public static final AtomTerm underflowAtom     = AtomTerm.get("underflow");      
  public static final AtomTerm zeroDivizorAtom   = AtomTerm.get("zero_divizor");   

  // misc
  public static final AtomTerm trueAtom = AtomTerm.get("true");
  public static final AtomTerm falseAtom = AtomTerm.get("false");

  // list constants
  public static final AtomTerm emptyListAtom = AtomTerm.get("[]");
  public static final CompoundTermTag listTag = CompoundTermTag.get(".",2);
}