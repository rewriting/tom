package test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;
import examples.factory.Student;
import tom.library.theory.ForSome;
import tom.library.theory.PropCheck;

@RunWith(PropCheck.class)
public class StudentTest {

    @Theory
    public void testToString(@ForSome(maxSampleSize = 10) Student student) {
        assertThat(student.toString(), is("Student [no=" + student.getNo() + ", name=" + student.getName() + "]"));
    }
    
}
