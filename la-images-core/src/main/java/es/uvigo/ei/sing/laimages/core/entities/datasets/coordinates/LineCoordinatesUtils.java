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
package es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDatasetConfiguration;
import es.uvigo.ei.sing.laimages.core.entities.datasets.LineData;

/**
 * Utility class to work with {@code LineCoordinates} objects.
 * 
 * @author Hugo López-Fernández
 *
 */
public class LineCoordinatesUtils {
	/**
	 * Return {@code true} if all the {@code LineData} are vertical and 
	 * {@code false} otherwise.
	 * 
	 * @param lines a list of {@code LineData}.
	 * @return {@code true} if all the {@code LineData} are vertical and 
	 * {@code false} otherwise.
	 */
	public static boolean areVertical(LineData[] lines) {
		return Stream.of(lines).allMatch(LineData::isVertical);
	}
	
	/**
	 * Return {@code true} if all the {@code LineData} are horizontal and 
	 * {@code false} otherwise.
	 * 
	 * @param lines a list of {@code LineData}.
	 * @return {@code true} if all the {@code LineData} are horizontal and 
	 * {@code false} otherwise.
	 */
	public static boolean areHorizontal(LineData[] lines) {
		return Stream.of(lines).noneMatch(LineData::isVertical);
	}

	/**
	 * Constructs an axis with all the line positions.
	 * 
	 * @param coordinates a list of {@code LineCoordinates}.
	 * @return a list of {@code Double} that represents lines positions axis.
	 */
	public static List<Double> getLinesPositionsAxis(
			List<LineCoordinates> coordinates) {
		if(coordinates.isEmpty()) {
			return Collections.emptyList();
		}
		
		return coordinates.stream().map(LineCoordinates::getPosition)
			.collect(Collectors.toList());
	}

	/**
	 * Constructs an axis for the lines ranges.
	 * 
	 * @param coordinates a list of {@code LineCoordinates}.
	 * @return a list of {@code Double} that represents lines ranges axis.
	 */
	public static List<Double> getLinesRangeAxis(
			List<LineCoordinates> coordinates) {
		requireCompatibleCoordinates(coordinates);
		
		if (coordinates.isEmpty()) {
			return Collections.emptyList();
		}
		
		Range xRange = findMinAndMaxXValues(coordinates);
		return xRange.sequence(coordinates.get(0).getRangeTick());
	}
	
	private static final void requireCompatibleCoordinates(
			List<LineCoordinates> coordinates) {
		Set<Double> rangeTicks = coordinates.stream()
				.map(LineCoordinates::getRangeTick).collect(Collectors.toSet());
		if (rangeTicks.size() > 1) {
			throw new IllegalArgumentException(
				"All coordinates must have the same range interval");
		}
	}

	private static final Range findMinAndMaxXValues(List<LineCoordinates> coordinates) {
		double minX = Double.MAX_VALUE;
		double maxX = 0;
		for(LineCoordinates lC : coordinates) {
			if(lC.getRangeStart() < minX ) {
				minX = lC.getRangeStart();
			}
			if(lC.getRangeEnd() > maxX ) {
				maxX = lC.getRangeEnd();
			}
		}
		return new Range(minX, maxX);
	}
	
	static class Range {
		
		private double min;
		private double max;

		public Range(double min, double max) {
			this.min = min;
			this.max = max;
		}

		public double getMin() {
			return min;
		}
		
		public double getMax() {
			return max;
		}
		
		public List<Double> sequence(double xTick) {
			List<Double> sequence = new ArrayList<Double>();
			double last = 0;
			int count = 0;
			while(last < this.max) {
				last = this.min + count * xTick;
				sequence.add(last);
				count++;
			}
			return sequence;
		}
	}

	/**
	 * Return {@code true} if each {@code LineData} have a different position
	 * {@code false} otherwise.
	 * 
	 * @param lines a list of {@code LineData}.
	 * @return {@code true} if each {@code LineData} have a different position
	 * {@code false} otherwise.
	 */
	public static boolean haveDifferentPositions(LineData[] lines) {
		return getPositionsStream(lines).count() == getPositionsStream(lines)
				.distinct().count();
	}

	private static DoubleStream getPositionsStream(LineData[] lines) {
		return 	Stream.of(lines)
				.map(LineData::getCoordinates)
				.mapToDouble(LineCoordinates::getPosition);
	}

	
	/**
	 * Normalizes the position of a list of {@code LineCoordinates} objects 
	 * so that:
	 * <ul>
	 * <li>First line is located at position 0 and the following are located
	 * at intervals of {@code ElementDatasetConfiguration::getPositionInterval}.
	 * </li>
	 * <li>Ranges are converted so that the minimum range start at 0.</li>
	 * </ul>
	 * 
	 * @param coordinates a list of {@code LineCoordinates} objects.
	 * @param configuration a {@code ElementDatasetConfiguration} object.
	 */
	public static final void normalizeCoordinates(
			List<LineCoordinates> coordinates, ElementDatasetConfiguration configuration
	) {
		double minRangeValue = coordinates.stream()
			.mapToDouble(LineCoordinates::getRangeStart).min().getAsDouble();
		double tick = configuration.getPositionInterval();
		for (int i = 0; i < coordinates.size(); i++) {
			LineCoordinates current = coordinates.get(i);
			
			current.setStartRangeCoordinate(
				current.getRangeStart()	- minRangeValue);
			current.setEndRangeCoordinate(
				current.getRangeEnd() - minRangeValue);
			current.setPosition(tick * i);
		}
	}
}
