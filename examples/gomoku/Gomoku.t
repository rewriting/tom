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
  private int size = 3;
  private int line = 3;

  private gomokuFactory factory;
  private int[][] board = new int[size][size];

  %vas {
    module gomoku
    imports int
    public
      sorts Pawn PawnList PawnListList
      
    abstract syntax
      pawnBlack() -> Pawn
      pawnWhite() -> Pawn
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
      humanPlay();
      computerPlay();
      draw();
      clear();
    }
    if(size*size%2 == 1) {
      humanPlay();
      draw();
    }
  }

  private void draw() {
    for(int i=0; i<size ; ++i) {
      for(int j=0 ; j<size ; ++j) {
        switch(board[i][j]) {
        case black :
          System.out.print("X\t"); break;
        case white:
          System.out.print("O\t"); break;
        default:
          System.out.print("_("+board[i][j]+")\t");
        }
      }
      System.out.print("\n");
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
      return `pawnBlack();
    case white:
      return `pawnWhite();
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

  private void searchPatterns(PawnListList pl) {
    int low = 1;
    int hight = 5;
    %match(PawnListList pl) {
      concPawnList(_*,concPawn(_*,pawnBlack(),pawnBlack(),empty(x,y),_*),_*) -> {
        board[`x][`y] = board[`x][`y]+hight;
      }
      concPawnList(_*,concPawn(_*,pawnBlack(),empty(x,y),pawnBlack(),_*),_*) -> {
        board[`x][`y] = board[`x][`y]+hight;
      }
      concPawnList(_*,concPawn(_*,empty(x,y),pawnBlack(),pawnBlack(),_*),_*) -> {
        board[`x][`y] = board[`x][`y]+hight;
      }
      concPawnList(_*,concPawn(_*,pawnBlack(),empty(x1,y1),empty(x2,y2),_*),_*) -> {
        board[`x1][`y1] = board[`x1][`y1]+low;
        board[`x2][`y2] = board[`x2][`y2]+low;
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawnBlack(),empty(x2,y2),_*),_*) -> {
        board[`x1][`y1] = board[`x1][`y1]+low;
        board[`x2][`y2] = board[`x2][`y2]+low;
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawnBlack(),_*),_*) -> {
        board[`x1][`y1] = board[`x1][`y1]+low;
        board[`x2][`y2] = board[`x2][`y2]+low;
      }
    }
    %match(PawnListList pl) {
      concPawnList(_*,concPawn(_*,pawnWhite(),pawnWhite(),empty(x,y),_*),_*) -> {
        board[`x][`y] = board[`x][`y]+hight;
      }
      concPawnList(_*,concPawn(_*,pawnWhite(),empty(x,y),pawnWhite(),_*),_*) -> {
        board[`x][`y] = board[`x][`y]+hight;
      }
      concPawnList(_*,concPawn(_*,empty(x,y),pawnWhite(),pawnWhite(),_*),_*) -> {
        board[`x][`y] = board[`x][`y]+hight;
      }
      concPawnList(_*,concPawn(_*,pawnWhite(),empty(x1,y1),empty(x2,y2),_*),_*) -> {
        board[`x1][`y1] = board[`x1][`y1]+low;
        board[`x2][`y2] = board[`x2][`y2]+low;
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawnWhite(),empty(x2,y2),_*),_*) -> {
        board[`x1][`y1] = board[`x1][`y1]+low;
        board[`x2][`y2] = board[`x2][`y2]+low;
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawnWhite(),_*),_*) -> {
        board[`x1][`y1] = board[`x1][`y1]+low;
        board[`x2][`y2] = board[`x2][`y2]+low;
      }
    }
  }

  public void humanPlay() {
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader stdin = new BufferedReader(isr);
      System.out.print("Line (1-"+(size)+") : ");
      String x = new String(stdin.readLine());
      System.out.print("Column (1-"+(size)+"): ");
      String y = new String(stdin.readLine());
      play(black,Integer.parseInt(x)-1,Integer.parseInt(y)-1);
    } catch(Exception e) {
      System.out.println("Error : "+e);
      humanPlay();
    }
  }

  public void computerPlay() {
    //Search horizontal
    searchPatterns(getHorizontalList());
    //Search vertical
    searchPatterns(getVerticalList());
    //Search diagonal
    searchPatterns(getDiagonalList());
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
      play(white,x,y);
    } catch(Exception e) {}
  }

}
