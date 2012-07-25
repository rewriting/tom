package fr.irisa.cairn.xtext.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xpand2.output.PostProcessor;
import org.eclipse.xtend.type.impl.java.JavaBeansMetaModel;

public class XPandFacadeComponent extends RuntimeWorkflowComponent {
	private String encoding;
	private String path;
	private String rule;
	private String target;
	private List<PostProcessor> postProcessors;

	public XPandFacadeComponent(IProject project, String encoding, String path,
			String rule, String target) {
		super(project);
		this.encoding = encoding;
		this.path = path;
		this.rule = rule;
		this.target = target;
		this.postProcessors = new ArrayList<PostProcessor>();
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		XpandFacade facade = getXpandFacade();
		monitor.beginTask("Begin Xpand generation", ProgressMonitor.UNKNOWN);
		facade.evaluate(rule, ctx.get(target));
	}

	public void addPostProcessor(PostProcessor p) {
		postProcessors.add(p);
	}

	public XpandFacade getXpandFacade() {
		final OutputImpl output = new OutputImpl();
		final Outlet outlet = new Outlet(false, this.encoding, null, true,
				this.path);
		for (PostProcessor p : postProcessors) {
			outlet.addPostprocessor(p);
		}

		output.addOutlet(outlet);

		final XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(
				output, null);
		execCtx.getResourceManager().setFileEncoding(this.encoding);
		execCtx.registerMetaModel(new JavaBeansMetaModel());
		final XpandFacade facade = XpandFacade.create(execCtx);

		return facade;
	}

}
