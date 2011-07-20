package tom.engine.parserv3.test;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.junit.Test;

import tom.engine.parserv3.streamanalysis.DelimitedSequenceDetector;
import tom.engine.parserv3.streamanalysis.EOLdetector;
import tom.engine.parserv3.streamanalysis.KeywordDetector;
import tom.engine.parserv3.streamanalysis.StreamAnalyst;
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
  
  @Test
  public void test_OneLineComment_macOs() {
    
    CharStream testStream = new ANTLRStringStream("0//34\r6789ABCFED");
    List<Integer> matchIndexes = Arrays.asList(2, 3, 4, 5);
    
    StreamAnalyst olColCommentDetector =
      new DelimitedSequenceDetector(new KeywordDetector("//"), new EOLdetector());
    
    while(testStream.LA(1)!=CharStream.EOF){
      
      olColCommentDetector.readChar(testStream);
      
      assertEquals("index="+testStream.index(),
                   matchIndexes.contains(testStream.index()),
                   olColCommentDetector.match());
      
      testStream.consume();
    
    }
  }
    
  @Test
  public void test_OneLineComment_win() {
      
    CharStream testStream = new ANTLRStringStream("0//34\r\n789ABCFED");
    List<Integer> matchIndexes = Arrays.asList(2, 3, 4, 5, 6);
      
    StreamAnalyst olCommentDetector =
      new DelimitedSequenceDetector(new KeywordDetector("//"), new EOLdetector());
      
    while(testStream.LA(1)!=CharStream.EOF){
        
      olCommentDetector.readChar(testStream);

      assertEquals("index="+testStream.index(),
                     matchIndexes.contains(testStream.index()),
                     olCommentDetector.match());
        
      testStream.consume();
      
    }
  }
      
  @Test
  public void test_OneLineComment_linux() {
      
    CharStream testStream = new ANTLRStringStream("0//34\n6789ABCFED");
    List<Integer> matchIndexes = Arrays.asList(2, 3, 4, 5);
      
    StreamAnalyst olCommentDetector =
      new DelimitedSequenceDetector(new KeywordDetector("//"), new EOLdetector());
      
    while(testStream.LA(1)!=CharStream.EOF){
        
      olCommentDetector.readChar(testStream);

      assertEquals("index="+testStream.index(),
                     matchIndexes.contains(testStream.index()),
                     olCommentDetector.match());
        
      testStream.consume();
      
    }
  }
  
  @Test
  public void test_MultilineComment(){
    CharStream testStream = new ANTLRStringStream("0/*34\n6*/9ABCFED");
    List<Integer> matchIndexes = Arrays.asList(2, 3, 4, 5, 6, 7, 8);
      
    StreamAnalyst mlCommentDetector =
      new DelimitedSequenceDetector("/*", "*/");
      
    while(testStream.LA(1)!=CharStream.EOF){
        
      mlCommentDetector.readChar(testStream);

      assertEquals("index="+testStream.index(),
                     matchIndexes.contains(testStream.index()),
                     mlCommentDetector.match());
        
      testStream.consume();
      
    }
  }
}
