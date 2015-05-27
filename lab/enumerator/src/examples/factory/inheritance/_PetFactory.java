
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

public class PetFactory {
    
    private static Enumeration<Pet> finalEnum = null;
    
    public static final Enumeration<Pet>  getEnumeration() {
        
        final F<String, F<Integer, Pet>> _cons1 = new F<String, F<Integer, Pet>>() {
            public F<Integer, Pet> apply(final String _cons1_arg0) { 
                return new F<Integer, Pet>() {
                    public Dog apply(final Integer _cons1_arg1) { 
                        return new Dog(_cons1_arg0, _cons1_arg1); 
                    } 
                }; 
            }
        };
        
        // constructor _cons1, param _cons1_arg0
        Enumeration<String> _cons1_arg0Enum = new Enumeration<String>(Combinators.makeString().parts().take(BigInteger.valueOf(4)));
        // constructor _cons1, param _cons1_arg1
        Enumeration<Integer> _cons1_arg1Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));
        
        Enumeration<Pet> _cons1Enum = Enumeration.apply(Enumeration.apply(Enumeration.singleton(_cons1), _cons1_arg0Enum), _cons1_arg1Enum);
        
        final F<String, F<Integer, Pet>> _cons2 = new F<String, F<Integer, Pet>>() {
            public F<Integer, Pet> apply(final String _cons1_arg0) { return new F<Integer, Pet>() {public examples.factory.inheritance.Cat apply(final Integer _cons1_arg1) { return new examples.factory.inheritance.Cat(_cons1_arg0, _cons1_arg1); } }; }
        };
        
        // constructor _cons1, param _cons1_arg0
        Enumeration<String> _cons2_arg0Enum = new Enumeration<String>(Combinators.makeString().parts().take(BigInteger.valueOf(4)));
        Enumeration<Integer> _cons2_arg1Enum = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));
        
        Enumeration<Pet> _cons2Enum = Enumeration.apply(Enumeration.apply(Enumeration.singleton(_cons2), _cons2_arg0Enum), _cons2_arg1Enum);
        
        // accumulating all constructors
        finalEnum = _cons1Enum.plus(_cons2Enum);
        
        return finalEnum;
        
    }

}