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

import static es.uvigo.ei.sing.icpms.core.TestUtils.assertElementDataEquals;

import org.junit.Test;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.LineData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.HorizontalLineCoordinates;

public class NormalizeElementDataTest {

	@Test
	public void normalizeElementDataWithStandardTest() {
		ElementData toNormalize = ElementData.createElementData(
			"To normalize", 
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{ 1d ,2d, 3d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.0d)
				),	
				new LineData(
					"Line 2", 
					new double[]{ 4d, 5d, 6d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				)	
			});
		ElementData standard = ElementData.createElementData(
			"Standard", 
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{ 2d, 4d, 6d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.0d)
				),	
				new LineData(
					"Line 2", 
					new double[]{ 0.5d, 0.5d, 0.5d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				)	
			});
		
		ElementData normalized = NormalizeElementData.normalize(standard, toNormalize);
		
		ElementData expectedNormalized = ElementData.createElementData(
			"Normalized", 
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[] { 0.5d, 0.5d, 0.5d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.0d)
				),	
				new LineData(
					"Line 2", 
					new double[] { 8d, 10d, 12d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				)	
			});		
		
		assertElementDataEquals(expectedNormalized, normalized);
	}
	
	@Test
	public void normalizeElementDataTest() {
		ElementData toNormalize = ElementData.createElementData(
			"To normalize", 
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{ 1d, 2d, 3d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.0d)
				),	
				new LineData(
					"Line 2", 
					new double[]{ 4d, 5d, 6d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				)	
			});
		
		ElementData normalized = NormalizeElementData.normalize(toNormalize);
		
		ElementData expectedNormalized = ElementData.createElementData(
			"Normalized", 
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[] { 0.16d, 0.33d, 0.5d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.0d)
				),	
				new LineData(
					"Line 2", 
					new double[] { 0.66d, 0.83d, 1.0d }, 
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				)	
			});		
		
		assertElementDataEquals(expectedNormalized, normalized);
	}
}