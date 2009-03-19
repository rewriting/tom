
package firewall.ast.types;        

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
//import firewall.ast.types.target.*;
//import firewall.ast.*;
public abstract class Target extends firewall.ast.AstAbstractType  {



  public boolean isAccept() {
    return false;
  }

  public boolean isClassify() {
    return false;
  }

  public boolean isClusterIp() {
    return false;
  }

  public boolean isConnMark() {
    return false;
  }

  public boolean isConnSecMark() {
    return false;
  }

  public boolean isDnat() {
    return false;
  }

  public boolean isDrop() {
    return false;
  }

  public boolean isDscp() {
    return false;
  }

  public boolean isEcn() {
    return false;
  }

  public boolean isLog() {
    return false;
  }

  public boolean isMark() {
    return false;
  }

  public boolean isMasquerade() {
    return false;
  }

  public boolean isMirror() {
    return false;
  }

  public boolean isNetMap() {
    return false;
  }

  public boolean isNfLog() {
    return false;
  }

  public boolean isNfQueue() {
    return false;
  }

  public boolean isNoTrack() {
    return false;
  }

  public boolean isQueue() {
    return false;
  }

  public boolean isRedirect() {
    return false;
  }

  public boolean isReject() {
    return false;
  }

  public boolean isReturn() {
    return false;
  }

  public boolean isUserRuleCall() {
    return false;
  }

  public boolean isSame() {
    return false;
  }

  public boolean isSecMark() {
    return false;
  }

  public boolean isSnat() {
    return false;
  }

  public boolean isTcpMss() {
    return false;
  }

  public boolean isTos() {
    return false;
  }

  public boolean isTrace() {
    return false;
  }

  public boolean isTtl() {
    return false;
  }

  public boolean isUlog() {
    return false;
  }

  public boolean isIpv4OptSstRip() {
    return false;
  }

  public boolean isSet() {
    return false;
  }

  public boolean isTarpit() {
    return false;
  }

  public String getuser_rule_name() {
    throw new UnsupportedOperationException("This Target has no user_rule_name");
  }

  public Target setuser_rule_name(String _arg) {
    throw new UnsupportedOperationException("This Target has no user_rule_name");
  }


  public static IdConverter idConv = new IdConverter();

  public aterm.ATerm toATerm() {
    // returns null to indicates sub-classes that they have to work
    return null;
  }

  public static firewall.ast.types.Target fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  public static firewall.ast.types.Target fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  public static firewall.ast.types.Target fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  public static firewall.ast.types.Target fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    ArrayList<firewall.ast.types.Target> tmp = new ArrayList<firewall.ast.types.Target>();
    ArrayList<firewall.ast.types.Target> table = new ArrayList<firewall.ast.types.Target>();
    aterm.ATerm convertedTerm = atConv.convert(trm);
    int nbr = 0;
    firewall.ast.types.Target res = null;

    tmp.add(firewall.ast.types.target.Accept.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Classify.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.ClusterIp.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.ConnMark.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.ConnSecMark.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Dnat.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Drop.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Dscp.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Ecn.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Log.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Mark.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Masquerade.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Mirror.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.NetMap.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.NfLog.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.NfQueue.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.NoTrack.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Queue.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Redirect.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Reject.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Return.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.UserRuleCall.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Same.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.SecMark.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Snat.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.TcpMss.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Tos.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Trace.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Ttl.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Ulog.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Ipv4OptSstRip.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Set.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.target.Tarpit.fromTerm(convertedTerm,atConv));


    for(int i=0;i<tmp.size();i++) {
      if(tmp.get(i) != null) {
        nbr++;
        table.add(tmp.get(i));
        if (res == null) {
          res = tmp.get(i);
        }
      }
    }
    switch(nbr) {
      case 0:
        throw new IllegalArgumentException("This is not a Target " + trm);
      case 1:
        return res;
      default:
        Logger.getLogger("Target").log(Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {table.toString(), "firewall.ast.types.Target", res.toString()});
        break;
    }
    return res;
  }

  public static firewall.ast.types.Target fromString(String s, ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  public static firewall.ast.types.Target fromStream(java.io.InputStream stream, ATermConverter atConv) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),atConv);
  }

  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public firewall.ast.types.Target reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Collection
   */
  /*
  public boolean add(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean containsAll(java.util.Collection c) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean contains(Object o) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean equals(Object o) { return this == o; }

  public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return false; }

  public java.util.Iterator iterator() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public int size() { return length(); }

  public Object[] toArray() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public Object[] toArray(Object[] a) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
  */
  
}
