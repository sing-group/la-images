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
package es.uvigo.ei.sing.icpms.core.gui.views;

import java.io.Serializable;
import java.util.Optional;

import org.jzy3d.colors.colormaps.ColorMapGrayscale;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.ColorMapRBG;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.ColorMapRainbowNoBorder;
import org.jzy3d.colors.colormaps.ColorMapRedAndGreen;
import org.jzy3d.colors.colormaps.ColorMapWhiteBlue;
import org.jzy3d.colors.colormaps.ColorMapWhiteGreen;
import org.jzy3d.colors.colormaps.ColorMapWhiteRed;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.gui.util.DecimalFormatter;
import es.uvigo.ei.sing.icpms.core.jzy3d.Range;

/**
 * 
 * An {@code ElementDataViewConfiguration} stores the configuration used by
 * {@code ElementDataView} and {@code ElementDatasetViewer} components to 
 * represent a {@code ElementDatæ}.  
 * 
 * @author hlfernandez
 *
 */
public class ElementDataViewConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Quality QUALITY = new Quality(
			true,   // depth
			false,  // alpha
			true,   // smoothColor
			false,  // smoothPoint
			false,  // smoothLine
			false,  // smoothPoligon
			true    // disableDepthBufferWhenAlpha
		);
	
	public enum ColorMap {
		RAINBOW("Rainbow", new ColorMapRainbow()), 
		RAINBOW_NO_BORDER("Rainbow (no border)", new ColorMapRainbowNoBorder()), 
		HOT_COLD("Hot cold", new ColorMapHotCold()), 
		RGB("RGB",new ColorMapRBG()), 
		GRAY_ESCALE("Gray scale", new ColorMapGrayscale()), 
		RED_AND_GREEN("Red and green", new ColorMapRedAndGreen()), 
		WHITE_BLUE("White and blue", new ColorMapWhiteBlue()), 
		WHITE_GREEN("White and green", new ColorMapWhiteGreen()), 
		WHITE_RED("White and red", new ColorMapWhiteRed());
		
		private IColorMap colormap;
		private String name;
		
		ColorMap(String name, IColorMap cm) {
			this.name = name;
			this.colormap = cm;
		}
		
		public IColorMap getColorMap() {
			return colormap;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum ColorMapRangeMode {
		ELEMENT("Element"), 
		DATASET("Dataset"), 
		CUSTOM("Custom");
		
		private String name;
		
		ColorMapRangeMode(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum InterpolationLevel {
		NONE("None", 0), LOW("Low", 1), MEDIUM("Medium", 2), HIGH("High", 3) ;
		
		private int level;
		private String name;
		
		InterpolationLevel(String name, int level) {
			this.name = name;
			this.level = level;
		}
		
		public int getLevel() {
			return level;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static final int DEFAULT_ELEMENT_INDEX = 0;
	public static final boolean DEFAULT_IS_3D_ENABLED = false;
	public static final Number DEFAULT_SCALE = 1;
	public static final Number DEFAULT_2D_SCALE = DEFAULT_SCALE;
	public static final Number DEFAULT_3D_SCALE = DEFAULT_SCALE;
	public static final ColorMap DEFAULT_COLOR_MAP = ColorMap.RAINBOW;
	public static final ColorMapRangeMode DEFAULT_COLOR_MAP_RANGE_MODE = 
			ColorMapRangeMode.ELEMENT;
	public static final InterpolationLevel DEFAULT_INTERPOLATION = 
		InterpolationLevel.NONE;
	public static final boolean DEFAULT_SHOW_COLOR_BAR_LEGEND = true;
	public static final boolean DEFAULT_SHOW_AXES = true;
	public static final boolean DEFAULT_SHOW_TICK_LINES = true;
	public static final ViewPositionMode DEFAULT_2D_POSITION_MODE = 
		ViewPositionMode.TOP;
	public static final ViewPositionMode DEFAULT_3D_POSITION_MODE = 
		ViewPositionMode.FREE;
	public static final ViewPositionMode FREE_2D_POSITION_MODE = 
		ViewPositionMode.FREE;
	
	private int elementIndex = DEFAULT_ELEMENT_INDEX;
	private boolean is3DEnabled = DEFAULT_IS_3D_ENABLED;
	private Number scale = DEFAULT_2D_SCALE;
	private ViewPositionMode viewPositionMode2D = DEFAULT_2D_POSITION_MODE;
	private Coord2d viewPoint = getDefault2DAngle();
	private InterpolationLevel interpolationLevel = DEFAULT_INTERPOLATION;
	private ColorMap colorMap = DEFAULT_COLOR_MAP;
	private ColorMapRangeMode rangeMode = DEFAULT_COLOR_MAP_RANGE_MODE;
	private boolean showColorBarLegend = DEFAULT_SHOW_COLOR_BAR_LEGEND;
	private boolean showAxes = DEFAULT_SHOW_AXES;
	private boolean showTickLines = DEFAULT_SHOW_TICK_LINES;
	private SerializableRange colorMapRange = null;
	
	/**
	 * Constructs a new {@code ElementDataViewConfiguration} with the default
	 * values.
	 */
	public ElementDataViewConfiguration() { }

	/**
	 * Constructs a new {@code ElementDataViewConfiguration}.
	 * 
	 * @param elementIndex the index of the {@code ElementDatæ} that is being
	 * actually displayed.
	 * @param is3DEnabled {@code true} if 3D view mode is enabled.
	 * @param scale the scaling value.
	 * @param viewPoint the {@code Coord2d} that indicates the view point.
	 * @param viewPositionMode2D the {@code ViewPositionMode} used in the 2D 
	 * view mode.
	 * @param interpolationLevel the {@code InterpolationLevel} used.
	 * @param colorMap the {@code ColorMap} used.
	 * @param rangeMode the {@code ColorMapRangeMode} used.
	 * @param colorMapRange the range of the color map.
	 * @param showColorBarLegend {@code true} is color bar legend is displayed.
	 * @param showAxes {@code true} is axes are displayed.
	 * @param showTickLines {@code true} is tick lines are displayed.
	 */
	public ElementDataViewConfiguration(int elementIndex, boolean is3DEnabled,
		Number scale, Coord2d viewPoint, ViewPositionMode viewPositionMode2D,
		InterpolationLevel interpolationLevel, ColorMap colorMap,
		ColorMapRangeMode rangeMode, Range colorMapRange, 
		boolean showColorBarLegend,	boolean showAxes, boolean showTickLines
	) {
		this.elementIndex = elementIndex;
		this.is3DEnabled = is3DEnabled;
		this.scale = scale;
		this.viewPoint = viewPoint;
		this.viewPositionMode2D = viewPositionMode2D;
		this.interpolationLevel = interpolationLevel;
		this.colorMap = colorMap;
		this.rangeMode = rangeMode;
		this.colorMapRange = new SerializableRange(colorMapRange);
		this.showColorBarLegend = showColorBarLegend;
		this.showAxes = showAxes;
		this.showTickLines = showTickLines;
	}
	
	public int getElementIndex() {
		return elementIndex;
	}
	
	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}

	public boolean is3DEnabled() {
		return is3DEnabled;
	}
	
	public void set3DEnabled(boolean enabled) {
		this.is3DEnabled = enabled;
	}

	public Number getScale() {
		return (is3DEnabled() ? this.scale : DEFAULT_2D_SCALE).doubleValue();
	}
	
	public void setScale(Number scale) {
		this.scale = scale;
	}

	public ViewPositionMode getViewPositionMode2D() {
		return viewPositionMode2D;
	}
	
	public void setViewPositionMode2D(ViewPositionMode viewPositionMode2D) {
		this.viewPositionMode2D = viewPositionMode2D;
	}

	public Coord2d getViewPoint() {
		return viewPoint;
	}
	
	public void setViewPoint(Coord2d viewPoint) {
		this.viewPoint = viewPoint;
	}

	public InterpolationLevel getInterpolationLevel() {
		return interpolationLevel;
	}
	
	public void setInterpolationLevel(InterpolationLevel interpolationLevel) {
		this.interpolationLevel = interpolationLevel;
	}

	public ColorMap getColorMap() {
		return colorMap;
	}
	
	public void setColorMap(ColorMap colorMap) {
		this.colorMap = colorMap;
	}

	public ColorMapRangeMode getRangeMode() {
		return rangeMode;
	}
	
	public void setRangeMode(ColorMapRangeMode rangeMode) {
		this.rangeMode = rangeMode;
	}

	public Optional<Range> getColorMapRange() {
		return Optional.ofNullable(
			colorMapRange == null ? null : colorMapRange.getRange());
	}
	
	public void setColorMapRange(Range colorMapRange) {
		this.colorMapRange = new SerializableRange(colorMapRange);
	}
	
	private class SerializableRange implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private float min;
		private float max;

		public SerializableRange(Range range) {
			this.min = range.getMin();
			this.max = range.getMax();
		}
		
		public float getMin() {
			return min * getScale().floatValue();
		}
		
		public float getMax() {
			return max * getScale().floatValue();
		}
		
		public Range getRange() {
			return new Range(getMin(), getMax());
		}
	}
	
	public void removeColorMapRange() {
		this.colorMapRange = null;
	}
	
	public boolean isShowColorBarLegend() {
		return showColorBarLegend;
	}
	
	public void setShowColorBarLegend(boolean showColorBarLegend) {
		this.showColorBarLegend = showColorBarLegend;
	}

	public boolean isShowAxes() {
		return showAxes;
	}
	
	public void setShowAxes(boolean showAxes) {
		this.showAxes = showAxes;
	}
	
	public boolean isShowTickLines() {
		return showTickLines;
	}
	
	public void setShowTickLines(boolean showTickLines) {
		this.showTickLines = showTickLines;
	}
	
	/**
	 * Sets the default configuration.
	 */
	public void resetToDefault() {
		this.elementIndex = DEFAULT_ELEMENT_INDEX;
		this.is3DEnabled = DEFAULT_IS_3D_ENABLED;
		this.scale = DEFAULT_2D_SCALE;
		this.viewPositionMode2D = DEFAULT_2D_POSITION_MODE;
		this.viewPoint = getDefault2DAngle();
		this.interpolationLevel = DEFAULT_INTERPOLATION;
		this.colorMap = DEFAULT_COLOR_MAP;
		this.rangeMode = DEFAULT_COLOR_MAP_RANGE_MODE;
		this.colorMapRange = null;
		this.showColorBarLegend = DEFAULT_SHOW_COLOR_BAR_LEGEND;
		this.showAxes = DEFAULT_SHOW_AXES;
		this.showTickLines = DEFAULT_SHOW_TICK_LINES;
	}

	/**
	 * Returns a new {@code Coord2d} with the default 2D angle.
	 * 
	 * @return a new {@code Coord2d} with the default 2D angle.
	 */
	public static final Coord2d getDefault3DAngle() {
		return new Coord2d((double) -3*Math.PI/4, (double) Math.PI/4);
	}
	
	/**
	 * Returns a new {@code Coord3d} with the default 3D angle.
	 * 
	 * @return a new {@code Coord3d} with the default 3D angle.
	 */
	public static final Coord2d getDefault2DAngle() {
		return new Coord2d((double) -Math.PI/2, (double) Math.PI/2);
	}
	
	/**
	 * Formats {@code value} into a string with 3 decimal places.
	 * 
	 * @param value the value to format.
	 * @return the formatted string.
	 */
	public static String formatAxeValue(double value) {
		return DecimalFormatter.format(value, 3);
	}
	
	/**
	 * Returns a new {@code Range} obtained by dividing original {@code range} 
	 * by {@code element.getMaxValue()}.
	 * 
	 * @param range a {@code Range} to normalize.
	 * @param element a {@code ElementData}.
	 * @return a normalized {@code Range}
	 */
	public static final Range normalizeColorMapRange(Range range, 
		ElementData element
	) {
		return new Range(
			range.getMin() / (float) element.getMaxValue(),
			range.getMax() / (float) element.getMaxValue()
		);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ElementDataViewConfiguration)) {
			return false;
		}
		ElementDataViewConfiguration other = (ElementDataViewConfiguration) obj;
		return 	this.getElementIndex() == other.getElementIndex() &&
				this.is3DEnabled() == other.is3DEnabled() &&
				this.getScale().equals(other.getScale()) &&
				this.getViewPositionMode2D().equals(other.getViewPositionMode2D()) &&
				equalsViewPoints(getViewPoint(), other.getViewPoint()) &&
				this.getInterpolationLevel().equals(other.getInterpolationLevel()) &&
				this.getColorMap().equals(other.getColorMap()) &&
				this.getRangeMode().equals(other.getRangeMode()) &&
				this.getColorMapRange().equals(other.getColorMapRange()) &&
				this.isShowColorBarLegend() == other.isShowColorBarLegend() &&
				this.isShowAxes() == other.isShowAxes() &&
				this.isShowTickLines() == other.isShowTickLines();
	}

	private boolean equalsViewPoints(Coord2d a, Coord2d b) {
		return a.getX() == b.getX() && a.getY() == b.getY();
	}
}
