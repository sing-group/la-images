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

import static es.uvigo.ei.sing.laimages.gui.dialogs.RangeInputDialog.COLOR_INVALID_INPUT;
import static es.uvigo.ei.sing.laimages.gui.dialogs.RangeInputDialog.COLOR_VALID_INPUT;
import static es.uvigo.ei.sing.laimages.gui.util.GUIUtils.getFileChooserSelectedFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import es.uvigo.ei.sing.laimages.gui.jzy3d.Range;
import es.uvigo.ei.sing.laimages.gui.util.FloatTextField;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMap;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMapRangeMode;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.InterpolationLevel;
import es.uvigo.ei.sing.laimages.gui.views.components.ScaleSpinner;
import es.uvigo.ei.sing.laimages.gui.views.components.ViewPointEditorPanel;

/**
 * This class constructs a dialog that allows user to introduce:
 * <ul>
 * <li>The file to save the data.</li>
 * <li>The image {@code ElementDataImageConfiguration}.</li>
 * <li>The image size.</li>
 * </ul> 
 * 
 * @author Hugo López-Fernández
 *
 */
public class ExportAllAsPngDialog extends ExportDialog {
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Export to PNG";
	private static final String DESCRIPTION = 
		"This dialog allows you to export al the elements as a PNG files. " + 
		"You can configure the settings for the images.";

	private JPanel inputComponents;
	private JComboBox<InterpolationLevel> interpolationLevelCB;
	private JComboBox<ColorMap> colorMapCB;
	private JComboBox<ColorMapRangeMode> rangeModeCB;
	private JCheckBox viewerModeCB;
	private JCheckBox showColorBarLegendCB;
	private JCheckBox showAxesCB;
	private JCheckBox showTickLinesCB;
	private File selectedFile;
	private JTextField fileName;
	private JFormattedTextField widthTF;
	private JFormattedTextField heightTF;
	private JFormattedTextField minRangeValue;
	private JFormattedTextField maxRangeValue;
	private Range datasetRange;
	private Coord2d currentViewPoint;
	private ScaleSpinner scaleSpinner;
	private JRadioButton rbDefaultViewPoint;
	private JRadioButton rbActualViewPoint;
	private JRadioButton rbCustomViewPoint;
	private ViewPointEditorPanel viewPointEditorPanel;
	
	/**
	 * Constructs a new instance of {@link ExportAllAsPngDialog}.
	 * 
	 * @param parent the parent frame.
	 */
	public ExportAllAsPngDialog(JFrame parent) {
		super(parent, TITLE, DESCRIPTION);

		this.configureDialogFile();
		this.setResizable(true);
		this.setIconsVisible(false);
		centerDialogOnParent();
		checkEnabled();
	}
	
	private void configureDialogFile() {
		final File selectedDataFile = getFileChooserSelectedFile(
			getFileChooser(), isAcceptDirectoriesOnly(), "");
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
		
		final JLabel lblInterpolationLevel = new JLabel("Interpolation level");
		JLabel lblInterpolationLevelHelp = new JLabel(ICON_HELP);
		lblInterpolationLevelHelp.setToolTipText("The interpolation level.");
		interpolationLevelCB = new JComboBox<InterpolationLevel>();
		interpolationLevelCB.setModel(new DefaultComboBoxModel<>(InterpolationLevel.values()));

		final JLabel lblColorMap = new JLabel("Color map");
		JLabel lblColorMapHelp = new JLabel(ICON_HELP);
		lblColorMapHelp.setToolTipText("The color map.");
		colorMapCB = new JComboBox<ColorMap>();
		colorMapCB.setModel(new DefaultComboBoxModel<>(ColorMap.values()));
		
		final JLabel lblViewerMode = new JLabel("3D view");
		JLabel lblViewerModeHelp = new JLabel(ICON_HELP);
		lblViewerModeHelp.setToolTipText("Check if you want a 3D image.");
		viewerModeCB = new JCheckBox();
		viewerModeCB.addItemListener((e) -> {
			positionModeChanged();
		});

		final JLabel lblScale = new JLabel("Scale");
		JLabel lblScaleHelp = new JLabel(ICON_HELP);
		lblScaleHelp.setToolTipText("The image scale."
			+ " Note that it only applies if 3D is selected");
		scaleSpinner = new ScaleSpinner();
		((JSpinner.DefaultEditor) scaleSpinner.getSpinner().getEditor())
			.getTextField().addFocusListener(FOCUS_LISTENER);
		
		final JLabel lblViewPoint = new JLabel("View point");
		JLabel lblViewPointHelp = new JLabel(ICON_HELP);
		lblViewPointHelp.setToolTipText("The view point angle (in degrees).");
		rbDefaultViewPoint = new JRadioButton("Default", true);
		rbActualViewPoint = new JRadioButton("Actual");
		rbCustomViewPoint = new JRadioButton("CUSTOM");
		ButtonGroup bgViewPoint = new ButtonGroup();
		bgViewPoint.add(rbDefaultViewPoint);
		bgViewPoint.add(rbActualViewPoint);
		bgViewPoint.add(rbCustomViewPoint);
		ActionListener viewPointActionListener = l -> {
			this.viewPointModeChanged();
		};
		rbDefaultViewPoint.addActionListener(viewPointActionListener);
		rbActualViewPoint.addActionListener(viewPointActionListener);
		rbCustomViewPoint.addActionListener(viewPointActionListener);
		
		JPanel viewPointButtonsPanel = new JPanel(new FlowLayout());
		viewPointButtonsPanel.add(rbDefaultViewPoint);
		viewPointButtonsPanel.add(Box.createHorizontalGlue());
		viewPointButtonsPanel.add(rbActualViewPoint);
		viewPointButtonsPanel.add(Box.createHorizontalGlue());
		viewPointButtonsPanel.add(rbCustomViewPoint);
		
		final JLabel lblViewPointEditor = new JLabel();
		final JLabel lblViewPointEditorHelp = new JLabel();
		viewPointEditorPanel = 
			new ViewPointEditorPanel(ElementDataViewConfiguration.getDefault2DAngle(), is3DEnabled());
		viewPointEditorPanel.addFocusListener(FOCUS_LISTENER);
		viewPointEditorPanel.setBorder(null);
		
		final JLabel lblShowLegend = new JLabel("Show color bar legend");
		JLabel lblShowLegendHelp = new JLabel(ICON_HELP);
		lblShowLegendHelp.setToolTipText("Check if color bar legend must be shown.");
		showColorBarLegendCB = new JCheckBox();

		final JLabel lblShowAxes = new JLabel("Show axes");
		JLabel lblShowAxesHelp = new JLabel(ICON_HELP);
		lblShowAxesHelp.setToolTipText("Check if image axes must be shown.");
		showAxesCB = new JCheckBox();

		final JLabel lblShowTickLines = new JLabel("Show tick lines");
		JLabel lblShowTickLinesHelp = new JLabel(ICON_HELP);
		lblShowTickLinesHelp.setToolTipText("Check if image tick lines axes must be shown.");
		showTickLinesCB = new JCheckBox();

		final JLabel lblFileName = new JLabel("File");
		JLabel lblFileNameHelp = new JLabel(ICON_HELP);
		if (isAcceptDirectoriesOnly()) {
			lblFileNameHelp
					.setToolTipText("The selected directory to export the images.");
		} else {
			lblFileNameHelp
				.setToolTipText("The selected file to export the image.");
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
		
		final JLabel lblRangeMode = new JLabel("Range mode");
		JLabel lblRangeModeHelp = new JLabel(ICON_HELP);
		lblRangeModeHelp.setToolTipText("The color mapper range mode.");
		rangeModeCB = new JComboBox<ColorMapRangeMode>();
		rangeModeCB.setModel(new DefaultComboBoxModel<>(ColorMapRangeMode.values()));
		rangeModeCB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					colorMapRangeModeChanged();
				}
			}
		});
		
		final JLabel lblRange = new JLabel("Range");
		JLabel lblRangeHelp = new JLabel(ICON_HELP);
		lblRangeHelp.setToolTipText("The color mapper range.");
		JPanel rangePanel = getRangePanel();

		final JLabel lblImageSize = new JLabel("Size");
		JLabel lblImageSizeHelp = new JLabel(ICON_HELP);
		lblImageSizeHelp.setToolTipText("The size in px of the image.");
		JPanel imageSizePanel = getImageSizePanel();
	
		groupLayout.setHorizontalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileName, Alignment.TRAILING)
					.addComponent(lblInterpolationLevel, Alignment.TRAILING)
					.addComponent(lblColorMap, Alignment.TRAILING)
					.addComponent(lblViewerMode, Alignment.TRAILING)
					.addComponent(lblScale, Alignment.TRAILING)
					.addComponent(lblViewPoint, Alignment.TRAILING)
					.addComponent(lblViewPointEditor, Alignment.TRAILING)
					.addComponent(lblShowLegend, Alignment.TRAILING)
					.addComponent(lblShowAxes, Alignment.TRAILING)
					.addComponent(lblShowTickLines, Alignment.TRAILING)
					.addComponent(lblRangeMode, Alignment.TRAILING)
					.addComponent(lblRange, Alignment.TRAILING)
					.addComponent(lblImageSize, Alignment.TRAILING))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(fileChooserPanel)
					.addComponent(interpolationLevelCB)
					.addComponent(colorMapCB)
					.addComponent(viewerModeCB)
					.addComponent(scaleSpinner.getSpinner())
					.addComponent(viewPointButtonsPanel)
					.addComponent(viewPointEditorPanel)
					.addComponent(showColorBarLegendCB)
					.addComponent(showAxesCB)
					.addComponent(showTickLinesCB)
					.addComponent(rangeModeCB)
					.addComponent(rangePanel)
					.addComponent(imageSizePanel))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileNameHelp)
					.addComponent(lblInterpolationLevelHelp)
					.addComponent(lblColorMapHelp)
					.addComponent(lblViewerModeHelp)
					.addComponent(lblScaleHelp)
					.addComponent(lblViewPointHelp)
					.addComponent(lblViewPointEditorHelp)
					.addComponent(lblShowLegendHelp)
					.addComponent(lblShowAxesHelp)
					.addComponent(lblShowTickLinesHelp)
					.addComponent(lblRangeModeHelp)
					.addComponent(lblRangeHelp)
					.addComponent(lblImageSizeHelp))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblFileName, Alignment.CENTER)
					.addComponent(fileChooserPanel, Alignment.CENTER)
					.addComponent(lblFileNameHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblInterpolationLevel, Alignment.CENTER)
					.addComponent(interpolationLevelCB, Alignment.CENTER)
					.addComponent(lblInterpolationLevelHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblColorMap, Alignment.CENTER)
					.addComponent(colorMapCB, Alignment.CENTER)
					.addComponent(lblColorMapHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblViewerMode, Alignment.CENTER)
					.addComponent(viewerModeCB, Alignment.CENTER)
					.addComponent(lblViewerModeHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblScale, Alignment.CENTER)
					.addComponent(scaleSpinner.getSpinner(), Alignment.CENTER)
					.addComponent(lblScaleHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblViewPoint, Alignment.CENTER)
					.addComponent(viewPointButtonsPanel, Alignment.CENTER)
					.addComponent(lblViewPointHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblViewPointEditor, Alignment.CENTER)
					.addComponent(viewPointEditorPanel, Alignment.CENTER)
					.addComponent(lblViewPointEditorHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblShowLegend, Alignment.CENTER)
					.addComponent(showColorBarLegendCB, Alignment.CENTER)
					.addComponent(lblShowLegendHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblShowAxes, Alignment.CENTER)
					.addComponent(showAxesCB, Alignment.CENTER)
					.addComponent(lblShowAxesHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblShowTickLines, Alignment.CENTER)
					.addComponent(showTickLinesCB, Alignment.CENTER)
					.addComponent(lblShowTickLinesHelp, Alignment.CENTER))
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblRangeMode, Alignment.CENTER)
					.addComponent(rangeModeCB, Alignment.CENTER)
					.addComponent(lblRangeModeHelp, Alignment.CENTER))						
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblRange, Alignment.CENTER)
					.addComponent(rangePanel, Alignment.CENTER)
					.addComponent(lblRangeHelp, Alignment.CENTER))						
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(lblImageSize, Alignment.CENTER)
					.addComponent(imageSizePanel, Alignment.CENTER)
					.addComponent(lblImageSizeHelp, Alignment.CENTER))
		);
		
		final JPanel toret = new JPanel(new BorderLayout());
		toret.add(optionsPane, BorderLayout.NORTH);
		
		return toret;
	}

	private void positionModeChanged() {
		scaleSpinner.setEnabled(is3DEnabled());
		viewPointEditorPanel.set3DEnabled(is3DEnabled());
		updateViewerPointEditor();
	}
	
	private void updateViewerPointEditor() {
		if(rbDefaultViewPoint.isSelected() || rbActualViewPoint.isSelected()) {
			viewPointEditorPanel.setViewPoint(getSelectedViewPoint());
		}
	}

	public final boolean is3DEnabled() {
		return viewerModeCB.isSelected();
	}
	
	private void viewPointModeChanged() {
		boolean editable = rbCustomViewPoint.isSelected();
		this.viewPointEditorPanel.setEditable(editable);
		updateViewerPointEditor();
	}

	protected void colorMapRangeModeChanged() {
		ColorMapRangeMode selected = getSelectedRangeMode();
		boolean customRangeMode = !(selected.equals(ColorMapRangeMode.ELEMENT)
				|| selected.equals(ColorMapRangeMode.DATASET));
		if (!customRangeMode) {
			updateRangeFields();
		}
		setColorMapRangeControlsEnabled(customRangeMode);
	}

	private void updateRangeFields() {
		if(getSelectedRangeMode().equals(ColorMapRangeMode.DATASET)) {
			minRangeValue.setValue((float) datasetRange.getMin());
			maxRangeValue.setValue((float) datasetRange.getMax());
		}	
	}

	private ColorMapRangeMode getSelectedRangeMode() {
		return (ColorMapRangeMode) rangeModeCB.getSelectedItem();
	}

	private void setColorMapRangeControlsEnabled(boolean enabled) {
		minRangeValue.setEnabled(enabled);
		maxRangeValue.setEnabled(enabled);
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
	    addFocusListener(widthTF);
	    heightTF = new JFormattedTextField(formatter);
	    heightTF.setValue(1000);
	    addFocusListener(heightTF);
	    
	    imageSizePanel.add(new JLabel(" Width:"));
	    imageSizePanel.add(widthTF);
	    imageSizePanel.add(new JLabel(" Height:"));
	    imageSizePanel.add(heightTF);
	    
		return imageSizePanel;
	}
	
	private JPanel getRangePanel() {
		JPanel rangePanel = new JPanel();
		rangePanel.setLayout(new GridLayout(1, 4));
		
		minRangeValue = new FloatTextField(0);
		maxRangeValue = new FloatTextField(1);
		
		addFocusListener(minRangeValue);
		addFocusListener(maxRangeValue);
		
		setColorMapRangeControlsEnabled(false);
		
		PropertyChangeListener listener = e -> checkRangeValues();
		minRangeValue.addPropertyChangeListener("value", listener);
		maxRangeValue.addPropertyChangeListener("value", listener);
		
		rangePanel.add(new JLabel(" Min.:"));
		rangePanel.add(minRangeValue);
		rangePanel.add(new JLabel(" Max.:"));
		rangePanel.add(maxRangeValue);
		
		return rangePanel;
	}
	
	private void checkRangeValues() {
		boolean validRange = isRangeValuesValid();
		setRangeTextFieldColor(validRange);
		checkEnabled();
	}
	
	private void setRangeTextFieldColor(boolean validValues) {
		Color backgroundColor = getTextFieldBackgroundColor(validValues);
		minRangeValue.setBackground(backgroundColor);
		maxRangeValue.setBackground(backgroundColor);
	}
	
	private Color getTextFieldBackgroundColor(boolean validValues) {
		return validValues ? COLOR_VALID_INPUT : COLOR_INVALID_INPUT;
	}
	
	private boolean isRangeValuesValid() {
		return getMinRangeValue() < getMaxRangeValue();
	}

	public float getMinRangeValue() {
		return (float) minRangeValue.getValue();
	}

	public float getMaxRangeValue() {
		return (float) maxRangeValue.getValue();
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
			getSelectedFile() != null && 
			checkImageSize() &&
			isRangeValuesValid()
		);
	}
	
	private boolean checkImageSize() {
		return getSelectedHeight() > 0 && getSelectedWidth() > 0;
	}
	
	@Override
	public void setVisible(boolean b) {
		this.interpolationLevelCB.setSelectedItem(Quality.Intermediate);
		super.setVisible(b);
	}

	public int getSelectedWidth() {
		return (int) widthTF.getValue();
	}
	
	public int getSelectedHeight() {
		return (int) heightTF.getValue();
	}
	
	public int getSelectedInterpolationLevel() {
		return ((InterpolationLevel) interpolationLevelCB.getSelectedItem()).getLevel();
	}
	
	public IColorMap getSelectedColorMap() {
		return ((ColorMap) colorMapCB.getSelectedItem()).getColorMap();
	}
	
	public Coord2d getSelectedViewPoint() {
		if(rbDefaultViewPoint.isSelected()) {
			if(is3DEnabled()) {
				return ElementDataViewConfiguration.getDefault3DAngle();
			} else {
				return ElementDataViewConfiguration.getDefault2DAngle();
			}
		} else if (rbActualViewPoint.isSelected()) {
			return this.currentViewPoint;
		}  else if (rbCustomViewPoint.isSelected()) {
			return viewPointEditorPanel.getViewPoint();
		}
		throw new IllegalStateException();
	}
	
	public boolean isShowColorBarLegend() {
		return showColorBarLegendCB.isSelected();
	}
	
	public boolean isShowAxes() {
		return showAxesCB.isSelected();
	}
	
	public boolean isShowTickLines() {
		return showTickLinesCB.isSelected();
	}
	
	public Optional<Range> getSelectedColorMapRange() {
		if(getColorMapRangeMode().equals(ColorMapRangeMode.ELEMENT)) {
			return Optional.empty();
		} else {
			return Optional.of(getColorMapRange());
		}
	}

	private ColorMapRangeMode getColorMapRangeMode() {
		return (ColorMapRangeMode) rangeModeCB.getSelectedItem();
	}

	private Range getColorMapRange() {
		return new Range(getMinRangeValue(), getMaxRangeValue());
	}

	public void setInterpolationLevel(InterpolationLevel interpolationLevel) {
		this.interpolationLevelCB.setSelectedItem(interpolationLevel);
	}

	public void setColorMap(ColorMap colorMap) {
		this.colorMapCB.setSelectedItem(colorMap);
	}
	
	public void setColorMapRangeMode(ColorMapRangeMode mode) {
		this.rangeModeCB.setSelectedItem(mode);
	}
	
	public void setColorMapRange(Range range) {
		setRangeValues(range);	
	}

	public void set3DEnabled(boolean enabled) {
		this.viewerModeCB.setSelected(enabled);
		this.positionModeChanged();
	}

	public void setShowColorBarLegend(boolean show) {
		this.showColorBarLegendCB.setSelected(show);
	}
	
	public void setShowAxes(boolean show) {
		this.showAxesCB.setSelected(show);
	}
	
	public void setShowTickLines(boolean show) {
		this.showTickLinesCB.setSelected(show);
	}
	
	public void setDatasetRange(Range range) {
		this.datasetRange = range;
		setRangeValues(range);
	}
	
	public void setCurrentViewPoint(Coord2d currentViewPoint) {
		this.currentViewPoint = currentViewPoint;
		this.rbActualViewPoint.setSelected(true);
		updateViewerPointEditor();
	}

	private void setRangeValues(Range range) {
		this.minRangeValue.setValue(range.getMin());
		this.maxRangeValue.setValue(range.getMax());
	}
	
	public void setScale(double scale) {
		this.scaleSpinner.setScale(new Double(scale));
	}
	
	public double getSelectedScale() {
		return is3DEnabled() ? 
			this.scaleSpinner.getScale().doubleValue() :
			ElementDataViewConfiguration.DEFAULT_2D_SCALE.doubleValue();
	}
	
	@Override
	protected boolean isAcceptDirectoriesOnly() {
		return true;
	}
}