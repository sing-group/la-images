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
package es.uvigo.ei.sing.laimages.core.entities.datasets;

import static es.uvigo.ei.sing.laimages.core.util.FileNameUtils.getFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import es.uvigo.ei.sing.laimages.core.io.csv.CSVFormat;
import es.uvigo.ei.sing.laimages.core.util.ProgressHandler;

/**
 * Default implementation of an {@link ElementDataset}.
 * 
 * @author Hugo López-Fernández
 *
 */
public class DefaultElementDataset implements ElementDataset, Serializable {
	private static final long serialVersionUID = 1L;
	
	private File path;
	private String name;
	private List<ElementData> elements;
	private ElementDatasetConfiguration configuration;
	private Double maxValue;
	private Double minValue;
	
	/**
	 * Constructs a new instance of {@code DefaultElementDataset}.
	 * 
	 * @param path the dataset path.
	 * @param name the dataset name.
	 * @param configuration the dataset configuration.
	 */
	public DefaultElementDataset(Path path, String name, 
		ElementDatasetConfiguration configuration
	) {
		this.path = path.toFile();
		this.name = name;
		this.elements = new LinkedList<ElementData>();
		this.configuration = configuration;
	}
	
	/**
	 * Constructs a new instance of {@code DefaultElementDataset} copying its
	 * configuration from {@code dataset}.
	 * 
	 * @param dataset the dataset to copy its configuration.
	 */
	public DefaultElementDataset(ElementDataset dataset) {
		this.path = dataset.getPath().toFile();
		this.name = dataset.getName();
		this.elements = new LinkedList<ElementData>(dataset.getElements());
		this.configuration = dataset.getConfiguration();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getElementCount() {
		return getElements().size();
	}
	
	@Override
	public List<ElementData> getElements() {
		return elements;
	}

	@Override
	public ElementDatasetConfiguration getConfiguration() {
		return this.configuration;
	}
	
	@Override
	public double getMinValue() {
		if (minValue == null) {
			minValue = getElements().stream()
					.mapToDouble(ElementData::getMinValue).min().getAsDouble();
		}
		return minValue;
	}

	@Override
	public double getMaxValue() {
		if (maxValue == null) {
			maxValue = getElements().stream()
					.mapToDouble(ElementData::getMaxValue).max().getAsDouble();
		}
		return maxValue;
	}	
	
	public void addElement(ElementData e) {
		this.elements.add(e);
	}
	
	public void addElements(List<ElementData> e) {
		this.elements.addAll(e);
	}

	@Override
	public List<String> getElementNames() {
		return getElements().stream().map(ElementData::getName)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ElementData> getElement(String name) {
		return getElements().stream()
				.filter(e -> e.getName().equals(name)).findFirst();
	}

	@Override
	public Path getPath() {
		return this.path.toPath();
	}

	@Override
	public void toCSV(ElementData elementData, File file, CSVFormat format) 
		throws IOException {
		elementData.toCSV(file, format);
	}
	
	@Override
	public void toCSV(File directory, CSVFormat format) throws IOException {
		toCSV(directory, format, () -> {});
	}

	@Override
	public void toCSV(File directory, CSVFormat format,
			ProgressHandler progressHandler) throws IOException {
		for (ElementData e : getElements()) {
			File elementFile = getFile(directory.toString(), e.getName(),
					".csv");
			toCSV(e, elementFile, format);
			progressHandler.progress();
		}
	}
}
