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
package es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates;

import java.io.Serializable;

/**
 * An abstract implementation of {@code LineCoordinates}.
 * 
 * @author hlfernandez
 *
 */
public abstract class AbstractLineCoordinates implements LineCoordinates,
	Serializable {
	private static final long serialVersionUID = 1L;
	
	private double rangeTick;
	private double rangeStart;
	private double rangeEnd;
	private double position;

	/**
	 * Constructs a new {@code AbstractLineCoordinates} with the specified 
	 * values.
	 * 
	 * @param rangeTick the rangeTick value.
	 * @param rangeStart the rangeStart value.
	 * @param rangeEnd the rangeEnd value.
	 * @param position the position value.
	 */
	public AbstractLineCoordinates(double rangeTick, double rangeStart,
		double rangeEnd, double position
	) {
		if (rangeTick <= 0) {
			throw new IllegalArgumentException(
				"Line range interval size must be greater than 0");
		}
		
		if (rangeStart > rangeEnd) {
			throw new IllegalArgumentException(
					"End range point must be higher than start range point");
		}

		this.rangeTick = rangeTick;
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
		this.position = position;
	}

	@Override
	public double getRangeStart() {
		return rangeStart;
	}

	@Override
	public double getRangeEnd() {
		return rangeEnd;
	}
	
	@Override
	public double getRangeTick() {
		return rangeTick;
	}

	@Override
	public double getPosition() {
		return position;
	}

	@Override
	public void setStartRangeCoordinate(double xStart) {
		this.rangeStart = xStart;
	}

	@Override
	public void setEndRangeCoordinate(double xEnd) {
		this.rangeEnd = xEnd;
	}
	
	@Override
	public void setPosition(double position) {
		this.position = position;
	};

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AbstractLineCoordinates)) {
			return false;
		}
		AbstractLineCoordinates anotherLC = (AbstractLineCoordinates) obj;
		return 
			this.getRangeTick() == anotherLC.getRangeTick() 	&&
			this.getRangeStart() == anotherLC.getRangeStart() 	&&
			this.getRangeEnd() == anotherLC.getRangeEnd() 		&&
			this.getPosition() == anotherLC.getPosition() 		&&
			this.isVertical() == anotherLC.isVertical();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb
			.append("LineCoordinates: line range = [")
			.append(this.rangeStart)
			.append(", ")
			.append(this.rangeEnd)
			.append("] by ")
			.append(rangeTick)
			.append(" ; line position = ")
			.append(this.position)
			.append(". Orientation: ")
			.append(isVertical()?"vertical":"horizontal")
			.append(".");
		return sb.toString();
	}
}
