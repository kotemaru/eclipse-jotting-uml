package org.kotemaru.eclipse.browsereditor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;


public abstract class AbstractNewWizard extends BasicNewResourceWizard {

	private NewWizardPage mainPage;
	
	public abstract String getTitle();
	public abstract String getDescription();
	public abstract String getFileExtension();
	public abstract byte[] getInitData();
	
	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public void addPages() {
		super.addPages();
		mainPage = new NewWizardPage(getTitle(), getSelection(), getInitData());
		mainPage.setTitle(getTitle());
		mainPage.setDescription(getDescription());
		mainPage.setFileExtension(getFileExtension());
		addPage(mainPage);
	}

	/* (non-Javadoc)
	 * Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle("JottingUML");
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * Method declared on IWizard.
	 */
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
		if (file == null) {
			return false;
		}

		selectAndReveal(file);

		// Open editor on new file.
		IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
		try {
			if (dw != null) {
				IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	
	static class NewWizardPage extends WizardNewFileCreationPage {
		private final byte[] initData;

		public NewWizardPage(String pageName, IStructuredSelection selection, byte[] data) {
			super(pageName, selection);
			this.initData = data;
		}
		
		protected InputStream getInitialContents() {
			return new ByteArrayInputStream(initData);
		}
	}

}
