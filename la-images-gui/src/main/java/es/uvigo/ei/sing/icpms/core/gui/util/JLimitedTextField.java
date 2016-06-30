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

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * A {@link JLimitedTextField} is an extension of {@link JTextField} that allows
 * specifying the maximum extension of the document ({@code Integer.MAX_VALUE} by default).
 * 
 * @author Hugo López-Fernández
 * @see JTextField
 *
 */
public class JLimitedTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	private int limit = Integer.MAX_VALUE;

	/**
	 * Constructs a {@link JLimitedTextField} with the limit specified by
	 * {@code limit}.
	 * 
	 * @param limit the initial limit.
	 */
	public JLimitedTextField(int limit) {
		super();
		this.limit = limit;
	}

	/**
	 * Constructs a {@link JLimitedTextField} with the text specified
	 * {@code string}.
	 * 
	 * @param string the initial text.
	 */
	public JLimitedTextField(String string) {
		super();
		insertString(0, string);
	}

	/**
	 * Constructs a {@link JLimitedTextField} with the limit specified by
	 * {@code limit} and the text specified by {@code text}.
	 * 
	 * @param string the initial text.
	 * @param limit the document limit.
	 */
	public JLimitedTextField(String string, int limit) {
		super();
		this.limit = limit;
		insertString(0, string);
	}
	
	/**
	 * Constructs a {@link JLimitedTextField} with the limit specified by
	 * {@code limit}, the text specified by {@code text} and the number of
	 * columns specified by {@code columns}.
	 * 
	 * @param string the document text.
	 * @param limit the document limit.
	 * @param cols the number of columns.
	 */
	public JLimitedTextField(String string, int limit, int cols) {
		super(cols);
		this.limit = limit;
		insertString(0, string);
	}

	private void insertString(int offset, String s) {
		try {
			getDocument().insertString(offset, s, null);
		} catch (BadLocationException e) {
		}
	}

	@Override
	protected Document createDefaultModel() {
		return new LimitDocument();
	}

	private class LimitDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		@Override
		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}

	}

}