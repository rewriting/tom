
/**
* This is auto generated class by the Apache velocity template engine
* The corresponding template is FactoryTemplate.vm
**/
package examples.factory.inheritance;

import java.math.BigInteger;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.enumerator.Finite;
import tom.library.enumerator.LazyList;
import tom.library.enumerator.P1;
import examples.factory.inheritance.Cat;

public class CatFactory {
    
    private static Enumeration<Cat> finalEnum = null;
    
    public static final Enumeration<Cat>  getEnumeration() {
        
        boolean singleton = false;
        int singletonSize = 0;
        
//////////////// generating constructors /////////////
        
        // constructor _cons1
        final F<String, F<Integer, Cat>> _cons1 = new F<String, F<Integer, Cat>>() {
            public F<Integer, Cat> apply(final String _cons1_arg0) { return new F<Integer, Cat>() {public examples.factory.inheritance.Cat apply(final Integer _cons1_arg1) { return new examples.factory.inheritance.Cat(_cons1_arg0, _cons1_arg1); } }; }
        };
        
        // constructor _cons1, param _cons1_arg0
        Enumeration<String> _cons1_arg0Enum = new Enumeration<String>(Combinators.makeString().parts().take(BigInteger.valueOf(8)));
        
        // constructor _cons1, param _cons1_arg1
        Enumeration<Integer> _cons1_arg1Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(8)));
        
        Enumeration<Cat> _cons1Enum = Enumeration.apply(Enumeration.apply(Enumeration.singleton(_cons1), _cons1_arg0Enum), _cons1_arg1Enum);
        
        // accumulating all constructors
        Enumeration<Cat> allConstructorsEnum = _cons1Enum;
        
        
        finalEnum = allConstructorsEnum;
        
        return finalEnum;
        
    }

}