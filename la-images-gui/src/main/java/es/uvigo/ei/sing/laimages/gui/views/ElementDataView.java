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
package es.uvigo.ei.sing.laimages.gui.views;

import static es.uvigo.ei.sing.laimages.gui.jzy3d.CoordinatesUtils.equalsTo;
import static es.uvigo.ei.sing.laimages.gui.jzy3d.CoordinatesUtils.toCoord3d;
import static es.uvigo.ei.sing.laimages.gui.jzy3d.ElementDataSurfaceAdapter.getElementRange;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_2D_POSITION_MODE;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_3D_POSITION_MODE;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.FREE_2D_POSITION_MODE;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.QUALITY;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.formatAxeValue;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.getDefault2DAngle;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.getDefault3DAngle;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.normalizeColorMapRange;
import static java.util.Objects.requireNonNull;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.events.IViewPointChangedListener;
import org.jzy3d.events.ViewPointChangedEvent;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ITickRenderer;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;

import com.jogamp.newt.event.MouseEvent;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.laimages.core.operations.NormalizeElementData;
import es.uvigo.ei.sing.laimages.gui.jzy3d.ElementDataSurfaceAdapter;
import es.uvigo.ei.sing.laimages.gui.jzy3d.Range;
import es.uvigo.ei.sing.laimages.gui.jzy3d.chart2d.Chart2dComponentFactory;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMap;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.InterpolationLevel;

/**
 * An {@code ElementDataView} is an object that returns a {@code ICanvas} with
 * the graphical representation of a given {@code ElementData}. This class
 * facilitates the creation of this representation as well as offers methods to
 * configure the display settings.
 * 
 * @author Hugo López-Fernández
 *
 */
public class ElementDataView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final double ROTATION_360_NEG = -7.853983;
	private static final double ROTATION_360_POS = 4.712389;

	private static final ITickRenderer TICK_RENDERER = ElementDataViewConfiguration::formatAxeValue;
			
	private static final String CARD_CHART_RENDERING = "CARD_CHART_RENDERING";
	private static final String CARD_CHART = "CARD_CHART";

	private CardLayout cardLayout;
	private JPanel renderingPanel;
	private Chart chart;
	private Component currentCanvas;
	private Shape surface;
	
	private IViewPointChangedListener viewPointChangedListener;
	private final NewtCameraMouseController customCameraController = 
		new CustomNewtCameraMouseController();
	
	private ElementData element;
	private ElementData normalizedElement;
	private ElementDataViewConfiguration configuration;
	private ElementDataSurfaceAdapter surfaceAdapter;
	
	private Coord3d current3DAngle = toCoord3d(getDefault3DAngle());
	private Coord3d current2DAngle = toCoord3d(getDefault2DAngle());
	
	/**
	 * Constructs an {@code ElementDataView} object and renders it using the
	 * configuration specified by the {@code ElementDataViewConfiguratioṇ} 
	 * object. 
	 * 
	 * @param element the {@code ElementData} to display. Can't be {@code null}.
	 * @param configuration the {@code ElementDataViewConfiguration} to apply. 
	 * Can't be {@code null}.
	 * @param viewPointChangedListener a {@code IViewPointChangedListener} to 
	 * 	add to the chart. Can't be {@code null}.
	 */
	public ElementDataView(ElementData element, 
		ElementDataViewConfiguration configuration,
		IViewPointChangedListener viewPointChangedListener
	) {
		this.element = requireNonNull(element);
		this.normalizedElement = NormalizeElementData.normalize(requireNonNull(element));
		this.viewPointChangedListener = requireNonNull(viewPointChangedListener);
		this.setInitialConfiguration(requireNonNull(configuration));
		this.chart = null;
		this.updateSurfaceAdapter();
		this.init();
	}
	
	private void setInitialConfiguration(ElementDataViewConfiguration configuration) {
		this.configuration = configuration;
		this.initCurrentAngle();		
	}
	
	private void initCurrentAngle() {
		if(this.configuration.is3DEnabled()) {
			this.current3DAngle = toCoord3d(this.configuration.getViewPoint());
		} else {
			this.current2DAngle = toCoord3d(this.configuration.getViewPoint());
		}
	}

	private void updateSurfaceAdapter() {
		this.surfaceAdapter = new ElementDataSurfaceAdapter(this.normalizedElement);
	}

	private void init() {
		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		this.add(getRenderingPanel(), CARD_CHART_RENDERING);
		addChart();
	}

	private void addChart() {
		currentCanvas = (Component) this.getCanvas();
		this.add(currentCanvas, CARD_CHART);
		showChart();
	}

	private void showChart() {
		cardLayout.show(this, CARD_CHART);
	}

	private Component getRenderingPanel() {
		if (renderingPanel == null) {
			renderingPanel = new JPanel();
			renderingPanel.setLayout(new BoxLayout(renderingPanel,
				BoxLayout.X_AXIS));
			renderingPanel.add(Box.createHorizontalGlue());
			JLabel renderingLbl = new JLabel("Rendering chart...");
			renderingLbl.setFont(renderingLbl.getFont().deriveFont(Font.BOLD));
			renderingPanel.add(renderingLbl);
			renderingPanel.add(Box.createHorizontalGlue());
		}
		return renderingPanel;
	}
	
	/**
	 * Return an {@code ICanvas} object with the element displayed.
	 * 
	 * @return an {@code ICanvas} object with the element displayed.
	 */
	public ICanvas getCanvas() {
		return getChart().getCanvas();
	}

	private synchronized Chart getChart() {
		if(chart == null) {
			surface = surfaceAdapter.generateSurface(getInterpolation().getLevel(),
				getColorMap(), getNormalizedColorMapRange(), 
				d -> d* getScale());
			if (is3DEnabled()) {
				chart = AWTChartComponentFactory.chart(QUALITY, "newt");
			} else {
				chart = Chart2dComponentFactory.chart(QUALITY, "newt");
			}
			chart.getScene().getGraph().add(surface);
			chart.getView().setSquared(false);
			chart.setAxeDisplayed(isShowAxes());
			chart.getAxeLayout().setTickLineDisplayed(isShowTickLines());
			chart.getAxeLayout().setXTickRenderer(TICK_RENDERER);
			chart.getAxeLayout().setYTickRenderer(TICK_RENDERER);
			chart.getAxeLayout().setZTickRenderer(this::formatZTick);
			chart.getView().addViewPointChangedListener(viewPointChangedListener);
			setColorBarLegend();
			_updateViewMode();
			chart.pauseAnimator();
		}
		return chart;
	}
	
	private String formatZTick(double value) {
		return formatAxeValue(value * (this.element.getMaxValue()) / getScale());
	}
	
	public double getScale() {
		return configuration.getScale().doubleValue();
	}

	public Range getNormalizedColorMapRange() {
		return normalizeColorMapRange(getColorMapRange(), this.element);
	}
	
	public Range getColorMapRange() {
		if(!this.configuration.getColorMapRange().isPresent()) {
			return getElementRange(element, d -> d * getScale()); 
		} else {
			return this.configuration.getColorMapRange().get();
		}
	}	

	private void setColorBarLegend() {
		if (isShowColorBarLegend()) {
			surface.setLegend(new AWTColorbarLegend(surface, chart
				.getView().getAxe().getLayout()));
		}
	}

	/**
	 * Sets the {@code ElementData} to display.
	 * 
	 * @param elementData the {@code ElementData} to display.
	 */
	public void setElementData(ElementData elementData) {
		if(!this.normalizedElement.equals(elementData)) {
			this.element = elementData;
			this.normalizedElement = NormalizeElementData.normalize(elementData);
			updateSurfaceAdapter();
			updateChart();
		}
	}

	/**
	 * Sets the {@code InterpolationLevel} to use.
	 * 
	 * @param interpolation the {@code InterpolationLevel} to use.
	 */
	public void setInterpolation(InterpolationLevel interpolation) {
		if(!this.getInterpolation().equals(interpolation)) {
			this.configuration.setInterpolationLevel(interpolation);
			updateChart();
		}
	}
	/**
	 * Sets the scale value.
	 * 
	 * @param scale the new scale value.
	 */
	public void setScale(Number scale) {
		if(!this.configuration.getScale().equals(scale)) {
			this.configuration.setScale(scale);
			this.updateChart();
		}
	}

	/**
	 * Sets the 3D mode view enabled.
	 * 
	 * @param enabled If true, enables 3D view.
	 */
	public void set3DEnabled(boolean enabled) {
		if(enabled != this.is3DEnabled()) {
			configuration.set3DEnabled(enabled);
			updateViewMode();
			SwingUtilities.invokeLater(this::updateChart);
		}
	}
	
	private final void updateViewMode() {
		showChartRendering();
		SwingUtilities.invokeLater(this::_updateViewMode);
	}
	
	private void showChartRendering() {
		cardLayout.show(this, CARD_CHART_RENDERING);
	}

	private void _updateViewMode() {
		configureViewPoint();
		showChart();
	}

	private void configureViewPoint() {
		if (is3DEnabled()) {
			this.chart.setViewMode(DEFAULT_3D_POSITION_MODE);
			this.chart.setViewPoint(current3DAngle);
		} else {
			this.chart.setViewMode(getCurrent2DPositionMode());
			if (is2DFreeModeEnabled()) {
				this.chart.setViewPoint(current2DAngle);
			}
		}
		updateCurrentAngle();
		addChartControllers();
	}

	private ViewPositionMode getCurrent2DPositionMode() {
		return this.configuration.getViewPositionMode2D();
	}

	private void addChartControllers() {
		if (is3DEnabled()) {
			this.chart.addController(this.customCameraController);
		} else {
			this.chart.removeController(this.customCameraController);
		}
	}

	public Coord2d getCurrentViewPoint() {
		return getChart().getViewPoint().getXY();
	}
	
	/**
	 * Rotates the view by the {@code angle} specified. If {@code angle} is 
	 * equals to new Coord2d(0, 0), then the original view point is reestablished. 
	 * 
	 * @param angle a {@code Coord2d} object specifying the rotation angle.
	 */
	public void rotateViewPoint(Coord2d angle) {
		if(angle.getX() == 0 && angle.getY() == 0) {
			setDefaultViewPositionMode();
		} else {
			if (!is3DEnabled() && !is2DFreeModeEnabled()) {
				enable2DFreeModeAndRotateViewPoint(angle);
			} else {
				_rotateViewPoint(angle);
			}
		}
	}
	
	private void _rotateViewPoint(Coord2d angle) {
		this.chart.getCanvas().getView().rotate(angle, true);
		if(shouldReset2DView()) {
			setDefaultViewPositionMode();
		} else {
			updateCurrentAngle();
		}
	}

	private boolean shouldReset2DView() {
				
		return !is3DEnabled() && (
				equalsTo(getChart().getViewPoint().x, getDefault2DAngle().x) ||
				equalsTo(getChart().getViewPoint().x, ROTATION_360_POS) ||
				equalsTo(getChart().getViewPoint().x, ROTATION_360_NEG));
	}

	private void enable2DFreeModeAndRotateViewPoint(Coord2d angle) {
		showChartRendering();
		set2DFreeMode();
		SwingUtilities.invokeLater(() -> {
			_rotateViewPoint(angle);
			showChart();
		});
	}

	private void updateCurrentAngle() {
		if (is3DEnabled()) {
			this.current3DAngle = this.chart.getViewPoint();
			fireViewPointChangedEvent(this.current3DAngle);
		} else {
			if (is2DFreeModeEnabled()) {
				this.current2DAngle = this.chart.getViewPoint();
			} else {
				this.current2DAngle = toCoord3d(getDefault2DAngle());
			}
			fireViewPointChangedEvent(this.current2DAngle);
		}
	}

	private void fireViewPointChangedEvent(Coord3d newAngle) {
		this.configuration.setViewPoint(newAngle.getXY());
		viewPointChangedListener.viewPointChanged(
			new ViewPointChangedEvent(this, newAngle));
	}

	private void setDefaultViewPositionMode() {
		if(is3DEnabled()) {
			current3DAngle = toCoord3d(getDefault3DAngle());
		} else {
			this.configuration.setViewPositionMode2D(DEFAULT_2D_POSITION_MODE);
			current2DAngle = toCoord3d(getDefault2DAngle());
		}
		configureViewPoint();
	}

	private boolean is2DFreeModeEnabled() {
		return this.chart.getViewMode().equals(FREE_2D_POSITION_MODE);
	}

	private void set2DFreeMode() {
		this.configuration.setViewPositionMode2D(FREE_2D_POSITION_MODE);
		
		this.chart.setViewMode(getCurrent2DPositionMode());
		this.chart.setViewPoint(toCoord3d(getDefault2DAngle()));
		this.chart.getAxeLayout().setZAxeLabelDisplayed(false);
		this.chart.getAxeLayout().setZTickLabelDisplayed(false);
	}

	private boolean is3DEnabled() {
		return configuration.is3DEnabled();
	}
	
	private void updateChart() { 
		showChartRendering();
		remove(currentCanvas);
		this.chart.dispose();
		this.chart = null;
		SwingUtilities.invokeLater(this::addChart);
	}

	private void updateColorMap() { 
		showChartRendering();
		SwingUtilities.invokeLater(this::_updateColorMap);
	}
	
	private void _updateColorMap() {
		this.surface.setColorMapper(
			new ColorMapper(
				this.getColorMap(), getNormalizedColorMapRange()
			)
		);
		setColorBarLegend();
		showChart();
	}

	public void setShowColorBarLegend(boolean show) {
		if (show != this.configuration.isShowColorBarLegend()) {
			this.configuration.setShowColorBarLegend(show);
			updateChart();
		}
	}
	
	public void setShowAxes(boolean show) {
		if (show != this.configuration.isShowAxes()) {
			this.configuration.setShowAxes(show);
			getChart().setAxeDisplayed(this.isShowAxes());
		}
	}
	
	public void setShowTickLines(boolean show) {
		if (show != this.configuration.isShowTickLines()) {
			this.configuration.setShowTickLines(show);
			getChart().getAxeLayout().setTickLineDisplayed(this.isShowTickLines());
			getChart().setAxeDisplayed(this.isShowAxes());
		}
	}
	
	public boolean isShowTickLines() {
		return this.configuration.isShowTickLines();
	}

	public boolean isShowColorBarLegend() {
		return this.configuration.isShowColorBarLegend();
	}
	
	public boolean isShowAxes() {
		return this.configuration.isShowAxes();
	}
	
	public int getInterpolationLevel() {
		return getInterpolation().getLevel();
	}

	public IColorMap getColorMap() {
		return getColorMapType().getColorMap();
	}
	
	public ColorMap getColorMapType() {
		return configuration.getColorMap();
	}

	public void setColorMap(ColorMap colorMap) {
		if(!this.getColorMapType().equals(colorMap)) {
			this.configuration.setColorMap(colorMap);
			updateColorMap();
		}
	}
	
	/**
	 * Return the chart {@code Quality}.
	 * 
	 * @return the chart {@code Quality}.
	 */
	public Quality getQuality() {
		return QUALITY;
	}
	
	public void setColorMapRange(Range range) {
		if(!getColorMapRange().equals(range)) {
			this.configuration.setColorMapRange(range);
			updateColorMapRange();
		} else {
			if(!this.configuration.getColorMapRange().isPresent()) {
				this.configuration.setColorMapRange(range);
			}
		}
	}

	/**
	 * Removes the color map range and updates the chart.
	 */
	public void removeColorMapRange() {
		if(this.configuration.getColorMapRange().isPresent()) {
			this.configuration.removeColorMapRange();
			updateColorMapRange();
		}
	}
	
	private void updateColorMapRange() {
		showChartRendering();
		SwingUtilities.invokeLater(this::_updateColorMapRange);
	}

	private void _updateColorMapRange() {
		this.surface.getColorMapper().setRange(getNormalizedColorMapRange());
		setColorBarLegend();
		showChart();
	}
	
	/**
	 * Returns the current {@code ElementDataViewConfiguration}.
	 * 
	 * @return the current {@code ElementDataViewConfiguration}.
	 */
	public ElementDataViewConfiguration getViewConfiguration() {
		return this.configuration;
	}
	
	/**
	 * Sets the default settings and updates the chart.
	 */
	public void setDefaultSettings() {
		this.configuration.resetToDefault();
		this.current2DAngle = toCoord3d(getDefault2DAngle());
		updateChart();
	}
	
	private InterpolationLevel getInterpolation() {
		return this.configuration.getInterpolationLevel();
	}
	
	/**
	 * A custom {@code NewtCameraMouseController} to disable mouse wheel events.
	 * 
	 * @author Hugo López-Fernández
	 *
	 */
	class CustomNewtCameraMouseController extends NewtCameraMouseController {
		
		@Override
		public void mouseWheelMoved(MouseEvent e) {
			/*
			 * Ignore mouse wheel
			 */
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			getChart().resumeAnimator();
			super.mousePressed(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			getChart().pauseAnimator();
		}
	}
}
