package test;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.Test;
import static org.junit.Assert.*;

import streamanalysis.NegativeImbricationDetector;
import streamanalysis.StreamAnalyst;

public class Test_NegativeImbricationDetector {

  @Test
  public void testImbricationStartingMinusOne(){
    
    CharStream testStream = new ANTLRStringStream("01{3{5{7}9}B}DEF");
    List<Integer> matchIndexes = Arrays.asList(0, 1, 12, 13, 14, 15);
    
    StreamAnalyst negImbrDetector = 
      new NegativeImbricationDetector('{', '}', -1);
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      negImbrDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   negImbrDetector.match());
      
      testStream.consume();
    
    }
  }
  
  @Test
  public void testImbricationStartingZero(){
    
    CharStream testStream = new ANTLRStringStream("0123{5{7}9}B}DEF");
    List<Integer> matchIndexes = Arrays.asList(12, 13, 14, 15);
    
    StreamAnalyst negImbrDetector = 
      new NegativeImbricationDetector('{', '}', 0);
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      negImbrDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   negImbrDetector.match());
      
      testStream.consume();
    
    }
  }
}