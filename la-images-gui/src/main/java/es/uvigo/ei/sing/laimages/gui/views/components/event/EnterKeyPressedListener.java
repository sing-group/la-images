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
package es.uvigo.ei.sing.laimages.gui.views.components.event;

import java.awt.event.KeyEvent;

/**
 * A concrete implementation of {@code AbstractKeyPressedListener} to listen
 * for enter key.
 * 
 * @author Hugo López-Fernández
 *
 */
public class EnterKeyPressedListener extends AbstractKeyPressedListener {

	/**
	 * Constructs an {@code EnterKeyPressedListener} with the specified
	 * {@code action}.
	 * 
	 * @param action a {@code Runnable} object to invoke when the enter key
	 * 	is pressed.
	 */
	public EnterKeyPressedListener(Runnable action) {
		super(action);
	}

	@Override
	protected int getKeyCode() {
		return KeyEvent.VK_ENTER;
	}

}
