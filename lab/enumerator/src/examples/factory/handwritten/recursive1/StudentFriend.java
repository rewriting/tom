package examples.factory.handwritten.recursive1;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class StudentFriend {

    private int no;
    private String name;
    private StudentFriend friend;

    @EnumerateGenerator(canBeNull = true)
    public StudentFriend(
        @Enumerate(maxSize = 8) int no,
        @Enumerate(maxSize = 6) StudentFriend friend
    ) {
        this.no = no;
        this.friend = friend;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    
    public StudentFriend getFriend() {
        return friend;
    }

    @Override
    public String toString() {
        return "StudentFriend [no=" + no + ", studentfriend="+friend+ "]";
    }

}
