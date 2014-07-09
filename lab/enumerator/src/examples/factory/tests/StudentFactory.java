package examples.factory.tests;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import java.math.BigInteger;
import examples.factory.Student;
public class StudentFactory{
public static final  Enumeration<Student> getEnumeration(){
boolean canBeNull = false;
// if(@PlayWithGenerators(canBeNull)){
    canBeNull = true;
// }
return getEnumeration(canBeNull);
}
public static final  Enumeration<Student> getEnumeration(boolean canBeNull) {
 Enumeration<Student> enumRes = null;
final F<Integer, F<String, examples.factory.Student>> _student= new F<Integer, F<String, examples.factory.Student>>(){
public F<String, examples.factory.Student> apply (final Integer arg0){
 return new F<String, examples.factory.Student>(){
public examples.factory.Student apply (final String arg1){
return new examples.factory.Student(arg0, arg1);
}
};
}
};

final Enumeration<Integer> arg0 = new Enumeration<Integer>(Combinators.makeInteger().parts().take(BigInteger.valueOf(8)));
final Enumeration<String> arg1 = new Enumeration<String>(Combinators.makeString().parts().take(BigInteger.valueOf(4)));

enumRes = Enumeration.apply(Enumeration.apply(Enumeration.singleton(_student), arg0), arg1);
if (canBeNull) {
final Enumeration<examples.factory.Student> emptyEnum = Enumeration.singleton(null);
enumRes = emptyEnum.plus(enumRes);
}
return enumRes;

}

}
