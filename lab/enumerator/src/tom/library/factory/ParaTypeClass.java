package tom.library.factory;

public class ParaTypeClass extends ParaType{
	private Class<?> classType;
	public ParaTypeClass(Class<?> classType){
		this.classType=classType;
	}
	@Override
	public String getStringClass() {
		// TODO Auto-generated method stub
		if(Tools.isPrimitive(classType)){
			return Tools.refactoringOfPrimitive(classType);
		}
		StringBuilder sb=new StringBuilder();
		sb.append(classType.getCanonicalName());
		if(paraTypes.size()>0){
			sb.append("<");
			int i=0;
			for(ParaType pt:paraTypes){
				sb.append(pt.getStringClass());
				if(i<paraTypes.size()-1){
					sb.append(",");
				}
				i++;
			}
			
			sb.append(">");
		}
		
		return sb+"";
	}
	public Class<?> getClassType() {
		return classType;
	}
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
}