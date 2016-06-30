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

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.icpms.core.jzy3d.ElementDataImageConfiguration;
import es.uvigo.ei.sing.icpms.core.util.ProgressHandler;

public interface ElementDatasetToPngExporter {
	
	/**
	 * Writes {@code elementData} into a PNG file.
	 * 
	 * @param data the {@code ElementData} to write.
	 * @param file the file to write the data.
	 * @param configuration the image configuration.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toPNG(ElementData data, File file,
			ElementDataImageConfiguration configuration) throws IOException;

	/**
	 * Writes all the {@code ElementData} into PNG files at {@code directory}.
	 * 
	 * @param dataset the dataset to export.
	 * @param directory the directory to save the data.
	 * @param configuration the image configuration.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toPNG(ElementDataset dataset, File directory,
			ElementDataImageConfiguration configuration) throws IOException;

	/**
	 * Writes all the {@code ElementData} into PNG files at {@code directory}.
	 * 
	 * @param dataset the dataset to export.
	 * @param directory the directory to save the data.
	 * @param configuration the image configuration.
	 * @param progressHandler the callback object to notify progress.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toPNG(ElementDataset dataset, File directory,
			ElementDataImageConfiguration configuration,
			ProgressHandler progressHandler) throws IOException;

}
