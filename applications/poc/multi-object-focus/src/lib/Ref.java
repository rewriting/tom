package lib;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 25/10/12
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class Ref<X> {
    public X value ;

    public      Ref(X x) { value = x; }
    public void set(X x) { value = x; }
}
