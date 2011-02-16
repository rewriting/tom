package ppeditor.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value={
TestPPCursor.class,
TestPPTextPosition.class
})

public class AllTests {}

