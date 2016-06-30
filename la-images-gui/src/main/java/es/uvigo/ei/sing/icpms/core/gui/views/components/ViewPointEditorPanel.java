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
package es.uvigo.ei.sing.icpms.core.gui.views.components;

import static es.uvigo.ei.sing.icpms.core.gui.util.DecimalFormatter.format;
import static es.uvigo.ei.sing.icpms.core.gui.views.ElementDataViewConfiguration.getDefault2DAngle;
import static es.uvigo.ei.sing.icpms.core.jzy3d.CoordinatesUtils.getDisplayAngle;
import static es.uvigo.ei.sing.icpms.core.jzy3d.CoordinatesUtils.getRealAngle;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.awt.FlowLayout;
import java.awt.event.FocusListener;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jzy3d.maths.Coord2d;

import es.uvigo.ei.sing.icpms.core.gui.util.FloatTextField;

/**
 * A panel that allows user to introduce a camera view point as a 2D coordinate 
 * in degrees and retrieve it as a {@code Coord2d} object. Note that if
 * 3D is not enabled, only X coordinate can be edited. 
 * 
 * @author hlfernandez
 *
 */
public class ViewPointEditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel xLbl = new JLabel("X: ");
	private JFormattedTextField xTextField;
	private JLabel yLbl = new JLabel("Y: ");
	private JFormattedTextField yTextField;
	private Coord2d displayViewPoint;
	private boolean is3DEnabled;
	private boolean editable = false;

	/**
	 * Constructs a new {@code ViewPointEditorPanel}.
	 * 
	 * @param viewPoint the initial coordinate.
	 * @param is3DEnabled {@code true} if 3D is enabled and {@code false} 
	 * 	otherwise.
	 */
	public ViewPointEditorPanel(Coord2d viewPoint, boolean is3DEnabled) {
		this.is3DEnabled = is3DEnabled;
		this.displayViewPoint = getDisplayAngle(viewPoint, is3DEnabled).getXY();
		init();
	}

	private void init() {
		this.setLayout(new FlowLayout());
		this.add(xLbl);
		this.add(getXTextField());
		this.add(yLbl);
		this.add(getYTextField());
	}

	private JFormattedTextField getXTextField() {
		if(xTextField == null) {
			xTextField = new FloatTextField(0f);
			xTextField.setText(getValue(displayViewPoint.x));
			xTextField.setColumns(10);
		}
		return xTextField;
	}

	private String getValue(float value) {
		return format(toDegrees(value), 2);
	}
	
	private JFormattedTextField getYTextField() {
		if(yTextField == null) {
			yTextField = new FloatTextField(0f);
			yTextField.setText(getValue(displayViewPoint.y));
			yTextField.setEnabled(is3DEnabled);
			yTextField.setColumns(10);
		}
		return yTextField;
	}

	private void updateFields() {
		getXTextField().setText(getValue(displayViewPoint.x));
		getYTextField().setText(getValue(displayViewPoint.y));
	}
	
	@Override
	public synchronized void addFocusListener(FocusListener l) {
		this.xTextField.addFocusListener(l);
		this.yTextField.addFocusListener(l);
	}

	/**
	 * Sets whether 3D mode is enabled.
	 * 
	 * @param is3dEnabled {@code true} if 3D is enabled and {@code false} 
	 * 	otherwise.
	 */
	public void set3DEnabled(boolean is3dEnabled) {
		this.is3DEnabled = is3dEnabled;
		updateFieldsEditability();
	}

	private void updateFieldsEditability() {
		this.xTextField.setEnabled(editable);
		this.yTextField.setEnabled(is3DEnabled && editable);
	}

	/**
	 * Sets the view point.
	 * 
	 * @param viewPoint a {@code Coord2d} view point.
	 */
	public void setViewPoint(Coord2d viewPoint) {
		this.displayViewPoint = getDisplayAngle(viewPoint, is3DEnabled).getXY();
		this.updateFields();
	}

	/**
	 * Returns the introduced {@code Coord2d} view point.
	 * 
	 * @return the introduced {@code Coord2d} view point.
	 */
	public Coord2d getViewPoint() {
		return getRealAngle(
					new Coord2d(getXCoordRadians(), getYCoordRadians()),
					is3DEnabled
				).getXY();
	}

	private double getXCoordRadians() {
		return toRadians((float) getXTextField().getValue());
	}
	
	private double getYCoordRadians() {
		return is3DEnabled ? 
			toRadians((float) getYTextField().getValue()) : 
			getDisplayAngle(getDefault2DAngle(), false).y;
	}

	/**
	 * Sets whether the input components must be editable.
	 * 
	 * @param editable {@code true} if the input components must be editable
	 * 	and false otherwise.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateFieldsEditability();
	}
}
