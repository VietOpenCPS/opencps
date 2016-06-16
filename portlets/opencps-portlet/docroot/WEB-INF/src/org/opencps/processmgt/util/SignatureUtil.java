package org.opencps.processmgt.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.imageio.ImageIO;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.opencps.util.PortletPropsKeys;

import vgca.svrsigner.ServerSigner;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.util.portlet.PortletProps;

public class SignatureUtil {
	
	public static Log log = LogFactoryUtil.getLog(SignatureUtil.class);

	public static void genComputerHashByNoiDungHoSo(ResourceRequest resourceRequest, ResourceResponse resourceResponse, FileEntry fileEntry, long ks, long dossierFileId, long dossierPartId, long index, long indexSize) throws IOException {
		String result = StringPool.BLANK;
		log.info("------vao 0 genComputerHashByNoiDungHoSo-----");
		long userId = PortalUtil.getUserId(resourceRequest);
		byte[] inHash = null;
		String fieldName = StringPool.BLANK;
		JSONObject jsonFeed = JSONFactoryUtil.createJSONObject();
		String tenDangNhap = "";

		JSONArray hashComputers = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray signFieldNames = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray filePaths = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray messages = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray fileNames = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray dossierFileIds = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray dossierPartIds = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray indexs = JSONFactoryUtil.getJSONFactory().createJSONArray();
		JSONArray indexSizes = JSONFactoryUtil.getJSONFactory().createJSONArray();
		String filePath = StringPool.BLANK;
		File file = null;
		try {
			// String realExportPathTmp = request.getContextPath();
			User user = UserLocalServiceUtil.fetchUser(userId);
			if (user != null) {
				tenDangNhap = user.getScreenName();
			}
			log.info("***************************tenDangNhap: " + tenDangNhap);
			
			String realPath = ReportUtils.getTemplateReportFilePath(resourceRequest);
			String realExportPath = realPath + "chuky/";

			String imageBase64 = StringPool.BLANK;
			String cer = realExportPath;
			String cerPath = cer + tenDangNhap + ".cer";
			String signImagePath = "";
			String imgsrc = realExportPath;
			if(ks == 1){
				signImagePath = imgsrc + tenDangNhap + ".png";
			}else{
				signImagePath = imgsrc + tenDangNhap + "_condau.png";
			}
			

			log.info("***************************cerPath: " + cerPath);
			log.info("***************************signImagePath: " + signImagePath);

			imageBase64 = SignatureUtil.getSignatureImageBase64ByPath(signImagePath);

			BufferedImage bufferedImage = SignatureUtil.getImageByPath(signImagePath);

			// tinh toa do chu ky
			String realExportDir = PortletProps
				    .get("opencps.file.system.temp.dir");
			filePath = saveAsPdf(realExportDir, fileEntry.getFileEntryId());
			file = new File(filePath);
//			filePath = getDLFileAbsPath(fileEntry);
			//TODO:
			// save dlFileEntry -> /liferay/tmp (a.pdf)
			// computerHash -> a.pdf -> a.temp.pdf
			// 2: signComlete (a.temp.pdf -> a.signed.pdf)
			// 3. a.signed.pdf -> document media thay cho dlFileEntry
			// 4. removes
			
			ExtractTextLocations textLocation = new ExtractTextLocations(filePath);

			log.info("*********************************" + textLocation.getAnchorX() + "-" + textLocation.getAnchorY()
					+ "********************************");

			log.info("*********************************" + textLocation.getPageLLX() + "-" + textLocation.getPageURX() + "-"
					+ textLocation.getPageLLY() + "-" + textLocation.getPageURY() + "*******************************");

			// doc file cer tren server
			Certificate cert = SignatureUtil.getCertificateByPath(cerPath);
			ServerSigner signer = SignatureUtil.getServerSigner(filePath, cert, imageBase64);

			log.info("***************************signer: " + signer + "*******filePath:" + filePath);

			// tinh kich thuoc cua anh

			int signatureImageWidth = (bufferedImage != null && bufferedImage.getWidth() > 0) ? bufferedImage.getWidth() : 80;

			int signatureImageHeight = (bufferedImage != null && bufferedImage.getHeight() > 0) ? bufferedImage.getHeight() : 80;
			float llx = textLocation.getAnchorX();

			float urx = llx + signatureImageWidth / 3;

			float lly = textLocation.getPageURY() - textLocation.getAnchorY() - signatureImageHeight / 3;

//			float lly = textLocation.getAnchorY() - signatureImageHeight / 3;
			
			float ury = lly + signatureImageHeight / 3;

			// inHash = signer.computeHash(new Rectangle(llx + 65, lly - 55, urx
			// + 114, ury-20), 1);
			log.info("***************************llx: " + llx + "*******urx:" + urx);
			log.info("***************************lly: " + lly + "*******ury:" + ury);
			signer.setSignatureAppearance(PdfSignatureAppearance.RenderingMode.GRAPHIC);   
			inHash = signer.computeHash(new Rectangle(llx, (lly/2 - 10), urx, ury), 1);

//			filePath2 = "/opt/liferay/jboss-7.0.2/standalone/deployments/TichHopGiaoThong-portlet.war/export/30798683514999_Shifting_Order.pdf";

			fieldName = signer.getSignatureName();
			signFieldNames.put(fieldName);
			hashComputers.put(Base64.encode(inHash));
			filePaths.put(filePath);
			fileNames.put(fileEntry.getTitle());
			dossierFileIds.put(dossierFileId+"");
			dossierPartIds.put(dossierPartId+"");
			indexs.put(index+"");
			indexSizes.put(indexSize+"");
			log.info("**************inHash: " + inHash + "-----------fieldName: " + fieldName + "----------filePath: " + filePath);
			messages.put("success");
		} catch (Exception e) {
			messages.put("Error signature filePath: " + filePath);
			hashComputers.put(StringPool.BLANK);
			signFieldNames.put(StringPool.BLANK);
			filePaths.put(filePath);
			log.error(e);
		}finally{
			if(Validator.isNotNull(file) && file.exists()){
				file.delete();
			}
		}

		
		log.info("-----------hashComputers: " +hashComputers );
		jsonFeed.put("hashComputers", hashComputers);
		jsonFeed.put("signFieldNames", signFieldNames);
		jsonFeed.put("filePaths", filePaths);
		jsonFeed.put("msg", messages);
		jsonFeed.put("fileNames", fileNames);
		jsonFeed.put("dossierFileIds", dossierFileIds);
		jsonFeed.put("dossierPartIds", dossierPartIds);
		jsonFeed.put("indexs", indexs);
		jsonFeed.put("indexSizes", indexSizes);
		PrintWriter out = resourceResponse.getWriter();
		out.print(jsonFeed.toString());

	}

	public static Certificate getCertificateByPath(String path) throws CertificateException, FileNotFoundException, URISyntaxException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		Certificate cert = cf.generateCertificate(new FileInputStream(new File(path)));

		return cert;
	}

	public static Certificate getCertificateByURL(String url) throws CertificateException, FileNotFoundException, URISyntaxException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		Certificate cert = cf.generateCertificate(new FileInputStream(new File(new URI(url))));

		return cert;
	}

	public static ServerSigner getServerSigner(String fullPath, Certificate cert, String imageBase64) {
		ServerSigner signer = new ServerSigner(fullPath, cert);
		signer.setSignatureGraphic(imageBase64);
		signer.setSignatureAppearance(PdfSignatureAppearance.RenderingMode.GRAPHIC_AND_DESCRIPTION);
		return signer;
	}

	public static String getSignatureImageBase64(String url) {

		String base64 = StringPool.BLANK;

		InputStream is = null;

		ByteArrayOutputStream os = null;

		try {
			is = new URL(url).openStream();
			os = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			byte[] imageBuff = null;

			int length;

			while ((length = is.read(buffer)) != -1)
				os.write(buffer, 0, length); // copy streams

			imageBuff = os.toByteArray();

			base64 = Base64.encode(imageBuff);

		} catch (Exception e) {
			_log.error(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				_log.error(e);
			}
		}
		return base64;
	}

	public static String getSignatureImageBase64ByPath(String fullPath) {

		String base64 = StringPool.BLANK;

		InputStream is = null;

		ByteArrayOutputStream os = null;

		try {
			is = new FileInputStream(new File(fullPath));
			os = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];

			byte[] imageBuff = null;

			int length;

			while ((length = is.read(buffer)) != -1)
				os.write(buffer, 0, length); // copy streams

			imageBuff = os.toByteArray();

			base64 = Base64.encode(imageBuff);

		} catch (Exception e) {
			_log.error(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				_log.error(e);
			}
		}
		return base64;
	}

	public static BufferedImage getImage(String url) throws IOException, URISyntaxException {
		InputStream is = null;
		BufferedImage bimg = null;
		try {
			is = new URL(url).openStream();
			bimg = ImageIO.read(is);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			if (is != null) {
				is.close();
			}
		}

		return bimg;
	}

	public static BufferedImage getImageByPath(String fullPath) throws IOException, URISyntaxException {
		InputStream is = null;
		BufferedImage bimg = null;
		try {
			is = new FileInputStream(new File(fullPath));
			bimg = ImageIO.read(is);
		} catch (Exception e) {
			_log.error(e);
		} finally {
			if (is != null) {
				is.close();
			}
		}

		return bimg;
	}

	public static String saveAsPdf(String dest, long fileId) throws IOException {
		log.info("--999994444349999--`-saveAsPdf----9934343499999-");
		// BufferedImage image = null;
		InputStream is = null;
		OutputStream os = null;
		String imagePath = StringPool.BLANK;
		try {
			
			// URL url = new URL(strURL);
			// image = ImageIO.read(url);
			// is = new URL(strURL).openStream();
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileId);
			if (fileEntry == null)
				return null;
			// is = getInputStreamByFileEntryId(fileId);
			is = fileEntry.getContentStream();
			// image = ImageIO.read(is);
			imagePath = dest + fileEntry.getTitle();
			// ImageIO.write(image, ext, new File(fileName));
			
			os = new FileOutputStream(imagePath);
			
			byte[] b = new byte[1024];
			int length;
			
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			
		} catch (Exception e) {
			log.error("111111111111111111111111111111111111");
			_log.error(e);
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
		return imagePath;
	}

	public static String saveAsImage(String strURL, String dest, String email, String ext, long fileId) throws IOException, PortalException, SystemException {
		log.info("--999994444349999--`-saveAsImage----9934343499999-");
		// BufferedImage image = null;
		InputStream is = null;
		OutputStream os = null;
		String imagePath = StringPool.BLANK;
		try {

			// URL url = new URL(strURL);
			// image = ImageIO.read(url);
			// is = new URL(strURL).openStream();
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileId);
			is = fileEntry.getContentStream();
			// image = ImageIO.read(is);
			imagePath = dest + email + "." + ext;
			// ImageIO.write(image, ext, new File(fileName));

			os = new FileOutputStream(imagePath);

			byte[] b = new byte[1024];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

		} catch (IOException e) {
			_log.error(e);
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
		return imagePath;
	}

//	public static FileEntry getFileEntry(long fileEnTryId) {
//		FileEntry fileEntry = null;
//
//		log.info("--999994444349999--`-fileEntry----9934343499999-" + fileEntry);
//		try {
//			long userId = ConfigurationManager.getLongProp("id_admin", 10196);
//			// long userId = 10198;
//			// LogFactoryMOC.getLog(DocumentUtils.class).debug("===Starting check permission============userId======="
//			// + userId);
//			User user = UserLocalServiceUtil.getUserById(userId);
//
//			PermissionThreadLocal.setPermissionChecker(PermissionCheckerFactoryUtil.create(user, true));
//			fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEnTryId);
//
//			// inputStream = fileEntry.getContentStream();
//		} catch (Exception e) {
//			e.printStackTrace();
//			// LogFactoryMOC.getLog(DocumentUtils.class).error(e);
//		}
//
//		// LogFactoryMOC.getLog(DocumentUtils.class).debug("===== getInputStreamByFileEntryId return inputStream :::"
//		// + inputStream);
//		return fileEntry;
//	}

//	public static InputStream getInputStreamByFileEntryId(long fileEnTryId) {
//		InputStream inputStream = null;
//		log.info("--999994444349999--`vao day -getInputStreamByFileEntryId----9934343499999-");
//		try {
//			long userId = ConfigurationManager.getLongProp("id_admin", 10196);
//			User user = UserLocalServiceUtil.getUserById(userId);
//
//			PermissionThreadLocal.setPermissionChecker(PermissionCheckerFactoryUtil.create(user, true));
//			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEnTryId);
//
//			inputStream = fileEntry.getContentStream();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return inputStream;
//	}
	public static String getDLFileAbsPath(FileEntry fileEntry) 
			throws PortalException, SystemException {
			  return PortletProps
					    .get("opencps.file.system.dir") + "/document_library/"
			    + fileEntry.getCompanyId() + "/"
			    + fileEntry.getFolderId() + "/"
			    + ((DLFileEntry) fileEntry.getModel()).getName() + "/"
			    + fileEntry.getVersion();
			}
	private static Log _log = LogFactoryUtil.getLog(SignatureUtil.class);
}
