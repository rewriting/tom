package jtom.adt;

abstract public class TomSignatureConstructor
extends aterm.pure.ATermApplImpl
implements aterm.ATerm
{
  protected aterm.ATerm term = null;

  TomSignatureFactory factory = null;

  public TomSignatureConstructor(TomSignatureFactory factory) {
    super(factory.getPureFactory());
    this.factory = factory;
  }

  protected void init(int hashCode, aterm.ATermList annos, aterm.AFun fun,	aterm.ATerm[] args) {
    super.init(hashCode, annos, fun, args);
  }
  protected void initHashCode(aterm.ATermList annos, aterm.AFun fun, aterm.ATerm[] i_args) {
  	super.initHashCode(annos, fun, i_args);
  }
  abstract public aterm.ATerm toTerm();
  public String toString() {
    return toTerm().toString();
  }
  protected void setTerm(aterm.ATerm term) {
   this.term = term;
  }
  public TomSignatureFactory getTomSignatureFactory() {
    return factory;
  }
  public boolean isSortDeclaration() {
    return false;
  }

  public boolean isSortOption() {
    return false;
  }

  public boolean isSortExpression() {
    return false;
  }

  public boolean isSortTargetLanguage() {
    return false;
  }

  public boolean isSortPosition() {
    return false;
  }

  public boolean isSortTomType() {
    return false;
  }

  public boolean isSortTomName() {
    return false;
  }

  public boolean isSortTomTerm() {
    return false;
  }

  public boolean isSortTomNumber() {
    return false;
  }

  public boolean isSortTomRule() {
    return false;
  }

  public boolean isSortInstruction() {
    return false;
  }

  public boolean isSortTomSymbol() {
    return false;
  }

  public boolean isSortPairNameDecl() {
    return false;
  }

  public boolean isSortTomSymbolTable() {
    return false;
  }

  public boolean isSortTomEntry() {
    return false;
  }

  public boolean isSortTomStructureTable() {
    return false;
  }

  public boolean isSortTomError() {
    return false;
  }

  public boolean isSortTomList() {
    return false;
  }

  public boolean isSortTomNumberList() {
    return false;
  }

  public boolean isSortTomRuleList() {
    return false;
  }

  public boolean isSortTomTypeList() {
    return false;
  }

  public boolean isSortOptionList() {
    return false;
  }

  public boolean isSortInstructionList() {
    return false;
  }

  public boolean isSortSlotList() {
    return false;
  }

  public boolean isSortTomEntryList() {
    return false;
  }

  public boolean isSortTomErrorList() {
    return false;
  }

}
