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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementDataset;
import es.uvigo.ei.sing.icpms.core.gui.analysis.LaImagesAnalysis;
import es.uvigo.ei.sing.icpms.core.gui.views.LaImagesAnalysisViewer;

public class TestUtils {
	
	/**
	 * Creates a new {@code LaImagesAnalysisViewer} for the 
	 * {@code ElementDataset dataset} and shows it.
	 * 
	 * @param dataset the {@code ElementDataset} to show.
	 */
	public static void createAndShowAnalysisViewer(ElementDataset dataset) {
		LaImagesAnalysisViewer viewer = new LaImagesAnalysisViewer(
			new LaImagesAnalysis(dataset));
		showComponent(viewer, new Dimension(1200, 900));
	}
	
	/**
	 * Creates a new {@code LaImagesAnalysisViewer} for the 
	 * {@code LaImagesAnalysis analysis} and shows it.
	 * 
	 * @param dataset the {@code LaImagesAnalysis} to show.
	 */
	public static void createAndShowAnalysisViewer(LaImagesAnalysis analysis) {
		LaImagesAnalysisViewer viewer = new LaImagesAnalysisViewer(analysis);
		showComponent(viewer, new Dimension(1200, 900));
	}
	
	/**
	 * Shows a JFrame containing the specified <code>component</code>.
	 * 
	 * @param component
	 *            JComponent to show
	 */
	public static final void showComponent(JComponent component, Dimension size) {
		fixJMenuBug();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(component);
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(size);
	}
	
	public static final void setNimbusLookAndFeel() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
	}
	
	/**
	 * Call the following two methods to avoid that JMenu's are rendered
	 * behind ElementDataView canvas.
	 */
	private static final void fixJMenuBug() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
	}
}
