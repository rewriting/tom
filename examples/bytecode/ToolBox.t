
package bytecode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import bytecode.classtree.*;
import bytecode.classtree.types.*;

public class ToolBox {
	%include { classtree/ClassTree.tom }

  private final static int[] accessFlags = {
    Opcodes.ACC_ABSTRACT,
    Opcodes.ACC_ANNOTATION,
    Opcodes.ACC_BRIDGE,
    Opcodes.ACC_DEPRECATED,
    Opcodes.ACC_ENUM,
    Opcodes.ACC_FINAL,
    Opcodes.ACC_INTERFACE,
    Opcodes.ACC_NATIVE,
    Opcodes.ACC_PRIVATE,
    Opcodes.ACC_PROTECTED,
    Opcodes.ACC_PUBLIC,
    Opcodes.ACC_STATIC,
    Opcodes.ACC_STRICT,
    Opcodes.ACC_SUPER,
    Opcodes.ACC_SYNCHRONIZED,
    Opcodes.ACC_SYNTHETIC,
    Opcodes.ACC_TRANSIENT,
    Opcodes.ACC_VARARGS,
    Opcodes.ACC_VOLATILE
  };
  private final static TAccess[] accessObj = {
    `ABSTRACT(),
    `ANNOTATION(),
    `BRIDGE(),
    `DEPRECATED(),
    `ENUM(),
    `FINAL(),
    `INTERFACE(),
    `NATIVE(),
    `PRIVATE(),
    `PROTECTED(),
    `PUBLIC(),
    `STATIC(),
    `STRICT(),
    `SUPER(),
    `SYNCHRONIZED(),
    `SYNTHETIC(),
    `TRANSIENT(),
    `VARARGS(),
    `VOLATILE()
  };

  public static TAccessList buildTAccess(int access) {
    TAccessList list = `AccessList();
    for(int i = 0; i < accessFlags.length; i++) {
      if((access & accessFlags[i]) != 0)
        list = `ConsAccessList(accessObj[i], list);
    }

    return list;
  }

  public static TValue buildTValue(Object v) {
    if(v instanceof String)
      return `StringValue((String)v);
    else if(v instanceof Integer)
      return `IntValue(((Integer)v).intValue());
    else if(v instanceof Long)
      return `LongValue(((Long)v).longValue());
    else if(v instanceof Float)
      return `FloatValue(((Float)v).floatValue());
    else if(v instanceof Double)
      return `DoubleValue(((Double)v).doubleValue());

    return null;
  }

  public static TStringList buildTStringList(String[] array) {
    TStringList list = `StringList();
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--)
        list = `ConsStringList(array[i], list);
    }

    return list;
  }

  public static TintList buildTintList(int[] array) {
    TintList list = `intList();
    if(array != null) {
      for(int i = array.length - 1; i >= 0; i--) {
        list = `ConsintList(array[i], list);
      }
    }

    return list;
  }

  public static TType buildTType(String type) {
    int t = Type.getType(type).getSort();
    TType ret = null;
    switch(t) {
      case Type.ARRAY:
        ret = `ARRAY();
      case Type.BOOLEAN:
        ret = `BOOLEAN();
      case Type.BYTE:
        ret = `BYTE();
      case Type.CHAR:
        ret = `CHAR();
      case Type.DOUBLE:
        ret = `DOUBLE();
      case Type.FLOAT:
        ret = `FLOAT();
      case Type.INT:
        ret = `INT();
      case Type.LONG:
        ret = `LONG();
      case Type.OBJECT:
        ret = `OBJECT();
      case Type.SHORT:
        ret = `SHORT();
      case Type.VOID:
        ret = `VOID();
    }

    return ret;
  }
}

