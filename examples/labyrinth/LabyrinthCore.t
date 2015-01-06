/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package labyrinth;

import java.util.*;
import labyrinth.boulder.types.*;
import tom.library.sl.VisitFailure;

public class LabyrinthCore {

  %include { boulder/boulder.tom }
  %include { sl.tom }
  %include { java/util/HashMap.tom }

  %op Bead beadPlayer(north:Bead, south:Bead, east:Bead, west:Bead, direction:String) {
    is_fsym(t) { (t!=null) && t.isbead()  }
    get_slot(north,t) { getNorthBead(t) }
    get_slot(south,t) { getSouthBead(t) }
    get_slot(east,t) { getEastBead(t) } 
    get_slot(west,t) { getWestBead(t) }
    get_slot(direction,t) { direction }
  }
 
  private HashMap space;
  private Bead player = null;	
  private Bead goal = null;	
  private String direction = "east";

  private static final int SIZE = 20;
  String[] 
	  lab = { "********************" ,
			  "*   *    *        D*" ,
			  "*   *  ***         *" ,
			  "*                  *" ,
			  "******         *****" ,
			  "*    ***           *" ,
			  "*    *      *      *" ,
			  "*    *      *      *" ,
			  "*    ****  *********" ,
			  "**                 *" ,
			  "*     **         * *" ,
			  "*      *        ** *" ,
			  "*      ******   *  *" ,
			  "* *         *****  *" ,
			  "***     **   *     *" ,
			  "*        *   *     *" ,
			  "*   **   *****     *" ,
			  "*    *     *       *" ,
			  "*P   *     *       *" ,
			  "********************" };
   
  public boolean oneStep() throws VisitFailure {	
	// RANDOM	
/*    player = (Bead)`Pselect(1,2,		
			Pselect(1,2,MoveNorth(),MoveEast()),
			Pselect(1,2,MoveWest(),MoveSouth())).visitLight(player);*/
	// RANDOM - INCREASED PROBABILITY FOR EAST AND NORTH	
/*    player = (Bead)`Pselect(3,4,		
			Pselect(1,2,MoveNorth(),MoveEast()),
			Pselect(1,2,MoveWest(),MoveSouth())).visitLight(player);
 
*/	    
	// RIGHT HAND RULE ( FOLLOW RIGHT WALL )
	player = (Bead)`ChoiceId(MoveRight(),MoveRight()).visitLight(player);	

	// if the player reached the goal
	if (reachedGoal()){
		space.put(player.getpos(),`bead(player.getpos(),PlayerHappy()));	
		return true;
	}
	return false;
  }

  %strategy MoveNorth() extends Identity(){
	visit Bead {
		beadPlayer[north=b@bead[value=Empty()]] -> { return move(`b);}
	}
  }

  %strategy MoveSouth() extends Identity(){
	visit Bead {
		beadPlayer[south=b@bead[value=Empty()]] -> { return move(`b);}
	}
  }

  %strategy MoveEast() extends Identity(){
	visit Bead {
		beadPlayer[east=b@bead[value=Empty()]] -> { return move(`b);}
	}
  }

  %strategy MoveWest() extends Identity(){
	visit Bead {
		beadPlayer[west=b@bead[value=Empty()]] -> { return move(`b); }
	}
  }
  
  %strategy CheckDirection() extends Identity(){
	visit Bead {		
		beadPlayer[direction="east",south=bead[value=Empty()]] -> { direction="south";return player; }
		beadPlayer[direction="east",south=bead[value=Wall()],east=bead[value=Wall()]] ->  { direction="north";return player; }

		beadPlayer[direction="west",north=bead[value=Empty()]] -> { direction="north";return player; }
		beadPlayer[direction="west",north=bead[value=Wall()],west=bead[value=Wall()]] ->  { direction="south";return player; }

		beadPlayer[direction="north",east=bead[value=Empty()]] -> { direction="east";return player; }
		beadPlayer[direction="north",east=bead[value=Wall()],north=bead[value=Wall()]] ->  { direction="west";return player; }

		beadPlayer[direction="south",west=bead[value=Empty()]] -> { direction="west";return player;}
		beadPlayer[direction="south",west=bead[value=Wall()],south=bead[value=Wall()]] ->  { direction="east";return player; }
	}
  }  	
  
  %strategy MoveRight() extends Identity(){
	visit Bead {
		beadPlayer[direction="east",south=b@bead[value=Empty()]] -> { direction="south";return move(`b); }
		beadPlayer[direction="east",south=bead[value=Wall()],east=b@bead[value=Empty()]] -> { return move(`b); }
		beadPlayer[direction="east",south=bead[value=Wall()],east=bead[value=Wall()]] ->  { direction="north"; }

		beadPlayer[direction="west",north=b@bead[value=Empty()]] -> { direction="north";return move(`b); }
		beadPlayer[direction="west",north=bead[value=Wall()],west=b@bead[value=Empty()]] -> { return move(`b); }
		beadPlayer[direction="west",north=bead[value=Wall()],west=bead[value=Wall()]] ->  { direction="south"; }

		beadPlayer[direction="north",east=b@bead[value=Empty()]] -> { direction="east";return move(`b); }
		beadPlayer[direction="north",east=bead[value=Wall()],north=b@bead[value=Empty()]] -> { return move(`b); }
		beadPlayer[direction="north",east=bead[value=Wall()],north=bead[value=Wall()]] ->  { direction="west"; }

		beadPlayer[direction="south",west=b@bead[value=Empty()]] -> { direction="west";return move(`b); }
		beadPlayer[direction="south",west=bead[value=Wall()],south=b@bead[value=Empty()]] -> { return move(`b); }
		beadPlayer[direction="south",west=bead[value=Wall()],south=bead[value=Wall()]] ->  { direction="east"; }
	}
  }
  

  private Bead move(Bead bead){
    Coord newCoord = bead.getpos();
	// update space
	space.put(player.getpos(),`bead(player.getpos(),Empty()));	
	player = `bead(newCoord,Player());
	space.put(newCoord,player);	
	return `bead(newCoord,Player());
  } 

  public void initialize() {
	for (int i=0; i < SIZE; i++){
		for (int j=0; j < SIZE; j++){
			char c = lab[i].charAt(j);
			%match (c){
				'*' -> { putBead(j,i,`Wall());}
				'D' -> { goal = `bead(pos(j,i),Goal());space.put(`pos(j,i),goal);}
				'P' -> { player = `bead(pos(j,i),Player());space.put(`pos(j,i),player);}
				' ' -> { putBead(j,i,`Empty());}
			}
		}	
    }    
  } 

  public void showMaze() {
	System.out.println("\n\n");
	for (int i=0; i < SIZE; i++){
		for (int j=0; j < SIZE; j++){
			Bead b = (Bead)space.get(`pos(j,i));			
			%match (b){
				bead[value=Empty()] -> { System.out.print(" ");}
				bead[value=Wall()] -> { System.out.print("*");}
				bead[value=Goal()] -> { System.out.print("D");}
				bead[value=Player()] -> { System.out.print("P");}
				bead[value=Walked()] -> { System.out.print("W");}
			}
		}
		System.out.println("");	
    }    
  } 

  
  public void run() throws VisitFailure{
    space = new HashMap();
    initialize();
    boolean fire = true; 
    while(fire) {
      //System.out.println(toMatrix(space));
      fire = oneStep();
    }
  }
  
 public HashMap start() {
    space = new HashMap();
    initialize();
    return space;
  }

  public boolean getStep() throws VisitFailure{
    return oneStep();     
  }

  private void putBead(int x, int y, BeadType beadType) {
    Coord p = `pos(x,y);
    Bead b = `bead(p,beadType);
    space.put(p, b);
  }

  private Bead getNorthBead(Bead b) {
    return (Bead) space.get(getNorthCoord(b.getpos()));
  }

  private Bead getSouthBead(Bead b) {
    return (Bead) space.get(getSouthCoord(b.getpos()));
  }

  private Bead getEastBead(Bead b) {
    return (Bead) space.get(getEastCoord(b.getpos()));
  }

  private Bead getWestBead(Bead b) {
    return (Bead) space.get(getWestCoord(b.getpos()));
  }

  private Coord getNorthCoord(Coord p) {
    return `pos(p.getx(),p.gety()-1);
  }

  private Coord getSouthCoord(Coord p) {
    return `pos(p.getx(),p.gety()+1);
  }

  private Coord getEastCoord(Coord p) {
    return `pos(p.getx()+1,p.gety());
  }

  private Coord getWestCoord(Coord p) {
    return `pos(p.getx()-1,p.gety());
  }

  private boolean reachedGoal() {
	if (goal.getpos() == getWestCoord(player.getpos())
		|| goal.getpos() == getEastCoord(player.getpos())
		|| goal.getpos() == getNorthCoord(player.getpos())
		|| goal.getpos() == getSouthCoord(player.getpos())){
		return true;
	}
	return false;
  }

  public HashMap getSpace(){
	return space;
  }	
}

/*

player = (Bead)`ChoiceId(CheckDirection(),
				ChoiceId(
					ChoiceId(MoveEast(),MoveWest()),
					ChoiceId(MoveNorth(),MoveSouth())
				)).visitLight(player);     

  %strategy MoveNorth() extends Identity(){
	visit Bead {
		beadPlayer[direction="north",north=b@bead[value=Empty()]] -> { return move(`b);}
	}
  }

  %strategy MoveSouth() extends Identity(){
	visit Bead {
		beadPlayer[direction="south",south=b@bead[value=Empty()]] -> { return move(`b);}
	}
  }

  %strategy MoveEast() extends Identity(){
	visit Bead {
		beadPlayer[direction="east",east=b@bead[value=Empty()]] -> { return move(`b);}
	}
  }

  %strategy MoveWest() extends Identity(){
	visit Bead {
		beadPlayer[direction="west",west=b@bead[value=Empty()]] -> { return move(`b); }
	}
  }
  
  %strategy CheckDirection() extends Identity(){
	visit Bead {		
		beadPlayer[direction="east",south=bead[value=Empty()]] -> { direction="south";return player; }
		beadPlayer[direction="east",south=bead[value=Wall()],east=bead[value=Wall()]] ->  { direction="north";return player; }

		beadPlayer[direction="west",north=bead[value=Empty()]] -> { direction="north";return player; }
		beadPlayer[direction="west",north=bead[value=Wall()],west=bead[value=Wall()]] ->  { direction="south";return player; }

		beadPlayer[direction="north",east=bead[value=Empty()]] -> { direction="east";return player; }
		beadPlayer[direction="north",east=bead[value=Wall()],north=bead[value=Wall()]] ->  { direction="west";return player; }

		beadPlayer[direction="south",west=bead[value=Empty()]] -> { direction="west";return player;}
		beadPlayer[direction="south",west=bead[value=Wall()],south=bead[value=Wall()]] ->  { direction="east";return player; }
	}
  }
*/
