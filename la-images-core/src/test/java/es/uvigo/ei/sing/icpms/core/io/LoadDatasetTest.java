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
package es.uvigo.ei.sing.icpms.core.io;

import static es.uvigo.ei.sing.icpms.core.TestUtils.assertElementDataEquals;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.NA23;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.NA23_NAME;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.TEST_DATASET_ELEMENT_NAMES;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.TEST_DATASET_INVALID_LINE_LENGTHS_DIRECTORY;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.TEST_DATASET_LINE_POSITIONS_REPEATED_DIRECTORY;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.TEST_DATASET_MISSING_POSITIONS_DIFFERENT_LINE_LENGTHS_DIRECTORY;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.TEST_DATASET_MISSING_POSITIONS_DIRECTORY;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.loadTestDataset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDatasetConfiguration;
import es.uvigo.ei.sing.icpms.core.io.exception.InvalidDataException;
import es.uvigo.ei.sing.icpms.core.io.exception.NoSuchStandardElementException;
import es.uvigo.ei.sing.icpms.core.io.exception.PositionsFileNotFoundException;
import es.uvigo.ei.sing.icpms.core.util.DefaultProgressHandler;
import es.uvigo.ei.sing.icpms.core.util.ProgressHandler;

public class LoadDatasetTest {

	@Test(expected = NoSuchStandardElementException.class)
	public void testLoadDatasetWithMissingStandardElement() throws IOException, NoSuchStandardElementException {
		ElementDatasetConfiguration datasetConfiguration = new ElementDatasetConfiguration(0.060d,	0.527d, 0.080d, "FOO");
		loadTestDataset(datasetConfiguration);
	}
	
	@Test
	public void testLoadDataset() throws IOException, NoSuchStandardElementException {
		ElementDatasetConfiguration datasetConfiguration = new ElementDatasetConfiguration(0.060d,	0.527d, 0.080d, "C12");
		ElementDataset dataset = loadTestDataset(datasetConfiguration);
		assertCorrectDataset(dataset);
	}

	@Test
	public void testLoadDatasetWithoutPositionsFile() throws IOException, NoSuchStandardElementException {
		ElementDatasetConfiguration datasetConfiguration = new ElementDatasetConfiguration(0.060d,	0.527d, 0.080d, "C12");
		LineDatasetLoader datasetLoader = new LineDatasetLoader(datasetConfiguration);
		ElementDataset dataset = datasetLoader.loadAndNormalizeDataset(TEST_DATASET_MISSING_POSITIONS_DIRECTORY.toPath());
		assertCorrectDataset(dataset);
	}
	
	@Test(expected = PositionsFileNotFoundException.class)
	public void testLoadDatasetWithoutPositionsFileAndDifferentLengthLines() throws IOException, NoSuchStandardElementException {
		ElementDatasetConfiguration datasetConfiguration = new ElementDatasetConfiguration(0.060d,	0.527d, 0.080d, "C12");
		LineDatasetLoader datasetLoader = new LineDatasetLoader(datasetConfiguration);
		datasetLoader.loadAndNormalizeDataset(TEST_DATASET_MISSING_POSITIONS_DIFFERENT_LINE_LENGTHS_DIRECTORY.toPath());
	}
	
	@Test(expected = InvalidDataException.class)
	public void testLoadDatasetWithInvalidLineLength() throws IOException, NoSuchStandardElementException {
		ElementDatasetConfiguration datasetConfiguration = new ElementDatasetConfiguration(0.060d,	0.527d, 0.080d, "C12");
		LineDatasetLoader datasetLoader = new LineDatasetLoader(datasetConfiguration);
		datasetLoader.loadAndNormalizeDataset(TEST_DATASET_INVALID_LINE_LENGTHS_DIRECTORY.toPath());
	}
	
	@Test
	public void testLoadDatasetWithRepeatedLinePositions() throws IOException, NoSuchStandardElementException {
		ElementDatasetConfiguration datasetConfiguration = new ElementDatasetConfiguration(0.060d,	0.527d, 0.080d, "C12");
		LineDatasetLoader datasetLoader = new LineDatasetLoader(datasetConfiguration);
		
		ProgressHandler progressHandler = new DefaultProgressHandler();
		datasetLoader.loadAndNormalizeDataset(TEST_DATASET_LINE_POSITIONS_REPEATED_DIRECTORY.toPath(), progressHandler);
		assertTrue(progressHandler.hasWarnings());
		assertEquals(
			Arrays.asList(new String[]{
				"Line LINHA_03.xl has been skipped since position 0.08 is already occupied."
			}),
			progressHandler.getWarnings()
		);
	}
	
	private void assertCorrectDataset(ElementDataset dataset) {
		ElementData Na23 = dataset.getElement(NA23_NAME).get();
		
		assertEquals(8,dataset.getElements().size());
		assertEquals(
				new HashSet<>(TEST_DATASET_ELEMENT_NAMES),
				new HashSet<>(dataset.getElementNames())
				);
		assertEquals(0.0d, dataset.getMinValue(), 0d);
		assertEquals(1.942478674706621d,dataset.getMaxValue(), 0d);
		
		assertElementDataEquals(NA23, Na23);
	}
}
