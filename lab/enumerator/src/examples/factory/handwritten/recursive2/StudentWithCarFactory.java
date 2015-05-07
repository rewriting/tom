
package examples.factory.handwritten.recursive2;

import java.math.BigInteger;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import examples.factory.handwritten.recursive2.StudentCar;
import examples.factory.handwritten.recursive2.StudentWithCar;

public class StudentWithCarFactory {

    public static final Enumeration<StudentWithCar>  getEnumeration() {
        
        final Enumeration<StudentWithCar> emptyAEnum = Enumeration.singleton(null);
        final Enumeration<StudentCar> emptyBEnum = Enumeration.singleton(null);
        
        Enumeration<StudentWithCar> finalEnum = null;
        
        final F<Integer, F<String, F<StudentCar, StudentWithCar>>> Acons1 = new F<Integer, F<String, F<StudentCar, StudentWithCar>>>() {
            public F<String, F<StudentCar, StudentWithCar>> apply(final Integer cons1_arg0) { return new F<String, F<StudentCar, StudentWithCar>>() {public F<StudentCar, StudentWithCar> apply(final String cons1_arg1) { return new F<StudentCar, StudentWithCar>() {public StudentWithCar apply(final StudentCar cons1_arg2) { return new StudentWithCar(cons1_arg0, cons1_arg1, cons1_arg2); } }; } }; }
        };
        
        final F<Integer, F<StudentWithCar, StudentCar>> Bcons1 = new F<Integer, F<StudentWithCar, StudentCar>>() {
            public F<StudentWithCar, StudentCar> apply(final Integer cons1_arg0) { return new F<StudentWithCar, StudentCar>() {public StudentCar apply(final StudentWithCar cons1_arg1) { return new StudentCar(cons1_arg0, cons1_arg1); } }; }
        };
        
        final Enumeration<Integer> cons1_arg0Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(2)));
        final Enumeration<String> cons1_arg1Enum = new Enumeration<String>(Combinators.makeString().parts().take(BigInteger.valueOf(2)));
        
        // one f1BA
        F<Enumeration<StudentCar>, Enumeration<StudentWithCar>> f1BA = new F<Enumeration<StudentCar>, Enumeration<StudentWithCar>>() {
            public Enumeration<StudentWithCar> apply(final Enumeration<StudentCar> t) {
                return emptyAEnum.plus(
                    Enumeration.apply(
                        Enumeration.apply(
                            Enumeration.apply(
                                Enumeration.singleton(Acons1),
                                cons1_arg0Enum
                            ),
                            cons1_arg1Enum
                        ),
                        t
                    )
                )
                .pay();
            }
        };
        //two f1AB
        F<Enumeration<StudentWithCar>, Enumeration<StudentCar>> f1AB = new F<Enumeration<StudentWithCar>, Enumeration<StudentCar>>() {
            public Enumeration<StudentCar> apply(final Enumeration<StudentWithCar> t) {
                return emptyBEnum.plus(
                    Enumeration.apply(
                        Enumeration.apply(
                            Enumeration.singleton(Bcons1),
                            cons1_arg0Enum
                        ),
                        t
                    )
                )
                .pay();
            }
        };
        
        // three BB
        F<Enumeration<StudentCar>, Enumeration<StudentCar>> f1_B = new F<Enumeration<StudentCar>, Enumeration<StudentCar>>() {
            public Enumeration<StudentCar> apply(final Enumeration<StudentCar> t) {
                return t.pay();
            }
        };
        
        // four AA
        F<Enumeration<StudentWithCar>, Enumeration<StudentWithCar>> f1_A = new F<Enumeration<StudentWithCar>, Enumeration<StudentWithCar>>() {
            public Enumeration<StudentWithCar> apply(final Enumeration<StudentWithCar> t) {
                return t.pay();
            }
        };
        
        Enumeration<StudentWithCar> cons1Enum = Enumeration.fixMultiple(f1AB, f1_B, f1BA, f1_A, f1_A);
        
        finalEnum = cons1Enum;
        
        return finalEnum;
        
    }

}