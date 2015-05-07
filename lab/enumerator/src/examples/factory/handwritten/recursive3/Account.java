package examples.factory.handwritten.recursive3;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Account {
    private int no;
    private Address address;

    @EnumerateGenerator(canBeNull = true)
    public Account(@Enumerate(maxSize = 3) int no,
            @Enumerate(maxSize = 4) Address address) {
        this.no = no;
        this.address = address;
    }

    
    public int getNo() {
        return no;
    }


    public Address getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return "Account [no=" + no + ", address=" + address + "]";
    }
}
