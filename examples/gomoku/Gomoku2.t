/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gomoku;

import aterm.pure.*;
import java.io.*;
import gomoku.gomoku.*;
import gomoku.gomoku.types.*;

class Gomoku2 {
  private Board board;
  private int size;
  private final static int black = -1;
  private final static int white = -2;

  %include { gomoku/Gomoku.tom }
  
  public static Gomoku2 getGomoku(int size) {
    Gomoku2 gomoku = new Gomoku2();
    gomoku.size = size;
    gomoku.board = new Board(size);
    return gomoku;
  } 

  public final static void main(String[] args) {
    int boardSize = 15;
    Gomoku2 test = getGomoku(boardSize);
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

    for(int i=0 ; i<1 ; i++) {
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
    }

    Pawn maxEmpty = board.getMaxEmpty();
    int x = maxEmpty.getx();
    int y = maxEmpty.gety();
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
      concPawnList(_*,X,_*) -> {
        %match(PawnList X){
          concPawn(_*,X1,X2,X3,X4,X5,X6*) ->{
            %match(Pawn X1, Pawn X2, Pawn X3, Pawn X4, Pawn X5, PawnList X6) { 
              pawn(c),pawn(c),pawn(c),pawn(c),empty(x5,y5),_ -> { board.addValue(`x5,`y5,value1); }
              pawn(c),pawn(c),pawn(c),empty(x4,y4),pawn(c),_ -> { board.addValue(`x4,`y4,value1); }
              pawn(c),pawn(c),empty(x3,y3),pawn(c),pawn(c),_ -> { board.addValue(`x3,`y3,value1); }
              empty(_,_),pawn(c),pawn(c),pawn(c),empty(x5,y5),concPawn(empty(_,_),_*) -> { board.addValue(`x5,`y5,value2); }
              empty(_,_),pawn(c),pawn(c),empty(x4,y4),pawn(c),concPawn(empty(_,_),_*) -> { board.addValue(`x4,`y4,value2); }
              empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),pawn(c),_ -> { board.addValue(`x1,`y1,value4); board.addValue(`x2,`y2,value4); }
              empty(x1,y1),pawn(c),empty(x3,y3),pawn(c),pawn(c),_ -> { board.addValue(`x1,`y1,value4); board.addValue(`x3,`y3,value4); }
              empty(x1,y1),pawn(c),pawn(c),empty(x4,y4),pawn(c),_ -> { board.addValue(`x1,`y1,value4); board.addValue(`x4,`y4,value4); }
              empty(x1,y1),pawn(c),pawn(c),pawn(c),empty(x5,y5),_ -> { board.addValue(`x1,`y1,value4); board.addValue(`x5,`y5,value4); }
              pawn(c),empty(x2,y2),empty(x3,y3),pawn(c),pawn(c),_ -> { board.addValue(`x2,`y2,value4); board.addValue(`x3,`y3,value4); }
              pawn(c),empty(x2,y2),pawn(c),empty(x4,y4),pawn(c),_ -> { board.addValue(`x2,`y2,value4); board.addValue(`x4,`y4,value4); }
              empty(_,_),empty(x2,y2),empty(x3,y3),pawn(c),pawn(c),concPawn(empty(_,_),_*) -> { board.addValue(`x2,`y2,value3); board.addValue(`x3,`y3,value3); }
              empty(_,_),empty(x2,y2),pawn(c),empty(x4,y4),pawn(c),concPawn(empty(_,_),_*) -> { board.addValue(`x2,`y2,value3); board.addValue(`x4,`y4,value3); }
              empty(_,_),pawn(c),empty(x3,y3),empty(x4,y4),pawn(c),concPawn(empty(_,_),_*) -> { board.addValue(`x3,`y3,value3); board.addValue(`x4,`y4,value3); }
              empty(_,_),empty(x2,y2),pawn(c),pawn(c),empty(x5,y5),concPawn(empty(_,_),_*) -> { board.addValue(`x2,`y2,value3); board.addValue(`x5,`y5,value3); }
              empty(x1,y1),empty(x2,y2),empty(x3,y3),pawn(c),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                //}
              }
              // __x_x
              empty(x1,y1),empty(x2,y2),pawn(c),empty(x4,y4),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x4,`y4,value5);
                //}
              }
              // __xx_
              empty(x1,y1),empty(x2,y2),pawn(c),pawn(c),empty(x5,y5),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x2,`y2,value5);
                board.addValue(`x5,`y5,value5);
                //}
              }
              // _x__x
              empty(x1,y1),pawn(c),empty(x3,y3),empty(x4,y4),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x3,`y3,value5);
                board.addValue(`x4,`y4,value5);
                //}
              }
              // _x_x_
              empty(x1,y1),pawn(c),empty(x3,y3),pawn(c),empty(x5,y5),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value5);
                board.addValue(`x3,`y3,value5);
                board.addValue(`x5,`y5,value5);
                //}
              }
              // x___x
              pawn(c),empty(x2,y2),empty(x3,y3),empty(x4,y4),pawn(c),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x2,`y2,value5);
                board.addValue(`x3,`y3,value5);
                board.addValue(`x4,`y4,value5);
                //}
              }
              // 1 in 6
              // ____x_
              empty(_,_),empty(x2,y2),empty(x3,y3),empty(x4,y4),pawn(_),concPawn(empty(_,_),_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x2,`y2,value6);
                board.addValue(`x3,`y3,value6);
                board.addValue(`x4,`y4,value6);
                //}
              }
              // ___x__
              empty(_,_),empty(x2,y2),empty(x3,y3),pawn(_),empty(x5,y5),concPawn(empty(_,_),_*) -> {
                //if(`c == patternColor) {
                board.addValue(`x2,`y2,value6);
                board.addValue(`x3,`y3,value6);
                board.addValue(`x5,`y5,value6);
                //}
              }
              //1 in 5
              // x____
              pawn(_),empty(x2,y2),empty(x3,y3),empty(x4,y4),empty(x5,y5),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x2,`y2,value7);
                board.addValue(`x3,`y3,value7);
                board.addValue(`x4,`y4,value7);
                board.addValue(`x5,`y5,value7);
                //}
              }
              // _x___
              empty(x1,y1),pawn(_),empty(x3,y3),empty(x4,y4),empty(x5,y5),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value7);
                board.addValue(`x3,`y3,value7);
                board.addValue(`x4,`y4,value7);
                board.addValue(`x5,`y5,value7);
                //}
              }
              // __x__
              empty(x1,y1),empty(x2,y2),pawn(_),empty(x4,y4),empty(x5,y5),_ -> {
                //if(`c == patternColor) {
                board.addValue(`x1,`y1,value7);
                board.addValue(`x2,`y2,value7);
                board.addValue(`x4,`y4,value7);
                board.addValue(`x5,`y5,value7);
                //}
              }
            }
          }
        }
      }
    }
  }
}
