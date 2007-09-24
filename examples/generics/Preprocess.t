import java.io.*;
import org.antlr.runtime.*;
import generics.types.*;
import generics.types.idlist.*;
import java.util.regex.*;
import java.util.*;

public class Preprocess {

  %include { generics/generics.tom }
  
  public static List<MatchResult> findAll(Pattern pattern, CharSequence text)
  {
    List<MatchResult> results = new ArrayList<MatchResult>();
    Matcher m = pattern.matcher(text);
    while(m.find()) results.add(m.toMatchResult());
    return results;
  }

  private static String readFile(String filename) throws Exception {
    BufferedReader in = new BufferedReader(new FileReader(filename));
    StringBuffer buffer = new StringBuffer();
    String line = null;
    while( (line = in.readLine()) != null )
      buffer.append(line + "\n");
    return buffer.toString();
  }

  public static Sig parse(String genericpart) throws Exception {
    GenericLexer lex = new GenericLexer(new ANTLRStringStream(genericpart));
    CommonTokenStream tokens = new CommonTokenStream(lex);
    GenericParser parser = new GenericParser(tokens);
    Sig res = parser.tom_file();
    //ted.VisitableViewer.toTreeStdout(res);
    return res;
  }

  private static Pattern uses = Pattern.compile("([_\\w]+)<([_\\w]+)>");
  private static IdList getGenericUses(String code) {
    IdList res = `idlist();
    List<MatchResult> results = findAll(uses, code);
    for(MatchResult r : results)
      res = `idlist(res*,genId(r.group(1),r.group(2)));
    return res;
  }

  private static void generateInfoList
    (StringBuffer res, InfoList il, String param, String value) {
      %match (il) {
        infolist(_*,info(l,r),_*) -> {
          res.append("\t" + `l);
          res.append(" { ");
          res.append(`r.replace("$"+param,value));
          res.append(" }\n");
        }
      }
  }

  private static void 
    generateTypeterm(StringBuffer res, TTInstance tti) {
      %match(tti) {
        ttinstance(
            typeterm(genId(name,param),infos),
            value
        )-> {
          res.append("%typeterm " + `name + "_" + `value + " {\n");
          `generateInfoList(res,infos,param,value);
          res.append("}\n");
        }
      }
    }

  private static void 
    generateArgs(StringBuffer res, IdList args, String param, String value) {
      %match(args) {
        idlist(genId(name,type),tail*) -> {
          res.append(`name);
          res.append(" : ");
          res.append(`type.equals(param) ? value : param);
          if (((IdList) `tail).length() > 0) { 
            res.append(",");
            generateArgs(res,`tail,param,value);
          }
        }
      }
    }

  private static void 
    generateOperator(StringBuffer res, OpInstance opi) {
      %match(opi) {
        opinstance(
            op(genId(retname,param),
               genId(name,param),
               args,
               infos),
            value
        )-> {
          res.append("%op " + `retname + "_" + `value + " ");
          res.append(`name + "_" + `value + "(");
          generateArgs(`res,`args,`param,`value);
          res.append(") {\n");
          `generateInfoList(res,infos,param,value);
          res.append("}\n");
        }

        opinstance(
            oplist(genId(retname,param),
               genId(name,param),
               arg,
               infos),
            value
        )-> {
          res.append("%oplist " + `retname + "_" + `value + " ");
          res.append(`name + "_" + `value + "(");
          res.append( `arg.equals(`param) ? `value : `arg );
          res.append("*) {\n");
          `generateInfoList(res,infos,param,value);
          res.append("}\n");
        }

      }
    }

  private static HashSet<TTInstance> getTTInstances(Sig sig, IdList uses) {
    HashSet<TTInstance> res = new HashSet<TTInstance>();
    %match(sig,uses) {
      // generate %typeterm for each use in user code
      sig(ttlist(_*,tt@typeterm[name=genId(id,_)],_*),_),
      idlist(_*,genId(id,value),_*) -> {
        res.add(`ttinstance(tt,value));
      }

      // generate %typeterm when it is the codomain of a 
      // used %op or %oplist
      sig(ttlist(_*,tt@typeterm[name=genId(ttid,_)],_*),
          olist(_*,(op|oplist)[ret=genId(ttid,T),name=genId(opid,T)],_*)
          ),
      idlist(_*,genId(opid,value),_*) -> {
        res.add(`ttinstance(tt,value));
      }

    }
    return res;
  }

  private static HashSet<OpInstance> getOpInstances(Sig sig, IdList uses) {
    HashSet<OpInstance> res = new HashSet<OpInstance>();
    %match(sig,uses) {
      // generate %op and %oplist for each use in the user code
      sig(_,olist(_*,o@(op|oplist)[name=genId(id,_)],_*)),
      idlist(_*,genId(id,value),_*) -> {
        res.add(`opinstance(o,value));
      }

      // generate %oplist for each use of the codomain
      sig(ttlist(_*,typeterm[name=genId(ttid,T)],_*),
          olist(_*,o@oplist[ret=genId(ttid,T),name=genId(opid,T)],_*)
          ),
      idlist(_*,genId(ttid,value),_*) -> {
        res.add(`opinstance(o,value));
      }

    }
    return res;
  }

  public static String instanciateTemplates
    (Collection<TTInstance> ttis, Collection<OpInstance> opis) {
      StringBuffer res = new StringBuffer();
      for(TTInstance tti: ttis) 
        `generateTypeterm(res,tti);
      for(OpInstance opi: opis) 
      `generateOperator(res,opi);
      return res.toString();
    }

  public static String modifyCode 
    (String code, Collection<TTInstance> ttis, Collection<OpInstance> opis) {
      String res = code;
      for(TTInstance tti: ttis) {
        %match(tti) {
          ttinstance(typeterm[name=genId(id,_)],value) -> {
            res = res.replaceAll("(\\W)" + `id + "<" + `value + ">",
                                 "$1" + `id + "_" + `value);
          }
        }
      }
      for(OpInstance opi: opis) {
        %match(opi) {
          opinstance((op|oplist)[name=genId(id,_)],value) -> {
            res = res.replaceAll("(\\W)" + `id + "<" + `value + ">",
                                 "$1" + `id + "_" + `value);
          }
        }
      }
      return res;
    }

  public static void main(String args[]) throws Exception {
    String content = readFile(args[0]); 
    String[] code = content.split("//generics");
    if(code.length != 3) 
      throw new RuntimeException(
          "generic part of the file has to be delimited by //generics"
          );

    Sig sig = parse(code[1]);
    IdList uses = getGenericUses(code[2]);
    Collection<TTInstance> ttis = getTTInstances(sig,uses);
    Collection<OpInstance> opis = getOpInstances(sig,uses);
    
    String templates = instanciateTemplates(ttis,opis);
    code[2] = modifyCode(code[2],ttis,opis);
    
    System.out.println(code[0]);
    System.out.println(templates);
    System.out.println(code[2]);
  }
}
