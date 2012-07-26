import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.Constants;
//import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import tom.mapping.dsl.TomMappingStandaloneSetupGenerated;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class Main {

	@Inject 
	private Provider<ResourceSet> resourceSetProvider;
	
	@Inject
	private IResourceValidator validator;
	
//	@Inject
//	private IGenerator generator;
	
	@Inject 
	private JavaIoFileSystemAccess fileAccess;

	@Inject @Named(Constants.FILE_EXTENSIONS)
	private String fileExtension;
	
	public static void main(String[] args) {
		checkArgs(args);
		try {
			Injector injector = new TomMappingStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
			Main main = injector.getInstance(Main.class);
			main.run(args[0]);
			
		} catch (Exception e) {
			System.err.println("Unexpected error");
			e.printStackTrace();
		}
	}

	private static void checkArgs(String[] args) {
		if (args.length != 1) {
			System.err.println("Name of the source folder expected");
			System.exit(-1);
		} else {
			File folder = new File(args[0]);
			if (!(folder.exists() && folder.isDirectory())) {
				System.err.println("Source folder " + folder.getAbsolutePath() + " not found");
				System.exit(-1);
			}
		}
	}
	
	private void run(String folderName) throws IOException {
		// load the resource
		ResourceSet set = resourceSetProvider.get();
		Set<Resource> resources = new LinkedHashSet<Resource>();
		for (String fileName : getSourceFiles(folderName)) {
			Resource modelResource = set.getResource(URI.createURI(fileName), true);
			resources.add(modelResource);
		}

		// configure and start the generator
		fileAccess.setOutputPath("src-gen/");
		for (Resource resource : resources) {
			// validate the resource
			List<Issue> list = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
			if (!list.isEmpty()) {
				for (Issue issue : list) {
					System.err.println(issue);
				}
				System.exit(-2);
			}

			//generator.doGenerate(resource, fileAccess);
		}
		
		System.out.println("Code generation finished.");
	}
	
	private List<String> getSourceFiles(String folderName) throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		File folder = new File(folderName);
		for (String s : folder.list()) {
			File f = new File(folder.getAbsolutePath() + '/' + s);
			String relativePath = folderName + '/' + s;
			if (isModelFile(f)) {
				result.add(relativePath);
			}
			else if (f.isDirectory()) {
				result.addAll(getSourceFiles(relativePath));
			}
		}		
		return result;
	}

	private boolean isModelFile(File f) {
		return f.isFile() && f.getName().endsWith("." + fileExtension);
	}
}