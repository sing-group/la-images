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
 * objects with horizontal orientation.
 * 
 * @author Hugo López-Fernández
 * 
 */
public class HorizontalElementData extends AbstractElementData {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an {@code HorizontalElementData}. All {@code LineData} must
	 * have horizontal orientation.
	 * 
	 * @param name the element name.
	 * @param lines a {@code LineData array} containing the data.
	 *
	 * @throws IllegalArgumentException if {@code lines} is empty, there
	 * 	are empty {@code LineData} objects in the array or any line does not
	 *  have horizontal orientation.
	 */
	public HorizontalElementData(String name, LineData[] lines) {
		super(name, lines);
	}
	
	@Override
	protected void checkLinesArray(LineData[] lines) {
		super.checkLinesArray(lines);
		if (!LineCoordinatesUtils.areHorizontal(lines)) {
			throw new IllegalArgumentException(
				"All lines must have have horizontal orientation");
		}
	};
	
	@Override
	public double[][] getData(double missingValue) {
		final List<Double> xAxis = this.getXAxis();
		final List<Double> yAxis = this.getYAxis();

		final double[][] data = new double[yAxis.size()][xAxis.size()];

		for (LineData line : this.getLines()) {
			final int startXIndex = indexOf(xAxis, line.getCoordinates().getRangeStart(), DELTA);
			final int endXIndex = startXIndex + line.getLength();
			final int yIndex = indexOf(yAxis, line.getCoordinates().getPosition(), DELTA);
			
			for (int column = 0; column < xAxis.size(); column++) {
				if (column >= startXIndex && column < endXIndex) {
					data[yIndex][column] = line.getData()[column - startXIndex];
				} else {
					data[yIndex][column] = missingValue;
				}
			}
		}
		
		return data;
	}
	
	@Override
	public List<Double> getXAxis() {
		return getLinesRangeAxis(getAllCoordinates());
	}

	@Override
	public List<Double> getYAxis() {
		return getLinesPositionsAxis(getAllCoordinates());
	}
	
	protected void writeLines(StringBuilder sb, CSVFormat format) {
		final List<Double> xAxis = this.getXAxis();

		final DecimalFormat formatter = format.getDecimalFormatter();
		for (LineData line : this.getLines()) {
			final double y = line.getCoordinates().getPosition();
			sb.append(formatter.format(y))
				.append(format.getColumnSeparator());
			
			final int startXIndex = indexOf(xAxis, line.getCoordinates().getRangeStart(), DELTA);
			final int endXIndex = startXIndex + line.getLength();
			
			for (int column = 0; column < xAxis.size(); column++) {
				if (column >= startXIndex && column < endXIndex) {
					sb.append(formatter.format(
						line.getData()[column - startXIndex]
					));
				}
				if (column < xAxis.size() - 1) {
					sb.append(format.getColumnSeparator());
				}
			}
			
			sb.append(format.getLineBreak());
		}
	}
}
