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
package es.uvigo.ei.sing.icpms.core.operations;

import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.LineData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.HorizontalLineCoordinates;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.VerticalLineCoordinates;

/**
 * A class that helps interpolating an {@code ElementData}. This is a linear
 * interpolation that can be adjusted with the {@code interpolationLevel}
 * parameter. This parameter represents the number of points generated
 * vertically or horizontally between two existing points. 
 * 
 * <p>For example, the matrix:
 * <pre><code>
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * </code></pre>
 * when interpolated with an interpolation level of 1 becomes:
 * <pre><code>
 * <strong>1.0</strong> 1.5 <strong>2.0</strong> 2.5 <strong>3.0</strong>
 * 2.5 3.0 3.5 4.0 4.5
 * <strong>4.0</strong> 4.5 <strong>5.0</strong> 5.5 <strong>6.0</strong>
 * 5.5 6.0 6.5 7.0 7.5
 * <strong>7.0</strong> 7.5 <strong>8.0</strong> 8.5 <strong>9.0</strong>
 * </code></pre>
 * and with an interpolation level of 2 becomes:
 * <pre><code>
 * <strong>1.00</strong> 1.33 1.66 <strong>2.00</strong> 2.33 2.66 <strong>3.00</strong>
 * 2.00 2.33 2.66 3.00 3.33 3.66 4.00
 * 3.00 3.33 3.66 4.00 4.33 4.66 5.00
 * <strong>4.00</strong> 4.33 4.66 <strong>5.00</strong> 5.33 5.66 <strong>6.00</strong>
 * 5.00 5.33 5.66 6.00 6.33 6.66 7.00
 * 6.00 6.33 6.66 7.00 7.33 7.66 8.00
 * <strong>7.00</strong> 7.33 7.66 <strong>8.00</strong> 8.33 8.66 <strong>9.00</strong>
 * </code></pre>
 * <p>
 * 
 * @author hlfernandez
 * @author mrjato
 */
public class Interpolator {
	private static final IntBinaryOperator CALCULATE_SIZE = (actual, level) ->
		actual + (actual - 1) * level;
	
	/**
	 * Interpolates an {@code ElementData} parameterized by {@code times}.
	 * 
	 * @param data the {@code ElementData} to interpolate.
	 * @param interpolationLevel the interpolation level. I.e the number of
	 * points generated vertically or horizontally between two existing points.
	 * @return the interpolated {@code ElementData}.
	 */
	public static ElementData interpolate(
		ElementData data, int interpolationLevel
	) {
		if (interpolationLevel == 0) {
			return data;
		} else {
			final double[][] newValues = 
				calculateNewValues(data.getData(0), interpolationLevel);
			
			final LineData[] newLines = calculateNewLines(data, interpolationLevel, newValues);
			
			return ElementData.createElementData(data.getName(), newLines);
		}
	}
	
	private static double[][] calculateNewValues(
		double[][] data, int interpolationLevel
	) {
		final int initialRows = data.length;
		final int initialColumns = data[0].length;
		
		final BivariateGridInterpolator interpolator = new BilinearInterpolator();
		final BivariateFunction function = interpolator.interpolate(
			IntStream.range(0, initialRows).asDoubleStream().toArray(),
			IntStream.range(0, initialColumns).asDoubleStream().toArray(),
			data
		);
		
		final int newNumRows = CALCULATE_SIZE.applyAsInt(
			initialRows, interpolationLevel);
		final int newNumColumns = CALCULATE_SIZE.applyAsInt(
			initialColumns, interpolationLevel);
		
		final double[][] newValues = new double[newNumRows][newNumColumns];
		
		final double xFactor = ((double) initialRows - 1d) / ((double) newNumRows - 1d);
		final double yFactor = ((double) initialColumns - 1d) / ((double) newNumColumns - 1d);
		for (int i = 0; i < newValues.length; i++) {
			for (int j = 0; j < newValues[i].length; j++) {
				newValues[i][j] = function.value(
					(double) i * xFactor, (double) j * yFactor
				);
			}
		}
		
		return newValues;
	}
	
	private static LineData[] calculateNewLines(
		ElementData data,
		int interpolationLevel,
		double[][] newValues
	) {
		final boolean isVertical = data.getCoordinates()[0].isVertical();
		int newLineCount = isVertical ? newValues[0].length : newValues.length;
		final LineData[] newLines = new LineData[newLineCount];
		
		final List<Double> xAxis = data.getXAxis();
		final List<Double> yAxis = data.getYAxis();
		final double minX = xAxis.stream().mapToDouble(Double::valueOf).min().orElseThrow(IllegalStateException::new);
		final double maxX = xAxis.stream().mapToDouble(Double::valueOf).max().orElseThrow(IllegalStateException::new);
		final double minY = yAxis.stream().mapToDouble(Double::valueOf).min().orElseThrow(IllegalStateException::new);
		final double maxY = yAxis.stream().mapToDouble(Double::valueOf).max().orElseThrow(IllegalStateException::new);
		final double tickX = (maxX - minX) / (double) (newValues[0].length - 1);
		final double tickY = (maxY - minY) / (double) (newValues.length - 1);
		
		for (int i = 0; i < newLineCount; i++) {
			final double[] lineValues = getLineValues(newValues, i, isVertical);
			
			final int minNumIndex = firstValueIndex(lineValues);
			final int maxNumIndex = lastValueIndex(lineValues);
			
			final double[] nonNanLineValues = new double[maxNumIndex - minNumIndex + 1];
			System.arraycopy(lineValues, minNumIndex, nonNanLineValues, 0, nonNanLineValues.length);
			
			newLines[i] = new LineData("Line " + (i + 1), nonNanLineValues, isVertical ?
				new VerticalLineCoordinates(
					tickY,
					minY + ((double) minNumIndex * tickY),
					minY + ((double) maxNumIndex * tickY),
					tickX * ((double) i) + minX
				) :
				new HorizontalLineCoordinates(
					tickX,
					minX + ((double) minNumIndex * tickX),
					minX + ((double) maxNumIndex * tickX),
					tickY * ((double) i) + minY
				)
			);
		}
		
		return newLines;
	}

	private static double[] getLineValues(double[][] values, int index, boolean vertical) {
		if (vertical) {
			final double[] lineValues = new double[values.length];
			
			for (int i = 0; i < lineValues.length; i++) {
				lineValues[i] = values[i][index];
			}
			
			return lineValues;
		} else {
			return values[index];
		}
	}
	
	private final static int firstValueIndex(double[] values) {
		for (int i = 0; i < values.length; i++) {
			if (!Double.isNaN(values[i]))
				return i;
		}
		
		throw new IllegalArgumentException("No value found");
	}
	
	private final static int lastValueIndex(double[] values) {
		for (int i = values.length - 1; i >= 0; i--) {
			if (!Double.isNaN(values[i]))
				return i;
		}
		
		throw new IllegalArgumentException("No value found");
	}
	
	
}
