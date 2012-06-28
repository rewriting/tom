package fr.irisa.cairn.ecore.tools.prettyprint;

import java.util.List;

public class PrettyPrint {

	public static String asList(List<? extends Object> objs, String sep) {
		String res = "";
		boolean first = true;
		for (Object obj : objs) {
			if (!first) {
				res += sep;
			}
			res += obj.toString();
			first = false;
		}
		return res;

	}

}
