
package examples.factory.handwritten.recursive3;

import java.math.BigInteger;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import examples.factory.handwritten.recursive3.User;

public class UserFactory {

    public static final Enumeration<User>  getEnumeration() {
        
        final Enumeration<User> emptyEnum = Enumeration.singleton(null);
        
        Enumeration<User> finalEnum = null;
        
        final F<Integer, F<Account, User>> cons1 = new F<Integer, F<Account, User>>() {
            public F<Account, User> apply(final Integer cons1_arg0) { return new F<Account, User>() {public User apply(final Account cons1_arg1) { return new User(cons1_arg0, cons1_arg1); } }; }
        };
        
        final Enumeration<Integer> cons1_arg0Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(2)));
        
//        F<Enumeration<Account>, Enumeration<User>> fba = new F<Enumeration<Account>, Enumeration<User>>() {
//            public Enumeration<User> apply(final Enumeration<Account> t) {
//                return emptyEnum.plus(
//                    Enumeration.apply(
//                        Enumeration.apply(
//                            Enumeration.singleton(cons1),
//                            cons1_arg0Enum
//                        ),
//                        t
//                    )
//                )
//                .pay();
//            }
//        };
//        
//        F<Enumeration<Account>, Enumeration<Account>> fbb = new F<Enumeration<Account>, Enumeration<Account>>() {
//            public Enumeration<Account> apply(final Enumeration<Account> t) {
//                return t.pay();
//            }
//        };
//        
//        F<Enumeration<User>, Enumeration<User>> faa = new F<Enumeration<User>, Enumeration<User>>() {
//            public Enumeration<User> apply(final Enumeration<User> t) {
//                return t.pay();
//            }
//        };
//        
//        Enumeration<User> cons1Enum = Enumeration.fix2(fba, fbb, faa, faa);
        
        final Enumeration<Account> cons1_arg1Enum = AccountFactory.getEnumeration();
        
        Enumeration<User> cons1Enum = Enumeration.apply(Enumeration.apply(Enumeration.singleton(cons1), cons1_arg0Enum), cons1_arg1Enum);
        
        finalEnum = cons1Enum;
        
        return finalEnum;
        
    }

}