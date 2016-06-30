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
package es.uvigo.ei.sing.laimages.gui.views.components;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * A graphical component that displays a {@code JSpinner} to set and get the
 * scale value.
 * 
 * @author Hugo López-Fernández
 *
 */
public class ScaleSpinner extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_SCALE = 1;
	public static final int DEFAULT_SCALE_MIN = 1;
	public static final int DEFAULT_SCALE_MAX = 100;
	public static final int DEFAULT_SCALE_STEP = 1;
	
	private ChangeListener scaleChangedListener;
	private JSpinner spinnerScale;

	private Number initialScaleValue = DEFAULT_SCALE;

	/**
	 * Constructs a {@code ScaleSpinner}.
	 */
	public ScaleSpinner() {
		init();
	}
	
	/**
	 * Constructs a {@code ScaleSpinner} and adds {@code scaleChangedListener} 
	 * to the {@code JSpinner}.
	 * 
	 * @param value the initial value.
	 * @param scaleChangedListener a {@code ChangeListener} to add to the
	 * {@code JSpinner} used.
	 */
	public ScaleSpinner(Number value, ChangeListener scaleChangedListener) {
		this.initialScaleValue  = value.intValue();
		this.scaleChangedListener = scaleChangedListener;
		init();
	}

	private void init() {
		this.setLayout(new GridLayout(1,2));
		SpinnerModel model = new SpinnerNumberModel(initialScaleValue,
				DEFAULT_SCALE_MIN, DEFAULT_SCALE_MAX, DEFAULT_SCALE_STEP);    
		spinnerScale = new JSpinner(model);
		this.add(new JLabel("Scale:"));
		this.add(spinnerScale);
		
		if(this.scaleChangedListener != null) {
			spinnerScale.addChangeListener(scaleChangedListener);
		}
	}
	
	public void setEnabled(boolean enabled) {
		spinnerScale.setEnabled(enabled);
	}
	
	public Number getScale() {
		return (Number) spinnerScale.getModel().getValue();
	}
	
	public void setScale(Number value) {
		spinnerScale.setValue(value);
	}
	
	public JSpinner getSpinner() {
		return this.spinnerScale;
	}
}
