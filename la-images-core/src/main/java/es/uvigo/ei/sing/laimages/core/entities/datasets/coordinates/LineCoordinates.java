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
package es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates;

/**
 * <p>
 * An {@code LineCoordinates} represents the coordinates of a {@code LineData}.
 * Each {@code LineData} is located on a position and its values are distributed 
 * on the line range with <i>rangeTick</i>. 
 * </p>
 * 
 * <p>
 * If {@code isVertical} is {@code false} then the position is the y point and 
 * the line range is the x axis of the line. If {@code isVertical} is 
 * {@code true} then the position is the x point and the line range is
 * the y axis of the line. 
 * </p>
 * 
 * @author Hugo López-Fernández
 *
 */
public interface LineCoordinates {

	public double getRangeStart();

	public double getRangeEnd();

	public double getRangeTick();

	public double getPosition();

	public void setStartRangeCoordinate(double xStart);

	public void setEndRangeCoordinate(double xEnd);
	
	public void setPosition(double position);
	
	public boolean isVertical();
}