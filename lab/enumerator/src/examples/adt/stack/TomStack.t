package examples.adt.stack;

import examples.adt.stack.stack.types.*;

public class TomStack implements IStack {

	%include{ stack/Stack.tom }

	private Stack stack;

	public TomStack() {
		stack = `empty();
	}

	@Override
	public  IStack empty() {
		return new TomStack();
	}

	@Override
	public TomStack push(Integer elem) {
		stack = `push(stack, elem);
    return this;
	}

	@Override
	public boolean isEmpty() {
		return stack.isempty();
	}

	@Override
	public Integer top() throws EmptyStackException {
		%match(stack) {
			push(_, e) -> { return `e; }
		}
		throw new EmptyStackException();
	}

	@Override
	public TomStack pop() throws EmptyStackException {
		%match(stack) {
			push(s, _) -> { 
				stack = `s;
				return this;
			}
		}
		throw new EmptyStackException();
	}

	@Override
	public int size() {
		return calculateSize(stack);
	}

	private int calculateSize(Stack s) {
		%match(s) {
			push(x, _) -> { return 1 + `calculateSize(x); }
		}
		return 0;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
    }
		if (obj == null){
			return false;
    }
		if (getClass() != obj.getClass()){
			return false;
    }
		IStack other = (IStack) obj;
		if (this.isEmpty()) {
      return other.isEmpty();
    }else{
      try{
        if (this.top().equals(other.top())) {
          this.pop();
          other.pop();
          return this.equals(other);
        }else{
          return false;
        } 
      }catch(EmptyStackException e){
        return false;
      }
    }
	}

// 	@Override
// 	public Stack getStack() {
// 		return stack;
// 	}

}

