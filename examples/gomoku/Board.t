package gomoku;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import java.io.*;
import gomoku.gomoku.*;
import gomoku.gomoku.types.*;

class Board {

  %include { gomoku/gomoku.tom }

  private final static int black = -1;
  private final static int white = -2;
  private int size;

  private gomokuFactory factory;
  private int[][] board;

  public Board(int size, gomokuFactory factory) {
    this.size = size;
    this.board = new int[size][size];
    this.factory = factory;
  } 

  public gomokuFactory getGomokuFactory() {
    return factory;
  }

  public int getWhite() {
    return white;
  }

  public int getBlack() {
    return black;
  }

  public void draw() {
    System.out.print("  \t"); 
    for(int j=0 ; j<size ; j++) {
      System.out.print((j%10) + " "); 
    }
    System.out.println();

    for(int i=0; i<size ; i++) {
      System.out.print(i + "\t"); 
      for(int j=0 ; j<size ; j++) {
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

    System.out.print("  \t"); 
    for(int j=0 ; j<size ; j++) {
      System.out.print((j%10) + " "); 
    }
    System.out.println();

  }

  public void clear() {
    for(int i=0; i<size ; i++) {
      for(int j=0 ; j<size ; j++) {
        if(board[i][j]>0) {
          board[i][j] = 0;
        }
      }
    }
  }

  public void play(int color, int x, int y) throws Exception {
    if(((x>-1)&&(y>-1))&&((x<size)&&(y<size))&&(board[x][y]>=0)) {
      board[x][y] = color;
    } else {
      throw new Exception("Out of bound or not empty");
    }
  }

  public Pawn getPawn(int x, int y) {
    switch(board[x][y]) {
    case black :
      return `pawn(black);
    case white:
      return `pawn(white);
    default:
      return `empty(x,y);
    }
  }

  public void addValue(int x, int y, int value) {
    board[x][y] += value;
  }

  public Pawn getMaxEmpty() {
    int max = -1;
    int x = -1;
    int y = -1;
    for(int i=0; i<size ; i++) {
      for(int j=0 ; j<size ; j++) {
        if(board[i][j]>max) {
          max = board[i][j];
          x = i;
          y = j;
        }
      }
    }
    return `empty(x,y);
  }

  public PawnListList getHorizontalList() {
    PawnListList list = `concPawnList();
    for(int i=0; i<size ; i++) {
      PawnList raw = `concPawn();
      for(int j=0 ; j<size ; j++) {
        raw = `concPawn(raw*,getPawn(i,j));
      }
      list = `concPawnList(list*,raw);
     }
    //System.out.println(list);
    return list;
  }

  public PawnListList getVerticalList() {
    PawnListList list = `concPawnList();
    for(int j=0; j<size ; ++j) {
      PawnList column = `concPawn();
      for(int i=0 ; i<size ; i++) {
        column = `concPawn(column*,getPawn(i,j));
      }
      list = `concPawnList(list*,column);
     }
    //System.out.println(list);
    return list;
  }

  public PawnListList getDiagonalList() {
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

}
