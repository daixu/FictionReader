package com.shangame.fiction.ui.wifi.nanohttpd;

import org.nanohttpd.protocols.http.tempfiles.ITempFileManager;
import org.nanohttpd.util.IFactory;

public class UploadFileManagerFactory implements IFactory<ITempFileManager> {

	@Override
	public ITempFileManager create() {
		return new UploadFileManager();
	}
}