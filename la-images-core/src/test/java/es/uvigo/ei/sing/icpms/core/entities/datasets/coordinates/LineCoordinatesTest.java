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

import static es.uvigo.ei.sing.icpms.core.TestUtils.assertListEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class LineCoordinatesTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLineCoordinatesWithInvalidXRange() {
		new HorizontalLineCoordinates(0.1d, 1d, 0d, 2d);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateLineCoordinatesWithInvalidXTick() {
		new HorizontalLineCoordinates(0.1d, 1d, 0d, 2d);
	}
	
	@Test
	public void testCreateHorizontalLineCoordinates() {
		LineCoordinates lC = new HorizontalLineCoordinates(0.1d, 0d, 1d, 2d);
		assertEquals(0d, lC.getRangeStart(), 0);
		assertEquals(1d, lC.getRangeEnd(), 0);
		assertEquals(2d, lC.getPosition(), 0);
		assertFalse(lC.isVertical());
	}
	
	@Test
	public void testCreateVerticalLineCoordinates() {
		LineCoordinates lC = new VerticalLineCoordinates(0.1d, 0d, 1d, 2d);
		assertEquals(0d, lC.getRangeStart(), 0);
		assertEquals(1d, lC.getRangeEnd(), 0);
		assertEquals(2d, lC.getPosition(), 0);
		assertTrue(lC.isVertical());
	}
	
	@Test
	public void testGetAxisFromHorizontalLineCoordinates() {
		List<Double> expectedLinesRangeAxis = Arrays.asList(new Double[]{
				0.0d, 0.1d, 0.2d, 0.3d, 0.4d});				
		List<Double> expectedLinesPositionsAxis = Arrays.asList(new Double[]{
				0.0d, 0.1d});				
		List<LineCoordinates> coordinatesList = Arrays.asList(
			new LineCoordinates[]{
				new HorizontalLineCoordinates(0.1d, 0.0d, 0.3d, 0.0d),
				new HorizontalLineCoordinates(0.1d, 0.1d, 0.4d, 0.1d)
			});
		
		List<Double> linesRangeAxis = LineCoordinatesUtils.getLinesRangeAxis(coordinatesList);
		List<Double> linesPositionsAxis = LineCoordinatesUtils.getLinesPositionsAxis(coordinatesList);
		
		assertListEquals(expectedLinesRangeAxis, linesRangeAxis, 0.1d);
		assertListEquals(expectedLinesPositionsAxis, linesPositionsAxis, 0.1d);
	}
	
	@Test
	public void testGetAxisFromVerticalLineCoordinates() {
		List<Double> expectedLinesRangeAxis = Arrays.asList(new Double[]{
				0.0d, 0.1d, 0.2d, 0.3d, 0.4d});				
		List<Double> expectedLinesPositionsAxis = Arrays.asList(new Double[]{
				0.0d, 0.1d});				
		List<LineCoordinates> coordinatesList = Arrays.asList(
			new LineCoordinates[]{
				new VerticalLineCoordinates(0.1d, 0.0d, 0.3d, 0.0d),
				new VerticalLineCoordinates(0.1d, 0.1d, 0.4d, 0.1d)
			});
		
		List<Double> linesRangeAxis = LineCoordinatesUtils.getLinesRangeAxis(coordinatesList);
		List<Double> linesPositionsAxis = LineCoordinatesUtils.getLinesPositionsAxis(coordinatesList);
		
		assertListEquals(expectedLinesRangeAxis, linesRangeAxis, 0.1d);
		assertListEquals(expectedLinesPositionsAxis, linesPositionsAxis, 0.1d);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetRangeAxisFromIncompatibleLineCoordinates() {
		List<LineCoordinates> coordinatesList = Arrays.asList(
			new LineCoordinates[]{
				new HorizontalLineCoordinates(0.05d, 0.0d, 0.3d, 0.0d),
				new HorizontalLineCoordinates(0.10d, 0.1d, 0.4d, 0.1d)
		});
		
		LineCoordinatesUtils.getLinesRangeAxis(coordinatesList);
	}

	@Test
	public void testGetAxisFromAnEmptyLineCoordinatesList() {
		List<LineCoordinates> coordinatesList = Collections.emptyList();
		assertListEquals(
			LineCoordinatesUtils.getLinesPositionsAxis(coordinatesList),
			Collections.emptyList(), 
			0d
		);
		assertListEquals(
			LineCoordinatesUtils.getLinesRangeAxis(coordinatesList),
			Collections.emptyList(), 
			0d
		);
	}
}
