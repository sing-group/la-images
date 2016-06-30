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
package es.uvigo.ei.sing.icpms.core.jzy3d;

/**
 * This class extends {@code org.jzy3d.maths.Range} to implement Range equality
 * method.
 * 
 * @author hlfernandez
 *
 */
public class Range extends org.jzy3d.maths.Range {

	public Range(float min, float max) {
		super(min, max);
	}

	@Override
	public boolean equals(Object obj) {
		Range toCompare = (Range) obj;
		return this.getMax() == toCompare.getMax()
				&& this.getMin() == toCompare.getMin();
	}
}
