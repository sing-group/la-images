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
package es.uvigo.ei.sing.laimages.gui;

import static es.uvigo.ei.sing.laimages.gui.util.TestUtils.createAndShowAnalysisViewer;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.sing.laimages.core.io.ResourceLoader;
import es.uvigo.ei.sing.laimages.core.io.exception.NoSuchStandardElementException;
import es.uvigo.ei.sing.laimages.gui.analysis.LaImagesAnalysis;
import es.uvigo.ei.sing.laimages.gui.analysis.io.LaImagesAnalysisReader;
import es.uvigo.ei.sing.laimages.gui.analysis.io.SerializationLaImagesAnalysisReader;
import es.uvigo.ei.sing.laimages.gui.util.TestUtils;

public class HorizontalElementAnalysisViewerTest {
	private static final File TEST_DATASET_FILE_ANALYSIS = ResourceLoader.loadResource(
		"/analysis/dataset.lai");

	public static void main(String[] args) throws IOException, NoSuchStandardElementException {
		TestUtils.setNimbusLookAndFeel();
		createAndShowAnalysisViewer(loadAnalysis());
	}

	private static LaImagesAnalysis loadAnalysis() throws IOException {
		LaImagesAnalysisReader reader = new SerializationLaImagesAnalysisReader();
		return reader.read(TEST_DATASET_FILE_ANALYSIS);
	}
}
