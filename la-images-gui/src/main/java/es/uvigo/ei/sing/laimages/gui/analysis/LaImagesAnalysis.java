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
package es.uvigo.ei.sing.laimages.gui.analysis;

import java.io.Serializable;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration;

/**
 * 
 * A class that represents a LA-iMageS analysis, that is: an
 * {@code ElementDataset dataset} together with an {@code ElementDataView
 * configuration}.
 * 
 * @author Hugo López-Fernández
 *
 */
public class LaImagesAnalysis implements Serializable {
	private static final long serialVersionUID = 1L;
	private ElementDataset dataset;
	private ElementDataViewConfiguration viewConfiguration;
	
	/**
	 * Constructs a new {@code LaImagesAnalysis} with the specified {@code
	 * dataset} and the default {@code ElementDataViewConfiguration}.
	 * 
	 * @param dataset a {@code ElementDataset}.
	 */
	public LaImagesAnalysis(ElementDataset dataset) {
		this.dataset = dataset;
		this.viewConfiguration = new ElementDataViewConfiguration();
	}
	
	/**
	 * Constructs a new {@code LaImagesAnalysis} with the specified {@code
	 * dataset} and {@code viewConfiguration}.
	 * 
	 * @param dataset a {@code ElementDataset}.
	 * @param viewConfiguration a {@code ElementDataViewConfiguration}.
	 */
	public LaImagesAnalysis(ElementDataset dataset,
			ElementDataViewConfiguration viewConfiguration
	) {
		this.dataset = dataset;
		this.viewConfiguration = viewConfiguration;
	}

	/**
	 * Returns the {@code ElementDataset} used in this analysis.
	 * 
	 * @return the {@code ElementDataset} used in this analysis.
	 */
	public ElementDataset getDataset() {
		return dataset;
	}
	
	/**
	 * Returns the {@code ElementDataViewConfiguration} used in this analysis.
	 * 
	 * @return the {@code ElementDataViewConfiguration} used in this analysis.
	 */
	public ElementDataViewConfiguration getConfiguration() {
		return viewConfiguration;
	}
}