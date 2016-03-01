/*
 * Gom
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/
package tom.gom.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public abstract class TemplateClass {
  protected GomClass gomClass;
  protected ClassName className;
  private GomEnvironment gomEnvironment;

  public TemplateClass(GomClass gomClass, GomEnvironment gomEnvironment) {
    this.gomClass = gomClass;
    this.className = gomClass.getClassName();
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

       

  public abstract void generate(Writer writer) throws java.io.IOException;

  public String className() {
    return className(this.className);
  }

  public String className(ClassName clsName) {
    {{if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )(( tom.gom.adt.objects.types.ClassName )clsName)) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

        return  (( tom.gom.adt.objects.types.ClassName )clsName).getName() ;
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:className got a strange ClassName "+clsName);
  }

  public String fullClassName() {
    return fullClassName(this.className);
  }

  public static String fullClassName(ClassName clsName) {
    {{if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )(( tom.gom.adt.objects.types.ClassName )clsName)) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { String  tom_pkgPrefix= (( tom.gom.adt.objects.types.ClassName )clsName).getPkg() ; String  tom_name= (( tom.gom.adt.objects.types.ClassName )clsName).getName() ;

        if(tom_pkgPrefix.length()==0) {
          return tom_name;
        } else {
          return tom_pkgPrefix+"."+tom_name;
        }
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:fullClassName got a strange ClassName "+clsName);
  }

  public String getPackage() {
    return getPackage(this.className);
  }

  public String getPackage(ClassName clsName) {
    {{if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )(( tom.gom.adt.objects.types.ClassName )clsName)) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

        return  (( tom.gom.adt.objects.types.ClassName )clsName).getPkg() ;
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:getPackage got a strange ClassName "+clsName);
  }

  public String hasMethod(SlotField slot) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        return "has"+ (( tom.gom.adt.objects.types.SlotField )slot).getName() ;
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:hasMethod got a strange SlotField "+slot);
  }

  public String getMethod(SlotField slot) {
    return getMethod(slot,false);
  }

  public String setMethod(SlotField slot) {
    return setMethod(slot,false);
  }

  /**
    * @param slot contains the name of the slot
    * @param jmi true to generate a name with a capital letter
    */
  public String getMethod(SlotField slot,boolean jmi) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_name= (( tom.gom.adt.objects.types.SlotField )slot).getName() ;

        if(jmi) {
          return "get"+jmize(tom_name);
        } else {
          return "get"+tom_name;
        }
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:getMethod got a strange SlotField "+slot);
  }

  /**
    * @param slot contains the name of the slot
    * @param jmi true to generate a name with a capital letter
    */
  public String setMethod(SlotField slot,boolean jmi) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_name= (( tom.gom.adt.objects.types.SlotField )slot).getName() ;

        if(jmi) {
          return "set"+jmize(tom_name);
        } else {
          return "set"+tom_name;
        }
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:setMethod got a strange SlotField "+slot);
  }

  private String jmize(String name) {
    //TODO
    return org.apache.commons.lang.StringUtils.capitalize(name);
  }

  public String index(SlotField slot) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        return "index_"+ (( tom.gom.adt.objects.types.SlotField )slot).getName() ;
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:index got a strange SlotField "+slot);
  }

  public String slotDomain(SlotField slot) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        return fullClassName( (( tom.gom.adt.objects.types.SlotField )slot).getDomain() );
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:slotDomain got a strange SlotField "+slot);
  }

  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }

  public String classFieldName(ClassName clsName) {
    {{if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )(( tom.gom.adt.objects.types.ClassName )clsName)) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

        return  (( tom.gom.adt.objects.types.ClassName )clsName).getName() .toLowerCase();
      }}}}}

    throw new GomRuntimeException(
        "TemplateClass:classFieldName got a strange ClassName "+clsName);
  }

  public void toStringSlotField(StringBuilder res, SlotField slot,
                                String element, String buffer) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )slot).getDomain() ;

        if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
          res.append(""+element+".toStringBuilder("+buffer+");\n"
);
        } else {
          if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "double") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "float") )) {
            res.append(""+buffer+".append("+element+");\n"
);
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "long") )) {
            res.append(""+buffer+".append("+element+");\n            "+buffer+".append(\"l\");\n"

);
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
            res.append(""+buffer+".append(((int)"+element+"-(int)'0'));\n"
);
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
            res.append(""+buffer+".append("+element+"?1:0);\n"
);
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
            String atchar = "@";
            res.append(""+buffer+".append('\"');\n            for (int i = 0; i < "+element+".length(); i++) {\n              char c = "+element+".charAt(i);\n              switch (c) {\n                case '\\n':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('n');\n                  break;\n                case '\\t':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('t');\n                  break;\n                case '\\b':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('b');\n                  break;\n                case '\\r':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('r');\n                  break;\n                case '\\f':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('f');\n                  break;\n                case '\\\\':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('\\\\');\n                  break;\n                case '\\'':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('\\'');\n                  break;\n                case '\\\"':\n                  "+buffer+".append('\\\\');\n                  "+buffer+".append('\\\"');\n                  break;\n                case '!':\n                case '"+atchar+"':\n                case '#':\n                case '$':\n                case '%':\n                case '^':\n                case '&':\n                case '*':\n                case '(':\n                case ')':\n                case '-':\n                case '_':\n                case '+':\n                case '=':\n                case '|':\n                case '~':\n                case '{':\n                case '}':\n                case '[':\n                case ']':\n                case ';':\n                case ':':\n                case '<':\n                case '>':\n                case ',':\n                case '.':\n                case '?':\n                case ' ':\n                case '/':\n                  "+buffer+".append(c);\n                  break;\n\n                default:\n                  if (java.lang.Character.isLetterOrDigit(c)) {\n                    "+buffer+".append(c);\n                  } else {\n                    "+buffer+".append('\\\\');\n                    "+buffer+".append((char) ('0' + c / 64));\n                    c = (char) (c % 64);\n                    "+buffer+".append((char) ('0' + c / 8));\n                    c = (char) (c % 8);\n                    "+buffer+".append((char) ('0' + c));\n                  }\n              }\n            }\n            "+buffer+".append('\"');\n"

















































































);
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") ) ||tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
            res.append(""+buffer+".append("+element+".toString());\n"
);
          } else {
            throw new GomRuntimeException("Builtin " + tom_domain+ " not supported");
          }
        }
      }}}}}

  }

  public void toATermSlotField(StringBuilder res, SlotField slot) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )slot).getDomain() ;

        if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
          res.append(getMethod(slot));
          res.append("().toATerm()");
        } else {
          if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "int") )) {
            res.append("(aterm.ATerm) atermFactory.makeInt(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
            res.append("(aterm.ATerm) atermFactory.makeInt(");
            res.append(getMethod(slot));
            res.append("()?1:0)");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "long") )) {
            res.append("(aterm.ATerm) atermFactory.makeLong(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "double") )) {
            res.append("(aterm.ATerm) atermFactory.makeReal(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "float") )) {
            res.append("(aterm.ATerm) atermFactory.makeReal(");
            res.append(getMethod(slot));
            res.append("())");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
            res.append("(aterm.ATerm) atermFactory.makeInt(((int)");
            res.append(getMethod(slot));
            res.append("()-(int)'0'))");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
            res.append("(aterm.ATerm) atermFactory.makeAppl(");
            res.append("atermFactory.makeAFun(");
            res.append(getMethod(slot));
            res.append("() ,0 , true))");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") ) ||tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )){
            res.append(getMethod(slot));
            res.append("()");
          } else {
            throw new GomRuntimeException("Builtin " + tom_domain+ " not supported");
          }
        }
      }}}}}

  }

  public void fromATermSlotField(StringBuilder buffer, SlotField slot, String appl, String atConv) {
    {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )slot).getDomain() ;

        if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
          buffer.append(fullClassName(tom_domain));
          buffer.append(".fromTerm(");
          buffer.append(appl);
          buffer.append(",");
          buffer.append(atConv);
          buffer.append(")");
        } else {
          if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "int") )) {
            buffer.append("convertATermToInt(").append(appl).append(", ").append(atConv).append(")");
          } else  if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "float") )) {
            buffer.append("convertATermToFloat(").append(appl).append(", ").append(atConv).append(")");
          } else  if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
            buffer.append("convertATermToBoolean(").append(appl).append(", ").append(atConv).append(")");
          } else  if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "long") )) {
            buffer.append("convertATermToLong(").append(appl).append(", ").append(atConv).append(")");
          } else  if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "double") )) {
            buffer.append("convertATermToDouble(").append(appl).append(", ").append(atConv).append(")");
          } else  if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
            buffer.append("convertATermToChar(").append(appl).append(", ").append(atConv).append(")");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
            buffer.append("convertATermToString(").append(appl).append(", ").append(atConv).append(")");
          } else if (tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") ) || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") ) ){
            buffer.append(appl);
          } else {
            throw new GomRuntimeException("Builtin " + tom_domain+ " not supported");
          }
        }
      }}}}}

  }

  protected String primitiveToReferenceType(String classname) {
    {{if ( true ) {if ( true ) {if ( "byte".equals((( String )classname)) ) {
 return "java.lang.Byte"; }}}}{if ( true ) {if ( true ) {if ( "short".equals((( String )classname)) ) {
 return "java.lang.Short"; }}}}{if ( true ) {if ( true ) {if ( "int".equals((( String )classname)) ) {
 return "java.lang.Integer"; }}}}{if ( true ) {if ( true ) {if ( "long".equals((( String )classname)) ) {
 return "java.lang.Long"; }}}}{if ( true ) {if ( true ) {if ( "float".equals((( String )classname)) ) {
 return "java.lang.Float"; }}}}{if ( true ) {if ( true ) {if ( "double".equals((( String )classname)) ) {
 return "java.lang.Double"; }}}}{if ( true ) {if ( true ) {if ( "boolean".equals((( String )classname)) ) {
 return "java.lang.Boolean"; }}}}{if ( true ) {if ( true ) {if ( "char".equals((( String )classname)) ) {
 return "java.lang.Character"; }}}}}

    return classname;
  }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".java";
  }

  protected File fileToGenerate() {
    GomStreamManager stream = getGomEnvironment().getStreamManager();
    File output = new File(stream.getDestDir(),fileName());
    return output;
  }

  public int generateFile() {
    try {
       File output = fileToGenerate();
       // make sure the directory exists
       // if creation failed, try again, as this can be a manifestation of a
       // race condition in mkdirs
       if (!output.getParentFile().mkdirs()) {
         output.getParentFile().mkdirs();
       }
       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
       generate(writer);
       writer.flush();
       writer.close();
    } catch(IOException e) {
      GomMessage.error(getLogger(),null,0,
          GomMessage.tomCodeGenerationFailure, e.getMessage());
      return 1;
    }
    return 0;
  }

  public String visitMethod(ClassName sortName) {
    return "visit_"+className(sortName);
  }

  public String isOperatorMethod(ClassName opName) {
    return "is"+className(opName);
  }

  public String getCollectionMethod(ClassName opName) {
    return "getCollection"+className(opName);
  }

  protected void slotDecl(java.io.Writer writer, SlotFieldList slotList)
                        throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyConcSlotField()) {
      SlotField slot = slotList.getHeadConcSlotField();
      slotList = slotList.getTailConcSlotField();
      if (index>0) { writer.write(", "); }
      {{if ( (slot instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )slot) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )slot)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tomMatch545_2= (( tom.gom.adt.objects.types.SlotField )slot).getDomain() ;if ( (tomMatch545_2 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch545_2) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

          writer.write( (( tom.gom.adt.objects.types.SlotField )slot).getName() );
          writer.write(":");
          writer.write( tomMatch545_2.getName() );
          index++;
        }}}}}}}

    }
  }

  protected void slotArgs(java.io.Writer writer, SlotFieldList slotList)
                        throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyConcSlotField()) {
      SlotField slot = slotList.getHeadConcSlotField();
      slotList = slotList.getTailConcSlotField();
      if (index>0) { writer.write(", "); }
      /* Warning: do not write the 'index' alone, this is not a valid variable
         name */
      writer.write("t"+index);
      index++;
    }
  }

  protected void slotArgsWithDollar(java.io.Writer writer, SlotFieldList slotList)
                        throws java.io.IOException {
    int index = 0;
    while(!slotList.isEmptyConcSlotField()) {
      SlotField slot = slotList.getHeadConcSlotField();
      slotList = slotList.getTailConcSlotField();
      if (index>0) { writer.write(", "); }
      /* Warning: do not write the 'index' alone, this is not a valid variable
         name */
      writer.write("$t"+index);
      index++;
    }
  }

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    return;
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
