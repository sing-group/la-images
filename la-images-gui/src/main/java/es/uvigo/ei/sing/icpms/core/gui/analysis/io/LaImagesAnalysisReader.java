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
package es.uvigo.ei.sing.icpms.core.gui.analysis.io;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.sing.icpms.core.gui.analysis.LaImagesAnalysis;

/**
 * Loads a {@link LaImagesAnalysis} from a file.
 * 
 * @author hlfernandez
 */
public interface LaImagesAnalysisReader {
	/**
	 * Reads a {@link LaImagesAnalysis} from a file.
	 * 
	 * @param file the file where the analysis is stored.
	 * @return the analysis read from the file.
	 * @throws IOException if an error happens while loading the analysis.
	 */
	public LaImagesAnalysis read(File file) throws IOException;
}