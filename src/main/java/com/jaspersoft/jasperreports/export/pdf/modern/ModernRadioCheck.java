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

import com.itextpdf.forms.fields.PdfFormField;

import net.sf.jasperreports.engine.export.type.PdfFieldCheckTypeEnum;
import net.sf.jasperreports.export.pdf.PdfRadioCheck;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernRadioCheck extends ModernPdfField implements PdfRadioCheck
{
	
	public ModernRadioCheck(ModernPdfProducer pdfProducer, PdfFormField radioCheckField)
	{
		super(pdfProducer, radioCheckField);
	}
	
	@Override
	public void setCheckType(PdfFieldCheckTypeEnum checkType)
	{
		//assuming the iText 2 values are fine
		field.setCheckType(checkType.getValue());
	}

	@Override
	public void setOnValue(String value)
	{
		//TODO lucian field.setValue(value);
	}

	@Override
	public void setChecked(boolean checked)
	{
		//TODO lucian radioCheckField.setChecked(checked);
	}

	@Override
	public void add()
	{
		pdfProducer.addFormField(field);
	}

	@Override
	public void addToGroup() throws IOException
	{
		//NOOP
	}

}
