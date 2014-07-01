package examples.factory.generation;

import examples.factory.generation.Enumerate;
import examples.factory.generation.Generator;

import java.lang.annotation.Annotation;
import java.util.List;

public class FieldConstructor {
	private List<Annotation> annotation;
	private Class typeChamp;
	private String nameField;
	
	
	public FieldConstructor(List<Annotation> annotation, Class typeChamp,String nameField) {
		super();
		this.annotation = annotation;
		this.typeChamp = typeChamp;
		this.nameField=nameField;
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
				+ typeChamp + "]";
	}
	
	
}
