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
package es.uvigo.ei.sing.icpms.core.jzy3d;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;

/**
 * A class to convert between {@code jzy3d} angles and user angles and
 * 	other
 * 
 * @author hlfernandez
 *
 */
public class CoordinatesUtils {
	private static final Coord3d INCREMENT_2D = toCoord3d(new Coord2d(
		toRadians(90), toRadians(0)));
	private static final Coord3d INCREMENT_3D = toCoord3d(new Coord2d(
		toRadians(180), toRadians(0)));
	
	/**
	 * Returns the user angle corresponding to {@code angle}, taking into 
	 * account if it is 2D or 3D mode.
	 * 
	 * @param angle a {@code Coord2d} angle to convert.
	 * @param is3D {@code true} it 3D mode is enabled and {@code false} 
	 * 	otherwise.
	 * @return the display {@code Coord3d} angle.
	 */
	public static final Coord3d getDisplayAngle(Coord2d angle, boolean is3D) {
		return getDisplayAngle(toCoord3d(angle), is3D);
	}
	
	/**
	 * Returns the user angle corresponding to {@code angle}, taking into 
	 * account if it is 2D or 3D mode.
	 * 
	 * @param angle a {@code Coord3d} angle to convert.
	 * @param is3D {@code true} it 3D mode is enabled and {@code false} 
	 * 	otherwise.
	 * @return the display {@code Coord3d} angle.
	 */	
	public static final Coord3d getDisplayAngle(Coord3d angle, boolean is3D) {
		Coord3d displayAngle = angle.clone()
			.add(is3D ? INCREMENT_3D : INCREMENT_2D);
		return new Coord3d(computeX(displayAngle), displayAngle.y, displayAngle.z);
	}
	
	private static double computeX(Coord3d toret) {
		double x = toret.x;
		double actualXDegrees = toDegrees(toret.x);
		if (actualXDegrees > 180) {
			double r = (actualXDegrees - 180) % (360);
			x = toRadians(-180 + r);
		} else if (actualXDegrees < -180) {
			double r = (Math.abs(180 + actualXDegrees)) % (360);
			x = toRadians(180 - r);
		}
		return x;
	}

	/**
	 * Returns the real angle corresponding to {@code angle}, taking into 
	 * account if it is 2D or 3D mode.
	 * 
	 * @param angle a {@code Coord2d} angle to convert.
	 * @param is3D {@code true} it 3D mode is enabled and {@code false} 
	 * 	otherwise.
	 * @return the real {@code Coord3d} angle.
	 */	
	public static final Coord3d getRealAngle(Coord2d angle, boolean is3D) {
		return getRealAngle(toCoord3d(angle), is3D);
	}
	
	/**
	 * Returns the real angle corresponding to {@code angle}, taking into 
	 * account if it is 2D or 3D mode.
	 * 
	 * @param angle a {@code Coord3d} angle to convert.
	 * @param is3D {@code true} it 3D mode is enabled and {@code false} 
	 * 	otherwise.
	 * @return the real {@code Coord3d} angle.
	 */		
	public static final Coord3d getRealAngle(Coord3d angle, boolean is3D) {
		if (is3D) {
			return angle.clone().sub(INCREMENT_3D);
		} else {
			return angle.clone().sub(INCREMENT_2D);
		}
	}
	
	/**
	 * Returns {@code true} if {@code Math.abs(x - y) < 0.00001} and 
	 * {@code false} otherwise.
	 * 
	 * @param x a {@code double} number.
	 * @param y a {@code double} number.
	 * @return {@code true} if {@code Math.abs(x - y) < 0.00001} and 
	 * {@code false} otherwise.
	 */
	public static final boolean equalsTo(double x, double y) {
		return Math.abs(x - y) < 0.00001;
	}
	
	/**
	 * Converts a given {@code Coord2d} object into a {@code Coord3d} object 
	 * with Z component equal to 0.
	 * 
	 * @param coord a {@code Coord2d} object.
	 * @return the converted {@code Coord3d} object with Z component equal to 0.
	 */
	public static final Coord3d toCoord3d(Coord2d coord) {
		return new Coord3d(coord, 0f);
	}
}
