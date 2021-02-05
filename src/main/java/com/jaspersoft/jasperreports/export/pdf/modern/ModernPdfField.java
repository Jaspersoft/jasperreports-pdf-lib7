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

import java.awt.Color;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.export.type.PdfFieldBorderStyleEnum;
import net.sf.jasperreports.export.pdf.PdfField;
import net.sf.jasperreports.export.pdf.PdfTextAlignment;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public abstract class ModernPdfField implements PdfField
{

	protected ModernPdfProducer pdfProducer;
	protected PdfFormField field;
	
	public ModernPdfField(ModernPdfProducer pdfProducer, PdfFormField field)
	{
		this.pdfProducer = pdfProducer;
		this.field = field;
	}

	@Override
	public void setBorderWidth(float borderWidth)
	{
		//TODO lucian float width = borderWidth > BaseField.BORDER_WIDTH_THICK ? BaseField.BORDER_WIDTH_THICK : borderWidth;
		field.setBorderWidth(borderWidth);
	}

	@Override
	public void setBackgroundColor(Color backcolor)
	{
		field.setBackgroundColor(ModernPdfUtils.toPdfColor(backcolor));
	}

	@Override
	public void setTextColor(Color forecolor)
	{
		field.setColor(ModernPdfUtils.toPdfColor(forecolor));
	}

	@Override
	public void setAlignment(PdfTextAlignment alignment)
	{
		field.setJustification(toPdfJustification(alignment));
	}
	
	protected int toPdfJustification(PdfTextAlignment alignment)
	{
		int justification;
		switch (alignment)
		{
		case CENTER:
			justification = PdfFormField.ALIGN_CENTER;
			break;
		case LEFT:
			justification = PdfFormField.ALIGN_LEFT;
			break;
		case RIGHT:
			justification = PdfFormField.ALIGN_RIGHT;
			break;
		default:
			throw new JRRuntimeException("Unsupported text field alignment " + alignment);
		}
		return justification;
	}

	@Override
	public void setBorderColor(Color lineColor)
	{
		field.setBorderColor(ModernPdfUtils.toPdfColor(lineColor));
	}

	@Override
	public void setBorderStyle(PdfFieldBorderStyleEnum borderStyle)
	{
		field.getWidgets().get(0).setBorderStyle(toBorderStyle(borderStyle));
		field.regenerateField();
	}
	
	protected PdfName toBorderStyle(PdfFieldBorderStyleEnum borderStyle)
	{
		PdfName pdfStyle;
		switch (borderStyle)
		{
		case SOLID:
			pdfStyle = PdfAnnotation.STYLE_SOLID;
			break;
		case DASHED:
			pdfStyle = PdfAnnotation.STYLE_DASHED;
			break;
		case BEVELED:
			pdfStyle = PdfAnnotation.STYLE_BEVELED;
			break;
		case INSET:
			pdfStyle = PdfAnnotation.STYLE_INSET;
			break;
		case UNDERLINE:
			pdfStyle = PdfAnnotation.STYLE_UNDERLINE;
			break;
		default:
			throw new JRRuntimeException("Unknoen field border style " + borderStyle);
		}
		return pdfStyle;
	}

	@Override
	public void setReadOnly()
	{
		field.setReadOnly(true);
	}

	@Override
	public void setText(String value)
	{
		field.setValue(value);
	}

	@Override
	public void setFont(Map<Attribute, Object> attributes, Locale locale)
	{
		PdfFontAttributes font = pdfProducer.getFont(attributes, locale);
		field.setFont(font.getPdfFont());
		field.setFontSize(font.getSize());
	}
	
	@Override
	public void setFontSize(float fontsize)
	{
		field.setFontSize(fontsize);
	}

	@Override
	public void setRotation(int rotation)
	{
		field.setRotation(rotation);
	}

	@Override
	public void setVisible()
	{
		field.setVisibility(PdfFormField.VISIBLE);
	}

}
