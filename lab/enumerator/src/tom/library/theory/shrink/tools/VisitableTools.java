package tom.library.theory.shrink.tools;

import tom.library.sl.Visitable;
import tom.library.sl.VisitableBuiltin;

public class VisitableTools {
	private static int size = 0;
	
	public static int size(Visitable term) {
		size = 0;
		if (term == null) {
			return Integer.MAX_VALUE;
		}
		if (term instanceof Visitable) {
			Visitable t = (Visitable) term;
			calculateSize(t);
		}
		return size;
	}
	
	private static void calculateSize(Visitable term) {
		if (term.getChildCount() == 0) {
			// calculate value
			size += calculateValue(term);
		} else {
			size ++;
		}
		for (Visitable v : term.getChildren()) {
			calculateSize(v);
		}
	}
	
	private static int calculateValue(Visitable term) {
		if (isValueInstanceOfInteger(term)) {
			return Math.abs(getValueFromTermInteger(term));
		} else if (isValueInstanceOfString(term)) {
			return getValueFromTermString(term).length();
		} else {
			return 1;
		}
	}
	
	public static boolean isValueInstanceOfInteger(Visitable term) {
		return castToVisitableBuiltin(term).getBuiltin() instanceof Integer;
	}
	
	public static boolean isValueInstanceOfString(Visitable term) {
		return castToVisitableBuiltin(term).getBuiltin() instanceof String;
	}
	
	public static String getValueFromTermString(Visitable term) {
		return (String) castToVisitableBuiltin(term).getBuiltin();
	}
	
	public static int getValueFromTermInteger(Visitable term) {
		return (Integer) castToVisitableBuiltin(term).getBuiltin(); 
	}
	
	private static VisitableBuiltin<?> castToVisitableBuiltin(Visitable term) {
		if (isInstanceOfVisitableBuiltin(term)) {
			return (VisitableBuiltin<?>) term;
		} else {
			return new VisitableBuiltin<Object>(null);
		}
	}
	
	private static boolean isInstanceOfVisitableBuiltin(Visitable term) {
		return term instanceof VisitableBuiltin<?>;
	}
	
	public static boolean isInstanceOfVisitable(Object term) {
		return term instanceof Visitable;
	}
}
