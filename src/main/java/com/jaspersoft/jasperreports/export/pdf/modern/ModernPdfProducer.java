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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AbstractElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.splitting.ISplitCharacters;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.PrintPageFormat;
import net.sf.jasperreports.engine.export.AbstractPdfTextRenderer;
import net.sf.jasperreports.engine.export.type.PdfFieldTypeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.NullOutputStream;
import net.sf.jasperreports.export.pdf.PdfChunk;
import net.sf.jasperreports.export.pdf.PdfContent;
import net.sf.jasperreports.export.pdf.PdfDocument;
import net.sf.jasperreports.export.pdf.PdfDocumentWriter;
import net.sf.jasperreports.export.pdf.PdfFontStyle;
import net.sf.jasperreports.export.pdf.PdfImage;
import net.sf.jasperreports.export.pdf.PdfImageTemplate;
import net.sf.jasperreports.export.pdf.PdfOutlineEntry;
import net.sf.jasperreports.export.pdf.PdfPhrase;
import net.sf.jasperreports.export.pdf.PdfProducer;
import net.sf.jasperreports.export.pdf.PdfProducerContext;
import net.sf.jasperreports.export.pdf.PdfRadioCheck;
import net.sf.jasperreports.export.pdf.PdfStructure;
import net.sf.jasperreports.export.pdf.PdfTextChunk;
import net.sf.jasperreports.export.pdf.PdfTextField;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfProducer implements PdfProducer
{
	
	private PdfProducerContext context;
	
	private ModernPdfStructure pdfStructure;
	
	private ModernDocument document;
	private ModernPdfWriter writer;

	private PdfWriter imageTesterPdfWriter;
	private Document imageTesterDocument;
	private PdfCanvas imageTesterPdfCanvas;
	
	private ISplitCharacters splitCharacter;
	
	private ModernPdfContent pdfContent;

	private boolean tagged;
	
	private PdfPage currentPage;
	private int currentPageNumber;
	
	private Map<String, PdfButtonFormField> radioGroups;

	public ModernPdfProducer(PdfProducerContext context)
	{
		this.context = context;
		this.currentPageNumber = 0;
	}

	@Override
	public PdfProducerContext getContext()
	{
		return context;
	}

	@Override
	public PdfDocument createDocument(PrintPageFormat pageFormat)
	{
		PageSize pageSize = new PageSize(pageFormat.getPageWidth(), pageFormat.getPageHeight());
		document = new ModernDocument(this, pageSize);
		return document;
	}

	public Document getDocument()
	{
		return document.getDocument();
	}
	
	@Override
	public PdfDocumentWriter createWriter(OutputStream os) throws JRException
	{
		imageTesterPdfWriter = new PdfWriter(new NullOutputStream());
		com.itextpdf.kernel.pdf.PdfDocument imageTesterPdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(imageTesterPdfWriter);
		imageTesterDocument = new Document(imageTesterPdfDocument, new PageSize(10, 10));
		PdfPage imageTesterPage = imageTesterPdfDocument.addNewPage();
		imageTesterPdfCanvas = new PdfCanvas(imageTesterPage);
		
		writer = new ModernPdfWriter(this, os);
		return writer;
	}
	
	public ModernPdfWriter getWriter()
	{
		return writer;
	}
	
	public PdfWriter getPdfWriter()
	{
		return writer.getPdfWriter();
	}

	@Override
	public void setTagged()
	{
		this.tagged = true;
	}
	
	public boolean isTagged()
	{
		return tagged;
	}

	@Override
	public PdfContent createPdfContent()
	{
		pdfContent = new ModernPdfContent(writer.getPdfWriter());
		return pdfContent;
	}

	@Override
	public PdfContent getPdfContent()
	{
		return pdfContent;
	}

	public PdfCanvas getPdfCanvas()
	{
		return pdfContent.getPdfCanvas();
	}

	@Override
	public void initReport()
	{
	}

	@Override
	public void setForceLineBreakPolicy(boolean forceLineBreakPolicy)
	{
		splitCharacter = forceLineBreakPolicy ? new BreakIteratorSplitCharacter() : null;
	}
	
	public PdfPage getCurrentPage()
	{
		return currentPage;
	}
	
	public int getCurrentPageNumber()
	{
		return currentPageNumber;
	}
	
	@Override
	public void newPage()
	{
		currentPage = document.getDocument().getPdfDocument().addNewPage();
		PdfCanvas pdfCanvas = new PdfCanvas(currentPage);
		pdfContent.setPdfCanvas(pdfCanvas);
		++currentPageNumber;
	}
	
	@Override
	public void setPageSize(PrintPageFormat pageFormat, int pageWidth, int pageHeight)
	{
		PageSize pageSize;
		switch (pageFormat.getOrientation())
		{
		case LANDSCAPE:
			// using rotate to indicate landscape page
			pageSize = new PageSize(pageHeight, pageWidth).rotate();
			break;
		default:
			pageSize = new PageSize(pageWidth, pageHeight);
			break;
		}
		document.getDocument().getPdfDocument().setDefaultPageSize(pageSize);		
	}

	@Override
	public void endPage()
	{
		if (radioGroups != null)
		{
			for (PdfButtonFormField radioGroup : radioGroups.values())
			{
				addFormField(radioGroup);
			}
			radioGroups = null;
			// radio groups that overflow unto next page don't seem to work; reset everything as it does not make sense to keep them
		}
	}

	@Override
	public void close()
	{
		document.getDocument().close();
		imageTesterDocument.close();
	}

	@Override
	public AbstractPdfTextRenderer getCustomTextRenderer(
			JRPrintText text, JRStyledText styledText, Locale textLocale,
			boolean awtIgnoreMissingFont, boolean defaultIndentFirstLine, boolean defaultJustifyLastLine)
	{
		return null;
	}

	@Override
	public PdfImage createImage(byte[] data, boolean verify) throws IOException, JRException
	{
		ImageData imageData = ImageDataFactory.create(data);
		Image image = new Image(imageData);
		
		if (verify)
		{
			imageTesterPdfCanvas.addImageWithTransformationMatrix(imageData, 10, 0, 0, 10, 0, 0);
		}
		
		return new ModernPdfImage(image);
	}
	
	@Override
	public PdfImage createImage(BufferedImage bi, int angle) throws IOException
	{
		ImageData imageData = ImageDataFactory.create(bi, null);
		Image image = new Image(imageData);
		
		ModernPdfImage pdfImage = new ModernPdfImage(image);
		pdfImage.setRotationDegrees(angle);
		return pdfImage;
	}
	
	public PdfFontAttributes getFont(Map<Attribute,Object> attributes, Locale locale)
	{
		ModernFontRecipient fontRecipient = new ModernFontRecipient();
		context.setFont(attributes, locale, false, fontRecipient);
		PdfFontAttributes font = fontRecipient.getFont();
		return font;
	}
	
	@Override
	public PdfTextChunk createChunk(String text, Map<Attribute,Object> attributes, Locale locale)
	{
		PdfFontAttributes font = getFont(attributes, locale);
		
		Text textElement = new Text(text);
		textElement.setFont(font.getPdfFont());
		textElement.setFontSize(font.getSize());
		if (font.getForecolor() != null)
		{
			textElement.setFontColor(ModernPdfUtils.toPdfColor(font.getForecolor()));
		}
		
		PdfFontStyle style = font.getStyle();
		if (style.isBold())
		{
			textElement.setBold();
		}
		if (style.isItalic())
		{
			textElement.setItalic();
		}
		//TODO lucian underline?
		
		if (splitCharacter != null)
		{
			//TODO use line break offsets if available?
			//TODO lucian
			//chunk.setSplitCharacter(splitCharacter);
		}
		
		return new ModernTextChunk(this, textElement, font);
	}

	@Override
	public PdfChunk createChunk(PdfImage imageContainer)
	{
		Image image = ((ModernPdfImage) imageContainer).getImage();
		return new ModernChunk(this, image);
	}
	
	@Override
	public PdfPhrase createPhrase()
	{
		Paragraph paragraph = new Paragraph();
		return new ModernPhrase(this, paragraph);
	}

	@Override
	public PdfPhrase createPhrase(PdfChunk chunk)
	{
		Paragraph paragraph = new Paragraph();
		ModernPhrase modernPhrase = new ModernPhrase(this, paragraph);
		modernPhrase.add(chunk);
		return modernPhrase;
	}
	
	@Override
	public PdfImageTemplate createImageTemplate(float templateWidth, float templateHeight)
	{
		PdfTemplate template = pdfContent.getPdfContentByte().createTemplate(templateWidth, templateHeight);
		return new ModernImageTemplate(this, template);
	}

	@Override
	public PdfTextField createTextField(float llx, float lly, float urx, float ury, String fieldName)
	{
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument = document.getDocument().getPdfDocument();
		Rectangle rect = new Rectangle(llx, lly, urx - llx, ury - lly);
		PdfFormField formField = PdfFormField.createText(pdfDocument, rect, fieldName);
		return new ModernPdfTextField(this, formField);
	}

	@Override
	public PdfTextField createComboField(float llx, float lly, float urx, float ury, String fieldName,
			String value, String[] options)
	{
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument = document.getDocument().getPdfDocument();
		Rectangle rect = new Rectangle(llx, lly, urx - llx, ury - lly);
		PdfFormField formField = PdfFormField.createComboBox(pdfDocument, rect, fieldName, value, options);
		return new ModernPdfTextField(this, formField);
	}

	@Override
	public PdfTextField createListField(float llx, float lly, float urx, float ury, String fieldName,
			String value, String[] options)
	{
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument = document.getDocument().getPdfDocument();
		Rectangle rect = new Rectangle(llx, lly, urx - llx, ury - lly);
		PdfFormField formField = PdfFormField.createList(pdfDocument, rect, fieldName, value, options);
		return new ModernPdfTextField(this, formField);
	}

	protected void addFormField(PdfFormField field)
	{
		PdfAcroForm form = PdfAcroForm.getAcroForm(document.getDocument().getPdfDocument(), true);
		form.addField(field);
	}

	@Override
	public PdfRadioCheck createCheckField(float llx, float lly, float urx, float ury, String fieldName, String onValue)
	{
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument = document.getDocument().getPdfDocument();
		Rectangle rectangle = new Rectangle(llx, lly, urx - llx, ury - lly);
		PdfButtonFormField checkBox = PdfFormField.createCheckBox(pdfDocument, rectangle, fieldName, onValue);
		return new ModernRadioCheck(this, checkBox);
	}

	@Override
	public PdfRadioCheck getRadioField(float llx, float lly, float urx, float ury, String fieldName, String onValue)
	{
		PdfButtonFormField radioGroup = getRadioGroup(fieldName, onValue);
		
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument = document.getDocument().getPdfDocument();		
		Rectangle rectangle = new Rectangle(llx, lly, urx - llx, ury - lly);
		PdfFormField checkBox = PdfFormField.createRadioButton(pdfDocument, rectangle, radioGroup, onValue);
		return new ModernRadioCheck(this, checkBox);
	}
	
	protected PdfButtonFormField getRadioGroup(String fieldName, String value)
	{
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument = document.getDocument().getPdfDocument();		
		PdfButtonFormField radioGroup = radioGroups == null ? null : radioGroups.get(fieldName);
		if (radioGroup == null)
		{
			if (radioGroups == null)
			{
				radioGroups = new HashMap<>();
			}
			
			radioGroup = PdfFormField.createRadioGroup(pdfDocument, fieldName, value);
			radioGroup.setToggleOff(false);
			
			radioGroups.put(fieldName, radioGroup);
		}
		return radioGroup;
	}

	@Override
	public PdfOutlineEntry getRootOutline()
	{
		PdfOutline rootOutline = document.getDocument().getPdfDocument().getOutlines(false);
		return new ModernPdfOutline(this, rootOutline);
	}

	@Override
	public PdfStructure getPdfStructure()
	{
		if (pdfStructure == null)
		{
			pdfStructure = new ModernPdfStructure(this);
		}
		return pdfStructure;
	}
	
}
