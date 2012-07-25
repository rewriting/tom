package fr.irisa.cairn.xtext.utils.standalone;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.xtext.util.Files;

import fr.irisa.cairn.xtext.utils.RuntimeWorkflowComponent;

/**
 * XXX: to clean (ihneritance from Runtime)
 * 
 * @author antoine
 * 
 */
public class StandaloneImportInjector extends RuntimeWorkflowComponent {

	public final static String INJECTED = "inject";
	public final static String INJECTED_IMPORT = "import";
	private String path;
	private String genmodel;
	private StandaloneSetupWithPluginMappings setup;

	public StandaloneImportInjector(IProject project) {
		super(project);
		// TODO Auto-generated constructor stub
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		monitor.beginTask("Injecting imports in Java implementation files",
				ProgressMonitor.UNKNOWN);
		URI uri = URI.createURI(genmodel);

		// resourceSet.setPackageRegistry(EPackage.Registry.INSTANCE);
		Resource res = setup.getResourceSet().getResource(uri, true);
		GenModel gm = (GenModel) res.getContents().get(0);

		for (GenPackage gp : gm.getGenPackages()) {
			inject(gp);
		}
	}

	private void inject(GenPackage p) {
		for (EClassifier c : p.getEcorePackage().getEClassifiers()) {

			List<String> Strings = getStrings(c);
			if (Strings.size() > 0) {
				String file = getImplPath(c, p);
				String content = Files.readFileIntoString(file);
				for (String ii : Strings) {
					if (!content.contains("import " + ii))
						content = content.replaceAll("package .*;",
								"$0 \nimport " + ii + ";\n");
				}
				Files.writeStringIntoFile(file, content);
			}
		}
		for (GenPackage gp : p.getSubGenPackages()) {
			inject(gp);
		}
	}

	private String getImplPath(EClassifier e, GenPackage p) {
		String root;
		if (EcorePlugin.getWorkspaceRoot() == null) {
			root = System.getProperty("user.dir");
		} else {
			root = EcorePlugin.getWorkspaceRoot().getLocation().toFile()
					.getAbsolutePath();
		}
		// String root = getWorkspace();
		return root + path + getPath(p) + "/" + "impl" + "/" + e.getName()
				+ "Impl.java";
	}

	private String getPath(GenPackage e) {
		String p = "/" + e.getEcorePackage().getName();
		if (e.getEcorePackage().getESuperPackage() == null) {
			String prefix = "";
			for (String s : e.getBasePackage().split("\\.")) {
				prefix += "/" + s;
			}
			return prefix + p;
		} else {
			return getPath(e.getSuperGenPackage()) + p;
		}
	}

	public void setGenModel(String g) {

		genmodel = g;
	}

	private List<String> getStrings(EClassifier e) {
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

	public void setSetup(StandaloneSetupWithPluginMappings s) {
		this.setup = s;
	}

}
