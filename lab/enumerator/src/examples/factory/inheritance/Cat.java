package examples.factory.inheritance;

import tom.library.factory.Enumerate;

public class Cat implements Pet {

    private String name;
    private int age;
    private String nickName;
    
    @Enumerate
    public Cat(@Enumerate String name,@Enumerate int age,@Enumerate String nickName) {
        this.name = name;
        this.age = age;
        this.nickName = nickName;
    }
    
    @Enumerate
    public Cat getHonored(@Enumerate String name,@Enumerate int age,@Enumerate String nickName) {
        this.name = "A"+name;
        this.age = age;
        this.nickName = nickName+"i";
        return this;
    }
    
    @Override
    public String toString() {
        return "Cat [name=" + name +", age="+ age +", nick="+nickName+ "]";
    }
}
