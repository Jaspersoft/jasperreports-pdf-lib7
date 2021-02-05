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

import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.AbstractElement;

import net.sf.jasperreports.export.pdf.PdfChunk;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernChunk implements PdfChunk
{

	private ModernPdfProducer pdfProducer;
	protected AbstractElement<?> element;

	public ModernChunk(ModernPdfProducer pdfProducer, AbstractElement<?> element)
	{
		this.pdfProducer = pdfProducer;
		this.element = element;
	}

	public AbstractElement<?> getElement()
	{
		return element;
	}
	
	@Override
	public void setLocalDestination(String anchorName)
	{
		//TODO lucian
		//chunk.setLocalDestination(anchorName);
	}

	@Override
	public void setJavaScriptAction(String script)
	{
		element.setAction(PdfAction.createJavaScript(script));
	}

	@Override
	public void setAnchor(String reference)
	{
		element.setAction(PdfAction.createURI(reference));
	}

	@Override
	public void setLocalGoto(String anchor)
	{
		element.setAction(PdfAction.createGoTo(anchor));
	}

	@Override
	public void setRemoteGoto(String reference, String anchor)
	{
		element.setAction(PdfAction.createGoToR(reference, anchor));
	}

	@Override
	public void setRemoteGoto(String reference, int page)
	{
		element.setAction(PdfAction.createGoToR(reference, page));
	}

}
