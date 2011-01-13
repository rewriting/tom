
public class ConstructWatcher {

    private int state;
    private String[] keywords;

    public ConstructWatcher(String[] keywords) {
        this.state = 0;
        this.keywords = keywords;
    }

    public void take(char c) {
        if (keyword.charAt(state) == c) {
            state++;
            return ;
        }
        state = 0;
        return ;
    }

    public String getRead() {
      return "not yet";
    }

}
