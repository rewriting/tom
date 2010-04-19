package strategycompiler;

import java.util.HashMap;
import tom.library.sl.*;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;


/**
 * Tom strategy compiler.
 * Allow Tom strategy inlining via the `compile' and `compileToFile' methods.
 * This class is aimed to be used at runtime.
 */
public class StrategyCompiler {
  %include { sl.tom }
  %include { adt/bytecode/Bytecode.tom }

  /**
   * Compiles the given strategy into the class file `inlinedClassName'.
   * Packages are not handled correctly yet (i.e. if `inlinedClassName' is
   * `foo.CompiledClass' then the class will be compiled into the file
   * `./foo.CompiledClass' instead of `./foo/CompiledClass'.
   * Moreover, the compiled class has only one constructor taking an array of
   * `Strategy'. Each object corresponding to an instance of each `user
   * strategy' defined by the `%strategy' mechanism. Thus, this method is not
   * really usefull at the moment..
   * 
   * @param subject The strategy to be compiled.
   * @param inlinedClassName The wanted compiled class name (without trailing `.class').
   */
  public static void compileToFile(Strategy subject, String inlinedClassName) {
    ClassCollector classCollector = new ClassCollector();
    ClassNode inlinedStrat = buildInlinedStrategy(subject, inlinedClassName, classCollector);
    ClassDumper.dumpClassToFile(inlinedStrat, inlinedClassName);
  }

  /**
   * Compiles the given strategy to a new one called `inlinedClassName' and
   * returned an instance of it.
   * The compiled class is store in memory and no file is generated.
   *
   * @param subject The strategy to be compiled.
   * @param inlinedClassName The wanted compiled class name (just choose an
   * unsued one).
   *
   * @return An new instance of the compiled class, or the subject if the
   * compilation fails.
   */
  public static Strategy compile(Strategy subject, String inlinedClassName) {
    /* Set the compiled strategy to subject in order to return it if the
       compilation fails.*/
    Strategy compiledStrategy = subject;
    try {
      /* Compiles the strategy.*/
      ClassCollector classCollector = new ClassCollector();
      ClassNode inlinedStrat = buildInlinedStrategy(subject, inlinedClassName, classCollector);      
      /*bytecode.CFGViewer.classToDot(inlinedStrat);*/

      /*DEBUG Dump the compiled class into a file 'tmp'*/
      /* ClassDumper.dumpClassNodeToFile(inlinedStrat,inlinedClassName+"tmp");*/

      /* Dump the compiled class into a `Class' object.*/
      Class ast = ClassDumper.dumpClass(inlinedStrat);      
      try {
        /* Subterms are the `%strategy' instances which was not inlined.*/
        Strategy[] subterms = classCollector.getSubterms();        
        Class<?>[] paramTypes = { subterms.getClass() };
        /* Retrieves the constructor from the `Class' object.*/
        java.lang.reflect.Constructor constructor = ast.getConstructor(paramTypes);
        Object[] params = { subterms };
        /* Get a new instance of the compiled class initialized with subterms.*/
        compiledStrategy = (Strategy)constructor.newInstance(params);
      } catch(Exception e) {
        System.err.println("Error when instantiate the compiled strategy `" + inlinedClassName + "'.");
        e.printStackTrace();
      }
    } catch(Exception e) {
      System.err.println("Error when compiling the strategy `" + inlinedClassName + "'.");
      e.printStackTrace();
    }

    return compiledStrategy;
  }

  /**
   * Builds the Gom tree corresponding to the inlined strategy.
   * i.e. builds the class header, builds the constructor, builds fields, call
   * the inliner and adds each generated methods to the class.
   *
   * @param subject The strategy to be compiled.
   * @param inlinedClassName The wanted compiled class name.
   * @param classCollector The class collector object in which generated
   * methods and fields are stored, as the subterm instances (to handle `%strategy').
   *
   * @return The Gom tree representing the compiled class.
   */
  private static ClassNode buildInlinedStrategy(Strategy subject, String inlinedClassName, ClassCollector classCollector) {
    /*System.out.println("Compiling strategy `" + inlinedClassName + "'...");*/

    /* Generates the constructor.*/
    classCollector.addMethod(buildConstructor(inlinedClassName));

    /* Starts the inlining.*/
    inlineClass(classCollector, subject, inlinedClassName, "");

    /* Builds the class header and adds fields and methods from the class
       collector.*/
    ClassNode ast =
      `Class(
          ClassInfo(
            inlinedClassName,
            EmptySignature(),
            AccessList(PUBLIC(), SUPER(), SYNCHRONIZED()),
            "tom/library/sl/AbstractStrategyCombinator",
            EmptyStringList(),
            EmptyInnerClassInfoList(),
            EmptyOuterClassInfo()),
          classCollector.getFieldList(),
          classCollector.getMethodList());

    return ast;
  }

  /**
   * Handles each generated `visit' method.
   * It is used to handle the first `visit' method and the generated ones.
   * A `visit' method is generated for each `Mu' object to handle the recursion properly.
   *
   * @param classCollector The class collector instance for the compiled class.
   * @param subject The strategy to be inlined.
   * @param inlinedClassName The compiled class name.
   * @param visitSuffix The suffix to be appened to the `visit' method name.
   */
  private static void inlineClass(ClassCollector classCollector, Strategy subject, String inlinedClassName, String visitSuffix) {
    /* As we generates a new `visitXXX' method, we need a new method collector.*/
    MethodCollector methodCollector = new MethodCollector();

    /* Starts the inlining as if we are doing a `visit' method call (allow `Mu',
       `MuVar' and `%strategy' handling).*/
    handleVisit(classCollector, methodCollector, subject, inlinedClassName, new SymbolTable()); 

    /* Builds the `Method' corresponding to the inlined code.*/
    Method method = buildVisitMethod(inlinedClassName, methodCollector, visitSuffix);

    /* Clean up the generated bytecode.*/
    try {
      method = (Method)`BottomUp(Clean()).visitLight(method);
    } catch(VisitFailure e) {}

    /* Adds the generated method into the class collector.*/
    classCollector.addMethod(method);
  }

  /**
   * Starts the inlining.
   * The subject is dumped into a Gom tree and then the `visit' method
   * definition is searched.
   * Then, the inlining strategy is applied to the tree corresponding to the
   * `visit' method.
   * The inlining is done in two phases. First, needed instructions are renamed
   * to prevent name capture, next, the inlining is really done.
   *
   * @param classCollector The class collector instance for the compiled class.
   * @param methodCollector The method collector containing each instructions
   * for the generated `visitXXX' method in which the subject will be inlined.
   * @param subject The strategy to be inlined.
   * @param inlinedClassName The compiled class name.
   */
  private static void inlineVisitMethod(ClassCollector classCollector, MethodCollector methodCollector, Strategy subject, String inlinedClassName) {
    String className = getClassName(subject);
    /*System.out.println("Inlining `visit' method in `" + className + "'...");*/

    /* Get the Gom term representing the class.*/
    ClassNode ast = ClassDumper.dumpClass(className);

    /* Iterates over each methods to find the `visit' definition.*/
    boolean visitMethodFlag = false;
    MethodList mList = ast.getmethods();    
    %match(mList) {
      MethodList(_*,
          Method(
            MethodInfo(
              owner,
              _,
              "visit",
              MethodDescriptor(
                FieldDescriptorList(ObjectType("tom/library/sl/Visitable")),
                ReturnDescriptor(ObjectType("tom/library/sl/Visitable"))),
              _,
              _),
            code@MethodCode[instructions=instructions]),
          _*) -> {
        if(!`owner.equals(className)) {
          System.err.println("`visit' method owner is `" + `owner + "', expected `" + className + "'.");
        } else {
          /* As we are inlining a new `visit' method, we need a new symbol table
             for the renaming strategy.*/
          SymbolTable symbolTable = new SymbolTable();
          MethodCode newCode = `code;

          /* Adds a `Astore(1)' instruction at the beginning of the inlined
             `visit' call to handle the `Visitable' parameter.
             The `Astore(1)' will be renamed as the other load/stores. Thus we
             now automatically in which local variable the parameter is.
             It works because each inlined method push on the stack the
             `returned value'.*/
          newCode = newCode.setinstructions(`InstructionList(Astore(1), instructions*));

          /* This strategy runs through the visit method instruction list.
             Each instruction is renamed accordingly to the symbol table.
             The symbol table gives a new fresh index/name for a given
             instruction with the specified index/name.
             A new symbol table is used for each inlined method call to
             prevent name capture.
             Then, we handle `getArgument' call and `visit' call to process
             the inlining.*/
          LabelNode methodEnd = `LabelNode(symbolTable.getFreshLabelIndex());
          Strategy inliner =
            `Sequence(
                TopDown(
                  Rename(classCollector, subject, inlinedClassName, symbolTable, ast.getfields())),
                TopDown(
                  InlineVisitCode(classCollector, methodCollector, subject, inlinedClassName, methodEnd, symbolTable)));

          /* TODO : replace with a strategy like : mu(MuVar("x"), OnceTopDownId(S(MuVar("x"))))*/

          /* Do the job.*/
          try {
            newCode = (MethodCode)inliner.visitLight(newCode);
          } catch(VisitFailure e) {}

          /* Appends an anchor to the end of the method. Thus, instead of doing
             a `return' call, we just jump to the end of the method.*/
          methodCollector.addInstruction(`Anchor(methodEnd));

          /* Adds local variables and try/catch blocks into the collector.*/
          %match(newCode) {
            MethodCode(_, var, tcb) -> {
              methodCollector.addVarList(`var);
              methodCollector.addTryCatchBlockList(`tcb);
            }
          }

          visitMethodFlag = true;
        }
      }
    }

    /* Should never append since all special cases are handled into the
       `handleVisit' method.*/
    if(!visitMethodFlag) {
      System.err.println("No suitable `visit' method found in class `" + className + "'.");
      System.exit(-1); /* FIXME*/
    }
  }

  %typeterm ClassCollector { 
    implement { ClassCollector } 
    is_sort(t)     { t instanceof ClassCollector }
  }

  %typeterm MethodCollector { 
    implement { MethodCollector } 
    is_sort(t)     { t instanceof MethodCollector }
  }

  %typeterm SymbolTable { 
    implement { SymbolTable } 
    is_sort(t)     { t instanceof SymbolTable }
  }

  /**
   * Strategy used to rename instructions which are prone to name capture.
   * This strategy also adds needed fields into the class collector.
   *
   * @param classCollector The class collector of the compiled class. The fields
   * will be added into it.
   * @param subject The strategy to be renamed.
   * @param inlinedClassName The root strategy.
   * @param symbolTable The symbol table matching old local variable index and
   * label to new ones.
   * @param subjectFieldList Fields list of the currently renamed strategy.
   *
   * @return The renamed code ready to be inlined.
   */
  %strategy Rename(
      classCollector:ClassCollector,
      subject:Strategy,
      inlinedClassName:String,
      symbolTable:SymbolTable,
      subjectFieldList:FieldList
      ) extends Identity() {
    visit Instruction {
      /* Renames local variable access.*/
      x@(
          Iload|Lload|Fload|Dload|Aload|
          Istore|Lstore|Fstore|Dstore|Astore|
          Iinc|Ret)[var=v] -> {
        return `x.setvar(symbolTable.getLocalVariableIndex(`v));
      }

      /* If a (Get|Put)(static|field) is matched, then the corresponding
         field must be added to the compiled class field list (into collector).
         The field is renamed too.*/
      x@(Getstatic|Putstatic|Getfield|Putfield)[name=name] -> {
        /* FIXME ugly !*/
        %match(name){
          /*we do not need to create vistors fields after inlining
            if we create them they are duplicated*/
          !"arguments" ->{
            /*Renaming of fields is not necessar */
            /*String newName = subject.toString().replace('.', '_').replace('@', '_') + "_" + `name;*/
            %match(subjectFieldList, String `name) {
              FieldList(_*, f@Field[name=fname], _*), fname -> {
                /*classCollector.addField(`f.setname(newName));*/
                classCollector.addField(`f);
              }
            }

            Instruction ins = `x.setowner(inlinedClassName);
            /*return ins.setname(newName);*/
            return ins;
          }
        }
      }
    }
    /* Labels must be renamed too as a `LabelNode(X)' can be present into the
       current code and in a future inlined code.*/
    visit LabelNode {
      LabelNode(id) -> {
        return `LabelNode(symbolTable.getLabelIndex(id));
      }
    }

    /* Rename local variable declaration.*/
    visit LocalVariable {
      x@LocalVariable[index=i] -> {
        return `x.setindex(symbolTable.getLocalVariableIndex(`i));
      }
    }
  }

  /**
   * Strategy which realizes the inlining.
   * `getArgument' call are removed and `visit' call are inlined when possible.
   *
   * @param classCollector The class collector of the compiled class.
   * @param methodCollector The method collector of the generated `visitXXX'
   * method in which the code will be inlined.
   * @param subject The root strategy.
   * @param inlinedClassName The compiled class name.
   * @param methodEnd The label of the end of the method.
   * @param symbolTable The symbol table matching old local variable index and
   *
   * @return Return a useless Gom term... In fact, all the inlined instructions
   * are added into the method collector instruction list. The returned term is
   * not the identity because of modifications used to avoid runs from the
   * beginning for each operation. It will be improved soon...
   */
  %strategy InlineVisitCode(
      classCollector:ClassCollector,
      methodCollector:MethodCollector,
      subject:Strategy,
      inlinedClassName:String,
      methodEnd:LabelNode,
      symbolTable:SymbolTable
      ) extends Identity() {
    visit InstructionList {
      /* Match a `arguments[index]' call.
         The index of the wanted argument is computed, and then the
         corresponding strategy is pushed into strategy stack of the collector.
         The captured instructions are removed because they will be useless
         after inlining.*/
      InstructionList(Aload(0),
          Getfield[],
          index,
          Aaload[],
          tail*) -> {
        /* Computes the index of the argument to be retrieve.*/
        int index = 0;
        boolean index_flag = false;
        %match(Instruction `index) {
          Iconst_m1() -> { index = -1; index_flag = true; }
          Iconst_0() -> { index = 0; index_flag = true; }
          Iconst_1() -> { index = 1; index_flag = true; }
          Iconst_2() -> { index = 2; index_flag = true; }
          Iconst_3() -> { index = 3; index_flag = true; }
          Iconst_4() -> { index = 4; index_flag = true; }
          Iconst_5() -> { index = 5; index_flag = true; }
          (Bipush|Sipush)(operand) -> { index = `operand; index_flag = true; }
          Ldc(IntValue(value)) -> { index = `value; index_flag = true; }
        }

        if(index_flag) {
          /* Gets the subterm normally returned by the `getArgument' call and
             push it onto the strategy stack.*/
          Strategy s = (Strategy)((AbstractStrategyCombinator)subject).getChildAt(index);
          classCollector.pushStrategy(s);

          /* Replaces the matched instructions by a `Nop()' one to ensure
             that the next term to be analysed is the tail.
             This is just to ensure that the matched instructions will
             not be twice.
             It allow us to 'remove' them and thus the instructions will not
             be added into the new instruction list builds in the collector.*/
          return `InstructionList(Nop(), tail*);
        }
      }

      /*/ Match a `visit' method call.
        The method call is inlined by recursively call the
        `inlineVisitMethodCode' method. The new subject is the last stategy
        being pushed into the symbol table.
        Special cases as `Mu' and `%strategy' are handle properly into the
        `handleVisit' method.*/
      InstructionList(Invokeinterface(
            _,
            "visit",
            MethodDescriptor(
              ConsFieldDescriptorList(
                ObjectType(
                  "tom/library/sl/Visitable"),
                EmptyFieldDescriptorList()),
              ReturnDescriptor(
                ObjectType(
                  "tom/library/sl/Visitable")))),
          tail*) -> {
        /*        %match(String `owner) {
                  ("jjtraveler/Visitor"|"jjtraveler/reflective/VisitableVisitor")() -> {*/
        Strategy lastStrategy = classCollector.popStrategy();
        handleVisit(classCollector, methodCollector, lastStrategy, inlinedClassName, symbolTable);

        /* Replaces the matched instructions by a `Nop()' one to ensure
           that the next term to be analysed is the tail.
           This is just to ensure that the matched instructions will
           not be twice.
           It allow us to 'remove' them and thus the instructions will not
           be added into the new instruction list builds in the collector.*/
        return `InstructionList(Nop(), tail*);
        /*          }
                    }*/
      }
    }

    visit Instruction {
      /* If the instruction is `return', then we replace it by a jump to the end
         of the current method code.*/
      x@Areturn() -> {
        methodCollector.addInstruction(`Goto(methodEnd));
        return `x;
      }

      /* No instruction is added into the collector if we have a `Nop()'.*/
      x@Nop() -> {
        return `x;
      }

      /* If we invoke a method of the current class, then we rename the owner to
         match the compiled class name.*/
      Invokevirtual(
          owner,
          name,
          methodDesc) -> {
        String subjectName = getClassName(subject);
        if(`owner.equals(subjectName)) {
          methodCollector.addInstruction(`Invokevirtual(inlinedClassName, name, methodDesc));
          return `Nop();
        }
      }

      /* No particular case, just add the instruction into the list.*/
      x@_ -> {
        methodCollector.addInstruction(`x);
      }
    }
  }

  /**
   * Strategy used to clean up the instruction list.
   */
  %strategy Clean() extends Identity() {
    visit InstructionList {
      InstructionList(Goto(x), a@Anchor(x), tail*) -> { return `InstructionList(a, tail*); }
    }
  }

  /**
   * Tells if a method of the given name is present in the given method list.
   *
   * @param methodName The searched method name.
   * @param methodList The method list to be scanned.
   *
   * @return true is a method with the given name is find, false else.
   */
  private static boolean methodExists(String methodName, MethodList methodList) {
    %match(methodList, String methodName) {
      MethodList(_*, Method(MethodInfo(_, _, name, _, _, _), _), _*), name -> {
        return true;
      }
    }

    return false;
  }

  /**
   * Returned a well formed class name for the given object.
   *
   * @param o The object.
   * @return The well formed class name.
   */
  private static String getClassName(Object o) {
    return o.getClass().getName().replace('.', '/');
  }

  /**
   * Cleans the given string to form a correct method name.
   *
   * @param n The method name to be cleaned.
   * @return The cleaned method name.
   */
  private static String cleanMethodName(String n) {
    /* FIXME ugly !!*/
    return n.replace('.', '_').replace('@', '_');
  }

  /**
   * Handles the creation of a new `visitXXX' method for the given strategy or
   * not. It is used when inlining a `visit' call.
   * If the subject is a `Mu' instance, then a new `visitXXX' methods is
   * generated. If it is a `MuVar' instance, then the `visit' call is replaced
   * by a call to the `visitXXX' method corresponding to the linked `Mu'.
   * A new `visitXXX' methods is also generated if the subject is an instance of
   * a `%strategy' class.
   * Else, the `visit' call is inlined in a classical way.
   *
   * @param classCollector The class collector of the compiled class.
   * @param methodCollector The method collector of the current `visitXXX' method.
   * @param subject The `visit' call subject.
   * @param inlinedClassName The compiled class name.
   * @param symbolTable The current symbol table.
   */
  private static void handleVisit(ClassCollector classCollector, MethodCollector methodCollector, Strategy subject, String inlinedClassName, SymbolTable symbolTable) {
    MethodDescriptor desc = 
      `MethodDescriptor(
          ConsFieldDescriptorList(
            ObjectType(
              "tom/library/sl/Visitable"),
            EmptyFieldDescriptorList()),
          ReturnDescriptor(
            ObjectType(
              "tom/library/sl/Visitable")));

    /* If no instructions were added into the instruction list, then the current
       subject is the first to be inlined.*/
    boolean isRoot = methodCollector.getInstructionList().isEmptyInstructionList();

    /* If subject is an instance of `AbstractStrategyCombinator', it means that we are
       inlining a 'base brick'.*/
    if(subject instanceof AbstractStrategyCombinator) {
      if(subject instanceof Mu) {
        /* To be sure that the strategy has been mu-expanded.*/
        Mu m = (Mu)subject;
        Mu.expand(m);

        String visitSuffix = cleanMethodName("_" + subject);

        /* If the visited `Mu' has not been encountered before, then we inlined
           it into a new `visitXXX' method.*/
        if(!methodExists("visit" + visitSuffix, classCollector.getMethodList())) {
          Strategy child = (Strategy)m.getChildAt(Mu.V);
          inlineClass(classCollector, child, inlinedClassName, visitSuffix);
        }

        /* Adds a call to the `visitXXX' method corresponding to the inlined
           `Mu'.*/
        if(isRoot) {
          methodCollector.addInstruction(`Aload(0));
          methodCollector.addInstruction(`Aload(1));
        } else {
          /* Here we need to store the parameter on the top of the stack and
             repush it after adding a `Astore(0) instruction. Thus, we can add
             an invoke instruction for a method of the compiled class.*/
          int freshIndex = symbolTable.getFreshLocalVariableIndex();
          methodCollector.addInstruction(`Astore(freshIndex));
          methodCollector.addInstruction(`Aload(0));
          methodCollector.addInstruction(`Aload(freshIndex));
        }
        methodCollector.addInstruction(`Invokevirtual(inlinedClassName, "visit" + visitSuffix, desc));
      } else if(subject instanceof MuVar) {
        /* If we have a `MuVar', we just call the `visitXXX' method of the
           corresponding `Mu'.*/
        MuVar muVar = (MuVar)subject;
        String visitSuffix = cleanMethodName("_" + muVar.getInstance());
        if(isRoot) {
          methodCollector.addInstruction(`Aload(0));
          methodCollector.addInstruction(`Aload(1));
        } else {
          /* Here we need to store the parameter on the top of the stack and
             repush it after adding a `Astore(0) instruction. Thus, we can add
             an invoke instruction for a method of the compiled class.*/
          int freshIndex = symbolTable.getFreshLocalVariableIndex();
          methodCollector.addInstruction(`Astore(freshIndex));
          methodCollector.addInstruction(`Aload(0));
          methodCollector.addInstruction(`Aload(freshIndex));
        }
        methodCollector.addInstruction(`Invokevirtual(inlinedClassName, "visit" + visitSuffix, desc));
      } else {
        /* Else we inline the method call in a classical way.*/
        if(isRoot)
          methodCollector.addInstruction(`Aload(1));
        inlineVisitMethod(classCollector, methodCollector, subject, inlinedClassName);
      }
    } else {
      /* The subject is a `%strategy' one.
         Thus we add the current instance into the subterm list to be able to
         instantiate the compiled class properly into the `compile' method.
         Then we add a call to the `getArgument' method of the current class
         in order to retrieve the wanted instance (the subterm == this).
         Finally we add a call to the `visit' method of the subterm.*/
      int i = classCollector.addSubterm(subject);
      int freshIndex = symbolTable.getFreshLocalVariableIndex();
      if(!isRoot)
        methodCollector.addInstruction(`Astore(freshIndex));
      methodCollector.addInstruction(`Aload(0));
      methodCollector.addInstruction(`Bipush(i));
      methodCollector.addInstruction(
          `Invokevirtual(
            inlinedClassName,
            "getChildAt",
            MethodDescriptor(
              ConsFieldDescriptorList(
                I(),
                EmptyFieldDescriptorList()),
              ReturnDescriptor(
                ObjectType(
                  "tom/library/sl/Visitable")))));
      if(isRoot)
        methodCollector.addInstruction(`Aload(1));
      else
        methodCollector.addInstruction(`Aload(freshIndex));
      methodCollector.addInstruction(`Invokeinterface("tom/library/sl/Strategy", "visit", desc));
    }

    if(isRoot)
      methodCollector.addInstruction(`Areturn());
  }

  /**
   * Returns the Gom term for the constructor.
   *
   * public `inlinedClassName'(Strategy[] array) {
   *   initSubterm(array);
   * }
   *
   * @param owner The compiled class name.
   * @return The Gom term of the constructor.
   */
  private static Method buildConstructor(String owner) {
    Method constructor = `Method(
        MethodInfo(
          owner,
          AccessList(PUBLIC()),
          "<init>",
          MethodDescriptor(
            FieldDescriptorList(
              ArrayType(
                ObjectType(
                  "tom/library/sl/Strategy"))),
            Void()),
          EmptySignature(),
          EmptyStringList()),
        MethodCode(
          InstructionList(
            Aload(0),
            Invokespecial(
              "tom/library/sl/AbstractStrategyCombinator",
              "<init>",
              MethodDescriptor(
                EmptyFieldDescriptorList(),
                Void())),
            Aload(0),
            Aload(1),
            Invokevirtual(
              owner,
              "initSubterm",
              MethodDescriptor(
                FieldDescriptorList(
                  ArrayType(
                    ObjectType(
                      "tom/library/sl/Strategy"))),
                Void())),
            Return()),
      EmptyLocalVariableList(),
      EmptyTryCatchBlockList()));

    return constructor;
  }

  /**
   * Builds the Gom term of a `visit' method.
   *
   * @param owner The compiled class name.
   * @param methodCollector The method collector containing the instruction
   * list, the local variable list and the try/catch blocks of the method.
   * @param methodSuffix The suffix to be appened to the `visit' method name.
   *
   * @return The Gom term.
   */
  private static Method buildVisitMethod(String owner, MethodCollector methodCollector, String methodSuffix) {
    Method visitMethod =
      `Method(
          MethodInfo(
            owner,
            AccessList(PUBLIC()),
            "visit" + methodSuffix,
            MethodDescriptor(
              FieldDescriptorList(ObjectType("tom/library/sl/Visitable")),
              ReturnDescriptor(ObjectType("tom/library/sl/Visitable"))),
            EmptySignature(),
            StringList("tom/library/sl/VisitFailure")),  /* FIXME */
          MethodCode(
            methodCollector.getInstructionList(),
            methodCollector.getVariableList(),
            methodCollector.getTryCatchBlockList()));

    return visitMethod;
  }

  /**
   * Clear the loaded class cache.
   */
  public static void clearCache() {
    ClassDumper.clearCache();
  }
}

