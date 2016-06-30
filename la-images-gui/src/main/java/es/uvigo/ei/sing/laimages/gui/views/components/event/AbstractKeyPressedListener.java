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
import java.awt.event.KeyListener;

/**
 * An abstract implementation of {@code KeyListener} that runs {@code action} 
 * when key code returned by {@code getKeyCode} is pressed.
 * 
 * @author Hugo López-Fernández
 *
 */
public abstract class AbstractKeyPressedListener implements KeyListener {

	private Runnable action;

	/**
	 * Constructs an {@code AbstractKeyPressedListener} with the specified
	 * {@code action}.
	 * 
	 * @param action a {@code Runnable} object to invoke when key is pressed.
	 */
	public AbstractKeyPressedListener(Runnable action) {
		this.action = action;
	}
	
	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { 
		if (e.getKeyCode() == getKeyCode()) {
			action.run();
		}
	}

	protected abstract int getKeyCode();

	@Override
	public void keyReleased(KeyEvent e) {}
}
