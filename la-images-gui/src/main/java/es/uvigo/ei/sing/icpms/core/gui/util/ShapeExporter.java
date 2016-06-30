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
package es.uvigo.ei.sing.icpms.core.gui.util;

import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.QUALITY;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.Settings;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.canvas.OffscreenCanvas;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.ViewportMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.plot3d.text.renderers.TextBitmapRenderer;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.util.gl2.GLUT;

import es.uvigo.ei.sing.icpms.core.jzy3d.chart2d.Chart2dComponentFactory;

/**
 * Helper class for off-screen 2D/3D chart export to image files.
 * 
 * @author Miguel Reboiro-Jato
 * @author Hugo López-Fernández
 *
 */
public final class ShapeExporter {
	private ShapeExporter() {}
	
	/**
	 * Exports a chart to a image file.
	 * 
	 * @param surface surface of the chart to export.
	 * @param is3D whether the surface is 3D or not.
	 * @param quality quality of the chart.
	 * @param size width and height of the image generated.
	 * @param imageFile file where the image will be stored. The extension of
	 * this file will determine the format of the image generated.
	 * @throws IOException if an error happens while exporting the chart.
	 */
	public static void exportShape(
		Shape surface, boolean is3D, Quality quality, int size, File imageFile
	) throws IOException {
		exportShape(surface, is3D, quality, size, size, e -> {}, imageFile);
	}
	
	/**
	 * Exports a chart to a image file.
	 * 
	 * @param surface surface of the chart to export.
	 * @param is3D whether the surface is 3D or not.
	 * @param quality quality of the chart.
	 * @param width width of the image generated.
	 * @param height height of the image generated.
	 * @param imageFile file where the image will be stored. The extension of
	 * this file will determine the format of the image generated.
	 * @throws IOException if an error happens while exporting the chart.
	 */
	public static void exportShape(
		Shape surface, boolean is3D, Quality quality, int width, int height, File imageFile
	) throws IOException {
		exportShape(surface, is3D, quality, width, height, e -> {}, imageFile);
	}
	
	/**
	 * Exports a chart to a image file.
	 * 
	 * @param surface surface of the chart to export.
	 * @param is3D whether the surface is 3D or not.
	 * @param quality quality of the chart.
	 * @param size width and height of the image generated.
	 * @param chartConfigurer function to configure the chart before exporting
	 * it to a file. This function is called after the chart is created and
	 * configured as a 2D chart and before the chart is exported to an image
	 * file.
	 * @param imageFile file where the image will be stored. The extension of
	 * this file will determine the format of the image generated.
	 * @throws IOException if an error happens while exporting the chart.
	 */
	public static void exportShape(
		Shape surface, boolean is3D, Quality quality, int size,
		Consumer<Chart> chartConfigurer,
		File imageFile
	) throws IOException {
		exportShape(surface, is3D, quality, size, size, chartConfigurer, imageFile);
	}
	
	/**
	 * Exports a chart to a image file.
	 * 
	 * @param surface surface of the chart to export.
	 * @param is3D whether the surface is 3D or not.
	 * @param quality quality of the chart.
	 * @param width width of the image generated.
	 * @param height height of the image generated.
	 * @param chartConfigurer function to configure the chart before exporting
	 * it to a file. This function is called after the chart is created and
	 * configured as a 2D chart and before the chart is exported to an image
	 * file.
	 * @param imageFile file where the image will be stored. The extension of
	 * this file will determine the format of the image generated.
	 * @throws IOException if an error happens while exporting the chart.
	 */
	public static void exportShape(
		Shape surface, boolean is3D, Quality quality,
		int width, int height,
		Consumer<Chart> chartConfigurer,
		File imageFile
	) throws IOException {
		final Chart chart = 
			new Chart(getChartComponentFactory(is3D, width, height), quality);
		chart.getScene().getGraph().add(surface);

        final IAxeLayout axe = chart.getAxeLayout();
        axe.setZAxeLabelDisplayed(true);
        
        final View view = chart.getView();
        view.setViewPositionMode(ViewPositionMode.TOP);
        view.setSquared(true);
        view.getCamera().setViewportMode(ViewportMode.STRETCH_TO_FILL);

        adjustFontSize(view, width, height);

        chartConfigurer.accept(chart);

        chart.screenshot(imageFile);
        
        ImageIO.write(ImageIO.read(imageFile), "png", imageFile);
	}
	
	private static IChartComponentFactory getChartComponentFactory(
		boolean is3d, int width, int height) 
	{
		if (is3d) {
			return new OffsetChart3dComponentFactory(width, height);
		} else {
			return new OffsetChart2dComponentFactory(width, height);
		}
	}

	private static void adjustFontSize(View view, int width, int height) {
		final AxeBox axeBox = (AxeBox) view.getAxe();
		if (width >= 1200 && height >= 1200) {
			axeBox.setTextRenderer(new TextBitmapRenderer() {
				{
					font = GLUT.BITMAP_HELVETICA_18;
					fontHeight = 18;
				}
			});
		}
	}
	
	private static final class OffsetChart2dComponentFactory
	extends Chart2dComponentFactory {
		private final int width;
		private final int height;

		private OffsetChart2dComponentFactory(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public ICanvas newCanvas(
			IChartComponentFactory factory,
			Scene scene, Quality quality, 
			String windowingToolkit, GLCapabilities capabilities
		) {
			return new OffscreenCanvas(
				factory, scene, QUALITY,
				Settings.getInstance().getGLCapabilities(),
				this.width, this.height
			);
		}

		@Override
		public ICanvas newCanvas(
			Scene scene, Quality quality,
			String windowingToolkit, GLCapabilities capabilities
		) {
			return newCanvas(this, scene, quality, windowingToolkit, capabilities);
		}
	}
	
	private static final class OffsetChart3dComponentFactory
	extends AWTChartComponentFactory {
		private final int width;
		private final int height;
		
		private OffsetChart3dComponentFactory(int width, int height) {
			this.width = width;
			this.height = height;
		}
		
		public ICanvas newCanvas(
			IChartComponentFactory factory,
			Scene scene, Quality quality, 
			String windowingToolkit, GLCapabilities capabilities
		) {
			return new OffscreenCanvas(
					factory, scene, QUALITY,
					Settings.getInstance().getGLCapabilities(),
					this.width, this.height
				);
		}
		
		@Override
		public ICanvas newCanvas(
			Scene scene, Quality quality,
			String windowingToolkit, GLCapabilities capabilities
		) {
			return newCanvas(this, scene, quality, windowingToolkit, capabilities);
		}
	}
}
