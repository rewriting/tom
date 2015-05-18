package examples.factory.handwritten.recursive3;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class User {
    
    private int no;
    private Account account;

    @Enumerate(canBeNull = true)
    public User(@Enumerate(maxSize = 3) int no,
            @Enumerate(maxSize = 4) Account account) {
        this.no = no;
        this.account = account;
    }

    
    public int getNo() {
        return no;
    }


    public Account getAccount() {
        return account;
    }


    @Override
    public String toString() {
        return "User [no=" + no + ", account=" + account + "]";
    }
}
