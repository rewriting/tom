package examples.lists;

//import examples.lists.blist.types.*;

public class BList {
	%gom{
		module BList
		imports int String boolean
		abstract syntax

		BList = empty() 
	        | con(head:Elem, tail:BList)
		
		Elem = cs(i:int)
	}
}