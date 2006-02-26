import shared.SharedObjectFactory;

public class Factory extends SharedObjectFactory {
  private static int DEFAULT_TERM_TABLE_SIZE = 16; // means 2^16 entries
  private static Factory instance = null;

	private Empty protoEmpty;
	private Cons protoCons;
	private Plop protoPlop;
	private Int protoInt;
	private ConsInt protoConsInt;

	public static Factory getInstance() {
		if(instance == null) {
			instance = new Factory();
		}
		return instance;
	}
	
  private Factory() {
    this(DEFAULT_TERM_TABLE_SIZE);
  }

  private Factory(int termTableSize) {
    super(termTableSize);

    protoEmpty = new Empty();
    protoCons = new Cons();
    protoPlop = new Plop();
    protoInt = new Int();
    protoConsInt = new ConsInt();
  }

	public Empty makeEmpty() {
    synchronized (protoEmpty) {
      protoEmpty.initHashCode();
      return (Empty) build(protoEmpty);
    }
	}
	
	public Cons makeCons(Element head, List tail) {
    synchronized (protoCons) {
      protoCons.initHashCode(head,tail);
      return (Cons) build(protoCons);
    }
	}

	public Plop makePlop() {
    synchronized (protoPlop) {
      protoPlop.initHashCode();
      return (Plop) build(protoPlop);
    }
	}
	
	public Int makeInt(int value) {
    synchronized (protoInt) {
      protoInt.initHashCode(value);
      return (Int) build(protoInt);
    }
	}
	
	public ConsInt makeConsInt(int headint, List tail) {
    synchronized (protoConsInt) {
      protoConsInt.initHashCode(headint,tail);
      return (ConsInt) build(protoConsInt);
    }
	}

  public List ListFromTerm(aterm.ATerm trm) {
    List tmp;
    tmp = List_EmptyFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = List_ConsFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }
    
		tmp = List_ConsIntFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a List: " + trm);
  }
  
	protected List List_EmptyFromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(appl.getName().equals(protoEmpty.getName())) {
				return makeEmpty();
			}
		}
		return null;
  }

	protected List List_ConsFromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(appl.getName().equals(protoCons.getName())) {
				return makeCons(
						ElementFromTerm(appl.getArgument(0)),
						ListFromTerm(appl.getArgument(1))
						);
			}
		}
		return null;
  }
	
	protected List List_ConsIntFromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(appl.getName().equals(protoConsInt.getName())) {
				return makeConsInt(
						((aterm.ATermInt)appl.getArgument(0)).getInt(), // special case for builtin argument
						ListFromTerm(appl.getArgument(1))
						);
			}
		}
		return null;
  }

  public Element ElementFromTerm(aterm.ATerm trm) {
    Element tmp;
    tmp = Element_PlopFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Element_IntFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a Element: " + trm);
  }
  
	protected Element Element_PlopFromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(appl.getName().equals(protoPlop.getName())) {
				return makePlop();
			}
		}
		return null;
  }

	/*
	 * je suis pour voir Int comme un constructeur ayant un fils builtin
	 * et non comme un cas special correspondant aux ATermInt
	 * il faudrait donc modifier le code suivant
	 */
	protected Element Element_IntFromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermInt) {
			aterm.ATermInt atint = (aterm.ATermInt) trm;
			return makeInt( atint.getInt() );
		}
		return null;
  }
	
}
