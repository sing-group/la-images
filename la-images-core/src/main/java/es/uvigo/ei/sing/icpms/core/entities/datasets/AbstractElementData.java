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
package es.uvigo.ei.sing.icpms.core.entities.datasets;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.LineCoordinates;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.LineCoordinatesUtils;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat;

/**
 * An abstract implementation of {@code ElementData}.
 * 
 * @author hlfernandez
 *
 */
public abstract class AbstractElementData implements ElementData, Serializable {
	private static final long serialVersionUID = 1L;
	
	protected static final double DELTA = 0.000001d;
	protected final LineData[] lines;
	protected String name;
	
	private double maxValue = Double.MIN_VALUE;

	/**
	 * Constructs an {@code AbstractElementData}.
	 * 
	 * @param name the element name.
	 * @param lines a {@code LineData array} containing the data.
	 * 
	 * @throws IllegalArgumentException if {@code lines} is empty or there
	 * 	are empty {@code LineData} objects in the array. 
	 */
	public AbstractElementData(String name, LineData[] lines) {
		checkLinesArray(lines);
		
		this.name = name;
		this.lines = lines;
	}
	
	protected void checkLinesArray(LineData[] lines) {
		if (lines.length == 0 || thereAreEmtyLines(lines)) {
			throw new IllegalArgumentException("Data can't be empty");
		} else if(!LineCoordinatesUtils.haveDifferentPositions(lines)) {
			throw new IllegalArgumentException(
				"Each LineData must have a different position");
		}
	}
	
	private static boolean thereAreEmtyLines(LineData[] lineData) {
		return Stream.of(lineData).map(LineData::isEmpty)
			.anyMatch(empty -> empty == true);
	}

	@Override
	public String getName() {
		return name;
	}
	
	protected List<LineCoordinates> getAllCoordinates() {
		return Stream.of(this.lines).map(LineData::getCoordinates)
			.collect(Collectors.toList());
	}

	@Override
	public int getNumLines() {
		return lines.length;
	}
	
	@Override
	public boolean isCompatibleWith(ElementData data) {
		return this.getNumLines() == data.getNumLines() && areAllDataLinesCompatibleWith(data);
	}

	private boolean areAllDataLinesCompatibleWith(ElementData data) {
		for (int i = 0; i < this.lines.length; i++) {
			boolean differentLength = 
				this.lines[i].getLength() != data.getLines()[i].getLength();
			boolean differentOrientation = 
				this.lines[i].isVertical() != data.getLines()[i].isVertical();
			if (differentLength || differentOrientation) {
				return false;
			}
		}
		return true;
	}

	@Override
	public double getMaxValue() {
		if(this.maxValue == Double.MIN_VALUE) {
			this.maxValue = Arrays.stream(this.getLines())
				.map(LineData::getData)
				.flatMapToDouble(DoubleStream::of)
			.max()
			.orElseThrow(() -> new IllegalStateException("No values found"));
		}
		
		return this.maxValue;
	}
	
	@Override
	public double getMinValue() {
		return Stream.of(getData(0.0d))	
				.flatMapToDouble(DoubleStream::of).min().getAsDouble();
	}
	
	@Override
	public LineData[] getLines() {
		return this.lines;
	}
	
	@Override
	public LineCoordinates[] getCoordinates() {
		return Stream.of(getLines()).map(LineData::getCoordinates)
				.toArray(LineCoordinates[]::new);
	}
	
	@Override
	public boolean isVertical() {
		return this.getLines()[0].isVertical();
	}
	
	@Override
	public void toCSV(File file, CSVFormat format) throws IOException {
		final StringBuilder sb = new StringBuilder();
		writeXAxis(sb, format);
		writeLines(sb, format);
		Files.write(file.toPath(), sb.toString().getBytes());
	}
	
	private final void writeXAxis(StringBuilder sb, CSVFormat format) {
		List<Double> xAxis = this.getXAxis();
		sb.append(format.getColumnSeparator());
		for (int column = 0; column < xAxis.size(); column++) {
			sb.append(format.getDecimalFormatter().format(xAxis.get(column)));
			if(column < xAxis.size()-1) {
				sb.append(format.getColumnSeparator());
			}
		}
		sb.append(format.getLineBreak());
	}

	protected static int indexOf(Collection<Double> collection, double value, 
		double delta
	) {
		int i = 0;
		for (double v : collection) {
			if (Math.abs(v - value) <= delta)
				return i;
			else i++;
		}
		
		return -1;
	}
	
	/**
	 * Write all the {@code LineData} objects into {@code sb} using the given 
	 * {@code CSVFormat}.
	 * 
	 * @param sb the {@code StringBuilder} to write the lines.
	 * @param format the {@code CSVFormat} to use.
	 */
	protected abstract void writeLines(StringBuilder sb, CSVFormat format);
}