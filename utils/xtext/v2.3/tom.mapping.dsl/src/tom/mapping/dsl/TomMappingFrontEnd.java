package tom.mapping.dsl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import model.Mapping;
import model.ModelPackage;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;



import com.google.inject.Injector;

@SuppressWarnings("deprecation")
public class TomMappingFrontEnd {

	private static final boolean FIX = true;
	
	public static Mapping parseString(String content) throws IOException {
		InputStream in = new StringBufferInputStream(content);
		return parseStream(in);
	}
	
	public static Mapping parseFile(String filename) throws IOException {
		
		InputStream in = new FileInputStream(filename);
		return parseStream(in);
	}
	public static Mapping parseStream(InputStream in) throws IOException {
 
		TomMappingStandaloneSetup instance = new TomMappingStandaloneSetup();
		Injector injector = instance.createInjectorAndDoEMFRegistration();
		TomMappingStandaloneSetup.doSetup();
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		XtextResourceSet rs = injector.getInstance(XtextResourceSet.class);	
		IResourceFactory factory  = injector.getInstance(IResourceFactory.class);
		XtextResource r = (XtextResource) factory.createResource(URI.createURI("internal.test"));
		rs.getResources().add(r);
		r.load(in,null);
		EcoreUtil.resolveAll(r);
		for(org.eclipse.emf.ecore.resource.Resource.Diagnostic error : r.getErrors()) {
			System.err.println(error);
			throw new RuntimeException(error.getMessage()+" "+error.getLine());
		}
		ICompositeNode node = r.getParseResult().getRootNode();
 		EObject root = r.getParseResult().getRootASTElement();
 		
 		Mapping toplevel= (Mapping) root;
 		
 		return toplevel;
	}

}