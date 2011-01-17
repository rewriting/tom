package tom.engine.prettyprinter.ppeditor;

import java.util.List;

public class PPCursor{
    
  private PPTextPosition position;
  private boolean insertion;
  private List<StringBuffer> fileBuffer;

    public PPCursor() {
    }

    public void moveCursorAbsolutely(PPTextPosition p) {}

    public void moveCursorRelatively(PPTextPosition p) {}

    public void setInsertion(boolean i) {}

//    public PPTextPosition getPosition() {}

//    public boolean isInsertion() {}

//    public String readFileBuffer() {}

    public void writeText(String s) {}

    public void eraseText(int n) {}

    public void eraseText(String s) {}

    public void eraseAll() {}

}
