/*
 * #%L
 * LA-iMageS
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
package es.uvigo.ei.sing.laimages.aibench.dialogs;

import java.awt.Color;
import java.io.File;
import java.util.Optional;

import javax.swing.JTextField;

import es.uvigo.ei.sing.laimages.core.entities.datasets.config.ElementDatasetConfigurationProperties;
import es.uvigo.ei.sing.laimages.core.util.FileNameUtils;

/**
 * A class used by {@code LoadDatasetOperationDialog} in order to control
 * text fields and load the properties file when the data directory changes.
 * 
 * @author Hugo López-Fernández
 * @see ElementDatasetConfigurationProperties
 */
public class LoadDatasetOperationConfigurationController {

	public static final String CONFIGURATION_FILE = "parameters.conf";
	private static final Color GREEN_PASTEL = new Color(189,236,182);
	
	private JTextField ablationSpeedTF;
	private JTextField acquisitionTimeTF;
	private JTextField spaceIntervalTF;
	private JTextField standardTF;

	public void setAblationSpeedTextField(JTextField ablationSpeedTF) {
		this.ablationSpeedTF = ablationSpeedTF;
	}

	public void setAcquisitionTimeTextField(JTextField acquisitionTimeTF) {
		this.acquisitionTimeTF = acquisitionTimeTF;
	}

	public void setSpaceIntervalTextField(JTextField spaceIntervalTF) {
		this.spaceIntervalTF = spaceIntervalTF;
	}
	
	public void setStandardTextField(JTextField standardTF) {
		this.standardTF = standardTF;
	}

	/**
	 * Tells the controller that the data directory has changed in order to
	 * try to load the configuration parameters file. 
	 * 
	 * @param dataDirectory the new data directory.
	 */
	public void dataDirectoryChanged(String dataDirectory) {
		ElementDatasetConfigurationProperties eCP = 
			new ElementDatasetConfigurationProperties(
				FileNameUtils.findFileIgnoreCase(
					new File(dataDirectory), CONFIGURATION_FILE).orElse(null)
			);
		updateInputTextFields(eCP);
	}

	private void updateInputTextFields(ElementDatasetConfigurationProperties eCP) {
		updateJTextField(ablationSpeedTF, eCP.getAblationSpeed());
		updateJTextField(acquisitionTimeTF, eCP.getAcquisitionTime());
		updateJTextField(spaceIntervalTF, eCP.getSpaceInterval());
		updateJTextField(standardTF, eCP.getStandard());
	}

	private void updateJTextField(JTextField tf, Optional<String> value) {
		if (value.isPresent()) {
			tf.setText(value.get());
			tf.setBackground(GREEN_PASTEL);
		}
	}
}
