#
#    TOM - To One Matching Compiler
#

javadir         = ${prefix}/classes/jtom/runtime/xml/adt
CLASSPATH_ENV   = CLASSPATH=$(JAVAROOT):$(srcdir)/$(JAVAROOT):$(JAVA_ATERM):$(SHARED_OBJECTS):$$CLASSPATH


JAVASOURCES     = TNodeFactory.java TNodeConstructor.java\
 TNode.java TNodeImpl.java\
 TNode_CommentNodeImpl.java TNode_CommentNode.java\
 TNode_ProcessingInstructionNodeImpl.java TNode_ProcessingInstructionNode.java\
 TNode_TextNodeImpl.java TNode_TextNode.java\
 TNode_CDATASectionNodeImpl.java TNode_CDATASectionNode.java\
 TNode_DocumentNodeImpl.java TNode_DocumentNode.java\
 TNode_ElementNodeImpl.java TNode_ElementNode.java\
 TNode_AttributeNodeImpl.java TNode_AttributeNode.java\
 TNode_DocumentTypeNodeImpl.java TNode_DocumentTypeNode.java\
 TNode_EntityReferenceNodeImpl.java TNode_EntityReferenceNode.java\
 TNode_EntityNodeImpl.java TNode_EntityNode.java\
 TNode_NotationNodeImpl.java TNode_NotationNode.java\
 TNodeList.java TNodeListImpl.java

java_JAVA       = $(JAVASOURCES)
EXTRA_DIST      = $(JAVASOURCES) TNode.tom

JAVAROOT        = ${top_builddir}/stable/

adtdir          = $(datadir)/jtom
adt_DATA        = TNode.tom
