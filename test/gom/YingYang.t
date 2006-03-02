
import ying.types.*;
import yang.types.*;

public class YingYang {

  %include { ying/Ying.tom }

  public void run() {
    Moon m = `ping(pong(ping(shi()))); 
    System.out.println("ping(pong(ping(shi()))) = " + m);
  }
  
  public static void main(String[] args) {
    YingYang test = new YingYang();
    test.run();
  }
}
