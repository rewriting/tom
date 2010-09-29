package modele;

import java.util.ArrayList;

public interface StructureComposition extends Structure{

	
	public ArrayList<Fil> recupererFils(Structure s,boolean b);
	public Structure getF();
	public Structure getG();
	
	
}
