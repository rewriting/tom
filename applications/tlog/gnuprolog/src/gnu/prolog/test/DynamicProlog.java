package gnu.prolog.test;

import java.util.LinkedList;
import java.util.Map;

import gnu.prolog.vm.PrologException;


public class DynamicProlog {

	public static void main(String args[]) {

		Context context = new Context();

		context.addAtom("arc(a,b).");
		context.addAtom("arc(a,c).");
		context.addAtom("arc(b,c).");
		context.addAtom("arc(b,d).");
		context.addAtom("arc(X,Z) :- arc(X,Y), arc(Y,Z).");

		String goalToRun = "arc(X,Y)";
		try {
			LinkedList<Map<String,String>> res = context.askForSubstitutions(goalToRun);
			for (Map<String,String> subs : res){
				for (String var : subs.keySet()){
					System.out.println(var+" = "+subs.get(var));
				}
				System.out.println();
			}
		} catch (PrologException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}

}
