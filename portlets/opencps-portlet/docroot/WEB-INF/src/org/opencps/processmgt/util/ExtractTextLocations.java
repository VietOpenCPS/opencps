package org.opencps.processmgt.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

public class ExtractTextLocations extends PDFTextStripper {

	private float anchorX = 0;
	private float anchorY = 0;
	private float signatureWidth = 0;
	private float signatureHeight = 0;
	private float pageWidth = 0;
	private float pageHeight = 0;
	private float pageLLX = 0;
	private float pageURX = 0;
	private float pageLLY = 0;
	private float pageURY = 0;

	public ExtractTextLocations() throws IOException {
		super.setSortByPosition(true);
	}

	public ExtractTextLocations(String fullPath) throws IOException {
		_log.info("-------------------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		// getSignaturePosition(fullPath);
		PDDocument document = null;
		//System.out.println("call ExtractTextLocations:::::::" +fullPath );
		try {
			File input = new File(fullPath);
			document = PDDocument.load(input);
		
			_log.info("-------------------------BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
			//System.out.println("document:::::::" +document );
			if (document.isEncrypted()) {
				_log.info("-------------------------111111111111111111111111111111111111");
				try {
					document.decrypt("");
					_log.info("-------------------------22222222222222222222222222222222222");
				} catch (Exception e) {
					_log.error(e);
				}
			}

			// ExtractTextLocations printer = new ExtractTextLocations();
			_log.info("-------------------------3333333333333333333333333333333333333");

			List allPages = document.getDocumentCatalog().getAllPages();
			if (allPages != null && allPages.size() > 0) {
				PDPage page = (PDPage) allPages.get(0);

				PDStream contents = page.getContents();
				_log.info("-------------------------444444444444444444444444444444444");
				if (contents != null) {
					this.processStream(page, page.findResources(), page.getContents().getStream());
				}
				_log.info("-------------------------55555555555555555555555555555555555");

				PDRectangle pageSize = page.findMediaBox();
				if (pageSize != null) {
					setPageWidth(pageSize.getWidth());
					setPageHeight(pageSize.getHeight());
					setPageLLX(pageSize.getLowerLeftX());
					setPageURX(pageSize.getUpperRightX());
					setPageLLY(pageSize.getLowerLeftY());
					setPageURY(pageSize.getUpperRightY());
				}
			}
		}
		
		catch (Exception e) {
			System.out.println("Exception:::::::" +e );
		} finally {
			if (document != null) {
				document.close();
			}
		}
		_log.info("-------------------------CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
	}
/*
	public ExtractTextLocations(String fullPath) throws IOException {
		// getSignaturePosition(fullPath);
		PDDocument document = null;
		//System.out.println("call ExtractTextLocations:::::::" +fullPath );
		try {
			File input = new File(fullPath);
			document = PDDocument.load(input);
			//System.out.println("document:::::::" +document );
			if (document.isEncrypted()) {
				try {
					document.decrypt("");
				} catch (Exception e) {
					_log.error(e);
				}
			}

			// ExtractTextLocations printer = new ExtractTextLocations();

			List allPages = document.getDocumentCatalog().getAllPages();
			if (allPages != null && allPages.size() > 0) {
				PDPage page = (PDPage) allPages.get(0);

				PDStream contents = page.getContents();
				if (contents != null) {
					this.processStream(page, page.findResources(), page.getContents().getStream());
				}

				PDRectangle pageSize = page.findMediaBox();
				if (pageSize != null) {
					setPageWidth(pageSize.getWidth());
					setPageHeight(pageSize.getHeight());
					setPageLLX(pageSize.getLowerLeftX());
					setPageURX(pageSize.getUpperRightX());
					setPageLLY(pageSize.getLowerLeftY());
					setPageURY(pageSize.getUpperRightY());
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception:::::::" +e );
		} finally {
			if (document != null) {
				document.close();
			}
		}
	}
	*/
	private void getSignaturePosition(String fullPath) throws IOException {

		_log.info("-------------------------CCCCCCCCCCCCCCCCCCCCCCCgetSignaturePositionCCCCCCCCCCCCCC");
		PDDocument document = null;
		try {
			File input = new File(fullPath);
			document = PDDocument.load(input);
			if (document.isEncrypted()) {
				try {
					document.decrypt("");
				} catch (Exception e) {
					_log.error(e);
				}
			}

			// ExtractTextLocations printer = new ExtractTextLocations();

			List allPages = document.getDocumentCatalog().getAllPages();
			if (allPages != null && allPages.size() > 0) {
				PDPage page = (PDPage) allPages.get(allPages.size() - 1);
				PDStream contents = page.getContents();
				if (contents != null) {
					this.processStream(page, page.findResources(), page
							.getContents().getStream());
				}
			}

		} finally {
			if (document != null) {
				document.close();
			}
		}
	}

	@Override
	protected void processTextPosition(TextPosition text) {
		/*
		 * System.out.println("String[" + text.getXDirAdj() + "," +
		 * text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale=" +
		 * text.getXScale() + " height=" + text.getHeightDir() + " space=" +
		 * text.getWidthOfSpace() + " width=" + text.getWidthDirAdj() + "]" +
		 * text.getCharacter());
		 */
		if (text.getCharacter().equals(StringPool.POUND)
				&& text.getFontSize() == 1L) {

			System.out.println("String[" + text.getXDirAdj() + ","
					+ text.getYDirAdj() + " fs=" + text.getFontSize()
					+ " xscale=" + text.getXScale() + " height="
					+ text.getHeightDir() + " space=" + text.getWidthOfSpace()
					+ " width=" + text.getWidthDirAdj() + "]"
					+ text.getCharacter());

			System.out.println("String[" + text.getX() + "," + text.getY()
					+ " fs=" + text.getFontSize() + " xscale="
					+ text.getXScale() + " height=" + text.getHeight()
					+ " space=" + text.getWidthOfSpace() + " width="
					+ text.getWidth() + "]" + text.getCharacter());
			setAnchorX(text.getX());
			setAnchorY(text.getY());
			setSignatureHeight(text.getHeight());
			setSignatureWidth(text.getWidth());
		}

	}

	public float getAnchorX() {
		return anchorX;
	}

	public void setAnchorX(float anchorX) {
		this.anchorX = anchorX;
	}

	public float getAnchorY() {
		return anchorY;
	}

	public void setAnchorY(float anchorY) {
		this.anchorY = anchorY;
	}

	public float getSignatureWidth() {
		return signatureWidth;
	}

	public void setSignatureWidth(float signatureWidth) {
		this.signatureWidth = signatureWidth;
	}

	public float getSignatureHeight() {
		return signatureHeight;
	}

	public void setSignatureHeight(float signatureHeight) {
		this.signatureHeight = signatureHeight;
	}

	public float getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
	}

	public float getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
	}
	
	

	public float getPageLLX() {
		return pageLLX;
	}

	public void setPageLLX(float pageLLX) {
		this.pageLLX = pageLLX;
	}

	public float getPageURX() {
		return pageURX;
	}

	public void setPageURX(float pageURX) {
		this.pageURX = pageURX;
	}

	public float getPageLLY() {
		return pageLLY;
	}

	public void setPageLLY(float pageLLY) {
		this.pageLLY = pageLLY;
	}

	public float getPageURY() {
		return pageURY;
	}

	public void setPageURY(float pageURY) {
		this.pageURY = pageURY;
	}



	private Log _log = LogFactoryUtil.getLog(ExtractTextLocations.class);

}