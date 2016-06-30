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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import es.uvigo.ei.sing.laimages.gui.util.FloatTextField;
import es.uvigo.ei.sing.laimages.gui.views.components.event.EnterKeyPressedListener;

/**
 * A dialog that allows users to introduce a range of values.
 * 
 * @author Hugo López-Fernández
 *
 */
public class RangeInputDialog extends InputJDialog {
	private static final long serialVersionUID = 1L;

	public static final Color COLOR_INVALID_INPUT = new Color(255,148,148);
	public static final Color COLOR_VALID_INPUT = 
		UIManager.getColor("TextField.background");
	
	private JPanel inputComponents;
	private JFormattedTextField minRangeValue;
	private JFormattedTextField maxRangeValue;

	/**
	 * Constructs a new instance of {@link RangeInputDialog}.
	 * 
	 * @param parent the parent frame of this dialog.
	 * @param title the dialog title.
	 * @param description the dialog description.
	 */
	public RangeInputDialog(JFrame parent, String title, String description) {
		super(parent, title, description);
		this.setResizable(false);
		this.cancelButton.setVisible(false);
	}

	@Override
	protected JPanel getInputComponents() {
		if (this.inputComponents == null) {
			
			inputComponents = new JPanel(new BorderLayout());
			
			minRangeValue = new FloatTextField(0);
			maxRangeValue = new FloatTextField(1);
			PropertyChangeListener listener = e -> checkRangeValues();
			minRangeValue.addPropertyChangeListener("value", listener);
			maxRangeValue.addPropertyChangeListener("value", listener);
			KeyListener keyListener = new EnterKeyPressedListener(this::onOkButtonClicked);
			minRangeValue.addKeyListener(keyListener);
			maxRangeValue.addKeyListener(keyListener);
			minRangeValue.addFocusListener(FOCUS_LISTENER);
			maxRangeValue.addFocusListener(FOCUS_LISTENER);
	
		    JPanel minValuePanel = new JPanel();
		    minValuePanel.setLayout(new BorderLayout());
			minValuePanel.add(new JLabel("Minimum: "), BorderLayout.WEST);
			minValuePanel.add(minRangeValue, BorderLayout.CENTER);
	
			JPanel maxValuePanel = new JPanel();
			maxValuePanel.setLayout(new BorderLayout());
			maxValuePanel.add(new JLabel("Maximum: "), BorderLayout.WEST);
			maxValuePanel.add(maxRangeValue, BorderLayout.CENTER);
		    
			inputComponents.add(minValuePanel, BorderLayout.NORTH);
			inputComponents.add(maxValuePanel, BorderLayout.SOUTH);
		}
		return this.inputComponents;
	}
	
	@Override
	protected JTextArea getDescriptionPane() {
		JTextArea descripionPane = super.getDescriptionPane();
		descripionPane.setPreferredSize(new Dimension(165, 75));
		return descripionPane;
	};
	
	private void checkRangeValues() {
		boolean validRange = isRangeValuesValid();
		setRangeTextFieldColor(validRange);
		checkEnabled();
	}
	
	private boolean isRangeValuesValid() {
		return getMinValue() < getMaxValue();
	}

	public float getMaxValue() {
		return (float) maxRangeValue.getValue();
	}
	
	public void setMaxValue(float max) {
		maxRangeValue.setValue(max);
	}

	public float getMinValue() {
		return (float) minRangeValue.getValue();
	}
	
	public void setMinValue(float min) {
		minRangeValue.setValue(min);
	}

	private void setRangeTextFieldColor(boolean validValues) {
		Color backgroundColor = getTextFieldBackgroundColor(validValues);
		minRangeValue.setBackground(backgroundColor);
		maxRangeValue.setBackground(backgroundColor);
	}
	
	private Color getTextFieldBackgroundColor(boolean validValues) {
		return validValues ? COLOR_VALID_INPUT : COLOR_INVALID_INPUT;
	}

	@Override
	protected void checkEnabled() {
		this.okButton.setEnabled(isRangeValuesValid());
	}
}
