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
package com.jaspersoft.jasperreports.export.pdf.modern;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.io.source.ByteUtils;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNameTree;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.colorspace.PdfCieBasedCs;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.PdfDocumentWriter;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfWriter implements PdfDocumentWriter
{
	
	private ModernPdfProducer pdfProducer;
	private OutputStream output;
	
	private PdfWriter pdfWriter;
	private WriterProperties writerProperties;

	private PdfVersion pdfVersion;

	private PdfPrintScalingEnum printScaling;
	private PdfaConformanceEnum pdfaConformance;

	private String language;
	private boolean displayMetadataTitle;
	private boolean noSpaceCharRation;

	public ModernPdfWriter(ModernPdfProducer pdfProducer, OutputStream output)
	{
		this.pdfProducer = pdfProducer;
		this.output = output;
		this.writerProperties = new WriterProperties();
	}

	public PdfWriter getPdfWriter()
	{
		return pdfWriter;
	}
	
	@Override
	public void setPdfVersion(PdfVersionEnum pdfVersion)
	{
		setPdfVersion(toPdfVersion(pdfVersion));
	}
	
	protected void setPdfVersion(PdfVersion pdfVersion)
	{
		this.pdfVersion = pdfVersion;
		writerProperties.setPdfVersion(pdfVersion);
	}
	
	protected static PdfVersion toPdfVersion(PdfVersionEnum pdfVersion)
	{
		PdfVersion version;
		switch (pdfVersion)
		{
		case VERSION_1_2:
			version = PdfVersion.PDF_1_2;
			break;
		case VERSION_1_3:
			version = PdfVersion.PDF_1_3;
			break;
		case VERSION_1_4:
			version = PdfVersion.PDF_1_4;
			break;
		case VERSION_1_5:
			version = PdfVersion.PDF_1_5;
			break;
		case VERSION_1_6:
			version = PdfVersion.PDF_1_6;
			break;
		case VERSION_1_7:
			version = PdfVersion.PDF_1_7;
			break;
		default:
			throw new JRRuntimeException("Unknown PDF version " + pdfVersion);
		}
		return version;
	}

	@Override
	public void setMinimalPdfVersion(PdfVersionEnum minimalVersion)
	{
		PdfVersion version = toPdfVersion(minimalVersion);
		if (version.compareTo(this.pdfVersion) > 0)
		{
			setPdfVersion(version);
		}
	}

	@Override
	public void setFullCompression()
	{
		writerProperties.setFullCompressionMode(true);
	}

	@Override
	public void setEncryption(String userPassword, String ownerPassword, 
			int permissions, boolean is128BitKey) throws JRException
	{
		writerProperties.setStandardEncryption(
				ByteUtils.getIsoBytes(userPassword),
				ByteUtils.getIsoBytes(ownerPassword),
				permissions,
				is128BitKey ? EncryptionConstants.STANDARD_ENCRYPTION_128 : EncryptionConstants.STANDARD_ENCRYPTION_40
				);
	}

	@Override
	public void setPrintScaling(PdfPrintScalingEnum printScaling)
	{
		this.printScaling = printScaling;
	}
	
	public PdfPrintScalingEnum getPrintScaling()
	{
		return printScaling;
	}

	@Override
	public void setNoSpaceCharRatio()
	{
		this.noSpaceCharRation = true;
	}
	
	public boolean isNoSpaceCharRatio()
	{
		return noSpaceCharRation;
	}

	@Override
	public void setTabOrderStructure()
	{
		//NOOP, set by default to PdfName.S
	}

	@Override
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	public String getLanguage()
	{
		return language;
	}

	@Override
	public void setPdfaConformance(PdfaConformanceEnum pdfaConformance)
	{
		this.pdfaConformance = pdfaConformance;
	}
	
	public PdfaConformanceEnum getPdfaConformance()
	{
		return pdfaConformance;
	}

	@Override
	public void createXmpMetadata(String title, String subject, String keywords)
	{
		writerProperties.addXmpMetadata();
	}

	@Override
	public void setRgbTransparencyBlending(boolean rgbTransparencyBlending)
	{
		//TODO lucian
		//pdfWriter.setRgbTransparencyBlending(rgbTransparencyBlending);
	}

	@Override
	public void setIccProfilePath(String iccProfilePath, InputStream iccIs) throws IOException
	{
		PdfDictionary pdfDictionary = new PdfDictionary();
		pdfDictionary.put(PdfName.Type, PdfName.OutputIntent);
		pdfDictionary.put(PdfName.OutputConditionIdentifier, new PdfString("sRGB IEC61966-2.1"));
		pdfDictionary.put(PdfName.Info, new PdfString("sRGB IEC61966-2.1"));
		pdfDictionary.put(PdfName.S, PdfName.GTS_PDFA1);
		PdfStream pdfICCBased = PdfCieBasedCs.IccBased.getIccProfileStream(iccIs);
		pdfICCBased.remove(PdfName.Alternate);
		pdfDictionary.put(PdfName.DestOutputProfile, pdfICCBased);

		PdfDocument pdfDocument = pdfProducer.getDocument().getPdfDocument();
		pdfDocument.getCatalog().put(PdfName.OutputIntents, new PdfArray(pdfDictionary));
	}

	@Override
	public void addJavaScript(String pdfJavaScript)
	{
		PdfDocument pdfDocument = pdfProducer.getDocument().getPdfDocument();
		PdfNameTree nameTree = pdfDocument.getCatalog().getNameTree(PdfName.JavaScript);
		PdfAction action = PdfAction.createJavaScript(pdfJavaScript);
		nameTree.addEntry("JRJavaScriptAction", action.getPdfObject());
	}

	@Override
	public void setDisplayMetadataTitle()
	{
		this.displayMetadataTitle = true;
	}
	
	public boolean isDisplayMetadataTitle()
	{
		return displayMetadataTitle;
	}
	
	public void open()
	{
		pdfWriter = new PdfWriter(output, writerProperties);
		pdfWriter.setCloseStream(false);
	}
	
}
