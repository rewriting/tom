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
% build in predicates
% Numbers before section descriptions correspond to parts of ISO Prolog 
% standard
% 7.8 control constructs rules for control constructs are not the same as for
% predicates, as result following declarations are not needed, but they are 
% left for some future reason.
:-control(true/0,  'gnu.prolog.vm.buildin.control.Control_true'). 
:-control(fail/0,  'gnu.prolog.vm.buildin.control.Control_fail'). 
:-control(!/0,     'gnu.prolog.vm.buildin.control.Control_cut'). 
:-control((',')/2, 'gnu.prolog.vm.buildin.control.Control_and'). 
:-control((';')/2, 'gnu.prolog.vm.buildin.control.Control_or'). 
:-control(('->')/2,'gnu.prolog.vm.buildin.control.Control_if_then'). 
:-control(catch/3, 'gnu.prolog.vm.buildin.control.Control_catch'). 
:-control(throw/1, 'gnu.prolog.vm.buildin.control.Control_throw'). 
% the only used is call/1
%:-control(call/1, 'gnu.prolog.vm.interpreter.Predicate_call'). 
:-build_in(call/1, 'gnu.prolog.vm.interpreter.Predicate_call'). 

% 8.2 term unification
% I'm really considering making this control constructs 
:-build_in((=)/2,  'gnu.prolog.vm.buildins.unification.Predicate_unify'). 
:-build_in((unify_with_occurs_check)/2, 'gnu.prolog.vm.buildins.unification.Predicate_unify_with_occurs_check'). 
:-build_in((\=)/2, 'gnu.prolog.vm.buildins.unification.Predicate_not_unifiable'). 

% 8.3 type testing
:-build_in(var/1,     'gnu.prolog.vm.buildins.typetesting.Predicate_var'). 
:-build_in(atom/1,    'gnu.prolog.vm.buildins.typetesting.Predicate_atom'). 
:-build_in(integer/1, 'gnu.prolog.vm.buildins.typetesting.Predicate_integer'). 
%:-build_in(real/1,    'gnu.prolog.vm.buildins.typetesting.Predicate_real'). 
:-build_in(float/1,   'gnu.prolog.vm.buildins.typetesting.Predicate_float'). 
:-build_in(atomic/1,  'gnu.prolog.vm.buildins.typetesting.Predicate_atomic'). 
:-build_in(compound/1,'gnu.prolog.vm.buildins.typetesting.Predicate_compound'). 
:-build_in(nonvar/1,  'gnu.prolog.vm.buildins.typetesting.Predicate_nonvar'). 
:-build_in(number/1,  'gnu.prolog.vm.buildins.typetesting.Predicate_number'). 
:-build_in(java_object/1, 'gnu.prolog.vm.buildins.typetesting.Predicate_java_object'). 
                                     
% 8.4 term comparsion
:-build_in((==)/2,  'gnu.prolog.vm.buildins.termcomparsion.Predicate_term_identical'). 
:-build_in((\==)/2, 'gnu.prolog.vm.buildins.termcomparsion.Predicate_term_not_identical'). 
:-build_in((@<)/2,  'gnu.prolog.vm.buildins.termcomparsion.Predicate_term_less_then'). 
:-build_in((@=<)/2, 'gnu.prolog.vm.buildins.termcomparsion.Predicate_term_less_then_or_equal'). 
:-build_in((@>)/2,  'gnu.prolog.vm.buildins.termcomparsion.Predicate_term_greater_then'). 
:-build_in((@>=)/2, 'gnu.prolog.vm.buildins.termcomparsion.Predicate_term_greater_then_or_equal'). 

% 8.5 term creation and decomposition
:-build_in(functor/3,  'gnu.prolog.vm.buildins.termcreation.Predicate_functor'). 
:-build_in(arg/3,      'gnu.prolog.vm.buildins.termcreation.Predicate_arg'). 
:-build_in((=..)/2,    'gnu.prolog.vm.buildins.termcreation.Predicate_univ'). 
:-build_in(copy_term/2,'gnu.prolog.vm.buildins.termcreation.Predicate_copy_term'). 

% 8.6 arithmetics evaluation
:-build_in((is)/2,'gnu.prolog.vm.buildins.arithmetics.Predicate_is'). 

% 8.7 arithmetic comparsion
:-build_in((=:=)/2,'gnu.prolog.vm.buildins.arithmetics.Predicate_equal'). 
:-build_in((=\=)/2,'gnu.prolog.vm.buildins.arithmetics.Predicate_not_equal'). 
:-build_in((<)/2,  'gnu.prolog.vm.buildins.arithmetics.Predicate_less_than'). 
:-build_in((=<)/2, 'gnu.prolog.vm.buildins.arithmetics.Predicate_less_than_or_equal'). 
:-build_in((>)/2,  'gnu.prolog.vm.buildins.arithmetics.Predicate_greater_than'). 
:-build_in((>=)/2, 'gnu.prolog.vm.buildins.arithmetics.Predicate_greater_than_or_equal'). 

% 8.8 clause retrieval and information
:-build_in(clause/2,  'gnu.prolog.vm.buildins.database.Predicate_clause'). 
:-build_in(current_predicate/1, 'gnu.prolog.vm.buildins.database.Predicate_current_predicate'). 

% 8.9 clause creation and destruction
:-build_in(asserta/1,  'gnu.prolog.vm.buildins.database.Predicate_asserta'). 
:-build_in(assertz/1,  'gnu.prolog.vm.buildins.database.Predicate_assertz'). 
:-build_in(retract/1,  'gnu.prolog.vm.buildins.database.Predicate_retract'). 
:-build_in(abolish/1,  'gnu.prolog.vm.buildins.database.Predicate_abolish'). 

% 8.10 All solusions
:-build_in(findall/3, 'gnu.prolog.vm.buildins.allsolutions.Predicate_findall'). 
:-build_in(bagof/3,   'gnu.prolog.vm.buildins.allsolutions.Predicate_bagof'). 
:-build_in(setof/3,   'gnu.prolog.vm.buildins.allsolutions.Predicate_setof'). 

% 8.11 stream slection and control
:-build_in(current_input/1,  'gnu.prolog.vm.buildins.io.Predicate_current_input'). 
:-build_in(current_output/1, 'gnu.prolog.vm.buildins.io.Predicate_current_output'). 
:-build_in(set_input/1,      'gnu.prolog.vm.buildins.io.Predicate_set_input'). 
:-build_in(set_output/1,     'gnu.prolog.vm.buildins.io.Predicate_set_output'). 
:-build_in(open/4,           'gnu.prolog.vm.buildins.io.Predicate_open'). 
open(Source_sink, Mode, Stream):- open(Source_sink, Mode, Stream, []).
:-build_in(close/2,           'gnu.prolog.vm.buildins.io.Predicate_close'). 
close(S_or_a) :- close(S_or_a, []).
:-build_in(flush_output/1, 'gnu.prolog.vm.buildins.io.Predicate_flush_output'). 
flush_output:-current_output(Stream), flush_output(Stream).
:-build_in(stream_property/2, 'gnu.prolog.vm.buildins.io.Predicate_stream_property'). 
:-build_in(at_end_of_stream/1, 'gnu.prolog.vm.buildins.io.Predicate_at_end_of_stream'). 
at_end_of_stream:- current_input(S), at_end_of_stream(S).
:-build_in(set_stream_position/2, 'gnu.prolog.vm.buildins.io.Predicate_set_stream_position'). 

% 8.12 character input/output
:-build_in(get_char/2, 'gnu.prolog.vm.buildins.io.Predicate_get_char').
get_char(Char):-
   current_input(S),get_char(S,Char). 
get_code(Code):-
   current_input(S),
   get_char(S,Char),
   ( Char = end_of_file ->
     code = -1
   ; char_code(Char,Code)
   ). 
get_code(S, Code):-
   get_char(S,Char),
   ( Char = end_of_file ->
     code = -1
   ; char_code(Char,Code)
   ). 
:-build_in(peek_char/2, 'gnu.prolog.vm.buildins.io.Predicate_peek_char').
peek_char(Char):-
   current_input(S),peek_char(S,Char). 
peek_code(Code):-
   current_input(S),
   peek_char(S,Char),
   ( Char = end_of_file ->
     code = -1
   ; char_code(Char,Code)
   ). 
peek_code(S, Code):-
   peek_char(S,Char),
   ( Char = end_of_file ->
     code = -1
   ; char_code(Char,Code)
   ). 
:-build_in(put_char/2, 'gnu.prolog.vm.buildins.io.Predicate_put_char').
put_char(Char):- current_output(S),put_char(S,Char). 
put_code(Code):- current_output(S), char_code(Char,Code), put_char(S,Char).
put_code(S, Code):- char_code(Char,Code), put_char(S,Char).
nl(S):- put_char(S,'\n'). 
nl:- current_output(S),put_char(S,'\n'). 
  
% 8.13 byte input/output

:-build_in(get_byte/2, 'gnu.prolog.vm.buildins.io.Predicate_get_byte').
get_byte(Char):- current_input(S),get_byte(S,Char). 
:-build_in(peek_byte/2, 'gnu.prolog.vm.buildins.io.Predicate_peek_byte').
peek_byte(Char):- current_input(S),peek_byte(S,Char). 
:-build_in(put_byte/2, 'gnu.prolog.vm.buildins.io.Predicate_put_byte').
put_byte(Char):- current_output(S),put_byte(S,Char). 


% 8.14 Term input/output

:-build_in(read_term/3, 'gnu.prolog.vm.buildins.io.Predicate_read_term').
read_term(Term,Options):-current_input(S),read_term(S,Term,Options).
read(S, Term):-read_term(S, Term,[]).
read(Term):-current_input(S),read_term(S,Term,[]).
:-build_in(write_term/3, 'gnu.prolog.vm.buildins.io.Predicate_write_term').
write_term(Term,Options):-current_output(S),write_term(S,Term,Options).
write(Term):-current_output(S),write_term(S,Term,[numbervars(true)]).
write(S,Term):-write_term(S,Term,[numbervars(true)]).
writeq(Term):-current_output(S),write_term(S,Term,[quoted(true),numbervars(true)]).
writeq(S,Term):-write_term(S,Term,[quoted(true),numbervars(true)]).
write_canonical(Term):-current_output(S),write_term(S,Term,[quoted(true),ignore_ops(true)]).
write_canonical(S,Term):-write_term(S,Term,[quoted(true),ignore_ops(true)]).
:-build_in(op/3, 'gnu.prolog.vm.buildins.io.Predicate_op').
:-build_in(current_op/3, 'gnu.prolog.vm.buildins.io.Predicate_current_op').

% char conversions are not yet supported
%:-build_in(char_conversion/2, 'gnu.prolog.vm.buildins.io.Predicate_char_conversion').
%:-build_in(current_char_conversion/2, 'gnu.prolog.vm.buildins.io.Predicate_current_char_conversion').

% 8.15 logic and cotrol

'\\+'(Goal) :- call(Goal),!,fail.
'\\+'(Goal).

once(Goal) :- call(Goal),!.

repeat.
repeat:-repeat.

% 8.16 Atomic term processing
:-build_in(atom_length/2,'gnu.prolog.vm.buildins.atomicterms.Predicate_atom_length'). 
:-build_in(atom_concat/3,'gnu.prolog.vm.buildins.atomicterms.Predicate_atom_concat'). 
:-build_in(sub_atom/5,'gnu.prolog.vm.buildins.atomicterms.Predicate_sub_atom'). 
:-build_in(atom_chars/2,'gnu.prolog.vm.buildins.atomicterms.Predicate_atom_chars'). 
:-build_in(atom_codes/2,'gnu.prolog.vm.buildins.atomicterms.Predicate_atom_codes'). 
:-build_in(char_code/2,'gnu.prolog.vm.buildins.atomicterms.Predicate_char_code'). 
:-build_in(number_chars/2,'gnu.prolog.vm.buildins.atomicterms.Predicate_number_chars'). 
:-build_in(number_codes/2,'gnu.prolog.vm.buildins.atomicterms.Predicate_number_codes').

% 8.17 Implementation defined hooks
:-build_in(set_prolog_flag/2,'gnu.prolog.vm.buildins.imphooks.Predicate_set_prolog_flag'). 
:-build_in(current_prolog_flag/2,'gnu.prolog.vm.buildins.imphooks.Predicate_current_prolog_flag'). 
:-build_in(halt/1,'gnu.prolog.vm.buildins.imphooks.Predicate_halt'). 
halt:-halt(0).
