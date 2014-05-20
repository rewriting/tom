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
	public void push(Integer elem) {
		stack = `push(val(elem), stack);
	}

	@Override
	public boolean isEmpty() {
		return stack.isempty();
	}

	@Override
	public Integer top() throws EmptyStackException {
		%match(stack) {
			push(val(x), y) -> { return `x; }
		}
		throw new EmptyStackException();
	}

	@Override
	public Integer pop() throws EmptyStackException {
		%match(stack) {
			push(val(x), y) -> { 
				stack = `y;
				return `x;
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
			push(x, y) -> { return 1 + `calculateSize(y); }
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

