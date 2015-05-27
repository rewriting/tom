package examples.factory.inheritance;

import tom.library.factory.Enumerate;

public class Person {
    
private Pet pet;
    
    @Enumerate
    public Person(@Enumerate(concreteClasses = {Dog.class, Cat.class}) Pet pet) {
        this.pet = pet;
    }
    
    @Override
    public String toString() {
        return "Pet [pet=" + pet + "]";
    }
    
}
