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
package es.uvigo.ei.sing.laimages.gui.jzy3d;

import static es.uvigo.ei.sing.laimages.core.operations.Interpolator.interpolate;
import static es.uvigo.ei.sing.laimages.core.operations.ScaleElementData.scale;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.primitives.Shape;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;

/**
 * A class that adapts an {@code ElementData} into a {@code Shape}.
 * 
 * @author Hugo López-Fernández
 *
 */
public class ElementDataSurfaceAdapter {

	private ElementData data;

	/**
	 * Constructs a new {@code ElementDataSurfaceAdapter}.
	 * 
	 * @param data the {@code ElementData} to adapt.
	 */
	public ElementDataSurfaceAdapter(ElementData data) {
		this.data = data;
	}
	
	/**
	 * Generates a {@code Shape} to represent the data using the specified {@code interpolationLevel} and {@code colormap}.
	 * 
	 * @param interpolationLevel an integer specifying the interpolation level to use.
	 * @param colorMap an {@code IColorMap} to configure the surface.
	 * @param colorMapRange a {@code Range} to create the surface color mapper.
	 * @param scale a {@code DoubleFunction<Double>} to scale the data.
	 * @return a new {@code Shape} with the surface for this data.
	 */
	public Shape generateSurface(int interpolationLevel, IColorMap colorMap, 
		Range colorMapRange, DoubleFunction<Double> scale
	) {
		final List<Coord3d> coords = dataToCoord3d(
			scale(
				scale,
				interpolate(data, interpolationLevel)
			)
		);
		
		final Shape surface = Builder.buildDelaunay(coords);
		surface.setColorMapper(new ColorMapper(colorMap, colorMapRange));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(false);
		return surface;
	}
	
	private final static List<Coord3d> dataToCoord3d(ElementData data) {
		final List<Coord3d> coords = new ArrayList<>();
		
		double[][] dataMatrix = data.getData(0.0d);
		List<Double> xAxis = data.getXAxis();
		List<Double> yAxis = data.getYAxis();
		for(int xIndex = 0; xIndex < xAxis.size(); xIndex++) {
			for(int yIndex = 0; yIndex < yAxis.size(); yIndex++) {
				coords.add(
					new Coord3d(
						xAxis.get(xIndex), 
						yAxis.get(yIndex), 
						dataMatrix[yIndex][xIndex]
					)
				);
			}
		}
		
		return coords;
	}
	
	public final static Range getElementRange(ElementData data, 
		DoubleFunction<Double> scale
	) {
		return new Range(
			(float) scale.apply(data.getMinValue()).doubleValue(), 
			(float) scale.apply(data.getMaxValue()).doubleValue()
		);
	}
}
