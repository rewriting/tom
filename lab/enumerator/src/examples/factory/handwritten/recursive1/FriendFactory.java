package examples.factory.handwritten.recursive1;

import java.math.BigInteger;
import examples.factory.handwritten.recursive1.Friend;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class FriendFactory {
    
    public static final Enumeration<Friend> getEnumeration() {

        final Enumeration<Friend> emptyEnum = Enumeration.singleton(null);
        
        Enumeration<Friend> finalEnum = null;
        
        final F<Integer, F<Friend, Friend>> cons1 = new F<Integer, F<Friend, Friend>>() {
            public F<Friend, Friend> apply(final Integer cons1_arg0) { return new F<Friend, Friend>() {public Friend apply(final Friend cons1_arg1) { return new Friend(cons1_arg0, cons1_arg1); } }; }
        };
        
        final Enumeration<Integer> cons1_arg0Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(8)));
        
        F<Enumeration<Friend>, Enumeration<Friend>> sfFun = new F<Enumeration<Friend>, Enumeration<Friend>>() {
            public Enumeration<Friend> apply(final Enumeration<Friend> t) {
                return emptyEnum.plus(
                    Enumeration.apply(
                        Enumeration.apply(
                            Enumeration.singleton(cons1),
                            cons1_arg0Enum
                        ),
                        t
                    )
                )
                .pay();
            }
        };
        
        Enumeration<Friend> cons1Enum = Enumeration.fix(sfFun);
        
        finalEnum = cons1Enum;
        
        return finalEnum;
    }
}
