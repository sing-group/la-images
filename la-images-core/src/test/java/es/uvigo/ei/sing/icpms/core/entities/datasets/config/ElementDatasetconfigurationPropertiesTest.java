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
package es.uvigo.ei.sing.icpms.core.entities.datasets.config;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class ElementDatasetconfigurationPropertiesTest {

	public static final File TEST_PROPERTIES_FILE = 
		new File("src/test/resources/configuration/parameters.conf");
	@Test
	public void testLoadElementDatasetConfiguration() {
		ElementDatasetConfigurationProperties parameters = 
			new ElementDatasetConfigurationProperties(TEST_PROPERTIES_FILE);
		
		assertEquals("0.06", parameters.getAblationSpeed().get());
		assertEquals("0.527", parameters.getAcquisitionTime().get());
		assertEquals("0.080", parameters.getSpaceInterval().get());
		assertEquals("C12", parameters.getStandard().get());
	}
}
