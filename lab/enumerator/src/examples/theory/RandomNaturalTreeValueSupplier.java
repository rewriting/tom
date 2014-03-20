package examples.theory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.contrib.theories.ParameterSignature;
import org.junit.contrib.theories.ParameterSupplier;
import org.junit.contrib.theories.PotentialAssignment;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.F2;

public class RandomNaturalTreeValueSupplier extends ParameterSupplier {

	int samplesize = 100;

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature signature) {
		samplesize = signature.getAnnotation(ForAllNaturalTree.class).sampleSize();
		return treesOfNatural(samplesize);
	}

	public static final List<PotentialAssignment> treesOfNatural(int size) {
		final Enumeration<Nat> zeroEnum = Enumeration.singleton((Nat)new Zero());

		F<Enumeration<Nat>,Enumeration<Nat>> sucEnum = new F<Enumeration<Nat>,Enumeration<Nat>>() {
			public Enumeration<Nat> apply(final Enumeration<Nat> e) {
				F<Nat,Nat> _suc = new F<Nat,Nat>() { public Nat apply(Nat x) { return new Suc(x); } };
				return zeroEnum.plus(sucEnumAux(_suc, e)).pay();
			}
		};

		final Enumeration<Nat> natEnum = Enumeration.fix(sucEnum);
		F<Nat,Tree<Nat>> _leaf = new F<Nat,Tree<Nat>>() { public Tree<Nat> apply(Nat x) { return new Leaf<Nat>(x); } };
		final Enumeration<Tree<Nat>> leafEnum = Enumeration.apply(Enumeration.singleton(_leaf),natEnum);

		final F2<Tree<Nat>,Tree<Nat>,Tree<Nat>> _fork = new F2<Tree<Nat>,Tree<Nat>,Tree<Nat>>() { public Tree<Nat> apply(Tree<Nat> l, Tree<Nat> r) { return new Fork<Nat>(l,r); } };
		F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>> forkEnum = new F<Enumeration<Tree<Nat>>,Enumeration<Tree<Nat>>>() {
			public Enumeration<Tree<Nat>> apply(final Enumeration<Tree<Nat>> e) {
				return leafEnum.plus(Enumeration.apply(Enumeration.apply(Enumeration.singleton(_fork.curry()),e),e)).pay();
			}
		};
		final Enumeration<Tree<Nat>> treeEnum = Enumeration.fix(forkEnum);

		List<PotentialAssignment> l = new ArrayList<PotentialAssignment>();
		
		for (int i = 0; i < size; i++) {
			
			final BigInteger j = BigInteger.valueOf(i);

			PotentialAssignment assignment = new PotentialAssignment() {

				@Override
				public Object getValue() throws CouldNotGenerateValueException {
					return treeEnum.get(j);
				}

				@Override
				public String getDescription() throws CouldNotGenerateValueException {
					// TODO Auto-generated method stub
					return null;
				}
			};
			l.add(assignment);
		}
		return l;
	}

	private static Enumeration<Nat> sucEnumAux(F<Nat,Nat> suc, Enumeration<Nat> e) {
		Enumeration<F<Nat,Nat>> singletonSuc = Enumeration.singleton(suc);
		Enumeration<Nat> res = Enumeration.apply(singletonSuc,e);
		return res;
	}




}
