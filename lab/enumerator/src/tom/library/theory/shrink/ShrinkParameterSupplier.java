package tom.library.theory.shrink;

import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.PotentialAssignment;

public interface ShrinkParameterSupplier {
	public List<PotentialAssignment> getValueSources(ParameterSignature signature, Object counterExample);
}
