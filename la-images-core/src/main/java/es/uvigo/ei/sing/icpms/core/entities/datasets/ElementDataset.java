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
package es.uvigo.ei.sing.icpms.core.entities.datasets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat;
import es.uvigo.ei.sing.icpms.core.util.ProgressHandler;

/**
 * A dataset composed by {@link ElementData}. 
 * 
 * @author hlfernandez
 *
 */
public interface ElementDataset {
	/**
	 * Return the dataset path.
	 * 
	 * @return the dataset path.
	 */
	public Path getPath();
	
	/**
	 * Return the dataset name.
	 * 
	 * @return the dataset name.
	 */
	public String getName();
	
	/**
	 * Sets the dataset name.
	 * 
	 * @param name the new dataset name.
	 */
	public void setName(String name);

	/**
	 * Return the number of {@code ElementData}.
	 * 
	 * @return the number of {@code ElementData}.
	 */
	public int getElementCount();
	
	/**
	 * Return the list of {@link ElementData}.
	 * 
	 * @return the list of {@link ElementData}.
	 */
	public List<ElementData> getElements();
	
	/**
	 * Return an {@code Optional<ElementData>} that contains the element 
	 * specified by {@code name} or it is empty if this element can't be found.
	 * 
	 * @param name the name of the element.
	 * @return an {@code Optional<ElementData>} that contains the element 
	 * 	specified by {@code name} or it is empty if this element can't be found.
	 */
	public Optional<ElementData> getElement(String name);
	
	/**
	 * Return the list of the element names.
	 * @return the list of the element names.
	 */
	public List<String> getElementNames();
	
	/**
	 * Return the {@code ElementDatasetConfiguration}.
	 * 
	 * @return the {@code ElementDatasetConfiguration}.
	 */
	public ElementDatasetConfiguration getConfiguration();
	
	/**
	 * Returns the maximum value in the dataset.
	 * 
	 * @return the maximum value in the dataset.
	 */
	public double getMinValue();
	
	/**
	 * Returns the minimum value in the dataset.
	 * 
	 * @return the minimum value in the dataset.
	 */
	public double getMaxValue();

	/**
	 * Writes {@code elementData} into a CSV file.
	 * 
	 * @param elementData the {@code ElementData} to export.
	 * @param file the file to write the data.
	 * @param format the {@code CSVFormat}.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toCSV(ElementData elementData, File file, CSVFormat format) throws IOException;

	/**
	 * Writes all the {@code ElementData} into CSV files at {@code directory}.
	 * 
	 * @param directory the directory to save the data.
	 * @param format the {@code CSVFormat}.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toCSV(File directory, CSVFormat format) throws IOException;
	
	/**
	 * Writes all the {@code ElementData} into CSV files at {@code directory}.
	 * 
	 * @param directory the directory to save the data.
	 * @param progressHandler the callback object to notify progress.
	 * @param format the {@code CSVFormat}.
	 * @throws IOException if an error occurs during the operation.
	 */
	public void toCSV(File directory, CSVFormat format, ProgressHandler progressHandler) throws IOException;
}
