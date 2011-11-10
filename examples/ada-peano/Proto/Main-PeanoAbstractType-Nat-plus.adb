with main.PeanoAbstractType.Nat; use main.peanoabstracttype.nat;

package body peano.types.nat.plus is

	function make(x1: Nat, x2: Nat) return plus is 
	begin

		this.gomProto.initHashCode(x1,x2);
		return (plus) factory.build(gomProto);
	end;

	procedure init(this: plus, x1: Nat, x2: Nat, hashCode: Integer) is
	begin
		this.x1 = x1;
		this.x2 = x2;
		this.hashCode = hashCode;
	end;

	procedure initHashCode(this: plus) is
	begin
		this.x1 = x1;
		this.x2 = x2;

		this.hashCode = this.hashFunction() ;
	end;

	function symbolName(this: plus) return String is
	begin
		return "plus";
	end;

	function getArity(this: plus) return Integer is
	begin
		return 2;
	end;

	function duplicate(this: plus) return SharedObject'Class is
	begin
		clone := new plus
        	clone.init(this.x1, this.x2, this.hashCode);
		return clone;
	end;

	function compareToLPO(this: plus, o: Object'Class) return Integer is
	begin
		-- Compare directly
		ao : peano.PeanoAbstractType := (peano.PeanoAbstractType) o ;
		if ao = this then return 0; end if; 
		-- Compare by name
		symbCmp : Integer := this.symbolName().compareTo(ao.symbolName) ;
		if symbCmp!=0 then return symbCmp; end if;
		-- Compare childs
		tco : plus := (plus) ao;

		x1Cmp : Integer := (this.x1).compareToLPO(tco.x1)
		if x1Cmp != 0 then return x1Cmp ; end if;

		x2Cmp : Integer := (this.x2).compareToLPO(tco.x2)
		if x2Cmp != 0 then return x2Cmp ; end if;


		Ada.TextIO.Put("Unable to compare");

	end;

	function compareTo(this: plus, o: Object'Class) return Integer is
	begin

		-- Compare directly
		ao : peano.PeanoAbstractType := (peano.PeanoAbstractType) o ;
		if ao = this then return 0; end if; 

		-- Compare by hashCode
		-- TODO
		if this.hashCode != ao.hashCode then return (this.hashCode < ao.hashCode())?-1:1; end if;


		-- Compare by name
		symbCmp : Integer := this.symbolName().compareTo(ao.symbolName) ;
		if symbCmp!=0 then return symbCmp; end if;
		-- Compare childs
	tco : plus := (plus) ao;

		x1Cmp : Integer := (this.x1).compareToLPO(tco.x1)
		if x1Cmp != 0 then return x1Cmp ; end if;

		x2Cmp : Integer := (this.x2).compareToLPO(tco.x2)
		if x2Cmp != 0 then return x2Cmp ; end if;

		

		Ada.Text_IO.Put("Unable to compare");
	end;

	function hashCode(this: plus) return Integer is 
	begin
		return this.hashCode;
	end;

	function equivalent(this: plus, o: SharedObject'Class) return Boolean  is
	begin
		if o in plus then
			peer: plus := (plus) o;
			return this.x1 = peer.pred ;
		end if;
		return false;
	end;

	function isplus(this: plus) return Boolean is
	begin
		return true;
	end;

	function getx1(this: plus) return Nat'Class is
	begin
		return this.x1;
	end;

	function setx1(this: plus, set_arg: Nat) return Nat'Class is
	begin
		return make(set_arg,this.x2);
	end;

	function getx2(this: plus) return Nat'Class is
	begin
		return this.x2;
	end;

	function setx2(this: plus, set_arg: Nat) return Nat'Class is
	begin
		return make(this.x1,set_arg);
	end;

	function getChildcount(this: access Visitable'Class) return Integer is
	begin
		return 2;
	end;

	function getChildAt(this: access Visitable'Class, index: Integer) return VisitablePtr is
	begin
		case index is
			when 0 => return this.x1'Access;
			when 1 => return this.x2'Access;
			when others => raise Outofbound;
		end case;
	end;


	function setChildAt(this: access Visitable'Class, index: Integer, v: access Visitable'Class) return VisitablePtr is
	begin

		case index is
			when 0 => return make(v.all, this.all.x2)'Access;
			when 1 => return make(this.all.x1, v.all)'Access;
			when others => raise Outofbound;
		end case;
	
	end;

	function setChildren(this: access Visitable'Class, childs: ObjectPtrArrayPtr ) return ObjectPtrArrayPtr is
	begin
		if childs.all(0).all in Nat and child.all(1).all in Nat
			then return make(childs.all(0).all, childs.all(1).all)'Access;
			else raise Incorrecttype;
		end if;
	end;

	function getChildren(this: access Visitable'Class) return ObjectPtrArrayPtr is
	begin
		res: array Natural range <> of tom.library.sl.Visitable;
		res(0) := this.all.x1 ;
		res(1) := this.all.x2 ;
		return res'Access;
	end;


	function hashFunction is
	begin 
		-- ?? TODO
	end;

