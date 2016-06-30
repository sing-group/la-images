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
package es.uvigo.ei.sing.icpms.core.entities.datasets;

import static es.uvigo.ei.sing.icpms.core.TestUtils.assertElementDataEquals;
import static es.uvigo.ei.sing.icpms.core.TestUtils.assertListEquals;
import static es.uvigo.ei.sing.icpms.core.TestUtils.assertMatrixEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.HorizontalLineCoordinates;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.VerticalLineCoordinates;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat.FileFormat;

public class VerticalElementDataTest {
	private static final File TEST_ELEMENT_DATA_VERTICAL_LIBRE_OFFICE_ES =
			new File("src/test/resources/export/ElementDataTest-Vertical-LibreOffice.es_ES.csv");
	private static final File TEST_ELEMENT_DATA_VERTICAL_LIBRE_OFFICE_US =
			new File("src/test/resources/export/ElementDataTest-Vertical-LibreOffice.en_US.csv");
	
	private static final ElementData TEST_ELEMENT_DATA_VERTICAL = 
		ElementData.createElementData(
			"Test element data",  
			new LineData[]{
					new LineData(
						"Line 1", 
						new double[]{1.0d}, 
						new VerticalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d) 
					),
					new LineData(
						"Line 2", 
						new double[]{3.0d, 4.0d}, 
						new VerticalLineCoordinates(0.1d, 0.0d, 0.1d, 0.1d) 
					),
					new LineData(
						"Line 3", 
						new double[]{5.0d, 6.0d}, 
						new VerticalLineCoordinates(0.1d, 0.1d, 0.2d, 0.2d) 
					)
			}
		);

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateEmptyElementData() {
		ElementData.createElementData("", new LineData[]{});
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateElementDataWithEmptyLineData() {
		ElementData.createElementData("", new LineData[] { 
			new LineData("", new double[] {},
				new VerticalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			)
		});
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateElementDataWithVerticalAndHorizontalLineData() {
		ElementData.createElementData("", new LineData[] { 
			new LineData("", new double[] {1.0d},
				new HorizontalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			),
			new LineData("", new double[] {1.0d},
				new VerticalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			),
		});
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateElementDataWithSamePositionLineData() {
		ElementData.createElementData("", new LineData[] { 
			new LineData("", new double[] {1.0d},
				new VerticalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			),
			new LineData("", new double[] {1.0d},
				new VerticalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			),
		});
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateInvalidElementData() {
		ElementData.createElementData(
			"",  
			new LineData[]{}
		);
	}
	
	@Test
	public void testCreateVerticalElementData() {
		ElementData actual = ElementData.createElementData(
			"Test element data",  
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{1.0d}, 
					new VerticalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d) 
				),
				new LineData(
					"Line 2", 
					new double[]{3.0d, 4.0d}, 
					new VerticalLineCoordinates(0.1d, 0.0d, 0.1d, 0.1d) 
				),
				new LineData(
					"Line 3", 
					new double[]{5.0d, 6.0d}, 
					new VerticalLineCoordinates(0.1d, 0.1d, 0.2d, 0.2d) 
				)
			}
		);
		
		assertElementDataEquals(TEST_ELEMENT_DATA_VERTICAL, actual);
		assertElementDataEquals(TEST_ELEMENT_DATA_VERTICAL, actual);
		assertMatrixEquals(
			new double[][]{
				new double[]{1.0d, 3.0d, 0.0d},
				new double[]{0.0d, 4.0d, 5.0d},
				new double[]{0.0d, 0.0d, 6.0d},
			}, 
			actual.getData(0.0d), 0.01);
		assertListEquals(
			Arrays.asList(new Double[]{0.0d, 0.1d, 0.2d}), 
			actual.getXAxis(), 0.01d);
		assertListEquals(
			Arrays.asList(new Double[]{0.0d, 0.1d, 0.2d}), 
			actual.getYAxis(), 0.01d);
	}
	
	@Test
	public void testCreateVerticalElementData2() {
		ElementData verticalElementData = ElementData.createElementData("Test", 
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{1.0d,1.0d,1.0d,0d,0d}, 
					new VerticalLineCoordinates(0.1d, 0.0d, 0.4d, 0.0d) 
				),
				new LineData(
					"Line 2", 
					new double[]{1.0d, 1.0d ,1.0d,0d,0d}, 
					new VerticalLineCoordinates(0.1d, 0.0d, 0.4d, 0.1d) 
				),
				new LineData(
					"Line 3", 
					new double[]{1.0d, 1.0d, 1.0d,1.0d,1.0d}, 
					new VerticalLineCoordinates(0.1d, 0.0d, 0.4d, 0.2d) 
				)
			}
		);
		assertMatrixEquals(
			new double[][]{
				new double[]{1.0d, 1.0d, 1.0d},
				new double[]{1.0d, 1.0d, 1.0d},
				new double[]{1.0d, 1.0d, 1.0d},
				new double[]{0.0d, 0.0d, 1.0d},
				new double[]{0.0d, 0.0d, 1.0d},
			}, 
			verticalElementData.getData(0.0d), 0.01
		);
		assertListEquals(
			Arrays.asList(new Double[]{0.0d, 0.1d, 0.2d}), 
			verticalElementData.getXAxis(), 0.01);
		assertListEquals(
			Arrays.asList(new Double[]{0.0d, 0.1d, 0.2d, 0.3d, 0.4d}), 
			verticalElementData.getYAxis(), 0.01);
	}

	@Test
	public void testVerticalElementDataToCSVVWithLibreOfficeFormatES() throws IOException {
		Locale.setDefault(new Locale("es", "ES"));
		
		File exportFile = exportElementToTmpCSVFile(TEST_ELEMENT_DATA_VERTICAL, FileFormat.LIBRE_OFFICE);
		
		assertTrue(FileUtils.contentEquals(TEST_ELEMENT_DATA_VERTICAL_LIBRE_OFFICE_ES, exportFile));
	}
	
	@Test
	public void testVerticalElementDataToCSVVWithLibreOfficeFormatUS() throws IOException {
		Locale.setDefault(new Locale("en", "US"));
		
		File exportFile = exportElementToTmpCSVFile(TEST_ELEMENT_DATA_VERTICAL, FileFormat.LIBRE_OFFICE);
		
		assertTrue(FileUtils.contentEquals(TEST_ELEMENT_DATA_VERTICAL_LIBRE_OFFICE_US, exportFile));
	}
	
	private final static File exportElementToTmpCSVFile(ElementData data,
			FileFormat format) throws IOException {
		File exportFile = File.createTempFile(data.getName(), ".csv");
		exportFile.deleteOnExit();

		data.toCSV(exportFile, new CSVFormat(format));

		return exportFile;
	}
}
