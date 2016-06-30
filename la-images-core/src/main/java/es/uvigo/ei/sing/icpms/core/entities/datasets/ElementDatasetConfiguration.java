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

import java.io.Serializable;

/**
 * An {@code ElementDatasetConfiguration} stores the ICP-MS acquisition 
 * parameters of a dataset.
 * 
 * @author hlfernandez
 *
 */
public class ElementDatasetConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double ablationSpeed;
	private double acquisitionTime;
	private double spaceInterval;
	private String standardElement;

	/**
	 * Constructs a new instance of {@code ElementDatasetConfiguration}.
	 * 
	 * @param ablationSpeed the ablation speed.
	 * @param acquisitionTime the acquisition time.
	 * @param spaceInterval the space interval.
	 * @param standardElement the standard element of the dataset.
	 */
	public ElementDatasetConfiguration(double ablationSpeed,
			double acquisitionTime, double spaceInterval, String standardElement) {

		this.ablationSpeed = ablationSpeed;
		this.acquisitionTime = acquisitionTime;
		this.spaceInterval = spaceInterval;
		this.standardElement = standardElement;
	}
	
	/**
	 * Returns the ablation speed.
	 * 
	 * @return the ablation speed. 
	 */
	public double getAblationSpeed() {
		return ablationSpeed;
	}
	
	/**
	 * Returns the acquisition time.
	 * 
	 * @return the acquisition time. 
	 */
	public double getAcquisitionTime() {
		return acquisitionTime;
	}
	
	/**
	 * Returns the space interval.
	 * 
	 * @return the space interval. 
	 */
	public double getSpaceInterval() {
		return spaceInterval;
	}

	/**
	 * Returns the interval between two values (measurements) of the same line,
	 * that is: the ablation speed multiplied by the acquisition time.
	 * 
	 * @return the interval between two values of the same line.
	 */
	public double getLineRangeInterval() {
		return this.ablationSpeed * this.acquisitionTime;
	}
	
	/**
	 * Returns the interval between two lines, that is, the space interval.
	 * 
	 * @return the interval between two lines.
	 */
	public double getPositionInterval() {
		return this.spaceInterval;
	}

	/**
	 * Returns the name of the standard element.
	 * 
	 * @return the name of the standard element.
	 */
	public String getStandardElement() {
		return standardElement;
	}

	/**
	 * Returns {@code true} if elements in the dataset should be normalized and
	 * {@code false} otherwise. Elements must be normalized if standard element
	 * name has been set.
	 * 
	 * @return {@code true} if elements in the dataset should be normalized and
	 * {@code false} otherwise
	 */
	public boolean shouldNormalize() {
		return standardElement == null || standardElement.equals("");
	}
}
