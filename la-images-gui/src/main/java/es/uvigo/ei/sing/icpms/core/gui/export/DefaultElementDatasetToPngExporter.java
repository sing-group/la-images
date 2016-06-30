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
package es.uvigo.ei.sing.icpms.core.gui.export;

import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.DEFAULT_2D_POSITION_MODE;
import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.DEFAULT_3D_POSITION_MODE;
import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.FREE_2D_POSITION_MODE;
import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.normalizeColorMapRange;
import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.formatAxeValue;
import static es.uvigo.ei.sing.icpms.core.jzy3d.ElementDataSurfaceAdapter.getElementRange;
import static es.uvigo.ei.sing.icpms.core.util.FileNameUtils.getFile;

import java.io.File;
import java.io.IOException;

import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ITickRenderer;
import org.jzy3d.plot3d.rendering.legends.colorbars.AWTColorbarLegend;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.icpms.core.gui.util.ShapeExporter;
import es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration;
import es.uvigo.ei.sing.icpms.core.jzy3d.ElementDataImageConfiguration;
import es.uvigo.ei.sing.icpms.core.jzy3d.ElementDataSurfaceAdapter;
import es.uvigo.ei.sing.icpms.core.jzy3d.Range;
import es.uvigo.ei.sing.icpms.core.operations.NormalizeElementData;
import es.uvigo.ei.sing.icpms.core.util.ProgressHandler;

public class DefaultElementDatasetToPngExporter implements ElementDatasetToPngExporter {
	@Override
	public void toPNG(ElementData data, File file, ElementDataImageConfiguration configuration)
	throws IOException {
		ElementData normalizedData = NormalizeElementData.normalize(data);
		
		ElementDataSurfaceAdapter surfaceAdapter = 
				new ElementDataSurfaceAdapter(normalizedData);
		final Shape surface = surfaceAdapter.generateSurface(
			configuration.getInterpolationLevel(),
			configuration.getColorMap(),
			normalizeColorMapRange(getColorMapRange(data, configuration), data),
			configuration.getScaleFunction()
		);
		
		ShapeExporter.exportShape(
			surface,
			configuration.is3D(),
			configuration.getQuality(),
			configuration.getWidth(),
			configuration.getHeight(),
			c -> {
				if (configuration.is3D()) {
					c.setViewMode(DEFAULT_3D_POSITION_MODE);
					c.setViewPoint(configuration.get3DViewPoint());
				} else if (configuration.is2DFreeMode()) {
						c.setViewMode(FREE_2D_POSITION_MODE);
						c.setViewPoint(configuration.get3DViewPoint());
					} else {
						c.setViewMode(DEFAULT_2D_POSITION_MODE);
					}
				c.setAxeDisplayed(configuration.isShowAxes());
				c.getAxeLayout().setTickLineDisplayed(configuration.isShowTickLines());
				c.getAxeLayout().setZAxeLabelDisplayed(configuration.is3D());
				c.getAxeLayout().setZTickLabelDisplayed(configuration.is3D());
				c.getView().setSquared(false);
				ITickRenderer tickRenderer = ElementDataViewConfiguration::formatAxeValue;
				c.getAxeLayout().setXTickRenderer(tickRenderer);
				c.getAxeLayout().setYTickRenderer(tickRenderer);
				c.getAxeLayout().setZTickRenderer( value -> 
					formatAxeValue(value * data.getMaxValue() / configuration.getScale())
				);
				if (configuration.isShowColorBarLegend()) {
					surface.setLegend(new AWTColorbarLegend(surface, c
							.getView().getAxe().getLayout()));
				}
			}, file
		);
	}

	@Override
	public void toPNG(ElementDataset dataset, File directory,
		ElementDataImageConfiguration configuration
	) throws IOException {
		toPNG(dataset, directory, configuration, () -> {});
	}

	@Override
	public void toPNG(ElementDataset dataset, File directory,
		ElementDataImageConfiguration configuration,
		ProgressHandler progressHandler
	) throws IOException {
		for (ElementData e : dataset.getElements()) {
			File elementFile = getFile(directory.toString(), e.getName(), ".png");
			toPNG(e, elementFile, configuration);
			progressHandler.progress();
		}
	}

	private Range getColorMapRange(ElementData data,
		ElementDataImageConfiguration configuration
	) {
		Range colorMapRange;
		boolean useElementRange = !configuration.getColorMapRange().isPresent();
		if(useElementRange) {
			colorMapRange = getElementRange(data, configuration.getScaleFunction());
		} else {
			colorMapRange = configuration.getColorMapRange().get();
		}
		return colorMapRange;
	}
}
