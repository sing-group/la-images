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
package es.uvigo.ei.sing.icpms.core.jzy3d;

import static es.uvigo.ei.sing.icpms.core.jzy3d.CoordinatesUtils.toCoord3d;

import java.util.Optional;
import java.util.function.DoubleFunction;

import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration;

/**
 * An {@code ElementDataImageConfiguration} stores the parameters needed to
 * export an {@code ElementData} as a PNG image using {@code ShapeExporter}.
 * 
 * @author hlfernandez
 *
 */
public class ElementDataImageConfiguration {

	private int width;
	private int height;
	private Quality quality;
	private int interpolationLevel;
	private IColorMap colorMap;
	private Optional<Range> colorMapRange;
	private Coord2d viewPoint;
	private boolean showColorBarLegend;
	private boolean showAxes;
	private boolean showTickLines;
	private double scale;
	private boolean threeDimensional;

	public ElementDataImageConfiguration(int width, int height,
			Quality quality, int interpolationLevel, IColorMap colorMap,
			Optional<Range> colorMapRange, Coord2d viewPoint,
			boolean showColorBarLegend, boolean showAxes, boolean showTickLines, 
			double scale, boolean is3D
	) {
		this.width = width;
		this.height = height;
		this.quality = quality;
		this.interpolationLevel = interpolationLevel;
		this.colorMap = colorMap;
		this.colorMapRange = colorMapRange;
		this.viewPoint = viewPoint;
		this.showColorBarLegend = showColorBarLegend;
		this.showAxes = showAxes;
		this.showTickLines = showTickLines;
		this.scale = scale;
		this.threeDimensional = is3D;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Quality getQuality() {
		return quality;
	}

	public int getInterpolationLevel() {
		return interpolationLevel;
	}

	public IColorMap getColorMap() {
		return colorMap;
	}
	
	public Optional<Range> getColorMapRange() {
		return colorMapRange;
	}

	public void setColorMapRange(Optional<Range> range) {
		this.colorMapRange = range;
	}

	public Coord2d getViewPoint() {
		return viewPoint;
	}
	
	public Coord3d get3DViewPoint() {
		return toCoord3d(viewPoint);
	}

	public boolean isShowColorBarLegend() {
		return showColorBarLegend;
	}
	
	public boolean isShowAxes() {
		return showAxes;
	}
	
	public boolean isShowTickLines() {
		return showTickLines;
	}
	
	public double getScale() {
		return scale;
	}
	
	public boolean is3D() {
		return threeDimensional;
	}
	
	public boolean is2DFreeMode() {
		return !CoordinatesUtils.equalsTo(getViewPoint().x,
				ElementDataViewConfiguration.getDefault2DAngle().x) && !is3D();
	}
	
	public DoubleFunction<Double> getScaleFunction() {
		return (d -> d * (double) scale);
	}
}
