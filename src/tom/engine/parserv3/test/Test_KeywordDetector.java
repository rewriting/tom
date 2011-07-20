package tom.engine.parserv3.test;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.Test;
import static org.junit.Assert.*;

import tom.engine.parserv3.streamanalysis.KeywordDetector;
import tom.engine.parserv3.streamanalysis.StreamAnalyst;

public class Test_KeywordDetector {

  @Test
  public void oneCharKeywordDetection(){
    
    CharStream testStream = new ANTLRStringStream("0%234%%789");
    List<Integer> matchIndexes = Arrays.asList(1, 5, 6);
    
    StreamAnalyst percentDetector = new KeywordDetector("%");
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      percentDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   percentDetector.match());
      
      testStream.consume();
    }
  }
  
  @Test
  public void oneCharKeywordDetection2(){
    CharStream testStream = new ANTLRStringStream("%12345678%");
    List<Integer> matchIndexes = Arrays.asList(0, 9);
    
    StreamAnalyst percentDetector = new KeywordDetector("%");
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      percentDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   percentDetector.match());
      
      testStream.consume();
    }
  }
  
  @Test
  public void stringKeywordDetection(){
    CharStream testStream = new ANTLRStringStream("0%M34%_M89");
    List<Integer> matchIndexes = Arrays.asList(2);
    
    StreamAnalyst percentMDetector = new KeywordDetector("%M");
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      percentMDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   percentMDetector.match());
      
      testStream.consume();
    }
  }
  
  @Test
  public void stringKeywordDetection2(){
    CharStream testStream = new ANTLRStringStream("%MM34%%7%M");
    List<Integer> matchIndexes = Arrays.asList(1, 9);
    
    StreamAnalyst percentMDetector = new KeywordDetector("%M");
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      percentMDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   percentMDetector.match());
      
      testStream.consume();
    }
  }
  
}
