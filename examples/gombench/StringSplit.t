package gombench;

public class StringSplit {
  %include {string.tom}

  public static final void main(String[] args) {
    int maxcount = 0;
    try {
      maxcount = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.err.println("Usage: java gombench.StringSplit <maxcount>");
      return;
    }
    for(int i = 1; i<=maxcount; i++) {
      System.out.print(i+"\t");
      System.out.println(benchnew(i));
    }
    System.out.println();
    for(int i = 1; i<=maxcount; i++) {
      System.out.print(i+"\t");
      System.out.println(benchold(i));
    }
    System.out.println();

    for(int i = 200; i<=(200+(maxcount*50)); i+=50) {
      System.out.print(i+"\t");
      System.out.println(benchfast(i));
    }
    System.out.println();
    for(int i = 200; i<=(200+(maxcount*50)); i+=50) {
      System.out.print(i+"\t");
      System.out.println(benchnew(i));
    }
    System.out.println();
  }

  public static double benchfast(int i) {
    String test = new String(makeInput(0));
    String mark = "";
		long startChrono = System.currentTimeMillis();
    for(int j = 0; j < i; j++) {
    %match(String test) {
      (login*,':',pass*,':',uid*,':',gid*,':',fname*,':',path*,':',shell*) -> {
        //mark = `login* + `pass* + `rest*;
      }
    }
    }
		long stopChrono = System.currentTimeMillis();
    return ((stopChrono-startChrono)/1000.);
  }

  public static double benchnew(int i) {
    String test = new String(makeInput(0));
    String mark = "";
		long startChrono = System.currentTimeMillis();
    for(int j = 0; j < i; j++) {
    %match(String test) {
      (login*,x,pass*,x,uid*,x,gid*,x,fname*,x,path*,x,shell*) -> {
        //mark = `login* + `pass* + `rest*;
      }
    }
    }
		long stopChrono = System.currentTimeMillis();
    return ((stopChrono-startChrono)/1000.);
  }

  public static double benchold(int i) {
    String test = new String(makeInput(0));
    String mark = "";
		long startChrono = System.currentTimeMillis();
    for(int j = 0; j < i; j++) {
    %match(String test) {
      (login*,a,pass*,b,uid*,c,gid*,d,fname*,e,path*,f,shell*) -> {
        if(`a == `b && `b == `c && `c == `d && `d == `e && `e == `f && `a == ':') {
          //mark = `login* + `pass* + `rest*;
        }
      }
    }
    }
		long stopChrono = System.currentTimeMillis();
    return ((stopChrono-startChrono)/1000.);
  }

  public static String makeInput(int i) {
    String tmpres = "postfix:*:27:27:Postfix User:/var/spool/postfix:/usr/bin/false";
    String res = tmpres;
    for (int j = 0; j < i; j++)
      res = res + "\n" + tmpres;
    return res;
  }

}
