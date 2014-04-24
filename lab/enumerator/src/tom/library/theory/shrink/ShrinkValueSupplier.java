package tom.library.theory.shrink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.theory.TomCheck;

public class ShrinkValueSupplier {
//	private List<Object> counterExamples;
	
//	public ShrinkValueSupplier(Object... counterExamples) {
//		this.counterExamples = Arrays.asList(counterExamples);
//	}
	
	public List<PotentialAssignment> getNextPotentialSources(ParameterSignature parameter, Object cex) throws Throwable {
			return getReducedTermsAsSources(parameter, cex);
	}
	
	protected List<PotentialAssignment> getReducedTermsAsSources(ParameterSignature parameter, Object term) throws Throwable {
		return TermReducer.build(term, getInitializedEnumeration(parameter)).getValueSources();
	}

	protected Enumeration<?> getInitializedEnumeration(ParameterSignature parameter) {
		return TomCheck.get(parameter.getType());
	}
	
//	protected boolean isFixPoint(List<Object> params) {
//		boolean fp = true;
//		for (int i = 0; i < params.size(); i++) {
//			if (!params.get(i).equals(counterExamples.get(i))) {
//				fp = false;
//				break;
//			}
//		}
//		return fp;
//	}
//	
//	public List<Object> getCounterExamples() {
//		return counterExamples;
//	}

//	public void setCounterExamples(List<Object> counterExamples) {
////		fixPoint = isFixPoint(counterExamples);
//		this.counterExamples = counterExamples;
//	}
//	
//	public void setCounterExamples(Object... counterExamples) {
////		fixPoint = isFixPoint(Arrays.asList(counterExamples));
//		this.counterExamples = Arrays.asList(counterExamples);
//	}
}
