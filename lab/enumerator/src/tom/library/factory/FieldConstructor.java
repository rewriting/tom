package tom.library.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public class FieldConstructor {
	private List<Annotation> annotation;
	private Class typeChamp;
	private Type typePamaretered;
	private Type genericParameterType;
	
	public FieldConstructor(Class<?> typeChamp){
		this.typeChamp=typeChamp;
	}
	
	public FieldConstructor(List<Annotation> annotation, Class<?> typeChamp,Type typePamaretered,Type genericParameterType) {
		super();
		this.annotation = annotation;
		this.typeChamp = typeChamp;
		this.typePamaretered=typePamaretered;
		this.genericParameterType=genericParameterType;
	}
	public Type getTypePamaretered() {
		return typePamaretered;
	}
	public void setTypePamaretered(Type typePamaretered) {
		this.typePamaretered = typePamaretered;
	}
	public Type getGenericParameterType() {
		return genericParameterType;
	}
	public void setGenericParameterType(Type genericParameterType) {
		this.genericParameterType = genericParameterType;
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
				+ typeChamp + ", typePamaretered="
				+ typePamaretered + ", genericParameterType="
				+ genericParameterType + "]";
	}


	
	
}
