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
package es.uvigo.ei.sing.laimages.gui.views;

import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_COLOR_MAP;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_COLOR_MAP_RANGE_MODE;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_INTERPOLATION;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_SCALE;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_SHOW_AXES;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_SHOW_COLOR_BAR_LEGEND;
import static es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.DEFAULT_SHOW_TICK_LINES;
import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jzy3d.events.IViewPointChangedListener;
import org.jzy3d.events.ViewPointChangedEvent;
import org.jzy3d.maths.Coord2d;

import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.laimages.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.laimages.core.io.csv.CSVFormat;
import es.uvigo.ei.sing.laimages.core.util.CommonFileChooser;
import es.uvigo.ei.sing.laimages.gui.analysis.LaImagesAnalysis;
import es.uvigo.ei.sing.laimages.gui.analysis.io.LaImagesAnalysisWriter;
import es.uvigo.ei.sing.laimages.gui.analysis.io.SerializationLaImagesAnalysisWriter;
import es.uvigo.ei.sing.laimages.gui.dialogs.ExportAllAsCsvDialog;
import es.uvigo.ei.sing.laimages.gui.dialogs.ExportAllAsPngDialog;
import es.uvigo.ei.sing.laimages.gui.dialogs.ExportAsPngDialog;
import es.uvigo.ei.sing.laimages.gui.dialogs.ExportCsvDialog;
import es.uvigo.ei.sing.laimages.gui.dialogs.RangeInputDialog;
import es.uvigo.ei.sing.laimages.gui.dialogs.TaskProgressDialog;
import es.uvigo.ei.sing.laimages.gui.export.DefaultElementDatasetToPngExporter;
import es.uvigo.ei.sing.laimages.gui.export.ElementDatasetToPngExporter;
import es.uvigo.ei.sing.laimages.gui.jzy3d.ElementDataImageConfiguration;
import es.uvigo.ei.sing.laimages.gui.jzy3d.Range;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMap;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.ColorMapRangeMode;
import es.uvigo.ei.sing.laimages.gui.views.ElementDataViewConfiguration.InterpolationLevel;
import es.uvigo.ei.sing.laimages.gui.views.components.RotationButtonsPanel;
import es.uvigo.ei.sing.laimages.gui.views.components.ScaleSpinner;
import es.uvigo.ei.sing.laimages.gui.views.components.ViewPointInformationPanel;
import es.uvigo.ei.sing.laimages.gui.views.components.event.RotationEvent;

/**
 * An {@code LaImagesAnalysisViewer} is a graphical component to display 
 * and control {@code LaImagesAnalysis} objects.
 * 
 * @author Hugo López-Fernández
 *
 */
public class LaImagesAnalysisViewer extends JPanel {
	public static final long serialVersionUID = 1L;

	protected final static FocusListener FOCUS_LISTENER = new FocusAdapter() {
		public void focusGained(FocusEvent e) {
			if (e.getComponent() instanceof JTextField) {
				SwingUtilities.invokeLater(() -> {
					((JTextField) e.getComponent()).selectAll();
				});
			}
		}
	};
	
	private final static ElementDatasetToPngExporter DATASET_EXPORTER =
		new DefaultElementDatasetToPngExporter();
	
	public final static ImageIcon ICON_AXES = getResource("icons/axes.png");
	public final static ImageIcon ICON_TICKS = getResource("icons/ticks.png");
	public final static ImageIcon ICON_SAVE = getResource("icons/save.png");
	
	private final static ImageIcon ICON_EXPORT_PNG = getResource("icons/export-png.png");
	private final static ImageIcon ICON_EXPORT_CSV = getResource("icons/export-csv.png");
	private final static ImageIcon ICON_RESET =	getResource("icons/reset.png");
	private final static ImageIcon ICON_INTERPOLATE = getResource("icons/interpolate.png");
	private final static ImageIcon ICON_COLORMAP = getResource("icons/colormap.png");
	private final static ImageIcon ICON_COLORMAP_SHOW = getResource("icons/colormap_show.png");
	private final static ImageIcon ICON_COLORMAP_RANGE = getResource("icons/colormap_range.png");
	
	private final static String ELEMENT_NAME = "ELEMENT_NAME";
	private static final Dimension PREFERRED_MENU_ITEM_SIZE = new Dimension(200, 25);
	
	private ElementDataset dataset;
	private List<String> elementNames;
	private JMenuBar menuBar;
	private JMenu exportMenu; 
	private JPanel eastPanel;
	private ElementDataView elementDataPanel;
	private JComboBox<Object> elementCombo;
	private JCheckBox viewerModeCB;
	private JCheckBoxMenuItem showColorBarLegendCB;
	private JCheckBoxMenuItem showAxesCB;
	private JCheckBoxMenuItem showTickLinesCB;
	private RotationButtonsPanel rotationButtonsPanel;
	private ViewPointInformationPanel viewPointInformationPanel;
	private ScaleSpinner scaleSpinner;
	private boolean mustUpdateElementDataPanel = true;
	private IViewPointChangedListener viewPointChangedListener = 
		this::viewPointChanged;
	private ButtonGroup interpolationButtons;
	private ButtonGroup colorMapButtons;
	private ButtonGroup rangeModeButtons;
	private Map<String, Float> minRangeValues = new HashMap<>(); 
	private Map<String, Float> maxRangeValues = new HashMap<>();

	private ElementDataViewConfiguration configuration = 
		new ElementDataViewConfiguration();
	
	/**
	 * Constructs a new instance of {@code ElementDatasetViewer}.
	 * 
	 * @param analysis the {@code LaImagesAnalysis} to display. 
	 * 	Can't be {@code null}.
	 */
	public LaImagesAnalysisViewer(LaImagesAnalysis analysis) {
		requireNonNull(analysis);
		this.dataset = requireNonNull(analysis.getDataset());
		this.configuration = requireNonNull(analysis.getConfiguration());
		this.elementNames = this.dataset.getElementNames();
		this.init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(getEastComponent(), BorderLayout.EAST);
		this.add(getCenterComponent(), BorderLayout.CENTER);
		this.add(getMenuBar(), BorderLayout.NORTH);
	}
	
	private JMenuBar getMenuBar() {
		if (this.menuBar == null) {
			this.menuBar = new JMenuBar();
			this.menuBar.add(getFileMenu());
			this.menuBar.add(getGraphicSettingsMenu());
			this.menuBar.add(getExportMenu());
		}
		return this.menuBar;
	}

	private JMenu getFileMenu() {
		JMenu file = new JMenu("File");
		file.add(new JMenuItem(getSaveAction()));
		fixMenuComponentsSize(file);
		return file;
	}

	private void fixMenuComponentsSize(JMenu menu) {
		Arrays.asList(menu.getMenuComponents()).forEach(c -> 
			c.setPreferredSize(PREFERRED_MENU_ITEM_SIZE));
	}
	
	private JMenu getGraphicSettingsMenu() {
		JMenu gS = new JMenu("Graphic settings");
		gS.add(getInterpolationLevelMenu());
		gS.add(getColorMapMenu());
		gS.add(getRangeModeMenu());
		gS.add(getShowColorBarLegendComponent());
		gS.add(getShowAxesLegendComponent());
		gS.add(getShowTickLinesComponent());
		gS.add(getResetMenuItem());
		return gS;
	}

	private JMenuItem getResetMenuItem() {
		JCheckBoxMenuItem resetMenuItem = new JCheckBoxMenuItem(getDefaultSettingsAction());
		resetMenuItem.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		resetMenuItem.setSelected(false);
		resetMenuItem.addActionListener(l -> {
			resetMenuItem.setSelected(false);
		});
		return resetMenuItem;
	}

	private JMenu getInterpolationLevelMenu() {
		JMenu interpolationLevel = new JMenu("Interpolation level");
		interpolationLevel.setIcon(ICON_INTERPOLATE);
		interpolationLevel.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		interpolationButtons = new ButtonGroup();
		Stream.of(InterpolationLevel.values()).forEach(i -> {
			JRadioButtonMenuItem item =	new InterpolationLevelRadioButtonMenuItem(i);
			interpolationButtons.add(item);
			interpolationLevel.add(item);
			
		});
		return interpolationLevel;
	}
	
	private class InterpolationLevelRadioButtonMenuItem 
		extends JRadioButtonMenuItem 
	{
		private static final long serialVersionUID = 1L;
		private InterpolationLevel level;
		
		public InterpolationLevelRadioButtonMenuItem(InterpolationLevel level) {
			super(level.toString());
			this.setSelected(level.equals(configuration.getInterpolationLevel()));
			this.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
			this.level = level;
			this.addActionListener((e) -> {
				interpolationModeChanged();
			});
		}
		
		public InterpolationLevel getLevel() {
			return level;
		}
	}
	
	private JMenu getColorMapMenu() {
		JMenu colorMap = new JMenu("Color Map");
		colorMap.setIcon(ICON_COLORMAP);
		colorMap.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		colorMapButtons = new ButtonGroup();
		Stream.of(ColorMap.values()).forEach(cm -> {
			JRadioButtonMenuItem item =	new ColorMapRadioButtonMenuItem(cm);
			this.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
			colorMapButtons.add(item);
			colorMap.add(item);
			
		});
		return colorMap;
	}

	private class ColorMapRadioButtonMenuItem extends JRadioButtonMenuItem {
		private static final long serialVersionUID = 1L;
		private ColorMap colorMap;
		
		public ColorMapRadioButtonMenuItem(ColorMap colorMap) {
			super(colorMap.toString());
			this.setSelected(colorMap.equals(configuration.getColorMap()));
			this.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
			this.colorMap = colorMap;
			this.addActionListener((e) -> {
				colorMapChanged();
			});
		}
		
		public ColorMap getColorMap() {
			return colorMap;
		}
	}
	
	private JMenu getRangeModeMenu() {
		JMenu rangeMode = new JMenu("Range Mode");
		rangeMode.setIcon(ICON_COLORMAP_RANGE);
		rangeMode.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		rangeModeButtons = new ButtonGroup();
		Stream.of(ColorMapRangeMode.values()).forEach(cm -> {
			JRadioButtonMenuItem item =	new ColorMapRangeRadioButtonMenuItem(cm);
			rangeModeButtons.add(item);
			rangeMode.add(item);
			
		});
		
		return rangeMode;
	}

	private class ColorMapRangeRadioButtonMenuItem extends JRadioButtonMenuItem {
		private static final long serialVersionUID = 1L;
		private ColorMapRangeMode colorMapRangeMode;
		
		public ColorMapRangeRadioButtonMenuItem(ColorMapRangeMode colorMapRangeMode) {
			super(colorMapRangeMode.toString());
			this.colorMapRangeMode = colorMapRangeMode;
			this.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
			this.setSelected(colorMapRangeMode.equals(configuration.getRangeMode()));
			if(configuration.getColorMapRange().isPresent()) {
				this.setRange(configuration.getColorMapRange().get());
			}
			this.addActionListener((e) -> {
				colorMapRangeModeChanged();
			});
		}
		
		public ColorMapRangeMode getColorMapRangeMode() {
			return colorMapRangeMode;
		}

		public void setRange(Range range) {
			if(getColorMapRangeMode().equals(ColorMapRangeMode.CUSTOM)) {
				this.setText(ColorMapRangeMode.CUSTOM + " [" + range.getMin() + 
					", " + range.getMax() + "]");
			}
		}
	}	

	private JCheckBoxMenuItem getShowColorBarLegendComponent() {
		showColorBarLegendCB = new JCheckBoxMenuItem("Show color bar legend",
			ICON_COLORMAP_SHOW, configuration.isShowColorBarLegend());
		showColorBarLegendCB.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		showColorBarLegendCB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				showColorBarLegendChanged();
			}
		});
		return showColorBarLegendCB;
	}
	
	private JCheckBoxMenuItem getShowAxesLegendComponent() {
		showAxesCB = new JCheckBoxMenuItem("Show axes",
			ICON_AXES, configuration.isShowAxes());
		showAxesCB.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		showAxesCB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				showAxesChanged();
			}
		});
		return showAxesCB;
	}
	
	private JCheckBoxMenuItem getShowTickLinesComponent() {
		showTickLinesCB = new JCheckBoxMenuItem("Show tick lines",
			ICON_TICKS, configuration.isShowTickLines());
		showTickLinesCB.setPreferredSize(PREFERRED_MENU_ITEM_SIZE);
		showTickLinesCB.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				showTickLinesChanged();
			}
		});
		return showTickLinesCB;
	}

	private JMenu getExportMenu() {
		if(exportMenu == null) {
			exportMenu = new JMenu("Export...");
			exportMenu.add(new JMenuItem(getExportAsImageAction()));
			exportMenu.add(new JMenuItem(getExportAllAsImageAction()));
			exportMenu.add(new JMenuItem(getExportAsCsvAction()));
			exportMenu.add(new JMenuItem(getExportAllAsCsvAction()));
			fixMenuComponentsSize(exportMenu);
		}
		return exportMenu;
	}

	private Component getCenterComponent() {
		if (elementDataPanel == null) {
			this.elementDataPanel = createElementDataView(
				this.dataset.getElements().get(configuration.getElementIndex()));
		}
		return elementDataPanel;
	}

	private Component getEastComponent() {
		if(this.eastPanel == null) {
			this.eastPanel = new JPanel(new BorderLayout());
			
			JPanel northPanel = new JPanel(new BorderLayout());
			
			JPanel optionsPanel = new JPanel();
			optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
			optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
			JPanel elementPanel = new JPanel();
			elementPanel.setLayout(new GridLayout(2,1));
			elementPanel.add(new JLabel("Element:"));
			elementCombo = new JComboBox<Object>(elementNames.toArray());
			elementCombo.setSelectedIndex(configuration.getElementIndex());
			elementCombo.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						selectedElementChanged();
					}
				}
			});
			elementPanel.add(elementCombo);

			JPanel viewerModePanel = new JPanel(new BorderLayout());
			viewerModePanel.setBorder(BorderFactory.createTitledBorder("3D viewer"));
			viewerModeCB = new JCheckBox("Enable 3D view", configuration.is3DEnabled());
			viewerModeCB.addItemListener((e) -> {
				positionModeChanged();
			});
			
			scaleSpinner = new ScaleSpinner(configuration.getScale(),
					e -> this.scaleChanged());
			scaleSpinner.setEnabled(configuration.is3DEnabled());
			scaleSpinner.setToolTipText("The scaling ratio. It is only applied "
				+ "if 3D mode is active");
			((JSpinner.DefaultEditor) scaleSpinner.getSpinner().getEditor())
				.getTextField().addFocusListener(FOCUS_LISTENER);
			
			viewerModePanel.add(viewerModeCB, BorderLayout.NORTH);
			viewerModePanel.add(scaleSpinner, BorderLayout.SOUTH);
			
			optionsPanel.add(elementPanel);
			optionsPanel.add(Box.createVerticalStrut(5));
			optionsPanel.add(viewerModePanel);
			optionsPanel.add(Box.createVerticalGlue());
			
			rotationButtonsPanel = new RotationButtonsPanel();
			rotationButtonsPanel.addRotationListener(this::rotateViewPoint);
			rotationButtonsPanel.set3DEnabled(is3DEnabled());
			
			viewPointInformationPanel = new ViewPointInformationPanel();
			viewPointInformationPanel.set3DEnabled(is3DEnabled());
			
			JPanel cameraControlPanel = new JPanel();
			cameraControlPanel.setLayout(new BoxLayout(cameraControlPanel, BoxLayout.Y_AXIS));
			cameraControlPanel.setBorder(BorderFactory.createTitledBorder("Camera control"));
			cameraControlPanel.add(rotationButtonsPanel);
			cameraControlPanel.add(Box.createVerticalStrut(2));
			cameraControlPanel.add(viewPointInformationPanel);
			cameraControlPanel.add(Box.createVerticalGlue());
			
			northPanel.add(optionsPanel, BorderLayout.NORTH);
			northPanel.add(cameraControlPanel, BorderLayout.SOUTH);
			
			this.eastPanel.add(northPanel, BorderLayout.NORTH);
		}
		return this.eastPanel;
	}

	private Action getSaveAction() {
		return new AbstractAction("Save analysis", ICON_SAVE) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveFC = CommonFileChooser.getInstance().getFilechooser();
				saveFC.setSelectedFile(new File(dataset.getName() + ".lai"));
				int returnVal =	saveFC.showSaveDialog(getParentFrame());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					saveAnalysis(saveFC.getSelectedFile());
				}
			}
		};
	}
	
	private void saveAnalysis(File file) {
		LaImagesAnalysisWriter writer = new SerializationLaImagesAnalysisWriter();
		try {
			writer.write(new LaImagesAnalysis(dataset, getViewConfiguration()), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ElementDataViewConfiguration getViewConfiguration() {
		ElementDataViewConfiguration viewConfiguration = elementDataPanel.getViewConfiguration();
		viewConfiguration.setElementIndex(getSelectedElementDataIndex());
		viewConfiguration.setRangeMode(getSelectedRangeMode());
		return viewConfiguration;
	}

	private Action getExportAsImageAction() {
		return new AbstractAction("As image ", ICON_EXPORT_PNG) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ExportAsPngDialog dialog = new ExportAsPngDialog(
						getParentFrame(),
						getSelectedElementData().getName() + ".png"
					);

				dialog.setVisible(true);

				if (!dialog.isCanceled()) {
					exportCurrentElementToPNG(
						dialog.getSelectedFile(),
						dialog.getSelectedWidth(),
						dialog.getSelectedHeight()
					);
				}
			}
		};
	}
	
	private Action getExportAllAsImageAction() {
		return new AbstractAction("All as image ", ICON_EXPORT_PNG) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ExportAllAsPngDialog dialog = new ExportAllAsPngDialog(
					getParentFrame());
				dialog.setInterpolationLevel(getSelectedInterpolation());
				dialog.setColorMap(getSelectedColorMap());
				dialog.set3DEnabled(is3DEnabled());
				dialog.setShowColorBarLegend(isShowColorBarLegend());
				dialog.setShowAxes(isShowAxes()); 
				dialog.setShowTickLines(isShowTickLines()); 
				dialog.setDatasetRange(getDatasetRange());
				dialog.setCurrentViewPoint(getCurrentViewPoint());
				dialog.setColorMapRangeMode(getSelectedRangeMode());
				dialog.setColorMapRange(getCurrentColorMapRange());
				dialog.setScale(getScale());
				dialog.setVisible(true);
				
				if (!dialog.isCanceled()) {
					exportDatasetToPNG(
						dialog.getSelectedFile(), 
						new ElementDataImageConfiguration(
							dialog.getSelectedWidth(), 
							dialog.getSelectedHeight(),
							elementDataPanel.getQuality(),
							dialog.getSelectedInterpolationLevel(),
							dialog.getSelectedColorMap(),
							dialog.getSelectedColorMapRange(),
							dialog.getSelectedViewPoint(),
							dialog.isShowColorBarLegend(),
							dialog.isShowAxes(),
							dialog.isShowTickLines(),
							dialog.getSelectedScale(),
							dialog.is3DEnabled()
						)
					);
				}
			}
		};
	}
	
	private Action getExportAsCsvAction() {
		return new AbstractAction("As CSV",	ICON_EXPORT_CSV) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				ExportCsvDialog dialog = new ExportCsvDialog(
						getParentFrame(), 
						getSelectedElementData().getName()+".csv"
					);

				dialog.setVisible(true);

				if (!dialog.isCanceled()) {
					try {
						dataset.toCSV(
								getSelectedElementData(),
								dialog.getSelectedFile(),
								dialog.getCSVFormat()
							);
					} catch (IOException e1) {
						JOptionPane
							.showMessageDialog(
								getParentFrame(),
								"An error occured while exporting the element to CSV.",
								"Export Error",
								JOptionPane.ERROR_MESSAGE
						);
					}
				}
			}
		};
	}
	
	private Action getExportAllAsCsvAction() {
		return new AbstractAction("All as CSV", ICON_EXPORT_CSV) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ExportAllAsCsvDialog dialog = new ExportAllAsCsvDialog(
						getParentFrame());
				dialog.setVisible(true);

				if (!dialog.isCanceled()) {
					exportDatasetToCSV(dialog.getSelectedFile(), dialog.getCSVFormat());
				}
			}
		};
	}
	
	private Action getDefaultSettingsAction() {
		return new AbstractAction("Reset to default", ICON_RESET) {
			private static final long serialVersionUID = 1L; 

			@Override
			public void actionPerformed(ActionEvent e) {
				setDefaultSettings();
			}
		};
	}

	protected Coord2d getCurrentViewPoint() {
		return elementDataPanel.getCurrentViewPoint();
	}

	public float getMinRangeValue() {
		ColorMapRangeMode rangeMode = getSelectedRangeMode();
		ElementData selectedElement = getSelectedElementData();
		float minRange = 0f;
		
		if (rangeMode.equals(ColorMapRangeMode.ELEMENT)) {
			minRange = (float) selectedElement.getMinValue();
		} else if (rangeMode.equals(ColorMapRangeMode.DATASET)) {
			minRange = (float) getDatasetRange().getMin();
		} else {
			minRange = minRangeValues.getOrDefault(
				selectedElement.getName(), 
				(float) selectedElement.getMinValue()
			);
		}
		
		return minRange;
	}

	public float getMaxRangeValue() {
		ColorMapRangeMode rangeMode = getSelectedRangeMode();
		ElementData selectedElement = getSelectedElementData();
		float maxRange = 0f;
		
		if (rangeMode.equals(ColorMapRangeMode.ELEMENT)) {
			maxRange = (float) selectedElement.getMaxValue();
		} else if (rangeMode.equals(ColorMapRangeMode.DATASET)) {
			maxRange = (float) getDatasetRange().getMax();
		} else {
			maxRange = maxRangeValues.getOrDefault(
				selectedElement.getName(), 
				(float) selectedElement.getMaxValue()
			);
		}
		
		return maxRange;
	}

	private void colorMapRangeModeChanged() {
		if (isCustomRangeMode()) {
			readCustomRangemode();
		}
		applyColorMapRange();
	}

	private boolean isCustomRangeMode() {
		ColorMapRangeMode selected = getSelectedRangeMode();
		return 	!(selected.equals(ColorMapRangeMode.ELEMENT)
				|| selected.equals(ColorMapRangeMode.DATASET));
	}
	
	private void readCustomRangemode() {
		RangeInputDialog rangeInput = new RangeInputDialog(getParentFrame(), 
			"Color map range", "Allows you to set the custom color map range.");
		rangeInput.setMinValue(getMinRangeValue());
		rangeInput.setMaxValue(getMaxRangeValue());
		rangeInput.setVisible(true);
		
		float minRangeValue = rangeInput.getMinValue();
		float maxRangeValue = rangeInput.getMaxValue();
		
		this.elementNames.forEach(n -> {
			this.minRangeValues.putIfAbsent(n, minRangeValue);
			this.maxRangeValues.putIfAbsent(n, maxRangeValue);
		});
		
		String selectedElement = getSelectedElementData().getName();
		
		this.minRangeValues.put(selectedElement, minRangeValue);
		this.maxRangeValues.put(selectedElement, maxRangeValue);
		
		ColorMapRangeRadioButtonMenuItem customRangeItem = getCustomRangeItem();
		customRangeItem.setRange(getCurrentColorMapRange());
	}
	
	private ColorMapRangeRadioButtonMenuItem getCustomRangeItem() {
		return ((ColorMapRangeRadioButtonMenuItem) 
				Collections.list(rangeModeButtons.getElements()).stream()
				.filter(b -> 
					((ColorMapRangeRadioButtonMenuItem) b).getColorMapRangeMode().equals(ColorMapRangeMode.CUSTOM)
				)
				.findFirst().get()
			);
	}

	private Range getDatasetRange() {
		return new Range((float) dataset.getMinValue(),
				(float) dataset.getMaxValue());
	}
	
	private ColorMapRangeMode getSelectedRangeMode() {
		return ((ColorMapRangeRadioButtonMenuItem) 
				Collections.list(rangeModeButtons.getElements()).stream()
				.filter(isSelected())
				.findFirst().get()
			).getColorMapRangeMode();
	}

	private void applyColorMapRange() {
		if(!mustUpdateElementDataPanel()) {
			return;
		}
		
		if(getSelectedRangeMode().equals(ColorMapRangeMode.ELEMENT)) {
			elementDataPanel.removeColorMapRange();
		} else {
			elementDataPanel.setColorMapRange(
				getCurrentColorMapRange()
			); 
		}
		
		if (getSelectedRangeMode().equals(ColorMapRangeMode.CUSTOM)) {
			getCustomRangeItem().setRange(getCurrentColorMapRange());
		}
	}
	
	private boolean mustUpdateElementDataPanel() {
		return mustUpdateElementDataPanel;
	}

	private Range getCurrentColorMapRange() {
		return new Range(getMinRangeValue(), getMaxRangeValue());
	}
	
	private void rotateViewPoint(RotationEvent e) {
		this.elementDataPanel.rotateViewPoint(e.getAngle());
	}

	private void exportCurrentElementToPNG(File file, int width, int height) {
		Thread t = new Thread( () -> {
			try {
				DATASET_EXPORTER.toPNG(
					getSelectedElementData(),
					file, 
					new ElementDataImageConfiguration(
						width, 
						height, 
						elementDataPanel.getQuality(), 
						elementDataPanel.getInterpolationLevel(), 
						elementDataPanel.getColorMap(), 
						Optional.of(elementDataPanel.getColorMapRange()),
						elementDataPanel.getCurrentViewPoint(), 
						elementDataPanel.isShowColorBarLegend(),
						elementDataPanel.isShowAxes(),
						elementDataPanel.isShowTickLines(),
						elementDataPanel.getScale(),
						is3DEnabled()
					)
				);
			} catch (Exception e) {
				JOptionPane
					.showMessageDialog(
						getParentFrame(),
						"An error occured while exporting the element to PNG.",
						"Export Error",
						JOptionPane.ERROR_MESSAGE
					);
			}
		});
		t.start();
	}

	private final ElementDataView createElementDataView(ElementData element) {
		ElementDataView elementDataView = 
			new ElementDataView(element, this.configuration, viewPointChangedListener);
		return elementDataView;
	}

	private void exportDatasetToCSV(File selectedFile, CSVFormat csvFormat) {
		setExportButtonsEnabled(false);
		List<String> taskNames = getExportElementToCSVTaskNames();
		TaskProgressDialog dialog = new TaskProgressDialog(
				getParentFrame(),
				"Exporting dataset to CSV",
				taskNames
			);
		
		Thread t = new Thread( () -> {
			dialog.setVisible(true);
			try {
				dataset.toCSV(selectedFile, csvFormat, () -> {
					dialog.nextTask();
				});
			} catch (Exception e1) {
				JOptionPane
						.showMessageDialog(
								getParentFrame(),
								"An error occured while exporting the element to CSV.",
								"Export Error", JOptionPane.ERROR_MESSAGE);
			}
			dialog.finished("Finished");
			dialog.dispose();
			setExportButtonsEnabled(true);
		});
		t.start();
	}

	private List<String> getExportElementToCSVTaskNames() {
		return getExportElementTaskNames("Task: export element " + ELEMENT_NAME
			+ " to file " + ELEMENT_NAME + ".csv");
	}

	private List<String> getExportElementTaskNames(String task) {
		List<String> taskNames = new LinkedList<String>();
		this.elementNames.stream().forEach(
				e -> taskNames.add(task.replaceAll(ELEMENT_NAME, e)));
		return taskNames;
	}

	private void exportDatasetToPNG(File directory, ElementDataImageConfiguration configuration) {
		setExportButtonsEnabled(false);
		List<String> taskNames = getExportElementToPNGTaskNames();
		
		TaskProgressDialog dialog = new TaskProgressDialog(
				getParentFrame(), 
				"Exporting dataset to PNG", 
				taskNames
			);
		Thread t = new Thread(() -> {
			dialog.setVisible(true);
			try {
				DATASET_EXPORTER.toPNG(dataset,
					directory,
					configuration,
					() -> dialog.nextTask()
				);
			} catch (Exception e) {
				JOptionPane
					.showMessageDialog(
						getParentFrame(),
						"An error occured while exporting the dataset to PNG.",
						"Export Error",
						JOptionPane.ERROR_MESSAGE);
			}
				dialog.nextTask();
			dialog.finished("Finished");
			dialog.dispose();
			setExportButtonsEnabled(true);
		});
		t.start();
	}
	
	private List<String> getExportElementToPNGTaskNames() {
		return getExportElementTaskNames("Task: export element " + ELEMENT_NAME
				+ " to file " + ELEMENT_NAME + ".png");
	}

	private void setExportButtonsEnabled(boolean enabled) {
		Arrays.asList((exportMenu.getMenuComponents())).forEach( c -> {
				((JMenuItem)c).setEnabled(enabled);
			});
		this.updateUI();
	}

	private void colorMapChanged() {
		elementDataPanel.setColorMap(getSelectedColorMap());
	}
	
	private void scaleChanged() {
		if(mustUpdateElementDataPanel()) {
			elementDataPanel.setScale(getScale());	
		}
		updateColorMapRange();
	}

	private void updateColorMapRange() {
		applyColorMapRange();
	}

	private double getScale() {
		return scaleSpinner.getScale().doubleValue();
	}

	private void showColorBarLegendChanged() {
		elementDataPanel.setShowColorBarLegend(isShowColorBarLegend());
	}

	private boolean isShowColorBarLegend() {
		return showColorBarLegendCB.isSelected();
	}
	
	private void showAxesChanged() {
		elementDataPanel.setShowAxes(isShowAxes());
	}
	
	private boolean isShowAxes() {
		return showAxesCB.isSelected();
	}
	
	private void showTickLinesChanged() {
		elementDataPanel.setShowTickLines(isShowTickLines());
	}

	private boolean isShowTickLines() {
		return showTickLinesCB.isSelected();
	}

	private final void setDefaultSettings() {
		this.elementDataPanel.setDefaultSettings();
		this.mustUpdateElementDataPanel = false;
		setDefaultScaleIntensity();
		setDefaultViewMode();
		setDefaultInterpolationLevel();
		setDefaultColorMap();
		setDefaultShowColorBarLegend();
		setDefaultShowAxes();
		setDefaultShowTickLines();
		setDefaultColorMapRangeMode();
		this.mustUpdateElementDataPanel = true;
		
	}

	private void setDefaultScaleIntensity() {
		scaleSpinner.setScale(DEFAULT_SCALE);
	}

	private final void setDefaultShowColorBarLegend() {
		showColorBarLegendCB.setSelected(DEFAULT_SHOW_COLOR_BAR_LEGEND);
		if(mustUpdateElementDataPanel()) {
			elementDataPanel.setShowColorBarLegend(DEFAULT_SHOW_COLOR_BAR_LEGEND);
		}
	}
	
	private final void setDefaultShowAxes() {
		showAxesCB.setSelected(DEFAULT_SHOW_AXES);
		if(mustUpdateElementDataPanel()) {
			elementDataPanel.setShowAxes(DEFAULT_SHOW_AXES);
		}
	}
	
	private final void setDefaultShowTickLines() {
		showTickLinesCB.setSelected(DEFAULT_SHOW_TICK_LINES);
		if(mustUpdateElementDataPanel()) {
			elementDataPanel.setShowTickLines(DEFAULT_SHOW_TICK_LINES);
		}
	}
	
	private final void setDefaultColorMap() {
		Collections.list(colorMapButtons.getElements()).forEach(a -> {
			ColorMapRadioButtonMenuItem c = ((ColorMapRadioButtonMenuItem) a);
			c.setSelected(c.getColorMap().equals(DEFAULT_COLOR_MAP));	
		});		
		if(mustUpdateElementDataPanel()) {
			elementDataPanel.setColorMap(getSelectedColorMap());
		}
	}
	
	private final void setDefaultInterpolationLevel() {
		Collections.list(interpolationButtons.getElements()).forEach(a -> {
			InterpolationLevelRadioButtonMenuItem i = ((InterpolationLevelRadioButtonMenuItem) a);
			i.setSelected(i.getLevel().equals(DEFAULT_INTERPOLATION));	
		});
		if(mustUpdateElementDataPanel()) {
			elementDataPanel.setInterpolation(getSelectedInterpolation());
		}
	}
	
	private final void setDefaultViewMode() {
		this.viewerModeCB.setSelected(false);
		if(mustUpdateElementDataPanel()) {
			set3DEnabled(is3DEnabled());
		}
	}
	
	private void set3DEnabled(boolean is3dEnabled) {
		elementDataPanel.set3DEnabled(is3dEnabled);
		rotationButtonsPanel.set3DEnabled(is3dEnabled);
		viewPointInformationPanel.set3DEnabled(is3DEnabled());
		scaleSpinner.setEnabled(is3dEnabled);
	}

	private final void setDefaultColorMapRangeMode() {
		Collections.list(rangeModeButtons.getElements()).forEach(a -> {
			ColorMapRangeRadioButtonMenuItem i = ((ColorMapRangeRadioButtonMenuItem) a);
			i.setSelected(i.getColorMapRangeMode().equals(DEFAULT_COLOR_MAP_RANGE_MODE));	
		});		

		if(mustUpdateElementDataPanel()) {
			elementDataPanel.removeColorMapRange();
		}
	}

	private final boolean is3DEnabled() {
		return viewerModeCB.isSelected();
	}
	
	private final void positionModeChanged() {
		set3DEnabled(is3DEnabled());
	}

	private final void selectedElementChanged() {
		elementDataPanel.setElementData(getSelectedElementData());
		applyColorMapRange();
	}
	
	private final void interpolationModeChanged() {
		elementDataPanel.setInterpolation(getSelectedInterpolation());
	}

	private final ElementData getSelectedElementData() {
		return this.dataset.getElements().get(this.elementCombo.getSelectedIndex());
	}
	
	private int getSelectedElementDataIndex() {
		return this.dataset.getElements().indexOf(getSelectedElementData());
	}
	
	private final InterpolationLevel getSelectedInterpolation() {
		return ((InterpolationLevelRadioButtonMenuItem) 
				Collections.list(interpolationButtons.getElements()).stream()
				.filter(isSelected())
				.findFirst().get()
			).getLevel();
	}

	private Predicate<? super AbstractButton> isSelected() {
		return b -> ((JRadioButtonMenuItem) b).isSelected();
	}
	
	private final ColorMap getSelectedColorMap() {
		return ((ColorMapRadioButtonMenuItem) 
				Collections.list(colorMapButtons.getElements()).stream()
				.filter(isSelected())
				.findFirst().get()
			).getColorMap();
	}
	
	private void viewPointChanged(ViewPointChangedEvent e) {
		viewPointInformationPanel.viewPointChanged(e.getViewPoint());
		rotationButtonsPanel.viewPointChanged(e.getViewPoint());
	}
	
	private final JFrame getParentFrame() {
		return (JFrame) SwingUtilities.getWindowAncestor(this);
	}
	
	private static final ImageIcon getResource(String resource) {
		return new ImageIcon(LaImagesAnalysisViewer.class.getResource(resource));
	}
}
