package org.kotemaru.eclipse.genericeditor;

import java.io.UnsupportedEncodingException;

import org.kotemaru.eclipse.browsereditor.AbstractNewWizard;


public class NewWizard extends AbstractNewWizard {

	public NewWizard() {
		this.get
	}

	@Override
	public String getTitle() {
		return Activator.getMe().getTitle();
	}

	@Override
	public String getDescription() {
		return Activator.getMe().getDesc();
	}

	@Override
	public String getFileExtension() {
		return Activator.getMe().getExtention();
	}

	@Override
	public byte[] getInitData() {
		try {
			return Activator.getMe().getInitData().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
