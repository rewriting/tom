package gomoku;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import java.io.*;
import gomoku.gomoku.*;
import gomoku.gomoku.types.*;

class Gomoku2 {
  private gomokuFactory factory;
  private Board board;
  private int size;
  private final static int black = -1;
  private final static int white = -2;

  %include { gomoku/gomoku.tom }
  
  public static Gomoku2 getGomoku(int size, gomokuFactory factory) {
    Gomoku2 gomoku = new Gomoku2();
    gomoku.factory = factory;
    gomoku.size = size;
    gomoku.board = new Board(size,factory);
    return gomoku;
  } 

  public gomokuFactory getGomokuFactory() {
    return factory;
  }
  
  public final static void main(String[] args) {
    int boardSize = 20;
    Gomoku2 test = getGomoku(boardSize,gomokuFactory.getInstance(new PureFactory()));
    test.run();
  }

  public void run() {
    //    board.draw();
    long startChrono = System.currentTimeMillis();
    for(int i=0; i<size*size/2 ; i++) {
      // humanPlay(black);
      computerPlay(black);
      computerPlay(white);
      //board.draw();
      board.clear();
      //winnerIs();
    }
    if(size*size%2 == 1) {
      computerPlay(black);
      //board.draw();
      //winnerIs();
    }
      long stopChrono  = System.currentTimeMillis();
      System.out.println("play \n in " + (stopChrono-startChrono) + " ms");    
      System.exit(0);
  }

  public void humanPlay(int color) {
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader stdin = new BufferedReader(isr);
      //      System.out.print("Line (0-"+(size-1)+") : ");
      String x = new String(stdin.readLine());
      //      System.out.print("Column (0-"+(size-1)+"): ");
      String y = new String(stdin.readLine());
      board.play(color,Integer.parseInt(x),Integer.parseInt(y));
    } catch(Exception e) {
      System.out.println("Error : "+e);
      humanPlay(color);
    }
  }

  public void computerPlay(int color) {
    PawnListList hor = board.getHorizontalList();
    PawnListList reverseHor = mapReverse(hor);
    PawnListList ver = board.getVerticalList();
    PawnListList reverseVer = mapReverse(ver);
    PawnListList dia = board.getDiagonalList();
    PawnListList reverseDia = mapReverse(dia);

    //Search horizontal
    searchPatterns(hor,white);
    //searchPatterns(hor,black);
    searchPatterns(reverseHor,white);
    //searchPatterns(reverseHor,black);
    //Search vertical
    searchPatterns(ver,white);
    //searchPatterns(ver,black);
    searchPatterns(reverseVer,white);
    //searchPatterns(reverseVer,black);
    //Search diagonal
    searchPatterns(dia,white);
    //searchPatterns(dia,black);
    searchPatterns(reverseDia,white);
    //searchPatterns(reverseDia,black);

    Pawn maxEmpty = board.getMaxEmpty();
    int x = maxEmpty.getX();
    int y = maxEmpty.getY();
    //    System.out.println("Computer play on line: "+(x)+" column: "+(y));
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
        //        System.exit(0);
      }
    }
  }

  private PawnListList mapReverse(PawnListList l) {
    %match(PawnListList l) {
      concPawnList() -> { return l; }
      concPawnList(head,tail*) -> { 
        PawnListList rtail = mapReverse(`tail);
        return `concPawnList(reverse(head),rtail*); 
      }
    }
    return null;
  }

  private PawnList reverse(PawnList l) {
    %match(PawnList l) {
      concPawn() -> { return l; }
      concPawn(head,tail*) -> { 
        PawnList rtail = reverse(`tail);
        return `concPawn(rtail*,head); 
      }
    }
    return null;
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
      // xxxx_
      concPawnList(_*,X,_*) -> {
        %match(PawnList X){
          concPawn(_*,X1,X2,X3,X4,X5,X6*) ->{
            %match(Pawn X1, Pawn X2, Pawn X3, Pawn X4, Pawn X5, PawnList X6) { 
              pawn(c),pawn(c),pawn(c),pawn(c),empty(x,y),_ -> {
                // `board.analyse(concPawn(_*,pawn(c),pawn(c),pawn(c),pawn(c),value(x,y,value1),_*));
                //if(`c == patternColor)
                board.addValue(`x,`y,value1);
              }
              // xxx_x
              pawn(c),pawn(c),pawn(c),empty(x,y),pawn(c),_ -> {
                //if(`c == patternColor)
                board.addValue(`x,`y,value1);
              }
              // xx_xx
              pawn(c),pawn(c),empty(x,y),pawn(c),pawn(c),_ -> {
                //if(`c == patternColor)
                board.addValue(`x,`y,value1);
              }

              // 3 -> winning
              // _xxx__
              empty[],pawn(c),pawn(c),pawn(c),empty(x,y),concPawn(empty[],_*) -> {
                //if(`c == patternColor)
                board.addValue(`x,`y,value2);
              }
              // _xx_x_
              empty[],pawn(c),pawn(c),empty(x,y),pawn(c),concPawn(empty[],_*) -> {
                //if(`c == patternColor)
                board.addValue(`x,`y,value2);
              }
              // 3
              // __xxx
              empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value4);
                board.addValue(`x2,`y2,value4);
                //}
              }
              // _x_xx
              empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value4);
                board.addValue(`x2,`y2,value4);
                //}
              }
              // _xx_x
              empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value4);
                board.addValue(`x2,`y2,value4);
                //}
              }
              // _xxx_
              empty(x1,y1),pawn(c),pawn(c),pawn(c),empty(x2,y2),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value4);
                board.addValue(`x2,`y2,value4);
                //}
              }
              // x__xx
              pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value4);
                board.addValue(`x2,`y2,value4);
                //}
              }
              // x_x_x
              pawn(c),empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value4);
                board.addValue(`x2,`y2,value4);
                //}
              }
              // 2 in 6
              // ___xx_
              empty[],empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),concPawn(empty[],_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value3);
                board.addValue(`x2,`y2,value3);
                //}
              }
              // __x_x_
              empty[],empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),concPawn(empty[],_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value3);
                board.addValue(`x2,`y2,value3);
                //}
              }
              // _x__x_
              empty[],pawn(c),empty(x1,y1),empty(x2,y2),pawn(c),concPawn(empty[],_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value3);
                board.addValue(`x2,`y2,value3);
                //}
              }
              // __xx__
              empty[],empty(x1,y1),pawn(c),pawn(c),empty(x2,y2),concPawn(empty[],_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value3);
                board.addValue(`x2,`y2,value3);
                //}
              }
              // 2 in 5
              // ___xx
              empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // __x_x
              empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // __xx_
              empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),empty(x3,y3),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // _x__x
              empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // _x_x_
              empty(x1,y1),pawn(c),empty(x2,y2),pawn(c),empty(x3,y3),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // x___x
              pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // 1 in 6
              // ____x_
              empty[],empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),concPawn(empty[],_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value6);
                board.addValue(`x2,`y2,value6);
                board.addValue(`x3,`y3,value6);
                //}
              }
              // ___x__
              empty[],empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),concPawn(empty[],_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value6);
                board.addValue(`x2,`y2,value6);
                board.addValue(`x3,`y3,value6);
                //}
              }
              //1 in 5
              // x____
              pawn(c),empty(x1,y1),empty(x2,y2),empty(x3,y3),empty(x4,y4),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value7);
                board.addValue(`x2,`y2,value7);
                board.addValue(`x3,`y3,value7);
                board.addValue(`x4,`y4,value7);
                //}
              }
              // _x___
              empty(x1,y1),pawn(c),empty(x2,y2),empty(x3,y3),empty(x4,y4),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value7);
                board.addValue(`x2,`y2,value7);
                board.addValue(`x3,`y3,value7);
                board.addValue(`x4,`y4,value7);
                //}
              }
              // __x__
              empty(x1,y1),empty(x2,y2),pawn(c),empty(x3,y3),empty(x4,y4),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value7);
                board.addValue(`x2,`y2,value7);
                board.addValue(`x3,`y3,value7);
                board.addValue(`x4,`y4,value7);
                //}
              }
            }
          }
        }
      }
    }
  }
}
