package lib.fun;

import lib.*;

/**
 * Created with IntelliJ IDEA.
 * User: christophe
 * Date: 24/10/12
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public interface FunCPS<Ans,X,Y> { public Ans applyCPS(X x, Fun<Y,Ans> k) throws MOFException; }
