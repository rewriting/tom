package examples.factory;
import java.math.BigInteger;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class StudentFactory {

		public static final Enumeration<Student> getEnumeration() {
			boolean canBeNull = false;
			// if(@Generator(canBeNull)){
			     canBeNull = true;
		    // }
			return getEnumeration(canBeNull);
		}

		public static final Enumeration<Student> getEnumeration(boolean canBeNull) {

			Enumeration<Student> enumRes = null;

			// F< arg1, F< arg2, ... F <argn, Student>...>
			final F<Integer, F<String, Student>> _student = new F<Integer, F<String, Student>>() {
				public F<String, Student> apply(final Integer n) {
					// F< arg2, ... F <argn, Student>...>
					return new F<String, Student>() {
						public Student apply(final String c) {
							return new Student(n, c);
						}
					};
				}
			};

			// //@Enumerate(maxSize=4)
			// int no,
			final Enumeration<Integer> noEnum = new Enumeration<Integer>(
					Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));

			// //@Enumerate
			// String String(maxSize=2)
			Enumeration<String> stringEnum = new Enumeration<String>(
					Combinators.makeString().parts().take(BigInteger.valueOf(3)));
			
			// as many apply as number of arguments
			enumRes = Enumeration.apply(
					Enumeration.apply(Enumeration.singleton(_student), noEnum),
					stringEnum);

			if (canBeNull) {
				final Enumeration<Student> emptyEnum = Enumeration.singleton(null);
				enumRes = emptyEnum.plus(enumRes);
			}

			return enumRes;
		}

	}
