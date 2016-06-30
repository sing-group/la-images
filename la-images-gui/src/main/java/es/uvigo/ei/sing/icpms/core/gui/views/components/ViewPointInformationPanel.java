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
import static es.uvigo.ei.sing.icpms.core.jzy3d.CoordinatesUtils.getDisplayAngle;
import static java.lang.Math.toDegrees;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jzy3d.maths.Coord3d;

/**
 * A panel that shows a given {@code Coord3d} in degrees. Implements 
 * {@code IViewPointChangedListener} so that it can listen for 
 * {@code ViewPointChangedEvent} events and update the panel information. 
 * 
 * @author hlfernandez
 *
 */
public class ViewPointInformationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Coord3d currentViewPoint = new Coord3d(0, 0, 0);
	private JLabel info = new JLabel();
	private boolean is3DEnabled = false;

	/**
	 * Constructs a new {@code ViewPointInformationPanel}.
	 */
	public ViewPointInformationPanel() {
		init();
	}

	private void init() {
		this.setLayout(new FlowLayout());
		this.add(Box.createHorizontalGlue());
		this.add(getInfoLabel());
		this.add(Box.createHorizontalGlue());
	}

	private JLabel getInfoLabel() {
		updateLabel(this.currentViewPoint);
		return this.info;
	}
	
	public boolean is3DEnabled() {
		return is3DEnabled;
	}

	public void viewPointChanged(Coord3d viewPoint) {
		updateCurrentViewPoint(viewPoint);
	}

	private void updateCurrentViewPoint(Coord3d viewPoint) {
		this.currentViewPoint = getDisplayAngle(viewPoint, is3DEnabled());
		updateLabel(this.currentViewPoint);
	}

	private void updateLabel(Coord3d viewPoint) {
		this.info.setToolTipText(
			is3DEnabled() ? "Format: (X, Y)" : "Format: (X)");
		this.info.setText(generateLabel(viewPoint));
	}

	private String generateLabel(Coord3d viewPoint) {
		StringBuilder sb = new StringBuilder();
		sb
			.append("(")
			.append(format(toDegrees(viewPoint.x), 2))
			.append("°");
		if(is3DEnabled()){
			sb
				.append(", ")
				.append(format(toDegrees(viewPoint.y), 2))
				.append("°");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public void set3DEnabled(boolean is3DEnabled) {
		this.is3DEnabled = is3DEnabled;
		updateLabel(currentViewPoint);
	}
}
