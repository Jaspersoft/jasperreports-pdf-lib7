/*
 * JasperReports Modern PDF Exporter
 * Copyright © 2005 - 2018 TIBCO Software Inc.
 * http://www.jaspersoft.com.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.export.pdf;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.options.SerializeOptions;
import com.itextpdf.kernel.Version;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.PdfConst;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfXmpCreator
{

	private static final Log log = LogFactory.getLog(PdfXmpCreator.class);
	
	private static final boolean XMP_LIBRARY;

	static
	{
		XMP_LIBRARY = determineXmpLibrary();
	}

	private static boolean determineXmpLibrary()
	{
		try
		{
			Class.forName("com.adobe.xmp.XMPMetaFactory");
			return true;
		} catch (ClassNotFoundException e)
		{
			log.info("Adobe XMP library not found");
			return false;
		}
	}

	public static boolean supported()
	{
		return XMP_LIBRARY;
	}

	public static byte[] createXmpMetadata(PdfDocument document, PdfaConformanceEnum pdfaConformance)
	{
		XmpWriter writer = new XmpWriter(document, pdfaConformance);
		return writer.createXmpMetadata();
	}

}


class XmpWriter
{
	private static final String FORMAT_PDF = "application/pdf";

	private static final String PDF_PRODUCER = "Producer";

	private static final String PDF_KEYWORDS = "Keywords";

	private static final String PDFA_PART = "part";

	private static final String PDFA_CONFORMANCE = "conformance";

	private static final String PDFA_PART_1 = "1";

	private static final String PDFA_CONFORMANCE_A = "A";

	private static final String PDFA_CONFORMANCE_B = "B";

	private static final String XMP_CREATE_DATE = "CreateDate";

	private static final String XMP_MODIFY_DATE = "ModifyDate";

	private static final String XMP_CREATOR_TOOL = "CreatorTool";
	
	private final PdfDocumentInfo info;
	private final PdfaConformanceEnum pdfaConformance;
	private PdfWriter pdfWriter;
	
	XmpWriter(PdfDocument document, PdfaConformanceEnum pdfaConformance)
	{
		this.info = document.getDocumentInfo();
		this.pdfaConformance = pdfaConformance;
		this.pdfWriter = document.getWriter();
	}
	
	byte[] createXmpMetadata()
	{
		try
		{
			XMPMeta xmp = XMPMetaFactory.create();
			xmp.setObjectName("");

			xmp.setProperty(XMPConst.NS_DC, PdfConst.Format, FORMAT_PDF);
			xmp.setProperty(XMPConst.NS_PDF, PDF_PRODUCER, Version.getInstance().getVersion());

			if (pdfaConformance == PdfaConformanceEnum.PDFA_1A)
			{
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_PART, PDFA_PART_1);
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_CONFORMANCE, PDFA_CONFORMANCE_A);
			}
			else if (pdfaConformance == PdfaConformanceEnum.PDFA_1B)
			{
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_PART, PDFA_PART_1);
				xmp.setProperty(XMPConst.NS_PDFA_ID, PDFA_CONFORMANCE, PDFA_CONFORMANCE_B);
			}

			xmp.setProperty(XMPConst.NS_XMP, XMP_CREATE_DATE, (info.getMoreInfo(PdfName.CreationDate.getValue())));
			xmp.setProperty(XMPConst.NS_XMP, XMP_MODIFY_DATE, (info.getMoreInfo(PdfName.ModDate.getValue())));

			String title = extractInfo(PdfName.Title);
			if (title != null)
			{
				xmp.setLocalizedText(XMPConst.NS_DC, PdfConst.Title, 
						//FIXME use the tag language?
						XMPConst.X_DEFAULT, XMPConst.X_DEFAULT, title);
			}

			String author = extractInfo(PdfName.Author);
			if (author != null)
			{
				//FIXME cache the options?
				PropertyOptions arrayOrdered = new PropertyOptions().setArrayOrdered(true);
				xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Creator, arrayOrdered, author, null);
			}

			String subject = extractInfo(PdfName.Subject);
			if (subject != null)
			{
				PropertyOptions array = new PropertyOptions().setArray(true);
				xmp.appendArrayItem(XMPConst.NS_DC, PdfConst.Subject, array, subject, null);
				xmp.setLocalizedText(XMPConst.NS_DC, PdfConst.Description, 
						XMPConst.X_DEFAULT, XMPConst.X_DEFAULT, subject);
			}

			String keywords = extractInfo(PdfName.Keywords);
			if (keywords != null)
			{
				xmp.setProperty(XMPConst.NS_PDF, PDF_KEYWORDS, keywords);
			}

			String creator = extractInfo(PdfName.Creator);
			if (creator != null)
			{
				xmp.setProperty(XMPConst.NS_XMP, XMP_CREATOR_TOOL, creator);
			}

			SerializeOptions options = new SerializeOptions();
			options.setUseCanonicalFormat(true);

			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			XMPMetaFactory.serialize(xmp, out, options);
			return out.toByteArray();
		}
		catch (XMPException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	private String extractInfo(PdfName key)
	{
		String value = info.getMoreInfo(key.getValue());
		//return value == null ? null : value.toUnicodeString();
		return value;
	}
}