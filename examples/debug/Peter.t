public class Peter {
  %include {int.tom}

  public final static void main(String[] args) {
    new Peter().run();
  }
  
  public void run() {
  	int constant = 0;
  	%match(int constant,int constant) {
      v,v -> {
				System.out.println("bingo");
        %match(int constant) {
          _ -> { /* bingo */ }
        }
      }
  	}
  }

}

