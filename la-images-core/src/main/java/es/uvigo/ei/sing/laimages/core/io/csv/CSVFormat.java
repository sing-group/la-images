/*
 * #%L
 * LA-iMageS Core
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
package es.uvigo.ei.sing.laimages.core.io.csv;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * A {@code CSVFormat} specifies the format of a CSV file.
 * 
 * @author Hugo López-Fernández
 *
 */
public class CSVFormat {

	public static enum FileFormat {
		CUSTOM("Custom"),
		EXCEL("Excel compatible CSV"), 
		LIBRE_OFFICE("Libre/Open Office compatible CSV");
		
		private String description;
		
		private FileFormat(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return this.description;
		}
		
		@Override
		public String toString() {
			return getDescription();
		}
	};
	
	private String lineBreak;
	private String columnSeparator;
	private boolean quoteHeaders;
	private char decimalSeparator = '.';
	private final DecimalFormat decimalFormatter =
		new DecimalFormat("0.0000");
	private final DecimalFormatSymbols symbols =
		new DecimalFormatSymbols(Locale.getDefault());
	
	/**
	 * Constructs a new {@code CSVFormat} instance.
	 * 
	 * @param format the {@code FileFormat} to initialize the CSV format.
	 * @throws IllegalArgumentException if format is {@code CUSTOM}.
	 */
	public CSVFormat(FileFormat format) throws IllegalArgumentException {
		switch (format) {
		case EXCEL:
			this.columnSeparator = ";";
			this.lineBreak = "\r\n";
			this.quoteHeaders = false;
			break;
		case LIBRE_OFFICE:
			if (symbols.getDecimalSeparator() == ',') {
				this.columnSeparator = ";";
			} else {
				this.columnSeparator = ",";
			}
			this.lineBreak = "\n";
			this.quoteHeaders = false;
			break;
		case CUSTOM:
			throw new IllegalArgumentException(
				"FileFormat.CUSTOM cannot be used to construct a new instance."
				+ " Use constructor with all options in this case.");
		}

		decimalSeparator = symbols.getDecimalSeparator();
		
		configureFormaters();
	}
	
	/**
	 * * Constructs a new {@code CSVFormat} instance.
	 * 
	 * @param columnSeparator the column separator.
	 * @param decimalSeparator the decimal separator.
	 * @param quoteHeaders true if headers must be quote.
	 * @param lineBreak the line break.
	 */
	public CSVFormat(String columnSeparator, char decimalSeparator,
			boolean quoteHeaders, String lineBreak) {
		this.columnSeparator = columnSeparator;
		this.lineBreak = lineBreak;
		this.quoteHeaders = quoteHeaders;
		this.decimalSeparator = decimalSeparator;

		configureFormaters();
	}

	private void configureFormaters() {
		if (decimalSeparator != symbols.getDecimalSeparator()) {
			symbols.setDecimalSeparator(decimalSeparator);
			this.decimalFormatter.setDecimalFormatSymbols(symbols);
		}
	}

	/**
	 * Returns the line break.
	 * 
	 * @return the line break.
	 */
	public String getLineBreak() {
		return lineBreak;
	}
	
	/**
	 * Returns the column separator.
	 * 
	 * @return the column separator.
	 */
	public String getColumnSeparator() {
		return columnSeparator;
	}
	
	/**
	 * Returns true if headers must be quote.
	 * 
	 * @return true if headers must be quote.
	 */
	public boolean isQuoteHeaders() {
		return quoteHeaders;
	}
	
	/**
	 * Returns the decimal formatter.
	 * 
	 * @return the deciaml formatter.
	 */
	public DecimalFormat getDecimalFormatter() {
		return decimalFormatter;
	}
}
