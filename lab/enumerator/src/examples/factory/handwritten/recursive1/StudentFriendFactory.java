package examples.factory.handwritten.recursive1;

import java.math.BigInteger;
import examples.factory.handwritten.recursive1.StudentFriend;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public class StudentFriendFactory {
    
    public static final Enumeration<StudentFriend> getEnumeration() {

        final Enumeration<StudentFriend> emptyEnum = Enumeration.singleton(null);
        
        Enumeration<StudentFriend> finalEnum = null;
        
        final F<Integer, F<StudentFriend, StudentFriend>> cons1 = new F<Integer, F<StudentFriend, StudentFriend>>() {
            public F<StudentFriend, StudentFriend> apply(final Integer cons1_arg0) { return new F<StudentFriend, StudentFriend>() {public StudentFriend apply(final StudentFriend cons1_arg1) { return new StudentFriend(cons1_arg0, cons1_arg1); } }; }
        };
        
        final Enumeration<Integer> cons1_arg0Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(8)));
        
        F<Enumeration<StudentFriend>, Enumeration<StudentFriend>> sfFun = new F<Enumeration<StudentFriend>, Enumeration<StudentFriend>>() {
            public Enumeration<StudentFriend> apply(final Enumeration<StudentFriend> t) {
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
        
        Enumeration<StudentFriend> cons1Enum = Enumeration.fix(sfFun);
        
        finalEnum = cons1Enum;
        
        return finalEnum;
    }
}
