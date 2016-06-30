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
package es.uvigo.ei.sing.laimages.core.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDatasetConfiguration;
import es.uvigo.ei.sing.laimages.core.entities.datasets.LineData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.HorizontalLineCoordinates;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinates;
import es.uvigo.ei.sing.laimages.core.io.coordinates.LineCoordinatesLoader;
import es.uvigo.ei.sing.laimages.core.io.exception.InvalidDataException;
import es.uvigo.ei.sing.laimages.core.io.exception.PositionsFileNotFoundException;
import es.uvigo.ei.sing.laimages.core.util.FileNameUtils;
import es.uvigo.ei.sing.laimages.core.util.ProgressHandler;

/**
 * A class to facilitate the conversion from Line files to {@code ElementData} objects.
 * 
 * @author Hugo López-Fernández
 *
 */
public class LineToElementConverter {

	public static final String POSITIONS_FILE_NAME = "positions.txt";
	
	private static final Comparator<String> LINE_MAP_COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return getLineNumber(o1).compareTo(getLineNumber(o2));
		}
	};
	
	private static final Comparator<File> LINE_FILES_COMPARATOR = new Comparator<File>(){
		@Override
		public int compare(File o1, File o2) {
			return getLineNumber(o1.getName()).compareTo(getLineNumber(o2.getName()));
		}
	};
	
	private FilenameFilter fileFilter;
	private File directory;
	private ElementDatasetConfiguration configuration;
	private LineCoordinates[] coordinates;
	private Map<String, Map<String, List<String>>> data;
	private Set<Integer> linesLength = new HashSet<Integer>();

	/**
	 * Constructs an instance of {@code LineToElementConverter} and parses all
	 * the files in {@code directory} that pass the {@code fileFilter}.
	 * 
	 * @param directory the directory containing the files.
	 * @param fileFilter the file filter criteria.
	 * @param configuration the acquisition parameters configuration.
	 * @throws IOException if an error occurs loading the data. 
	 */
	public LineToElementConverter(File directory, FilenameFilter fileFilter, 
			ElementDatasetConfiguration configuration
	) throws IOException {
		this.directory = directory;
		this.fileFilter = fileFilter;
		this.configuration = configuration;
		this.process();
		this.coordinates = loadLineCoordinates(directory);
	}

	/**
	 * Constructs an instance of {@code LineToElementConverter} and parses all
	 * the files in {@code directory} that pass the {@code fileFilter}.
	 * 
	 * @param directory the directory containing the files.
	 * @param fileFilter the file filter criteria.
	 * @param configuration the acquisition parameters configuration.
	 * @throws IOException if an error occurs loading the data. 
	 */
	public LineToElementConverter(String directory, FilenameFilter fileFilter, 
		ElementDatasetConfiguration configuration
	) throws IOException {
		this(new File(directory), fileFilter, configuration);
	}
	
	private void process() {
		data = new HashMap<String, Map<String,List<String>>>();
		for(File lineFile : getLineFiles()) {
			parseLineFile(data, lineFile);
		}
	}
	
	private File[] getLineFiles() {
		File[] files = this.directory.listFiles(fileFilter);
		Arrays.sort(files, LINE_FILES_COMPARATOR);
		return files;
	}
	
	private static final Integer getLineNumber(String lineName) {
		Pattern p = Pattern.compile("[0-9][0-9]*");
		Matcher m = p.matcher(lineName);
		if(m.find()) {
		    int n = Integer.parseInt(m.group(0));
		    return n;
		} else {
			return 0;
		}
	}
	
	private void parseLineFile(Map<String, Map<String, List<String>>> data,
			File lineFile) {
		 try {
			String lineName = lineFile.getName().replaceAll(" ", "_");
			Scanner input = new Scanner(lineFile);
			String[] headers = null;
			
			if (input.hasNextLine()) {
				input.nextLine();
			}
			
			if (input.hasNextLine()) {
				String header = input.nextLine();
				headers = header.split(",");
				for(int i = 1; i < headers.length; i++) {
					data.putIfAbsent(headers[i], new TreeMap<String, List<String>>(LINE_MAP_COMPARATOR));
					data.get(headers[i]).putIfAbsent(lineName, new LinkedList<String>());
				}
			}
			
			int lineLength = 0;
			
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if(!line.equals("")) {
					lineLength++;
					String[] values = line.split(",");
					for(int i = 1; i < values.length; i++) {
						data.get(headers[i]).get(lineName).add(values[i]);
					}
				}
			}
			
			linesLength.add(lineLength);
			
			input.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns the list of {@code ElementData} loaded.
	 * 
	 * @param progressHandler a {@code ProgressHandler} object.
	 * @return the list of {@code ElementData} loaded.
	 * @throws InvalidDataException if the line length (defined by its 
	 * 	coordinates) is longer than the available lengh.
	 */
	public List<ElementData> getElements(ProgressHandler progressHandler) 
		throws InvalidDataException 
	{
		List<ElementData> elements = new LinkedList<ElementData>();
		
		for (String element : data.keySet()) {
			Map<String, List<String>> elementData = data.get(element);
			
			Iterator<String> lineIterator = elementData.keySet().iterator();
			List<LineData> lines = new LinkedList<LineData>();
			Set<Double> linesPositions = new HashSet<Double>();
			int lineIndex = 0; 
			while(lineIterator.hasNext()) {
				String line = lineIterator.next();
				double[] lineValues = toDoubleArray(elementData.get(line));
				LineCoordinates currentCoordinates = this.coordinates[lineIndex];
				int validLineMeasurements = (int) (
					1 +	Math.round(
						(currentCoordinates.getRangeEnd() - currentCoordinates.getRangeStart())
						/ currentCoordinates.getRangeTick()
					)
				);

				if (validLineMeasurements > lineValues.length) {
					throw new InvalidDataException("The number of required "
							+ "measurements for line " + line + " is " + 
							validLineMeasurements + " but file " + line + 
							" contains only " + lineValues.length + 
							" measurements. Please, check that the line length "
							+ "and the data source file are correct.");
				}
				
				if(linesPositions.contains(currentCoordinates.getPosition())) {
					progressHandler.warn(
						getSkippedLineMessage(line, currentCoordinates));
				} else {
					lines.add(new LineData(
						line,
						Arrays.copyOfRange(lineValues, 0, validLineMeasurements),
						currentCoordinates
					));
					linesPositions.add(currentCoordinates.getPosition());
				}
				lineIndex++;
			}
			
			elements.add(
				ElementData.createElementData(
					element, lines.toArray(new LineData[lines.size()])
				)
			);
		}
		
		return elements;
	}
	
	private String getSkippedLineMessage(String line,
			LineCoordinates currentCoordinates) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("Line ")
			.append(line)
			.append(" has been skipped since position ")
			.append(currentCoordinates.getPosition())
			.append(" is already occupied.");
		return sb.toString();
	}

	private static final double[] toDoubleArray(List<String> list) {
		double[] toret = new double[list.size()];
		int i = 0;
		for(String s : list) {
			toret[i++] = Double.valueOf(s);
		}
		return toret;
	}
	
	private LineCoordinates[] loadLineCoordinates(File directory) throws IOException {
		File positionsFile = getPositionsFile(directory);
		if(!checkPositionsFile(positionsFile)) {
			if(!allLinesHaveSameLength()) {
				throw new PositionsFileNotFoundException(
					"Dataset directory must contain a fille called "
					+ POSITIONS_FILE_NAME + " specifying the line "
					+ "positions or all lines must have the same length."
				);
			} else {
				return generatePositions(getLinesCount(), getlinesLength());
			}
		} else {
			return LineCoordinatesLoader.loadCoordinates(positionsFile,
					configuration.getLineRangeInterval());
		}
	}

	private File getPositionsFile(File directory) {
		return 	FileNameUtils.findFileIgnoreCase(directory, POSITIONS_FILE_NAME)
				.orElse(null);
	}

	private static final boolean checkPositionsFile(File file) {
		return file!=null && file.isFile() && file.canRead();
	}

	private int getLinesCount() {
		return getLineFiles().length;
	}

	private boolean allLinesHaveSameLength() {
		return getlinesLengths().size() == 1;
	}

	private Set<Integer> getlinesLengths() {
		Set<Integer> linesLengths = new HashSet<Integer>();
		for (Entry<String, Map<String, List<String>>> element : this.data
				.entrySet()) {
			for(Entry<String, List<String>> line : element.getValue().entrySet()) {
				linesLengths.add(line.getValue().size());
			}
		}
		return linesLengths;
	}
	
	private int getlinesLength() {
		return getlinesLengths().iterator().next();
	}
	
	private final LineCoordinates[] generatePositions(int lines, int measurements){
		final double incX = this.configuration.getAblationSpeed() * this.configuration.getAcquisitionTime();
		final double incY = this.configuration.getSpaceInterval();

		double startX = 0;
		double endX = incX * (measurements-1);
		
		LineCoordinates[] lineCoordinates = new LineCoordinates[lines];
		for(int i = 0; i < lines; i++) {
			lineCoordinates[i] = new HorizontalLineCoordinates(incX, startX, endX, incY * i);
		}
		
		return lineCoordinates;
	}
}
