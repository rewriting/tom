ith Ada.Text_IO; use Ada.Text_IO;

procedure Main is  

%typeterm Nat {
  implement { main.peano.types.Nat }
  is_sort(t) { ($t in main.peano.types.Nat) }

  equals(t1,t2) { ($t1=$t2) }

}
%op Nat zero() {
  is_fsym(t) { ($t in main.peano.types.nat.zero) }
  make() { main.peano.types.nat.zero.make() }
}

%op Nat suc(pred:Nat) {
  is_fsym(t) { ($t in main.peano.types.nat.suc) }
  get_slot(pred, t) { $t.getpred() }
  make(t0) { main.peano.types.nat.suc.make($t0) }
}

%op Nat plus(x1:Nat, x2:Nat) {
  is_fsym(t) { ($t in main.peano.types.nat.plus) }
  get_slot(x1, t) { $t.getx1() }
  get_slot(x2, t) { $t.getx2() }
  make(t0, t1) { main.peano.types.nat.plus.make($t0, $t1) }
}

  procedure evaluate(n: Nat) is
begin

    %match(n) {
      plus(x, zero()) -> { return `x; }
      plus(x, suc(y)) -> { return `suc(plus(x,y)); }
    }
    return n;

end evaluate;



    two: Nat := `plus(suc(zero()),suc(zero()));

begin

    Put_Line(two);
    two := evaluate(two);
    Put_Line(two);
    
	

end Main;

