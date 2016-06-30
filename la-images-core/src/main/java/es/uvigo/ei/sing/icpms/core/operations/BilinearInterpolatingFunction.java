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

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

public class BilinearInterpolatingFunction implements BivariateFunction {
	private final double[] xval;
	private final double[] yval;
	private final double[][] fval;

	public BilinearInterpolatingFunction(double[] xval, double[] yval, double[][] fval)
	throws DimensionMismatchException, NoDataException, NonMonotonicSequenceException {
		if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
			throw new NoDataException();
		}
		if (xval.length != fval.length) {
			throw new DimensionMismatchException(xval.length, fval.length);
		}
		if (yval.length != fval[0].length) {
			throw new DimensionMismatchException(yval.length, fval[0].length);
		}

		MathArrays.checkOrder(xval);
		MathArrays.checkOrder(yval);
		
		this.xval = xval;
		this.yval = yval;
		this.fval = fval;
	}

	@Override
	public double value(double x, double y) {
		final boolean inRow = isInRow(x);
		final boolean inColumn = isInColumn(y);
		
		if (inRow && inColumn) {
			final int i = getXIndex(x);
			final int j = getYIndex(y);
			
			return this.fval[i][j];
		} else if (inRow) {
			final int i = getXIndex(x);
			final int j1 = previousY(y);
			final int j2 = nextY(y);
			final double y1 = this.yval[j1];
			final double y2 = this.yval[j2];
			final double q1 = this.fval[i][j1];
			final double q2 = this.fval[i][j2];
			
			// Horizontal linear interpolation
			return ((y - y1) * (q2 - q1)) / (y2 - y1) + q1;
		} else if (inColumn) {
			final int j = getYIndex(y);
			final int i1 = previousX(x);
			final int i2 = nextX(x);
			final double x1 = this.xval[i1];
			final double x2 = this.xval[i2];
			final double q1 = this.fval[i1][j];
			final double q2 = this.fval[i2][j];

			// Vertical linear interpolation
			return ((x - x1) * (q2 - q1)) / (x2 - x1) + q1;
		} else {
			final int i1 = previousX(x);
			final int i2 = nextX(x);
			final int j1 = previousY(y);
			final int j2 = nextY(y);
			
			final double x1 = this.xval[i1];
			final double x2 = this.xval[i2];
			final double y1 = this.yval[j1];
			final double y2 = this.yval[j2];
			final double q11 = this.fval[i1][j1];
			final double q12 = this.fval[i1][j2];
			final double q21 = this.fval[i2][j1];
			final double q22 = this.fval[i2][j2];
			
			// Bilinear interpolation
			return ((x2 - x) * (y2 - y))/((x2 - x1) * (y2 - y1)) * q11
				 + ((x - x1) * (y2 - y))/((x2 - x1) * (y2 - y1)) * q21
				 + ((x2 - x) * (y - y1))/((x2 - x1) * (y2 - y1)) * q12
				 + ((x - x1) * (y - y1))/((x2 - x1) * (y2 - y1)) * q22;
		}
	}
	
	private boolean isInRow(double x) {
		return isInArray(x, this.xval);
	}
	
	private boolean isInColumn(double y) {
		return isInArray(y, this.yval);
	}

	private int getXIndex(double x) {
		return indexOf(x, this.xval);
	}
	
	private int getYIndex(double y) {
		return indexOf(y, this.yval);
	}
	
	private static int indexOf(double value, double[] values) {
		for (int i = 0; i < values.length; i++) {
			if (value == values[i])
				return i;
		}
		
		throw new IllegalArgumentException("value not found");
	}
	
	private static boolean isInArray(double value, double[] values) {
		for (double v : values) {
			if (v == value)
				return true;
		}
		
		return false;
	}
	
	private int previousX(double x) {
		return previous(x, this.xval);
	}
	
	private int previousY(double y) {
		return previous(y, this.yval);
	}
	
	private int nextX(double x) {
		return next(x, this.xval);
	}
	
	private int nextY(double y) {
		return next(y, this.yval);
	}
	
	private static int previous(double value, double[] values) {
		if (value < values[0] || value > values[values.length - 1])
            throw new OutOfRangeException(value, values[0], values[values.length - 1]);
		
		for (int i = values.length - 1; i >= 0; i--) {
			if (!Double.isNaN(values[i]) && values[i] < value) {
				return i;
			}
		}
		
		return 0;
	}
	
	private static int next(double value, double[] values) {
		if (value < values[0] || value > values[values.length - 1])
			throw new OutOfRangeException(value, values[0], values[values.length - 1]);
		
		for (int i = 0; i < values.length; i++) {
			if (!Double.isNaN(values[i]) && values[i] > value) {
				return i;
			}
		}
		
		return values.length - 1;
	}

}
