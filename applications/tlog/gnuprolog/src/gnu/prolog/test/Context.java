package gnu.prolog.test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import gnu.prolog.database.PrologTextLoaderError;
import gnu.prolog.io.ParseException;
import gnu.prolog.io.ReadOptions;
import gnu.prolog.io.TermReader;
import gnu.prolog.term.AtomTerm;
import gnu.prolog.term.Term;
import gnu.prolog.term.VariableTerm;
import gnu.prolog.vm.Environment;
import gnu.prolog.vm.Interpreter;
import gnu.prolog.vm.PrologCode;
import gnu.prolog.vm.PrologException;
import gnu.prolog.vm.Interpreter.Goal;

public class Context {

	private Environment environment = null;
	private Interpreter interpreter = null;
	private ReadOptions readOptions = null;

	public Context() {
		environment = new Environment();
	}

	// initializes the current environment so it can execute a specific goal.
	private Goal initInterpreter(String goal) throws PrologException {
		interpreter = environment.createInterpreter();
		environment.runIntialization(interpreter);
		for (Iterator ierr = environment.getLoadingErrors().iterator(); ierr.hasNext();) {
			PrologTextLoaderError err = (PrologTextLoaderError) ierr.next();
			System.err.println(err);
		}
		StringReader srd = new StringReader(goal);
		TermReader trd = new TermReader(srd);
		readOptions = new ReadOptions();
		readOptions.operatorSet = environment.getOperatorSet();
		Term goalTerm;
		try {
			goalTerm = trd.readTermEof(readOptions);
			return interpreter.prepareGoal(goalTerm);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addAtom(String atom) {
		environment.ensureLoaded(atom);
	}

	public boolean ask(String goal) throws PrologException {
		Goal intGoal = initInterpreter(goal);
		int result = interpreter.execute(intGoal);
		if (result == PrologCode.SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

	public LinkedList<Map<String, String>> askForSubstitutions(String goal, int depth) throws PrologException {
		Goal intGoal = initInterpreter(goal);
		LinkedList<Map<String, String>> subsList = new LinkedList<Map<String, String>>();
		boolean next = true;
		int subsFound = 0;
		while (next) {
			int result;
			try {
				result = interpreter.execute(intGoal);
			} catch (StackOverflowError err){
				//XXX Stops execution when algorithm recurses too deeply
				return subsList;
			}
			if (result == PrologCode.FAIL || subsFound >= depth) {
				next = false;
			} else {
				Iterator ivars = readOptions.variableNames.keySet().iterator();
				Map<String, String> subs = new HashMap<String, String>();
				while (ivars.hasNext()) {
					String name = (String) ivars.next();
					VariableTerm term = ((VariableTerm) readOptions.variableNames.get(name));
					AtomTerm term2 = (AtomTerm) term.dereference();
					subs.put(name, term2.value);
				}
				subsList.add(subs);
				if (result == PrologCode.SUCCESS_LAST) {
					next = false;
				}
			}
			subsFound++;
		}
		return subsList;
	}

	public LinkedList<Map<String, String>> askForSubstitutions(String goal) throws PrologException {
		return askForSubstitutions(goal, Integer.MAX_VALUE);
	}

}
