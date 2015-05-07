
package examples.factory.handwritten.recursive3;

import java.math.BigInteger;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import examples.factory.handwritten.recursive3.Address;

public class AddressFactory {

    public static final Enumeration<Address>  getEnumeration() {
        
        final Enumeration<Address> emptyEnum = Enumeration.singleton(null);
        final Enumeration<User> emptyEnumB = Enumeration.singleton(null);
        
        Enumeration<Address> finalEnum = null;
        
        final F<Integer, F<User, Address>> cons1BA = new F<Integer, F<User, Address>>() {
            public F<User, Address> apply(final Integer cons1_arg0) { return new F<User, Address>() {public Address apply(final User cons1_arg1) { return new Address(cons1_arg0, cons1_arg1); } }; }
        };
        
        final Enumeration<Integer> cons1_arg0Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(3)));
        
        F<Enumeration<User>, Enumeration<Address>> fba = new F<Enumeration<User>, Enumeration<Address>>() {
            public Enumeration<Address> apply(final Enumeration<User> t) {
                return emptyEnum.plus(
                    Enumeration.apply(
                        Enumeration.apply(
                            Enumeration.singleton(cons1BA),
                            cons1_arg0Enum
                        ),
                        t
                    )
                )
                .pay();
            }
        };
        
        F<Enumeration<User>, Enumeration<User>> fbb = new F<Enumeration<User>, Enumeration<User>>() {
            public Enumeration<User> apply(final Enumeration<User> t) {
                return t.pay();
            }
        };
        
        F<Enumeration<Address>, Enumeration<Address>> faa = new F<Enumeration<Address>, Enumeration<Address>>() {
            public Enumeration<Address> apply(final Enumeration<Address> t) {
                return t.pay();
            }
        };
        
        // fab!!!
        final F<Integer, F<Account, User>> cons1CB = new F<Integer, F<Account, User>>() {
            public F<Account, User> apply(final Integer cons1_arg0) { return new F<Account, User>() {public User apply(final Account cons1_arg1) { return new User(cons1_arg0, cons1_arg1); } }; }
        };
        
        final F<Integer, F<Address, Account>> cons1AC = new F<Integer, F<Address, Account>>() {
            public F<Address, Account> apply(final Integer cons1_arg0) { return new F<Address, Account>() {public Account apply(final Address cons1_arg1) { return new Account(cons1_arg0, cons1_arg1); } }; }
        };
        
        F<Enumeration<Address>, Enumeration<User>> fab = new F<Enumeration<Address>, Enumeration<User>>() {
            public Enumeration<User> apply(final Enumeration<Address> t) {
                return emptyEnumB.plus(
                    Enumeration.apply(
                        Enumeration.apply(
                            Enumeration.singleton(cons1CB),
                            cons1_arg0Enum
                        ),
                        Enumeration.apply(
                            Enumeration.apply(
                                Enumeration.singleton(cons1AC),
                                cons1_arg0Enum
                            ),
                            t
                        )
                    )
                )
                .pay();
            }
        };
        
//        Enumeration<Address> cons1Enum = Enumeration.fix2(fba, fbb, faa, faa);
        
        Enumeration<Address> cons1Enum = Enumeration.fixMultiple(fab, fbb, fba, faa, faa);
        
        finalEnum = cons1Enum;
        
        return finalEnum;
        
    }

}