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
package es.uvigo.ei.sing.laimages.gui.dialogs;

import javax.swing.JFrame;

/**
 * This class constructs a dialog that allows user to introduce:
 * <ul>
 * <li>The file format.</li>
 * <li>The file to save the data.</li>
 * <li>Optionally, parameters to configure the custom file format.</li>
 * </ul> 
 * 
 * @author Hugo López-Fernández
 *
 */
public class ExportAllAsCsvDialog extends ExportCsvDialog {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new instance of {@code ExportAllAsCsvDialog}.
	 * 
	 * @param parent the parent frame.
	 */
	public ExportAllAsCsvDialog(JFrame parent) {
		super(parent, "");
	}

	@Override
	protected boolean isAcceptDirectoriesOnly() {
		return true;
	}
}