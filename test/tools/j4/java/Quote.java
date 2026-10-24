/**
 * The quoting of tom/engine/bridge.JavaQuote (\\ \" \n \r \t escaped, any
 * other char below 0x20 or 0x7f written as backslash-u and 4 hex digits) and its inverse, for the
 * J4.1a equivalence helpers.
 */
public class Quote {
  public static String quote(String s) {
    StringBuilder b = new StringBuilder("\"");
    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch(c) {
        case '\\': b.append("\\\\"); break;
        case '"': b.append("\\\""); break;
        case '\n': b.append("\\n"); break;
        case '\r': b.append("\\r"); break;
        case '\t': b.append("\\t"); break;
        default:
          if(c < 0x20 || c == 0x7f) {
            b.append(String.format("\\u%04x", (int)c));
          } else {
            b.append(c);
          }
      }
    }
    return b.append('"').toString();
  }

  public static String unquote(String q) {
    if(q.length() < 2 || q.charAt(0) != '"' || q.charAt(q.length()-1) != '"') {
      throw new IllegalArgumentException("not a quoted string: " + q);
    }
    StringBuilder b = new StringBuilder();
    for(int i = 1; i < q.length()-1; i++) {
      char c = q.charAt(i);
      if(c != '\\') {
        b.append(c);
        continue;
      }
      char e = q.charAt(++i);
      switch(e) {
        case 'n': b.append('\n'); break;
        case 'r': b.append('\r'); break;
        case 't': b.append('\t'); break;
        case 'u': b.append((char)Integer.parseInt(q.substring(i+1, i+5), 16)); i += 4; break;
        default: b.append(e);
      }
    }
    return b.toString();
  }
}
