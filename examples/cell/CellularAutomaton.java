/*
 * 	CellularAutomaton.java
 * 	par Emmanuel Hainry et Blaise Potard
 *
 * 	classe abstraite que doivent etendre les automates cellulaires
 */

package cell;

public abstract class CellularAutomaton {

public abstract int nextGeneration(int[] Neighbourhood) ;

public abstract int[] nextGenerationConfig(int[] Config) ;

}
