package gomoku;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import java.io.*;
import gomoku.gomoku.*;
import gomoku.gomoku.types.*;

class Gomoku {
  private gomokuFactory factory;
  private Board board;
  private int size;
  private final static int black = -1;
  private final static int white = -2;

  %include { gomoku.tom }
  
  public static Gomoku getGomoku(int size, gomokuFactory factory) {
    Gomoku gomoku = new Gomoku();
    gomoku.factory = factory;
    gomoku.size = size;
    gomoku.board = new Board(size,factory);
    return gomoku;
  } 

  public gomokuFactory getGomokuFactory() {
    return factory;
  }
  
  public final static void main(String[] args) {
    int boardSize = 15;
    Gomoku test = getGomoku(boardSize,gomokuFactory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    board.draw();
    for(int i=0; i<size*size/2 ; i++) {
      humanPlay(black);
      computerPlay(white);
      board.draw();
      board.clear();
      winnerIs();
    }
    if(size*size%2 == 1) {
      humanPlay(black);
      board.draw();
      winnerIs();
    }
  }

  public void humanPlay(int color) {
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader stdin = new BufferedReader(isr);
      System.out.print("Line (0-"+(size-1)+") : ");
      String x = new String(stdin.readLine());
      System.out.print("Column (0-"+(size-1)+"): ");
      String y = new String(stdin.readLine());
      board.play(color,Integer.parseInt(x),Integer.parseInt(y));
    } catch(Exception e) {
      System.out.println("Error : "+e);
      humanPlay(color);
    }
  }

  public void computerPlay(int color) {
    //Search horizontal
    searchPatterns(board.getHorizontalList(),white);
    searchPatterns(board.getHorizontalList(),black);
    //Search vertical
    searchPatterns(board.getVerticalList(),white);
    searchPatterns(board.getVerticalList(),black);
    //Search diagonal
    searchPatterns(board.getDiagonalList(),white);
    searchPatterns(board.getDiagonalList(),black);

    Pawn maxEmpty = board.getMaxEmpty();
    int x = maxEmpty.getX();
    int y = maxEmpty.getY();
    System.out.println("Computer play on line: "+(x)+" column: "+(y));
    try {
      board.play(color,x,y);
    } catch(Exception e) {}
  }

  public void winnerIs() {
    winningPattern(board.getHorizontalList());
    winningPattern(board.getVerticalList());
    winningPattern(board.getDiagonalList());
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
        `board.addValue(x,y,value1);

          `board.addValue(x,y,value1);
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),pawn(c),empty(x,y),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value1);
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x,y),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value1);
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x,y),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value1);
      }
      concPawnList(_*,concPawn(_*,empty(x,y),pawn(c),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value1);
      }
      // 3 -> winning
      concPawnList(_*,concPawn(_*,empty[],pawn(c),pawn(c),pawn(c),empty(x,y),empty[],_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value2);
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),pawn(c),empty(x,y),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value2);
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x,y),pawn(c),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value2);
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x,y),pawn(c),pawn(c),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor)
          `board.addValue(x,y,value2);
      }
      // 3
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),pawn(c),pawn(c),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value4);
          `board.addValue(x2,y2,value4);
          //}
      }
      // 2 in 6
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value3);
          `board.addValue(x2,y2,value3);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value3);
          `board.addValue(x2,y2,value3);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value3);
          `board.addValue(x2,y2,value3);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value3);
          `board.addValue(x2,y2,value3);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value3);
          `board.addValue(x2,y2,value3);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value3);
          `board.addValue(x2,y2,value3);
          //}
      }
      // 2 in 5
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      concPawnList(_*,concPawn(_*,pawn(c),pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value5);
          `board.addValue(x2,y2,value5);
          `board.addValue(x3,y3,value5);
          //}
      }
      // 1 in 6
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value6);
          `board.addValue(x2,y2,value6);
          `board.addValue(x3,y3,value6);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value6);
          `board.addValue(x2,y2,value6);
          `board.addValue(x3,y3,value6);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value6);
          `board.addValue(x2,y2,value6);
          `board.addValue(x3,y3,value6);
          //}
      }
      concPawnList(_*,concPawn(_*,empty[],pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),empty[],_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value6);
          `board.addValue(x2,y2,value6);
          `board.addValue(x3,y3,value6);
          //}
      }
      //1 in 5
      concPawnList(_*,concPawn(_*,pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value7);
          `board.addValue(x2,y2,value7);
          `board.addValue(x3,y3,value7);
          `board.addValue(x4,y4,value7);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value7);
          `board.addValue(x2,y2,value7);
          `board.addValue(x3,y3,value7);
          `board.addValue(x4,y4,value7);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value7);
          `board.addValue(x2,y2,value7);
          `board.addValue(x3,y3,value7);
          `board.addValue(x4,y4,value7);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),empty(x4,y4),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value7);
          `board.addValue(x2,y2,value7);
          `board.addValue(x3,y3,value7);
          `board.addValue(x4,y4,value7);
          //}
      }
      concPawnList(_*,concPawn(_*,empty(x1,y1),empty(x2,y2),empty(x3,y3),empty(x4,y4),pawn(c),_*),_*) -> {
        //if(`c == patternColor) {
          `board.addValue(x1,y1,value7);
          `board.addValue(x2,y2,value7);
          `board.addValue(x3,y3,value7);
          `board.addValue(x4,y4,value7);
          //}
      }
    }
  }

}
