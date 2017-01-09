/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2009-2017, Universite de Lorraine, Inria
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

package tom.emf;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Transforms an EcorePackage to XMI thanks an EcoreMapping (ecoremapping.tom)
 * 
 * @author Nicolas HENRY
 * 
 */

public class EcoreMappingToXMI {

    private static <O> org.eclipse.emf.common.util.EList<O> appendEAnnotationEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }      public static <O extends org.eclipse.emf.ecore.EObject> O constructEStringToStringMapEntry(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    private static <O> org.eclipse.emf.common.util.EList<O> appendEntryEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEObject(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }     private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  tom_get_slice_EObjectEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  subject, int begin, int end) {      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EObject>(end-begin) ;     while(begin!=end) {       result =  appendEObjectEList( subject.get(begin) ,result) ;       begin++;     }     return result;   }    private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  tom_append_array_EObjectEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  l2,  org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  l1) {     int size1 =  l1.size() ;     int size2 =  l2.size() ;     int index;      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EObject>(size1+size2) ;     index=size1;     while(index >0) {       result =  appendEObjectEList( l1.get(size1-index) ,result) ;       index--;     }      index=size2;     while(index > 0) {       result =  appendEObjectEList( l2.get(size2-index) ,result) ;       index--;     }     return result;   }  private static <O> org.eclipse.emf.common.util.EList<O> appendEObjectEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEAnnotation(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }      private static <O> org.eclipse.emf.common.util.EList<O> appendEGenericTypeEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEGenericType(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructETypeParameter(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    private static <O> org.eclipse.emf.common.util.EList<O> appendETypeParameterEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEAttribute(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }     private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  tom_get_slice_EClassEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  subject, int begin, int end) {      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EClass>(end-begin) ;     while(begin!=end) {       result =  appendEClassEList( subject.get(begin) ,result) ;       begin++;     }     return result;   }    private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  tom_append_array_EClassEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  l2,  org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  l1) {     int size1 =  l1.size() ;     int size2 =  l2.size() ;     int index;      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EClass>(size1+size2) ;     index=size1;     while(index >0) {       result =  appendEClassEList( l1.get(size1-index) ,result) ;       index--;     }      index=size2;     while(index > 0) {       result =  appendEClassEList( l2.get(size2-index) ,result) ;       index--;     }     return result;   }  private static <O> org.eclipse.emf.common.util.EList<O> appendEClassEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEParameter(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    private static <O> org.eclipse.emf.common.util.EList<O> appendEParameterEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }     private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  tom_get_slice_EClassifierEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  subject, int begin, int end) {      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EClassifier>(end-begin) ;     while(begin!=end) {       result =  appendEClassifierEList( subject.get(begin) ,result) ;       begin++;     }     return result;   }    private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  tom_append_array_EClassifierEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  l2,  org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  l1) {     int size1 =  l1.size() ;     int size2 =  l2.size() ;     int index;      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EClassifier>(size1+size2) ;     index=size1;     while(index >0) {       result =  appendEClassifierEList( l1.get(size1-index) ,result) ;       index--;     }      index=size2;     while(index > 0) {       result =  appendEClassifierEList( l2.get(size2-index) ,result) ;       index--;     }     return result;   }  private static <O> org.eclipse.emf.common.util.EList<O> appendEClassifierEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEOperation(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    private static <O> org.eclipse.emf.common.util.EList<O> appendEOperationEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    private static <O> org.eclipse.emf.common.util.EList<O> appendEStructuralFeatureEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEClass(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEDataType(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEEnumLiteral(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    private static <O> org.eclipse.emf.common.util.EList<O> appendEEnumLiteralEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEEnum(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    private static <O> org.eclipse.emf.common.util.EList<O> appendEPackageEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEPackage(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEFactory(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }     private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  tom_get_slice_EAttributeEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  subject, int begin, int end) {      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EAttribute>(end-begin) ;     while(begin!=end) {       result =  appendEAttributeEList( subject.get(begin) ,result) ;       begin++;     }     return result;   }    private static   org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  tom_append_array_EAttributeEList( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  l2,  org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  l1) {     int size1 =  l1.size() ;     int size2 =  l2.size() ;     int index;      org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  result =  new org.eclipse.emf.common.util.BasicEList<org.eclipse.emf.ecore.EAttribute>(size1+size2) ;     index=size1;     while(index >0) {       result =  appendEAttributeEList( l1.get(size1-index) ,result) ;       index--;     }      index=size2;     while(index > 0) {       result =  appendEAttributeEList( l2.get(size2-index) ,result) ;       index--;     }     return result;   }  private static <O> org.eclipse.emf.common.util.EList<O> appendEAttributeEList(O e,org.eclipse.emf.common.util.EList<O> l) {   l.add(e);   return l; }    public static <O extends org.eclipse.emf.ecore.EObject> O constructEReference(O o, Object[] objs) {   int i=0;   EList<EStructuralFeature> sfes = o.eClass().getEAllStructuralFeatures();   for(EStructuralFeature esf : sfes) {     if(esf.isChangeable()) {       o.eSet(esf, objs[i]);       i++;     }   }   return o; }        
  // LIterals definitions 
  public static final String SERIALIZABLE = "serializable";
  public static final String ORDERED      = "ordered";
  public static final String UNIQUE       = "unique";
  public static final String LOWERBOUND   = "lowerBound";
  public static final String UPPERBOUND   = "upperBound";
  public static final String CHANGEABLE   = "changeable";
  public static final String VOLATILE     = "volatile";
  public static final String TRANSIENT    = "transient";
  public static final String UNSETTABLE   = "unsettable";
  public static final String DERIVED      = "derived";

  /**
   * Return XML string matching the object
   * @param eo EPackage to match
   * @param subpackage to set if eo is a subpackage
   * @return XML string matching the object
   */
  
  public static String parse(EPackage eo, boolean subpackage) {
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EPackage ) {if ( (( org.eclipse.emf.ecore.EPackage )eo) instanceof org.eclipse.emf.ecore.EPackage ) {

        sb.append("<"+(subpackage?"eSubpackages":"ecore:EPackage " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" " +
            "xsi:schemaLocation=\"http://www.eclipse.org/emf/2002/Ecore \" ") +
            "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EPackage )eo).eGet((( org.eclipse.emf.ecore.EPackage )eo).eClass().getEStructuralFeature("name")) + "\" " +
            "nsURI=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EPackage )eo).eGet((( org.eclipse.emf.ecore.EPackage )eo).eClass().getEStructuralFeature("nsURI")) + "\" " +
            "nsPrefix=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EPackage )eo).eGet((( org.eclipse.emf.ecore.EPackage )eo).eClass().getEStructuralFeature("nsPrefix")) + "\" " +
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EPackage )eo).eGet((( org.eclipse.emf.ecore.EPackage )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)(( org.eclipse.emf.ecore.EPackage )eo).eGet((( org.eclipse.emf.ecore.EPackage )eo).eClass().getEStructuralFeature("eClassifiers")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EPackage>)(( org.eclipse.emf.ecore.EPackage )eo).eGet((( org.eclipse.emf.ecore.EPackage )eo).eClass().getEStructuralFeature("eSubpackages")) ));
        sb.append("</ecore:EPackage>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EClass to match
   * @return XML string matching the object
   */
  
  public static String parse(EClass eo) {
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EClass ) {if ( (( org.eclipse.emf.ecore.EClass )eo) instanceof org.eclipse.emf.ecore.EClass ) { String  tom___instanceClassName= (java.lang.String)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("instanceClassName")) ; String  tom___instanceTypeName= (java.lang.String)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("instanceTypeName")) ; boolean  tom____abstract= (java.lang.Boolean)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("abstract")) ; boolean  tom____interface= (java.lang.Boolean)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("interface")) ; org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>  tom___eSuperTypes= (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("eSuperTypes")) ; org.eclipse.emf.ecore.EClass  tom___c=(( org.eclipse.emf.ecore.EClass )eo);

        StringBuffer sb2=new StringBuffer();
        { /* unamed block */{ /* unamed block */if ( tom___eSuperTypes instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)tom___eSuperTypes).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)tom___eSuperTypes).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass>)tom___eSuperTypes).get(0) instanceof org.eclipse.emf.ecore.EClass)) ) {if ( (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes) instanceof org.eclipse.emf.common.util.EList<?> && ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes).size() == 0 || ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes).size()>0 && (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes).get(0) instanceof org.eclipse.emf.ecore.EClass)) ) {int tomMatch3_end_5=0;do {{ /* unamed block */if (!(tomMatch3_end_5 >=  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes).size() )) { org.eclipse.emf.ecore.EClass  tomMatch3_19= (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes).get(tomMatch3_end_5) ;if ( (( org.eclipse.emf.ecore.EClass )tomMatch3_19) instanceof org.eclipse.emf.ecore.EClass ) {
 sb2.append("#//" +  (java.lang.String)tomMatch3_19.eGet(tomMatch3_19.eClass().getEStructuralFeature("name")) + " "); }}tomMatch3_end_5=tomMatch3_end_5 + 1;}} while(!(tomMatch3_end_5 >  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClass> )tom___eSuperTypes).size() ));}}}}

        sb.append("<eClassifiers " +
            "xsi:type=\"ecore:EClass\" " +
            "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("name")) + "\" " + 
            (! EObject.class.isAssignableFrom(tom___c.getInstanceClass()) ? "instanceClassName=\"" + tom___instanceClassName+ "\" " : "") + 
            (!tom___instanceClassName.equals(tom___instanceTypeName) ? "instanceTypeName=\"" + tom___instanceTypeName+ "\" " : "") + 
            ((Boolean)tom___c.eClass().getEStructuralFeature("abstract").getDefaultValue() != tom____abstract? "abstract=\"" + tom____abstract+ "\" " : "") + 
            ((Boolean)tom___c.eClass().getEStructuralFeature("interface").getDefaultValue() != tom____interface? "interface=\"" + tom____interface+ "\" " : "") + 
            (sb2.length() > 0 ? "eSuperTypes=\"" + sb2 + "\" " : "") + 
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("eTypeParameters")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EOperation>)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("eOperations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EStructuralFeature>)(( org.eclipse.emf.ecore.EClass )eo).eGet((( org.eclipse.emf.ecore.EClass )eo).eClass().getEStructuralFeature("eStructuralFeatures")) ));
        sb.append("</eClassifiers>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EDataType to match
   * @return XML string matching the object
   */
  
  public static String parse(EDataType eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EDataType ) {if ( (( org.eclipse.emf.ecore.EDataType )eo) instanceof org.eclipse.emf.ecore.EDataType ) { String  tom___instanceClassName= (java.lang.String)(( org.eclipse.emf.ecore.EDataType )eo).eGet((( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature("instanceClassName")) ; String  tom___instanceTypeName= (java.lang.String)(( org.eclipse.emf.ecore.EDataType )eo).eGet((( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature("instanceTypeName")) ; boolean  tom___serializable= (java.lang.Boolean)(( org.eclipse.emf.ecore.EDataType )eo).eGet((( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature("serializable")) ;

        sb.append("<eClassifiers " +
            "xsi:type=\"ecore:EDataType\" " +
            "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EDataType )eo).eGet((( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature("name")) + "\" " +
            "instanceClassName=\""+tom___instanceClassName+"\" " +
            (!tom___instanceClassName.equals(tom___instanceTypeName)?"instanceTypeName=\"" + tom___instanceTypeName+ "\" ":"") + 
            ((Boolean)(( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature(EcoreMappingToXMI.SERIALIZABLE).getDefaultValue() != tom___serializable?"serializable=\"" + tom___serializable+ "\" ":"") + 
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EDataType )eo).eGet((( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)(( org.eclipse.emf.ecore.EDataType )eo).eGet((( org.eclipse.emf.ecore.EDataType )eo).eClass().getEStructuralFeature("eTypeParameters")) ));
        sb.append("</eClassifiers>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EEnum to match
   * @return XML string matching the object
   */
  
  public static String parse(EEnum eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EEnum ) {if ( (( org.eclipse.emf.ecore.EEnum )eo) instanceof org.eclipse.emf.ecore.EEnum ) { String  tom___instanceClassName= (java.lang.String)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("instanceClassName")) ; String  tom___instanceTypeName= (java.lang.String)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("instanceTypeName")) ; boolean  tom___serializable= (java.lang.Boolean)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("serializable")) ;

        sb.append("<eClassifiers " +
            "xsi:type=\"ecore:EEnum\" " +
            "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("name")) + "\" " +
            "instanceClassName=\""+tom___instanceClassName+"\" " +
            (!tom___instanceClassName.equals(tom___instanceTypeName)?"instanceTypeName=\"" + tom___instanceTypeName+ "\" ":"") + 
            ((Boolean)(( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature(EcoreMappingToXMI.SERIALIZABLE).getDefaultValue() != tom___serializable?"serializable=\"" + tom___serializable+ "\" ":"") + 
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("eTypeParameters")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EEnumLiteral>)(( org.eclipse.emf.ecore.EEnum )eo).eGet((( org.eclipse.emf.ecore.EEnum )eo).eClass().getEStructuralFeature("eLiterals")) ));
        sb.append("</eClassifiers>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EAnnotation to match
   * @return XML string matching the object
   */
  
  public static String parse(EAnnotation eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EAnnotation ) {if ( (( org.eclipse.emf.ecore.EAnnotation )eo) instanceof org.eclipse.emf.ecore.EAnnotation ) { String  tom___source= (java.lang.String)(( org.eclipse.emf.ecore.EAnnotation )eo).eGet((( org.eclipse.emf.ecore.EAnnotation )eo).eClass().getEStructuralFeature("source")) ;

        sb.append("<eAnnotations" + (tom___source!= null?" source=\""+tom___source+"\"":"") + ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EAnnotation )eo).eGet((( org.eclipse.emf.ecore.EAnnotation )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)(( org.eclipse.emf.ecore.EAnnotation )eo).eGet((( org.eclipse.emf.ecore.EAnnotation )eo).eClass().getEStructuralFeature("details")) ));
        sb.append("</eAnnotations>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EAttribute to match
   * @return XML string matching the object
   */
  
  public static String parse(EAttribute eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EAttribute ) {if ( (( org.eclipse.emf.ecore.EAttribute )eo) instanceof org.eclipse.emf.ecore.EAttribute ) { boolean  tom___ordered= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("ordered")) ; boolean  tom___unique= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("unique")) ; int  tom___lowerBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("lowerBound")) ; int  tom___upperBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("upperBound")) ; org.eclipse.emf.ecore.EClassifier  tom___eType= (org.eclipse.emf.ecore.EClassifier)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("eType")) ; org.eclipse.emf.ecore.EGenericType  tom___eGenericType= (org.eclipse.emf.ecore.EGenericType)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("eGenericType")) ; boolean  tom___changeable= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("changeable")) ; boolean  tom____volatile= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("volatile")) ; boolean  tom____transient= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("transient")) ; String  tom___defaultValueLiteral= (java.lang.String)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("defaultValueLiteral")) ; boolean  tom___unsettable= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("unsettable")) ; boolean  tom___derived= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("derived")) ; boolean  tom___iD= (java.lang.Boolean)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("iD")) ; org.eclipse.emf.ecore.EAttribute  tom___a=(( org.eclipse.emf.ecore.EAttribute )eo);

        sb.append("<eStructuralFeatures xsi:type=\"ecore:EAttribute\" " + 
            "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("name")) + "\" " + 
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != tom___ordered?"ordered=\"" + tom___ordered+ "\" ":"") + 
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != tom___unique?"unique=\"" + tom___unique+ "\" ":"") + 
            ((Integer)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != tom___lowerBound?"lowerBound=\"" + tom___lowerBound+ "\" ":"") + 
            ((Integer)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != tom___upperBound?"upperBound=\"" + tom___upperBound+ "\" ":"") + 
            (tom___eType!= null&&tom___eType.getETypeParameters().size() == 0?"eType=\"#//" + tom___eType.getName() + "\" ":"") +
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.CHANGEABLE).getDefaultValue() != tom___changeable?"changeable=\"" + tom___changeable+ "\" ":"") + 
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.VOLATILE).getDefaultValue() != tom____volatile?"volatile=\"" + tom____volatile+ "\" ":"") + 
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.TRANSIENT).getDefaultValue() != tom____transient?"transient=\"" + tom____transient+ "\" ":"") + 
            (tom___defaultValueLiteral!= null?"defaultValueLiteral=\"" + tom___defaultValueLiteral+ "\" ":"") + 
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.UNSETTABLE).getDefaultValue() != tom___unsettable?"unsettable=\"" + tom___unsettable+ "\" ":"") + 
            ((Boolean)tom___a.eClass().getEStructuralFeature(EcoreMappingToXMI.DERIVED).getDefaultValue() != tom___derived?"derived=\"" + tom___derived+ "\" ":"") + 
            ((Boolean)tom___a.eClass().getEStructuralFeature("iD").getDefaultValue() != tom___iD?"iD=\"" + tom___iD+ "\" ":"") + 
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EAttribute )eo).eGet((( org.eclipse.emf.ecore.EAttribute )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        if(tom___eGenericType!= null && (tom___eGenericType.getEClassifier() != tom___eType|| tom___eType.getETypeParameters().size() >0)){
          sb.append(parse(tom___eGenericType, false));
        }
        sb.append("</eStructuralFeatures>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EReference to match
   * @return XML string matching the object
   */
  
  public static String parse(EReference eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EReference ) {if ( (( org.eclipse.emf.ecore.EReference )eo) instanceof org.eclipse.emf.ecore.EReference ) { boolean  tom___ordered= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("ordered")) ; boolean  tom___unique= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("unique")) ; int  tom___lowerBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("lowerBound")) ; int  tom___upperBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("upperBound")) ; org.eclipse.emf.ecore.EClassifier  tom___eType= (org.eclipse.emf.ecore.EClassifier)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("eType")) ; org.eclipse.emf.ecore.EGenericType  tom___eGenericType= (org.eclipse.emf.ecore.EGenericType)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("eGenericType")) ; boolean  tom___changeable= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("changeable")) ; boolean  tom____volatile= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("volatile")) ; boolean  tom____transient= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("transient")) ; String  tom___defaultValueLiteral= (java.lang.String)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("defaultValueLiteral")) ; boolean  tom___unsettable= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("unsettable")) ; boolean  tom___derived= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("derived")) ; boolean  tom___containment= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("containment")) ; boolean  tom___resolveProxies= (java.lang.Boolean)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("resolveProxies")) ; org.eclipse.emf.ecore.EReference  tom___eOpposite= (org.eclipse.emf.ecore.EReference)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("eOpposite")) ; org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>  tom___eKeys= (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("eKeys")) ; org.eclipse.emf.ecore.EReference  tom___r=(( org.eclipse.emf.ecore.EReference )eo);

        StringBuffer sb2=new StringBuffer();
      { /* unamed block */{ /* unamed block */if ( tom___eKeys instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)tom___eKeys).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)tom___eKeys).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute>)tom___eKeys).get(0) instanceof org.eclipse.emf.ecore.EAttribute)) ) {if ( (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys) instanceof org.eclipse.emf.common.util.EList<?> && ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys).size() == 0 || ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys).size()>0 && (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys).get(0) instanceof org.eclipse.emf.ecore.EAttribute)) ) {int tomMatch9_end_5=0;do {{ /* unamed block */if (!(tomMatch9_end_5 >=  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys).size() )) { org.eclipse.emf.ecore.EAttribute  tomMatch9_23= (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys).get(tomMatch9_end_5) ;if ( (( org.eclipse.emf.ecore.EAttribute )tomMatch9_23) instanceof org.eclipse.emf.ecore.EAttribute ) {

        sb2.append("#//" + ( (org.eclipse.emf.ecore.EClassifier)tomMatch9_23.eGet(tomMatch9_23.eClass().getEStructuralFeature("eType")) ).getName() + "/" +  (java.lang.String)tomMatch9_23.eGet(tomMatch9_23.eClass().getEStructuralFeature("name")) + " ");
        }}tomMatch9_end_5=tomMatch9_end_5 + 1;}} while(!(tomMatch9_end_5 >  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAttribute> )tom___eKeys).size() ));}}}}

      sb.append("<eStructuralFeatures xsi:type=\"ecore:EReference\" " + 
        "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("name")) + "\" " + 
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != tom___ordered?"ordered=\"" + tom___ordered+ "\" ":"") + 
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != tom___unique?"unique=\"" + tom___unique+ "\" ":"") + 
        ((Integer)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != tom___lowerBound?"lowerBound=\"" + tom___lowerBound+ "\" ":"") + 
        ((Integer)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != tom___upperBound?"upperBound=\"" + tom___upperBound+ "\" ":"") + 
        (tom___eType!= null&&tom___eType.getETypeParameters().size() == 0?"eType=\"#//" + tom___eType.getName() + "\" ":"") +
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.CHANGEABLE).getDefaultValue() != tom___changeable?"changeable=\"" + tom___changeable+ "\" ":"") + 
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.VOLATILE).getDefaultValue() != tom____volatile?"volatile=\"" + tom____volatile+ "\" ":"") + 
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.TRANSIENT).getDefaultValue() != tom____transient?"transient=\"" + tom____transient+ "\" ":"") + 
        (tom___defaultValueLiteral!= null?"defaultValueLiteral=\""+tom___defaultValueLiteral+ "\" ":"") +
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.UNSETTABLE).getDefaultValue() != tom___unsettable?"unsettable=\"" + tom___unsettable+ "\" ":"") + 
        ((Boolean)tom___r.eClass().getEStructuralFeature(EcoreMappingToXMI.DERIVED).getDefaultValue() != tom___derived?"derived=\"" + tom___derived+ "\" ":"") + 
        ((Boolean)tom___r.eClass().getEStructuralFeature("containment").getDefaultValue() != tom___containment?"containment=\"" + tom___containment+ "\" ":"") + 
        ((Boolean)tom___r.eClass().getEStructuralFeature("resolveProxies").getDefaultValue() != tom___resolveProxies?"resolveProxies=\"" + tom___resolveProxies+ "\" ":"") + 
        (tom___eOpposite!= null?"eOpposite=\"#//" + tom___eType.getName() + "/" + tom___eOpposite.getName() + "\" ":"") + 
        (sb2.length()>0?"eKeys=\"" + sb2 + "\" ":"") + 
        ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EReference )eo).eGet((( org.eclipse.emf.ecore.EReference )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        if(tom___eGenericType!= null && (tom___eGenericType.getEClassifier() != tom___eType|| tom___eType.getETypeParameters().size() >0)){
          sb.append(parse(tom___eGenericType, false));
        }
        sb.append("</eStructuralFeatures>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EGenericType to match
   * @param argument to set if eo is an argument
   * @return XML string matching the object
   */
  
  public static String parse(EGenericType eo, boolean argument){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EGenericType ) {if ( (( org.eclipse.emf.ecore.EGenericType )eo) instanceof org.eclipse.emf.ecore.EGenericType ) { org.eclipse.emf.ecore.EClassifier  tom___eClassifier= (org.eclipse.emf.ecore.EClassifier)(( org.eclipse.emf.ecore.EGenericType )eo).eGet((( org.eclipse.emf.ecore.EGenericType )eo).eClass().getEStructuralFeature("eClassifier")) ;

        sb.append((!argument?"<eGenericType ":"<eTypeArguments ") + 
            (tom___eClassifier!= null?"eClassifier=\"#//" + tom___eClassifier.getName() + "\"":"") + 
            ">");
        sb.append(parse( (org.eclipse.emf.ecore.EGenericType)(( org.eclipse.emf.ecore.EGenericType )eo).eGet((( org.eclipse.emf.ecore.EGenericType )eo).eClass().getEStructuralFeature("eUpperBound")) , false));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EGenericType>)(( org.eclipse.emf.ecore.EGenericType )eo).eGet((( org.eclipse.emf.ecore.EGenericType )eo).eClass().getEStructuralFeature("eTypeArguments")) ));
        sb.append(parse( (org.eclipse.emf.ecore.EGenericType)(( org.eclipse.emf.ecore.EGenericType )eo).eGet((( org.eclipse.emf.ecore.EGenericType )eo).eClass().getEStructuralFeature("eLowerBound")) , false));
        sb.append(parse( (org.eclipse.emf.ecore.ETypeParameter)(( org.eclipse.emf.ecore.EGenericType )eo).eGet((( org.eclipse.emf.ecore.EGenericType )eo).eClass().getEStructuralFeature("eTypeParameter")) ));
        sb.append(!argument?"</eGenericType>":"</eTypeArguments>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EOperation to match
   * @return XML string matching the object
   */
  
  public static String parse(EOperation eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EOperation ) {if ( (( org.eclipse.emf.ecore.EOperation )eo) instanceof org.eclipse.emf.ecore.EOperation ) { boolean  tom___ordered= (java.lang.Boolean)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("ordered")) ; boolean  tom___unique= (java.lang.Boolean)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("unique")) ; int  tom___lowerBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("lowerBound")) ; int  tom___upperBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("upperBound")) ; org.eclipse.emf.ecore.EClassifier  tom___eType= (org.eclipse.emf.ecore.EClassifier)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eType")) ; org.eclipse.emf.ecore.EGenericType  tom___eGenericType= (org.eclipse.emf.ecore.EGenericType)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eGenericType")) ; org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>  tom___eExceptions= (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eExceptions")) ; org.eclipse.emf.ecore.EOperation  tom___o=(( org.eclipse.emf.ecore.EOperation )eo);

        StringBuffer sb2 = new StringBuffer();
        { /* unamed block */{ /* unamed block */if ( tom___eExceptions instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)tom___eExceptions).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)tom___eExceptions).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier>)tom___eExceptions).get(0) instanceof org.eclipse.emf.ecore.EClassifier)) ) {if ( (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions) instanceof org.eclipse.emf.common.util.EList<?> && ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions).size() == 0 || ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions).size()>0 && (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions).get(0) instanceof org.eclipse.emf.ecore.EClassifier)) ) {int tomMatch12_end_5=0;do {{ /* unamed block */if (!(tomMatch12_end_5 >=  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions).size() )) {

              sb2.append("#//" +  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions).get(tomMatch12_end_5) .getName() + " ");
            }tomMatch12_end_5=tomMatch12_end_5 + 1;}} while(!(tomMatch12_end_5 >  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EClassifier> )tom___eExceptions).size() ));}}}}

        sb.append("<eOperations " +
            "name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("name")) + "\" " +
            ((Boolean)tom___o.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != tom___ordered?"ordered=\"" + tom___ordered+ "\" ":"") + 
            ((Boolean)tom___o.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != tom___unique?"unique=\"" + tom___unique+ "\" ":"") + 
            ((Integer)tom___o.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != tom___lowerBound?"lowerBound=\"" + tom___lowerBound+ "\" ":"") + 
            ((Integer)tom___o.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != tom___upperBound?"upperBound=\"" + tom___upperBound+ "\" ":"") + 
            (tom___eType!= null&&tom___eType.getETypeParameters().size() == 0?"eType=\"#//" + tom___eType.getName() + "\" ":"") +
            (sb2.length() > 0?"eExceptions=\"" + sb2 + "\" ":"") + 
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        if(tom___eGenericType!= null && (tom___eGenericType.getEClassifier() != tom___eType|| tom___eType.getETypeParameters().size() >0)){
          sb.append(parse(tom___eGenericType, false));
        }
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.ETypeParameter>)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eTypeParameters")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EParameter>)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eParameters")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EGenericType>)(( org.eclipse.emf.ecore.EOperation )eo).eGet((( org.eclipse.emf.ecore.EOperation )eo).eClass().getEStructuralFeature("eGenericExceptions")) ));
        sb.append("</eOperations>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EParameter to match
   * @return XML string matching the object
   */
  
  public static String parse(EParameter eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EParameter ) {if ( (( org.eclipse.emf.ecore.EParameter )eo) instanceof org.eclipse.emf.ecore.EParameter ) { boolean  tom___ordered= (java.lang.Boolean)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("ordered")) ; boolean  tom___unique= (java.lang.Boolean)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("unique")) ; int  tom___lowerBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("lowerBound")) ; int  tom___upperBound= (java.lang.Integer)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("upperBound")) ; org.eclipse.emf.ecore.EClassifier  tom___eType= (org.eclipse.emf.ecore.EClassifier)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("eType")) ; org.eclipse.emf.ecore.EGenericType  tom___eGenericType= (org.eclipse.emf.ecore.EGenericType)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("eGenericType")) ; org.eclipse.emf.ecore.EParameter  tom___p=(( org.eclipse.emf.ecore.EParameter )eo);

        sb.append("<eParameters name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("name")) + "\" "+
            ((Boolean)tom___p.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != tom___ordered?"ordered=\"" + tom___ordered+ "\" ":"") + 
            ((Boolean)tom___p.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != tom___unique?"unique=\"" + tom___unique+ "\" ":"") + 
            ((Integer)tom___p.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != tom___lowerBound?"lowerBound=\"" + tom___lowerBound+ "\" ":"") + 
            ((Integer)tom___p.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != tom___upperBound?"upperBound=\"" + tom___upperBound+ "\" ":"") + 
            (tom___eType!= null&&tom___eType.getETypeParameters().size() == 0?"eType=\"#//" + tom___eType.getName() + "\" ":"") +
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EParameter )eo).eGet((( org.eclipse.emf.ecore.EParameter )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        if(tom___eGenericType!= null && (tom___eGenericType.getEClassifier() != tom___eType|| tom___eType.getETypeParameters().size() >0)){
            sb.append(parse(tom___eGenericType, false));
          }
        sb.append("</eParameters>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo ETypeParameter to match
   * @return XML string matching the object
   */
  
  public static String parse(ETypeParameter eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.ETypeParameter ) {if ( (( org.eclipse.emf.ecore.ETypeParameter )eo) instanceof org.eclipse.emf.ecore.ETypeParameter ) {

        sb.append("<eTypeParameters name=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.ETypeParameter )eo).eGet((( org.eclipse.emf.ecore.ETypeParameter )eo).eClass().getEStructuralFeature("name")) + "\">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.ETypeParameter )eo).eGet((( org.eclipse.emf.ecore.ETypeParameter )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EGenericType>)(( org.eclipse.emf.ecore.ETypeParameter )eo).eGet((( org.eclipse.emf.ecore.ETypeParameter )eo).eClass().getEStructuralFeature("eBounds")) ));
        sb.append("</eTypeParameters>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EEnumLiteral to match
   * @return XML string matching the object
   */
  
  public static String parse(EEnumLiteral eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof org.eclipse.emf.ecore.EEnumLiteral ) {if ( (( org.eclipse.emf.ecore.EEnumLiteral )eo) instanceof org.eclipse.emf.ecore.EEnumLiteral ) { String  tom___name= (java.lang.String)(( org.eclipse.emf.ecore.EEnumLiteral )eo).eGet((( org.eclipse.emf.ecore.EEnumLiteral )eo).eClass().getEStructuralFeature("name")) ; int  tom___value= (java.lang.Integer)(( org.eclipse.emf.ecore.EEnumLiteral )eo).eGet((( org.eclipse.emf.ecore.EEnumLiteral )eo).eClass().getEStructuralFeature("value")) ; String  tom___literal= (java.lang.String)(( org.eclipse.emf.ecore.EEnumLiteral )eo).eGet((( org.eclipse.emf.ecore.EEnumLiteral )eo).eClass().getEStructuralFeature("literal")) ;

        sb.append("<eLiterals " +
            "name=\"" + tom___name+ "\" " +
            ((Integer)(( org.eclipse.emf.ecore.EEnumLiteral )eo).eClass().getEStructuralFeature("value").getDefaultValue() != tom___value?"value=\"" + tom___value+ "\" " : "") +
            (!tom___literal.equals(tom___name) ? "literal=\"" + tom___literal+ "\" " : "") +
            ">");
        sb.append(parse( (org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EAnnotation>)(( org.eclipse.emf.ecore.EEnumLiteral )eo).eGet((( org.eclipse.emf.ecore.EEnumLiteral )eo).eClass().getEStructuralFeature("eAnnotations")) ));
        sb.append("</eLiterals>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo Entry to match
   * @return XML string matching the object
   */
  
  public static String parse(java.util.Map.Entry<String, String> eo){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( eo instanceof java.util.Map.Entry<?, ?> ) {if ( (( org.eclipse.emf.ecore.EObject )eo) instanceof java.util.Map.Entry<?, ?> ) {

        sb.append("<details key=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EObject )eo).eGet((( org.eclipse.emf.ecore.EObject )eo).eClass().getEStructuralFeature("key")) + "\" value=\"" +  (java.lang.String)(( org.eclipse.emf.ecore.EObject )eo).eGet((( org.eclipse.emf.ecore.EObject )eo).eClass().getEStructuralFeature("value")) + "\"/>");
      }}}}

    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EClassifier to match
   * @return XML string matching the object
   */
  
  public static String parse(EClassifier eo){
    StringBuffer sb=new StringBuffer();
    if(eo instanceof EClass){
      sb.append(parse((EClass)eo));
    }else if(eo instanceof EEnum){
      sb.append(parse((EEnum)eo));
    }else if(eo instanceof EDataType){
      sb.append(parse((EDataType)eo));
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EList to match
   * @return XML string matching the object
   */
  
  public static String parse(EList<?> el){
    StringBuffer sb=new StringBuffer();
    { /* unamed block */{ /* unamed block */if ( el instanceof org.eclipse.emf.common.util.EList<?> && (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)el).size() == 0 || (((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)el).size()>0 && ((org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject>)el).get(0) instanceof org.eclipse.emf.ecore.EObject)) ) {if ( (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el) instanceof org.eclipse.emf.common.util.EList<?> && ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el).size() == 0 || ((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el).size()>0 && (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el).get(0) instanceof org.eclipse.emf.ecore.EObject)) ) {int tomMatch17_2=0;if (!(tomMatch17_2 >=  (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el).size() )) { org.eclipse.emf.ecore.EObject  tom___o= (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el).get(tomMatch17_2) ;


        if((tom___o) instanceof EClassifier){
          sb.append(parse((EClassifier)tom___o));
        }else if((tom___o) instanceof EAnnotation){
          sb.append(parse((EAnnotation)tom___o));
        }else if((tom___o) instanceof EAttribute){
          sb.append(parse((EAttribute)tom___o));
        }else if((tom___o) instanceof EReference){
          sb.append(parse((EReference)tom___o));
        }else if((tom___o) instanceof EOperation){
          sb.append(parse((EOperation)tom___o));
        }else if((tom___o) instanceof EParameter){
          sb.append(parse((EParameter)tom___o));
        }else if((tom___o) instanceof EGenericType){
          sb.append(parse((EGenericType)tom___o, true));
        }else if((tom___o) instanceof java.util.Map.Entry<?,?>){
          sb.append(parse((java.util.Map.Entry<String,String>)tom___o));
        }else if((tom___o) instanceof ETypeParameter){
          sb.append(parse((ETypeParameter)tom___o));
        }else if((tom___o) instanceof EPackage){
          sb.append(parse((EPackage)tom___o,true));
        }else if((tom___o) instanceof EEnumLiteral){
          sb.append(parse((EEnumLiteral)tom___o));
        }else{
          throw new IllegalArgumentException("Unknown list element type : \n"+tom___o.toString());
        }
        sb.append(parse(tom_get_slice_EObjectEList((( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el),tomMatch17_2 + 1, (( org.eclipse.emf.common.util.EList<org.eclipse.emf.ecore.EObject> )el).size() )));
      }}}}}

    return sb.toString();
  }
  
  /**
   * An error manager showing possible XML errors
   */

  public static class ErrorManager implements ErrorHandler{
    public void warning(SAXParseException exception) throws SAXException{
      throw exception;
    }
    public void error(SAXParseException exception) throws SAXException{
      throw exception;
    }
    public void fatalError(SAXParseException exception) throws SAXException{
      throw exception;
    }
  }
  
  public static void main(String [] args) {
    if(args.length != 1) {
      System.out.println("usage : java TomMappingFromEcore <EPackageClassName>");
      System.exit(1);
    }
    String xml ="";
    try {
      EPackage p2 = (EPackage) Class.forName(args[0]).getField("eINSTANCE").get(
              null);
      
      xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + parse(p2, false);
      
      DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
      DocumentBuilder constructeur = fabrique.newDocumentBuilder();
      constructeur.setErrorHandler(new ErrorManager());
      
      Document document = constructeur.parse(new InputSource(new StringReader(xml)));
  
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
  
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(document);
      transformer.transform(source, result);
  
      String xmlString = result.getWriter().toString();
      System.out.println(xmlString);
    } catch(Exception e) {
      e.printStackTrace();
      System.err.println(xml.replaceAll(">", ">\n"));
    }

  }
}
