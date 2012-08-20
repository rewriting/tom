package body TestPosition is

procedure run_test is
begin
	
	-- testEquals
	declare
	p1 : Position := makeFromArray(new IntArray'(1,1));
	p2 : Position := makeFromArray(new IntArray'(1,1));
	begin
	assert(p1.equals(p2), "Error in equals");
	end;

	--	testNormalize
	declare
	p : Position  := makeFromArray(new IntArray'(1,-1,2,-2,1,2,3,-3,1,1));
	pp  : Position := p.getCanonicalPath;
	begin
	assert(pp.equals(makeFromArray(new IntArray'(1,2,1,1))), "Error in normalize");
	end;

	-- testAdd
	declare
	p1 : Position := makeFromArray(new IntArray'(1,1,2,1));
	p2 : Position := makeFromArray(new IntArray'(1,2,1,2,1));
	r : Position := p2.add(p1);
	begin
	r := r.getCanonicalPath;
	assert(r.equals(makeFromArray(new IntArray'(1,2,1,2,1,1,1,2,1))), "Error in add");
	end;

	-- testInverse

	declare
	p : Position := makeFromArray(new IntArray'(1,1,2));
	r : Position := p.inverse;
	begin
	r := r.getCanonicalPath;
	assert(r.equals(makeFromArray(new IntArray'(-2,-1,-1))), "Error in inverse");
	end;

	-- testSub
	declare
	p1 : Position := makeFromArray(new IntArray'(1,1,2,1));
	p2 : Position := makeFromArray(new IntArray'(1,2,1,2,1));
	r : Position := p2.sub(p1);
	begin
	r := r.getCanonicalPath;
	assert(r.equals(makeFromArray(new IntArray'(-1, -2, -1, 2, 1, 2, 1))), "Error in sub");
	end;

	-- testHashCode
	declare
	p1 : Position := makeFromArray(new IntArray'(1,1));
	p2 : Position := makeFromArray(new IntArray'(1,1));
	begin
	assert(p1.hashCode = p2.hashCode, "HashCode for similar objects should be equal");
	end;

end;

end TestPosition;
