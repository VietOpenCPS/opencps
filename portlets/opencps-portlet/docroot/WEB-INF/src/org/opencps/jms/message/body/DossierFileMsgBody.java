/**
 * OpenCPS is the open source Core Public Services software
 * Copyright (C) 2016-present OpenCPS community

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package org.opencps.jms.message.body;

import java.io.Serializable;

import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierPart;
import org.opencps.dossiermgt.model.FileGroup;

/**
 * @author trungnt
 */
public class DossierFileMsgBody implements Serializable {

	private static final long serialVersionUID = 1L;

	public byte[] getBytes() {

		return _bytes;
	}

	public DossierFile getDossierFile() {

		return _dossierFile;
	}

	public DossierPart getDossierPart() {

		return _dossierPart;
	}

	public String getExtension() {

		return _extension;
	}

	public String getFileDescription() {

		return _fileDescription;
	}

	public FileGroup getFileGroup() {

		return _fileGroup;
	}

	public String getFileName() {

		return _fileName;
	}

	public String getFileTitle() {

		return _fileTitle;
	}

	public String getMimeType() {

		return _mimeType;
	}

	public void setBytes(byte[] bytes) {

		this._bytes = bytes;
	}

	public void setDossierFile(DossierFile dossierFile) {

		this._dossierFile = dossierFile;
	}

	public void setDossierPart(DossierPart dossierPart) {

		this._dossierPart = dossierPart;
	}

	public void setExtension(String extension) {

		this._extension = extension;
	}

	public void setFileDescription(String fileDescription) {

		this._fileDescription = fileDescription;
	}

	public void setFileGroup(FileGroup fileGroup) {

		this._fileGroup = fileGroup;
	}
	
	public DossierPart getFileGroupDossierPart() {
	
		return _fileGroupDossierPart;
	}

	public void setFileGroupDossierPart(DossierPart fileGroupDossierPart) {
	
		this._fileGroupDossierPart = fileGroupDossierPart;
	}

	public void setFileName(String fileName) {

		this._fileName = fileName;
	}

	public void setFileTitle(String fileTitle) {

		this._fileTitle = fileTitle;
	}

	public void setMimeType(String mimeType) {

		this._mimeType = mimeType;
	}

	private byte[] _bytes;

	private DossierFile _dossierFile;

	private DossierPart _dossierPart;

	private FileGroup _fileGroup;
	
	private DossierPart _fileGroupDossierPart;

	private String _extension;

	private String _fileDescription;

	private String _fileName;

	private String _fileTitle;

	private String _mimeType;
}
