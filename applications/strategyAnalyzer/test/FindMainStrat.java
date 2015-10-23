import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindMainStrat{
	

	private String className = "";

	FindMainStrat(String name) {
		this.className = name;
	}

	public Object run(Object t) {
		try{
			Class<?> c = Class.forName(this.className);
			Method[] allMethods = c.getDeclaredMethods();
			Pattern p = Pattern.compile(".*mainStrat.*");
			Pattern encodeToMetalevel = Pattern.compile(".*encode.*");
			Pattern decodeFromMetalevel = Pattern.compile(".*decode.*");
			List<Method> matchingMethod = new ArrayList<Method>();
			List<Method> encodeMethod = new ArrayList<Method>();
			List<Method> decodeMethod = new ArrayList<Method>();
			for(Method m: allMethods) {
				if(p.matcher(m.getName()).matches()){
					matchingMethod.add(m);
				}	
				if(encodeToMetalevel.matcher(m.getName()).matches()){
					encodeMethod.add(m);
				}
				if(decodeFromMetalevel.matcher(m.getName()).matches()){
					decodeMethod.add(m);
				}
			}
		
			try{
				matchingMethod.get(0).setAccessible(true);
				Object res = t;
				if(!encodeMethod.isEmpty()) {
					encodeMethod.get(0).setAccessible(true);
					decodeMethod.get(0).setAccessible(true);
					res = encodeMethod.get(0).invoke(null, encodeMethod.get(0).getParameterTypes()[0].cast(res));		
					res = matchingMethod.get(0).invoke(null, matchingMethod.get(0).getParameterTypes()[0].cast(res));
					res = decodeMethod.get(0).invoke(null, decodeMethod.get(0).getParameterTypes()[0].cast(res));
					encodeMethod.get(0).setAccessible(false);
					decodeMethod.get(0).setAccessible(false);
					matchingMethod.get(0).setAccessible(false);
					return res;
				}
				else {	
					res = matchingMethod.get(0).invoke(null, matchingMethod.get(0).getParameterTypes()[0].cast(res));
					matchingMethod.get(0).setAccessible(false);
					return res;
				}
			}
			catch (Exception e){
				 e.printStackTrace();
	 			 return null;
			}

		}
		catch (ClassNotFoundException e) {
	 	   e.printStackTrace();
	  	  return null;
	    
		}
	
    	}
}
