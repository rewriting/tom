package polygraphes;

import polygraphes.polygraphicprogram.*;
import polygraphes.polygraphicprogram.types.*;


public class PolygraphicProgram {

%include { polygraphicprogram/PolygraphicProgram.tom }

public static void main(String[] args) {

//jeu de cellules pour les tests : 

//1-chemin reprsentant les entiers naturels
	OnePath nat=`OneCell("nat");
//constructeurs sur les entiers naturels
	TwoPath zero=`TwoCell("zero",OneId(),nat,Constructor());
	TwoPath succ =`TwoCell("succ",nat,nat,Constructor());
//2-cellules de structure
	TwoPath eraser= `TwoCell("eraser",nat,OneId(),Structure());
	TwoPath duplication= `TwoCell("duplication",nat,OneC0(nat,nat),Structure());
	TwoPath permutation = `TwoCell("permutation",OneC0(nat,nat),OneC0(nat,nat),Structure());
//addition, soustraction et division
	TwoPath plus = `TwoCell("plus",OneC0(nat,nat),nat,Function());
	TwoPath minus = `TwoCell("minus",OneC0(nat,nat),nat,Function());
	TwoPath division = `TwoCell("division",OneC0(nat,nat),nat,Function());
	TwoPath multiplication = `TwoCell("multiplication",OneC0(nat,nat),nat,Function());
//3-cellules de structure
	ThreePath zeroPerm1 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(zero,TwoId(nat)),permutation),TwoC0(TwoId(nat),zero),Structure());
	ThreePath zeroPerm2 = `ThreeCell("zeroPerm1",TwoC1(TwoC0(TwoId(nat),zero),permutation),TwoC0(zero,TwoId(nat)),Structure());
	ThreePath zeroDup = `ThreeCell("zeroDup",TwoC1(zero,duplication),TwoC0(zero,zero),Structure());
	ThreePath zeroEraz = `ThreeCell("zeroEraz",TwoC1(zero,eraser),TwoId(OneId()),Structure());
	ThreePath succPerm1 = `ThreeCell("succPerm1",TwoC1(TwoC0(succ,TwoId(nat)),permutation),TwoC0(TwoId(nat),succ),Structure());
	ThreePath succPerm2 = `ThreeCell("succPerm1",TwoC1(TwoC0(TwoId(nat),succ),permutation),TwoC0(succ,TwoId(nat)),Structure());
	ThreePath succDup = `ThreeCell("succDup",TwoC1(succ,duplication),TwoC0(succ,succ),Structure());
	ThreePath succEraz = `ThreeCell("succEraz",TwoC1(zero,succ),TwoId(OneId()),Structure());
//regles
	ThreePath plusZero = `ThreeCell("plusZero",TwoC1(TwoC0(zero,TwoId(nat)),plus),TwoId(nat),Function());
	ThreePath plusSucc = `ThreeCell("plusSucc",TwoC1(TwoC0(succ,TwoId(nat)),plus),TwoC1(plus,succ),Function());
	ThreePath minusZero1 = `ThreeCell("minusZero1",TwoC1(TwoC0(zero,TwoId(nat)),minus),TwoC1(eraser,zero),Function());
	ThreePath minusZero2 = `ThreeCell("minusZero1",TwoC1(TwoC0(TwoId(nat),zero),minus),TwoId(nat),Function());
	ThreePath minusDoubleSucc = `ThreeCell("minusDoubleSucc",TwoC1(TwoC0(succ,succ),minus),minus,Function());
	ThreePath divZero = `ThreeCell("divZero",TwoC1(TwoC0(zero,TwoId(nat)),division),TwoC1(eraser,zero),Function());	
	ThreePath divSucc = `ThreeCell("divSucc",TwoC1(TwoC0(succ,TwoId(nat)),division),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ),Function());
	ThreePath multZero = `ThreeCell("multZero",TwoC1(TwoC0(zero,TwoId(nat)),multiplication),TwoC1(eraser,zero),Function());	
	ThreePath multSucc = `ThreeCell("multSucc",TwoC1(TwoC0(succ,TwoId(nat)),multiplication),TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(multiplication,TwoId(nat)),plus),Function());


//System.out.println(threeT2(divSucc));
//System.out.println(`TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ));
//System.out.println(twoDefined(`TwoC1(TwoC0(TwoId(nat),duplication),TwoC0(minus,TwoId(nat)),division,succ)));//renvoie false-->pas bon
//System.out.println(twoS1(division));
//System.out.println(twoT1(`TwoC0(minus,TwoId(nat))));
//System.out.println(`TwoId(nat));
//System.out.println(twoT1(`TwoId(nat)));
//System.out.println(twoDefined(`TwoC1(TwoC0(minus,TwoId(nat)),division)));
//System.out.println(threeS2(`ThreeId(division)));
//`ThreeCell("erreurvoulue",eraser,permutation,Structure());

}

//pour chacune des fonctions suivantes, on suppose les chemins pralablement bien forms (pour l'instant)
//il faudra vrifier  un moment que c'est bien form mais les fonctions pour vrifier utilisent des fonctions qui fonctionnent si c'est dj bien form
//donc il va falloir affiner tout a
public static OnePath twoS1(TwoPath t){
%match (t){
			TwoId(X) -> { return `X; }
			TwoCell[Source=x] -> { return `x; }
			TwoC0(head,tail*) -> { return `OneC0(twoS1(head),twoS1(tail*)); }
			TwoC1(head,tail*) -> { return twoS1(`head); }
}
//return `OneId();
throw new tom.engine.exception.TomRuntimeException("strange term: "+t);
}

public static OnePath twoT1(TwoPath t){
%match (t){
			TwoId(X) -> { return `X; }
			TwoCell[Target=x] -> { return `x; }
			TwoC0(head,tail*) -> { return `OneC0(twoT1(head),twoT1(tail*)); }
			TwoC1(head*,tail) -> { return twoT1(`tail); }
}
//return `OneId();
throw new tom.engine.exception.TomRuntimeException("strange term: "+t);
}

public static OnePath threeS1(ThreePath t){
%match (t){	
			ThreeId(X) -> { return `twoS1(X); }
			ThreeCell[Source=x] -> { return `twoS1(x); }
			ThreeC0(head,tail*) -> { return `OneC0(threeS1(head),threeS1(tail*)); }
			ThreeC1(head,tail*) -> { return threeS1(`head); }
			ThreeC2(head,tail*) -> { return threeS1 (`head); }
}
//return `OneId();
throw new tom.engine.exception.TomRuntimeException("strange term: "+t);
}

public static OnePath threeT1(ThreePath t){
%match (t){
			ThreeId(X) -> { return `twoT1(X); }
			ThreeCell[Target=x] -> { return `twoT1(x); }
			ThreeC0(head,tail*) -> { return `OneC0(threeT1(head),threeT1(tail*)); }
			ThreeC1(head*,tail) -> { return threeT1(`tail); }
			ThreeC2(head,tail*) -> { return threeT1 (`head); }
}
//return `OneId();
throw new tom.engine.exception.TomRuntimeException("strange term: "+t);
}

public static TwoPath threeS2(ThreePath t){
%match (t){
			ThreeId(X) -> { return `X; }
			ThreeCell[Source=x] -> { return `x; }
			ThreeC0(head,tail*) -> { return `TwoC0(threeS2(head),threeS2(tail*)); }
			ThreeC1(head,tail*) -> { return `TwoC1(threeS2(head),threeS2(tail*)); }
			ThreeC2(head,tail*) -> { return threeS2 (`head); }
}
//return `TwoId(OneId());
throw new tom.engine.exception.TomRuntimeException("strange term: "+t);
}

public static TwoPath threeT2(ThreePath t){
%match (t){
			ThreeId(X) -> { return `X; }
			ThreeCell[Target=x] -> { return `x; }
			ThreeC0(head,tail*) -> { return `TwoC0(threeT2(head),threeT2(tail*)); }
			ThreeC1(head,tail*) -> { return `TwoC1(threeT2(head),threeT2(tail*)); }
			ThreeC2(head*,tail) -> { return threeT2 (`tail); }
}
//return `TwoId(OneId());
throw new tom.engine.exception.TomRuntimeException("strange term: "+t);
}

public static boolean oneDefined(OnePath o){return true;}
public static boolean twoDefined(TwoPath t){
%match (t){
			TwoCell[] -> { return true; }
			TwoC0(_*) -> { return true; }
			TwoC1(head, tail*) -> { return `twoT1(head)==`twoS1(tail)&&twoDefined(`tail);}
	}
return false;
}
public static boolean threeDefined(ThreePath t){
%match (t){
			ThreeCell[] -> { return true; }
			ThreeC0(_*) -> { return true; }
			ThreeC1(ThreeCell[Source=source1],ThreeCell[Source=source2],tail*) -> { return `twoT1(source1)==`twoS1(source2)&&threeDefined(`tail);}
			ThreeC2(ThreeCell[Target=target1],ThreeCell[Source=source2],tail*) -> { return `target1==`source2&&threeDefined(`tail);}
	}
return false;
}
}
