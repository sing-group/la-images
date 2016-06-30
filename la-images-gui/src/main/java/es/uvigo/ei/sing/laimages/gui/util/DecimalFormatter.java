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
package es.uvigo.ei.sing.laimages.gui.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * An utility class to format decimal numbers. The default parameters are:
 * <ul>
 * <li>Decimal separator: '.'.</li>
 * <li>Decimal digits: 4.</li>
 * </ul>
 * 
 * @author Hugo López-Fernández
 *
 */
public class DecimalFormatter {
	
	private static final DecimalFormat decimalFormatter =
		new DecimalFormat();
	private static final int DEFAULT_DIGITS = 4;
	
	static {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormatter.setDecimalFormatSymbols(symbols);
	}
	
	/**
	 * Formats {@code d} using the default parameters.
	 * 
	 * @param d a double to format.
	 * @return the formatted number.
	 */
	public static synchronized String format(double d) {
		setFractionDigits(DEFAULT_DIGITS);
		return decimalFormatter.format(d);
	}

	/**
	 * Formats {@code d} using the specified number of decimal digits.
	 * 
	 * @param d a double to format.
	 * @param digits the number of decimal digits.
	 * @return the formatted number.
	 */
	public static synchronized String format(double d, int digits) {
		setFractionDigits(digits);
		return decimalFormatter.format(d);
	}
	
	private static void setFractionDigits(int digits) {
		decimalFormatter.setMinimumFractionDigits(digits);
		decimalFormatter.setMaximumFractionDigits(digits);
	}
}
