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
package es.uvigo.ei.sing.icpms.core.io.coordinates;

import static es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.LineCoordinatesUtils.getLinesRangeAxis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.HorizontalLineCoordinates;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.LineCoordinates;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.VerticalLineCoordinates;

/**
 * <p>
 * This class reads a line coordinates file into a {@code LineCoordinates} array.
 * </p>
 * 
 * <p>
 * The following is an example of coordinates file:
 * </p>
 * <pre>
 *   LINE
 *   0.0,0.0,0.0
 *   5.1856800000000005,0.0,0.0
 *   LINE
 *   0.0,0.08,0.0
 *   5.1856800000000005,0.08,0.0
 * </pre>
 * 
 * <p>
 * Coordinates files have the following format:
 * </p>
 * <ul>
 * <li>There are several 'LINE' blocks that contains one or more lines of numbers (x, y, z).</li>
 * <li>For each 'LINE' block we are interested in the first and last lines of numbers:
 * 	<ul>
 * 		<li>First line contains the X start point and the Y start point of the line.</li>
 * 		<li>Last line contains the X end point and the Y end point of the line.</li>
 * 	</ul>
 * </li>
 * <li>If the X start point is equal to the X end point, then this unique X coordinate is the 
 * line position. Otherwise, the Y start and end points will be equal and are the line position.</li>
 * <li>The pair of coordinates that are different represents the line range values.</li>
 * </ul>
 * 
 * <p>Since line range coordinates from different 'LINE' blocks may be unaligned (e.g.: for one 'LINE' the range
 * axis may go from 0.0 to 0.5 and from other 'LINE' it may go from 0.01 to 0.51) so it is 
 * necessary to align them so lines are compatible and can be represented in a common range axis.</p>
 * 
 * @author hlfernandez
 *
 */
public class LineCoordinatesLoader {

	private static final String LINE_BLOCK_TAG = "LINE";
	
	/**
	 * Reads the line coordinates specified at {@code coordinatesFile} and
	 * constructs a {@code LineCoordinates} array with the aligned coordinates.
	 * 
	 * @param coordinatesFile the coordinates file.
	 * @param xTick the size of the ticks in the X axis.
	 * @return s a {@code LineCoordinates} array with the aligned coordinates
	 * @throws IOException if an error occurs parsing the coordinates file.
	 */
	public static final LineCoordinates[] loadCoordinates(File coordinatesFile,
			double xTick) throws IOException {
		return parseCoordinatesFile(coordinatesFile, xTick);
	}

	private static final LineCoordinates[] parseCoordinatesFile(File coordinatesFile, double xTick) throws IOException {
		List<String> lines = Files.readAllLines(coordinatesFile.toPath());
		List<LineCoordinates> coordinatesList = loadCoordinates(xTick, lines);
		alignCoordinates(coordinatesList);

		return coordinatesList.toArray(new LineCoordinates[coordinatesList.size()]);
	}

	private static List<LineCoordinates> loadCoordinates(double xTick,
			List<String> lines) {
		List<LineCoordinates> coordinatesList = new LinkedList<LineCoordinates>();
		Iterator<String> linesIt = lines.iterator();
		String previousLine = null;
		Coordinate start = null;
		
		while(linesIt.hasNext()) {
			String currentLine = linesIt.next();
		
			if(currentLine.equals(LINE_BLOCK_TAG)) {
				readEndLineAndAddCoordinate(xTick, coordinatesList,
						previousLine, start);
			} else {
				if(previousLine.equals(LINE_BLOCK_TAG)) {
					start = readCoordinate(currentLine);
				}
			}
			
			previousLine = currentLine;
		}

		readEndLineAndAddCoordinate(xTick, coordinatesList, previousLine, start);
		
		return coordinatesList;
	}

	private static void readEndLineAndAddCoordinate(double xTick,
			List<LineCoordinates> coordinatesList, String previousLine,
			Coordinate start) {
		Coordinate end;
		end = readCoordinate(previousLine);
		addLineCoordinates(coordinatesList, start, end, xTick);
	}

	private static void addLineCoordinates(
			List<LineCoordinates> coordinatesList, Coordinate start,
			Coordinate end, double xTick) {
		if (start == null || end == null) {
			return;
		}
		
		LineCoordinates newLineCoordinates = null;
		double xStart, xEnd, y;
		if(start.getX() == end.getX()) {
			xStart = start.getY();
			xEnd = end.getY();
			y = start.getX();
			newLineCoordinates = new VerticalLineCoordinates(xTick, xStart, xEnd, y);
		} else {
			xStart = start.getX();
			xEnd = end.getX();
			y = start.getY();
			newLineCoordinates = new HorizontalLineCoordinates(xTick, xStart, xEnd, y);
		}
		coordinatesList.add(newLineCoordinates);
	}

	private static Coordinate readCoordinate(String line) {
		if (line != null) {
			String[] split = line.split(",");
			return new Coordinate(
				Double.valueOf(split[0]),
				Double.valueOf(split[1])
			);
		}
		return null;
	}

	static class Coordinate {
		private double x;
		private double y;
		
		public Coordinate(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
	}
	
	private static void alignCoordinates(
			List<LineCoordinates> coordinates) {
		List<Double> linesRangeAxis = getLinesRangeAxis(coordinates);
		for(LineCoordinates lC : coordinates) {
			double closest = findClosest(lC.getRangeStart(), linesRangeAxis);
			lC.setStartRangeCoordinate(closest);
			closest = findClosest(lC.getRangeEnd(), linesRangeAxis);
			lC.setEndRangeCoordinate(closest);
		}
	}
	
	private static final double findClosest(double target, List<Double> values) {
		double closest = Double.NaN;
		double minimumDifference = Double.MAX_VALUE;
		for(double value : values) {
			double difference = Math.abs(value-target);
			if(difference < minimumDifference) {
				minimumDifference = difference;
				closest = value;
			}
		}
		return closest;
	}
}
