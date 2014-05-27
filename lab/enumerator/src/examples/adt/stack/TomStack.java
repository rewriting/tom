package examples.adt.stack;

import examples.adt.stack.stack.types.*;

public class TomStack implements IStack {

	private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_Stack(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Stack(Object t) {return  (t instanceof examples.adt.stack.stack.types.Stack) ;}private static  examples.adt.stack.stack.types.Stack  tom_make_empty() { return  examples.adt.stack.stack.types.stack.empty.make() ;}private static boolean tom_is_fun_sym_push( examples.adt.stack.stack.types.Stack  t) {return  (t instanceof examples.adt.stack.stack.types.stack.push) ;}private static  examples.adt.stack.stack.types.Stack  tom_make_push( int  t0,  examples.adt.stack.stack.types.Stack  t1) { return  examples.adt.stack.stack.types.stack.push.make(t0, t1) ;}private static  int  tom_get_slot_push_element( examples.adt.stack.stack.types.Stack  t) {return  t.getelement() ;}private static  examples.adt.stack.stack.types.Stack  tom_get_slot_push_stack( examples.adt.stack.stack.types.Stack  t) {return  t.getstack() ;}  

	private Stack stack;

	public TomStack() {
		stack = tom_make_empty();
	}

	@Override
	public  IStack empty() {
		return new TomStack();
	}

	@Override
	public TomStack push(Integer elem) {
		stack = tom_make_push(elem,stack);
    return this;
	}

	@Override
	public boolean isEmpty() {
		return stack.isempty();
	}

	@Override
	public Integer top() throws EmptyStackException {
		{{if (tom_is_sort_Stack(stack)) {if (tom_is_sort_Stack((( examples.adt.stack.stack.types.Stack )stack))) {if (tom_is_fun_sym_push((( examples.adt.stack.stack.types.Stack )(( examples.adt.stack.stack.types.Stack )stack)))) {
 return tom_get_slot_push_element((( examples.adt.stack.stack.types.Stack )stack)); }}}}}

		throw new EmptyStackException();
	}

	@Override
	public TomStack pop() throws EmptyStackException {
		{{if (tom_is_sort_Stack(stack)) {if (tom_is_sort_Stack((( examples.adt.stack.stack.types.Stack )stack))) {if (tom_is_fun_sym_push((( examples.adt.stack.stack.types.Stack )(( examples.adt.stack.stack.types.Stack )stack)))) {
 
				stack = tom_get_slot_push_stack((( examples.adt.stack.stack.types.Stack )stack));
// 				return `x;
				return this;
			}}}}}

		throw new EmptyStackException();
	}

	@Override
	public int size() {
		return calculateSize(stack);
	}

	private int calculateSize(Stack s) {
		{{if (tom_is_sort_Stack(s)) {if (tom_is_sort_Stack((( examples.adt.stack.stack.types.Stack )s))) {if (tom_is_fun_sym_push((( examples.adt.stack.stack.types.Stack )(( examples.adt.stack.stack.types.Stack )s)))) {
 return 1 + calculateSize(tom_get_slot_push_stack((( examples.adt.stack.stack.types.Stack )s))); }}}}}

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

