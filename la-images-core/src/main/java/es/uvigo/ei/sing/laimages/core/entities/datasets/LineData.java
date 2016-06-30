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

import java.io.Serializable;

import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinates;

/**
 * A {@code LineData} object stores the measurements and the coordinates
 * of one element line.
 * 
 * @author Hugo López-Fernández
 * @see LineCoordinates
 *
 */
public class LineData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double[] data;
	private LineCoordinates coordinates;

	/**
	 * Constructs a new {@code LineData}.
	 * 
	 * @param name the line name.
	 * @param data the line values.
	 * @param coordinates the {@code LineCoordinates}.
	 */
	public LineData(String name, double[] data, LineCoordinates coordinates) {
		this.name = name;
		this.data = data;
		this.coordinates = coordinates;
	}
	
	public String getName() {
		return name;
	}
	
	public double[] getData() {
		return data;
	}

	public int getLength() {
		return data.length;
	}
	
	public LineCoordinates getCoordinates() {
		return coordinates;
	}
	
	public boolean isEmpty() {
		return this.data.length == 0;
	}

	public boolean isVertical() {
		return this.coordinates.isVertical();
	}
}
