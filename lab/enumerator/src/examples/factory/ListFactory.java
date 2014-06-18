package examples.factory;

import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

import java.util.ArrayList;
import java.util.List;

public class ListFactory {


	public static final <T> Enumeration<List<T>> getEnumeration(
			final Enumeration<T> enumeration) {
		
		Enumeration<List<T>> enumRes = null;
		
		final Enumeration<List<T>> emptyEnum = Enumeration.singleton((List<T>) new ArrayList<T>());

		final F<T, F<List<T>, List<T>>> _list = new F<T, F<List<T>, List<T>>>() {
					public F<List<T>, List<T>> apply(final T e) {
						return new F<List<T>, List<T>>(){
							public List<T> apply(final List<T> l) {
								List<T> list=new ArrayList<T>();
								list.add(e);
								list.addAll(l);
								return list;
							}
						};
			}
		};
		
		F<Enumeration<List<T>>, Enumeration<List<T>>> listEnum = new F<Enumeration<List<T>>, Enumeration<List<T>>>() {
			public Enumeration<List<T>> apply(final Enumeration<List<T>> t) {
				return emptyEnum.plus(
						Enumeration.apply(Enumeration.apply(
								Enumeration.singleton(_list), enumeration), t))
						.pay();
			}
		};
		enumRes=Enumeration.fix(listEnum);
		
		return enumRes;
	}

}
