
public class ConstructWatcher {

    private int state;
    private String keyword;

    public ConstructWatcher(String keyword) {
        this.state = 0;
        this.keyword = keyword;
    }

    public void take(char c) {
        if (keyword.charAt(state) == c) {
            state++;
            return ;
        }
        state = 0;
        return ;
    }
}
