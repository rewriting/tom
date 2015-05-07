
package examples.factory.handwritten.recursive3;

import java.math.BigInteger;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import examples.factory.handwritten.recursive3.Account;

public class AccountFactory {

    public static final Enumeration<Account>  getEnumeration() {
        
        final Enumeration<Account> emptyEnum = Enumeration.singleton(null);
        
        Enumeration<Account> finalEnum = null;
        
        final F<Integer, F<Address, Account>> cons1 = new F<Integer, F<Address, Account>>() {
            public F<Address, Account> apply(final Integer cons1_arg0) { return new F<Address, Account>() {public Account apply(final Address cons1_arg1) { return new Account(cons1_arg0, cons1_arg1); } }; }
        };
        
        final Enumeration<Integer> cons1_arg0Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(3)));
        
//        F<Enumeration<Address>, Enumeration<Account>> fba = new F<Enumeration<Address>, Enumeration<Account>>() {
//            public Enumeration<Account> apply(final Enumeration<Address> t) {
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
//        F<Enumeration<Address>, Enumeration<Address>> fbb = new F<Enumeration<Address>, Enumeration<Address>>() {
//            public Enumeration<Address> apply(final Enumeration<Address> t) {
//                return t.pay();
//            }
//        };
//        
//        F<Enumeration<Account>, Enumeration<Account>> faa = new F<Enumeration<Account>, Enumeration<Account>>() {
//            public Enumeration<Account> apply(final Enumeration<Account> t) {
//                return t.pay();
//            }
//        };
//        
//        Enumeration<Account> cons1Enum = Enumeration.fix2(fba, fbb, faa, faa);
        
        final Enumeration<Address> cons1_arg1Enum = AddressFactory.getEnumeration();
        
        Enumeration<Account> cons1Enum = Enumeration.apply(Enumeration.apply(Enumeration.singleton(cons1), cons1_arg0Enum), cons1_arg1Enum);
        
        finalEnum = cons1Enum;
        
        return finalEnum;
        
    }

}