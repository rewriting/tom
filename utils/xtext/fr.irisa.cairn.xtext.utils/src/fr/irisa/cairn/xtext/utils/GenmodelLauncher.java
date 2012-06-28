package fr.irisa.cairn.xtext.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory.Descriptor;
import org.eclipse.emf.codegen.ecore.genmodel.GenBase;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenClassGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapterFactory;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

/**
 * Launcher for code generation of a {@link GenModel} in an Eclipse runtime
 * environment.
 * 
 * @author antoine
 * 
 */
public class GenmodelLauncher extends RuntimeWorkflowComponent {
	public final static String INJECTED = "inject";
	public final static String INJECTED_IMPORT = "import";

	public GenmodelLauncher(IProject project) {
		super(project);
		// TODO Auto-generated constructor stub
	}

	private String genmodel;


	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		monitor.beginTask("Generating JAVA code from Genmodel",
				ProgressMonitor.UNKNOWN);
		// Build genmodel
		URI uri = getURI(genmodel);
		Resource res = getResourceSet().getResource(uri, true);
		GenModel gm = (GenModel) res.getContents().get(0);
		gm.setForceOverwrite(false);
		gm.setCanGenerate(true);

		// Find imports

		List<String> imports = new UniqueEList<String>();
		for (GenPackage gp : gm.getGenPackages()) {
			inject(gp, imports);
		}

		// Build generator
		GeneratorAdapterFactory.Descriptor.Registry.INSTANCE.addDescriptor(
				GenModelPackage.eNS_URI, new MyDescriptor(imports));
		Generator generator = new Generator();
		generator.setInput(gm);
		generator.getOptions().codeFormatting = true;
		

		generator.generate(gm, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE,
				new BasicMonitor.Printing(System.out));

	}

	public void setGenModel(String g) {
		this.genmodel = g;
	}

	private static void inject(GenPackage p, List<String> imports) {
		for (EClassifier c : p.getEcorePackage().getEClassifiers()) {
			List<String> injectedImports = getInjectedImports(c);
			if (injectedImports.size() > 0) {
				for (String ii : injectedImports) {
					imports.add(ii);
				}
			}
		}
		for (GenPackage gp : p.getSubGenPackages()) {
			inject(gp, imports);
		}
	}

	private static List<String> getInjectedImports(EClassifier e) {
		List<String> injected = new ArrayList<String>();
		for (EAnnotation a : e.getEAnnotations()) {
			if (a.getSource().equals(INJECTED)) {
				EMap<String, String> details = a.getDetails();
				for (String k : details.keySet()) {
					if (k.equals(INJECTED_IMPORT)) {
						String ii = details.get(k);
						injected.add(ii);
					}
				}
			}
		}
		return injected;
	}

	private static class MyDescriptor implements Descriptor {
		private List<String> imports;

		public MyDescriptor(List<String> imports) {
			super();
			this.imports = imports;
		}

		public GeneratorAdapterFactory createAdapterFactory() {
			return new GenModelGeneratorAdapterFactory() {

				@Override
				public Adapter createGenClassAdapter() {
					return new MyGenClassGeneratorAdapter(this, imports);
				}

				@Override
				public Adapter createGenOperationAdapter() {
					// TODO Auto-generated method stub
					return super.createGenOperationAdapter();
				}
			};
		}
	}

	// private static class MyGenOperationAdapter extends GenOperator

	private static class MyGenClassGeneratorAdapter extends
			GenClassGeneratorAdapter {
		private List<String> imports;

		public MyGenClassGeneratorAdapter(
				GeneratorAdapterFactory generatorAdapterFactory,
				List<String> imports) {
			super(generatorAdapterFactory);
			this.imports = imports;
		}

		@Override
		protected void createImportManager(String packageName, String className) {
			importManager = new MyImportManager(packageName);
			importManager.addMasterImport(packageName, className);
			updateImportManager();
			for (String ii : imports) {
				getImportManager().addImport(ii);
			}
		}

		private void updateImportManager() {
			if (generatingObject != null) {
				((GenBase) generatingObject).getGenModel().setImportManager(
						importManager);
			}
		}

		private static class MyImportManager extends ImportManager {

			public MyImportManager(String compilationUnitPackage) {
				super(compilationUnitPackage);
			}

			@Override
			public void addImport(String qualifiedName) {
				if (qualifiedName.contains("static ")) {
					imports.add(qualifiedName);
				} else
					super.addImport(qualifiedName);
			}

		}

	}

}
