
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
import examples.factory.inheritance.Dog;

public class DogFactory {
    
    private static Enumeration<Dog> finalEnum = null;
    
    public static final Enumeration<Dog>  getEnumeration() {
        
        boolean singleton = false;
        int singletonSize = 0;
        
//////////////// generating constructors /////////////
        
        // constructor _cons1
        final F<String, F<Integer, Dog>> _cons1 = new F<String, F<Integer, Dog>>() {
            public F<Integer, Dog> apply(final String _cons1_arg0) { return new F<Integer, Dog>() {public examples.factory.inheritance.Dog apply(final Integer _cons1_arg1) { return new examples.factory.inheritance.Dog(_cons1_arg0, _cons1_arg1); } }; }
        };
        
        // constructor _cons1, param _cons1_arg0
        Enumeration<String> _cons1_arg0Enum = new Enumeration<String>(Combinators.makeString().parts().take(BigInteger.valueOf(8)));
        
        // constructor _cons1, param _cons1_arg1
        Enumeration<Integer> _cons1_arg1Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(8)));
        
        Enumeration<Dog> _cons1Enum = Enumeration.apply(Enumeration.apply(Enumeration.singleton(_cons1), _cons1_arg0Enum), _cons1_arg1Enum);
        
        // accumulating all constructors
        Enumeration<Dog> allConstructorsEnum = _cons1Enum;
        
        
        finalEnum = allConstructorsEnum;
        
        return finalEnum;
        
    }

}