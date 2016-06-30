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

import static es.uvigo.ei.sing.icpms.core.io.TestVerticalDatasetUtils.NA23_NAME;
import static es.uvigo.ei.sing.icpms.core.io.TestVerticalDatasetUtils.NA23_REDUCED;
import static es.uvigo.ei.sing.icpms.core.io.TestVerticalDatasetUtils.TEST_DATASET_ELEMENT_NAMES;
import static es.uvigo.ei.sing.icpms.core.io.TestVerticalDatasetUtils.loadTestDataset;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.icpms.core.io.exception.NoSuchStandardElementException;

public class LoadVerticalDatasetTest {

	@Test
	public void testLoadDataset() throws IOException, NoSuchStandardElementException {
		ElementDataset dataset = loadTestDataset();
		assertCorrectDataset(dataset);
	}
	
	private void assertCorrectDataset(ElementDataset dataset) {
		ElementData Na23 = dataset.getElement(NA23_NAME).get();
		assertEquals(6,dataset.getElements().size());
		assertEquals(
				new HashSet<>(TEST_DATASET_ELEMENT_NAMES),
				new HashSet<>(dataset.getElementNames())
				);
		assertEquals(0.0d, dataset.getMinValue(), 0d);
		assertEquals(8.57418401048601d,dataset.getMaxValue(), 0d);
		
		assertEquals(Na23.getNumLines(), 72);
		assertEquals(Na23.isVertical(), true);
		for(int i = 0; i < 3; i++) {
			assertArrayEquals(
				NA23_REDUCED.getLines()[i].getData(), 
				Na23.getLines()[i].getData(), 
				0.01
			);
		}
		assertArrayEquals(
			NA23_REDUCED.getCoordinates(), 
			Arrays.copyOfRange(Na23.getCoordinates(),0, 3)
		);
	}
}
