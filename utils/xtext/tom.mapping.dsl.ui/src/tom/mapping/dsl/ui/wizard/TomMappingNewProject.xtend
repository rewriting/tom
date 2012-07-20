package tom.mapping.dsl.ui.wizard

import org.eclipse.xtext.generator.IFileSystemAccess

class TomMappingNewProject {
	
	extension Tools = new Tools()
	
	String prefix = ""
	
	def compile(TomMappingProjectInfo tmpi, IFileSystemAccess fsa) {
		
		fsa.generateFile(prefix+"src/mappings/Model.tmap", tmpi.model())
		fsa.generateFile(prefix+"META-INF/MANIFEST.MF", tmpi.manifest())
		fsa.generateFile(prefix+"src/"+tmpi.projectName.path()+"/generator/MappingGenerator.java", tmpi.generator())
		fsa.generateFile(prefix+".project", tmpi.project())
		fsa.generateFile(prefix+"src/"+tmpi.projectName.path()+"/tom/sources/Example.t", tmpi.tomFile())
	}
	
	
	def main(TomMappingProjectInfo tmpi) {
		tmpi.model()
		tmpi.generator()
		tmpi.manifest()
		tmpi.tomFile()
		tmpi.project()
	}
	
	def model(TomMappingProjectInfo tmpi) {
		'''
		TomMapping MyMapping {
			
		}
		'''
	}
	
	
	def manifest(TomMappingProjectInfo tmpi) {
		'''
		Manifest-Version: 1.0
		Bundle-ManifestVersion: 2
		Bundle-Name: «tmpi.projectName»
		undle-Vendor: My Company
		Bundle-Version: 1.0.0
		Bundle-SymbolicName: «tmpi.projectName»; singleton:=true
		Bundle-ActivationPolicy: lazy
		Require-Bundle: com.ibm.icu,
		org.eclipse.xtext,
		org.eclipse.xtext.generator,
		org.eclipse.xtend,
		org.eclipse.xtend.typesystem.emf,
		org.eclipse.xpand,
		de.itemis.xtext.antlr;resolution:=optional,
		 org.eclipse.xtend.util.stdlib,
		org.eclipse.emf.mwe2.launch;resolution:=optional,
		tom.mapping.dsl.generator,
		org.eclipse.jface.text;bundle-version="3.6.0",
		org.eclipse.jdt.core;bundle-version="3.6.0",
		tom.mapping.model;bundle-version="1.0.0",
		org.eclipse.xtext.ecore;bundle-version="1.0.0",
		org.eclipse.core.runtime;bundle-version="3.6.0",
		fr.irisa.cairn.eclipse.tom;bundle-version="1.0.0"
		Import-Package: org.apache.log4j,
		org.apache.commons.logging
		Bundle-RequiredExecutionEnvironment: J2SE-1.5
		'''		
	}
	
	
	def generator(TomMappingProjectInfo tmpi) {
		'''
		package «tmpi.projectName».generator;

import tom.mapping.dsl.generator.TomMappingGenerator;

public class MappingGenerator {
		private final static String MODEL_PATH = "src/mappings/";
		private final static String TOM_PATH = "src-tom/";
		private final static String JAVA_PACKAGE = "internal/";
		private final static String JAVA_PATH = "src-gen/«tmpi.projectName.path()»/";
		private final static String ENCODING = "UTF-8";

		public static void main(String[] args) {
		MappingGenerator.generate();
		}
		
		public static void generate() {
		TomMappingGenerator.generate(MODEL_PATH, JAVA_PATH, JAVA_PACKAGE,
				TOM_PATH, ENCODING);
		}
}
		'''
	} 
	
	
	def project(TomMappingProjectInfo tmpi) {
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		<projectDescription>
	<name>«tmpi.projectName»</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>fr.loria.eclipse.tom.GomBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>fr.loria.eclipse.tom.TomBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>fr.loria.eclipse.tom.TomBuildAnalyser</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.pde.ManifestBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.pde.SchemaBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.xtext.ui.shared.xtextBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
		<nature>org.eclipse.pde.PluginNature</nature>
		<nature>org.eclipse.xtext.ui.shared.xtextNature</nature>
	</natures>
</projectDescription>
		'''
	}	
	
	
	def tomFile(TomMappingProjectInfo tmpi) {
		'''
		package «tmpi.projectName».tom;

		public class Example {

		}
		'''
	}
	
}