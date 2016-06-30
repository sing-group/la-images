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

import java.io.File;
import java.io.IOException;
import java.util.List;

import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinates;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinatesUtils;
import es.uvigo.ei.sing.laimages.core.io.csv.CSVFormat;

/**
 * An {@code ElementData} stores the data of an element in the following way:
 * <ul>
 * <li>It has several {@code LineData} elements where the data are stored.</li>
 * <li>Each {@code LineData} stores the line values and the 
 * {@code LineCoordinates}, where the line positions are specified.</li>
 * </ul>  
 *  
 * @author Hugo López-Fernández
 * @author Miguel Reboiro-Jato
 * 
 * @see LineData
 * @see LineCoordinates
 *
 */
public interface ElementData {
	/**
	 * Returns the element name.
	 * 
	 * @return the element name.
	 */
	public String getName();
	
	/**
	 * Returns the data matrix. Since lines can have different lengths, common 
	 * X and Y axis are created for all lines and missing values are 
	 * represented by {@code missingValue}.
	 * 
	 * @param missingValue the value to use for missing values.
	 * @return the data matrix.
	 */
	public double[][] getData(double missingValue);
	
	/**
	 * Return the x axis of the matrix returned by {@code getData}.
	 * 
	 * @return a list that represents the x axis.
	 */
	public List<Double> getXAxis();
	
	/**
	 * Return the y axis of the matrix returned by {@code getData}.
	 * 
	 * @return a list that represents the y axis.
	 */
	public List<Double> getYAxis();
	
	/**
	 * Return the {@code LineData} array.
	 * 
	 * @return the {@code LineData} array.
	 */
	public LineData[] getLines();
	
	/**
	 * Returns the lines count.
	 * 
	 * @return the lines count.
	 */
	public int getNumLines();
	
	/**
	 * Returns true if the {@code ElementData} is compatible with {@code data}.
	 * 
	 * @param data the {@code ElementData} to compare.
	 * @return true if the {@code ElementData} is compatible with {@code data}.
	 */
	public boolean isCompatibleWith(ElementData data);
	
	/**
	 * Returns the maximum value in the data matrix.
	 * 
	 * @return the maximum value in the data matrix.
	 */
	public double getMaxValue();
	
	/**
	 * Returns the minimum value in the data matrix.
	 * 
	 * @return the minimum value in the data matrix.
	 */
	public double getMinValue();
	
	/**
	 * Return a {@code LineCoordinates} array corresponding to each 
	 * {@code LineData} in {@code getLines()}.
	 * 
	 * @return a {@code LineCoordinates} array.
	 */
	public LineCoordinates[] getCoordinates();
	
	/**
	 * Writes the element into a CSV file.
	 * 
	 * @param file the file to write the data.
	 * @param format the {@code CSVFormat}.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toCSV(File file, CSVFormat format) throws IOException;
	
	/**
	 * Return {@code true} if the orientation is vertical and {@code false} if
	 * it is horizontal.
	 *  
	 * @return {@code true} if the orientation is vertical and {@code false} if
	 * it is horizontal.
	 */
	public boolean isVertical();
	
	/**
	 * Constructs an {@code ElementData}.
	 * 
	 * @param name the element name.
	 * @param lines a {@code LineData array} containing the data.
	 * @return a new {@code ElementData}.
	 * @throws IllegalArgumentException if all the {@code LineData} don't have
	 * the same orientation.
	 */
	public static ElementData createElementData(String name, LineData[] lines) {
		if (LineCoordinatesUtils.areVertical(lines)) {
			return new VerticalElementData(name, lines);
		} else if(LineCoordinatesUtils.areHorizontal(lines)) {
			return new HorizontalElementData(name, lines);
		} else {
			throw new IllegalArgumentException(
				"All lines must have the same orientation");
		}
	}
}
