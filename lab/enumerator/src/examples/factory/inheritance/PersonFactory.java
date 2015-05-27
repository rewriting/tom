
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
import examples.factory.inheritance.Person;

public class PersonFactory {
    
    private static Enumeration<Person> finalEnum = null;
    
    public static final Enumeration<Person>  getEnumeration() {
        
//////////////// generating constructors /////////////
        
        // constructor _cons1
        final F<examples.factory.inheritance.Pet, Person> _cons1 = new F<examples.factory.inheritance.Pet, Person>() {
            public examples.factory.inheritance.Person apply(final examples.factory.inheritance.Pet _cons1_arg0) { return new examples.factory.inheritance.Person(_cons1_arg0); }
        };
        
        // constructor _cons1, param _cons1_arg0
        Enumeration<examples.factory.inheritance.Pet> _cons1_arg0Enum = examples.factory.inheritance.PetFactory.getEnumeration();
        
        Enumeration<Person> _cons1Enum = Enumeration.apply(Enumeration.singleton(_cons1), _cons1_arg0Enum);
        
        // accumulating all constructors
        Enumeration<Person> allConstructorsEnum = _cons1Enum;
        
        
        finalEnum = allConstructorsEnum;
        
        return finalEnum;
        
    }

}