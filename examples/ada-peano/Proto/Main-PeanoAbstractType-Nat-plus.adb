with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package body main.PeanoAbstractType.Nat.Plus is

	function make(x1: NatPtr; x2: NatPtr) return plus is 
	tempAdr : SharedObjectPtr ;
	begin

		initHashCode(plusGomProto.all,x1,x2);
		build(main.PeanoAbstractType.factory.all,plusGomProto.all,tempAdr);
		return plus(tempAdr.all) ;
	end;

	procedure initHashCode(this: in out plus; x1: NatPtr; x2: NatPtr) is
	begin
		this.x1 := x1 ;
		this.x2 := x2;

		this.hashCode := this.hashFunction ;
	end;

	procedure init(this: in out plus; x1: NatPtr; x2: NatPtr; hash: Integer) is
	begin
		this.x1 := x1 ;
		this.x2 := x2;

		this.hashCode := hash ;
	end;


	function symbolName(this: plus) return String is
	begin
		return "plus";
	end;

	function toString(this: plus) return String is
	begin
		return "plus("&this.x1.toString&","&this.x2.toString&")" ; 
	end;

	function getArity(this: plus) return Integer is
	begin
		return 2;
	end;

	function duplicate(this: plus) return plus is
		clone : access plus := new plus ;
	begin
        	clone.init(this.x1, this.x2, this.hashCode);
		return clone.all;
	end;

	function equivalent(this: plus; o: SharedObject'Class) return Boolean  is
	peer : plus ;
	begin
		if o in plus then
			peer :=	plus(o) ;
			return this.x1 = peer.x1 and this.x2 = peer.x2 ;
		end if;
		return false;
	end;

	function isplus(this: plus) return Boolean is
	begin
		return true;
	end;

	function getx1(this: plus) return Nat'Class is
	begin
		return this.x1.all;
	end;

	function setx1(this: plus; set_arg: NatPtr) return Nat'Class is
	begin
		return make(set_arg,this.x2);
	end;

	function getx2(this: plus) return Nat'Class is
	begin
		return this.x2.all;
	end;

	function setx2(this: plus; set_arg: NatPtr) return Nat'Class is
	begin
		return make(this.x1,set_arg);
	end;

	function hashFunction(this: plus) return Integer is
	begin 
	
		return(this.x1.hashCode+this.x2.hashCode); 
	
	end;

end main.PeanoAbstractType.Nat.Plus ; 
