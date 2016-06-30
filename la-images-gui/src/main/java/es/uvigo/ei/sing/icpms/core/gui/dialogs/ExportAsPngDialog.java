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
package es.uvigo.ei.sing.icpms.core.gui.dialogs;

import static es.uvigo.ei.sing.icpms.core.gui.util.GUIUtils.getFileChooserSelectedFile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

/**
 * This class constructs a dialog that allows user to introduce:
 * <ul>
 * <li>The file to save the data.</li>
 * <li>The image {@code Quality}.</li>
 * <li>The image size.</li>
 * </ul> 
 * 
 * @author Hugo López-Fernández
 *
 */
public class ExportAsPngDialog extends ExportDialog {
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Export to PNG";
	private static final String DESCRIPTION = 
		"This dialog allows you to export the current chart " + 
		"as a PNG file with the current settings.";
	
	private JPanel inputComponents;
	private File selectedFile;
	private JTextField fileName;
	private JFormattedTextField widthTF;
	private JFormattedTextField heightTF;
	
	private String singleFileName;

	/**
	 * Constructs a new instance of {@link ExportAsPngDialog}.
	 * 
	 * @param parent the parent frame.
	 * @param singleFileName the name of the single file name.
	 */
	public ExportAsPngDialog(JFrame parent, String singleFileName) {
		super(parent, TITLE, DESCRIPTION);
		this.singleFileName = singleFileName;
		this.configureDialogFile();
		this.setResizable(true);
		this.setIconsVisible(false);
		centerDialogOnParent();
		checkEnabled();
	}

	private void configureDialogFile() {
		final File selectedDataFile = getFileChooserSelectedFile(
			getFileChooser(), isAcceptDirectoriesOnly(), singleFileName);
		setSelectedFile(selectedDataFile);
	}

	@Override
	protected JPanel getInputComponents() {
		UIManager.put("TaskPane.animate", Boolean.FALSE);
		
		if (inputComponents != null)
			throw new RuntimeException(
					"Can't create input components twice (app bug)");
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setCommitsOnValidEdit(true);

		JPanel optionsPane = new JPanel();
		final GroupLayout groupLayout = new GroupLayout(optionsPane);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		optionsPane.setLayout(groupLayout);
		
		final JLabel lblFileName = new JLabel("File");
		JLabel lblFileNameHelp = new JLabel(ICON_HELP);
		if (isAcceptDirectoriesOnly()) {
			lblFileNameHelp
				.setToolTipText("The selected directory to export the images");
		} else {
			lblFileNameHelp
				.setToolTipText("The selected file to export the image");
		}
		fileName = new JTextField("", 20);
		fileName.setEditable(false);
		fileName.addMouseListener(getBrowseListener());

		JButton browseButton = new JButton(ICON_BROWSE);
		browseButton.setToolTipText("Opens a file chooser to select a file");
		browseButton.addMouseListener(getBrowseListener());
		
		final JPanel fileChooserPanel = new JPanel(new BorderLayout());
		fileChooserPanel.add(fileName, BorderLayout.CENTER);
		fileChooserPanel.add(browseButton, BorderLayout.EAST);
		
		final JLabel lblImageSize = new JLabel("Size");
		JLabel lblImageSizeHelp = new JLabel(ICON_HELP);
		lblImageSizeHelp.setToolTipText("Sets the size in px of the image");
		JPanel imageSizePanel = getImageSizePanel();
		
		groupLayout.setHorizontalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileName, Alignment.TRAILING)
					.addComponent(lblImageSize, Alignment.TRAILING))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(fileChooserPanel)
					.addComponent(imageSizePanel))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileNameHelp)
					.addComponent(lblImageSizeHelp))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileName, Alignment.CENTER)
					.addComponent(fileChooserPanel, Alignment.CENTER)
					.addComponent(lblFileNameHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblImageSize, Alignment.CENTER)
					.addComponent(imageSizePanel, Alignment.CENTER)
					.addComponent(lblImageSizeHelp, Alignment.CENTER))
		);
		
		final JPanel toret = new JPanel(new BorderLayout());
		toret.add(optionsPane, BorderLayout.NORTH);
		
		return toret;
	}

	private JPanel getImageSizePanel() {
		JPanel imageSizePanel = new JPanel();
		imageSizePanel.setLayout(new GridLayout(1, 4));
		
	    NumberFormat format = NumberFormat.getInstance();
	    format.setGroupingUsed(false);
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setCommitsOnValidEdit(true);
	    widthTF = new JFormattedTextField(formatter);
	    widthTF.setValue(1200);
	    widthTF.addFocusListener(FOCUS_LISTENER);
	    heightTF = new JFormattedTextField(formatter);
	    heightTF.setValue(1000);
	    heightTF.addFocusListener(FOCUS_LISTENER);
	    
	    imageSizePanel.add(new JLabel(" Width:"));
	    imageSizePanel.add(widthTF);
	    imageSizePanel.add(new JLabel(" Height:"));
	    imageSizePanel.add(heightTF);
	    
		return imageSizePanel;
	}

	protected void setSelectedFile(File newFile) {
		selectedFile = newFile;
		if (!isAcceptDirectoriesOnly() && !newFile.getName().toLowerCase().endsWith(".png")) {
			selectedFile = new File(newFile.getAbsolutePath() + ".png");
		}
		fileName.setText(selectedFile.getAbsolutePath());
	}

	/**
	 * Return {@code true} if user clicked in the cancel button. {@code false}
	 * otherwise.
	 * 
	 * @return {@code true} if user clicked in the cancel button. {@code false}
	 * otherwise.
	 */
	public boolean isCanceled() {
		return canceled;
	}

	public File getSelectedFile() {
		return this.selectedFile;
	}
	
	protected void checkEnabled() {
		this.okButton.setEnabled(
			getSelectedFile() != null && checkImageSize()
		);
	}
	
	private boolean checkImageSize() {
		return getSelectedHeight() > 0 && getSelectedWidth() > 0;
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	public int getSelectedWidth() {
		return (int) widthTF.getValue();
	}
	
	public int getSelectedHeight() {
		return (int) heightTF.getValue();
	}

	@Override
	protected boolean isAcceptDirectoriesOnly() {
		return false;
	}
}