package tom.engine.parserv3.tester;

import java.util.ArrayList;
import java.util.List;

public class TestResult {

  // fields
  private List<Entry> entries = new ArrayList<Entry>();
  
  // constructor
  public TestResult() {}
	
  // usefull stuffs
  enum TestIssue{
    PASSED ("test passed."),
    ERROR_WHILE_PARSING ("parser failed"),
    BAD_TREE ("produced tree was different from expected one"),
    ERROR_WHILE_ADAPTING ("adaptor failed"),
    INTERNAL_TESTER_PROBLEM ("internal tester problem");
    
    private String desc;
    
    TestIssue(String desc) {
      this.desc = desc;
    }
    
    @Override
    public String toString() {
      return desc;
    }
  }
	
  private static class Entry{
    private final String fileAbsPath;
    private final String description;

    private final TestIssue issue;
    private final String actualResult;
    private final String expectedResult;
    
    private Entry(String fileAbsPath, String description, TestIssue issue,
                  String actualResult, String expectedResult) {
      this.fileAbsPath = fileAbsPath;
      this.description = description;
      this.issue = issue;
      this.actualResult = actualResult;
      this.expectedResult = expectedResult;
    }
  }
	
  // methods
  public void addResult(String fileAbsPath, String description,
          TestIssue testIssue, String actualResult, String expectedResult){
      
	  entries.add(new Entry(fileAbsPath, description, testIssue, actualResult,
				expectedResult));
  }
  
  public void addResult(TestFile testFile, TestIssue testIssue,
		  String actualResult, String expectedResult){
	  entries.add(new Entry(testFile.getPath(), testFile.getDescription(),
			  testIssue, actualResult, expectedResult));
  }
  
  // print Stuffs
  private final String fatLine  =">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
  private final String midLine  ="================================================================================";
  //private final String thinLine ="--------------------------------------------------------------------------------";
  private final String expected ="expected : ---------------------------------------------------------------------";
  private final String actual   ="actual : -----------------------------------------------------------------------";
  
  public String getTextualAbstract(){
    StringBuilder passed = new StringBuilder();
    StringBuilder failed = new StringBuilder();
    
    int passedCount=0;
    int failedCount=0;
    
    int id = 0;
      for(Entry e : entries){
        id++;
    	  
    	if(e.issue==TestIssue.PASSED){
          passedCount++;
          
          passed.append("["+id+"][PASSED] : ").append(e.description).append('\n');
		}else{
		  failedCount++;	
			
		  failed.append(midLine).append('\n');
		  failed.append("["+id+"][FAILED : "+e.issue.toString()+"] : ").append(e.description).append('\n');
		  failed.append(e.fileAbsPath).append('\n');
		  failed.append('\n');
		  failed.append(expected).append('\n');
		  failed.append(e.expectedResult).append('\n');
		  failed.append(actual).append('\n');
		  failed.append(e.actualResult).append('\n');
		  failed.append(midLine).append('\n');
		}
      }
      
      return fatLine+'\n'+passed.toString()+fatLine+'\n'+failed.toString()+fatLine+'\n'+
             ((failedCount==0)?"Test Passed ^^" : "Test Failed !") +
             "("+passedCount+"/"+(passedCount+failedCount)+")";
  }
  

  
}
