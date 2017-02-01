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
package es.uvigo.ei.sing.laimages.core.operations;

import static es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData.createElementData;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.LineData;

/**
 * A class that helps normalizing an {@code ElementData} by a given standard
 * {@code ElementData}.
 * 
 * @author Hugo López-Fernández
 * @author Miguel Reboiro-Jato
 *
 */
public class NormalizeElementData {
	/**
	 * Normalizes the list of {@code ElementData} contained in
	 * {@code toNormalize} by the {@code standard} element.
	 * 
	 * @param standard the standard {@code ElementData}.
	 * @param toNormalize the list of {@code ElementData} to normalize.
	 * @return the normalized {@code ElementData}.
	 */
	public static List<ElementData> normalize(
		ElementData standard, List<ElementData> toNormalize
	) {
		return toNormalize.stream()
			.map(data -> normalize(standard, data))
		.collect(toList());
	}
	
	/**
	 * Normalizes the list of {@code ElementData} contained in
	 * {@code toNormalize} by the {@code standard} element.
	 * 
	 * @param standard the standard {@code ElementData}.
	 * @param toNormalize the list of {@code ElementData} to normalize.
	 * @return the normalized {@code ElementData}.
	 */
	public static ElementData[] normalize(
		ElementData standard, ElementData ... toNormalize
	) {
		return Stream.of(toNormalize)
			.map(data -> normalize(standard, data))
		.toArray(ElementData[]::new);
	}
	
	/**
	 * Normalizes the {@code toNormalize} element by the {@code standard} element.
	 * Note that if {@code standard} element contains zeroes, {@code NaN} values
	 * will appear at these positions in the normalized {@code ElementData}.
	 * 
	 * @param standard the standard {@code ElementData}.
	 * @param toNormalize the {@code ElementData} to normalize.
	 * @return the normalized {@code ElementData}.
	 */
	public static ElementData normalize(
		ElementData standard, ElementData toNormalize
	) {
		if (!toNormalize.isCompatibleWith(standard))
			throw new IllegalArgumentException("standard and toNormalize are not compatible");
		
		final LineData[] normalized = new LineData[toNormalize.getNumLines()];
		
		for (int i = 0; i < toNormalize.getNumLines(); i++) {
			final LineData lDtoNormalize = toNormalize.getLines()[i];
			final LineData lDStandard = standard.getLines()[i];
			final double[] normalizedValues = normalize(
				lDtoNormalize.getData(), lDStandard.getData()
			);
			
			normalized[i] = new LineData(
				lDtoNormalize.getName(),
				normalizedValues,
				lDtoNormalize.getCoordinates()
			);
		}
		
		return createElementData(toNormalize.getName(), normalized);
	}

	private static final double[] normalize(double[] data, double[] standard) {
		final double[] normalized = new double[data.length];

		for (int i = 0; i < data.length; i++) {
			normalized[i] = data[i] / standard[i];

			if (Double.isInfinite(normalized[i])) {
				normalized[i] = Double.NaN;
			}
		}
		
		return normalized;
	}
	
	/**
	 * Normalizes {@code toNormalize} so that intensity ranges between 0 and 1.
	 * 
	 * @param toNormalize the {@code ElementData} to normalize.
	 * @return the normalized {@code ElementData}.
	 */
	public static ElementData normalize(ElementData toNormalize) {
		final LineData[] normalized = new LineData[toNormalize.getNumLines()];

		for (int i = 0; i < toNormalize.getNumLines(); i++) {
			final LineData lDtoNormalize = toNormalize.getLines()[i];
			double maxValue = toNormalize.getMaxValue();

			final double[] normalizedValues = normalize(
				lDtoNormalize.getData(), maxValue
			);
			
			normalized[i] = new LineData(
				lDtoNormalize.getName(),
				normalizedValues,
				lDtoNormalize.getCoordinates()
			);
		}
		
		ElementData createElementData = createElementData(toNormalize.getName(), normalized);
		return createElementData;
	}
	
	private static final double[] normalize(double[] data, double value) {
		final double[] normalized = new double[data.length];
		
		for (int i = 0; i < data.length; i++) {
			normalized[i] = data[i] / value;
		}
		
		return normalized;
	}
}
