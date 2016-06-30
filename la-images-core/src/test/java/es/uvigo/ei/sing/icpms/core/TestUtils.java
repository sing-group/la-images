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
package es.uvigo.ei.sing.icpms.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.LineData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.LineCoordinates;

public class TestUtils {

	public static final void assertMatrixEquals(double[][] expected,
			double[][] actual, double delta) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actual[i], delta);
		}
	}
	
	public static final void assertListEquals(List<Double> expected,
			List<Double> actual, double delta) {
		assertArrayEquals(toArray(expected), toArray(actual), delta);
	}
	
	private static final double[] toArray(List<Double> list) {
		return list.stream().mapToDouble(Double::valueOf).toArray();
	}
	
	public static void assertElementDataEquals(
		ElementData expected, ElementData actual
	) {
		assertEquals(expected.getNumLines(), actual.getNumLines());
		assertArrayEquals(getLineLengths(expected), getLineLengths(actual));
		assertArrayEquals(getLineNames(expected), getLineNames(actual));
		assertEquals(actual.isVertical(), actual.isVertical());
		
		for (int i = 0; i < expected.getNumLines(); i++) {
			assertArrayEquals(
				expected.getLines()[i].getData(), 
				actual.getLines()[i].getData(), 
				0.01
			);
			
			assertEqualCoordinates(
				expected.getCoordinates()[i],
				actual.getCoordinates()[i]
			);
		}
	}
	
	public static void assertEqualCoordinates(
		LineCoordinates expected, LineCoordinates actual
	) {
		assertEquals(expected.getPosition(), actual.getPosition(), 0.000001d);
		assertEquals(expected.getRangeStart(), actual.getRangeStart(), 0.000001d);
		assertEquals(expected.getRangeEnd(), actual.getRangeEnd(), 0.000001d);
		assertEquals(expected.getRangeTick(), actual.getRangeTick(), 0.000001d);
	}
	
	private static int[] getLineLengths(ElementData interpolated) {
		return Stream.of(interpolated.getLines())
			.mapToInt(LineData::getLength)
		.toArray();
	}

	private static String[] getLineNames(ElementData interpolated) {
		return Stream.of(interpolated.getLines()).map(LineData::getName)
				.toArray(String[]::new);
	}
}
