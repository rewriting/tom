package test;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.Test;

import streamanalysis.DelimitedSequenceDetector;
import streamanalysis.StreamAnalyst;
import static org.junit.Assert.*;


public class Test_DelimitedSequenceDetector {

  @Test
  public void test_HostStringDetection(){
    
    //with an input text like :    0"2\"567"9A\""EF
    //we'd like found to go like : fttttttttfffttff 
    CharStream testStream = new ANTLRStringStream("0\"2\\\"567\"9A\\\"\"EF");
    List<Integer> matchIndexes = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 12, 13);
    
    StreamAnalyst hostStringDetector =
      new DelimitedSequenceDetector("\"", "\"", '\\');
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      hostStringDetector.readChar(testStream);
      
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   hostStringDetector.match());
      
      testStream.consume();
    }
    
  }
  
}
