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

package org.opencps.util;

import java.security.SignatureException;
import java.security.cert.X509Certificate;

import org.opencps.pki.PdfPkcs7Signer;
import org.opencps.pki.PdfSigner;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author trungnt
 */

public class SignatureUtil {

	/**
	 * @param filePath
	 * @param certPath
	 * @param tempFilePath
	 * @param signedFilePath
	 * @param isVisible
	 * @param imagePath
	 * @return
	 */
	public static PdfSigner getPdfSigner(
		String filePath, String certPath, String tempFilePath,
		String signedFilePath, boolean isVisible, String imagePath) {

		X509Certificate cert = null;
		PdfSigner pdfSigner = null;
		try {
			cert = CertUtil.getX509CertificateByPath(certPath);
		}
		catch (Exception e) {
			_log.error(e);
		}

		if (cert != null) {
			pdfSigner =
				new PdfSigner(
					filePath, cert, tempFilePath, signedFilePath, isVisible);

			if (Validator.isNotNull(imagePath)) {
				pdfSigner.setSignatureGraphic(imagePath);
			}

		}

		return pdfSigner;
	}

	public static PdfPkcs7Signer getPdfPkcs7Signer(
		String filePath, String certPath, String tempFilePath,
		String signedFilePath, boolean isVisible, String imagePath) {

		X509Certificate cert = null;
		PdfPkcs7Signer pdfSigner = null;
		try {
			cert = CertUtil.getX509CertificateByPath(certPath);
		}
		catch (Exception e) {
			_log.error(e);
		}

		if (cert != null) {
			pdfSigner =
				new PdfPkcs7Signer(
					filePath, cert, tempFilePath, signedFilePath, isVisible);

			if (Validator.isNotNull(imagePath)) {
				pdfSigner.setSignatureGraphic(imagePath);
			}

		}

		return pdfSigner;
	}

	/**
	 * @param pdfSigner
	 * @param llx
	 * @param lly
	 * @param urx
	 * @param ury
	 * @return
	 * @throws SignatureException
	 */
	public static byte[] computerHash(
		PdfSigner pdfSigner, float llx, float lly, float urx, float ury)
		throws SignatureException {

		return pdfSigner.computeHash(llx, lly, urx, ury);
	}

	private static Log _log =
		LogFactoryUtil.getLog(SignatureUtil.class.getName());

}
