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
package es.uvigo.ei.sing.icpms.core.gui;

import static es.uvigo.ei.sing.icpms.core.gui.util.TestUtils.createAndShowAnalysisViewer;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.sing.icpms.core.entities.datasets.DefaultElementDataset;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDatasetConfiguration;
import es.uvigo.ei.sing.icpms.core.entities.datasets.LineData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.VerticalLineCoordinates;
import es.uvigo.ei.sing.icpms.core.gui.util.TestUtils;
import es.uvigo.ei.sing.icpms.core.io.exception.NoSuchStandardElementException;

public class ZVerticalElementDatasetViewerTest {
	private static final ElementData VERTICAL_ELEMENT_DATA = 
		ElementData.createElementData("Test", 
				new LineData[]{
					new LineData(
						"Line 1",
						new double[] { 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.0d, 0.5d, 0.0d)
					),
					new LineData(
						"Line 2",
						new double[] { 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.0d, 0.5d, 0.1d)
					),
					new LineData(
						"Line 3", 
						new double[] { 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.3d, 0.5d, 0.2d)
					),
					new LineData(
						"Line 4",
						new double[] { 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.2d, 0.4d, 0.3d)
					),
					new LineData(
						"Line 5",
						new double[] { 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.1d, 0.3d, 0.4d) 
					),
					new LineData(
						"Line 6",
						new double[] { 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.0d, 0.2d, 0.5d)
					),
					new LineData(
						"Line 7",
						new double[] { 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.0d, 0.5d, 0.6d)
					),
					new LineData(
						"Line 8",
						new double[] { 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d },
						new VerticalLineCoordinates(0.1d, 0.0d, 0.5d, 0.7d)
					)
				}
			);

	public static void main(String[] args) throws IOException, NoSuchStandardElementException {
		TestUtils.setNimbusLookAndFeel();
		createAndShowAnalysisViewer(testMissingPositionsDataset());
	}

	private static ElementDataset testMissingPositionsDataset() {
		DefaultElementDataset d = new DefaultElementDataset(
			new File("/tmp").toPath(), 
			"Test", 
			new ElementDatasetConfiguration(0.1d, 0.1d, 0.1d, ""));
		d.addElement(VERTICAL_ELEMENT_DATA);
		return d;
	}
}
