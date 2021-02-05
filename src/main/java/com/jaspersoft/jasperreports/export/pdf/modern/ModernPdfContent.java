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
import java.awt.geom.AffineTransform;

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.LineCapStyle;
import net.sf.jasperreports.export.pdf.PdfContent;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfContent implements PdfContent
{
	
	private PdfCanvas pdfCanvas;
	
	private PdfExtGState[] fillAlphaStates = new PdfExtGState[256];
	private boolean fillAlphaSet = false;

	private PdfExtGState[] strokeAlphaStates = new PdfExtGState[256];
	private boolean strokeAlphaSet = false;

	public ModernPdfContent()
	{
	}

	public PdfCanvas getPdfCanvas()
	{
		return pdfCanvas;
	}
	
	public void setPdfCanvas(PdfCanvas pdfCanvas)
	{
		this.pdfCanvas = pdfCanvas;
	}
	
	@Override
	public void setFillColor(Color color)
	{
		setFillColorAlpha(color.getAlpha());
		pdfCanvas.setFillColor(ModernPdfUtils.toPdfColor(color));
	}

	@Override
	public void setFillColorAlpha(int alpha)
	{
		if (alpha != 255)
		{
			setFillAlpha(alpha);
			fillAlphaSet = true;
		}
	}
	
	@Override
	public void resetFillColor()
	{
		if (fillAlphaSet)
		{
			setFillAlpha(255);
			fillAlphaSet = false;
		}
	}

	protected void setFillAlpha(int alpha)
	{
		PdfExtGState state = fillAlphaStates[alpha];
		if (state == null)
		{
			state = new PdfExtGState();
			state.setFillOpacity(((float) alpha)/255);
			fillAlphaStates[alpha] = state;
		}
		pdfCanvas.setExtGState(state);
	}

	@Override
	public void setStrokeColor(Color color)
	{
		int alpha = color.getAlpha();
		if (alpha != 255)
		{
			setStrokeAlpha(alpha);
			strokeAlphaSet = true;
		}
		
		pdfCanvas.setStrokeColor(ModernPdfUtils.toPdfColor(color));
	}
	
	@Override
	public void resetStrokeColor()
	{
		if (strokeAlphaSet)
		{
			setStrokeAlpha(255);
			strokeAlphaSet = false;
		}
	}

	protected void setStrokeAlpha(int alpha)
	{
		PdfExtGState state = strokeAlphaStates[alpha];
		if (state == null)
		{
			state = new PdfExtGState();
			state.setStrokeOpacity(((float) alpha)/255);
			strokeAlphaStates[alpha] = state;
		}
		pdfCanvas.setExtGState(state);
	}

	@Override
	public void setLineWidth(float lineWidth)
	{
		pdfCanvas.setLineWidth(lineWidth);
	}

	@Override
	public void setLineCap(LineCapStyle lineCap)
	{
		int lineCapValue;
		switch (lineCap)
		{
		case BUTT:
			lineCapValue = PdfCanvasConstants.LineCapStyle.BUTT;
			break;
		case ROUND:
			lineCapValue = PdfCanvasConstants.LineCapStyle.ROUND;
			break;
		case PROJECTING_SQUARE:
			lineCapValue = PdfCanvasConstants.LineCapStyle.PROJECTING_SQUARE;
			break;
		default:
			throw new JRRuntimeException("Unknown line cap style " + lineCap);
		}
		pdfCanvas.setLineCapStyle(lineCapValue);
	}

	@Override
	public void setLineDash(float phase)
	{
		pdfCanvas.setLineDash(phase);
	}

	@Override
	public void setLineDash(float unitsOn, float unitsOff, float phase)
	{
		pdfCanvas.setLineDash(unitsOn, unitsOff, phase);
	}

	@Override
	public void strokeLine(float x1, float y1, float x2, float y2)
	{
		pdfCanvas.moveTo(x1, y1);
		pdfCanvas.lineTo(x2, y2);
		pdfCanvas.stroke();
	}

	@Override
	public void fillRectangle(float x, float y, float width, float height)
	{
		pdfCanvas.rectangle(x, y, width, height);
		pdfCanvas.fill();		
	}

	@Override
	public void fillRoundRectangle(float x, float y, float width, float height, float radius)
	{
		pdfCanvas.roundRectangle(x, y, width, height, radius);
		pdfCanvas.fill();
	}

	@Override
	public void strokeRoundRectangle(float x, float y, float width, float height, float radius)
	{
		pdfCanvas.roundRectangle(x, y, width, height, radius);
		pdfCanvas.stroke();
	}

	@Override
	public void fillEllipse(float x1, float y1, float x2, float y2)
	{
		pdfCanvas.ellipse(x1, y1, x2, y2);
		pdfCanvas.fill();
	}

	@Override
	public void strokeEllipse(float x1, float y1, float x2, float y2)
	{
		pdfCanvas.ellipse(x1, y1, x2, y2);
		pdfCanvas.stroke();
	}

	@Override
	public void setLiteral(String string)
	{
		pdfCanvas.getContentStream().getOutputStream().writeString(string);
	}

	@Override
	public void transform(AffineTransform atrans)
	{
        double matrix[] = new double[6];
        atrans.getMatrix(matrix);
		pdfCanvas.concatMatrix(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
	}

}
