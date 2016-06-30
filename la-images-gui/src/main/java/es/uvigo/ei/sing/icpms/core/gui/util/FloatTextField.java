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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

/**
 * An extension of {@code JFormattedTextField} to create text fields for allow
 * the input of float values.
 * 
 * @author hlfernandez
 *
 */
public class FloatTextField extends JFormattedTextField {
	private static final long serialVersionUID = 1L;
	private static NumberFormat format = DecimalFormat
			.getInstance(Locale.ENGLISH);
	private static NumberFormatter formatter = new NumberFormatter(format);

	static {
		format.setMaximumFractionDigits(6);
		formatter.setValueClass(Float.class);
		formatter.setMinimum(-Float.MAX_VALUE);
		formatter.setMaximum(Float.MAX_VALUE);
		formatter.setCommitsOnValidEdit(true);
	}

	/**
	 * Constructs a new {@code FloatTextField} and sets {@code value} as input
	 * value.
	 * 
	 * @param value the initial input value.
	 */
	public FloatTextField(float value) {
		super(formatter);
		setValue(value);
	}
}
