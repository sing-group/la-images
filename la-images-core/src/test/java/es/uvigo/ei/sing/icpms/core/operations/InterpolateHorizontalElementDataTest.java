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
package es.uvigo.ei.sing.icpms.core.operations;

import static es.uvigo.ei.sing.icpms.core.TestUtils.assertElementDataEquals;

import org.junit.Test;

import es.uvigo.ei.sing.icpms.core.entities.datasets.ElementData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.LineData;
import es.uvigo.ei.sing.icpms.core.entities.datasets.coordinates.HorizontalLineCoordinates;

public class InterpolateHorizontalElementDataTest {

	@Test
	public void testInterpolateElementDataWithLevel0() {
		final ElementData toInterpolate = ElementData.createElementData(
			"To interpolate",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d },
					new HorizontalLineCoordinates(0.1d, 0d, 0d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 1d, 2d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.1d, 0.1d)
				)
			}
		);

		final ElementData interpolated = Interpolator.interpolate(toInterpolate, 0);

		final ElementData expectedInterpolated = ElementData.createElementData(
			"Interpolated",
			new LineData[] {
				new LineData(
					"Line 1",
					new double[] { 1d },
					new HorizontalLineCoordinates(0.1d, 0d, 0d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 1d, 2d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.1d, 0.1d)
				)
			}
		);

		assertElementDataEquals(expectedInterpolated, interpolated);
	}

	@Test
	public void testInterpolateElementDataWithLevel1_1() {
		final ElementData toInterpolate = ElementData.createElementData(
			"To interpolate",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d },
					new HorizontalLineCoordinates(0.1d, 0d, 0d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 1d, 2d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.1d, 0.1d)
				),
			}
		);

		final ElementData interpolated = Interpolator.interpolate(toInterpolate, 1);
		
		final ElementData expectedInterpolated = ElementData.createElementData(
			"Interpolated",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d, 0.5d, 0.0d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.1d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 1d, 1.0d, 1.0d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.1d, 0.05d)
				),
				new LineData(
					"Line 3",
					new double[] { 1d, 1.5d, 2d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.1d, 0.1d)
				)
			}
		);

		assertElementDataEquals(expectedInterpolated, interpolated);
	}

	@Test
	public void testInterpolateElementDataWithLevel1_2() {
		final ElementData toInterpolate = ElementData.createElementData(
				"To interpolate",
				new LineData[]{
					new LineData(
						"Line 1",
						new double[] { 1d },
						new HorizontalLineCoordinates(0.1d, 0.1d, 0.1d, 0d)
					),
					new LineData(
						"Line 2",
						new double[] { 1d, 2d },
						new HorizontalLineCoordinates(0.1d, 0d, 0.1d, 0.1d)
					),
				}
			);

		final ElementData interpolated = Interpolator.interpolate(toInterpolate, 1);

		final ElementData expectedInterpolated = ElementData.createElementData(
			"Interpolated",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 0.0d, 0.5d, 1d },
					new HorizontalLineCoordinates(0.05d, 0.0d, 0.1d, 0.0d)
				),
				new LineData(
					"Line 2",
					new double[] { 0.5d, 1.0d, 1.5d },
					new HorizontalLineCoordinates(0.05d, 0.0d, 0.1d, 0.05d)
				),
				new LineData(
					"Line 3",
					new double[] { 1d, 1.5d, 2d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.1d, 0.1d)
				)
			}
		);

		assertElementDataEquals(expectedInterpolated, interpolated);
	}

	@Test
	public void testInterpolateElementDataWithLevel1_3() {
		final ElementData toInterpolate = ElementData.createElementData(
			"To interpolate",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d },
					new HorizontalLineCoordinates(0.1d, 0d, 0d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 1d, 2d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.1d, 0.1d)
				),
				new LineData(
					"Line 3",
					new double[] { 1d, 2d, 3d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.2d)
				),
			}
		);

		final ElementData interpolated = Interpolator.interpolate(toInterpolate, 1);

		final ElementData expectedInterpolated = ElementData.createElementData(
			"Interpolated",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1.0d, 0.5d, 0.0d, 0.0d, 0.0d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 1.0d, 1.0d, 1.0d, 0.5d, 0.0d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.05d)
				),
				new LineData(
					"Line 3",
					new double[] { 1.0d, 1.5d, 2.0d, 1.0d, 0.0d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.1d)
				),
				new LineData(
					"Line 4",
					new double[] { 1.0d, 1.5d, 2.0d, 1.75d, 1.5d},
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.15d)
				),
				new LineData(
					"Line 5",
					new double[] { 1d, 1.5d, 2d, 2.5d, 3d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.2d)
				)
			}
		);

		assertElementDataEquals(expectedInterpolated, interpolated);
	}

	@Test
	public void testInterpolateElementDataWithLevel1_4() {
		final ElementData toInterpolate = ElementData.createElementData(
			"To interpolate",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d, 2d, 3d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 4d, 5d, 6d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				),
				new LineData(
					"Line 3",
					new double[] { 7d, 8d, 9d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.2d)
				)
			}
		);

		final ElementData interpolated = Interpolator.interpolate(toInterpolate, 1);

		final ElementData expectedInterpolated = ElementData.createElementData(
			"Interpolated",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d, 1.5d, 2d, 2.5d, 3d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 2.5d, 3d, 3.5d, 4d, 4.5d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.05d)
				),
				new LineData(
					"Line 3",
					new double[] { 4d, 4.5d, 5d, 5.5d, 6d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.1d)
				),
				new LineData(
					"Line 4",
					new double[] { 5.5d, 6d, 6.5d, 7d, 7.5d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.15d)
				),
				new LineData(
					"Line 5",
					new double[] { 7d, 7.5d, 8d, 8.5d, 9d },
					new HorizontalLineCoordinates(0.05d, 0d, 0.2d, 0.2d)
				)
			}
		);

		assertElementDataEquals(expectedInterpolated, interpolated);;
	}

	@Test
	public void interpolateElementDataWithLevel2() {
		final ElementData toInterpolate = ElementData.createElementData(
			"To interpolate",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d, 2d, 3d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 4d, 5d, 6d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.1d)
				),
				new LineData(
					"Line 3",
					new double[] { 7d, 8d, 9d },
					new HorizontalLineCoordinates(0.1d, 0d, 0.2d, 0.2d)
				)
			});

		final ElementData interpolated = Interpolator.interpolate(toInterpolate, 2);

		final ElementData expectedInterpolated = ElementData.createElementData(
			"Interpolated",
			new LineData[]{
				new LineData(
					"Line 1",
					new double[] { 1d, 1.33d, 1.66d, 2d, 2.33d, 2.66d, 3d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0d)
				),
				new LineData(
					"Line 2",
					new double[] { 2d, 2.33d, 2.66d, 3d, 3.33d, 3.66d, 4d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0.1d/3)
				),
				new LineData(
					"Line 3",
					new double[] { 3d, 3.33d, 3.66d, 4d, 4.33d, 4.66d, 5d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0.1d*2/3)
				),
				new LineData(
					"Line 4",
					new double[] { 4d, 4.33d, 4.66d, 5d, 5.33d, 5.66d, 6d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0.1d)
				),
				new LineData(
					"Line 5",
					new double[] { 5d, 5.33d, 5.66d, 6d, 6.33d, 6.66d, 7d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0.1d*4/3)
				)	,
				new LineData(
					"Line 6",
					new double[] { 6d, 6.33d, 6.66d, 7d, 7.33d, 7.66d, 8d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0.1d*5/3)
				),
				new LineData(
					"Line 7",
					new double[] { 7d, 7.33d, 7.66d, 8d, 8.33d, 8.66d, 9d },
					new HorizontalLineCoordinates(0.1d/3d, 0d, 0.2d, 0.2d)
				)
			}
		);

		assertElementDataEquals(expectedInterpolated, interpolated);
	}
}
