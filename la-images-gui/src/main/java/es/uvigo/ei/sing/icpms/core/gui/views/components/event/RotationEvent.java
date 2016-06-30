/*
 * #%L
 * LA-iMageS GUI
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
package es.uvigo.ei.sing.icpms.core.gui.views.components.event;

import org.jzy3d.maths.Coord2d;

/**
 * An event which indicates that a rotation event occurred in a component.
 * 
 * @author hlfernandez
 *
 */
public class RotationEvent {

	private Coord2d angle;

	/**
	 * Constructs a {@code RotationEvent} object with the specified 
	 * {@code angle}.
	 * 
	 * @param angle a {@code Coord2d} object indicating the rotation angle.
	 */
	public RotationEvent(Coord2d angle) {
		this.angle = angle;
	}
	
	/**
	 * Returns a {@code Coord2d} object indicating the rotation angle.
	 * 
	 * @return a {@code Coord2d} object indicating the rotation angle.
	 */
	public Coord2d getAngle() {
		return angle;
	}
}
