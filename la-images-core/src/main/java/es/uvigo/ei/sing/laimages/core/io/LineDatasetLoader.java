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
package es.uvigo.ei.sing.laimages.core.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.uvigo.ei.sing.laimages.core.entities.datasets.DefaultElementDataset;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDatasetConfiguration;
import es.uvigo.ei.sing.laimages.core.entities.datasets.LineData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinatesUtils;
import es.uvigo.ei.sing.laimages.core.io.exception.NoSuchStandardElementException;
import es.uvigo.ei.sing.laimages.core.io.exception.PositionsFileNotFoundException;
import es.uvigo.ei.sing.laimages.core.operations.NormalizeElementData;
import es.uvigo.ei.sing.laimages.core.util.ProgressHandler;

/**
 * A dataset loader that loads {@code ElementData} elements from files.
 * 
 * @author Hugo López-Fernández
 *
 */
public class LineDatasetLoader {

	private static final String[] extensions = new String[]{"xl"};
	private final FilenameFilter fileFilter;
	private final ElementDatasetConfiguration configuration;

	/**
	 * Constructs a new instance of {@code LineDatasetLoader}.
	 * 
	 * @param configuration the dataste configuration.
	 */
	public LineDatasetLoader(ElementDatasetConfiguration configuration) {
		this.fileFilter = new ExtensionFilenameFilter(extensions);
		this.configuration = configuration;
	}
	
	private final static class ExtensionFilenameFilter
	implements FilenameFilter, Serializable {
		private static final long serialVersionUID = 1L;
		
		private final String[] extensions;
		
		public ExtensionFilenameFilter(String ... extensions) {
			super();
			this.extensions = extensions;
		}

		@Override
		public boolean accept(File dir, String name) {
			return Stream.of(extensions)
					.filter(e -> name.toLowerCase().endsWith(e)).count() > 0;
		}
	}

	/**
	 * Loads an {@code ElementDataset} from a given {@code path} and normalizes
	 * all the elements by the standard element. Note that the standard element
	 * is not loaded.Line's coordinates are also normalized so that:
	 * <ul>
	 * <li>Minimum values of both line's position and range are zero.</li>
	 * <li>Lines' order is given by files' order. This means that the first line
	 * is located at position 0 and next lines are separated by the corresponding
	 * position interval (i.e.: the space interval).</li>
	 * </ul>
	 * 
	 * @param path the path where the dataset is stored.
	 * @return an {@code ElementDataset}.
	 * 
	 * @throws IOException If there is some problem reading the dataset.
	 * @throws NoSuchStandardElementException If the standard element is not 
	 * 	present in the dataset.
	 * @throws PositionsFileNotFoundException If the positions file is not 
	 * 	located at the dataset directory.
	 */
	public ElementDataset loadAndNormalizeDataset(Path path)
			throws IOException, NoSuchStandardElementException,
			PositionsFileNotFoundException 
	{
		return loadAndNormalizeDataset(path, () -> {});
	}
	
	/**
	 * Loads an {@code ElementDataset} from a given {@code path} and normalizes
	 * all the elements by the standard element. Note that the standard element
	 * is not loaded. Line's coordinates are also normalized so that:
	 * <ul>
	 * <li>Minimum values of both line's position and range are zero.</li>
	 * <li>Lines' order is given by files' order. This means that the first line
	 * is located at position 0 and next lines are separated by the corresponding
	 * position interval (i.e.: the space interval).</li>
	 * </ul>
	 * 
	 * @param path the path where the dataset is stored.
	 * @param progressHandler a {@code ProgressHandler} object.
	 * @return an {@code ElementDataset}.
	 * 
	 * @throws IOException If there is some problem reading the dataset.
	 * @throws NoSuchStandardElementException If the standard element is not 
	 * 	present in the dataset.
	 * @throws PositionsFileNotFoundException If the positions file is not 
	 * 	located at the dataset directory.
	 */
	public ElementDataset loadAndNormalizeDataset(Path path, 
		ProgressHandler progressHandler) throws IOException, 
		NoSuchStandardElementException, PositionsFileNotFoundException 
	{
		checkPath(path);
		
		DefaultElementDataset dataset = createDataset(
			path, 
			loadElementData(path, progressHandler)
		);
		
		normalizeCoordinates(dataset);
		
		return dataset;
	}

	private void checkPath(Path path) throws IOException {
		if (!Files.isDirectory(path) || !Files.isReadable(path))
			throw new IOException("Path must be a readable directory.");
	}
	
	private List<ElementData> loadElementData(Path path, 
		ProgressHandler progressHandler) throws IOException 
	{
		LineToElementConverter converter = 
			new LineToElementConverter(path.toFile(), fileFilter, configuration);
		return converter.getElements(progressHandler);
	}
	
	private DefaultElementDataset createDataset(Path path, 
		List<ElementData> elements) throws NoSuchStandardElementException 
	{
		DefaultElementDataset dataset = new DefaultElementDataset(path, path
				.getFileName().toString(), this.configuration);
		if (this.configuration.shouldNormalize()) {
			dataset.addElements(elements);
		} else {
			dataset.addElements(
				normalizeElementData(
					elements, this.configuration.getStandardElement())
			);
		}
		return dataset;
	}

	private static final List<ElementData> normalizeElementData(
		List<ElementData> elements, String standardElement) 
		throws NoSuchStandardElementException 
	{
		ElementData standardElementData = 
			getStandardElementData(elements, standardElement);
		return NormalizeElementData.normalize(standardElementData,elements)
				.stream()
					.filter(e -> !e.getName().equals(standardElement))
						.collect(Collectors.toList());
	}
	
	private static final ElementData getStandardElementData(
		List<ElementData> elements, String standardElement)
		throws NoSuchStandardElementException 
	{
		Optional<ElementData> standardElementData = elements.stream()
				.filter(e -> e.getName().equals(standardElement)).findFirst();
		if (standardElementData.isPresent()) {
			return standardElementData.get();
		} else {
			throw new NoSuchStandardElementException("Standard element "
					+ standardElement + " is not present in the dataset");
		}
	}
	
	private void normalizeCoordinates(DefaultElementDataset dataset) {
		if (dataset.getElementCount() > 0) {
			LineCoordinatesUtils.normalizeCoordinates(
				Stream.of(dataset.getElements().get(0).getLines())
				.map(LineData::getCoordinates).collect(Collectors.toList()), 
				configuration
			);
		}
	}
}
