public class Peter {
  %typeint

  public final static void main(String[] args) {
    new Peter().run();
  }
  
  public void run() {
  	int constant = 0;
  	%match(int constant,int constant) {
      v,v -> {
        %match(int constant) {
          _ -> { /* bingo */ }
        }
      }
  	}
  }

}

