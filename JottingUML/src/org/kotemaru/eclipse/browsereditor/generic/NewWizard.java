package org.kotemaru.eclipse.browsereditor.generic;

import java.io.UnsupportedEncodingException;

import org.kotemaru.eclipse.browsereditor.AbstractNewWizard;


public class NewWizard extends AbstractNewWizard {

	private static final String TITLE = "JottingUML";
	private static final String DESC  = "";
	private static final String EXT   = "jul";
	private static final String INIT_DATA =
	"<?xml version='1.0' encoding='utf-8' ?>" +
	"<svg xml:space='preserve' width='1' height='1' xmlns='http://www.w3.org/2000/svg'>" +
	"<metadata id='JottingUML-data'>{}</metadata>" +
	"</svg>";

	
	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public String getDescription() {
		return DESC;
	}

	@Override
	public String getFileExtension() {
		return EXT;
	}

	@Override
	public byte[] getInitData() {
		try {
			return INIT_DATA.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
