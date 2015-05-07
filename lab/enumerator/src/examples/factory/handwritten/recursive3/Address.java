package examples.factory.handwritten.recursive3;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Address {
    private int no;
    private User user;

    @EnumerateGenerator(canBeNull = true)
    public Address(@Enumerate(maxSize = 3) int no,
            @Enumerate(maxSize = 8) User user) {
        this.no = no;
        this.user = user;
    }

    
    public int getNo() {
        return no;
    }


    public User getUser() {
        return user;
    }


    @Override
    public String toString() {
        return "Address [no=" + no + ", user=" + user + "]";
    }
}
