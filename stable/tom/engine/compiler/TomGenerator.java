    /*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
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

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.compiler;
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.*;

public class TomGenerator extends TomBase {

  public TomGenerator(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
  
    
    


    


    


    



    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    


    
// ------------------------------------------------------------
  
    /*
     * Generate the goal language
     */
  
  public void generate(jtom.tools.OutputCode out, int deep, TomTerm subject)
    throws IOException {
      //System.out.println("Generate: " + subject);
      //%variable

    statistics().numberPartsGoalLanguage++;

    
    {
       TomTerm  tom_match1_1 = null;
      tom_match1_1 = ( TomTerm ) subject;
matchlab_match1_pattern1: {
         TomList  l = null;
        if(tom_is_fun_sym_Tom(tom_match1_1)) {
           TomList  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomList ) tom_get_slot_Tom_list(tom_match1_1);
          l = ( TomList ) tom_match1_1_1;
          
        generateList(out,deep,l);
        return;
      
        }
}
matchlab_match1_pattern2: {
        if(tom_is_fun_sym_Line(tom_match1_1)) {
           String  tom_match1_1_1 = null;
          tom_match1_1_1 = ( String ) tom_get_slot_Line_string(tom_match1_1);
           return; 
        }
}
matchlab_match1_pattern3: {
         String  name = null;
        if(tom_is_fun_sym_BuildVariable(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_BuildVariable_astName(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            name = ( String ) tom_match1_1_1_1;
            
        out.write(name);
        return;
      
          }
        }
}
matchlab_match1_pattern4: {
         TomList  l1 = null;
        if(tom_is_fun_sym_BuildVariable(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_BuildVariable_astName(tom_match1_1);
          if(tom_is_fun_sym_Position(tom_match1_1_1)) {
             TomList  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match1_1_1);
            l1 = ( TomList ) tom_match1_1_1_1;
            
        out.write("tom" + numberListToIdentifier(l1));
          //out.write("tom");
          //numberListToIdentifier(out,l1);
        return;
      
          }
        }
}
matchlab_match1_pattern5: {
         String  name = null;
        if(tom_is_fun_sym_BuildVariableStar(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_BuildVariableStar_astName(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            name = ( String ) tom_match1_1_1_1;
            
        out.write(name);
        return;
      
          }
        }
}
matchlab_match1_pattern6: {
         TomList  argList = null;
         String  name = null;
        if(tom_is_fun_sym_BuildTerm(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_BuildTerm_astName(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_BuildTerm_args(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            name = ( String ) tom_match1_1_1_1;
            argList = ( TomList ) tom_match1_1_2;
            
        out.write("tom_make_");
        out.write(name);
        if(Flags.eCode && argList.isEmpty()) {
        } else {
          out.writeOpenBrace();
          while(!argList.isEmpty()) {
            generate(out,deep,argList.getHead());
            argList = argList.getTail();
            if(!argList.isEmpty()) {
              out.writeComa();
            }
          }
          out.writeCloseBrace();
        }
        return;
      
          }
        }
}
matchlab_match1_pattern7: {
         String  name = null;
         TomList  argList = null;
        if(tom_is_fun_sym_BuildList(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_BuildList_astName(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_BuildList_args(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            name = ( String ) tom_match1_1_1_1;
            argList = ( TomList ) tom_match1_1_2;
            
        TomSymbol tomSymbol = symbolTable().getSymbol(name);
        String listType = getTLType(getSymbolCodomain(tomSymbol));
        int size = 0;
        while(!argList.isEmpty()) {
          TomTerm elt = argList.getHead();
          if(elt.isBuildVariableStar()) {
            out.write("tom_insert_list_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          } else {
            out.write("tom_make_insert_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          }
          argList = argList.getTail();
          size++;
        }
        out.write("(" + listType + ") tom_make_empty_" + name + "()");
        for(int i=0; i<size; i++) {
          out.write(")");
        }
        return;
      
          }
        }
}
matchlab_match1_pattern8: {
         String  name = null;
         TomList  argList = null;
        if(tom_is_fun_sym_BuildArray(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_BuildArray_astName(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_BuildArray_args(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            name = ( String ) tom_match1_1_1_1;
            argList = ( TomList ) tom_match1_1_2;
            
        TomSymbol tomSymbol = symbolTable().getSymbol(name);
        String listType = getTLType(getSymbolCodomain(tomSymbol));
        int size = 0;
        TomList reverse = reverse(argList);
        while(!reverse.isEmpty()) {
          TomTerm elt = reverse.getHead();
          if(elt.isBuildVariableStar()) {
            out.write("tom_append_array_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          } else {
            out.write("tom_make_append_" + name + "(");
            generate(out,deep,elt);
            out.write(",");
          }
          reverse = reverse.getTail();
          size++;
        }
        out.write("(" + listType + ") tom_make_empty_" + name + "(" + size + ")"); 
        for(int i=0; i<size; i++) { 
            //out.write("," + i + ")");
          out.write(")"); 
        }
        return;
      
          }
        }
}
matchlab_match1_pattern9: {
         String  name = null;
         TomList  argList = null;
        if(tom_is_fun_sym_FunctionCall(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_FunctionCall_astName(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_FunctionCall_args(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            name = ( String ) tom_match1_1_1_1;
            argList = ( TomList ) tom_match1_1_2;
            
        out.write(name + "(");
        while(!argList.isEmpty()) {
          generate(out,deep,argList.getHead());
          argList = argList.getTail();
          if(!argList.isEmpty()) {
            out.write(", ");
          }
        }
        out.write(")");
        return;
      
          }
        }
}
matchlab_match1_pattern10: {
         TomTerm  t1 = null;
         TomTerm  t2 = null;
        if(tom_is_fun_sym_DotTerm(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
           TomTerm  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_DotTerm_kid1(tom_match1_1);
          tom_match1_1_2 = ( TomTerm ) tom_get_slot_DotTerm_kid2(tom_match1_1);
          t1 = ( TomTerm ) tom_match1_1_1;
          t2 = ( TomTerm ) tom_match1_1_2;
          
        generate(out,deep,t1);
        out.write(".");
        generate(out,deep,t2);
        return;
      
        }
}
matchlab_match1_pattern11: {
         TomList  matchDeclarationList = null;
         TomList  namedBlockList = null;
        if(tom_is_fun_sym_CompiledMatch(tom_match1_1)) {
           TomList  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomList ) tom_get_slot_CompiledMatch_decls(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_CompiledMatch_automataList(tom_match1_1);
          matchDeclarationList = ( TomList ) tom_match1_1_1;
          namedBlockList = ( TomList ) tom_match1_1_2;
          
        if(Flags.supportedBlock) {
          generate(out,deep,
tom_make_OpenBlock()          );
        }
        generateList(out,deep+1,matchDeclarationList);
        generateList(out,deep+1,namedBlockList);
        if(Flags.supportedBlock) {
          generate(out,deep,
tom_make_CloseBlock()          );
        }
        return;
      
        }
}
matchlab_match1_pattern12: {
         TomList  instList = null;
         String  blockName = null;
        if(tom_is_fun_sym_NamedBlock(tom_match1_1)) {
           String  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
          tom_match1_1_1 = ( String ) tom_get_slot_NamedBlock_blockName(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_NamedBlock_instList(tom_match1_1);
          blockName = ( String ) tom_match1_1_1;
          instList = ( TomList ) tom_match1_1_2;
          
        if(Flags.cCode) {
          out.writeln("{");
          generateList(out,deep+1,instList);
          out.writeln("}" + blockName +  ":;");
        } else if(Flags.jCode) {
          out.writeln(blockName + ": {");
          generateList(out,deep+1,instList);
          out.writeln("}");
        } else if(Flags.eCode) {
          System.out.println("NamedBlock: Eiffel code not yet implemented");
            //System.exit(1);
        }  
        return;
      
        }
}
matchlab_match1_pattern13: {
         Expression  exp = null;
         TomList  succesList = null;
        if(tom_is_fun_sym_IfThenElse(tom_match1_1)) {
           Expression  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
           TomList  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Expression ) tom_get_slot_IfThenElse_condition(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_IfThenElse_succesList(tom_match1_1);
          tom_match1_1_3 = ( TomList ) tom_get_slot_IfThenElse_failureList(tom_match1_1);
          exp = ( Expression ) tom_match1_1_1;
          succesList = ( TomList ) tom_match1_1_2;
          if(tom_is_fun_sym_Empty(tom_match1_1_3)) {
            
        statistics().numberIfThenElseTranformed++;
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,"if("); generateExpression(out,deep,exp); out.writeln(") {");
          generateList(out,deep+1,succesList);
          out.writeln(deep,"}");
        } else if(Flags.eCode) {
          out.write(deep,"if "); generateExpression(out,deep,exp); out.writeln(" then ");
          generateList(out,deep+1,succesList);
          out.writeln(deep,"end;");
        }
        return;
      
          }
        }
}
matchlab_match1_pattern14: {
         TomList  failureList = null;
         TomList  succesList = null;
         Expression  exp = null;
        if(tom_is_fun_sym_IfThenElse(tom_match1_1)) {
           Expression  tom_match1_1_1 = null;
           TomList  tom_match1_1_2 = null;
           TomList  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Expression ) tom_get_slot_IfThenElse_condition(tom_match1_1);
          tom_match1_1_2 = ( TomList ) tom_get_slot_IfThenElse_succesList(tom_match1_1);
          tom_match1_1_3 = ( TomList ) tom_get_slot_IfThenElse_failureList(tom_match1_1);
          exp = ( Expression ) tom_match1_1_1;
          succesList = ( TomList ) tom_match1_1_2;
          failureList = ( TomList ) tom_match1_1_3;
          
        statistics().numberIfThenElseTranformed++;
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,"if("); generateExpression(out,deep,exp); out.writeln(") {");
          generateList(out,deep+1,succesList);
          out.writeln(deep,"} else {");
          generateList(out,deep+1,failureList);
          out.writeln(deep,"}");
        } else if(Flags.eCode) {
          out.write(deep,"if "); generateExpression(out,deep,exp); out.writeln(" then ");
          generateList(out,deep+1,succesList);
          out.writeln(deep," else ");
          generateList(out,deep+1,failureList);
          out.writeln(deep,"end;");
        }
        return;
      
        }
}
matchlab_match1_pattern15: {
         TomList  succesList = null;
         Expression  exp = null;
        if(tom_is_fun_sym_DoWhile(tom_match1_1)) {
           TomList  tom_match1_1_1 = null;
           Expression  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomList ) tom_get_slot_DoWhile_instList(tom_match1_1);
          tom_match1_1_2 = ( Expression ) tom_get_slot_DoWhile_condition(tom_match1_1);
          succesList = ( TomList ) tom_match1_1_1;
          exp = ( Expression ) tom_match1_1_2;
          
        out.writeln(deep,"do {");
        generateList(out,deep+1,succesList);
        out.write(deep,"} while("); generateExpression(out,deep,exp); out.writeln(");");
        return;
      
        }
}
matchlab_match1_pattern16: {
         TomList  l1 = null;
         Option  option1 = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_Variable(tom_match1_1)) {
           Option  tom_match1_1_1 = null;
           TomName  tom_match1_1_2 = null;
           TomType  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match1_1);
          tom_match1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match1_1);
          tom_match1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match1_1);
          option1 = ( Option ) tom_match1_1_1;
          if(tom_is_fun_sym_Position(tom_match1_1_2)) {
             TomList  tom_match1_1_2_1 = null;
            tom_match1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match1_1_2);
            l1 = ( TomList ) tom_match1_1_2_1;
            type1 = ( TomType ) tom_match1_1_3;
            
          //System.out.println("Variable(option1,Position(l1),type1)");
          //System.out.println("subject = " + subject);
          //System.out.println("l1      = " + l1);

          /*
           * sans type: re-definition lorsque %variable est utilise
           * avec type: probleme en cas de filtrage dynamique
           */
        out.write("tom" + numberListToIdentifier(l1));
        return;
      
          }
        }
}
matchlab_match1_pattern17: {
         String  name1 = null;
         Option  option1 = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_Variable(tom_match1_1)) {
           Option  tom_match1_1_1 = null;
           TomName  tom_match1_1_2 = null;
           TomType  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match1_1);
          tom_match1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match1_1);
          tom_match1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match1_1);
          option1 = ( Option ) tom_match1_1_1;
          if(tom_is_fun_sym_Name(tom_match1_1_2)) {
             String  tom_match1_1_2_1 = null;
            tom_match1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_2);
            name1 = ( String ) tom_match1_1_2_1;
            type1 = ( TomType ) tom_match1_1_3;
            
        out.write(name1);
        return;
      
          }
        }
}
matchlab_match1_pattern18: {
         Option  option1 = null;
         TomList  l1 = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_VariableStar(tom_match1_1)) {
           Option  tom_match1_1_1 = null;
           TomName  tom_match1_1_2 = null;
           TomType  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match1_1);
          tom_match1_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match1_1);
          tom_match1_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match1_1);
          option1 = ( Option ) tom_match1_1_1;
          if(tom_is_fun_sym_Position(tom_match1_1_2)) {
             TomList  tom_match1_1_2_1 = null;
            tom_match1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match1_1_2);
            l1 = ( TomList ) tom_match1_1_2_1;
            type1 = ( TomType ) tom_match1_1_3;
            
        out.write("tom" + numberListToIdentifier(l1));
        return;  
      
          }
        }
}
matchlab_match1_pattern19: {
         String  name1 = null;
         TomType  type1 = null;
         Option  option1 = null;
        if(tom_is_fun_sym_VariableStar(tom_match1_1)) {
           Option  tom_match1_1_1 = null;
           TomName  tom_match1_1_2 = null;
           TomType  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match1_1);
          tom_match1_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match1_1);
          tom_match1_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match1_1);
          option1 = ( Option ) tom_match1_1_1;
          if(tom_is_fun_sym_Name(tom_match1_1_2)) {
             String  tom_match1_1_2_1 = null;
            tom_match1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_2);
            name1 = ( String ) tom_match1_1_2_1;
            type1 = ( TomType ) tom_match1_1_3;
            
        out.write(name1);
        return;
      
          }
        }
}
matchlab_match1_pattern20: {
         TomTerm  var = null;
         String  type = null;
         Option  option1 = null;
         TomName  name1 = null;
         String  tlType = null;
        if(tom_is_fun_sym_Declaration(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_Declaration_kid1(tom_match1_1);
          if(tom_is_fun_sym_Variable(tom_match1_1_1)) {
             Option  tom_match1_1_1_1 = null;
             TomName  tom_match1_1_1_2 = null;
             TomType  tom_match1_1_1_3 = null;
            tom_match1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match1_1_1);
            tom_match1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match1_1_1);
            tom_match1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match1_1_1);
            var = ( TomTerm ) tom_match1_1_1;
            option1 = ( Option ) tom_match1_1_1_1;
            name1 = ( TomName ) tom_match1_1_1_2;
            if(tom_is_fun_sym_Type(tom_match1_1_1_3)) {
               TomType  tom_match1_1_1_3_1 = null;
               TomType  tom_match1_1_1_3_2 = null;
              tom_match1_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match1_1_1_3);
              tom_match1_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match1_1_1_3);
              if(tom_is_fun_sym_TomType(tom_match1_1_1_3_1)) {
                 String  tom_match1_1_1_3_1_1 = null;
                tom_match1_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match1_1_1_3_1);
                type = ( String ) tom_match1_1_1_3_1_1;
                if(tom_is_fun_sym_TLType(tom_match1_1_1_3_2)) {
                   TargetLanguage  tom_match1_1_1_3_2_1 = null;
                  tom_match1_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match1_1_1_3_2);
                  if(tom_is_fun_sym_TL(tom_match1_1_1_3_2_1)) {
                     String  tom_match1_1_1_3_2_1_1 = null;
                    tom_match1_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match1_1_1_3_2_1);
                    tlType = ( String ) tom_match1_1_1_3_2_1_1;
                    
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,tlType + " ");
          generate(out,deep,var);
        } else if(Flags.eCode) {
          generate(out,deep,var);
          out.write(deep,": " + tlType);
        }
        
        if(Flags.jCode && !isBoolType(type) && !isIntType(type)) {
          out.writeln(" = null;");
        } else {
          out.writeln(";");
        }
        return;
      
                  }
                }
              }
            }
          }
        }
}
matchlab_match1_pattern21: {
         TomTerm  var = null;
         String  tlType = null;
         TomName  name1 = null;
         Option  option1 = null;
        if(tom_is_fun_sym_Declaration(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_Declaration_kid1(tom_match1_1);
          if(tom_is_fun_sym_VariableStar(tom_match1_1_1)) {
             Option  tom_match1_1_1_1 = null;
             TomName  tom_match1_1_1_2 = null;
             TomType  tom_match1_1_1_3 = null;
            tom_match1_1_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match1_1_1);
            tom_match1_1_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match1_1_1);
            tom_match1_1_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match1_1_1);
            var = ( TomTerm ) tom_match1_1_1;
            option1 = ( Option ) tom_match1_1_1_1;
            name1 = ( TomName ) tom_match1_1_1_2;
            if(tom_is_fun_sym_Type(tom_match1_1_1_3)) {
               TomType  tom_match1_1_1_3_1 = null;
               TomType  tom_match1_1_1_3_2 = null;
              tom_match1_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match1_1_1_3);
              tom_match1_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match1_1_1_3);
              if(tom_is_fun_sym_TLType(tom_match1_1_1_3_2)) {
                 TargetLanguage  tom_match1_1_1_3_2_1 = null;
                tom_match1_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match1_1_1_3_2);
                if(tom_is_fun_sym_TL(tom_match1_1_1_3_2_1)) {
                   String  tom_match1_1_1_3_2_1_1 = null;
                  tom_match1_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match1_1_1_3_2_1);
                  tlType = ( String ) tom_match1_1_1_3_2_1_1;
                  
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,tlType + " ");
          generate(out,deep,var);
        } else if(Flags.eCode) {
          generate(out,deep,var);
          out.write(deep,": " + tlType);
        }
        out.writeln(";");
        return;
      
                }
              }
            }
          }
        }
}
matchlab_match1_pattern22: {
         TomTerm  var = null;
         Option  option1 = null;
         TomName  name1 = null;
         String  tlType = null;
         String  type = null;
         Expression  exp = null;
        if(tom_is_fun_sym_Assign(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
           Expression  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_Assign_kid1(tom_match1_1);
          tom_match1_1_2 = ( Expression ) tom_get_slot_Assign_source(tom_match1_1);
          if(tom_is_fun_sym_Variable(tom_match1_1_1)) {
             Option  tom_match1_1_1_1 = null;
             TomName  tom_match1_1_1_2 = null;
             TomType  tom_match1_1_1_3 = null;
            tom_match1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match1_1_1);
            tom_match1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match1_1_1);
            tom_match1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match1_1_1);
            var = ( TomTerm ) tom_match1_1_1;
            option1 = ( Option ) tom_match1_1_1_1;
            name1 = ( TomName ) tom_match1_1_1_2;
            if(tom_is_fun_sym_Type(tom_match1_1_1_3)) {
               TomType  tom_match1_1_1_3_1 = null;
               TomType  tom_match1_1_1_3_2 = null;
              tom_match1_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match1_1_1_3);
              tom_match1_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match1_1_1_3);
              if(tom_is_fun_sym_TomType(tom_match1_1_1_3_1)) {
                 String  tom_match1_1_1_3_1_1 = null;
                tom_match1_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match1_1_1_3_1);
                type = ( String ) tom_match1_1_1_3_1_1;
                if(tom_is_fun_sym_TLType(tom_match1_1_1_3_2)) {
                   TargetLanguage  tom_match1_1_1_3_2_1 = null;
                  tom_match1_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match1_1_1_3_2);
                  if(tom_is_fun_sym_TL(tom_match1_1_1_3_2_1)) {
                     String  tom_match1_1_1_3_2_1_1 = null;
                    tom_match1_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match1_1_1_3_2_1);
                    tlType = ( String ) tom_match1_1_1_3_2_1_1;
                    exp = ( Expression ) tom_match1_1_2;
                    
        out.indent(deep);
        generate(out,deep,var);
        if(Flags.cCode || Flags.jCode) {
          out.write(" = (" + tlType + ") ");
        } else if(Flags.eCode) {
          if(isBoolType(type) || isIntType(type)) {
            out.write(" := ");
          } else {
              //out.write(" ?= ");
            String assignSign = " := ";
            
                    {
                       Expression  tom_match2_1 = null;
                      tom_match2_1 = ( Expression ) exp;
matchlab_match2_pattern1: {
                        if(tom_is_fun_sym_GetSubterm(tom_match2_1)) {
                           TomTerm  tom_match2_1_1 = null;
                           TomTerm  tom_match2_1_2 = null;
                          tom_match2_1_1 = ( TomTerm ) tom_get_slot_GetSubterm_term(tom_match2_1);
                          tom_match2_1_2 = ( TomTerm ) tom_get_slot_GetSubterm_number(tom_match2_1);
                          
                assignSign = " ?= ";
              
                        }
}
                    }
                    
            out.write(assignSign);
          }
        }
        generateExpression(out,deep,exp);
        out.writeln(";");
        return;
      
                  }
                }
              }
            }
          }
        }
}
matchlab_match1_pattern23: {
         TomTerm  var = null;
         String  tlType = null;
         TomName  name1 = null;
         Expression  exp = null;
         Option  option1 = null;
        if(tom_is_fun_sym_Assign(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
           Expression  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_Assign_kid1(tom_match1_1);
          tom_match1_1_2 = ( Expression ) tom_get_slot_Assign_source(tom_match1_1);
          if(tom_is_fun_sym_VariableStar(tom_match1_1_1)) {
             Option  tom_match1_1_1_1 = null;
             TomName  tom_match1_1_1_2 = null;
             TomType  tom_match1_1_1_3 = null;
            tom_match1_1_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match1_1_1);
            tom_match1_1_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match1_1_1);
            tom_match1_1_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match1_1_1);
            var = ( TomTerm ) tom_match1_1_1;
            option1 = ( Option ) tom_match1_1_1_1;
            name1 = ( TomName ) tom_match1_1_1_2;
            if(tom_is_fun_sym_Type(tom_match1_1_1_3)) {
               TomType  tom_match1_1_1_3_1 = null;
               TomType  tom_match1_1_1_3_2 = null;
              tom_match1_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match1_1_1_3);
              tom_match1_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match1_1_1_3);
              if(tom_is_fun_sym_TLType(tom_match1_1_1_3_2)) {
                 TargetLanguage  tom_match1_1_1_3_2_1 = null;
                tom_match1_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match1_1_1_3_2);
                if(tom_is_fun_sym_TL(tom_match1_1_1_3_2_1)) {
                   String  tom_match1_1_1_3_2_1_1 = null;
                  tom_match1_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match1_1_1_3_2_1);
                  tlType = ( String ) tom_match1_1_1_3_2_1_1;
                  exp = ( Expression ) tom_match1_1_2;
                  
        out.indent(deep);
        generate(out,deep,var);
        if(Flags.cCode || Flags.jCode) {
          out.write(" = (" + tlType + ") ");
        } else if(Flags.eCode) {
          out.write(" := ");
        }
        generateExpression(out,deep,exp);
        out.writeln(";");
        return;
      
                }
              }
            }
          }
        }
}
matchlab_match1_pattern24: {
         TomTerm  var = null;
        if(tom_is_fun_sym_Increment(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_Increment_kid1(tom_match1_1);
          if(tom_is_fun_sym_Variable(tom_match1_1_1)) {
             Option  tom_match1_1_1_1 = null;
             TomName  tom_match1_1_1_2 = null;
             TomType  tom_match1_1_1_3 = null;
            tom_match1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match1_1_1);
            tom_match1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match1_1_1);
            tom_match1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match1_1_1);
            var = ( TomTerm ) tom_match1_1_1;
            
        generate(out,deep,var);
        out.write(" = ");
        generate(out,deep,var);
        out.writeln(" + 1;");
        return;
      
          }
        }
}
matchlab_match1_pattern25: {
         TomList  l = null;
        if(tom_is_fun_sym_Action(tom_match1_1)) {
           TomList  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomList ) tom_get_slot_Action_instList(tom_match1_1);
          l = ( TomList ) tom_match1_1_1;
          
        while(!l.isEmpty()) {
          generate(out,deep,l.getHead());
          l = l.getTail();
        }
          //out.writeln("// ACTION: " + l);
        return;
      
        }
}
matchlab_match1_pattern26: {
         TomList  numberList = null;
        if(tom_is_fun_sym_ExitAction(tom_match1_1)) {
           TomList  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomList ) tom_get_slot_ExitAction_numberList(tom_match1_1);
          numberList = ( TomList ) tom_match1_1_1;
          
        if(Flags.cCode) {
          out.writeln(deep,"goto matchlab" + numberListToIdentifier(numberList) + ";");
        } else if(Flags.jCode) {
          out.writeln(deep,"break matchlab" + numberListToIdentifier(numberList) + ";");
        } else if(Flags.eCode) {
          System.out.println("ExitAction: Eiffel code not yet implemented");
            //System.exit(1);
        }
        return;
      
        }
}
matchlab_match1_pattern27: {
         TomTerm  exp = null;
        if(tom_is_fun_sym_Return(tom_match1_1)) {
           TomTerm  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TomTerm ) tom_get_slot_Return_kid1(tom_match1_1);
          exp = ( TomTerm ) tom_match1_1_1;
          
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,"return ");
          generate(out,deep,exp);
          out.writeln(deep,";");
        } else if(Flags.eCode) {
          out.writeln(deep,"if Result = Void then");
          out.write(deep+1,"Result := ");
          generate(out,deep+1,exp);
          out.writeln(deep+1,";");
          out.writeln(deep,"end;");
        }
        return;
      
        }
}
matchlab_match1_pattern28: {
        if(tom_is_fun_sym_OpenBlock(tom_match1_1)) {
           out.writeln(deep,"{"); return; 
        }
}
matchlab_match1_pattern29: {
        if(tom_is_fun_sym_CloseBlock(tom_match1_1)) {
           out.writeln(deep,"}"); return; 
        }
}
matchlab_match1_pattern30: {
        if(tom_is_fun_sym_EndLocalVariable(tom_match1_1)) {
           out.writeln(deep,"do"); return; 
        }
}
matchlab_match1_pattern31: {
         String  tomName = null;
         TomList  varList = null;
        if(tom_is_fun_sym_MakeFunctionBegin(tom_match1_1)) {
           TomName  tom_match1_1_1 = null;
           TomTerm  tom_match1_1_2 = null;
          tom_match1_1_1 = ( TomName ) tom_get_slot_MakeFunctionBegin_astName(tom_match1_1);
          tom_match1_1_2 = ( TomTerm ) tom_get_slot_MakeFunctionBegin_subjectListAST(tom_match1_1);
          if(tom_is_fun_sym_Name(tom_match1_1_1)) {
             String  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_1);
            tomName = ( String ) tom_match1_1_1_1;
            if(tom_is_fun_sym_SubjectList(tom_match1_1_2)) {
               TomList  tom_match1_1_2_1 = null;
              tom_match1_1_2_1 = ( TomList ) tom_get_slot_SubjectList_list(tom_match1_1_2);
              varList = ( TomList ) tom_match1_1_2_1;
              
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String glType = getTLType(getSymbolCodomain(tomSymbol));
        String name = tomSymbol.getAstName().getString();
        
        if(Flags.cCode || Flags.jCode) {
          out.write(deep,glType + " " + name + "(");
        } else if(Flags.eCode) {
          out.write(deep,name + "(");
        }
        while(!varList.isEmpty()) {
          TomTerm localVar = varList.getHead();
          matchBlock: {
            
              {
                 TomTerm  tom_match3_1 = null;
                tom_match3_1 = ( TomTerm ) localVar;
matchlab_match3_pattern1: {
                   TomTerm  v = null;
                   TomType  type2 = null;
                   Option  option2 = null;
                   TomName  name2 = null;
                  if(tom_is_fun_sym_Variable(tom_match3_1)) {
                     Option  tom_match3_1_1 = null;
                     TomName  tom_match3_1_2 = null;
                     TomType  tom_match3_1_3 = null;
                    tom_match3_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match3_1);
                    tom_match3_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match3_1);
                    tom_match3_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match3_1);
                    v = ( TomTerm ) tom_match3_1;
                    option2 = ( Option ) tom_match3_1_1;
                    name2 = ( TomName ) tom_match3_1_2;
                    type2 = ( TomType ) tom_match3_1_3;
                    
                if(Flags.cCode || Flags.jCode) {
                  out.write(deep,getTLType(type2) + " ");
                  generate(out,deep,v);
                } else if(Flags.eCode) {
                  generate(out,deep,v);
                  out.write(deep,": " + getTLType(type2));
                }
                break matchBlock;
              
                  }
}
matchlab_match3_pattern2: {
                  
                System.out.println("MakeFunction: strange term: " + localVar);
                System.exit(1);
              
}
              }
              
          }
          varList = varList.getTail();
          if(!varList.isEmpty()) {
            if(Flags.cCode || Flags.jCode) {
              out.write(deep,", ");
            } else if(Flags.eCode) {
              out.write(deep,"; ");
            }
          }
        }
        if(Flags.cCode || Flags.jCode) {
          out.writeln(deep,") {");
        } else if(Flags.eCode) {
          out.writeln(deep,"): " + glType + " is");
          out.writeln(deep,"local ");
        }
          //out.writeln(deep,"return null;");
        return;
      
            }
          }
        }
}
matchlab_match1_pattern32: {
        if(tom_is_fun_sym_MakeFunctionEnd(tom_match1_1)) {
          
        if(Flags.cCode || Flags.jCode) {
          out.writeln(deep,"}");
        } else if(Flags.eCode) {
          out.writeln(deep,"end;");
        }
        return;
      
        }
}
matchlab_match1_pattern33: {
         TargetLanguage  t = null;
        if(tom_is_fun_sym_TargetLanguageToTomTerm(tom_match1_1)) {
           TargetLanguage  tom_match1_1_1 = null;
          tom_match1_1_1 = ( TargetLanguage ) tom_get_slot_TargetLanguageToTomTerm_tl(tom_match1_1);
          t = ( TargetLanguage ) tom_match1_1_1;
          
        generateTargetLanguage(out,deep,t);
        return;
      
        }
}
matchlab_match1_pattern34: {
         Declaration  t = null;
        if(tom_is_fun_sym_DeclarationToTomTerm(tom_match1_1)) {
           Declaration  tom_match1_1_1 = null;
          tom_match1_1_1 = ( Declaration ) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match1_1);
          t = ( Declaration ) tom_match1_1_1;
          
        generateDeclaration(out,deep,t);
        return;
      
        }
}
matchlab_match1_pattern35: {
         Expression  t = null;
        if(tom_is_fun_sym_ExpressionToTomTerm(tom_match1_1)) {
           Expression  tom_match1_1_1 = null;
          tom_match1_1_1 = ( Expression ) tom_get_slot_ExpressionToTomTerm_astExpression(tom_match1_1);
          t = ( Expression ) tom_match1_1_1;
          
        generateExpression(out,deep,t);
        return;
      
        }
}
matchlab_match1_pattern36: {
         TomTerm  t = null;
        t = ( TomTerm ) tom_match1_1;
        
        System.out.println("Cannot generate code for: " + t);
        System.exit(1);
      
}
    }
    
  }

  public void generateExpression(jtom.tools.OutputCode out, int deep, Expression subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    
    {
       Expression  tom_match4_1 = null;
      tom_match4_1 = ( Expression ) subject;
matchlab_match4_pattern1: {
         Expression  exp = null;
        if(tom_is_fun_sym_Not(tom_match4_1)) {
           Expression  tom_match4_1_1 = null;
          tom_match4_1_1 = ( Expression ) tom_get_slot_Not_arg(tom_match4_1);
          exp = ( Expression ) tom_match4_1_1;
          
        if(Flags.cCode || Flags.jCode) {
          out.write("!(");
          generateExpression(out,deep,exp);
          out.write(")");
        } else if(Flags.eCode) {
          out.write("not ");
          generateExpression(out,deep,exp);
        }
        return;
      
        }
}
matchlab_match4_pattern2: {
         Expression  exp1 = null;
         Expression  exp2 = null;
        if(tom_is_fun_sym_And(tom_match4_1)) {
           Expression  tom_match4_1_1 = null;
           Expression  tom_match4_1_2 = null;
          tom_match4_1_1 = ( Expression ) tom_get_slot_And_arg1(tom_match4_1);
          tom_match4_1_2 = ( Expression ) tom_get_slot_And_arg2(tom_match4_1);
          exp1 = ( Expression ) tom_match4_1_1;
          exp2 = ( Expression ) tom_match4_1_2;
          
        generateExpression(out,deep,exp1);
        out.write(" && ");
        generateExpression(out,deep,exp2);
        return;
      
        }
}
matchlab_match4_pattern3: {
        if(tom_is_fun_sym_TrueGL(tom_match4_1)) {
          
        if(Flags.cCode) {
          out.write(" 1 ");
        } else if(Flags.jCode) {
          out.write(" true ");
        } else if(Flags.eCode) {
          out.write(" true ");
        }
        return;
      
        }
}
matchlab_match4_pattern4: {
        if(tom_is_fun_sym_FalseGL(tom_match4_1)) {
          
        if(Flags.cCode) {
          out.write(" 0 ");
        } else if(Flags.jCode) {
          out.write(" false ");
        } else if(Flags.eCode) {
          out.write(" false ");
        }
        return;
      
        }
}
matchlab_match4_pattern5: {
         TomTerm  var = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_IsEmptyList(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_IsEmptyList_kid1(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var = ( TomTerm ) tom_match4_1_1;
            type1 = ( TomType ) tom_match4_1_1_3;
            
        out.write("tom_is_empty_" + getTomType(type1) + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      
          }
        }
}
matchlab_match4_pattern6: {
         TomTerm  varIndex = null;
         TomType  type1 = null;
         TomTerm  varArray = null;
        if(tom_is_fun_sym_IsEmptyArray(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_IsEmptyArray_kid1(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_IsEmptyArray_kid2(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            varArray = ( TomTerm ) tom_match4_1_1;
            type1 = ( TomType ) tom_match4_1_1_3;
            if(tom_is_fun_sym_Variable(tom_match4_1_2)) {
               Option  tom_match4_1_2_1 = null;
               TomName  tom_match4_1_2_2 = null;
               TomType  tom_match4_1_2_3 = null;
              tom_match4_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_2);
              tom_match4_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_2);
              tom_match4_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_2);
              varIndex = ( TomTerm ) tom_match4_1_2;
              
        generate(out,deep,varIndex);
        out.write(" >= ");
        out.write("tom_get_size_" + getTomType(type1) + "(");
        generate(out,deep,varArray);
        out.write(")");
        return;
      
            }
          }
        }
}
matchlab_match4_pattern7: {
         TomTerm  var = null;
         Option  option = null;
         TomType  type1 = null;
         String  tomName = null;
         TomList  l = null;
        if(tom_is_fun_sym_EqualFunctionSymbol(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_EqualFunctionSymbol_kid1(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_EqualFunctionSymbol_kid2(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var = ( TomTerm ) tom_match4_1_1;
            type1 = ( TomType ) tom_match4_1_1_3;
            if(tom_is_fun_sym_Appl(tom_match4_1_2)) {
               Option  tom_match4_1_2_1 = null;
               TomName  tom_match4_1_2_2 = null;
               TomList  tom_match4_1_2_3 = null;
              tom_match4_1_2_1 = ( Option ) tom_get_slot_Appl_option(tom_match4_1_2);
              tom_match4_1_2_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match4_1_2);
              tom_match4_1_2_3 = ( TomList ) tom_get_slot_Appl_args(tom_match4_1_2);
              option = ( Option ) tom_match4_1_2_1;
              if(tom_is_fun_sym_Name(tom_match4_1_2_2)) {
                 String  tom_match4_1_2_2_1 = null;
                tom_match4_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match4_1_2_2);
                tomName = ( String ) tom_match4_1_2_2_1;
                l = ( TomList ) tom_match4_1_2_3;
                
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomName termNameAST = tomSymbol.getAstName();
        OptionList termOptionList = tomSymbol.getOption().getOptionList();
        
        Declaration isFsymDecl = getIsFsymDecl(termOptionList);
        if(isFsymDecl != null) {
          generateExpression(out,deep,
tom_make_IsFsym(termNameAST,var)                );
        } else {
          String s = (String)getFunSymMap.get(type1);
          if(s == null) {
            s = "tom_cmp_fun_sym_" + getTomType(type1) + "(tom_get_fun_sym_" + getTomType(type1) + "(";
            getFunSymMap.put(type1,s);
          }
          out.write(s);
          generate(out,deep,var);
          out.write(") , " + getSymbolCode(tomSymbol) + ")");
        }
        return;
      
              }
            }
          }
        }
}
matchlab_match4_pattern8: {
         TomType  type1 = null;
         TomTerm  var2 = null;
         TomTerm  var1 = null;
        if(tom_is_fun_sym_EqualFunctionSymbol(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_EqualFunctionSymbol_kid1(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_EqualFunctionSymbol_kid2(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var1 = ( TomTerm ) tom_match4_1_1;
            type1 = ( TomType ) tom_match4_1_1_3;
            var2 = ( TomTerm ) tom_match4_1_2;
            
          //System.out.println("EqualFunctionSymbol(...," + var2 + ")");
        out.write("tom_cmp_fun_sym_" + getTomType(type1) + "(");
        out.write("tom_get_fun_sym_" + getTomType(type1) + "(");
        generate(out,deep,var1);
        out.write(") , " + var2 + ")");
        return;
      
          }
        }
}
matchlab_match4_pattern9: {
         TomTerm  var1 = null;
         TomTerm  var2 = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_EqualTerm(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_EqualTerm_kid1(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_EqualTerm_kid2(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var1 = ( TomTerm ) tom_match4_1_1;
            type1 = ( TomType ) tom_match4_1_1_3;
            var2 = ( TomTerm ) tom_match4_1_2;
            
        out.write("tom_terms_equal_" + getTomType(type1) + "(");
        generate(out,deep,var1);
        out.write(", ");
        generate(out,deep,var2);
        out.write(")");
        return;
      
          }
        }
}
matchlab_match4_pattern10: {
         TomTerm  var2 = null;
         TomType  type1 = null;
         TomTerm  var1 = null;
        if(tom_is_fun_sym_EqualTerm(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_EqualTerm_kid1(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_EqualTerm_kid2(tom_match4_1);
          if(tom_is_fun_sym_VariableStar(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match4_1_1);
            var1 = ( TomTerm ) tom_match4_1_1;
            type1 = ( TomType ) tom_match4_1_1_3;
            var2 = ( TomTerm ) tom_match4_1_2;
            
        out.write("tom_terms_equal_" + getTomType(type1) + "(");
        generate(out,deep,var1);
        out.write(", ");
        generate(out,deep,var2);
        out.write(")");
        return;
      
          }
        }
}
matchlab_match4_pattern11: {
         TomTerm  var = null;
         Option  option1 = null;
         TomType  type1 = null;
         TomList  l1 = null;
         String  opname = null;
        if(tom_is_fun_sym_IsFsym(tom_match4_1)) {
           TomName  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomName ) tom_get_slot_IsFsym_astName(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_IsFsym_term(tom_match4_1);
          if(tom_is_fun_sym_Name(tom_match4_1_1)) {
             String  tom_match4_1_1_1 = null;
            tom_match4_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match4_1_1);
            opname = ( String ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match4_1_2)) {
               Option  tom_match4_1_2_1 = null;
               TomName  tom_match4_1_2_2 = null;
               TomType  tom_match4_1_2_3 = null;
              tom_match4_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_2);
              tom_match4_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_2);
              tom_match4_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_2);
              var = ( TomTerm ) tom_match4_1_2;
              option1 = ( Option ) tom_match4_1_2_1;
              if(tom_is_fun_sym_Position(tom_match4_1_2_2)) {
                 TomList  tom_match4_1_2_2_1 = null;
                tom_match4_1_2_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_2_2);
                l1 = ( TomList ) tom_match4_1_2_2_1;
                type1 = ( TomType ) tom_match4_1_2_3;
                
        String s = (String)isFsymMap.get(opname);
        if(s == null) {
          s = "tom_is_fun_sym_" + opname + "(";
          isFsymMap.put(opname,s);
        } 
        out.write(s);
        generate(out,deep,var);
        out.write(")");
        return;
      
              }
            }
          }
        }
}
matchlab_match4_pattern12: {
         TomTerm  var = null;
         TomType  type1 = null;
         Integer  number = null;
         TomList  l1 = null;
         Option  option1 = null;
        if(tom_is_fun_sym_GetSubterm(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_GetSubterm_term(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_GetSubterm_number(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var = ( TomTerm ) tom_match4_1_1;
            option1 = ( Option ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Position(tom_match4_1_1_2)) {
               TomList  tom_match4_1_1_2_1 = null;
              tom_match4_1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_1_2);
              l1 = ( TomList ) tom_match4_1_1_2_1;
              type1 = ( TomType ) tom_match4_1_1_3;
              if(tom_is_fun_sym_Number(tom_match4_1_2)) {
                 Integer  tom_match4_1_2_1 = null;
                tom_match4_1_2_1 = ( Integer ) tom_get_slot_Number_integer(tom_match4_1_2);
                number = ( Integer ) tom_match4_1_2_1;
                
        String s = (String)getSubtermMap.get(type1);
        if(s == null) {
          s = "tom_get_subterm_" + getTomType(type1) + "(";
          getSubtermMap.put(type1,s);
        } 
        out.write(s);
        generate(out,deep,var);
        out.write(", " + number + ")");
        return;
      
              }
            }
          }
        }
}
matchlab_match4_pattern13: {
         TomTerm  var = null;
         String  slotName = null;
         TomList  l1 = null;
         Option  option1 = null;
         String  opname = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_GetSlot(tom_match4_1)) {
           TomName  tom_match4_1_1 = null;
           String  tom_match4_1_2 = null;
           TomTerm  tom_match4_1_3 = null;
          tom_match4_1_1 = ( TomName ) tom_get_slot_GetSlot_astName(tom_match4_1);
          tom_match4_1_2 = ( String ) tom_get_slot_GetSlot_slotNameString(tom_match4_1);
          tom_match4_1_3 = ( TomTerm ) tom_get_slot_GetSlot_term(tom_match4_1);
          if(tom_is_fun_sym_Name(tom_match4_1_1)) {
             String  tom_match4_1_1_1 = null;
            tom_match4_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match4_1_1);
            opname = ( String ) tom_match4_1_1_1;
            slotName = ( String ) tom_match4_1_2;
            if(tom_is_fun_sym_Variable(tom_match4_1_3)) {
               Option  tom_match4_1_3_1 = null;
               TomName  tom_match4_1_3_2 = null;
               TomType  tom_match4_1_3_3 = null;
              tom_match4_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_3);
              tom_match4_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_3);
              tom_match4_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_3);
              var = ( TomTerm ) tom_match4_1_3;
              option1 = ( Option ) tom_match4_1_3_1;
              if(tom_is_fun_sym_Position(tom_match4_1_3_2)) {
                 TomList  tom_match4_1_3_2_1 = null;
                tom_match4_1_3_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_3_2);
                l1 = ( TomList ) tom_match4_1_3_2_1;
                type1 = ( TomType ) tom_match4_1_3_3;
                
        out.write("tom_get_slot_" + opname + "_" + slotName + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      
              }
            }
          }
        }
}
matchlab_match4_pattern14: {
         TomTerm  var = null;
         TomList  l1 = null;
         TomType  type1 = null;
         Option  option1 = null;
        if(tom_is_fun_sym_GetHead(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_GetHead_kid1(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var = ( TomTerm ) tom_match4_1_1;
            option1 = ( Option ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Position(tom_match4_1_1_2)) {
               TomList  tom_match4_1_1_2_1 = null;
              tom_match4_1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_1_2);
              l1 = ( TomList ) tom_match4_1_1_2_1;
              type1 = ( TomType ) tom_match4_1_1_3;
              
        out.write("tom_get_head_" + getTomType(type1) + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      
            }
          }
        }
}
matchlab_match4_pattern15: {
         TomTerm  var = null;
         TomList  l1 = null;
         TomType  type1 = null;
         Option  option1 = null;
        if(tom_is_fun_sym_GetTail(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_GetTail_kid1(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var = ( TomTerm ) tom_match4_1_1;
            option1 = ( Option ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Position(tom_match4_1_1_2)) {
               TomList  tom_match4_1_1_2_1 = null;
              tom_match4_1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_1_2);
              l1 = ( TomList ) tom_match4_1_1_2_1;
              type1 = ( TomType ) tom_match4_1_1_3;
              
        out.write("tom_get_tail_" + getTomType(type1) + "(");
        generate(out,deep,var); 
        out.write(")");
        return;
      
            }
          }
        }
}
matchlab_match4_pattern16: {
         TomTerm  var = null;
         Option  option1 = null;
         TomList  l1 = null;
         TomType  type1 = null;
        if(tom_is_fun_sym_GetSize(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_GetSize_kid1(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            var = ( TomTerm ) tom_match4_1_1;
            option1 = ( Option ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Position(tom_match4_1_1_2)) {
               TomList  tom_match4_1_1_2_1 = null;
              tom_match4_1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_1_2);
              l1 = ( TomList ) tom_match4_1_1_2_1;
              type1 = ( TomType ) tom_match4_1_1_3;
              
        out.write("tom_get_size_" + getTomType(type1) + "(");
        generate(out,deep,var);
        out.write(")");
        return;
      
            }
          }
        }
}
matchlab_match4_pattern17: {
         TomTerm  varIndex = null;
         TomTerm  varName = null;
         TomType  type1 = null;
         TomList  l1 = null;
         Option  option2 = null;
         Option  option1 = null;
         TomType  type2 = null;
         TomList  l2 = null;
        if(tom_is_fun_sym_GetElement(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_GetElement_kid1(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_GetElement_kid2(tom_match4_1);
          if(tom_is_fun_sym_Variable(tom_match4_1_1)) {
             Option  tom_match4_1_1_1 = null;
             TomName  tom_match4_1_1_2 = null;
             TomType  tom_match4_1_1_3 = null;
            tom_match4_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_1);
            tom_match4_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_1);
            tom_match4_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_1);
            varName = ( TomTerm ) tom_match4_1_1;
            option1 = ( Option ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Position(tom_match4_1_1_2)) {
               TomList  tom_match4_1_1_2_1 = null;
              tom_match4_1_1_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_1_2);
              l1 = ( TomList ) tom_match4_1_1_2_1;
              type1 = ( TomType ) tom_match4_1_1_3;
              if(tom_is_fun_sym_Variable(tom_match4_1_2)) {
                 Option  tom_match4_1_2_1 = null;
                 TomName  tom_match4_1_2_2 = null;
                 TomType  tom_match4_1_2_3 = null;
                tom_match4_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_2);
                tom_match4_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_2);
                tom_match4_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_2);
                varIndex = ( TomTerm ) tom_match4_1_2;
                option2 = ( Option ) tom_match4_1_2_1;
                if(tom_is_fun_sym_Position(tom_match4_1_2_2)) {
                   TomList  tom_match4_1_2_2_1 = null;
                  tom_match4_1_2_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_2_2);
                  l2 = ( TomList ) tom_match4_1_2_2_1;
                  type2 = ( TomType ) tom_match4_1_2_3;
                  
        out.write("tom_get_element_" + getTomType(type1) + "(");
        generate(out,deep,varName);
        out.write(",");
        generate(out,deep,varIndex);
        out.write(")");
        return;
      
                }
              }
            }
          }
        }
}
matchlab_match4_pattern18: {
         String  name = null;
         TomTerm  varBegin = null;
         TomType  type1 = null;
         TomTerm  varEnd = null;
         Option  option1 = null;
         Option  option2 = null;
         TomType  type2 = null;
         TomList  l2 = null;
         TomList  l1 = null;
        if(tom_is_fun_sym_GetSliceList(tom_match4_1)) {
           TomName  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
           TomTerm  tom_match4_1_3 = null;
          tom_match4_1_1 = ( TomName ) tom_get_slot_GetSliceList_astName(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_GetSliceList_variableBeginAST(tom_match4_1);
          tom_match4_1_3 = ( TomTerm ) tom_get_slot_GetSliceList_variableEndAST(tom_match4_1);
          if(tom_is_fun_sym_Name(tom_match4_1_1)) {
             String  tom_match4_1_1_1 = null;
            tom_match4_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match4_1_1);
            name = ( String ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match4_1_2)) {
               Option  tom_match4_1_2_1 = null;
               TomName  tom_match4_1_2_2 = null;
               TomType  tom_match4_1_2_3 = null;
              tom_match4_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_2);
              tom_match4_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_2);
              tom_match4_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_2);
              varBegin = ( TomTerm ) tom_match4_1_2;
              option1 = ( Option ) tom_match4_1_2_1;
              if(tom_is_fun_sym_Position(tom_match4_1_2_2)) {
                 TomList  tom_match4_1_2_2_1 = null;
                tom_match4_1_2_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_2_2);
                l1 = ( TomList ) tom_match4_1_2_2_1;
                type1 = ( TomType ) tom_match4_1_2_3;
                if(tom_is_fun_sym_Variable(tom_match4_1_3)) {
                   Option  tom_match4_1_3_1 = null;
                   TomName  tom_match4_1_3_2 = null;
                   TomType  tom_match4_1_3_3 = null;
                  tom_match4_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_3);
                  tom_match4_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_3);
                  tom_match4_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_3);
                  varEnd = ( TomTerm ) tom_match4_1_3;
                  option2 = ( Option ) tom_match4_1_3_1;
                  if(tom_is_fun_sym_Position(tom_match4_1_3_2)) {
                     TomList  tom_match4_1_3_2_1 = null;
                    tom_match4_1_3_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_3_2);
                    l2 = ( TomList ) tom_match4_1_3_2_1;
                    type2 = ( TomType ) tom_match4_1_3_3;
                    
        
        out.write("tom_get_slice_" + name + "(");
        generate(out,deep,varBegin);
        out.write(",");
        generate(out,deep,varEnd);
        out.write(")");
        return;
      
                  }
                }
              }
            }
          }
        }
}
matchlab_match4_pattern19: {
         TomTerm  varBegin = null;
         String  name = null;
         TomTerm  expEnd = null;
         Option  option1 = null;
         TomType  type2 = null;
         TomList  l2 = null;
         TomTerm  varArray = null;
         TomType  type1 = null;
         Option  option2 = null;
         TomList  l1 = null;
        if(tom_is_fun_sym_GetSliceArray(tom_match4_1)) {
           TomName  tom_match4_1_1 = null;
           TomTerm  tom_match4_1_2 = null;
           TomTerm  tom_match4_1_3 = null;
           TomTerm  tom_match4_1_4 = null;
          tom_match4_1_1 = ( TomName ) tom_get_slot_GetSliceArray_astName(tom_match4_1);
          tom_match4_1_2 = ( TomTerm ) tom_get_slot_GetSliceArray_subjectListName(tom_match4_1);
          tom_match4_1_3 = ( TomTerm ) tom_get_slot_GetSliceArray_variableBeginAST(tom_match4_1);
          tom_match4_1_4 = ( TomTerm ) tom_get_slot_GetSliceArray_variableEndAST(tom_match4_1);
          if(tom_is_fun_sym_Name(tom_match4_1_1)) {
             String  tom_match4_1_1_1 = null;
            tom_match4_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match4_1_1);
            name = ( String ) tom_match4_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match4_1_2)) {
               Option  tom_match4_1_2_1 = null;
               TomName  tom_match4_1_2_2 = null;
               TomType  tom_match4_1_2_3 = null;
              tom_match4_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_2);
              tom_match4_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_2);
              tom_match4_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_2);
              varArray = ( TomTerm ) tom_match4_1_2;
              option1 = ( Option ) tom_match4_1_2_1;
              if(tom_is_fun_sym_Position(tom_match4_1_2_2)) {
                 TomList  tom_match4_1_2_2_1 = null;
                tom_match4_1_2_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_2_2);
                l1 = ( TomList ) tom_match4_1_2_2_1;
                type1 = ( TomType ) tom_match4_1_2_3;
                if(tom_is_fun_sym_Variable(tom_match4_1_3)) {
                   Option  tom_match4_1_3_1 = null;
                   TomName  tom_match4_1_3_2 = null;
                   TomType  tom_match4_1_3_3 = null;
                  tom_match4_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match4_1_3);
                  tom_match4_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match4_1_3);
                  tom_match4_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match4_1_3);
                  varBegin = ( TomTerm ) tom_match4_1_3;
                  option2 = ( Option ) tom_match4_1_3_1;
                  if(tom_is_fun_sym_Position(tom_match4_1_3_2)) {
                     TomList  tom_match4_1_3_2_1 = null;
                    tom_match4_1_3_2_1 = ( TomList ) tom_get_slot_Position_numberList(tom_match4_1_3_2);
                    l2 = ( TomList ) tom_match4_1_3_2_1;
                    type2 = ( TomType ) tom_match4_1_3_3;
                    expEnd = ( TomTerm ) tom_match4_1_4;
                    
        
        out.write("tom_get_slice_" + name + "(");
        generate(out,deep,varArray);
        out.write(",");
        generate(out,deep,varBegin);
        out.write(",");
        generate(out,deep,expEnd);
        out.write(")");
        return;
      
                  }
                }
              }
            }
          }
        }
}
matchlab_match4_pattern20: {
         TomTerm  t = null;
        if(tom_is_fun_sym_TomTermToExpression(tom_match4_1)) {
           TomTerm  tom_match4_1_1 = null;
          tom_match4_1_1 = ( TomTerm ) tom_get_slot_TomTermToExpression_astTerm(tom_match4_1);
          t = ( TomTerm ) tom_match4_1_1;
          
        generate(out,deep,t);
        return;
      
        }
}
matchlab_match4_pattern21: {
         Expression  t = null;
        t = ( Expression ) tom_match4_1;
        
        System.out.println("Cannot generate code for expression: " + t);
        System.exit(1);
      
}
    }
    
  }

  public void generateTargetLanguage(jtom.tools.OutputCode out, int deep, TargetLanguage subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    
    {
       TargetLanguage  tom_match5_1 = null;
      tom_match5_1 = ( TargetLanguage ) subject;
matchlab_match5_pattern1: {
         String  t = null;
        if(tom_is_fun_sym_TL(tom_match5_1)) {
           String  tom_match5_1_1 = null;
          tom_match5_1_1 = ( String ) tom_get_slot_TL_code(tom_match5_1);
          t = ( String ) tom_match5_1_1;
          
        statistics().numberPartsCopied++;
        out.writeln(deep,t);
        return;
      
        }
}
matchlab_match5_pattern2: {
         TargetLanguage  t = null;
        t = ( TargetLanguage ) tom_match5_1;
        
        System.out.println("Cannot generate code for: " + t);
        System.exit(1);
      
}
    }
    
  }

  public void generateOption(jtom.tools.OutputCode out, int deep, Option subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    
    {
       Option  tom_match6_1 = null;
      tom_match6_1 = ( Option ) subject;
matchlab_match6_pattern1: {
         Declaration  decl = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match6_1)) {
           Declaration  tom_match6_1_1 = null;
          tom_match6_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match6_1);
          decl = ( Declaration ) tom_match6_1_1;
          
        generateDeclaration(out,deep,decl);
        return;
      
        }
}
matchlab_match6_pattern2: {
        if(tom_is_fun_sym_OriginTracking(tom_match6_1)) {
           TomName  tom_match6_1_1 = null;
           TomTerm  tom_match6_1_2 = null;
          tom_match6_1_1 = ( TomName ) tom_get_slot_OriginTracking_astName(tom_match6_1);
          tom_match6_1_2 = ( TomTerm ) tom_get_slot_OriginTracking_line(tom_match6_1);
           return; 
        }
}
matchlab_match6_pattern3: {
        if(tom_is_fun_sym_DefinedSymbol(tom_match6_1)) {
           return; 
        }
}
matchlab_match6_pattern4: {
        if(tom_is_fun_sym_SlotList(tom_match6_1)) {
           TomList  tom_match6_1_1 = null;
          tom_match6_1_1 = ( TomList ) tom_get_slot_SlotList_list(tom_match6_1);
           return; 
        }
}
matchlab_match6_pattern5: {
        if(tom_is_fun_sym_LRParen(tom_match6_1)) {
           TomName  tom_match6_1_1 = null;
          tom_match6_1_1 = ( TomName ) tom_get_slot_LRParen_astName(tom_match6_1);
           return; 
        }
}
matchlab_match6_pattern6: {
         Option  t = null;
        t = ( Option ) tom_match6_1;
        
        System.out.println("Cannot generate code for option: " + t);
        System.exit(1);
      
}
    }
    
  }
  
  public void generateDeclaration(jtom.tools.OutputCode out, int deep, Declaration subject)
    throws IOException {
    if(subject==null) { return; }
    
    statistics().numberPartsGoalLanguage++;
    
    {
       Declaration  tom_match7_1 = null;
      tom_match7_1 = ( Declaration ) subject;
matchlab_match7_pattern1: {
         String  tomName = null;
        if(tom_is_fun_sym_SymbolDecl(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_SymbolDecl_astName(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            tomName = ( String ) tom_match7_1_1_1;
            
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        OptionList optionList = tomSymbol.getOption().getOptionList();
        TomList l = tomSymbol.getTypesToType().getList();
        TomType type1 = tomSymbol.getTypesToType().getCodomain();
        String name1 = tomSymbol.getAstName().getString();
          
        if(Flags.cCode) {
            // TODO: build an abstract declaration
          int argno=1;
            /*
              String s = "";
          if(!l.isEmpty()) {
            s = getTLType(type1) + " " + name1;
            
            if(!l.isEmpty()) {
              s += "(";
              while (!l.isEmpty()) {
                s += getTLType(l.getHead()) + " _" + argno;
                argno++;
                l = l.getTail() ;
                if(!l.isEmpty()) {
                  s += ",";
                }
              }
              s += ");";
            }
          }
            generate(out,deep,makeTL(s));
            */

          out.indent(deep);
          if(!l.isEmpty()) {
            out.write(getTLType(type1));
            out.writeSpace();
            out.write(name1);
            if(!l.isEmpty()) {
              out.writeOpenBrace();
              while (!l.isEmpty()) {
                out.write(getTLType(l.getHead().getAstType()));
                out.writeUnderscore();
                out.write(argno);
                argno++;
                l = l.getTail() ;
                if(!l.isEmpty()) {
                  out.writeComa();
                }
              }
              out.writeCloseBrace();
              out.writeSemiColon();
            }
          }
          out.writeln();
        } else if(Flags.jCode) {
            // do nothing
        } else if(Flags.eCode) {
            // do nothing
        }

          // inspect the optionList
        generateOptionList(out, deep, optionList);
        return ;
      
          }
        }
}
matchlab_match7_pattern2: {
         String  name = null;
         String  glType = null;
         Option  option = null;
         String  type = null;
         String  tlCode = null;
        if(tom_is_fun_sym_GetFunctionSymbolDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_termArg(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      glType = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_TLCode(tom_match7_1_2)) {
                         TargetLanguage  tom_match7_1_2_1 = null;
                        tom_match7_1_2_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_2);
                        if(tom_is_fun_sym_TL(tom_match7_1_2_1)) {
                           String  tom_match7_1_2_1_1 = null;
                          tom_match7_1_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_1);
                          tlCode = ( String ) tom_match7_1_2_1_1;
                          
        String args[];
        if(!Flags.strictType) {
          args = new String[] { getTLType(getUniversalType()), name };
        } else {
          args = new String[] { glType, name };
        }

        TomType returnType = isIntType(type)?getIntType():getUniversalType();
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(returnType),
                                       "tom_get_fun_sym", type,args,tlCode));
        return;
      
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern3: {
         Option  option1 = null;
         String  name1 = null;
         String  tlCode = null;
         Option  option2 = null;
         String  name2 = null;
         String  glType1 = null;
         String  type1 = null;
         String  type2 = null;
         String  glType2 = null;
        if(tom_is_fun_sym_GetSubtermDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_GetSubtermDecl_termArg(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetSubtermDecl_numberArg(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_GetSubtermDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type1 = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      glType1 = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
                         Option  tom_match7_1_2_1 = null;
                         TomName  tom_match7_1_2_2 = null;
                         TomType  tom_match7_1_2_3 = null;
                        tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
                        tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
                        tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
                        option2 = ( Option ) tom_match7_1_2_1;
                        if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                           String  tom_match7_1_2_2_1 = null;
                          tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                          name2 = ( String ) tom_match7_1_2_2_1;
                          if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                             TomType  tom_match7_1_2_3_1 = null;
                             TomType  tom_match7_1_2_3_2 = null;
                            tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                            tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                            if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                               String  tom_match7_1_2_3_1_1 = null;
                              tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                              type2 = ( String ) tom_match7_1_2_3_1_1;
                              if(tom_is_fun_sym_TLType(tom_match7_1_2_3_2)) {
                                 TargetLanguage  tom_match7_1_2_3_2_1 = null;
                                tom_match7_1_2_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_2_3_2);
                                if(tom_is_fun_sym_TL(tom_match7_1_2_3_2_1)) {
                                   String  tom_match7_1_2_3_2_1_1 = null;
                                  tom_match7_1_2_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_3_2_1);
                                  glType2 = ( String ) tom_match7_1_2_3_2_1_1;
                                  if(tom_is_fun_sym_TLCode(tom_match7_1_3)) {
                                     TargetLanguage  tom_match7_1_3_1 = null;
                                    tom_match7_1_3_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_3);
                                    if(tom_is_fun_sym_TL(tom_match7_1_3_1)) {
                                       String  tom_match7_1_3_1_1 = null;
                                      tom_match7_1_3_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_1);
                                      tlCode = ( String ) tom_match7_1_3_1_1;
                                      
        String args[];
        if(Flags.strictType || Flags.eCode) {
          args = new String[] { glType1, name1,
                                glType2, name2 };
        } else {
          args = new String[] { getTLType(getUniversalType()), name1,
                                glType2, name2 };
        }
        generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_get_subterm", type1,
                                   args,
                                   tlCode));
        return;
      
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern4: {
         String  name1 = null;
         String  type1 = null;
         String  tomName = null;
         Option  option1 = null;
         String  glType1 = null;
         String  tlCode = null;
        if(tom_is_fun_sym_IsFsymDecl(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_IsFsymDecl_astName(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_IsFsymDecl_term(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_IsFsymDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            tomName = ( String ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
               Option  tom_match7_1_2_1 = null;
               TomName  tom_match7_1_2_2 = null;
               TomType  tom_match7_1_2_3 = null;
              tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
              tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
              tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
              option1 = ( Option ) tom_match7_1_2_1;
              if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                 String  tom_match7_1_2_2_1 = null;
                tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                name1 = ( String ) tom_match7_1_2_2_1;
                if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                   TomType  tom_match7_1_2_3_1 = null;
                   TomType  tom_match7_1_2_3_2 = null;
                  tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                  tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                  if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                     String  tom_match7_1_2_3_1_1 = null;
                    tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                    type1 = ( String ) tom_match7_1_2_3_1_1;
                    if(tom_is_fun_sym_TLType(tom_match7_1_2_3_2)) {
                       TargetLanguage  tom_match7_1_2_3_2_1 = null;
                      tom_match7_1_2_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_2_3_2);
                      if(tom_is_fun_sym_TL(tom_match7_1_2_3_2_1)) {
                         String  tom_match7_1_2_3_2_1_1 = null;
                        tom_match7_1_2_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_3_2_1);
                        glType1 = ( String ) tom_match7_1_2_3_2_1_1;
                        if(tom_is_fun_sym_TLCode(tom_match7_1_3)) {
                           TargetLanguage  tom_match7_1_3_1 = null;
                          tom_match7_1_3_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_3);
                          if(tom_is_fun_sym_TL(tom_match7_1_3_1)) {
                             String  tom_match7_1_3_1_1 = null;
                            tom_match7_1_3_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_1);
                            tlCode = ( String ) tom_match7_1_3_1_1;
                            
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String opname = tomSymbol.getAstName().getString();

	  TomType returnType = getBoolType();
	  String argType;
	  if(Flags.strictType) {
	      argType = glType1;
	  } else {
	      argType = getTLType(getUniversalType());
	  }

	  generateTargetLanguage(out,deep, genDecl(getTLType(returnType),
				     "tom_is_fun_sym", opname,
				     new String[] { argType, name1 },
				     tlCode));
	  return;
      
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern5: {
         String  type1 = null;
         String  name1 = null;
         Option  option1 = null;
         String  glType1 = null;
         String  slotName = null;
         String  tlCode = null;
         String  tomName = null;
        if(tom_is_fun_sym_GetSlotDecl(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
           TomTerm  tom_match7_1_4 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_GetSlotDecl_astName(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetSlotDecl_slotName(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_GetSlotDecl_term(tom_match7_1);
          tom_match7_1_4 = ( TomTerm ) tom_get_slot_GetSlotDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            tomName = ( String ) tom_match7_1_1_1;
            if(tom_is_fun_sym_SlotName(tom_match7_1_2)) {
               String  tom_match7_1_2_1 = null;
              tom_match7_1_2_1 = ( String ) tom_get_slot_SlotName_string(tom_match7_1_2);
              slotName = ( String ) tom_match7_1_2_1;
              if(tom_is_fun_sym_Variable(tom_match7_1_3)) {
                 Option  tom_match7_1_3_1 = null;
                 TomName  tom_match7_1_3_2 = null;
                 TomType  tom_match7_1_3_3 = null;
                tom_match7_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_3);
                tom_match7_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_3);
                tom_match7_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_3);
                option1 = ( Option ) tom_match7_1_3_1;
                if(tom_is_fun_sym_Name(tom_match7_1_3_2)) {
                   String  tom_match7_1_3_2_1 = null;
                  tom_match7_1_3_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_3_2);
                  name1 = ( String ) tom_match7_1_3_2_1;
                  if(tom_is_fun_sym_Type(tom_match7_1_3_3)) {
                     TomType  tom_match7_1_3_3_1 = null;
                     TomType  tom_match7_1_3_3_2 = null;
                    tom_match7_1_3_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_3_3);
                    tom_match7_1_3_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_3_3);
                    if(tom_is_fun_sym_TomType(tom_match7_1_3_3_1)) {
                       String  tom_match7_1_3_3_1_1 = null;
                      tom_match7_1_3_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_3_3_1);
                      type1 = ( String ) tom_match7_1_3_3_1_1;
                      if(tom_is_fun_sym_TLType(tom_match7_1_3_3_2)) {
                         TargetLanguage  tom_match7_1_3_3_2_1 = null;
                        tom_match7_1_3_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_3_3_2);
                        if(tom_is_fun_sym_TL(tom_match7_1_3_3_2_1)) {
                           String  tom_match7_1_3_3_2_1_1 = null;
                          tom_match7_1_3_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_3_2_1);
                          glType1 = ( String ) tom_match7_1_3_3_2_1_1;
                          if(tom_is_fun_sym_TLCode(tom_match7_1_4)) {
                             TargetLanguage  tom_match7_1_4_1 = null;
                            tom_match7_1_4_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_4);
                            if(tom_is_fun_sym_TL(tom_match7_1_4_1)) {
                               String  tom_match7_1_4_1_1 = null;
                              tom_match7_1_4_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_4_1);
                              tlCode = ( String ) tom_match7_1_4_1_1;
                              
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        String opname = tomSymbol.getAstName().getString();
        TomList typesList = tomSymbol.getTypesToType().getList();
        OptionList optionList = tomSymbol.getOption().getOptionList();
        
        int slotIndex = getSlotIndex(optionList,slotName);
        TomList l = typesList;
        for(int index = 0; !l.isEmpty() && index<slotIndex ; index++) {
          l = l.getTail();
        }
        TomType returnType = l.getHead().getAstType();
        
	String argType;
	  if(Flags.strictType) {
	      argType = glType1;
	  } else {
	      argType = getTLType(getUniversalType());
	  }
        generateTargetLanguage(out,deep, genDecl(getTLType(returnType),
                                   "tom_get_slot", opname  + "_" + slotName,
                                   new String[] { argType, name1 },
                                   tlCode));
        return;
      
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern6: {
         String  tlCode = null;
         Option  option1 = null;
         String  name2 = null;
         Option  option2 = null;
         String  name1 = null;
         String  type2 = null;
         String  type1 = null;
        if(tom_is_fun_sym_CompareFunctionSymbolDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type1 = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
                     Option  tom_match7_1_2_1 = null;
                     TomName  tom_match7_1_2_2 = null;
                     TomType  tom_match7_1_2_3 = null;
                    tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
                    tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
                    tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
                    option2 = ( Option ) tom_match7_1_2_1;
                    if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                       String  tom_match7_1_2_2_1 = null;
                      tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                      name2 = ( String ) tom_match7_1_2_2_1;
                      if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                         TomType  tom_match7_1_2_3_1 = null;
                         TomType  tom_match7_1_2_3_2 = null;
                        tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                        tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                        if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                           String  tom_match7_1_2_3_1_1 = null;
                          tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                          type2 = ( String ) tom_match7_1_2_3_1_1;
                          if(tom_is_fun_sym_TLCode(tom_match7_1_3)) {
                             TargetLanguage  tom_match7_1_3_1 = null;
                            tom_match7_1_3_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_3);
                            if(tom_is_fun_sym_TL(tom_match7_1_3_1)) {
                               String  tom_match7_1_3_1_1 = null;
                              tom_match7_1_3_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_1);
                              tlCode = ( String ) tom_match7_1_3_1_1;
                              
        TomType argType1 = (isIntType(type1))?getIntType():getUniversalType();
        TomType argType2 = (isIntType(type2))?getIntType():getUniversalType();
        generateTargetLanguage(out,deep, genDecl(getTLType(getBoolType()), "tom_cmp_fun_sym", type1,
                                   new String[] {
                                     getTLType(argType1), name1,
                                     getTLType(argType2), name2
                                   },
                                   tlCode));
        return;
      
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern7: {
         Option  option2 = null;
         String  tlCode = null;
         Option  option1 = null;
         String  name1 = null;
         String  type1 = null;
         String  type2 = null;
         String  name2 = null;
        if(tom_is_fun_sym_TermsEqualDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg2(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_TermsEqualDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type1 = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
                     Option  tom_match7_1_2_1 = null;
                     TomName  tom_match7_1_2_2 = null;
                     TomType  tom_match7_1_2_3 = null;
                    tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
                    tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
                    tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
                    option2 = ( Option ) tom_match7_1_2_1;
                    if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                       String  tom_match7_1_2_2_1 = null;
                      tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                      name2 = ( String ) tom_match7_1_2_2_1;
                      if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                         TomType  tom_match7_1_2_3_1 = null;
                         TomType  tom_match7_1_2_3_2 = null;
                        tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                        tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                        if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                           String  tom_match7_1_2_3_1_1 = null;
                          tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                          type2 = ( String ) tom_match7_1_2_3_1_1;
                          if(tom_is_fun_sym_TLCode(tom_match7_1_3)) {
                             TargetLanguage  tom_match7_1_3_1 = null;
                            tom_match7_1_3_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_3);
                            if(tom_is_fun_sym_TL(tom_match7_1_3_1)) {
                               String  tom_match7_1_3_1_1 = null;
                              tom_match7_1_3_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_1);
                              tlCode = ( String ) tom_match7_1_3_1_1;
                              
        TomType argType1 = (isIntType(type1))?getIntType():getUniversalType();
        TomType argType2 = (isIntType(type2))?getIntType():getUniversalType();

        generateTargetLanguage(out,deep, genDecl(getTLType(getBoolType()), "tom_terms_equal", type1,
                                   new String[] {
                                     getTLType(argType1), name1,
                                     getTLType(argType2), name2
                                   },
                                   tlCode));
        return;
      
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern8: {
         String  name1 = null;
         String  type = null;
         Option  option1 = null;
         String  tlType = null;
         String  tlCode = null;
        if(tom_is_fun_sym_GetHeadDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_GetHeadDecl_kid1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetHeadDecl_kid2(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      tlType = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_TLCode(tom_match7_1_2)) {
                         TargetLanguage  tom_match7_1_2_1 = null;
                        tom_match7_1_2_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_2);
                        if(tom_is_fun_sym_TL(tom_match7_1_2_1)) {
                           String  tom_match7_1_2_1_1 = null;
                          tom_match7_1_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_1);
                          tlCode = ( String ) tom_match7_1_2_1_1;
                          
        String argType;
        if(Flags.strictType) {
          argType = tlType;
        } else {
          argType = getTLType(getUniversalType());
        }

        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getUniversalType()),
                                       "tom_get_head", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern9: {
         String  tlCode = null;
         String  name1 = null;
         Option  option1 = null;
         String  type = null;
         String  tlType = null;
        if(tom_is_fun_sym_GetTailDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_GetTailDecl_kid1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetTailDecl_kid2(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      tlType = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_TLCode(tom_match7_1_2)) {
                         TargetLanguage  tom_match7_1_2_1 = null;
                        tom_match7_1_2_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_2);
                        if(tom_is_fun_sym_TL(tom_match7_1_2_1)) {
                           String  tom_match7_1_2_1_1 = null;
                          tom_match7_1_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_1);
                          tlCode = ( String ) tom_match7_1_2_1_1;
                          
        String returnType, argType;
        if(Flags.strictType) {
          returnType = tlType;
          argType = tlType;
        } else {
          returnType = getTLType(getUniversalType());
          argType = getTLType(getUniversalType());
        }
        
        generateTargetLanguage(out,deep,
                               genDecl(returnType, "tom_get_tail", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern10: {
         String  type = null;
         String  name1 = null;
         String  tlType = null;
         Option  option1 = null;
         String  tlCode = null;
        if(tom_is_fun_sym_IsEmptyDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_IsEmptyDecl_kid1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_IsEmptyDecl_kid2(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      tlType = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_TLCode(tom_match7_1_2)) {
                         TargetLanguage  tom_match7_1_2_1 = null;
                        tom_match7_1_2_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_2);
                        if(tom_is_fun_sym_TL(tom_match7_1_2_1)) {
                           String  tom_match7_1_2_1_1 = null;
                          tom_match7_1_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_1);
                          tlCode = ( String ) tom_match7_1_2_1_1;
                          
        String argType;
        if(Flags.strictType) {
          argType = tlType;
        } else {
          argType = getTLType(getUniversalType());
        }

        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getBoolType()),
                                       "tom_is_empty", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern11: {
         String  tlCode = null;
         String  opname = null;
        if(tom_is_fun_sym_MakeEmptyList(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_MakeEmptyList_astName(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_MakeEmptyList_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            opname = ( String ) tom_match7_1_1_1;
            if(tom_is_fun_sym_TLCode(tom_match7_1_2)) {
               TargetLanguage  tom_match7_1_2_1 = null;
              tom_match7_1_2_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_2);
              if(tom_is_fun_sym_TL(tom_match7_1_2_1)) {
                 String  tom_match7_1_2_1_1 = null;
                tom_match7_1_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_1);
                tlCode = ( String ) tom_match7_1_2_1_1;
                
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getUniversalType()),
                                       "tom_make_empty", opname,
                                       new String[] { },
                                       tlCode));
        return;
      
              }
            }
          }
        }
}
matchlab_match7_pattern12: {
         String  opname = null;
         String  name2 = null;
         Option  option1 = null;
         TomType  fullEltType = null;
         String  tlType1 = null;
         String  tlType2 = null;
         String  type2 = null;
         String  name1 = null;
         Option  option2 = null;
         String  type1 = null;
         TomType  fullListType = null;
         String  tlCode = null;
        if(tom_is_fun_sym_MakeAddList(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
           TomTerm  tom_match7_1_4 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_MakeAddList_astName(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_MakeAddList_varElt(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_MakeAddList_varList(tom_match7_1);
          tom_match7_1_4 = ( TomTerm ) tom_get_slot_MakeAddList_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            opname = ( String ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
               Option  tom_match7_1_2_1 = null;
               TomName  tom_match7_1_2_2 = null;
               TomType  tom_match7_1_2_3 = null;
              tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
              tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
              tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
              option1 = ( Option ) tom_match7_1_2_1;
              if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                 String  tom_match7_1_2_2_1 = null;
                tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                name1 = ( String ) tom_match7_1_2_2_1;
                if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                   TomType  tom_match7_1_2_3_1 = null;
                   TomType  tom_match7_1_2_3_2 = null;
                  tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                  tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                  fullEltType = ( TomType ) tom_match7_1_2_3;
                  if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                     String  tom_match7_1_2_3_1_1 = null;
                    tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                    type1 = ( String ) tom_match7_1_2_3_1_1;
                    if(tom_is_fun_sym_TLType(tom_match7_1_2_3_2)) {
                       TargetLanguage  tom_match7_1_2_3_2_1 = null;
                      tom_match7_1_2_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_2_3_2);
                      if(tom_is_fun_sym_TL(tom_match7_1_2_3_2_1)) {
                         String  tom_match7_1_2_3_2_1_1 = null;
                        tom_match7_1_2_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_3_2_1);
                        tlType1 = ( String ) tom_match7_1_2_3_2_1_1;
                        if(tom_is_fun_sym_Variable(tom_match7_1_3)) {
                           Option  tom_match7_1_3_1 = null;
                           TomName  tom_match7_1_3_2 = null;
                           TomType  tom_match7_1_3_3 = null;
                          tom_match7_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_3);
                          tom_match7_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_3);
                          tom_match7_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_3);
                          option2 = ( Option ) tom_match7_1_3_1;
                          if(tom_is_fun_sym_Name(tom_match7_1_3_2)) {
                             String  tom_match7_1_3_2_1 = null;
                            tom_match7_1_3_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_3_2);
                            name2 = ( String ) tom_match7_1_3_2_1;
                            if(tom_is_fun_sym_Type(tom_match7_1_3_3)) {
                               TomType  tom_match7_1_3_3_1 = null;
                               TomType  tom_match7_1_3_3_2 = null;
                              tom_match7_1_3_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_3_3);
                              tom_match7_1_3_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_3_3);
                              fullListType = ( TomType ) tom_match7_1_3_3;
                              if(tom_is_fun_sym_TomType(tom_match7_1_3_3_1)) {
                                 String  tom_match7_1_3_3_1_1 = null;
                                tom_match7_1_3_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_3_3_1);
                                type2 = ( String ) tom_match7_1_3_3_1_1;
                                if(tom_is_fun_sym_TLType(tom_match7_1_3_3_2)) {
                                   TargetLanguage  tom_match7_1_3_3_2_1 = null;
                                  tom_match7_1_3_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_3_3_2);
                                  if(tom_is_fun_sym_TL(tom_match7_1_3_3_2_1)) {
                                     String  tom_match7_1_3_3_2_1_1 = null;
                                    tom_match7_1_3_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_3_2_1);
                                    tlType2 = ( String ) tom_match7_1_3_3_2_1_1;
                                    if(tom_is_fun_sym_TLCode(tom_match7_1_4)) {
                                       TargetLanguage  tom_match7_1_4_1 = null;
                                      tom_match7_1_4_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_4);
                                      if(tom_is_fun_sym_TL(tom_match7_1_4_1)) {
                                         String  tom_match7_1_4_1_1 = null;
                                        tom_match7_1_4_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_4_1);
                                        tlCode = ( String ) tom_match7_1_4_1_1;
                                        
        String returnType, argListType,argEltType;
        if(Flags.strictType) {
          argEltType = tlType1;
          argListType = tlType2;
          returnType = argListType;
        } else {
          argEltType  = getTLType(getUniversalType());
          argListType = getTLType(getUniversalType());
          returnType  = argListType;
        }
        
        generateTargetLanguage(out,deep, genDecl(returnType,
                                                 "tom_make_insert", opname,
                                                 new String[] {
                                                   argEltType, name1,
                                                   argListType, name2
                                                 },
                                                 tlCode));
        
        generateTargetLanguage(out,deep, genDeclList(opname, fullListType,fullEltType));
        return;
      
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern13: {
         Option  option2 = null;
         String  tlType2 = null;
         String  tlType1 = null;
         String  tlCode = null;
         String  type2 = null;
         Option  option1 = null;
         String  type1 = null;
         String  name1 = null;
         String  name2 = null;
        if(tom_is_fun_sym_GetElementDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_GetElementDecl_kid1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetElementDecl_kid2(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_GetElementDecl_kid3(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type1 = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      tlType1 = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
                         Option  tom_match7_1_2_1 = null;
                         TomName  tom_match7_1_2_2 = null;
                         TomType  tom_match7_1_2_3 = null;
                        tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
                        tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
                        tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
                        option2 = ( Option ) tom_match7_1_2_1;
                        if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                           String  tom_match7_1_2_2_1 = null;
                          tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                          name2 = ( String ) tom_match7_1_2_2_1;
                          if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                             TomType  tom_match7_1_2_3_1 = null;
                             TomType  tom_match7_1_2_3_2 = null;
                            tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                            tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                            if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                               String  tom_match7_1_2_3_1_1 = null;
                              tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                              type2 = ( String ) tom_match7_1_2_3_1_1;
                              if(tom_is_fun_sym_TLType(tom_match7_1_2_3_2)) {
                                 TargetLanguage  tom_match7_1_2_3_2_1 = null;
                                tom_match7_1_2_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_2_3_2);
                                if(tom_is_fun_sym_TL(tom_match7_1_2_3_2_1)) {
                                   String  tom_match7_1_2_3_2_1_1 = null;
                                  tom_match7_1_2_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_3_2_1);
                                  tlType2 = ( String ) tom_match7_1_2_3_2_1_1;
                                  if(tom_is_fun_sym_TLCode(tom_match7_1_3)) {
                                     TargetLanguage  tom_match7_1_3_1 = null;
                                    tom_match7_1_3_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_3);
                                    if(tom_is_fun_sym_TL(tom_match7_1_3_1)) {
                                       String  tom_match7_1_3_1_1 = null;
                                      tom_match7_1_3_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_1);
                                      tlCode = ( String ) tom_match7_1_3_1_1;
                                      
        String returnType, argType;
        if(Flags.strictType) {
          returnType = getTLType(getUniversalType());
          argType = tlType1;
        } else {
          returnType = getTLType(getUniversalType());
          argType = getTLType(getUniversalType());
        }
      
        generateTargetLanguage(out,deep, genDecl(returnType,
                               "tom_get_element", type1,
                               new String[] {
                                 argType, name1,
                                 getTLType(getIntType()), name2
                               },
                               tlCode));
        return;
      
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern14: {
         String  name1 = null;
         String  tlCode = null;
         String  type = null;
         Option  option1 = null;
         String  tlType = null;
        if(tom_is_fun_sym_GetSizeDecl(tom_match7_1)) {
           TomTerm  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
          tom_match7_1_1 = ( TomTerm ) tom_get_slot_GetSizeDecl_kid1(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_GetSizeDecl_kid2(tom_match7_1);
          if(tom_is_fun_sym_Variable(tom_match7_1_1)) {
             Option  tom_match7_1_1_1 = null;
             TomName  tom_match7_1_1_2 = null;
             TomType  tom_match7_1_1_3 = null;
            tom_match7_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_1);
            tom_match7_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_1);
            tom_match7_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_1);
            option1 = ( Option ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Name(tom_match7_1_1_2)) {
               String  tom_match7_1_1_2_1 = null;
              tom_match7_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1_2);
              name1 = ( String ) tom_match7_1_1_2_1;
              if(tom_is_fun_sym_Type(tom_match7_1_1_3)) {
                 TomType  tom_match7_1_1_3_1 = null;
                 TomType  tom_match7_1_1_3_2 = null;
                tom_match7_1_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_1_3);
                tom_match7_1_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_1_3);
                if(tom_is_fun_sym_TomType(tom_match7_1_1_3_1)) {
                   String  tom_match7_1_1_3_1_1 = null;
                  tom_match7_1_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_1_3_1);
                  type = ( String ) tom_match7_1_1_3_1_1;
                  if(tom_is_fun_sym_TLType(tom_match7_1_1_3_2)) {
                     TargetLanguage  tom_match7_1_1_3_2_1 = null;
                    tom_match7_1_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_1_3_2);
                    if(tom_is_fun_sym_TL(tom_match7_1_1_3_2_1)) {
                       String  tom_match7_1_1_3_2_1_1 = null;
                      tom_match7_1_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_1_3_2_1);
                      tlType = ( String ) tom_match7_1_1_3_2_1_1;
                      if(tom_is_fun_sym_TLCode(tom_match7_1_2)) {
                         TargetLanguage  tom_match7_1_2_1 = null;
                        tom_match7_1_2_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_2);
                        if(tom_is_fun_sym_TL(tom_match7_1_2_1)) {
                           String  tom_match7_1_2_1_1 = null;
                          tom_match7_1_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_1);
                          tlCode = ( String ) tom_match7_1_2_1_1;
                          
        String argType;
        if(Flags.strictType) {
          argType = tlType;
        } else {
          argType = getTLType(getUniversalType());
        }
        
        generateTargetLanguage(out,deep,
                               genDecl(getTLType(getIntType()),
                                       "tom_get_size", type,
                                       new String[] { argType, name1 },
                                       tlCode));
        return;
      
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern15: {
         String  type1 = null;
         Option  option1 = null;
         String  name1 = null;
         String  tlCode = null;
         String  opname = null;
        if(tom_is_fun_sym_MakeEmptyArray(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_MakeEmptyArray_astName(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_MakeEmptyArray_varSize(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_MakeEmptyArray_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            opname = ( String ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
               Option  tom_match7_1_2_1 = null;
               TomName  tom_match7_1_2_2 = null;
               TomType  tom_match7_1_2_3 = null;
              tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
              tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
              tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
              option1 = ( Option ) tom_match7_1_2_1;
              if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                 String  tom_match7_1_2_2_1 = null;
                tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                name1 = ( String ) tom_match7_1_2_2_1;
                if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                   TomType  tom_match7_1_2_3_1 = null;
                   TomType  tom_match7_1_2_3_2 = null;
                  tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                  tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                  if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                     String  tom_match7_1_2_3_1_1 = null;
                    tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                    type1 = ( String ) tom_match7_1_2_3_1_1;
                    if(tom_is_fun_sym_TLCode(tom_match7_1_3)) {
                       TargetLanguage  tom_match7_1_3_1 = null;
                      tom_match7_1_3_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_3);
                      if(tom_is_fun_sym_TL(tom_match7_1_3_1)) {
                         String  tom_match7_1_3_1_1 = null;
                        tom_match7_1_3_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_1);
                        tlCode = ( String ) tom_match7_1_3_1_1;
                        
        generateTargetLanguage(out,deep, genDecl(getTLType(getUniversalType()), "tom_make_empty", opname,
                                   new String[] {
                                     getTLType(getIntType()), name1,
                                   },
                                   tlCode));
        return;
      
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern16: {
         Option  option1 = null;
         String  name1 = null;
         TomType  fullEltType = null;
         String  tlType2 = null;
         Option  option2 = null;
         String  tlCode = null;
         String  type1 = null;
         TomType  fullArrayType = null;
         String  name2 = null;
         String  tlType1 = null;
         String  type2 = null;
         String  opname = null;
        if(tom_is_fun_sym_MakeAddArray(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomTerm  tom_match7_1_2 = null;
           TomTerm  tom_match7_1_3 = null;
           TomTerm  tom_match7_1_4 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_MakeAddArray_astName(tom_match7_1);
          tom_match7_1_2 = ( TomTerm ) tom_get_slot_MakeAddArray_varElt(tom_match7_1);
          tom_match7_1_3 = ( TomTerm ) tom_get_slot_MakeAddArray_varList(tom_match7_1);
          tom_match7_1_4 = ( TomTerm ) tom_get_slot_MakeAddArray_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            opname = ( String ) tom_match7_1_1_1;
            if(tom_is_fun_sym_Variable(tom_match7_1_2)) {
               Option  tom_match7_1_2_1 = null;
               TomName  tom_match7_1_2_2 = null;
               TomType  tom_match7_1_2_3 = null;
              tom_match7_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_2);
              tom_match7_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_2);
              tom_match7_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_2);
              option1 = ( Option ) tom_match7_1_2_1;
              if(tom_is_fun_sym_Name(tom_match7_1_2_2)) {
                 String  tom_match7_1_2_2_1 = null;
                tom_match7_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_2_2);
                name1 = ( String ) tom_match7_1_2_2_1;
                if(tom_is_fun_sym_Type(tom_match7_1_2_3)) {
                   TomType  tom_match7_1_2_3_1 = null;
                   TomType  tom_match7_1_2_3_2 = null;
                  tom_match7_1_2_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_2_3);
                  tom_match7_1_2_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_2_3);
                  fullEltType = ( TomType ) tom_match7_1_2_3;
                  if(tom_is_fun_sym_TomType(tom_match7_1_2_3_1)) {
                     String  tom_match7_1_2_3_1_1 = null;
                    tom_match7_1_2_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_2_3_1);
                    type1 = ( String ) tom_match7_1_2_3_1_1;
                    if(tom_is_fun_sym_TLType(tom_match7_1_2_3_2)) {
                       TargetLanguage  tom_match7_1_2_3_2_1 = null;
                      tom_match7_1_2_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_2_3_2);
                      if(tom_is_fun_sym_TL(tom_match7_1_2_3_2_1)) {
                         String  tom_match7_1_2_3_2_1_1 = null;
                        tom_match7_1_2_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_2_3_2_1);
                        tlType1 = ( String ) tom_match7_1_2_3_2_1_1;
                        if(tom_is_fun_sym_Variable(tom_match7_1_3)) {
                           Option  tom_match7_1_3_1 = null;
                           TomName  tom_match7_1_3_2 = null;
                           TomType  tom_match7_1_3_3 = null;
                          tom_match7_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match7_1_3);
                          tom_match7_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match7_1_3);
                          tom_match7_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match7_1_3);
                          option2 = ( Option ) tom_match7_1_3_1;
                          if(tom_is_fun_sym_Name(tom_match7_1_3_2)) {
                             String  tom_match7_1_3_2_1 = null;
                            tom_match7_1_3_2_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_3_2);
                            name2 = ( String ) tom_match7_1_3_2_1;
                            if(tom_is_fun_sym_Type(tom_match7_1_3_3)) {
                               TomType  tom_match7_1_3_3_1 = null;
                               TomType  tom_match7_1_3_3_2 = null;
                              tom_match7_1_3_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match7_1_3_3);
                              tom_match7_1_3_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match7_1_3_3);
                              fullArrayType = ( TomType ) tom_match7_1_3_3;
                              if(tom_is_fun_sym_TomType(tom_match7_1_3_3_1)) {
                                 String  tom_match7_1_3_3_1_1 = null;
                                tom_match7_1_3_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match7_1_3_3_1);
                                type2 = ( String ) tom_match7_1_3_3_1_1;
                                if(tom_is_fun_sym_TLType(tom_match7_1_3_3_2)) {
                                   TargetLanguage  tom_match7_1_3_3_2_1 = null;
                                  tom_match7_1_3_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match7_1_3_3_2);
                                  if(tom_is_fun_sym_TL(tom_match7_1_3_3_2_1)) {
                                     String  tom_match7_1_3_3_2_1_1 = null;
                                    tom_match7_1_3_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_3_3_2_1);
                                    tlType2 = ( String ) tom_match7_1_3_3_2_1_1;
                                    if(tom_is_fun_sym_TLCode(tom_match7_1_4)) {
                                       TargetLanguage  tom_match7_1_4_1 = null;
                                      tom_match7_1_4_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_4);
                                      if(tom_is_fun_sym_TL(tom_match7_1_4_1)) {
                                         String  tom_match7_1_4_1_1 = null;
                                        tom_match7_1_4_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_4_1);
                                        tlCode = ( String ) tom_match7_1_4_1_1;
                                        

        String returnType, argListType,argEltType;
        if(Flags.strictType) {
          argEltType  = tlType1;
          argListType = tlType2;
          returnType  = argListType;

        } else {
          argEltType  = getTLType(getUniversalType());
          argListType = getTLType(getUniversalType());
          returnType  = argListType;
        }

        generateTargetLanguage(out,deep,
                               genDecl(argListType,
                                       "tom_make_append", opname,
                                       new String[] {
                                         argEltType, name1,
                                         argListType, name2
                                       },
                                       tlCode));
        generateTargetLanguage(out,deep, genDeclArray(opname, fullArrayType,fullEltType));
        return;
      
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match7_pattern17: {
         String  opname = null;
         TomType  returnType = null;
         TomList  argList = null;
         String  tlCode = null;
        if(tom_is_fun_sym_MakeDecl(tom_match7_1)) {
           TomName  tom_match7_1_1 = null;
           TomType  tom_match7_1_2 = null;
           TomList  tom_match7_1_3 = null;
           TomTerm  tom_match7_1_4 = null;
          tom_match7_1_1 = ( TomName ) tom_get_slot_MakeDecl_astName(tom_match7_1);
          tom_match7_1_2 = ( TomType ) tom_get_slot_MakeDecl_astType(tom_match7_1);
          tom_match7_1_3 = ( TomList ) tom_get_slot_MakeDecl_args(tom_match7_1);
          tom_match7_1_4 = ( TomTerm ) tom_get_slot_MakeDecl_tlCode(tom_match7_1);
          if(tom_is_fun_sym_Name(tom_match7_1_1)) {
             String  tom_match7_1_1_1 = null;
            tom_match7_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match7_1_1);
            opname = ( String ) tom_match7_1_1_1;
            returnType = ( TomType ) tom_match7_1_2;
            argList = ( TomList ) tom_match7_1_3;
            if(tom_is_fun_sym_TLCode(tom_match7_1_4)) {
               TargetLanguage  tom_match7_1_4_1 = null;
              tom_match7_1_4_1 = ( TargetLanguage ) tom_get_slot_TLCode_tl(tom_match7_1_4);
              if(tom_is_fun_sym_TL(tom_match7_1_4_1)) {
                 String  tom_match7_1_4_1_1 = null;
                tom_match7_1_4_1_1 = ( String ) tom_get_slot_TL_code(tom_match7_1_4_1);
                tlCode = ( String ) tom_match7_1_4_1_1;
                
        generateTargetLanguage(out,deep, genDeclMake(opname, returnType, argList, tlCode));
        return;
      
              }
            }
          }
        }
}
matchlab_match7_pattern18: {
         Declaration  t = null;
        t = ( Declaration ) tom_match7_1;
        
        System.out.println("Cannot generate code for declaration: " + t);
        System.exit(1);
      
}
    }
    
  }

  public void generateList(OutputCode out, int deep, TomList subject)
    throws IOException {
      //%variable
    if(subject.isEmpty()) {
      return;
    }

    TomTerm t = subject.getHead();
    TomList l = subject.getTail(); 
    generate(out,deep,t);
    generateList(out,deep,l);
  }

  public void generateOptionList(OutputCode out, int deep, OptionList subject)
    throws IOException {
      //%variable
    if(subject.isEmptyOptionList()) {
      return;
    }

    Option t = subject.getHead();
    OptionList l = subject.getTail(); 
    generateOption(out,deep,t);
    generateOptionList(out,deep,l);
  }

  // ------------------------------------------------------------
  private TargetLanguage genDecl(String returnType,
                        String declName,
                        String suffix,
                        String args[],
                        String tlCode) {
    String s = "";
    if(!Flags.genDecl) { return null; }

    if(Flags.cCode) {
      s = "#define " + declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i+1];
        i+=2;
        if(i<args.length) {
          s+= ", ";
        }
      }
      s += ") (" + tlCode +")";
    } else if(Flags.jCode) {
      s = "public " + returnType + " " + declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i] + " " + args[i+1];
        i+=2;
        if(i<args.length) {
          s+= ", ";
        }
      } 
      s += ") { return " + tlCode + "; }";
    } else if(Flags.eCode) {
      s = declName + "_" + suffix + "(";
      for(int i=0 ; i<args.length ; ) {
        s+= args[i+1] + ": " + args[i];
        i+=2;
        if(i<args.length) {
          s+= "; ";
        }
      } 
      s += "): " + returnType + " is do Result := " + tlCode + "end;";
    }
    return 
tom_make_TL(s)    ;
  }
  
  private TargetLanguage genDeclMake(String opname, TomType returnType, TomList argList, String tlCode) {
    //%variable
    String s = "";
    if(!Flags.genDecl) { return null; }

    if(Flags.cCode) {
        //System.out.println("genDeclMake: not yet implemented for C");
        //System.exit(1);
      s = "#define tom_make_" + opname + "(";
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          
    {
       TomTerm  tom_match8_1 = null;
      tom_match8_1 = ( TomTerm ) arg;
matchlab_match8_pattern1: {
         String  type = null;
         String  glType = null;
         String  name = null;
         Option  option = null;
        if(tom_is_fun_sym_Variable(tom_match8_1)) {
           Option  tom_match8_1_1 = null;
           TomName  tom_match8_1_2 = null;
           TomType  tom_match8_1_3 = null;
          tom_match8_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match8_1);
          tom_match8_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match8_1);
          tom_match8_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match8_1);
          option = ( Option ) tom_match8_1_1;
          if(tom_is_fun_sym_Name(tom_match8_1_2)) {
             String  tom_match8_1_2_1 = null;
            tom_match8_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match8_1_2);
            name = ( String ) tom_match8_1_2_1;
            if(tom_is_fun_sym_Type(tom_match8_1_3)) {
               TomType  tom_match8_1_3_1 = null;
               TomType  tom_match8_1_3_2 = null;
              tom_match8_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match8_1_3);
              tom_match8_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match8_1_3);
              if(tom_is_fun_sym_TomType(tom_match8_1_3_1)) {
                 String  tom_match8_1_3_1_1 = null;
                tom_match8_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match8_1_3_1);
                type = ( String ) tom_match8_1_3_1_1;
                if(tom_is_fun_sym_TLType(tom_match8_1_3_2)) {
                   TargetLanguage  tom_match8_1_3_2_1 = null;
                  tom_match8_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match8_1_3_2);
                  if(tom_is_fun_sym_TL(tom_match8_1_3_2_1)) {
                     String  tom_match8_1_3_2_1_1 = null;
                    tom_match8_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match8_1_3_2_1);
                    glType = ( String ) tom_match8_1_3_2_1_1;
                    
              s += name;
              break matchBlock;
            
                  }
                }
              }
            }
          }
        }
}
matchlab_match8_pattern2: {
        
              System.out.println("genDeclMake: strange term: " + arg);
              System.exit(1);
            
}
    }
    
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += ", ";
        }
      }
      s += ") (" + tlCode + ")";
    } else if(Flags.jCode) {
      s = "public " + getTLType(returnType) + " tom_make_" + opname + "(";
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          
    {
       TomTerm  tom_match9_1 = null;
      tom_match9_1 = ( TomTerm ) arg;
matchlab_match9_pattern1: {
         Option  option = null;
         String  glType = null;
         String  name = null;
         String  type = null;
        if(tom_is_fun_sym_Variable(tom_match9_1)) {
           Option  tom_match9_1_1 = null;
           TomName  tom_match9_1_2 = null;
           TomType  tom_match9_1_3 = null;
          tom_match9_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match9_1);
          tom_match9_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match9_1);
          tom_match9_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match9_1);
          option = ( Option ) tom_match9_1_1;
          if(tom_is_fun_sym_Name(tom_match9_1_2)) {
             String  tom_match9_1_2_1 = null;
            tom_match9_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match9_1_2);
            name = ( String ) tom_match9_1_2_1;
            if(tom_is_fun_sym_Type(tom_match9_1_3)) {
               TomType  tom_match9_1_3_1 = null;
               TomType  tom_match9_1_3_2 = null;
              tom_match9_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match9_1_3);
              tom_match9_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match9_1_3);
              if(tom_is_fun_sym_TomType(tom_match9_1_3_1)) {
                 String  tom_match9_1_3_1_1 = null;
                tom_match9_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match9_1_3_1);
                type = ( String ) tom_match9_1_3_1_1;
                if(tom_is_fun_sym_TLType(tom_match9_1_3_2)) {
                   TargetLanguage  tom_match9_1_3_2_1 = null;
                  tom_match9_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match9_1_3_2);
                  if(tom_is_fun_sym_TL(tom_match9_1_3_2_1)) {
                     String  tom_match9_1_3_2_1_1 = null;
                    tom_match9_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match9_1_3_2_1);
                    glType = ( String ) tom_match9_1_3_2_1_1;
                    
              s += glType + " " + name;
              break matchBlock;
            
                  }
                }
              }
            }
          }
        }
}
matchlab_match9_pattern2: {
        
              System.out.println("genDeclMake: strange term: " + arg);
              System.exit(1);
            
}
    }
    
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += ", ";
        }
      }
      s += ") { return " + tlCode + "; }";
    } else if(Flags.eCode) {
      boolean braces = !argList.isEmpty();
      s = "tom_make_" + opname;
      if(braces) {
        s = s + "(";
      }
      while(!argList.isEmpty()) {
        TomTerm arg = argList.getHead();
        matchBlock: {
          
    {
       TomTerm  tom_match10_1 = null;
      tom_match10_1 = ( TomTerm ) arg;
matchlab_match10_pattern1: {
         String  name = null;
         Option  option = null;
         String  type = null;
         String  glType = null;
        if(tom_is_fun_sym_Variable(tom_match10_1)) {
           Option  tom_match10_1_1 = null;
           TomName  tom_match10_1_2 = null;
           TomType  tom_match10_1_3 = null;
          tom_match10_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match10_1);
          tom_match10_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match10_1);
          tom_match10_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match10_1);
          option = ( Option ) tom_match10_1_1;
          if(tom_is_fun_sym_Name(tom_match10_1_2)) {
             String  tom_match10_1_2_1 = null;
            tom_match10_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match10_1_2);
            name = ( String ) tom_match10_1_2_1;
            if(tom_is_fun_sym_Type(tom_match10_1_3)) {
               TomType  tom_match10_1_3_1 = null;
               TomType  tom_match10_1_3_2 = null;
              tom_match10_1_3_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match10_1_3);
              tom_match10_1_3_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match10_1_3);
              if(tom_is_fun_sym_TomType(tom_match10_1_3_1)) {
                 String  tom_match10_1_3_1_1 = null;
                tom_match10_1_3_1_1 = ( String ) tom_get_slot_TomType_string(tom_match10_1_3_1);
                type = ( String ) tom_match10_1_3_1_1;
                if(tom_is_fun_sym_TLType(tom_match10_1_3_2)) {
                   TargetLanguage  tom_match10_1_3_2_1 = null;
                  tom_match10_1_3_2_1 = ( TargetLanguage ) tom_get_slot_TLType_tl(tom_match10_1_3_2);
                  if(tom_is_fun_sym_TL(tom_match10_1_3_2_1)) {
                     String  tom_match10_1_3_2_1_1 = null;
                    tom_match10_1_3_2_1_1 = ( String ) tom_get_slot_TL_code(tom_match10_1_3_2_1);
                    glType = ( String ) tom_match10_1_3_2_1_1;
                    
              s += name + ": " + glType;
              break matchBlock;
            
                  }
                }
              }
            }
          }
        }
}
matchlab_match10_pattern2: {
        
              System.out.println("genDeclMake: strange term: " + arg);
              System.exit(1);
            
}
    }
    
        }
        argList = argList.getTail();
        if(!argList.isEmpty()) {
          s += "; ";
        }
      }
      if(braces) {
        s = s + ")";
      }
      s += ": " + getTLType(returnType) + " is do Result := " + tlCode + "end;";
    }
    return 
tom_make_TL(s)    ;
  }
  
  private TargetLanguage genDeclList(String name, TomType listType, TomType eltType) {
    //%variable
    String s = "";
    if(!Flags.genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);

    String utype = glType;
    String modifier = "";
    if(Flags.eCode) {
      System.out.println("genDeclList: Eiffel code not yet implemented");
    } else if(Flags.jCode) {
      modifier ="public ";
      if(!Flags.strictType) {
        utype =  getTLType(getUniversalType());
      }
    }

    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast +  "tom_make_empty_" + name;
    String is_empty = "tom_is_empty_" + tomType;
    String make_insert = listCast + "tom_make_insert_" + name;
    String get_head = eltCast + "tom_get_head_" + tomType;
    String get_tail = listCast + "tom_get_tail_" + tomType;
    String reverse = listCast + "tom_reverse_" + name;

    s+= modifier + utype + " tom_reverse_" + name + "(" + utype + " l) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!" + is_empty + "(l) ) {\n"; 
    s+= "      result = " + make_insert + "(" + get_head + "(l),result);\n";    
    s+= "      l = " + get_tail + "(l);\n";  
    s+= "    }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_insert_list_" + name +  "(" + utype + " l1, " + utype + " l2) {\n";
    s+= "   " + glType + " reverse = " + reverse + "(l1);\n";
    s+= "    while(!" + is_empty + "(reverse) ) {\n";  
    s+= "      l2 = " + make_insert + "(" + get_head + "(reverse),l2);\n";  
    s+= "      reverse = " + get_tail + "(reverse);\n";  
    s+= "    }\n";  
    s+= "    return l2;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_get_slice_" + name + "(" + utype + " begin, " + utype + " end) {\n"; 
    s+= "   " + glType + " result = " + make_empty + "();\n"; 
    s+= "    while(!tom_terms_equal_" + tomType + "(begin,end)) {\n";
    s+= "      result = " + make_insert + "(" + get_head + "(begin),result);\n";
    s+= "      begin = " + get_tail + "(begin);\n";
    s+="     }\n";
    s+= "    result = " + reverse + "(result);\n";
    s+= "    return result;\n";
    s+= "  }\n";
    
    return 
tom_make_TL(s)    ;
  }

  private TargetLanguage genDeclArray(String name, TomType listType, TomType eltType) {
    //%variable
    String s = "";
    if(!Flags.genDecl) { return null; }

    String tomType = getTomType(listType);
    String glType = getTLType(listType);
    String tlEltType = getTLType(eltType);
    String utype = glType;
    String modifier = "";
    if(Flags.eCode) {
      System.out.println("genDeclArray: Eiffel code not yet implemented");
    } else if(Flags.jCode) {
      modifier ="public ";
      if(!Flags.strictType) {
        utype =  getTLType(getUniversalType());
      }
    }

    String listCast = "(" + glType + ")";
    String eltCast = "(" + getTLType(eltType) + ")";
    String make_empty = listCast + "tom_make_empty_" + name;
    String make_append = listCast + "tom_make_append_" + name;
    String get_element = eltCast + "tom_get_element_" + tomType;

    
    s = modifier + utype + " tom_get_slice_" + name +  "(" + utype + " subject, int begin, int end) {\n";
    s+= "   " + glType + " result = " + make_empty + "(end - begin);\n";
    s+= "    while( begin != end ) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(subject, begin),result);\n";
    s+= "      begin++;\n";
    s+="     }\n";
    s+= "    return result;\n";
    s+= "  }\n";
    s+= "\n";
    
    s+= modifier + utype + " tom_append_array_" + name +  "(" + utype + " l2, " + utype + " l1) {\n";
    s+= "    int size1 = tom_get_size_" + tomType + "(l1);\n";
    s+= "    int size2 = tom_get_size_" + tomType + "(l2);\n";
    s+= "    int index;\n";
    s+= "   " + glType + " result = " + make_empty + "(size1+size2);\n";

    s+= "    index=size1;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l1,(size1-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";

    s+= "    index=size2;\n";
    s+= "    while(index > 0) {\n";
    s+= "      result = " + make_append + "(" + get_element + "(l2,(size2-index)),result);\n";
    s+= "      index--;\n";
    s+= "    }\n";
   
    s+= "    return result;\n";
    s+= "  }\n";
    return 
tom_make_TL(s)    ;
  }
  
  private HashMap getSubtermMap = new HashMap();
  private HashMap getFunSymMap = new HashMap();
  private HashMap isFsymMap = new HashMap();
}
  

