package examples.factory.generation;

import examples.factory.generation.Enumerate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public class FieldConstructor {
	private List<Annotation> annotation;
	private Class typeChamp;
	private String nameField;
	private Type typePamaretered;
	
	public FieldConstructor(List<Annotation> annotation, Class typeChamp,String nameField,Type typePamaretered) {
		super();
		this.annotation = annotation;
		this.typeChamp = typeChamp;
		this.nameField=nameField;
		this.typePamaretered=typePamaretered;
	}
	public String getNameField() {
		return nameField;
	}
	public void setNameField(String nameField) {
		this.nameField = nameField;
	}
	public List<Annotation> getAnnotation() {
		return annotation;
	}
	public void setAnnotation(List<Annotation> annotation) {
		this.annotation = annotation;
	}
	
	public Type getParameter() {
		return typePamaretered;
	}
	public void setParameter(Type parameter) {
		this.typePamaretered = typePamaretered;
	}
	public boolean containsAnnotation(Class annot){
		boolean b=false;
		int i=0;
		while(!b&&i<annotation.size()){
			b=annotation.get(i).annotationType().getSimpleName().equals(annot.getSimpleName());
			i++;
		}
		return b;
	}
	
	public Class getTypeChamp() {
		return typeChamp;
	}
	public void setTypeChamp(Class typeChamp) {
		this.typeChamp = typeChamp;
	}
	@Override
	public String toString() {
		return "FieldConstructor [annotation=" + annotation + ", typeChamp="
				+ typeChamp + ", nameField=" + nameField + ", parameter="
				+ typePamaretered + "]";
	}

	
	
}
