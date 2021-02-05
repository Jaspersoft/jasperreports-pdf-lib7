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

import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;

import net.sf.jasperreports.export.pdf.PdfTextField;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfTextField extends ModernPdfField implements PdfTextField
{
	
	public ModernPdfTextField(ModernPdfProducer pdfProducer, PdfFormField textField)
	{
		super(pdfProducer, textField);
	}

	@Override
	public void setEdit()
	{
		((PdfChoiceFormField) field).setEdit(true);
	}

	@Override
	public void setMultiline()
	{
		((PdfTextFormField) field).setMultiline(true);
	}

	@Override
	public void add()
	{
		pdfProducer.addFormField(field);
	}

}
