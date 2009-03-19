
package firewall.ast;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

import firewall.LangageParser;

public class AstAdaptor {
  public static shared.SharedObject getTerm(Tree tree) {
    shared.SharedObject res = null;
    if(tree.isNil()) {
      throw new RuntimeException("nil term");
    }
    if(tree.getType()==Token.INVALID_TOKEN_TYPE) {
      throw new RuntimeException("bad type");
    }
    
    switch (tree.getType()) {
      case LangageParser.Ipv4OptSstRip:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Ipv4OptSstRip.make();
          break;
        }
      case LangageParser.Options:
        {

          if(tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = firewall.ast.types.options.Options.make(field0);
          break;
        }
      case LangageParser.ClusterIp:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.ClusterIp.make();
          break;
        }
      case LangageParser.Classify:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Classify.make();
          break;
        }
      case LangageParser.Return:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Return.make();
          break;
        }
      case LangageParser.Ulog:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Ulog.make();
          break;
        }
      case LangageParser.Localhost:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.communication.Localhost.make();
          break;
        }
      case LangageParser.Reject:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Reject.make();
          break;
        }
      case LangageParser.Mark:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Mark.make();
          break;
        }
      case LangageParser.Input:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.rule.Input.make();
          break;
        }
      case LangageParser.Icmp:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.protocol.Icmp.make();
          break;
        }
      case LangageParser.Tcp:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.protocol.Tcp.make();
          break;
        }
      case LangageParser.Block:
        {

          if(tree.getChildCount()!=2) {
            throw new RuntimeException("Node " + tree + ": 2 child(s) expected, but " + tree.getChildCount() + " found");
          }
          firewall.ast.types.Rule field0 = (firewall.ast.types.Rule)AstAdaptor.getTerm(tree.getChild(0));
          firewall.ast.types.InstructionList field1 = (firewall.ast.types.InstructionList)AstAdaptor.getTerm(tree.getChild(1));
          res = firewall.ast.types.block.Block.make(field0, field1);
          break;
        }
      case LangageParser.UserRuleDef:
        {

          if(tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = firewall.ast.types.rule.UserRuleDef.make(field0);
          break;
        }
      case LangageParser.Accept:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Accept.make();
          break;
        }
      case LangageParser.Tarpit:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Tarpit.make();
          break;
        }
      case LangageParser.Queue:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Queue.make();
          break;
        }
      case LangageParser.Drop:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Drop.make();
          break;
        }
      case LangageParser.UserRuleCall:
        {

          if(tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = firewall.ast.types.target.UserRuleCall.make(field0);
          break;
        }
      case LangageParser.ConnSecMark:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.ConnSecMark.make();
          break;
        }
      case LangageParser.Redirect:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Redirect.make();
          break;
        }
      case LangageParser.NfQueue:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.NfQueue.make();
          break;
        }
      case LangageParser.None:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.opts.None.make();
          break;
        }
      case LangageParser.NetMap:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.NetMap.make();
          break;
        }
      case LangageParser.Tos:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Tos.make();
          break;
        }
      case LangageParser.Same:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Same.make();
          break;
        }
      case LangageParser.InstructionList:
        {
          res = firewall.ast.types.instructionlist.EmptyInstructionList.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            firewall.ast.types.Instruction elem = (firewall.ast.types.Instruction)AstAdaptor.getTerm(tree.getChild(i));
            firewall.ast.types.instructionlist.InstructionList list = (firewall.ast.types.instructionlist.InstructionList) res;
            res = list.append(elem);
          }
          break;
        }
      case LangageParser.Blocks:
        {
          res = firewall.ast.types.file.EmptyBlocks.make();
          for(int i = 0; i < tree.getChildCount(); i++) {
            firewall.ast.types.Block elem = (firewall.ast.types.Block)AstAdaptor.getTerm(tree.getChild(i));
            firewall.ast.types.file.Blocks list = (firewall.ast.types.file.Blocks) res;
            res = list.append(elem);
          }
          break;
        }
      case LangageParser.Masquerade:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Masquerade.make();
          break;
        }
      case LangageParser.Mirror:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Mirror.make();
          break;
        }
      case LangageParser.All_:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.protocol.All_.make();
          break;
        }
      case LangageParser.Udp:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.protocol.Udp.make();
          break;
        }
      case LangageParser.SecMark:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.SecMark.make();
          break;
        }
      case LangageParser.NfLog:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.NfLog.make();
          break;
        }
      case LangageParser.ConnMark:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.ConnMark.make();
          break;
        }
      case LangageParser.Prerouting:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.rule.Prerouting.make();
          break;
        }
      case LangageParser.Snat:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Snat.make();
          break;
        }
      case LangageParser.Output:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.rule.Output.make();
          break;
        }
      case LangageParser.Postrouting:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.rule.Postrouting.make();
          break;
        }
      case LangageParser.Set:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Set.make();
          break;
        }
      case LangageParser.Ttl:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Ttl.make();
          break;
        }
      case LangageParser.Trace:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Trace.make();
          break;
        }
      case LangageParser.Ecn:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Ecn.make();
          break;
        }
      case LangageParser.Dscp:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Dscp.make();
          break;
        }
      case LangageParser.NoTrack:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.NoTrack.make();
          break;
        }
      case LangageParser.Log:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Log.make();
          break;
        }
      case LangageParser.Forward:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.rule.Forward.make();
          break;
        }
      case LangageParser.Ins:
        {

          if(tree.getChildCount()!=6) {
            throw new RuntimeException("Node " + tree + ": 6 child(s) expected, but " + tree.getChildCount() + " found");
          }
          firewall.ast.types.Target field0 = (firewall.ast.types.Target)AstAdaptor.getTerm(tree.getChild(0));
          firewall.ast.types.Protocol field1 = (firewall.ast.types.Protocol)AstAdaptor.getTerm(tree.getChild(1));
          firewall.ast.types.Opts field2 = (firewall.ast.types.Opts)AstAdaptor.getTerm(tree.getChild(2));
          firewall.ast.types.Communication field3 = (firewall.ast.types.Communication)AstAdaptor.getTerm(tree.getChild(3));
          firewall.ast.types.Communication field4 = (firewall.ast.types.Communication)AstAdaptor.getTerm(tree.getChild(4));
          firewall.ast.types.Options field5 = (firewall.ast.types.Options)AstAdaptor.getTerm(tree.getChild(5));
          res = firewall.ast.types.instruction.Ins.make(field0, field1, field2, field3, field4, field5);
          break;
        }
      case LangageParser.Dnat:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.Dnat.make();
          break;
        }
      case LangageParser.Ip_Addr:
        {

          if(tree.getChildCount()!=1) {
            throw new RuntimeException("Node " + tree + ": 1 child(s) expected, but " + tree.getChildCount() + " found");
          }
          String field0 = tree.getChild(0).getText();
          res = firewall.ast.types.communication.Ip_Addr.make(field0);
          break;
        }
      case LangageParser.TcpMss:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.target.TcpMss.make();
          break;
        }
      case LangageParser.Anywhere:
        {

          if(tree.getChildCount()!=0) {
            throw new RuntimeException("Node " + tree + ": 0 child(s) expected, but " + tree.getChildCount() + " found");
          }
          res = firewall.ast.types.communication.Anywhere.make();
          break;
        }

    }
    return res;
  }
}
