public class ConstructWatcher {

    private int state;
    private String[] keywords;
/* now tools for efficient recognition */
    private int card;/* total number of keywords looked for */
    private int[] toCheck;/* the indexes of keywords that can possibly be matched */
    private boolean ready;/* whether one of the keywords has been found */
    private int matchedConstruct;/* which one */
    private StringBuffer read;/* a memory to store characters read before returning them to the parser */

    public ConstructWatcher(String[] keywords) {
        this.state = 0;
        this.keywords = keywords;
        card = keywords.length;
        toCheck = new int[card];
        initToCheck();
        ready = false;
        matchedConstruct = -1;
    }

    private void initToCheck() {
      for(int i = 0; i < card; i++) {
        toCheck[i] = i;
      }
    }

    public void take(char c) {
      int stillPossible = 0;
      for(int i = 0; i < card; i++) {
        if(toCheck[i] == -1) {
          break;
        }
        else if(keywords[toCheck[i]].charAt(state) == c) {
          toCheck[stillPossible] = i;
          stillPossible++;
        }
      }
      for(int i = stillPossible; i < card; i++) {/* now blank the rest of toCheck with -1s */
        toCheck[i] = -1;
      }
      if(stillPossible == 0) {/* no matching character found among the keywords */
        read.append(c);
        state = 0;
        initToCheck();
      }
      else {
        state++;
        for(int i = 0; i < stillPossible; i++) {/* check if we reached the end of one of the keywords */
          if(keywords[toCheck[i]].length() == state) {
            matchedConstruct = toCheck[i];
            ready = true;
            break;
          }
        }
      }
      
    }

    public boolean ready() {
      return ready;
    }

    public int getConstruct() {
      return matchedConstruct;
    }

    public String getRead() {
      String result = "";
      if(state == 0) {
        result = read.toString();
        read = new StringBuffer();
      }
      return result;
    }

}
