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
package es.uvigo.ei.sing.icpms.core.io.exception;

import java.io.IOException;

import es.uvigo.ei.sing.icpms.core.io.coordinates.LineCoordinatesLoader;

/**
 * A {@code PositionsFileNotFoundException} is thrown by the
 * {@code LineCoordinatesLoader} when the positions file can not be found.
 * 
 * @author hlfernandez
 * @see LineCoordinatesLoader
 */
public class PositionsFileNotFoundException extends IOException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@code PositionsFileNotFoundException}, saving a reference
	 * to the error message string s for later retrieval by the getMessage
	 * method.
	 * 
	 * @param message the detail message.
	 */
	public PositionsFileNotFoundException(String message) {
		super(message);
	}
}
