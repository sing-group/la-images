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
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.NA23_NAME;
import static es.uvigo.ei.sing.icpms.core.io.TestDatasetUtils.loadTestDataset;
import static es.uvigo.ei.sing.icpms.core.matcher.HasEqualFileContentMatcher.hasEqualFileContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.HorizontalLineCoordinates;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat.FileFormat;
import es.uvigo.ei.sing.icpms.core.io.exception.NoSuchStandardElementException;

public class HorizontalElementDatasetTest {
	private static final File TEST_ELEMENT_DATA_LIBRE_OFFICE_ES =
		new File("src/test/resources/export/ElementDataTest-LibreOffice.es_ES.csv");
	private static final File TEST_ELEMENT_DATA_LIBRE_OFFICE_US =
		new File("src/test/resources/export/ElementDataTest-LibreOffice.en_US.csv");
		
	private static final ElementData TEST_ELEMENT_DATA = 
		ElementData.createElementData(
			"Test element data",  
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{1.0d}, 
					new HorizontalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d) 
				),
				new LineData(
					"Line 2", 
					new double[]{3.0d, 4.0d}, 
					new HorizontalLineCoordinates(0.1d, 0.0d, 0.1d, 0.1d) 
				),
				new LineData(
					"Line 3", 
					new double[]{5.0d, 6.0d}, 
					new HorizontalLineCoordinates(0.1d, 0.1d, 0.2d, 0.2d) 
				)
			}
		);
		
	private static final File NA23_LIBRE_OFFICE_ES =
		new File("src/test/resources/export/Na23-LibreOffice.es_ES.csv");
	private static final File NA23_EXCEL_ES =
		new File("src/test/resources/export/Na23-Excel.es_ES.csv");
	private static final File NA23_LIBRE_OFFICE_US =
		new File("src/test/resources/export/Na23-LibreOffice.en_US.csv");
	private static final File NA23_EXCEL_US =
		new File("src/test/resources/export/Na23-Excel.en_US.csv");
	
	private static ElementDataset dataset;
	private static ElementData Na23;
	
	@BeforeClass
	public static void setUp()
	throws IOException, NoSuchStandardElementException {
		dataset = loadTestDataset();
		Na23 = dataset.getElement(NA23_NAME).get();
	}
	
	@Test
	public void testExportElementToCSVWithLibreOfficeFormatES()
	throws IOException {
		testExport(new Locale("es", "ES"), FileFormat.LIBRE_OFFICE, NA23_LIBRE_OFFICE_ES);
	}

	@Test
	public void testExportElementToCSVWithExcelFormatES() throws IOException {
		testExport(new Locale("es", "ES"), FileFormat.EXCEL, NA23_EXCEL_ES);
	}
	
	@Test
	public void testExportElementToCSVWithLibreOfficeFormatUS()
	throws IOException {
		testExport(Locale.US, FileFormat.LIBRE_OFFICE, NA23_LIBRE_OFFICE_US);
	}

	@Test
	public void testExportElementToCSVWithExcelFormatUS() throws IOException {
		testExport(Locale.US, FileFormat.EXCEL, NA23_EXCEL_US);
	}
	
	private final static void testExport(
		Locale locale, FileFormat format, File expectedFile
	) throws IOException {
		Locale.setDefault(locale);
		
		File exportFile = exportElementToTmpCSVFile(Na23, format);
		
		assertThat(exportFile, hasEqualFileContent(expectedFile));
	}
	
	@Test
	public void testExportDatasetToCSV()
	throws IllegalArgumentException, IOException {
		File exportDirectory = createTempDir();
		
		dataset.toCSV(exportDirectory, new CSVFormat(FileFormat.LIBRE_OFFICE));
		
		assertEquals(dataset.getElementCount(),
			exportDirectory.listFiles().length);
		
		FileUtils.deleteDirectory(exportDirectory);
	}
	
	private final static File exportElementToTmpCSVFile(ElementData data, FileFormat format)
	throws IOException {
		File exportFile = File.createTempFile(data.getName(), ".csv");
		exportFile.deleteOnExit();
		
		dataset.toCSV(data, exportFile, new CSVFormat(format));
		
		return exportFile;
	}

	private final static File createTempDir() throws IOException {
		return Files.createTempDirectory("icp-ms-core-test").toFile();
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testCreateElementDataWithSamePositionLineData() {
		ElementData.createElementData("", new LineData[] { 
			new LineData("", new double[] {1.0d},
				new HorizontalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			),
			new LineData("", new double[] {1.0d},
				new HorizontalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d)
			),
		});
	}	
	
	@Test
	public void testCreateHorizontalElementData() {
		ElementData actual = ElementData.createElementData(
			"Test element data",  
			new LineData[]{
				new LineData(
					"Line 1", 
					new double[]{1.0d}, 
					new HorizontalLineCoordinates(0.1d, 0.0d, 0.0d, 0.0d) 
				),
				new LineData(
					"Line 2", 
					new double[]{3.0d, 4.0d}, 
					new HorizontalLineCoordinates(0.1d, 0.0d, 0.1d, 0.1d) 
				),
				new LineData(
					"Line 3", 
					new double[]{5.0d, 6.0d}, 
					new HorizontalLineCoordinates(0.1d, 0.1d, 0.2d, 0.2d) 
				)
			}
		);

		assertElementDataEquals(TEST_ELEMENT_DATA, actual);
		assertMatrixEquals(
			new double[][]{
				new double[]{1.0d, 0.0d, 0.0d},
				new double[]{3.0d, 4.0d, 0.0d},
				new double[]{0.0d, 5.0d, 6.0d},
			}, 
			actual.getData(0.0d), 0.01
		);
		assertListEquals(
			Arrays.asList(new Double[]{0.0d, 0.1d, 0.2d}), 
			actual.getXAxis(), 0.01d);
		assertListEquals(
			Arrays.asList(new Double[]{0.0d, 0.1d, 0.2d}), 
			actual.getYAxis(), 0.01d);		
	}

	@Test
	public void testElementDataToCSVVWithLibreOfficeFormatES() throws IOException {
		Locale.setDefault(new Locale("es", "ES"));
		
		File exportFile = exportElementToTmpCSVFile(TEST_ELEMENT_DATA, FileFormat.LIBRE_OFFICE);
		
		assertTrue(FileUtils.contentEquals(TEST_ELEMENT_DATA_LIBRE_OFFICE_ES, exportFile));
	}

	@Test
	public void testElementDataToCSVVWithLibreOfficeFormatUS() throws IOException {
		Locale.setDefault(new Locale("en", "US"));
		
		File exportFile = exportElementToTmpCSVFile(TEST_ELEMENT_DATA, FileFormat.LIBRE_OFFICE);
		
		assertTrue(FileUtils.contentEquals(TEST_ELEMENT_DATA_LIBRE_OFFICE_US, exportFile));
	}	
}
