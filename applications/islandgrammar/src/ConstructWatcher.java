public class ConstructWatcher {

  private int state;
  private String keyword;

  public ConstructWatcher(String keyword) {
    state = 0;
    this.keyword = keyword;
  }

  public boolean take(char c) {
    if(keyword.charAt(state) == c) {
      state++;
      return true;
    }
    return false;
  }

}
