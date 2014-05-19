package examples.adt.stack;

import examples.adt.stack.stackgom.types.*;

public class StackFactory {
	%include{ stackgom/StackGom.tom }
	private static StackFactory INSTANCE;

	private StackFactory() {
	}

	public static StackFactory build() {
		if (INSTANCE == null) {
			INSTANCE = new StackFactory();
		} 
		return INSTANCE;
	}
	
	public IStack createTomStack(Stack s) throws Exception {
		return makeTomStack(s);
	}

	protected TomStack makeTomStack(Stack s) throws Exception {
		TomStack tStack = new TomStack();
		evaluateStack(tStack, s);
		return tStack;
	}

	protected void evaluateStack(IStack object, Stack s) throws Exception {
		%match(s) {
			push(x, y) -> {
				Elem e = null;
				if (`x.isval()) {
					e = `x;
				} else {
					e = `evaluateTop(x);
				}
				object.push(`x);
				`evaluateStack(object, y);
			}
		}
	}

	protected Elem evaluateTop(Elem e) throws EmptyStackException {
		%match(e) {
			v@val(_) -> { return `v; }
			top(push(x, y)) -> { return `x; }
		}
		throw new EmptyStackException("Evaluate top on empty stack or bad term occured: " + e);
	}

	public IStack createListStack(Stack s) throws Exception {
		ListStack lStack = new ListStack();
		evaluateStack(lStack, s);
		return lStack;
	}
}
