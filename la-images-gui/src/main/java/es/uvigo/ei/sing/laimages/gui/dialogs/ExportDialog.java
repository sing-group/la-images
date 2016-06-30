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

import static es.uvigo.ei.sing.laimages.gui.util.GUIUtils.configureFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import es.uvigo.ei.sing.laimages.core.util.CommonFileChooser;

/**
 * This class serves as superclass to other classes implementing Export dialogs
 * and provides some common icons and methods.
 * 
 * @author Hugo López-Fernández
 *
 */
public abstract class ExportDialog extends InputJDialog {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new instance of {@link ExportDialog}.
	 * 
	 * @param parent the parent frame of this dialog.
	 * @param title the dialog title.
	 * @param description the dialog description.
	 */
	protected ExportDialog(JFrame parent, String title, String description) {
		super(parent, title, description);
		this.setModal(true);
	}

	/**
	 * Returns the dialog filechooser.
	 * 
	 * @return the dialog filechooser.
	 */
	protected JFileChooser getFileChooser() {
		return CommonFileChooser.getInstance().getFilechooser();
	}

	@Override
	public void setVisible(boolean b) {
		pack();
		super.setVisible(b);
	}

	protected void onBrowse() {
		JFileChooser fileChooser = getFileChooser();
		configureDialogFileChooser(fileChooser);
		int returnVal = fileChooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setSelectedFile(fileChooser.getSelectedFile());
			checkEnabled();
		}
	}

	protected void configureDialogFileChooser(JFileChooser fileChooser) {
		configureFileChooser(
			getFileChooser(), 
			isAcceptDirectoriesOnly(),
			getSelectedFile()
		);
	}
	
	/**
	 * Return {@code true} if dialog only accepts directories to export and 
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if dialog only accepts directories to export and 
	 * {@code false} otherwise.
	 */
	protected abstract boolean isAcceptDirectoriesOnly();

	/**
	 * Returns the file selected by the user.
	 * 
	 * @return the file selected by the user
	 */
	protected abstract File getSelectedFile();
	
	/**
	 * Sets the file selected by the user.
	 * 
	 * @param f the file selected by the user.
	 */
	protected abstract void setSelectedFile(File f);
	
	protected MouseListener getBrowseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3) {
					onBrowse();
				}
			}
		};
	}
}