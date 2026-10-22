// Generated from /Users/pem/github/tom/src/tom/engine/parser/tomjava/TomJavaParser.g4 by ANTLR 4.5.3
package tom.engine.parser.tomjava;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TomJavaParser}.
 */
public interface TomJavaParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(TomJavaParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(TomJavaParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#declarationsUnit}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationsUnit(TomJavaParser.DeclarationsUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#declarationsUnit}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationsUnit(TomJavaParser.DeclarationsUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#expressionUnit}.
	 * @param ctx the parse tree
	 */
	void enterExpressionUnit(TomJavaParser.ExpressionUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#expressionUnit}.
	 * @param ctx the parse tree
	 */
	void exitExpressionUnit(TomJavaParser.ExpressionUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(TomJavaParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(TomJavaParser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(TomJavaParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(TomJavaParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(TomJavaParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(TomJavaParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(TomJavaParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(TomJavaParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceModifier(TomJavaParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceModifier(TomJavaParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifier(TomJavaParser.VariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifier(TomJavaParser.VariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(TomJavaParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(TomJavaParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameters(TomJavaParser.TypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameters(TomJavaParser.TypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(TomJavaParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(TomJavaParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void enterTypeBound(TomJavaParser.TypeBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void exitTypeBound(TomJavaParser.TypeBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(TomJavaParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(TomJavaParser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstants(TomJavaParser.EnumConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstants(TomJavaParser.EnumConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstant(TomJavaParser.EnumConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstant(TomJavaParser.EnumConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterEnumBodyDeclarations(TomJavaParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitEnumBodyDeclarations(TomJavaParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(TomJavaParser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(TomJavaParser.InterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(TomJavaParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(TomJavaParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(TomJavaParser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(TomJavaParser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(TomJavaParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(TomJavaParser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(TomJavaParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(TomJavaParser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(TomJavaParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(TomJavaParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(TomJavaParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(TomJavaParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypeTypeOrVoid(TomJavaParser.TypeTypeOrVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeTypeOrVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypeTypeOrVoid(TomJavaParser.TypeTypeOrVoidContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericMethodDeclaration(TomJavaParser.GenericMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericMethodDeclaration(TomJavaParser.GenericMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericConstructorDeclaration(TomJavaParser.GenericConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericConstructorDeclaration(TomJavaParser.GenericConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(TomJavaParser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(TomJavaParser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(TomJavaParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(TomJavaParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBodyDeclaration(TomJavaParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBodyDeclaration(TomJavaParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberDeclaration(TomJavaParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberDeclaration(TomJavaParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(TomJavaParser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(TomJavaParser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclarator(TomJavaParser.ConstantDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclarator(TomJavaParser.ConstantDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodDeclaration(TomJavaParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodDeclaration(TomJavaParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#interfaceMethodModifier}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodModifier(TomJavaParser.InterfaceMethodModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#interfaceMethodModifier}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodModifier(TomJavaParser.InterfaceMethodModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericInterfaceMethodDeclaration(TomJavaParser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericInterfaceMethodDeclaration(TomJavaParser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(TomJavaParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(TomJavaParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(TomJavaParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(TomJavaParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(TomJavaParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(TomJavaParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(TomJavaParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(TomJavaParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(TomJavaParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(TomJavaParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(TomJavaParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(TomJavaParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(TomJavaParser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(TomJavaParser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedNameList(TomJavaParser.QualifiedNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedNameList(TomJavaParser.QualifiedNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(TomJavaParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(TomJavaParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(TomJavaParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(TomJavaParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(TomJavaParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(TomJavaParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void enterLastFormalParameter(TomJavaParser.LastFormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void exitLastFormalParameter(TomJavaParser.LastFormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(TomJavaParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(TomJavaParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(TomJavaParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(TomJavaParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(TomJavaParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(TomJavaParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(TomJavaParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(TomJavaParser.FloatLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(TomJavaParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(TomJavaParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePairs(TomJavaParser.ElementValuePairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePairs(TomJavaParser.ElementValuePairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePair(TomJavaParser.ElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePair(TomJavaParser.ElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(TomJavaParser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(TomJavaParser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(TomJavaParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(TomJavaParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeDeclaration(TomJavaParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeDeclaration(TomJavaParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeBody(TomJavaParser.AnnotationTypeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeBody(TomJavaParser.AnnotationTypeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementDeclaration(TomJavaParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementDeclaration(TomJavaParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementRest(TomJavaParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementRest(TomJavaParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodOrConstantRest(TomJavaParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodOrConstantRest(TomJavaParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodRest(TomJavaParser.AnnotationMethodRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodRest(TomJavaParser.AnnotationMethodRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationConstantRest(TomJavaParser.AnnotationConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationConstantRest(TomJavaParser.AnnotationConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(TomJavaParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(TomJavaParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(TomJavaParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(TomJavaParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(TomJavaParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(TomJavaParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(TomJavaParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(TomJavaParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#localTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalTypeDeclaration(TomJavaParser.LocalTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#localTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalTypeDeclaration(TomJavaParser.LocalTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(TomJavaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(TomJavaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(TomJavaParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(TomJavaParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(TomJavaParser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(TomJavaParser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(TomJavaParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(TomJavaParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void enterResourceSpecification(TomJavaParser.ResourceSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void exitResourceSpecification(TomJavaParser.ResourceSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#resources}.
	 * @param ctx the parse tree
	 */
	void enterResources(TomJavaParser.ResourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#resources}.
	 * @param ctx the parse tree
	 */
	void exitResources(TomJavaParser.ResourcesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(TomJavaParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(TomJavaParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(TomJavaParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(TomJavaParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(TomJavaParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(TomJavaParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(TomJavaParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(TomJavaParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(TomJavaParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(TomJavaParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(TomJavaParser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(TomJavaParser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(TomJavaParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(TomJavaParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(TomJavaParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(TomJavaParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(TomJavaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(TomJavaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#funTerm}.
	 * @param ctx the parse tree
	 */
	void enterFunTerm(TomJavaParser.FunTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#funTerm}.
	 * @param ctx the parse tree
	 */
	void exitFunTerm(TomJavaParser.FunTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(TomJavaParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(TomJavaParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameters(TomJavaParser.LambdaParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameters(TomJavaParser.LambdaParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#lambdaBody}.
	 * @param ctx the parse tree
	 */
	void enterLambdaBody(TomJavaParser.LambdaBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#lambdaBody}.
	 * @param ctx the parse tree
	 */
	void exitLambdaBody(TomJavaParser.LambdaBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(TomJavaParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(TomJavaParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(TomJavaParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(TomJavaParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(TomJavaParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(TomJavaParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#createdName}.
	 * @param ctx the parse tree
	 */
	void enterCreatedName(TomJavaParser.CreatedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#createdName}.
	 * @param ctx the parse tree
	 */
	void exitCreatedName(TomJavaParser.CreatedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void enterInnerCreator(TomJavaParser.InnerCreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void exitInnerCreator(TomJavaParser.InnerCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(TomJavaParser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(TomJavaParser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorRest(TomJavaParser.ClassCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorRest(TomJavaParser.ClassCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocation(TomJavaParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocation(TomJavaParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentsOrDiamond(TomJavaParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentsOrDiamond(TomJavaParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArgumentsOrDiamond(TomJavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArgumentsOrDiamond(TomJavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArguments(TomJavaParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArguments(TomJavaParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(TomJavaParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(TomJavaParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeType}.
	 * @param ctx the parse tree
	 */
	void enterTypeType(TomJavaParser.TypeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeType}.
	 * @param ctx the parse tree
	 */
	void exitTypeType(TomJavaParser.TypeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(TomJavaParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(TomJavaParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(TomJavaParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(TomJavaParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void enterSuperSuffix(TomJavaParser.SuperSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void exitSuperSuffix(TomJavaParser.SuperSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocationSuffix(TomJavaParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocationSuffix(TomJavaParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(TomJavaParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(TomJavaParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#javaIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterJavaIdentifier(TomJavaParser.JavaIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#javaIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitJavaIdentifier(TomJavaParser.JavaIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#tomDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTomDeclaration(TomJavaParser.TomDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#tomDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTomDeclaration(TomJavaParser.TomDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#tomStatement}.
	 * @param ctx the parse tree
	 */
	void enterTomStatement(TomJavaParser.TomStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#tomStatement}.
	 * @param ctx the parse tree
	 */
	void exitTomStatement(TomJavaParser.TomStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#tomTerm}.
	 * @param ctx the parse tree
	 */
	void enterTomTerm(TomJavaParser.TomTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#tomTerm}.
	 * @param ctx the parse tree
	 */
	void exitTomTerm(TomJavaParser.TomTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#metaquote}.
	 * @param ctx the parse tree
	 */
	void enterMetaquote(TomJavaParser.MetaquoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#metaquote}.
	 * @param ctx the parse tree
	 */
	void exitMetaquote(TomJavaParser.MetaquoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#matchStatement}.
	 * @param ctx the parse tree
	 */
	void enterMatchStatement(TomJavaParser.MatchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#matchStatement}.
	 * @param ctx the parse tree
	 */
	void exitMatchStatement(TomJavaParser.MatchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#strategyStatement}.
	 * @param ctx the parse tree
	 */
	void enterStrategyStatement(TomJavaParser.StrategyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#strategyStatement}.
	 * @param ctx the parse tree
	 */
	void exitStrategyStatement(TomJavaParser.StrategyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#includeStatement}.
	 * @param ctx the parse tree
	 */
	void enterIncludeStatement(TomJavaParser.IncludeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#includeStatement}.
	 * @param ctx the parse tree
	 */
	void exitIncludeStatement(TomJavaParser.IncludeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#gomStatement}.
	 * @param ctx the parse tree
	 */
	void enterGomStatement(TomJavaParser.GomStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#gomStatement}.
	 * @param ctx the parse tree
	 */
	void exitGomStatement(TomJavaParser.GomStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#ruleStatement}.
	 * @param ctx the parse tree
	 */
	void enterRuleStatement(TomJavaParser.RuleStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#ruleStatement}.
	 * @param ctx the parse tree
	 */
	void exitRuleStatement(TomJavaParser.RuleStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#unknownBlock}.
	 * @param ctx the parse tree
	 */
	void enterUnknownBlock(TomJavaParser.UnknownBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#unknownBlock}.
	 * @param ctx the parse tree
	 */
	void exitUnknownBlock(TomJavaParser.UnknownBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#gomOptions}.
	 * @param ctx the parse tree
	 */
	void enterGomOptions(TomJavaParser.GomOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#gomOptions}.
	 * @param ctx the parse tree
	 */
	void exitGomOptions(TomJavaParser.GomOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#visit}.
	 * @param ctx the parse tree
	 */
	void enterVisit(TomJavaParser.VisitContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#visit}.
	 * @param ctx the parse tree
	 */
	void exitVisit(TomJavaParser.VisitContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#actionRule}.
	 * @param ctx the parse tree
	 */
	void enterActionRule(TomJavaParser.ActionRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#actionRule}.
	 * @param ctx the parse tree
	 */
	void exitActionRule(TomJavaParser.ActionRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#tomBlock}.
	 * @param ctx the parse tree
	 */
	void enterTomBlock(TomJavaParser.TomBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#tomBlock}.
	 * @param ctx the parse tree
	 */
	void exitTomBlock(TomJavaParser.TomBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#slotList}.
	 * @param ctx the parse tree
	 */
	void enterSlotList(TomJavaParser.SlotListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#slotList}.
	 * @param ctx the parse tree
	 */
	void exitSlotList(TomJavaParser.SlotListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#slot}.
	 * @param ctx the parse tree
	 */
	void enterSlot(TomJavaParser.SlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#slot}.
	 * @param ctx the parse tree
	 */
	void exitSlot(TomJavaParser.SlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#patternlist}.
	 * @param ctx the parse tree
	 */
	void enterPatternlist(TomJavaParser.PatternlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#patternlist}.
	 * @param ctx the parse tree
	 */
	void exitPatternlist(TomJavaParser.PatternlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(TomJavaParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(TomJavaParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(TomJavaParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(TomJavaParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#bqterm}.
	 * @param ctx the parse tree
	 */
	void enterBqterm(TomJavaParser.BqtermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#bqterm}.
	 * @param ctx the parse tree
	 */
	void exitBqterm(TomJavaParser.BqtermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#pairSlotBqterm}.
	 * @param ctx the parse tree
	 */
	void enterPairSlotBqterm(TomJavaParser.PairSlotBqtermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#pairSlotBqterm}.
	 * @param ctx the parse tree
	 */
	void exitPairSlotBqterm(TomJavaParser.PairSlotBqtermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#bqcomposite}.
	 * @param ctx the parse tree
	 */
	void enterBqcomposite(TomJavaParser.BqcompositeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#bqcomposite}.
	 * @param ctx the parse tree
	 */
	void exitBqcomposite(TomJavaParser.BqcompositeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#composite}.
	 * @param ctx the parse tree
	 */
	void enterComposite(TomJavaParser.CompositeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#composite}.
	 * @param ctx the parse tree
	 */
	void exitComposite(TomJavaParser.CompositeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(TomJavaParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(TomJavaParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#fsymbol}.
	 * @param ctx the parse tree
	 */
	void enterFsymbol(TomJavaParser.FsymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#fsymbol}.
	 * @param ctx the parse tree
	 */
	void exitFsymbol(TomJavaParser.FsymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#headSymbol}.
	 * @param ctx the parse tree
	 */
	void enterHeadSymbol(TomJavaParser.HeadSymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#headSymbol}.
	 * @param ctx the parse tree
	 */
	void exitHeadSymbol(TomJavaParser.HeadSymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(TomJavaParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(TomJavaParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#explicitArgs}.
	 * @param ctx the parse tree
	 */
	void enterExplicitArgs(TomJavaParser.ExplicitArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#explicitArgs}.
	 * @param ctx the parse tree
	 */
	void exitExplicitArgs(TomJavaParser.ExplicitArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#implicitArgs}.
	 * @param ctx the parse tree
	 */
	void enterImplicitArgs(TomJavaParser.ImplicitArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#implicitArgs}.
	 * @param ctx the parse tree
	 */
	void exitImplicitArgs(TomJavaParser.ImplicitArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#typeterm}.
	 * @param ctx the parse tree
	 */
	void enterTypeterm(TomJavaParser.TypetermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#typeterm}.
	 * @param ctx the parse tree
	 */
	void exitTypeterm(TomJavaParser.TypetermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(TomJavaParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(TomJavaParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#oplist}.
	 * @param ctx the parse tree
	 */
	void enterOplist(TomJavaParser.OplistContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#oplist}.
	 * @param ctx the parse tree
	 */
	void exitOplist(TomJavaParser.OplistContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#oparray}.
	 * @param ctx the parse tree
	 */
	void enterOparray(TomJavaParser.OparrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#oparray}.
	 * @param ctx the parse tree
	 */
	void exitOparray(TomJavaParser.OparrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#termBlock}.
	 * @param ctx the parse tree
	 */
	void enterTermBlock(TomJavaParser.TermBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#termBlock}.
	 * @param ctx the parse tree
	 */
	void exitTermBlock(TomJavaParser.TermBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#implement}.
	 * @param ctx the parse tree
	 */
	void enterImplement(TomJavaParser.ImplementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#implement}.
	 * @param ctx the parse tree
	 */
	void exitImplement(TomJavaParser.ImplementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#equalsTerm}.
	 * @param ctx the parse tree
	 */
	void enterEqualsTerm(TomJavaParser.EqualsTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#equalsTerm}.
	 * @param ctx the parse tree
	 */
	void exitEqualsTerm(TomJavaParser.EqualsTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#isSort}.
	 * @param ctx the parse tree
	 */
	void enterIsSort(TomJavaParser.IsSortContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#isSort}.
	 * @param ctx the parse tree
	 */
	void exitIsSort(TomJavaParser.IsSortContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#isFsym}.
	 * @param ctx the parse tree
	 */
	void enterIsFsym(TomJavaParser.IsFsymContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#isFsym}.
	 * @param ctx the parse tree
	 */
	void exitIsFsym(TomJavaParser.IsFsymContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#make}.
	 * @param ctx the parse tree
	 */
	void enterMake(TomJavaParser.MakeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#make}.
	 * @param ctx the parse tree
	 */
	void exitMake(TomJavaParser.MakeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#makeEmptyList}.
	 * @param ctx the parse tree
	 */
	void enterMakeEmptyList(TomJavaParser.MakeEmptyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#makeEmptyList}.
	 * @param ctx the parse tree
	 */
	void exitMakeEmptyList(TomJavaParser.MakeEmptyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#makeEmptyArray}.
	 * @param ctx the parse tree
	 */
	void enterMakeEmptyArray(TomJavaParser.MakeEmptyArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#makeEmptyArray}.
	 * @param ctx the parse tree
	 */
	void exitMakeEmptyArray(TomJavaParser.MakeEmptyArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#makeAppendArray}.
	 * @param ctx the parse tree
	 */
	void enterMakeAppendArray(TomJavaParser.MakeAppendArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#makeAppendArray}.
	 * @param ctx the parse tree
	 */
	void exitMakeAppendArray(TomJavaParser.MakeAppendArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#makeInsertList}.
	 * @param ctx the parse tree
	 */
	void enterMakeInsertList(TomJavaParser.MakeInsertListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#makeInsertList}.
	 * @param ctx the parse tree
	 */
	void exitMakeInsertList(TomJavaParser.MakeInsertListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#getSlot}.
	 * @param ctx the parse tree
	 */
	void enterGetSlot(TomJavaParser.GetSlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#getSlot}.
	 * @param ctx the parse tree
	 */
	void exitGetSlot(TomJavaParser.GetSlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#getHead}.
	 * @param ctx the parse tree
	 */
	void enterGetHead(TomJavaParser.GetHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#getHead}.
	 * @param ctx the parse tree
	 */
	void exitGetHead(TomJavaParser.GetHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#getTail}.
	 * @param ctx the parse tree
	 */
	void enterGetTail(TomJavaParser.GetTailContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#getTail}.
	 * @param ctx the parse tree
	 */
	void exitGetTail(TomJavaParser.GetTailContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#getElement}.
	 * @param ctx the parse tree
	 */
	void enterGetElement(TomJavaParser.GetElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#getElement}.
	 * @param ctx the parse tree
	 */
	void exitGetElement(TomJavaParser.GetElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#isEmptyList}.
	 * @param ctx the parse tree
	 */
	void enterIsEmptyList(TomJavaParser.IsEmptyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#isEmptyList}.
	 * @param ctx the parse tree
	 */
	void exitIsEmptyList(TomJavaParser.IsEmptyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#getSize}.
	 * @param ctx the parse tree
	 */
	void enterGetSize(TomJavaParser.GetSizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#getSize}.
	 * @param ctx the parse tree
	 */
	void exitGetSize(TomJavaParser.GetSizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#getDefault}.
	 * @param ctx the parse tree
	 */
	void enterGetDefault(TomJavaParser.GetDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#getDefault}.
	 * @param ctx the parse tree
	 */
	void exitGetDefault(TomJavaParser.GetDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link TomJavaParser#tomIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterTomIdentifier(TomJavaParser.TomIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TomJavaParser#tomIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitTomIdentifier(TomJavaParser.TomIdentifierContext ctx);
}