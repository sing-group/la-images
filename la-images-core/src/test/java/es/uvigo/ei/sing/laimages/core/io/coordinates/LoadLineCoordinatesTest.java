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
package es.uvigo.ei.sing.laimages.core.io.coordinates;

import static es.uvigo.ei.sing.laimages.core.io.TestDatasetUtils.TEST_DATASET_COORDINATES;
import static es.uvigo.ei.sing.laimages.core.io.TestDatasetUtils.TEST_DATASET_COORDINATES_FILE;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.HorizontalLineCoordinates;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinates;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.VerticalLineCoordinates;
import es.uvigo.ei.sing.laimages.core.io.coordinates.LineCoordinatesLoader;

public class LoadLineCoordinatesTest {
	
	private static final File TEST_COORDINATES_DIRECTORY = new File(
			"src/test/resources/coordinates");
	
	private static final double X_TICK = 0.1;
	
	public static final LineCoordinates[] TEST_HORIZONTAL_COORDINATES = new LineCoordinates[] {
		new HorizontalLineCoordinates(X_TICK, 0.1, 1.1, 1),
		new HorizontalLineCoordinates(X_TICK, 0.2, 1.0, 2),
		new HorizontalLineCoordinates(X_TICK, 0.4, 1.1, 3)
	};

	public static final File TEST_HORIZONTAL_COORDINATES_FILE = new File(
			TEST_COORDINATES_DIRECTORY, "horizontal-positions.txt");

	public static final LineCoordinates[] TEST_VERTICAL_COORDINATES = new LineCoordinates[] {
		new VerticalLineCoordinates(X_TICK, 0.1, 1.1, 1),
		new VerticalLineCoordinates(X_TICK, 0.2, 1.0, 2),
		new VerticalLineCoordinates(X_TICK, 0.4, 1.1, 3)
	};
	
	public static final File TEST_VERTICAL_COORDINATES_FILE = new File(
			TEST_COORDINATES_DIRECTORY, "vertical-positions.txt");
	
	@Test
	public void loadAlignHorizontalCoordinatesTest() throws IOException {
		Assert.assertArrayEquals(
			TEST_HORIZONTAL_COORDINATES, 
			LineCoordinatesLoader.loadCoordinates(TEST_HORIZONTAL_COORDINATES_FILE, X_TICK)
		);		
	}
	
	@Test
	public void loadAlignVerticalCoordinatesTest() throws IOException {
		Assert.assertArrayEquals(
			TEST_VERTICAL_COORDINATES, 
			LineCoordinatesLoader.loadCoordinates(TEST_VERTICAL_COORDINATES_FILE, X_TICK)
		);		
	}
	
	@Test
	public void loadDatasetCoordinatesTest() throws IOException {
		Assert.assertArrayEquals(
			TEST_DATASET_COORDINATES, 
			LineCoordinatesLoader.loadCoordinates(TEST_DATASET_COORDINATES_FILE, 0.03162d)
		);		
	}
}
