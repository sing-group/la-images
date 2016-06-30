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
package es.uvigo.ei.sing.icpms.core.gui.dialogs;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A {@code ProgressDialog} facilitates the creation of progress dialogs based
 * on fixed task lists.
 * 
 * @author hlfernandez
 *
 */
public class TaskProgressDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JFrame parent;
	private List<String> tasks;
	private JLabel taskLabel;
	private JProgressBar progressBar;
	private Iterator<String> tasksIterator;
	private int increment;
	private JPanel content;
	
	/**
	 * Constructs a new {@code ProgressDialog} instance.
	 * 
	 * @param parent the parent frame to show the dialog.
	 * @param title the dialog title.
	 * @param tasks the list of tasks.
	 */
	public TaskProgressDialog(JFrame parent, String title, List<String> tasks) {
		super(parent, title);
		this.parent = parent;
		this.tasks = tasks;
		this.tasksIterator = this.tasks.iterator();
		this.increment = 100 / tasks.size();
		this.initComponent();
	}

	private void initComponent() {
		this.content = new JPanel();
		this.content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.content.setLayout(new GridLayout(2, 1));
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		taskLabel = new JLabel(tasksIterator.next());

		this.content.add(taskLabel);
		this.content.add(progressBar);

		this.add(this.content);
		this.setResizable(false);
		centerDialogOnScreen();
		this.setModal(false);
	}

	/**
	 * Sets the progress finished.
	 * 
	 * @param message the message to show in the label.
	 */
	public void finished(String message) {
		this.progressBar.setValue(100);
		this.taskLabel.setText(message);
		
	}

	/**
	 * Sets next task in the label and increments the progress.
	 */
	public void nextTask() {
		if(tasksIterator.hasNext()) {
			this.progressBar.setValue(this.progressBar.getValue()+this.increment);
			this.taskLabel.setText(tasksIterator.next());
		} else {
			this.progressBar.setValue(100);
		}
		
	}

	protected void centerDialogOnScreen() {
		this.pack();
		this.setLocationRelativeTo(parent);
	}
}
