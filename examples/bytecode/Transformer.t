/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package bytecode;

import java.io.FileOutputStream;
import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;
import tom.library.adt.bytecode.types.tstringlist.*;
import tom.library.adt.bytecode.types.tlabellist.*;
import tom.library.adt.bytecode.types.tintlist.*;
import tom.library.bytecode.*;
import java.util.Vector;

public class Transformer {

	%include { mustrategy.tom }
	%include { adt/bytecode/Bytecode.tom }
	
	public static Vector  v = new Vector();
	public static String clm;

	//On cherche INVOKEVIRTUAL
	%strategy FindFileAccess() extends Identity() {
    	visit TInstructionList {
		(before*,New("java/io/FileReader"),Dup(),Aload(nombre),
				Invokespecial[owner="java/io/FileReader", name="<init>"],
				Astore(nombre2),middle*,Aload(nombre2),Aload(nombre1),
				Invokevirtual("java/io/FileReader","read",MethodDescriptor(ConsFieldDescriptorList(ArrayType(C()),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
				Pop(), after*)-> {
				
				System.out.println("Acces a un fichier (reader(buf))" );
					return `InstructionList(before*,
					middle*,
					New("bytecode/SecureAccess"),
					Dup(),
					Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
					Aload(nombre),
					Aload(nombre1),
					Invokevirtual("bytecode/SecureAccess","sreadBuf",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),ConsFieldDescriptorList(ArrayType(C()),EmptyFieldDescriptorList())),Void())),
  					after*); 
						
					}


//Cas lire 1:new FileReader(nameFile).read();
//Cas lire 2
  		
//NEW java/io/FileReader
 //   DUP
  //  ALOAD 0
   // INVOKESPECIAL java/io/FileReader.<init>(Ljava/lang/String;)V
    //ASTORE 1
    //GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    //ALOAD 1
    //INVOKEVIRTUAL java/io/FileReader.read()I
    //INVOKEVIRTUAL java/io/PrintStream.println(I)V
	
			(before*,New("java/io/FileReader"),Dup(),Aload(nombre),
			Invokespecial[owner="java/io/FileReader", name="<init>"],
			middle*,
			Invokevirtual[owner ="java/io/FileReader",name="read"], 
			Pop(),
			after*) -> {
				System.out.println("Attempt to read a file");
			 /*return `InstructionList(before*,
					  New("bytecode/SecureAccess"),
                      Dup(),
			 		  Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
                      Aload(nombre),
                      Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(FieldDescriptorList(ObjectType("java/lang/String")),Void())),
                      after*); */
			return `InstructionList(before*,
					New("bytecode/SecureAccess"),
					Dup(),
					Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
					Aload(nombre),
					Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
					Pop(),
					Return(),
					after*);


			}
		
			/*(before*,New("bytecode/SecureAccess"),after*) -> {
				System.out.println("!:"+`after);
			 
			}*/

//Cas lire3
    		(before*,Aload(nombre), Invokevirtual[owner ="java/io/FileReader",name="read"], Pop(),after*) -> {
				System.out.println("Acces a un fichier 2");
			 /*return `InstructionList(before*,
					  New("bytecode/SecureAccess"),
                      Dup(),
			 		  Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
                      Aload(nombre-1),
                      Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),EmptyFieldDescriptorList()),Void())),
                      after*); */
return `InstructionList(before*,
					New("bytecode/SecureAccess"),
					Dup(),
					Invokespecial("bytecode/SecureAccess","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
					Aload(nombre),
					Invokevirtual("bytecode/SecureAccess","sread",MethodDescriptor(ConsFieldDescriptorList(ObjectType("java/lang/String"),EmptyFieldDescriptorList()),ReturnDescriptor(I()))),
					Pop(),
					Return(),
					after*);
			}

			/*(before*,New(n@nomClass),Dup(),Invokespecial(nm@nom,"<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),Astore(nombre),after*) -> {
					if(!v.contains(`n)){
						System.out.println("nouvelle classe"+`n);
						Class c;
						try {
							c = Class.forName(`n);

							if(c.getSuperclass().toString().equals("class java.lang.ClassLoader")){
							System.out.println("ClassLoader mechant!");
							clm=`n;
							return `InstructionList(before*,
					  		New("SClassLoader"),
					  		Dup(),
							Invokespecial("SClassLoader","<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
							Astore(nombre),
                      		after*);
						}
							else{
								
						
								return `InstructionList(before*,
					  			New(nomClass),
					  			Dup(),
								Invokespecial(nm,"<init>",MethodDescriptor(EmptyFieldDescriptorList(),Void())),
								Astore(nombre),
                      			after*);
							}
						} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						}
						
					}

					
				}*/

				
		}//ferme visit
	}//ferme strategy
	
	/*%strategy FindOtherClass() extends Identity() {
		visit TInstructionList {
			(before*,New(n@nomClass),after*) -> {
					System.out.println("nouvelle classe"+`n);
				}
		}//ferme visit	
	}//ferme strategy*/
		

	//On parcourt toutes les méthodes de la classe
	//On cherche les appelles FileReader.read();
	//(statégie FindDileAccess sera appliquée)
	
	  private static TClass fileAccesVerify(TClass givenClass) {
       	TMethodList methods = givenClass.getmethods();
		TMethodList secureMethods = `MethodList();
	    %match(TMethodList methods) {
		      MethodList(_*, x, _*) -> {
	    	    System.out.println("Analysis of method "+`x.getinfo().getname());
	        	TInstructionList ins = `x.getcode().getinstructions();
	        	    		TInstructionList secureInstList = (TInstructionList) `TopDown(FindFileAccess()).apply(ins);
	        		TMethodCode secureCode = `x.getcode().setinstructions(secureInstList);
	        		TMethod secureMethod = `x.setcode(secureCode);
	  				secureMethods = `MethodList(secureMethods*,secureMethod);	
	  		        
	      	} 
	    }
    	return givenClass.setmethods(secureMethods);
  	}
	
	

 private static TClass renameClass(TClass clazz, String newName) {
    TClassInfo classInfo = clazz.getinfo();
    String currentName = classInfo.getname();

    TClass newClass = clazz.setinfo(classInfo.setname(newName));
    return (TClass)`TopDown(RenameDescAndOwner(currentName, newName)).apply(newClass);
  }

 %strategy RenameDescAndOwner(currentName:String, newName:String) extends Identity() {
    visit TOuterClassInfo {
      x@OuterClassInfo[owner=owner] -> {
        if(`owner.equals(currentName))
          return `x.setowner(newName);
      }
    }

    visit TLocalVariable {
      x@LocalVariable[typeDesc=t] -> {
        if(`t.equals(currentName))
          return `x.settypeDesc(newName);
      }
    }

    visit TFieldDescriptor {
      x@ObjectType[className=n] -> {
        if(`n.equals(currentName))
          return `x.setclassName(newName);
      }
    }

    visit TMethodInfo {
      x@MethodInfo[owner=owner] -> {
        if(`owner.equals(currentName))
          return `x.setowner(newName);
      }
    }

    visit TInstruction {
      x@(Getstatic|Putstatic|Getfield|Putfield|
          Invokevirtual|Invokespecial|Invokestatic|Invokeinterface)
        [owner=owner] -> {
          if(`owner.equals(currentName))
            return `x.setowner(newName);
        }
      x@(New|Anewarray|Checkcast|Instanceof|Multianewarray)[typeDesc=t] -> {
        if(`t.equals(currentName))
          return `x.settypeDesc(newName);
      }
    }

  }


	
	public static byte[] transformer(String fichier){
		v.add(fichier);
		System.out.println("Parsing class file " + fichier + " ...");
    	BytecodeReader br = new BytecodeReader(fichier);
      	System.out.println("Analyzing ...");
      	TClass c= br.getTClass();
	  	//String secureName = c.getinfo().getname() + "Secure";
      	//TClass cSecured = renameClass(fileAccesVerify(c),secureName);
      	c = fileAccesVerify(c);
		BytecodeGenerator bg = new BytecodeGenerator();
      	return bg.toBytecode(c);
   
	}

	

}//ferme Transformateur     

