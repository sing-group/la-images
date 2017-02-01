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
package es.uvigo.ei.sing.laimages.aibench.operations;

import static es.uvigo.ei.aibench.core.operation.annotation.Direction.INPUT;
import static es.uvigo.ei.aibench.core.operation.annotation.Direction.OUTPUT;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.sing.laimages.aibench.datatypes.AiBenchLaImagesAnalysis;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDatasetConfiguration;
import es.uvigo.ei.sing.laimages.core.io.LineDatasetLoader;
import es.uvigo.ei.sing.laimages.core.io.exception.NoSuchStandardElementException;
import es.uvigo.ei.sing.laimages.core.io.exception.PositionsFileNotFoundException;
import es.uvigo.ei.sing.laimages.core.util.DefaultProgressHandler;
import es.uvigo.ei.sing.laimages.gui.analysis.LaImagesAnalysis;

/**
 * An AIBench operation to load an ICP-MS dataset.
 * 
 * @author Hugo López-Fernández
 *
 */
@Operation(description = "Loads a dataset.")
public class LoadDatasetOperation {

	public static final String PORT_NAME_DATA_DIRECTORY 	= "Data directory";
	public static final String PORT_NAME_DATA_EXTENSIONS 	= "File extensions";
	public static final String PORT_NAME_STANDARD 			= "Standard";
	public static final String PORT_NAME_ABLATION_SPEED 	= "Ablation speed (mm/s)";
	public static final String PORT_NAME_ACQUISITION_TIME 	= "Acquisition time (s)";
	public static final String PORT_NAME_SPACE_INTERVAL 	= "Space interval (mm)";
	
	private static final String PORT_NAME_STANDARD_DESCRIPTION
		= "Internal standard is used to normalize the results and to overcome "
			+ "the instrumental oscillation. User must choose a specific "
			+ "element to be monitored, for example, a known matrix element "
			+ "in the sample or those intentionally added by user .";
	private static final String PORT_NAME_ABLATION_SPEED_DESCRIPTION
		= "Speed set by the user in the laser operational mode CONTINUOUS FIRING.";
	private static final String PORT_NAME_ACQUISITION_TIME_DESCRIPTION 	
		= "This parameter refers to the time needed by the acquisition of one "
			+ "point for all elements monitored in the ICP-MS. This parameter "
			+ "is intrinsically linked to ICP-MS parameters, such as the "
			+ "number of isotopes monitored, sweeps, number of replicates "
			+ "and dwell (or residence) time. ";
	private static final String PORT_NAME_SPACE_INTERVAL_DESCRIPTION	
		= "The space interval is the distance among the center of two lines. "
			+ "The lowest space interval results in the highest image resolution.";
	
	private File directory;
	private double ablationSpeed;
	private double acquisitionTime;
	private double spaceInterval;
	private String standardElement;
	
	/**
	 * Sets the data directory to load (INPUT port).
	 * 
	 * @param directory the data directory.
	 */
	@Port(
		direction = INPUT,
		name = PORT_NAME_DATA_DIRECTORY,
		description = "Directory where the dataset is stored",
		order = 1,
		extras="selectionMode=directories"
	)
	public void setDataDirectory(File directory) {
		this.directory = directory;
	}

	/**
	 * Sets the element used as standard (INPUT port).
	 * 
	 * @param standardElement the element used as standard.
	 */
	@Port(
		direction = INPUT,
		name = PORT_NAME_STANDARD,
		defaultValue = "C12",
		description = PORT_NAME_STANDARD_DESCRIPTION,
		order = 2
	)
	public void setStandardElement(String standardElement) {
		this.standardElement = standardElement;
	}
	
	/**
	 * Sets the ablation speed (INPUT port).
	 * 
	 * @param ablationSpeed the ablation speed.
	 */
	@Port(
		direction = INPUT,
		name = PORT_NAME_ABLATION_SPEED,
		defaultValue = "0.060",
		description = PORT_NAME_ABLATION_SPEED_DESCRIPTION,
		order = 3
	)
	public void setAblationSpeed(double ablationSpeed) {
		this.ablationSpeed = ablationSpeed;
	}
	
	
	/**
	 * Sets the acquisition time (INPUT port).
	 * 
	 * @param acquisitionTime the acquisition time.
	 */
	@Port(
		direction = INPUT,
		name = PORT_NAME_ACQUISITION_TIME,
		defaultValue = "0.527",
		description = PORT_NAME_ACQUISITION_TIME_DESCRIPTION,
		order = 4
	)
	public void setAcquisitionTime(double acquisitionTime) {
		this.acquisitionTime = acquisitionTime;
	}
	
	/**
	 * Sets the space interval (INPUT port).
	 * 
	 * @param spaceInterval the space interval.
	 */
	@Port(
		direction = INPUT,
		name = PORT_NAME_SPACE_INTERVAL,
		defaultValue = "0.080",
		description = PORT_NAME_SPACE_INTERVAL_DESCRIPTION,
		order = 5
	)
	public void setSpaceInterval(double spaceInterval) {
		this.spaceInterval = spaceInterval;
	}
	
	/**
	 * Loads the data and returns the experiment (OUTPUT port).
	 * 
	 * @return the experiment.
	 * @throws IOException if an error occurs reading the dataset.
	 * @throws NoSuchStandardElementException If the standardElement is not 
	 * 	present in the dataset
	 */
	@Port(direction = OUTPUT, order = 1000)
	public AiBenchLaImagesAnalysis run() throws IOException,
			NoSuchStandardElementException {
		return new AiBenchLaImagesAnalysis(loadAnalysis());
	}
	
	private LaImagesAnalysis loadAnalysis()
			throws PositionsFileNotFoundException, IOException,
			NoSuchStandardElementException {
		return new LaImagesAnalysis(loadDataset()); 
	}

	private ElementDataset loadDataset() throws IOException,
			NoSuchStandardElementException, PositionsFileNotFoundException {
		ElementDatasetConfiguration configuration = new ElementDatasetConfiguration(
				ablationSpeed, acquisitionTime, spaceInterval, standardElement);
		LineDatasetLoader datasetLoader = new LineDatasetLoader(configuration);
		DefaultProgressHandler progressHandler = new DefaultProgressHandler();
		
		ElementDataset dataset = datasetLoader.loadAndNormalizeDataset(this.directory.toPath(), progressHandler);
		checkWarnings(progressHandler);
		
		return dataset;
	}

	private void checkWarnings(DefaultProgressHandler progressHandler) {
		if(progressHandler.hasWarnings()) {
			JOptionPane.showMessageDialog(Workbench.getInstance().getMainFrame(),
				getWarningComponent(progressHandler.getWarnings()),
				"Warning",
				JOptionPane.WARNING_MESSAGE
			);
		}
	}

	private JComponent getWarningComponent(List<String> warnings) {
		JTextPane warning = new JTextPane();
		warning.setContentType("text/html");
		warning.setText(getWarningMessage(warnings));
		warning.setEditable(false);
		Dimension size = new Dimension(400, 300);
		warning.setPreferredSize(size);
		warning.setSize(size);
		warning.setMaximumSize(size);
		return new JScrollPane(warning);
	}

	private String getWarningMessage(List<String> warnings) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>Warnings ocurred during data loading: <ul>");
		warnings.forEach(w -> {
			sb.append("<li>").append(w).append("</li>");
		});
		sb.append("</ul></html>");
		return sb.toString();
	}
}
