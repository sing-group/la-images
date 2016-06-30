/*
 * #%L
 * LA-iMageS
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
package es.uvigo.ei.sing.laimages.aibench.datatypes;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Property;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.sing.laimages.gui.analysis.LaImagesAnalysis;

/**
 * An AIBench datatype representing an {@code LaImagesAnalysis}.
 * 
 * @author Hugo López-Fernández
 *
 */
@Datatype(
	structure = Structure.COMPLEX, 
	namingMethod = "getName", 
	renameable = true, 
	setNameMethod = "setName"
)
public class AiBenchLaImagesAnalysis extends LaImagesAnalysis {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new instance of {@code AIBenchLaImagesAnalysis}.
	 * 
	 * @param analysis the source analysis.
	 */
	public AiBenchLaImagesAnalysis(LaImagesAnalysis analysis) {
		super(analysis.getDataset(), analysis.getConfiguration());
	}
	
	@Property(name = "Ablation speed (mm/s)")
	public double getAblationSpeed() {
		return getDataset().getConfiguration().getAblationSpeed();
	}

	@Property(name = "Acquisition time (s)")
	public double getAcquisitionTime() {
		return getDataset().getConfiguration().getAcquisitionTime();
	}
	
	@Property(name = "Space interval (mm)")
	public double getSpaceInterval() {
		return getDataset().getConfiguration().getSpaceInterval();
	}

	@Property(name = "Lines orientation")
	public String getLinesOrientation() {
		return getDataset().getElements().get(0).isVertical()?"Vertical":"Horizontal";
	}
	
	
	public void setName(String name) {
		getDataset().setName(name);
	}
	
	public String getName() {
		return getDataset().getName();
	}
}