/* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
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

    Pierre-Etienne Moreau e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.tools;

import jtom.*;
import java.util.*;
import jtom.adt.tomsignature.types.*;
import jtom.xml.*;
import jtom.exception.TomRuntimeException;

public class TomFactory extends TomBase {

// ------------------------------------------------------------
  /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/  /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/     /*  * old definition of String %typeterm String {   implement           { String }   get_fun_sym(t)      { t }   cmp_fun_sym(s1,s2)  { s1.equals(s2) }   get_subterm(t, n)   { null }   equals(t1,t2)       { t1.equals(t2) } } */ /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  */   /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/   /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/   /* Generated by TOM (version 2.1 stable - Under development): Do not edit this file *//*  *  * Copyright (c) 2004, Pierre-Etienne Moreau  * All rights reserved.  *   * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are  * met:   *  - Redistributions of source code must retain the above copyright  *  notice, this list of conditions and the following disclaimer.    *  - Redistributions in binary form must reproduce the above copyright  *  notice, this list of conditions and the following disclaimer in the  *  documentation and/or other materials provided with the distribution.  *  - Neither the name of the INRIA nor the names of its  *  contributors may be used to endorse or promote products derived from  *  this software without specific prior written permission.  *   * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *   **/     
// ------------------------------------------------------------

  public TomFactory() {
    super();
  }
  
  public String encodeXMLString(SymbolTable symbolTable, String name) {
    name = "\"" + name + "\"";
    getAstFactory().makeStringSymbol(symbolTable,name, new LinkedList());
    return name;
  }

  public TomList metaEncodeTermList(SymbolTable symbolTable,TomList list) {
     { jtom.adt.tomsignature.types.TomList tom_match1_1=(( jtom.adt.tomsignature.types.TomList)list);{ if(tom_is_fun_sym_emptyTomList(tom_match1_1) ||  false ) {
 return tom_make_emptyTomList(); } if(tom_is_fun_sym_manyTomList(tom_match1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match1_1_1=tom_get_slot_manyTomList_head(tom_match1_1); { jtom.adt.tomsignature.types.TomList tom_match1_1_2=tom_get_slot_manyTomList_tail(tom_match1_1); { jtom.adt.tomsignature.types.TomTerm head=tom_match1_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match1_1_2;

        return tom_make_manyTomList(metaEncodeXMLAppl(symbolTable,head),metaEncodeTermList(symbolTable,tail))
;
      }}}} }}}

    return list;
  }

  public TomTerm encodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * encode a String into a quoted-string
       * Appl(...,Name("string"),...) becomes
       * Appl(...,Name("\"string\""),...)
       */
    NameList newNameList = tom_empty_list_concTomName();
     { jtom.adt.tomsignature.types.TomTerm tom_match2_1=(( jtom.adt.tomsignature.types.TomTerm)term);{ if(tom_is_fun_sym_Appl(tom_match2_1) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match2_1_2=tom_get_slot_Appl_nameList(tom_match2_1); if(tom_is_fun_sym_concTomName(tom_match2_1_2) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match2_1_2_list1=tom_match2_1_2; { jtom.adt.tomsignature.types.NameList tom_match2_1_2_begin1=tom_match2_1_2_list1; { jtom.adt.tomsignature.types.NameList tom_match2_1_2_end1=tom_match2_1_2_list1; while (!(tom_is_empty_NameList(tom_match2_1_2_end1))) {tom_match2_1_2_list1=tom_match2_1_2_end1;{ { jtom.adt.tomsignature.types.TomName tom_match2_1_2_2=tom_get_head_NameList(tom_match2_1_2_list1);tom_match2_1_2_list1=tom_get_tail_NameList(tom_match2_1_2_list1); if(tom_is_fun_sym_Name(tom_match2_1_2_2) ||  false ) { { String  tom_match2_1_2_2_1=tom_get_slot_Name_string(tom_match2_1_2_2); { String  name=tom_match2_1_2_2_1;

        newNameList = (NameList)newNameList.append(tom_make_Name(encodeXMLString(symbolTable,name)));
      }} }}tom_match2_1_2_end1=tom_get_tail_NameList(tom_match2_1_2_end1);} }}}} }} }}}

    term = term.setNameList(newNameList);
      //System.out.println("encodeXMLAppl = " + term);
    return term;
  }

  public TomTerm metaEncodeXMLAppl(SymbolTable symbolTable, TomTerm term) {
      /*
       * meta-encode a String into a TextNode
       * Appl(...,Name("\"string\""),...) becomes
       * Appl(...,Name("TextNode"),[Appl(...,Name("\"string\""),...)],...)
       */
      //System.out.println("metaEncode: " + term);
     { jtom.adt.tomsignature.types.TomTerm tom_match3_1=(( jtom.adt.tomsignature.types.TomTerm)term);{ if(tom_is_fun_sym_Appl(tom_match3_1) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match3_1_2=tom_get_slot_Appl_nameList(tom_match3_1); if(tom_is_fun_sym_concTomName(tom_match3_1_2) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match3_1_2_list1=tom_match3_1_2; if(!(tom_is_empty_NameList(tom_match3_1_2_list1))) { { jtom.adt.tomsignature.types.TomName tom_match3_1_2_1=tom_get_head_NameList(tom_match3_1_2_list1);tom_match3_1_2_list1=tom_get_tail_NameList(tom_match3_1_2_list1); if(tom_is_fun_sym_Name(tom_match3_1_2_1) ||  false ) { { String  tom_match3_1_2_1_1=tom_get_slot_Name_string(tom_match3_1_2_1); { String  tomName=tom_match3_1_2_1_1; if(tom_is_empty_NameList(tom_match3_1_2_list1)) {

          //System.out.println("tomName = " + tomName);
        TomSymbol tomSymbol = symbolTable.getSymbol(tomName);
        if(tomSymbol != null) {
          if(isStringOperator(tomSymbol)) {
            Option info = tom_make_OriginTracking(tom_make_Name(Constants.TEXT_NODE),-1,tom_make_Name("??"));
            term = tom_make_Appl(getAstFactory().makeOption(info),tom_cons_list_concTomName(tom_make_Name(Constants.TEXT_NODE),tom_empty_list_concTomName()),tom_cons_list_concTomTerm(term,tom_empty_list_concTomTerm()),tsf().makeConstraintList())

;
              //System.out.println("metaEncodeXmlAppl = " + term);
          }
        }
       }}} }} }} }} }}}

    return term;
  }

  public boolean isExplicitTermList(LinkedList childs) {
    if(childs.size() == 1) {
      TomTerm term = (TomTerm) childs.getFirst();
       { jtom.adt.tomsignature.types.TomTerm tom_match4_1=(( jtom.adt.tomsignature.types.TomTerm)term);{ if(tom_is_fun_sym_Appl(tom_match4_1) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match4_1_2=tom_get_slot_Appl_nameList(tom_match4_1); { jtom.adt.tomsignature.types.TomList tom_match4_1_3=tom_get_slot_Appl_args(tom_match4_1); if(tom_is_fun_sym_concTomName(tom_match4_1_2) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match4_1_2_list1=tom_match4_1_2; if(!(tom_is_empty_NameList(tom_match4_1_2_list1))) { { jtom.adt.tomsignature.types.TomName tom_match4_1_2_1=tom_get_head_NameList(tom_match4_1_2_list1);tom_match4_1_2_list1=tom_get_tail_NameList(tom_match4_1_2_list1); if(tom_is_fun_sym_Name(tom_match4_1_2_1) ||  false ) { { String  tom_match4_1_2_1_1=tom_get_slot_Name_string(tom_match4_1_2_1); if(tom_cmp_fun_sym_String(tom_get_fun_sym_String(tom_match4_1_2_1_1) , "") ||  false ) { if(tom_is_empty_NameList(tom_match4_1_2_list1)) { { jtom.adt.tomsignature.types.TomList args=tom_match4_1_3;

          return true;
        } } }} }} }} }}} }}}

    }
    return false;
  }
  
  public LinkedList metaEncodeExplicitTermList(SymbolTable symbolTable, TomTerm term) {
    LinkedList list = new LinkedList();
     { jtom.adt.tomsignature.types.TomTerm tom_match5_1=(( jtom.adt.tomsignature.types.TomTerm)term);{ if(tom_is_fun_sym_Appl(tom_match5_1) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match5_1_2=tom_get_slot_Appl_nameList(tom_match5_1); { jtom.adt.tomsignature.types.TomList tom_match5_1_3=tom_get_slot_Appl_args(tom_match5_1); if(tom_is_fun_sym_concTomName(tom_match5_1_2) ||  false ) { { jtom.adt.tomsignature.types.NameList tom_match5_1_2_list1=tom_match5_1_2; if(!(tom_is_empty_NameList(tom_match5_1_2_list1))) { { jtom.adt.tomsignature.types.TomName tom_match5_1_2_1=tom_get_head_NameList(tom_match5_1_2_list1);tom_match5_1_2_list1=tom_get_tail_NameList(tom_match5_1_2_list1); if(tom_is_fun_sym_Name(tom_match5_1_2_1) ||  false ) { { String  tom_match5_1_2_1_1=tom_get_slot_Name_string(tom_match5_1_2_1); if(tom_cmp_fun_sym_String(tom_get_fun_sym_String(tom_match5_1_2_1_1) , "") ||  false ) { if(tom_is_empty_NameList(tom_match5_1_2_list1)) { { jtom.adt.tomsignature.types.TomList args=tom_match5_1_3;

        while(!args.isEmpty()) {
          list.add(metaEncodeXMLAppl(symbolTable,args.getHead()));
          args= args.getTail();
        }
        return list;
      } } }} }} }} }}} }


          //System.out.println("metaEncodeExplicitTermList: strange case: " + term);
        list.add(term);
        return list;
      }}

  }

  public TomTerm buildList(TomName name,TomList args) {
     { jtom.adt.tomsignature.types.TomList tom_match6_1=(( jtom.adt.tomsignature.types.TomList)args);{ if(tom_is_fun_sym_emptyTomList(tom_match6_1) ||  false ) {

        return tom_make_BuildEmptyList(name);
       } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_VariableStar(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;



        TomTerm subList = buildList(name,tail);
        return tom_make_BuildAppendList(name,head,subList);
      }} }}} } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_Composite(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomList tom_match6_1_1_1=tom_get_slot_Composite_args(tom_match6_1_1); if(tom_is_fun_sym_concTomTerm(tom_match6_1_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomList tom_match6_1_1_1_list1=tom_match6_1_1_1; { jtom.adt.tomsignature.types.TomList tom_match6_1_1_1_begin1=tom_match6_1_1_1_list1; { jtom.adt.tomsignature.types.TomList tom_match6_1_1_1_end1=tom_match6_1_1_1_list1; while (!(tom_is_empty_TomList(tom_match6_1_1_1_end1))) {tom_match6_1_1_1_list1=tom_match6_1_1_1_end1;{ { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1_1_2=tom_get_head_TomList(tom_match6_1_1_1_list1);tom_match6_1_1_1_list1=tom_get_tail_TomList(tom_match6_1_1_1_list1); if(tom_is_fun_sym_VariableStar(tom_match6_1_1_1_2) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1_1_2; if(tom_is_empty_TomList(tom_match6_1_1_1_list1)) { { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;          TomTerm subList = buildList(name,tail);          return tom_make_BuildAppendList(name,head,subList);        } }} }}tom_match6_1_1_1_end1=tom_get_tail_TomList(tom_match6_1_1_1_end1);} }}}} }} }}} } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_BuildTerm(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;





        TomTerm subList = buildList(name,tail);
        return tom_make_BuildConsList(name,head,subList);
      }} }}} } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_BuildVariable(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;          TomTerm subList = buildList(name,tail);          return tom_make_BuildConsList(name,head,subList);        }} }}} } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_Variable(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;          TomTerm subList = buildList(name,tail);          return tom_make_BuildConsList(name,head,subList);        }} }}} } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_Composite(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;          TomTerm subList = buildList(name,tail);          return tom_make_BuildConsList(name,head,subList);        }} }}} } if(tom_is_fun_sym_manyTomList(tom_match6_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match6_1_1=tom_get_slot_manyTomList_head(tom_match6_1); { jtom.adt.tomsignature.types.TomList tom_match6_1_2=tom_get_slot_manyTomList_tail(tom_match6_1); if(tom_is_fun_sym_TargetLanguageToTomTerm(tom_match6_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match6_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match6_1_2;


        TomTerm subList = buildList(name,tail);
        return subList;
      }} }}} }}}



    throw new TomRuntimeException("buildList strange term: " + args);
     
  }

  public TomTerm buildArray(TomName name,TomList args) {
    return buildArray(name,(TomList)args.reverse(),0);
  }

  private TomTerm buildArray(TomName name,TomList args, int size) {
     { jtom.adt.tomsignature.types.TomList tom_match7_1=(( jtom.adt.tomsignature.types.TomList)args);{ if(tom_is_fun_sym_emptyTomList(tom_match7_1) ||  false ) {

        return tom_make_BuildEmptyArray(name,size);
       } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_VariableStar(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;



          /*System.out.println("head = " + head);*/
        TomTerm subList = buildArray(name,tail,size+1);
        return tom_make_BuildAppendArray(name,head,subList);
      }} }}} } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_Composite(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomList tom_match7_1_1_1=tom_get_slot_Composite_args(tom_match7_1_1); if(tom_is_fun_sym_concTomTerm(tom_match7_1_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomList tom_match7_1_1_1_list1=tom_match7_1_1_1; { jtom.adt.tomsignature.types.TomList tom_match7_1_1_1_begin1=tom_match7_1_1_1_list1; { jtom.adt.tomsignature.types.TomList tom_match7_1_1_1_end1=tom_match7_1_1_1_list1; while (!(tom_is_empty_TomList(tom_match7_1_1_1_end1))) {tom_match7_1_1_1_list1=tom_match7_1_1_1_end1;{ { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1_1_2=tom_get_head_TomList(tom_match7_1_1_1_list1);tom_match7_1_1_1_list1=tom_get_tail_TomList(tom_match7_1_1_1_list1); if(tom_is_fun_sym_VariableStar(tom_match7_1_1_1_2) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1_1_2; if(tom_is_empty_TomList(tom_match7_1_1_1_list1)) { { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;            /*System.out.println("head = " + head);*/          TomTerm subList = buildArray(name,tail,size+1);          return tom_make_BuildAppendArray(name,head,subList);        } }} }}tom_match7_1_1_1_end1=tom_get_tail_TomList(tom_match7_1_1_1_end1);} }}}} }} }}} } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_BuildTerm(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;





        TomTerm subList = buildArray(name,tail,size+1);
        return tom_make_BuildConsArray(name,head,subList);
      }} }}} } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_BuildVariable(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;          TomTerm subList = buildArray(name,tail,size+1);          return tom_make_BuildConsArray(name,head,subList);        }} }}} } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_Variable(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;          TomTerm subList = buildArray(name,tail,size+1);          return tom_make_BuildConsArray(name,head,subList);        }} }}} } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_Composite(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;          TomTerm subList = buildArray(name,tail,size+1);          return tom_make_BuildConsArray(name,head,subList);        }} }}} } if(tom_is_fun_sym_manyTomList(tom_match7_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm tom_match7_1_1=tom_get_slot_manyTomList_head(tom_match7_1); { jtom.adt.tomsignature.types.TomList tom_match7_1_2=tom_get_slot_manyTomList_tail(tom_match7_1); if(tom_is_fun_sym_TargetLanguageToTomTerm(tom_match7_1_1) ||  false ) { { jtom.adt.tomsignature.types.TomTerm head=tom_match7_1_1; { jtom.adt.tomsignature.types.TomList tail=tom_match7_1_2;


	TomTerm subList = buildArray(name,tail,size);
	return subList;
      }} }}} }}}



    throw new TomRuntimeException("buildArray strange term: " + args);
     
  }

  
}
