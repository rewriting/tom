package enumerator;

public interface Function1<S, T> {

   /**
    * Evaluates this function on it's arguments.
    * 
    * @param a The first argument.
    * @return The result.
    */
   public S apply(T a);
}
