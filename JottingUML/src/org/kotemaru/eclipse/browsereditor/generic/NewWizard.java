package org.kotemaru.eclipse.browsereditor.generic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.kotemaru.eclipse.browsereditor.AbstractNewWizard;


public class NewWizard extends AbstractNewWizard {

	private static byte[] initData = null;
	
	@Override
	public String getTitle() {
		return GenericConfig.get("NewWizard.title");
	}

	@Override
	public String getDescription() {
		return GenericConfig.get("NewWizard.desc");
	}

	@Override
	public String getFileExtension() {
		return GenericConfig.get("NewWizard.ext");
	}

	@Override
	public byte[] getInitData() {
		if (initData == null)  initData = makeInitDate();
		return initData;
	}

	private byte[] makeInitDate() {
		String fname = GenericConfig.get("NewWizard.initFile");
		try {
			InputStream in = this.getClass().getResourceAsStream(fname);
			try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				int n=0;
				byte[] buff = new byte[1024];
				while ((n=in.read(buff))>0) {
					bout.write(buff,0,n);
				}
				byte[] data = bout.toByteArray();
				return data;
			} finally {
				in.close();
			}
		} catch (IOException e) {
			return null;
		}
	}

	
	
}
