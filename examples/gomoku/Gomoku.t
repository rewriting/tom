package gomoku;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import java.io.*;
import gomoku.gomoku.gomoku.*;
import gomoku.gomoku.gomoku.types.*;

class Gomoku {

  private final static int black = -1;
  private final static int white = -2;
  private int size = 10;

  private gomokuFactory factory;
  private int[][] board = new int[size][size];

  %vas {
    module gomoku
    imports int
    public
      sorts Pawn PawnList PawnListList
      
    abstract syntax
      pawn(color:Int) -> Pawn
      empty(x:Int,y:Int) -> Pawn
      concPawn(Pawn*) -> PawnList
      concPlateList(PawnList*) -> PawnListList
   }
  
  public Gomoku(gomokuFactory factory) {
    this.factory = factory;
  } 

  public gomokuFactory getGomokuFactory() {
    return factory;
  }
  
  public final static void main(String[] args) {
    Gomoku test = new Gomoku(gomokuFactory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    draw();
    for(int i=0; i<size*size/2 ; ++i) {
      humanPlay(black);
      computerPlay(white);
      draw();
      clear();
      winnerIs();
    }
    if(size*size%2 == 1) {
      humanPlay(black);
      draw();
      winnerIs();
    }
  }

  public void humanPlay(int color) {
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader stdin = new BufferedReader(isr);
      System.out.print("Line (1-"+(size)+") : ");
      String x = new String(stdin.readLine());
      System.out.print("Column (1-"+(size)+"): ");
      String y = new String(stdin.readLine());
      play(color,Integer.parseInt(x)-1,Integer.parseInt(y)-1);
    } catch(Exception e) {
      System.out.println("Error : "+e);
      humanPlay(color);
    }
  }

  public void computerPlay(int color) {
    //Search horizontal
    searchPatterns(getHorizontalList(),white);
    searchPatterns(getHorizontalList(),black);
    //Search vertical
    searchPatterns(getVerticalList(),white);
    searchPatterns(getVerticalList(),black);
    //Search diagonal
    searchPatterns(getDiagonalList(),white);
    searchPatterns(getDiagonalList(),black);
    int max = -1;
    int x = -1;
    int y = -1;
    for(int i=0; i<size ; ++i)
      for(int j=0 ; j<size ; ++j)
        if(board[i][j]>max) {
          max = board[i][j];
          x = i;
          y = j;
        }
    System.out.println("Computer play on line: "+(x+1)+" column: "+(y+1));
    try {
      play(color,x,y);
    } catch(Exception e) {}
  }

  private void draw() {
    for(int i=0; i<size ; ++i) {
      for(int j=0 ; j<size ; ++j) {
        switch(board[i][j]) {
        case black :
          System.out.print("X "); break;
        case white:
          System.out.print("O "); break;
        default:
          //System.out.print("_("+board[i][j]+")\t");
          System.out.print("_ ");
        }
      }
      System.out.print("\n");
    }
  }

  public void winnerIs() {
    winningPattern(getHorizontalList());
    winningPattern(getVerticalList());
    winningPattern(getDiagonalList());
  }

  private void winningPattern(PawnListList pl) {
    %match(PawnListList pl) {
      //Five pawn of the same color in line
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        switch(`c) {
        case black :
          System.out.println("X won !\n"); break;
        case white:
          System.out.print("O won !\n"); break;
        }
        System.exit(0);
      }
    }
  }

  private void clear() {
    for(int i=0; i<size ; ++i)
      for(int j=0 ; j<size ; ++j)
        if(board[i][j]>0)
          board[i][j] = 0;
  }

  private void play(int color, int x, int y) throws Exception {
    if(((x>-1)&&(y>-1))&&((x<size)&&(y<size))&&(board[x][y]>=0))
        board[x][y] = color;
    else
      throw new Exception("Out of bound or not empty");
    }

  private Pawn getPawn(int x, int y) {
    switch(board[x][y]) {
    case black :
      return `pawn(black);
    case white:
      return `pawn(white);
    default:
      return `empty(x,y);
    }
  }

  private PawnListList getHorizontalList() {
    PawnList tmp;
    PawnListList list = `concPawnList();
    for(int i=0; i<size ; ++i) {
      tmp = `concPawn();
      for(int j=0 ; j<size ; ++j) {
        tmp = `concPawn(tmp*,getPawn(i,j));
      }
      list = `concPawnList(list*,tmp);
     }
    //System.out.println(list);
    return list;
  }

  private PawnListList getVerticalList() {
    PawnList tmp;
    PawnListList list = `concPawnList();
    for(int j=0; j<size ; ++j) {
      tmp = `concPawn();
      for(int i=0 ; i<size ; ++i) {
        tmp = `concPawn(tmp*,getPawn(i,j));
      }
      list = `concPawnList(list*,tmp);
     }
    //System.out.println(list);
    return list;
  }

  private PawnListList getDiagonalList() {
    PawnList tmp = `concPawn();
    PawnListList list = `concPawnList();
    for(int i=0; i<size ; ++i) {
      int x = i;
      int y = 0;
      for(int j=0 ; j<size ; ++j) {
        tmp = `concPawn(tmp*,getPawn(x,y));
        --x;
        ++y;
        if(x<0) {
          x = size-1;
          list = `concPawnList(list*,tmp);
          tmp = `concPawn();
        }
      }
      if(!tmp.isEmpty()) {
        list = `concPawnList(list*,tmp);
        tmp = `concPawn();
      }
     }
    for(int i=0; i<size ; ++i) {
      int x = i;
      int y = 0;
      for(int j=0 ; j<size ; ++j) {
        tmp = `concPawn(tmp*,getPawn(x,y));
        ++x;
        ++y;
        if(x>size-1) {
          x = 0;
          list = `concPawnList(list*,tmp);
          tmp = `concPawn();
        }
      }
      if(!tmp.isEmpty()) {
        list = `concPawnList(list*,tmp);
        tmp = `concPawn();
      }
     }
    //System.out.println(list);
    return list;
  }

  private void searchPatterns(PawnListList pl, int patternColor) {
    int value1 = 10000;
    int value2 = 500;
    int value3 = 100;
    int value4 = 100;
    int value5 = 10;
    int value6 = 15;
    int value7 = 1;
    %match(PawnListList pl) {
      // 4 -> winning
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),pawn(c),pawn(c),empty(x,y),_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value1;
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),pawn(c),empty(x,y),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value1;
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x,y),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value1;
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x,y),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value1;
      }
      concPawnList(_*,concPawn(_*,empty(x,y),pawn(c),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value1;
      }
      // 3 -> winning
      concPawnList(_*,concPawn(_*,empty[],pawn(c),pawn(c),pawn(c),empty(x,y),empty[],_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value2;
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),pawn(c),empty(x,y),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value2;
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x,y),pawn(c),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value2;
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x,y),pawn(c),pawn(c),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor)
          board[`x][`y] = board[`x][`y]+value2;
      }
      // 3
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),pawn(c),pawn(c),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value4;
          board[`x2][`y2] = board[`x2][`y2]+value4;
          //}
      }
      // 2 in 6
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value3;
          board[`x2][`y2] = board[`x2][`y2]+value3;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value3;
          board[`x2][`y2] = board[`x2][`y2]+value3;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value3;
          board[`x2][`y2] = board[`x2][`y2]+value3;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value3;
          board[`x2][`y2] = board[`x2][`y2]+value3;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value3;
          board[`x2][`y2] = board[`x2][`y2]+value3;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value3;
          board[`x2][`y2] = board[`x2][`y2]+value3;
          //}
      }
      // 2 in 5
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value5;
          board[`x2][`y2] = board[`x2][`y2]+value5;
          board[`x3][`y3] = board[`x3][`y3]+value5;
          //}
      }
      // 1 in 6
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value6;
          board[`x2][`y2] = board[`x2][`y2]+value6;
          board[`x3][`y3] = board[`x3][`y3]+value6;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value6;
          board[`x2][`y2] = board[`x2][`y2]+value6;
          board[`x3][`y3] = board[`x3][`y3]+value6;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value6;
          board[`x2][`y2] = board[`x2][`y2]+value6;
          board[`x3][`y3] = board[`x3][`y3]+value6;
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value6;
          board[`x2][`y2] = board[`x2][`y2]+value6;
          board[`x3][`y3] = board[`x3][`y3]+value6;
          //}
      }
      //1 in 5
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value7;
          board[`x2][`y2] = board[`x2][`y2]+value7;
          board[`x3][`y3] = board[`x3][`y3]+value7;
          board[`x4][`y4] = board[`x4][`y4]+value7;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value7;
          board[`x2][`y2] = board[`x2][`y2]+value7;
          board[`x3][`y3] = board[`x3][`y3]+value7;
          board[`x4][`y4] = board[`x4][`y4]+value7;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value7;
          board[`x2][`y2] = board[`x2][`y2]+value7;
          board[`x3][`y3] = board[`x3][`y3]+value7;
          board[`x4][`y4] = board[`x4][`y4]+value7;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value7;
          board[`x2][`y2] = board[`x2][`y2]+value7;
          board[`x3][`y3] = board[`x3][`y3]+value7;
          board[`x4][`y4] = board[`x4][`y4]+value7;
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),empty(x3,y3),empty(x4,y4),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          board[`x1][`y1] = board[`x1][`y1]+value7;
          board[`x2][`y2] = board[`x2][`y2]+value7;
          board[`x3][`y3] = board[`x3][`y3]+value7;
          board[`x4][`y4] = board[`x4][`y4]+value7;
          //}
      }
    }
  }

}
