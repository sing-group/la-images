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
package es.uvigo.ei.sing.icpms.core.gui.util;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * Utility class to support common GUI operations.
 * 
 * @author hlfernandez
 *
 */
public class GUIUtils {

	/**
	 * Generates the selected file for the given {@code JFileChooser} based on
	 * the {@code directoryOnly} and {@code fileName} parameters.
	 * 
	 * @param fchooser the target {@code JFileChooser}
	 * @param directoryOnly if only directories are accepted.
	 * @param fileName the candidate name for the selected file.
	 * @return the selected {@code File}.
	 */
	public static final File getFileChooserSelectedFile(JFileChooser fchooser,
			boolean directoryOnly, String fileName) {
		File targetSelectedFile;
		File currentSelectedFile = fchooser.getSelectedFile();
		if (currentSelectedFile == null) {
			if (directoryOnly) {
				targetSelectedFile = fchooser.getCurrentDirectory();
			} else {
				targetSelectedFile = new File(fchooser.getCurrentDirectory(),
						fileName);
			}
		} else {
			if (directoryOnly) {
				targetSelectedFile = currentSelectedFile.isDirectory() ? currentSelectedFile
						: currentSelectedFile.getParentFile();
			} else {
				File dir = currentSelectedFile.isDirectory() ? currentSelectedFile
						: currentSelectedFile.getParentFile();
				targetSelectedFile = new File(dir, fileName);
			}
		}
		return targetSelectedFile;
	}
	
	/**
	 * Configures {@code fchooser}.
	 * 
	 * @param fchooser the {@code JFileChooser} to configure.
	 * @param directoryOnly if only directories are accepted.
	 * @param selectedFile the selected file.
	 */
	public static final void configureFileChooser(
			JFileChooser fchooser, boolean directoryOnly, File selectedFile
	) {
		int mode = directoryOnly ? 
			JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY;
		fchooser.setFileSelectionMode(mode);
		fchooser.setSelectedFile(selectedFile);
	}
}
