import shared.SharedObjectFactory;

public class Factory extends SharedObjectFactory {
  private static int DEFAULT_TERM_TABLE_SIZE = 16; // means 2^16 entries

	private Empty protoEmpty;
	private Cons protoCons;
	private Plop protoPlop;
	private Int protoInt;

  public Factory() {
    this(DEFAULT_TERM_TABLE_SIZE);
  }

  public Factory(int termTableSize) {
    super(termTableSize);

    protoEmpty = new Empty();
    protoCons = new Cons();
    protoPlop = new Plop();
    protoInt = new Int();
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
    synchronized (protoCons) {
      protoInt.initHashCode(value);
      return (Int) build(protoInt);
    }
	}
}
