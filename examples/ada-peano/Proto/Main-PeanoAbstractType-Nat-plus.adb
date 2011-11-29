with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package body main.PeanoAbstractType.Nat.Plus is

	function make(x1: NatPtr; x2: NatPtr) return Plus is 
	begin

		plusGomProto.all.initHashCode(x1,x2);
		return plus(build(main.PeanoAbstractType.factory.all,plusGomProto.all));
	end;


	procedure init(this: in out plus; x1: NatPtr; x2: NatPtr; hash: Integer) is
	begin
		this.x1 := x1 ;
		this.x2 := x2;

		this.hashCode := hash ;
	end;

		procedure initHashCode(this: in out plus; x1: NatPtr; x2: NatPtr) is
	begin
		this.x1 := x1 ;
		this.x2 := x2 ;

		this.hashCode := this.hashFunction ;
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

	function duplicate(this: plus) return SharedObject'Class is
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
			return (equivalent(this.x1.all,peer.x1.all))and(equivalent(this.x2.all,peer.x2.all)) ;
		end if;
		return false;
	end;

	function isplus(this: plus) return Boolean is
	begin
		return true;
	end;

	function hashFunction(this: plus) return Integer is
	begin 
	
		return(this.x1.hashCode+this.x2.hashCode); 
	
	end;

end main.PeanoAbstractType.Nat.Plus ; 
