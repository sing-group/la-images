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
package es.uvigo.ei.sing.laimages.gui.io;

import static es.uvigo.ei.sing.laimages.core.TestUtils.assertElementDataEquals;
import static es.uvigo.ei.sing.laimages.core.io.TestDatasetUtils.NA23_NAME;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.jzy3d.maths.Coord2d;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.laimages.core.io.TestDatasetUtils;
import es.uvigo.ei.sing.laimages.core.io.exception.NoSuchStandardElementException;
import es.uvigo.ei.sing.laimages.gui.analysis.LaImagesAnalysis;
import es.uvigo.ei.sing.laimages.gui.analysis.io.LaImagesAnalysisReader;
import es.uvigo.ei.sing.laimages.gui.analysis.io.LaImagesAnalysisWriter;
import es.uvigo.ei.sing.laimages.gui.analysis.io.SerializationLaImagesAnalysisReader;
import es.uvigo.ei.sing.laimages.gui.analysis.io.SerializationLaImagesAnalysisWriter;
import es.uvigo.ei.sing.laimages.gui.jzy3d.Range;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMap;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMapRangeMode;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.InterpolationLevel;

@RunWith(Parameterized.class)
public class SerializeLaImagesAnalysisTest {
	private static final ElementDataViewConfiguration CUSTOM_CONFIGURATION;
	
	static {
		CUSTOM_CONFIGURATION = new ElementDataViewConfiguration();
		CUSTOM_CONFIGURATION.setElementIndex(1);
		CUSTOM_CONFIGURATION.set3DEnabled(false);
		CUSTOM_CONFIGURATION.setScale(5f);
		CUSTOM_CONFIGURATION.setViewPositionMode2D(ElementDataViewConfiguration.FREE_2D_POSITION_MODE);
		CUSTOM_CONFIGURATION.setViewPoint(new Coord2d(1, 0.5));
		CUSTOM_CONFIGURATION.setInterpolationLevel(InterpolationLevel.MEDIUM);
		CUSTOM_CONFIGURATION.setColorMap(ColorMap.RGB);
		CUSTOM_CONFIGURATION.setRangeMode(ColorMapRangeMode.CUSTOM);
		CUSTOM_CONFIGURATION.setColorMapRange(new Range(0.1f, 2.5f));
		CUSTOM_CONFIGURATION.setShowColorBarLegend(false);
		CUSTOM_CONFIGURATION.setShowAxes(false);
	}
	
	private final LaImagesAnalysisWriter writer = new SerializationLaImagesAnalysisWriter();
	private final LaImagesAnalysisReader reader = new SerializationLaImagesAnalysisReader();
	private ElementDataViewConfiguration viewConfiguration;

	public SerializeLaImagesAnalysisTest(ElementDataViewConfiguration viewConfiguration) {
		this.viewConfiguration = viewConfiguration;
	}
	
	@Parameters
	public static Object[][] parameters() {
		return new Object[][] {
			new Object[] { new ElementDataViewConfiguration() },
			new Object[] { CUSTOM_CONFIGURATION }
		};
	}
	
	@Test
	public void testStoreAndRestore()  throws IOException, NoSuchStandardElementException {
		ElementDataset dataset = TestDatasetUtils.loadTestDataset();
		LaImagesAnalysis analysis = new LaImagesAnalysis(dataset, viewConfiguration);

		final File dest = File.createTempFile("laimages", "lai");
		writer.write(analysis, dest);
		
		LaImagesAnalysis readed = reader.read(dest);
		
		assertAnalysisEquals(analysis, readed);
	}

	private static void assertAnalysisEquals(LaImagesAnalysis expected,
		LaImagesAnalysis actual
	) {
		assertEquals(expected.getConfiguration(), actual.getConfiguration());
		assertCorrectDataset(expected.getDataset(), actual.getDataset());
	}
	
	private static void assertCorrectDataset(ElementDataset expected, ElementDataset actual) {
		ElementData expectedNa23 = expected.getElement(NA23_NAME).get();
		ElementData actualNa23 = actual.getElement(NA23_NAME).get();
		
		assertEquals(expected.getElementCount(), actual.getElementCount());
		assertEquals(
				new HashSet<>(expected.getElementNames()),
				new HashSet<>(actual.getElementNames())
			);
		assertEquals(expected.getMinValue(), actual.getMinValue(), 0d);
		assertEquals(expected.getMaxValue(), actual.getMaxValue(), 0d);
		
		assertElementDataEquals(expectedNa23, actualNa23);
	}
}
