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
package es.uvigo.ei.sing.icpms.core.operations;

import java.util.List;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.LineData;

/**
 * A class that helps scaling an {@code ElementData} applying a given scale
 * function.
 * 
 * @author hlfernandez
 *
 */
public class ScaleElementData {

	/**
	 * Scales the list of {@code ElementData} contained in {@code toScale} 
	 * applying the {@code scale} function.
	 * 
	 * @param scale a {@code DoubleFunction<Double>}.
	 * @param toScale the list of {@code ElementData} to scale.
	 * @return the scaled {@code ElementData}.
	 */
	public static List<ElementData> scale(DoubleFunction<Double> scale, 
		List<ElementData> toScale
	) {
		return toScale.stream()
				.map(data -> scale(scale, data))
				.collect(Collectors.toList());
	}
	
	/**
	 * Scales the list of {@code ElementData} contained in {@code toScale} 
	 * applying the {@code scale} function.
	 * 
	 * @param scale a {@code DoubleFunction<Double>}.
	 * @param toScale the list of {@code ElementData} to scale.
	 * @return the scaled {@code ElementData}.
	 */
	public static ElementData[] scale(DoubleFunction<Double> scale, 
		ElementData ... toScale
	) {
		return Stream.of(toScale)
			.map(data -> scale(scale, data))
		.toArray(ElementData[]::new);
	}
	
	/**
	 * Scales the {@code toScale} element applying the {@code scale} function.
	 * 
	 * @param scale a {@code DoubleFunction<Double>}.
	 * @param toScale the {@code ElementData} to scale.
	 * @return the scaled {@code ElementData}.
	 */
	public static ElementData scale(DoubleFunction<Double> scale, 
		ElementData toScale
	) {
		final LineData[] scaled = new LineData[toScale.getNumLines()];
		
		for (int i = 0; i < toScale.getNumLines(); i++) {
			LineData lDtoScale = toScale.getLines()[i];
			double[] scaledValues = scale(lDtoScale.getData(), scale);
			scaled[i] = new LineData(lDtoScale.getName(), scaledValues, 
				lDtoScale.getCoordinates());
		}
		return ElementData.createElementData(toScale.getName(), scaled);
	}

	private static final double[] scale(double[] data, DoubleFunction<Double> scale) {
		double[] scaled = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			scaled[i] = scale.apply(data[i]);
		}
		return scaled;
	}
}
