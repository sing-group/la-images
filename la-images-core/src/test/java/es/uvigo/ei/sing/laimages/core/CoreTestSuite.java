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
package es.uvigo.ei.sing.laimages.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.ei.sing.laimages.core.entities.datasets.DatasetsTestSuite;
import es.uvigo.ei.sing.laimages.core.entities.datasets.config.ElementDatasetconfigurationPropertiesTest;
import es.uvigo.ei.sing.laimages.core.entities.datasets.coordinates.LineCoordinatesTest;
import es.uvigo.ei.sing.laimages.core.io.IOTestSuite;
import es.uvigo.ei.sing.laimages.core.io.coordinates.LoadLineCoordinatesTest;
import es.uvigo.ei.sing.laimages.core.operations.OperationsTestSuite;

@RunWith(Suite.class)
@SuiteClasses({
	DatasetsTestSuite.class,
	LineCoordinatesTest.class,
	IOTestSuite.class,
	ElementDatasetconfigurationPropertiesTest.class,
	LoadLineCoordinatesTest.class,
	OperationsTestSuite.class
})
public class CoreTestSuite {
}
