package tom.mapping.dsl.launch;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import tom.mapping.dsl.generator.TomMappingGenerator;

public abstract class AbstractTomMappingHandler extends AbstractHandler {
	private IFile _file;

	private GenerateAdapterJob _job;

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		try {
			this._file = this.getFilePath(event);
			if (this._job == null) {
				this._job = new GenerateAdapterJob();
			}
			this._job.schedule();
			return null;
		} catch (final ExecutionException e) {
			throw new ExecutionException(e.getMessage());
		}
	}

	abstract protected IFile getFilePath(final ExecutionEvent event)
			throws ExecutionException;

	private class GenerateAdapterJob extends Job {
	
		GenerateAdapterJob() {
			super("Generating Tom Mapping Source code for " + AbstractTomMappingHandler.this._file.getName());
		}
	
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
	
			try {
	
				System.out.println("Starting the tom mapping generator..");
				final TomMappingGenerator generator = new TomMappingGenerator(AbstractTomMappingHandler.this._file);
				generator.run(AbstractTomMappingHandler.this._file, monitor);
				System.out.println("DONE");
				AbstractTomMappingHandler.this._job = null;
			} catch (final Exception e) {
				System.out.println("FAILED "+e.getMessage());
				e.printStackTrace();
			}
			return Status.OK_STATUS;
		}
	}
}
