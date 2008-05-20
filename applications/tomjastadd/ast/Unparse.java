package ast;

import ast.AST.*;
import ast.AST.Token;
import ast.AST.SimpleNode;

public aspect Unparse {
  public Token SimpleNode.firstToken;
  public Token SimpleNode.lastToken; 
  public void SimpleNode.unparseComment(StringBuffer buf) {
        Token tt = firstToken.specialToken;
        if (tt != null) {
            while (tt.specialToken != null) tt = tt.specialToken;
            while (tt != null) {
                buf.append(addUnicodeEscapes(tt.image));
                tt = tt.next;
            }
        }
  }

  public String SimpleNode.unparseComment() {
    StringBuffer buf = new StringBuffer();
    unparseComment(buf);
    return buf.toString();
  }
    private String SimpleNode.addUnicodeEscapes(String str) {
        String retval = "";
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ((ch < 0x20 || ch > 0x7e) &&
                ch != '\t' && ch != '\n' && ch != '\r' && ch != '\f') {
                String s = "0000" + Integer.toString(ch, 16);
                retval += "\\u" + s.substring(s.length() - 4, s.length());
            } else {
                retval += ch;
            }
        }
        return retval;
     }

}
