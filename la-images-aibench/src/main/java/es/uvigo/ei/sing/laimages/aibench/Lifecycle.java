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
package es.uvigo.ei.sing.laimages.aibench;

import java.util.Locale;

import javax.help.HelpBroker;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.clipboard.ClipboardListener;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.inputgui.Common;
import es.uvigo.ei.sing.laimages.aibench.datatypes.AiBenchLaImagesAnalysis;
import es.uvigo.ei.sing.laimages.core.util.CommonFileChooser;

/**
 * Lifecycle class for the "LA-iMageS AIBench User Interface" plugin.
 * 
 * @author Hugo López-Fernández
 *
 */
public class Lifecycle extends org.platonos.pluginengine.PluginLifecycle {
	private static final Class<?>[] AUTO_OPEN_CLASSES = new Class<?>[] { 
		AiBenchLaImagesAnalysis.class 
	};
	private static final ImageIcon ICON_LAIMAGES =
		new ImageIcon(Lifecycle.class.getResource("/icons/icon.png"));
	private static final ImageIcon ICON_HELP =
		new ImageIcon(Lifecycle.class.getResource("icons/help.png"));

	/**
	 * Called when the plugin starts.
	 * 
	 * <p>
	 * By now, it does:
	 * </p>
	 * <ul>
	 * <li>Sets the default locale to {@code Locale.ENGLISH}.</li>
	 * <li>Adds a clipboard listener in order to open the datasets when
	 * they are added.</li>
	 * <li>Sets the application icon and maximize the main frame.</li>
	 * <li>Adds the "about" button to the toolbar.</li>
	 * <li>Calls methods to fix a bug in {@code JMenu}s that causes that
	 * they are rendered under the canvas.</li>
	 * </ul>
	 */
	@Override
	public void start() {
		SwingUtilities.invokeLater(() -> {
			configureLocale();
			fixJMenuBug();
			Workbench.getInstance().getMainFrame().setIconImage(
				ICON_LAIMAGES.getImage()
			);
			
			Workbench.getInstance().getMainFrame()
				.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
			Workbench.getInstance().getToolBar().add(Box.createHorizontalGlue());
			Workbench.getInstance().getToolBar().add(getHelpButton());
			AboutFrame.getInstance().addToJToolbar(
				Workbench.getInstance().getToolBar());
			
			Core.getInstance().getClipboard().addClipboardListener(
				new BringViewListener(AUTO_OPEN_CLASSES));
		});
		
		CommonFileChooser.getInstance().setFilechooser(Common.SINGLE_FILE_CHOOSER);
	}
	
	private static final void configureLocale() {
		Locale.setDefault(Locale.ENGLISH);
		JComponent.setDefaultLocale(Locale.ENGLISH);
	}
	
	/**
	 * Call the following two methods to avoid that JMenu's are rendered
	 * behind ElementDataView canvas.
	 */
	private static final void fixJMenuBug() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
	}
	
	private static JButton getHelpButton() {
		final JButton helpButton = new JButton(ICON_HELP);
		
		final HelpBroker helpBroker = Core.getInstance().getHelpBroker();
		if (helpBroker != null) {
			helpBroker.enableHelpOnButton(
				helpButton, "top", helpBroker.getHelpSet());
			
			helpBroker.enableHelpKey(
				Workbench.getInstance().getMainFrame(),
				"top", helpBroker.getHelpSet()
			);
		}
		return helpButton;
	}

	private class BringViewListener implements ClipboardListener {
		private Class<?>[] autoOpenClasses;

		public BringViewListener(Class<?>[] autoOpenClasses) {
			this.autoOpenClasses = autoOpenClasses;
		}

		public void elementAdded(ClipboardItem item) {
			for (Class<?> autoOpen : this.autoOpenClasses) {
				if (autoOpen.isAssignableFrom(item.getUserData().getClass())) {
					Workbench.getInstance().showData(item);
					break;
				}
			}
		}

		public void elementRemoved(ClipboardItem item) {

		}
	}
}
