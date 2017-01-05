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

  %include { emf/ecore.tom }
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
    %match(eo) {
      EPackage(eAnnotations, name, nsURI, nsPrefix, _, eClassifiers, eSubpackages) -> {
        sb.append("<"+(subpackage?"eSubpackages":"ecore:EPackage " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" " +
            "xsi:schemaLocation=\"http://www.eclipse.org/emf/2002/Ecore \" ") +
            "name=\"" + `name + "\" " +
            "nsURI=\"" + `nsURI + "\" " +
            "nsPrefix=\"" + `nsPrefix + "\" " +
            ">");
        sb.append(parse(`eAnnotations));
        sb.append(parse(`eClassifiers));
        sb.append(parse(`eSubpackages));
        sb.append("</ecore:EPackage>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EClass to match
   * @return XML string matching the object
   */
  
  public static String parse(EClass eo) {
    StringBuffer sb=new StringBuffer();
    %match(eo) {
      c@EClass(eAnnotations, name, instanceClassName, instanceTypeName, eTypeParameters, _abstract, _interface, eSuperTypes, eOperations, eStructuralFeatures, _) -> {
        StringBuffer sb2=new StringBuffer();
        %match(eSuperTypes){
          EClassEList(_*, EClass(_, name, _, _, _, _, _, _, _, _, _), _*) -> { sb2.append("#//" + `name + " "); }
        }
        sb.append("<eClassifiers " +
            "xsi:type=\"ecore:EClass\" " +
            "name=\"" + `name + "\" " + 
            (! EObject.class.isAssignableFrom(`c.getInstanceClass()) ? "instanceClassName=\"" + `instanceClassName + "\" " : "") + 
            (!`instanceClassName.equals(`instanceTypeName) ? "instanceTypeName=\"" + `instanceTypeName + "\" " : "") + 
            ((Boolean)`c.eClass().getEStructuralFeature("abstract").getDefaultValue() != `_abstract ? "abstract=\"" + `_abstract + "\" " : "") + 
            ((Boolean)`c.eClass().getEStructuralFeature("interface").getDefaultValue() != `_interface ? "interface=\"" + `_interface + "\" " : "") + 
            (sb2.length() > 0 ? "eSuperTypes=\"" + sb2 + "\" " : "") + 
            ">");
        sb.append(parse(`eAnnotations));
        sb.append(parse(`eTypeParameters));
        sb.append(parse(`eOperations));
        sb.append(parse(`eStructuralFeatures));
        sb.append("</eClassifiers>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EDataType to match
   * @return XML string matching the object
   */
  
  public static String parse(EDataType eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      d@EDataType(eAnnotations, name, instanceClassName, instanceTypeName, eTypeParameters, serializable) -> {
        sb.append("<eClassifiers " +
            "xsi:type=\"ecore:EDataType\" " +
            "name=\"" + `name + "\" " +
            "instanceClassName=\""+`instanceClassName+"\" " +
            (!`instanceClassName.equals(`instanceTypeName)?"instanceTypeName=\"" + `instanceTypeName + "\" ":"") + 
            ((Boolean)`d.eClass().getEStructuralFeature(EcoreMappingToXMI.SERIALIZABLE).getDefaultValue() != `serializable?"serializable=\"" + `serializable + "\" ":"") + 
            ">");
        sb.append(parse(`eAnnotations));
        sb.append(parse(`eTypeParameters));
        sb.append("</eClassifiers>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EEnum to match
   * @return XML string matching the object
   */
  
  public static String parse(EEnum eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      e@EEnum(eAnnotations, name, instanceClassName, instanceTypeName, eTypeParameters, serializable, eLiterals) -> {
        sb.append("<eClassifiers " +
            "xsi:type=\"ecore:EEnum\" " +
            "name=\"" + `name + "\" " +
            "instanceClassName=\""+`instanceClassName+"\" " +
            (!`instanceClassName.equals(`instanceTypeName)?"instanceTypeName=\"" + `instanceTypeName + "\" ":"") + 
            ((Boolean)`e.eClass().getEStructuralFeature(EcoreMappingToXMI.SERIALIZABLE).getDefaultValue() != `serializable?"serializable=\"" + `serializable + "\" ":"") + 
            ">");
        sb.append(parse(`eAnnotations));
        sb.append(parse(`eTypeParameters));
        sb.append(parse(`eLiterals));
        sb.append("</eClassifiers>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EAnnotation to match
   * @return XML string matching the object
   */
  
  public static String parse(EAnnotation eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      EAnnotation(eAnnotations, source, details, _, _, _) -> {
        sb.append("<eAnnotations" + (`source != null?" source=\""+`source+"\"":"") + ">");
        sb.append(parse(`eAnnotations));
        sb.append(parse(`details));
        sb.append("</eAnnotations>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EAttribute to match
   * @return XML string matching the object
   */
  
  public static String parse(EAttribute eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      a@EAttribute(eAnnotations, name, ordered, unique, lowerBound, upperBound, eType, eGenericType, changeable, _volatile, _transient, defaultValueLiteral, unsettable, derived, iD) -> {
        sb.append("<eStructuralFeatures xsi:type=\"ecore:EAttribute\" " + 
            "name=\"" + `name + "\" " + 
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != `ordered?"ordered=\"" + `ordered + "\" ":"") + 
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != `unique?"unique=\"" + `unique + "\" ":"") + 
            ((Integer)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != `lowerBound?"lowerBound=\"" + `lowerBound + "\" ":"") + 
            ((Integer)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != `upperBound?"upperBound=\"" + `upperBound + "\" ":"") + 
            (`eType != null&&`eType.getETypeParameters().size() == 0?"eType=\"#//" + `eType.getName() + "\" ":"") +
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.CHANGEABLE).getDefaultValue() != `changeable?"changeable=\"" + `changeable + "\" ":"") + 
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.VOLATILE).getDefaultValue() != `_volatile?"volatile=\"" + `_volatile + "\" ":"") + 
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.TRANSIENT).getDefaultValue() != `_transient?"transient=\"" + `_transient + "\" ":"") + 
            (`defaultValueLiteral != null?"defaultValueLiteral=\"" + `defaultValueLiteral + "\" ":"") + 
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.UNSETTABLE).getDefaultValue() != `unsettable?"unsettable=\"" + `unsettable + "\" ":"") + 
            ((Boolean)`a.eClass().getEStructuralFeature(EcoreMappingToXMI.DERIVED).getDefaultValue() != `derived?"derived=\"" + `derived + "\" ":"") + 
            ((Boolean)`a.eClass().getEStructuralFeature("iD").getDefaultValue() != `iD?"iD=\"" + `iD + "\" ":"") + 
            ">");
        sb.append(parse(`eAnnotations));
        if(`eGenericType != null && (`eGenericType.getEClassifier() != `eType || `eType.getETypeParameters().size() >0)){
          sb.append(parse(`eGenericType, false));
        }
        sb.append("</eStructuralFeatures>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EReference to match
   * @return XML string matching the object
   */
  
  public static String parse(EReference eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      r@EReference(eAnnotations, name, ordered, unique, lowerBound, upperBound, eType, eGenericType, changeable, _volatile, _transient, defaultValueLiteral, unsettable, derived, containment, resolveProxies, eOpposite, eKeys) -> {
        StringBuffer sb2=new StringBuffer();
      %match(eKeys){
        EAttributeEList(_*,EAttribute(_, namea, _, _, _, _, ec, _, _, _, _, _, _, _, _),_*) -> {
        sb2.append("#//" + (`ec).getName() + "/" + `namea + " ");
        }
      }
      sb.append("<eStructuralFeatures xsi:type=\"ecore:EReference\" " + 
        "name=\"" + `name + "\" " + 
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != `ordered?"ordered=\"" + `ordered + "\" ":"") + 
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != `unique?"unique=\"" + `unique + "\" ":"") + 
        ((Integer)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != `lowerBound?"lowerBound=\"" + `lowerBound + "\" ":"") + 
        ((Integer)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != `upperBound?"upperBound=\"" + `upperBound + "\" ":"") + 
        (`eType != null&&`eType.getETypeParameters().size() == 0?"eType=\"#//" + `eType.getName() + "\" ":"") +
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.CHANGEABLE).getDefaultValue() != `changeable?"changeable=\"" + `changeable + "\" ":"") + 
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.VOLATILE).getDefaultValue() != `_volatile?"volatile=\"" + `_volatile + "\" ":"") + 
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.TRANSIENT).getDefaultValue() != `_transient?"transient=\"" + `_transient + "\" ":"") + 
        (`defaultValueLiteral != null?"defaultValueLiteral=\""+`defaultValueLiteral + "\" ":"") +
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.UNSETTABLE).getDefaultValue() != `unsettable?"unsettable=\"" + `unsettable + "\" ":"") + 
        ((Boolean)`r.eClass().getEStructuralFeature(EcoreMappingToXMI.DERIVED).getDefaultValue() != `derived?"derived=\"" + `derived + "\" ":"") + 
        ((Boolean)`r.eClass().getEStructuralFeature("containment").getDefaultValue() != `containment?"containment=\"" + `containment + "\" ":"") + 
        ((Boolean)`r.eClass().getEStructuralFeature("resolveProxies").getDefaultValue() != `resolveProxies?"resolveProxies=\"" + `resolveProxies + "\" ":"") + 
        (`eOpposite != null?"eOpposite=\"#//" + `eType.getName() + "/" + `eOpposite.getName() + "\" ":"") + 
        (sb2.length()>0?"eKeys=\"" + sb2 + "\" ":"") + 
        ">");
        sb.append(parse(`eAnnotations));
        if(`eGenericType != null && (`eGenericType.getEClassifier() != `eType || `eType.getETypeParameters().size() >0)){
          sb.append(parse(`eGenericType, false));
        }
        sb.append("</eStructuralFeatures>");
      }
    }
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
    %match(eo){
      EGenericType(eUpperBound, eTypeArguments, eLowerBound, eTypeParameter, eClassifier) -> {
        sb.append((!argument?"<eGenericType ":"<eTypeArguments ") + 
            (`eClassifier != null?"eClassifier=\"#//" + `eClassifier.getName() + "\"":"") + 
            ">");
        sb.append(parse(`eUpperBound, false));
        sb.append(parse(`eTypeArguments));
        sb.append(parse(`eLowerBound, false));
        sb.append(parse(`eTypeParameter));
        sb.append(!argument?"</eGenericType>":"</eTypeArguments>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EOperation to match
   * @return XML string matching the object
   */
  
  public static String parse(EOperation eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      o@EOperation(eAnnotations, name, ordered, unique, lowerBound, upperBound, eType, eGenericType, eTypeParameters, eParameters, eExceptions, eGenericExceptions) -> {
        StringBuffer sb2 = new StringBuffer();
        %match(eExceptions){
            EClassifierEList(_*, ec, _*) -> {
              sb2.append("#//" + `ec.getName() + " ");
            }
        }
        sb.append("<eOperations " +
            "name=\"" + `name + "\" " +
            ((Boolean)`o.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != `ordered?"ordered=\"" + `ordered + "\" ":"") + 
            ((Boolean)`o.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != `unique?"unique=\"" + `unique + "\" ":"") + 
            ((Integer)`o.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != `lowerBound?"lowerBound=\"" + `lowerBound + "\" ":"") + 
            ((Integer)`o.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != `upperBound?"upperBound=\"" + `upperBound + "\" ":"") + 
            (`eType != null&&`eType.getETypeParameters().size() == 0?"eType=\"#//" + `eType.getName() + "\" ":"") +
            (sb2.length() > 0?"eExceptions=\"" + sb2 + "\" ":"") + 
            ">");
        sb.append(parse(`eAnnotations));
        if(`eGenericType != null && (`eGenericType.getEClassifier() != `eType || `eType.getETypeParameters().size() >0)){
          sb.append(parse(`eGenericType, false));
        }
        sb.append(parse(`eTypeParameters));
        sb.append(parse(`eParameters));
        sb.append(parse(`eGenericExceptions));
        sb.append("</eOperations>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EParameter to match
   * @return XML string matching the object
   */
  
  public static String parse(EParameter eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      p@EParameter(eAnnotations, name, ordered, unique, lowerBound, upperBound, eType, eGenericType) -> {
        sb.append("<eParameters name=\"" + `name + "\" "+
            ((Boolean)`p.eClass().getEStructuralFeature(EcoreMappingToXMI.ORDERED).getDefaultValue() != `ordered?"ordered=\"" + `ordered + "\" ":"") + 
            ((Boolean)`p.eClass().getEStructuralFeature(EcoreMappingToXMI.UNIQUE).getDefaultValue() != `unique?"unique=\"" + `unique + "\" ":"") + 
            ((Integer)`p.eClass().getEStructuralFeature(EcoreMappingToXMI.LOWERBOUND).getDefaultValue() != `lowerBound?"lowerBound=\"" + `lowerBound + "\" ":"") + 
            ((Integer)`p.eClass().getEStructuralFeature(EcoreMappingToXMI.UPPERBOUND).getDefaultValue() != `upperBound?"upperBound=\"" + `upperBound + "\" ":"") + 
            (`eType != null&&`eType.getETypeParameters().size() == 0?"eType=\"#//" + `eType.getName() + "\" ":"") +
            ">");
        sb.append(parse(`eAnnotations));
        if(`eGenericType != null && (`eGenericType.getEClassifier() != `eType || `eType.getETypeParameters().size() >0)){
            sb.append(parse(`eGenericType, false));
          }
        sb.append("</eParameters>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo ETypeParameter to match
   * @return XML string matching the object
   */
  
  public static String parse(ETypeParameter eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      ETypeParameter(eAnnotations, name, eBounds) -> {
        sb.append("<eTypeParameters name=\"" + `name + "\">");
        sb.append(parse(`eAnnotations));
        sb.append(parse(`eBounds));
        sb.append("</eTypeParameters>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo EEnumLiteral to match
   * @return XML string matching the object
   */
  
  public static String parse(EEnumLiteral eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      el@EEnumLiteral(eAnnotations, name, value, _, literal) -> {
        sb.append("<eLiterals " +
            "name=\"" + `name + "\" " +
            ((Integer)`el.eClass().getEStructuralFeature("value").getDefaultValue() != `value?"value=\"" + `value + "\" " : "") +
            (!`literal.equals(`name) ? "literal=\"" + `literal + "\" " : "") +
            ">");
        sb.append(parse(`eAnnotations));
        sb.append("</eLiterals>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Return XML string matching the object
   * @param eo Entry to match
   * @return XML string matching the object
   */
  
  public static String parse(java.util.Map.Entry<String, String> eo){
    StringBuffer sb=new StringBuffer();
    %match(eo){
      EStringToStringMapEntry(key, value) -> {
        sb.append("<details key=\"" + `key + "\" value=\"" + `value + "\"/>");
      }
    }
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
    %match(el){
      EObjectEList(o, l*) -> 
      {
        if((`o) instanceof EClassifier){
          sb.append(parse((EClassifier)`o));
        }else if((`o) instanceof EAnnotation){
          sb.append(parse((EAnnotation)`o));
        }else if((`o) instanceof EAttribute){
          sb.append(parse((EAttribute)`o));
        }else if((`o) instanceof EReference){
          sb.append(parse((EReference)`o));
        }else if((`o) instanceof EOperation){
          sb.append(parse((EOperation)`o));
        }else if((`o) instanceof EParameter){
          sb.append(parse((EParameter)`o));
        }else if((`o) instanceof EGenericType){
          sb.append(parse((EGenericType)`o, true));
        }else if((`o) instanceof java.util.Map.Entry<?,?>){
          sb.append(parse((java.util.Map.Entry<String,String>)`o));
        }else if((`o) instanceof ETypeParameter){
          sb.append(parse((ETypeParameter)`o));
        }else if((`o) instanceof EPackage){
          sb.append(parse((EPackage)`o,true));
        }else if((`o) instanceof EEnumLiteral){
          sb.append(parse((EEnumLiteral)`o));
        }else{
          throw new IllegalArgumentException("Unknown list element type : \n"+`o.toString());
        }
        sb.append(parse(`l));
      }
    }
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
