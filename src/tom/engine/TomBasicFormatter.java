package jtom;

import java.util.logging.*;

public class TomBasicFormatter extends Formatter {

  public String format(LogRecord record) {
    Level level = record.getLevel();
    String stringLevel;

    if( level.equals(Level.SEVERE) ) {
      stringLevel = "ERROR";
    } else {
      stringLevel = level.toString();
    }

    return stringLevel + ":" + formatMessage(record) + "\n";
  }

}
