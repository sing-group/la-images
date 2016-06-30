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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import es.uvigo.ei.sing.icpms.core.gui.util.JLimitedTextField;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat;
import es.uvigo.ei.sing.icpms.core.io.csv.CSVFormat.FileFormat;

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
public class ExportCsvDialog extends ExportDialog {
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Export to CSV";
	private static final String DESCRIPTION = 
		"This dialog allows you to export the data as a CSV file(s). " + 
		"You can select a preconfigured format (Excel, " +
		"Libre/Open Office) or Custom to customize it.";
	
	private JPanel inputComponents;
	private JXTaskPane customFormatOptionsTaskPane;
	private JComboBox<FileFormat> fileFormatCB;
	private File selectedFile;
	private JTextField fileName;
	private JRadioButton rbtnSepTab, rbtnSepCustom;
	private JTextField txtFieldCustomColumnSeparator;
	private JRadioButton rbtnDecimalSepPoint;
	private JRadioButton rbtnDecimalSepComma;
	private JRadioButton rbtnDecimalSepCustom;
	private JTextField txtFieldCustomDecimalPoint;
	private JRadioButton rbtnLineSepWindows;
	private JRadioButton rbtnLineSepLinux;
	private JRadioButton rbtnLineSepMacOS;
	private JCheckBox quoteHeaders;
	private final DecimalFormatSymbols symbols =
		new DecimalFormatSymbols(Locale.getDefault());

	private String singleFileName;
	
	/**
	 * Constructs a new instance of {@link ExportCsvDialog}.
	 * 
	 * @param parent the parent frame.
	 * @param singleFileName the name of the single file name.
	 */
	public ExportCsvDialog(JFrame parent, String singleFileName) {
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
	protected JComponent getInputComponents() {
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
		
		final JLabel lblFormat = new JLabel("Format");
		JLabel lblFormatHelp = new JLabel(ICON_HELP);
		lblFormatHelp.setToolTipText("The file format");
		fileFormatCB = new JComboBox<FileFormat>();
		fileFormatCB.setModel(new DefaultComboBoxModel<>(FileFormat.values()));
		fileFormatCB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					fileFormatSelectionChanged();
			}
		});

		final JLabel lblFileName = new JLabel("File");
		JLabel lblFileNameHelp = new JLabel(ICON_HELP);
		if (isAcceptDirectoriesOnly()) {
			lblFileNameHelp
					.setToolTipText("The selected directory to export the data");
		} else {
			lblFileNameHelp
			.setToolTipText("The selected file to export the data");
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

		groupLayout.setHorizontalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileName, Alignment.TRAILING)
					.addComponent(lblFormat, Alignment.TRAILING))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(fileChooserPanel)
					.addComponent(fileFormatCB))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileNameHelp)
					.addComponent(lblFormatHelp))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileName, Alignment.CENTER)
					.addComponent(fileChooserPanel, Alignment.CENTER)
					.addComponent(lblFileNameHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFormat, Alignment.CENTER)
					.addComponent(fileFormatCB, Alignment.CENTER)
					.addComponent(lblFormatHelp, Alignment.CENTER))
		);

		final JXTaskPaneContainer customOptionsTaskPaneContainer =
			new JXTaskPaneContainer();
		customOptionsTaskPaneContainer.setOpaque(false);
		customOptionsTaskPaneContainer.setBorder(
			BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		customFormatOptionsTaskPane = new JXTaskPane();
		customFormatOptionsTaskPane.setTitle("Custom format");
		customFormatOptionsTaskPane.setCollapsed(true);
		customFormatOptionsTaskPane.addPropertyChangeListener("collapsed",
			new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					pack();
				}
			});
		
		JPanel customOptionsPane = new JPanel();
		final GroupLayout customOptionsGroupLayout =
			new GroupLayout(customOptionsPane);
		customOptionsGroupLayout.setAutoCreateContainerGaps(true);
		customOptionsGroupLayout.setAutoCreateGaps(true);
		customOptionsPane.setLayout(customOptionsGroupLayout);
		
		customFormatOptionsTaskPane.add(customOptionsPane,
			BorderLayout.CENTER);
		customOptionsTaskPaneContainer.add(customFormatOptionsTaskPane);
		
		final ActionListener columnSeparatorListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkCustomSeparatorButtons();	
			}
		};
		
		final ActionListener actionListener = new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				formatCustomized();				
			}
		};
		
		final JLabel lblQuoteHeaders = new JLabel("Quote headers");
		final JLabel lblQuoteHeadersHelp = new JLabel(ICON_HELP);
		lblQuoteHeadersHelp.setToolTipText(
			"Sets wether table headers should be quoted");
		JPanel quotePanel = new JPanel(new GridLayout(1,1));
		quoteHeaders = new JCheckBox("", false);
		quoteHeaders.addActionListener(actionListener);
		quotePanel.add(quoteHeaders);
		
		final DocumentListener documentListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkEnabled();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkEnabled();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkEnabled();
			}
		};
		
		final JLabel lblColumnSeparator = new JLabel("Column separator");
		final JLabel lblColumnSeparatorHelp = new JLabel(ICON_HELP);
		lblColumnSeparatorHelp.setToolTipText(
			"Sets the column separator");
		final JPanel columnSeparatorPanel = new JPanel(new GridLayout(1,3));
		rbtnSepTab = new JRadioButton("Tabulator");
		rbtnSepTab.setSelected(false);
		rbtnSepCustom = new JRadioButton("CUSTOM");
		rbtnSepCustom.setSelected(true);
		txtFieldCustomColumnSeparator = new JLimitedTextField(",",1,3);
		txtFieldCustomColumnSeparator.getDocument().addDocumentListener(
			documentListener);
		final Dimension size = new Dimension(25,25);
		txtFieldCustomColumnSeparator.setMaximumSize(size);
		txtFieldCustomColumnSeparator.setSize(size);
		txtFieldCustomColumnSeparator.addFocusListener(FOCUS_LISTENER);
		final JPanel customColumnSeparatorTFPanel = new JPanel();
		final BoxLayout cCSPanelLayout = new BoxLayout(
			customColumnSeparatorTFPanel, BoxLayout.X_AXIS);
		customColumnSeparatorTFPanel.setLayout(cCSPanelLayout);
		customColumnSeparatorTFPanel.add(Box.createHorizontalGlue(),
			BorderLayout.EAST);
		customColumnSeparatorTFPanel.add(txtFieldCustomColumnSeparator);
		columnSeparatorPanel.add(rbtnSepTab);
		columnSeparatorPanel.add(rbtnSepCustom);
		columnSeparatorPanel.add(customColumnSeparatorTFPanel);
		
		final ButtonGroup separatorGroup = new ButtonGroup();
		separatorGroup.add(rbtnSepTab);
		separatorGroup.add(rbtnSepCustom);
		
		rbtnSepTab.addActionListener(columnSeparatorListener);
		rbtnSepCustom.addActionListener(columnSeparatorListener);
		rbtnSepTab.addActionListener(actionListener);
		rbtnSepCustom.addActionListener(actionListener);
		
		final JLabel lblDecimalSeparator = new JLabel("Decimal separator");
		final JLabel lblDecimalSeparatorHelp = new JLabel(ICON_HELP);
		lblDecimalSeparatorHelp.setToolTipText(
			"Sets the decimal separator");
		final JPanel decimalSeparatorPanel = new JPanel(new GridLayout(1,4));
		rbtnDecimalSepPoint = new JRadioButton("Point");
		rbtnDecimalSepPoint.setSelected(true);
		rbtnDecimalSepPoint.addActionListener(actionListener);
		rbtnDecimalSepComma = new JRadioButton("Comma");
		rbtnDecimalSepComma.setSelected(false);
		rbtnDecimalSepComma.addActionListener(actionListener);
		rbtnDecimalSepCustom = new JRadioButton("CUSTOM");
		rbtnDecimalSepCustom.setSelected(false);
		rbtnDecimalSepCustom.addActionListener(actionListener);
		txtFieldCustomDecimalPoint = new JLimitedTextField(".", 1, 3);
		txtFieldCustomDecimalPoint.getDocument().addDocumentListener(
			documentListener);
		txtFieldCustomDecimalPoint.setMaximumSize(size);
		txtFieldCustomDecimalPoint.setSize(size);
		txtFieldCustomDecimalPoint.addFocusListener(FOCUS_LISTENER);
		final JPanel customDecimalPointTFPanel  = new JPanel();
		final BoxLayout cDPPanelLayout = new BoxLayout(
			customDecimalPointTFPanel, BoxLayout.X_AXIS);
		customDecimalPointTFPanel.setLayout(cDPPanelLayout);
		customDecimalPointTFPanel.add(Box.createHorizontalGlue(),
			BorderLayout.EAST);
		customDecimalPointTFPanel.add(txtFieldCustomDecimalPoint,
			BorderLayout.EAST);
		decimalSeparatorPanel.add(rbtnDecimalSepPoint);
		decimalSeparatorPanel.add(rbtnDecimalSepComma);
		decimalSeparatorPanel.add(rbtnDecimalSepCustom);
		decimalSeparatorPanel.add(customDecimalPointTFPanel);
		
		final ButtonGroup decimalPointGroup = new ButtonGroup();
		decimalPointGroup.add(rbtnDecimalSepPoint);
		decimalPointGroup.add(rbtnDecimalSepComma);
		decimalPointGroup.add(rbtnDecimalSepCustom);
		
		final ActionListener decimalPointListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkDecimalPointButtons();				
			}
		};
		rbtnDecimalSepPoint.addActionListener(decimalPointListener);
		rbtnDecimalSepComma.addActionListener(decimalPointListener);
		rbtnDecimalSepCustom.addActionListener(decimalPointListener);
		
		final JLabel lblLineSeparator = new JLabel("Line break");
		final JLabel lblLineSeparatorHelp = new JLabel(ICON_HELP);
		lblLineSeparatorHelp.setToolTipText("Sets the line break");
		final JPanel lineSeparatorPanel = new JPanel(new GridLayout(1,3));
		rbtnLineSepWindows = new JRadioButton("Windows");
		rbtnLineSepWindows.setSelected(true);
		rbtnLineSepWindows.addActionListener(actionListener);
		rbtnLineSepLinux = new JRadioButton("Linux");
		rbtnLineSepLinux.setSelected(false);
		rbtnLineSepLinux.addActionListener(actionListener);
		rbtnLineSepMacOS = new JRadioButton("Mac OS X");
		rbtnLineSepMacOS.setSelected(false);
		rbtnLineSepMacOS.addActionListener(actionListener);
		lineSeparatorPanel.add(rbtnLineSepWindows);
		lineSeparatorPanel.add(rbtnLineSepLinux);
		lineSeparatorPanel.add(rbtnLineSepMacOS);
		
		final ButtonGroup lineBreakGroup = new ButtonGroup();
		lineBreakGroup.add(rbtnLineSepWindows);
		lineBreakGroup.add(rbtnLineSepLinux);
		lineBreakGroup.add(rbtnLineSepMacOS);
		
		customOptionsGroupLayout.setHorizontalGroup(customOptionsGroupLayout
			.createSequentialGroup()
			.addGroup(customOptionsGroupLayout.createParallelGroup()
				.addComponent(lblQuoteHeaders, Alignment.TRAILING)
				.addComponent(lblColumnSeparator, Alignment.TRAILING)
				.addComponent(lblDecimalSeparator, Alignment.TRAILING)
				.addComponent(lblLineSeparator, Alignment.TRAILING)
			)
			.addGroup(customOptionsGroupLayout.createParallelGroup()
				.addComponent(quotePanel)
				.addComponent(columnSeparatorPanel)
				.addComponent(decimalSeparatorPanel)
				.addComponent(lineSeparatorPanel)
			)
			.addGroup(customOptionsGroupLayout.createParallelGroup()
				.addComponent(lblQuoteHeadersHelp)
				.addComponent(lblColumnSeparatorHelp)
				.addComponent(lblDecimalSeparatorHelp)
				.addComponent(lblLineSeparatorHelp)
			)			
			);
		customOptionsGroupLayout.setVerticalGroup(customOptionsGroupLayout
			.createSequentialGroup()
			.addGroup(customOptionsGroupLayout.createParallelGroup()
				.addComponent(lblQuoteHeaders, Alignment.CENTER)
				.addComponent(quotePanel, Alignment.CENTER)
				.addComponent(lblQuoteHeadersHelp, Alignment.CENTER))
			.addGroup(customOptionsGroupLayout.createParallelGroup()
				.addComponent(lblColumnSeparator, Alignment.CENTER)
				.addComponent(columnSeparatorPanel, Alignment.CENTER)
				.addComponent(lblColumnSeparatorHelp, Alignment.CENTER))
			.addGroup(customOptionsGroupLayout
				.createParallelGroup()
				.addComponent(lblDecimalSeparator, Alignment.CENTER)
				.addComponent(decimalSeparatorPanel, Alignment.CENTER)
				.addComponent(lblDecimalSeparatorHelp, Alignment.CENTER))
			.addGroup(customOptionsGroupLayout.createParallelGroup()
				.addComponent(lblLineSeparator, Alignment.CENTER)
				.addComponent(lineSeparatorPanel, Alignment.CENTER)
				.addComponent(lblLineSeparatorHelp, Alignment.CENTER)));
		
		final JPanel toret = new JPanel(new BorderLayout());
		toret.add(optionsPane, BorderLayout.NORTH);
		toret.add(customOptionsTaskPaneContainer, BorderLayout.CENTER);
		
		return toret;
	}

	protected void formatCustomized() {
		if (!this.fileFormatCB.getSelectedItem().equals(FileFormat.CUSTOM)) {
			this.fileFormatCB.setSelectedItem(FileFormat.CUSTOM);
		}
	}

	protected void fileFormatSelectionChanged() {
		final FileFormat selectedItem = (FileFormat)
			this.fileFormatCB.getSelectedItem();
		
		this.customFormatOptionsTaskPane.setCollapsed(
			!selectedItem.equals(FileFormat.CUSTOM));
		
		if (!selectedItem.equals(FileFormat.CUSTOM)) {
			updateCustomValues(selectedItem);
		}
		
		checkEnabled();
	}

	private void updateCustomValues(FileFormat format) {
		this.rbtnDecimalSepCustom.setSelected(true);
		this.rbtnSepCustom.setSelected(true);
		this.quoteHeaders.setSelected(false);
		this.txtFieldCustomDecimalPoint.setText(
				String.valueOf(symbols.getDecimalSeparator()));
		if (format.equals(FileFormat.EXCEL)) {
			this.rbtnLineSepWindows.setSelected(true);
			this.txtFieldCustomColumnSeparator.setText(";");
		} else if (format.equals(FileFormat.LIBRE_OFFICE)) {
			this.rbtnLineSepLinux.setSelected(true);
			if (symbols.getDecimalSeparator() == ',') {
				this.txtFieldCustomColumnSeparator.setText(";");
			} else {
				this.txtFieldCustomColumnSeparator.setText(",");
			}
		}
	}

	protected void checkCustomSeparatorButtons() {
		txtFieldCustomColumnSeparator.setEditable(rbtnSepCustom.isSelected());
		checkEnabled();
	}
	
	protected void checkDecimalPointButtons() {
		txtFieldCustomDecimalPoint.setEditable(
			rbtnDecimalSepCustom.isSelected());
		checkEnabled();
	}

	private boolean checkSeparators() {
		if (fileFormatCB.getSelectedItem().equals(FileFormat.CUSTOM)
			&& !getColumnSeparator().equals("")
			&& getColumnSeparator().charAt(0) == getDecimalSeparator()) {
			JOptionPane.showMessageDialog(
				this,
				"You have selected the same column and decimal separator, "
				+ "which may result in an unreadable file.",
				"Invalid configuration",
				JOptionPane.WARNING_MESSAGE
			);
			return false;
		}
		return true;
	}

	protected void setSelectedFile(File newFile) {
		selectedFile = newFile;
		if (!isAcceptDirectoriesOnly() && !newFile.getName().toLowerCase().endsWith(".csv")) {
			selectedFile = new File(newFile.getAbsolutePath() + ".csv");
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

	/**
	 * Return the {@code CSVFormat} based on the configuration selected by
	 * the user.
	 * 
	 * @return Return the {@code CSVFormat} based on the configuration
	 * selected by the user.
	 */
	public CSVFormat getCSVFormat() {
		CSVFormat toret;
		
		final FileFormat selectedFormat =
			(FileFormat) fileFormatCB.getSelectedItem();
		
		if (selectedFormat.equals(FileFormat.EXCEL)
			|| selectedFormat.equals(FileFormat.LIBRE_OFFICE)) {
			try {
				toret = new CSVFormat(selectedFormat);
			} catch (Exception e) {
				return null;
			}
		} else {
			toret = new CSVFormat(
				getColumnSeparator(), getDecimalSeparator(),
				quoteHeaders.isSelected(), getLineBreak()
			);
		}
		
		return toret;
	}
	
	private String getColumnSeparator() {
		final String columnSeparator;
		
		if (rbtnSepTab.isSelected()) {
			columnSeparator = "\t";
		} else if (rbtnSepCustom.isSelected()) {
			columnSeparator = txtFieldCustomColumnSeparator.getText();
		} else {
			columnSeparator = null;
		}
		
		return columnSeparator;
	}

	private char getDecimalSeparator() {
		final char decimalSeparator;
		
		if (rbtnDecimalSepComma.isSelected()) {
			decimalSeparator = ',';
		} else if (rbtnDecimalSepPoint.isSelected()) {
			decimalSeparator = '.';
		} else if (rbtnDecimalSepCustom.isSelected()) {
			decimalSeparator = txtFieldCustomDecimalPoint.getText().charAt(0);
		} else {
			decimalSeparator = 0;
		}
		
		return decimalSeparator;
	}
	
	private String getLineBreak() {
		final String lineBreak;
		
		if (rbtnLineSepWindows.isSelected()) {
			lineBreak = "\r\n";
		} else {
			lineBreak = "\n";
		}
		
		return lineBreak;
	}

	/**
	 * Return the file selected by the user.
	 * 
	 * @return the file selected by the user
	 */
	public File getSelectedFile() {
		return this.selectedFile;
	}
	
	protected void checkEnabled() {
		this.okButton.setEnabled(
			getSelectedFile() != null && checkSeparators()
		);
	}
	
	@Override
	public void setVisible(boolean b) {
		this.fileFormatCB.setSelectedItem(FileFormat.EXCEL);
		super.setVisible(b);
	}
	
	@Override
	protected boolean isAcceptDirectoriesOnly() {
		return false;
	}
}
