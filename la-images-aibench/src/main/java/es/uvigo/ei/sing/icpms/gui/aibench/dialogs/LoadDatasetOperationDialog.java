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
package es.uvigo.ei.sing.icpms.gui.aibench.dialogs;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.inputgui.ParamProvider;
import es.uvigo.ei.aibench.workbench.inputgui.ParamsWindow;
import es.uvigo.ei.sing.icpms.gui.aibench.operations.LoadDatasetOperation;

/**
 * An extension of {@code ParamsWindow} to retrieve {@code LoadDatasetOperation}
 * text fields in order to listen for changes in the data directory port and
 * try to load configuration parameters into other ports.
 * 
 * @author hlfernandez
 *
 */
public class LoadDatasetOperationDialog extends ParamsWindow {
	private static final long serialVersionUID = 1L;
	private LoadDatasetOperationConfigurationController configurationController =
		new LoadDatasetOperationConfigurationController();

	protected ParamProvider getParamProvider(final Port arg0,
			final Class<?> arg1, final Object arg2
	) {
		ParamProvider paramProvider = super.getParamProvider(arg0, arg1, arg2);
		
		if(arg0.name().equals(LoadDatasetOperation.PORT_NAME_DATA_DIRECTORY)) {
			lookTextField(paramProvider).getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void removeUpdate(DocumentEvent e) { }
	
					@Override
					public void insertUpdate(DocumentEvent e) {
						dataDirectoryChanged(e);
					}
	
					@Override
					public void changedUpdate(DocumentEvent e) { }
			});
		} else if (arg0.name().equals(
				LoadDatasetOperation.PORT_NAME_ABLATION_SPEED)) {
			JTextField ablationSpeedTF = lookTextField(paramProvider);
			ablationSpeedTF.getDocument().addDocumentListener(
					new CustomDocumentListener(ablationSpeedTF));
			configurationController.setAblationSpeedTextField(ablationSpeedTF);
		} else if (arg0.name().equals(
				LoadDatasetOperation.PORT_NAME_ACQUISITION_TIME)) {
			JTextField acquisitionTimeTF = lookTextField(paramProvider);
			acquisitionTimeTF.getDocument().addDocumentListener(
					new CustomDocumentListener(acquisitionTimeTF));
			configurationController.setAcquisitionTimeTextField(acquisitionTimeTF);
		} else if (arg0.name().equals(
				LoadDatasetOperation.PORT_NAME_SPACE_INTERVAL)) {
			JTextField spaceIntervalTF = lookTextField(paramProvider);
			spaceIntervalTF.getDocument().addDocumentListener(
					new CustomDocumentListener(spaceIntervalTF));
			configurationController.setSpaceIntervalTextField(spaceIntervalTF);
		} else if (arg0.name().equals(
				LoadDatasetOperation.PORT_NAME_STANDARD)) {
			JTextField standardTF = lookTextField(paramProvider);
			standardTF.getDocument().addDocumentListener(
					new CustomDocumentListener(standardTF));
			configurationController.setStandardTextField(standardTF);
		}
		
		return paramProvider;
	}

	private JTextField lookTextField(ParamProvider paramProvider) {
		JComponent component = paramProvider.getComponent();
		if (component instanceof JTextField) {
			return (JTextField) component;
		}
		for(Component c : component.getComponents()) {
			if (c instanceof JTextField) {
				return (JTextField) c;
			}
		}
		throw new RuntimeException("LoadDatasetOperation: port's text "
			+ "fields not found");
	}
	
	private void dataDirectoryChanged(DocumentEvent e) {
		String dataDirectory;
		try {
			dataDirectory = e.getDocument().getText(0,
					e.getDocument().getLength());
			configurationController.dataDirectoryChanged(dataDirectory);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
	
	private class CustomDocumentListener implements DocumentListener {
		
		private JTextField textField;

		public CustomDocumentListener(JTextField tf) {
			this.textField = tf;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			textField.setBackground(UIManager.getColor("TextField.background"));
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			textField.setBackground(UIManager.getColor("TextField.background"));
		}

		@Override
		public void changedUpdate(DocumentEvent e) { }
	}
}
