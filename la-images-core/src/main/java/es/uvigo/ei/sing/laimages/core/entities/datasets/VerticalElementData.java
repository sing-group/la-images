/*
 * #%L
 * LA-iMageS Core
 * %%
 * Copyright (C) 2016 Marco Aurélio Zezzi Arruda, Gustavo de Souza
 * 			Pessôa, José Luis Capelo Martínez, Florentino Fdez-Riverola, Miguel
 * 			Reboiro-Jato, Hugo López-Fdez, and Daniel Glez-Peña
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package es.uvigo.ei.sing.laimages.core.entities.datasets;

import static es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinatesUtils.getLinesPositionsAxis;
import static es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinatesUtils.getLinesRangeAxis;

import java.text.DecimalFormat;
import java.util.List;

import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinatesUtils;
import es.uvigo.ei.sing.laimages.core.io.csv.CSVFormat;

/**
 * An implementation of {@code AbstractElementData} that stores {@code LineData}
 * objects with vertical orientation.
 * 
 * @author Hugo López-Fernández
 * 
 */
public class VerticalElementData extends AbstractElementData {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code VerticalElementData}. All {@code LineData} must
	 * have vertical orientation.
	 * 
	 * @param name the element name.
	 * @param lines a {@code LineData array} containing the data.

	 * @throws IllegalArgumentException if {@code lines} is empty, there
	 * 	are empty {@code LineData} objects in the array or any line does not
	 *  have vertical orientation.
	 */
	public VerticalElementData(String name, LineData[] lines) {
		super(name, lines);
	}
	
	@Override
	protected void checkLinesArray(LineData[] lines) {
		super.checkLinesArray(lines);
		if (!LineCoordinatesUtils.areVertical(lines)) {
			throw new IllegalArgumentException(
				"All lines must have have vertical orientation");
		}
	};
	
	@Override
	public double[][] getData(double missingValue) {
		final List<Double> xAxis = this.getXAxis();
		final List<Double> yAxis = this.getYAxis();

		final double[][] data = new double[yAxis.size()][xAxis.size()];

		for (LineData line : this.getLines()) {
			final int startYIndex = indexOf(yAxis, line.getCoordinates().getRangeStart(), 0.000001d);
			final int endYIndex = startYIndex + line.getLength();
			final int xIndex = indexOf(xAxis, line.getCoordinates().getPosition(), 0.000001d);

			for (int row = 0; row < yAxis.size(); row++) {
				if (row >= startYIndex && row < endYIndex) {
					data[row][xIndex] = line.getData()[row - startYIndex];
				} else {
					data[row][xIndex] = missingValue;
				}
			}
		}
		
		return data;
	}
	
	@Override
	public List<Double> getXAxis() {
		return getLinesPositionsAxis(getAllCoordinates());
	}

	@Override
	public List<Double> getYAxis() {
		return getLinesRangeAxis(getAllCoordinates());
	}
	
	protected void writeLines(StringBuilder sb, CSVFormat format) {
		final List<Double> xAxis = this.getXAxis();
		final List<Double> yAxis = this.getYAxis();
		
		final DecimalFormat formatter = format.getDecimalFormatter();
		for (Double y : yAxis) {
			final int yIndex = indexOf(yAxis, y, DELTA);
			
			sb.append(formatter.format(y))
				.append(format.getColumnSeparator());
			
			for (Double x : xAxis) {
				final int xIndex = indexOf(xAxis, x, DELTA);
				final LineData line = this.getLines()[xIndex];
				
				final double lineRangeStart = line.getCoordinates().getRangeStart();
				final double lineRangeEnd = line.getCoordinates().getRangeEnd();
				final int lineRangeStartIndex = indexOf(yAxis, lineRangeStart, DELTA);
				
				if (y >= lineRangeStart && y <= lineRangeEnd) {
					sb.append(formatter.format(
						line.getData()[yIndex - lineRangeStartIndex]
					));
				}
				
				if (xIndex < xAxis.size() - 1) {
					sb.append(format.getColumnSeparator());
				}
			}
			
			sb.append(format.getLineBreak());
		}
	}
}
