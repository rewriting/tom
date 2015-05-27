package examples.factory.inheritance;

import tom.library.factory.Enumerate;

public class Cat implements Pet {

    private String name;
    private int age;
    
    @Enumerate
    public Cat(@Enumerate String name,@Enumerate int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public String toString() {
        return "Cat [name=" + name +", age="+age+ "]";
    }
}
