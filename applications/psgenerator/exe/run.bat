@echo try -Djava.util.logging.config.file=logging.properties for logging
@echo try -ea for tracing... will create a big psgen.trace file
@java -cp %CLASS_PATH%;..\lib\antlr.jar;..\lib\apigen.jar;..\lib\aterm.jar;..\lib\concurrent.jar;..\lib\jtom.jar;..\lib\mutraveler.jar;..\lib\PlatformOption.jar;..\lib\plugin-platform.jar;..\lib\Set.jar;..\lib\shared-objects.jar;..\lib\TNode.jar;..\lib\tom-library.jar;..\lib\TomSignature.jar;..\lib\vas-to-adt.jar;..\lib\commandline.jar;..\lib\psgen.jar fr.loria.protheo.psgenerator.Generator -o:clips ../examples/houses.pss
@pause