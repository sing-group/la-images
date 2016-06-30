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
package es.uvigo.ei.sing.icpms.core.entities.datasets.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

/**
 * An extension of properties to read element dataset configuration properties.
 * 
 * @author hlfernandez
 *
 */
public class ElementDatasetConfigurationProperties extends Properties {
	private static final long serialVersionUID = 1L;
	
	public static final String ABLATION_SPEED = "ablation.speed";
	public static final String SPACE_INTERVAL = "space.interval";
	public static final String ACQUISITION_TIME = "acquisition.time";
	public static final String STANDARD = "standard";

	/**
	 * Creates a new {@code ElementDatasetConfigurationProperties} and loads
	 * {@code file}.
	 * 
	 * @param file a string with the path of the file to load.
	 */
	public ElementDatasetConfigurationProperties(File file) {
		try {
			this.load(new FileInputStream(file));
		} catch (Exception e) {
		}
	}

	/**
	 * Returns a {@code Optional} with the ablation speed loaded value or null
	 * if it has not found.
	 * 
	 * @return a {@code Optional} with the ablation speed loaded value or null
	 * if it has not found.
	 */
	public Optional<String> getAblationSpeed() {
		return Optional.ofNullable((String) getProperty(ABLATION_SPEED));
	}
	
	/**
	 * Returns a {@code Optional} with the acquisition time loaded value or null
	 * if it has not found.
	 * 
	 * @return a {@code Optional} with the acquisition time loaded value or null
	 * if it has not found.
	 */	
	public Optional<String> getAcquisitionTime() {
		return Optional.ofNullable((String) getProperty(ACQUISITION_TIME));
	}
	
	/**
	 * Returns a {@code Optional} with the space interval loaded value or null
	 * if it has not found.
	 * 
	 * @return a {@code Optional} with the space interval loaded value or null
	 * if it has not found.
	 */	
	public Optional<String> getSpaceInterval() {
		return Optional.ofNullable((String) getProperty(SPACE_INTERVAL));
	}
	
	/**
	 * Returns a {@code Optional} with the standard loaded value or null
	 * if it has not found.
	 * 
	 * @return a {@code Optional} with the standard loaded value or null
	 * if it has not found.
	 */	
	public Optional<String> getStandard() {
		return Optional.ofNullable((String) getProperty(STANDARD));
	}
}
