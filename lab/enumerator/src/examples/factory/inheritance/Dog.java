package examples.factory.inheritance;

import tom.library.factory.Enumerate;

public class Dog implements Pet {

    private String name;
    private int age;
    
    @Enumerate
    public Dog(@Enumerate String name,@Enumerate int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "Dog [name=" + name +", age="+age+ "]";
    }
}
