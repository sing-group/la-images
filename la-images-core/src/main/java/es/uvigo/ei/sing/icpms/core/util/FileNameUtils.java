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
package es.uvigo.ei.sing.icpms.core.util;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class to support common file name operations.
 * 
 * @author hlfernandez
 *
 */
public class FileNameUtils {

	/**
	 * Returns a File located at {@code dir} with the given {@code name} and
	 * {@code extension}. If the name is not available, it will add an
	 * autonumeric suffix to the name.
	 * 
	 * @param dir the directory location.
	 * @param name the file name.
	 * @param extension the file extension.
	 * @return a File located at {@code dir} with the given {@code name} and
	 * {@code extension}. If the name is not available, it will add an
	 * autonumeric suffix to the name.
	 */
	public final static File getFile(String dir, String name, String extension) {
		File target = new File(dir, name + extension);
		int i = 1;
		while (target.exists()) {
			target = new File(dir, name + "_" + i + extension);
			i++;
		}
		return target;
	}
	
	/**
	 * Finds file named {@code name} located at {@code dir} ignoring case and
	 * returns an {@code Optional<File>} containing the it if it can be found.
	 * 
	 * @param dir the directory location.
	 * @param name the file name.
	 * @return an {@code Optional<File>}.
	 */
	public static final Optional<File> findFileIgnoreCase(File dir, String name) {
		return 	Stream.of(
					dir.listFiles(f -> f.getName().equalsIgnoreCase(name))
				).findFirst();
	}
}
