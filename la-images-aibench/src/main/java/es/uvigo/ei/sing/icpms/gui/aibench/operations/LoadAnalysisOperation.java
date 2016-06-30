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
package es.uvigo.ei.sing.icpms.gui.aibench.operations;

import static es.uvigo.ei.aibench.core.operation.annotation.Direction.INPUT;
import static es.uvigo.ei.aibench.core.operation.annotation.Direction.OUTPUT;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.sing.icpms.core.gui.analysis.LaImagesAnalysis;
import es.uvigo.ei.sing.icpms.core.gui.analysis.io.LaImagesAnalysisReader;
import es.uvigo.ei.sing.icpms.core.gui.analysis.io.SerializationLaImagesAnalysisReader;
import es.uvigo.ei.sing.icpms.core.io.exception.NoSuchStandardElementException;
import es.uvigo.ei.sing.icpms.gui.aibench.datatypes.AiBenchLaImagesAnalysis;

/**
 * An AIBench operation to load an LA-iMageS analysis.
 * 
 * @author hlfernandez
 *
 */
@Operation(description = "Loads an analysis.")
public class LoadAnalysisOperation {
	private File analysis;
	
	/**
	 * Sets the data directory to load (INPUT port).
	 * 
	 * @param analysis the data directory.
	 */
	@Port(
		direction = INPUT,
		name = "Analysis file",
		description = "Analysis (*lai) file",
		order = 1,
		extras = "filters=.*\\.lai|: LA-iMageS analysis file"
	)
	public void setDataDirectory(File analysis) {
		this.analysis = analysis;
	}
	@Port(direction = OUTPUT, order = 1000)
	
	public AiBenchLaImagesAnalysis run() throws IOException, NoSuchStandardElementException {
		return new AiBenchLaImagesAnalysis(loadAnalysis());
	}

	private LaImagesAnalysis loadAnalysis() throws IOException{
		LaImagesAnalysisReader reader = new SerializationLaImagesAnalysisReader();
		LaImagesAnalysis loadedAnalysis = reader.read(analysis);
		
		return loadedAnalysis;
	}
}
