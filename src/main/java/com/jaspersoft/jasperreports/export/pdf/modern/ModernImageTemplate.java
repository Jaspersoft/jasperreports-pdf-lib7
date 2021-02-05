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

import java.awt.Graphics2D;
import java.io.IOException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.export.pdf.PdfImageTemplate;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernImageTemplate implements PdfImageTemplate
{

	private ModernPdfProducer pdfProducer;
	private PdfTemplate template;
	
	public ModernImageTemplate(ModernPdfProducer pdfProducer, PdfTemplate template)
	{
		this.pdfProducer = pdfProducer;
		this.template = template;
	}

	@Override
	public Graphics2D createGraphics(float templateWidth, float templateHeight)
	{
		return template.createGraphics(templateWidth, templateHeight, new LocalFontMapper());
	}

	@Override
	public Graphics2D createGraphicsShapes(float templateWidth, float templateHeight)
	{
		return template.createGraphicsShapes(templateWidth, templateHeight);
	}

	@Override
	public void add(float ratioX, float ratioY, float x, float y) throws IOException
	{
		PdfContentByte pdfContentByte = pdfProducer.getPdfContentByte();
		pdfContentByte.saveState();
		pdfContentByte.addTemplate(
			template,
			ratioX, 0f, 0f, ratioY, x, y);
		pdfContentByte.restoreState();
		
		pdfProducer.getPdfWriter().releaseTemplate(template);
	}
	
	/**
	 *
	 */
	class LocalFontMapper implements FontMapper
	{
		public LocalFontMapper()
		{
		}

		@Override
		public BaseFont awtToPdf(java.awt.Font font)
		{
			// not setting underline and strikethrough as we only need the base font.
			// underline and strikethrough will not work here because PdfGraphics2D
			// doesn't check the font attributes.
			Map<Attribute,Object> atts = new HashMap<Attribute,Object>();
			atts.putAll(font.getAttributes());
			Font pdfFont = pdfProducer.getFont(atts, null);
			return pdfFont.getBaseFont();
		}

		@Override
		public java.awt.Font pdfToAwt(BaseFont font, int size)
		{
			return null;
		}
	}

}
