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

import static es.uvigo.ei.sing.icpms.core.jzy3d.CoordinatesUtils.equalsTo;
import static es.uvigo.ei.sing.icpms.core.jzy3d.CoordinatesUtils.getDisplayAngle;
import static java.lang.Math.toRadians;

import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;

import es.uvigo.ei.sing.icpms.core.gui.views.components.event.RotationEvent;
import es.uvigo.ei.sing.icpms.core.gui.views.components.event.RotationListener;

/**
 * A graphical component with four rotation buttons (left, right, up and down)
 * plus a reset button. Click in any button generates a {@code RotationEvent}
 * with the rotation angle and registered {@code RotationListener}s are 
 * notified.
 * 
 * @author hlfernandez
 * @see RotationEvent
 * @see RotationListener
 *
 */
public class RotationButtonsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final double ROTATE_STEP = toRadians(22.5);
	
	private static final Coord2d ROTATION_DOWN_ANGLE = 
		new Coord2d(0, ROTATE_STEP);
	
	private static final Coord2d ROTATION_UP_ANGLE = 
		new Coord2d(0, -ROTATE_STEP);
	
	private static final Coord2d ROTATION_RIGHT_ANGLE = 
		new Coord2d(-ROTATE_STEP, 0);
	
	private static final Coord2d ROTATION_LEFT_ANGLE = 
		new Coord2d(ROTATE_STEP, 0);
	
	private static final Coord2d RESET_ANGLE = 
		new Coord2d(0, 0);
	
	public final static ImageIcon ICON_UP =
		new ImageIcon(RotationButtonsPanel.class.getResource("icons/up.png"));
	public final static ImageIcon ICON_DOWN =
		new ImageIcon(RotationButtonsPanel.class.getResource("icons/down.png"));
	public final static ImageIcon ICON_LEFT =
		new ImageIcon(RotationButtonsPanel.class.getResource("icons/left.png"));
	public final static ImageIcon ICON_RIGHT =
		new ImageIcon(RotationButtonsPanel.class.getResource("icons/right.png"));
	public final static ImageIcon ICON_RESET =
		new ImageIcon(RotationButtonsPanel.class.getResource("icons/reset.png"));

	private RotateButton rotateLeftBtn;
	private RotateButton rotateRightBtn;
	private RotateButton rotateUpBtn;
	private RotateButton rotateDownBtn;
	private RotateButton resetViewBtn;

	private boolean is3dEnabled = false;
	
	/**
	 * Constructs a new {@code RotationButtonsPanel}.
	 */
	public RotationButtonsPanel() {
		init();
	}

	private void init() {
		this.setBorder(BorderFactory.createEmptyBorder(1, 5, 5, 1));

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		rotateLeftBtn = new RotateButton(ICON_LEFT, this::rotateLeft,
			"Rotate left (X axis)");
		rotateRightBtn = new RotateButton(ICON_RIGHT, this::rotateRight, 
			"Rotate right (X axis)");
		rotateUpBtn = new RotateButton(ICON_UP, this::rotateUp,
			"Rotate up (Y axis)");
		rotateDownBtn = new RotateButton(ICON_DOWN, this::rotateDown,
			"Rotate down (Y axis)");
		resetViewBtn = new RotateButton(ICON_RESET, this::reset,
			"Reset view");

		JPanel gap1 = new JPanel();
		JPanel gap2 = new JPanel();
		JPanel gap3 = new JPanel();
		JPanel gap4 = new JPanel();
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup()
						.addComponent(gap1)
						.addComponent(rotateRightBtn)
						.addComponent(gap2)
				)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(rotateDownBtn)
						.addComponent(resetViewBtn)
						.addComponent(rotateUpBtn)
				)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(gap3)
						.addComponent(rotateLeftBtn)
						.addComponent(gap4)
				)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup()
					.addComponent(gap1)
					.addComponent(rotateUpBtn)
					.addComponent(gap3)
				)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(rotateRightBtn)
						.addComponent(resetViewBtn)
						.addComponent(rotateLeftBtn)
					)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(gap2)
						.addComponent(rotateDownBtn)
						.addComponent(gap4)
				)
		);
	}

	private class RotateButton extends JButton {
		private static final long serialVersionUID = 1L;

		public RotateButton(ImageIcon icon, Runnable actionPerformed,
				String tooltip) {
			super(new RotateAction(icon, actionPerformed));
			this.setToolTipText(tooltip);
		}
	}

	private class RotateAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private Runnable actionPerformed;

		public RotateAction(ImageIcon icon, Runnable actionPerformed) {
			super("", icon);
			this.actionPerformed = actionPerformed;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.actionPerformed.run();
		}
	}

	private void rotateLeft() {
		fireRotationEvent(new RotationEvent(ROTATION_LEFT_ANGLE));
	}

	private void rotateRight() {
		fireRotationEvent(new RotationEvent(ROTATION_RIGHT_ANGLE));
	}
	
	private void rotateUp() {
		fireRotationEvent(new RotationEvent(ROTATION_UP_ANGLE));
	}
	
	private void rotateDown() {
		fireRotationEvent(new RotationEvent(ROTATION_DOWN_ANGLE));
	}
	
	private void reset() {
		fireRotationEvent(new RotationEvent(RESET_ANGLE));
	}
	
	public void set3DEnabled(boolean is3dEnabled) {
		this.is3dEnabled = is3dEnabled;
		rotateUpBtn.setEnabled(is3dEnabled);
		rotateDownBtn.setEnabled(is3dEnabled);
	}
	
	private void fireRotationEvent(RotationEvent e) {
		Stream.of(getRotationListeners()).forEach(r -> r.onRotationEvent(e));
	}
	
	public void viewPointChanged(Coord3d viewPoint) {
		viewPoint = getDisplayAngle(viewPoint, is3dEnabled());

		rotateDownBtn.setEnabled(
			is3dEnabled() && !equalsTo(viewPoint.y, toRadians(90)));
		rotateUpBtn.setEnabled(
			is3dEnabled() && !equalsTo(viewPoint.y, toRadians(-90)));
	}
	
	private boolean is3dEnabled() {
		return this.is3dEnabled ;
	}

	/**
	 * Adds the specified rotation listener to receive rotation events from this
	 * component. If listener {@code l} is  {@code null}, no exception is
	 * thrown and no action is performed.
	 * 
	 * @param l the rotation listener.
	 */
	public void addRotationListener(RotationListener l) {
		this.listenerList.add(RotationListener.class, l);
	}
	
	/**
	 * Removes the specified rotation listener so that it no longer receives
	 * rotation events from this component. This method performs no function, 
	 * nor does it throw an exception, if the listener specified by the argument 
	 * was not previously added to this component. If listener {@code l} is 
	 * {@code null}, no exception is thrown and no action is performed.
	 * 
	 * @param l the rotation listener.
	 */
	public void removeRotationListener(RotationListener l) {
		this.listenerList.remove(RotationListener.class, l);
	}
	
	/**
	 * Returns an array of all the rotation listeners registered on this
	 * component.
	 * 
	 * @return all of this component's @{code RotationListener} or an empty
	 * array if no rotation listeners are currently registered.
	 */
	public RotationListener[] getRotationListeners() {
		return this.listenerList.getListeners(RotationListener.class);
	}
}
