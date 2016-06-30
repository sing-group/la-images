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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * This class serves as superclass to other classes implementing Export dialogs
 * and provides some common icons and methods.
 * 
 * @author Hugo López-Fernández
 *
 */
public abstract class InputJDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	protected static final ImageIcon ICON_ACCEPT = new ImageIcon(
		InputJDialog.class.getResource("icons/ok.png"));
	protected static final ImageIcon ICON_CANCEL = new ImageIcon(
		InputJDialog.class.getResource("icons/cancel.png"));
	protected static final ImageIcon ICON_BROWSE = new ImageIcon(
		InputJDialog.class.getResource("icons/browse.png"));
	protected static final ImageIcon ICON_HELP = new ImageIcon(
		InputJDialog.class.getResource("icons/help.png"));
	
	
	protected final static FocusListener FOCUS_LISTENER = new FocusAdapter() {
		public void focusGained(FocusEvent e) {
			if (e.getComponent() instanceof JTextField) {
				SwingUtilities.invokeLater(() -> {
					((JTextField) e.getComponent()).selectAll();
				});
			}
		}
	};
	
	protected JFrame parent;
	private JTextArea descriptionArea;
	private String description;
	protected JButton okButton;
	protected JButton cancelButton;
	protected boolean canceled = true;
	
	/**
	 * Constructs a new instance of {@link InputJDialog}.
	 * 
	 * @param parent the parent frame of this dialog.
	 * @param title the dialog title.
	 * @param description the dialog description.
	 */
	protected InputJDialog(JFrame parent, String title, String description) {
		super(parent);
		this.parent = parent;
		this.description = description;
		this.setModal(true);
		this.setTitle(title);
		this.setLayout(new BorderLayout());
		this.add(getDescriptionPane(), BorderLayout.NORTH);
		this.add(new JScrollPane(getInputComponents()), BorderLayout.CENTER);
		this.add(getButtonsPane(), BorderLayout.SOUTH);
		this.setResizable(true);
		this.setIconsVisible(false);
		centerDialogOnParent();
		checkEnabled();
		addKeyBindings();
	}

	protected JTextArea getDescriptionPane() {
		if (this.descriptionArea == null) {
			this.descriptionArea = new JTextArea(this.description);
			this.descriptionArea.setMargin(new Insets(10, 10, 10, 10));
			this.descriptionArea.setWrapStyleWord(true);
			this.descriptionArea.setLineWrap(true);
			this.descriptionArea.setEditable(false);
			this.descriptionArea.setBackground(Color.WHITE);
			this.descriptionArea.setOpaque(true);
		}
		return this.descriptionArea;
	}

	/**
	 * Returns a {@code JComponent} with the input components.
	 * 
	 * @return a {@code JComponent} with the input components.
	 */
	protected abstract JComponent getInputComponents();

	/**
	 * Checks if Ok button must be enabled. 
	 */
	protected abstract void checkEnabled();
	
	/**
	 * Returns the buttons panel of the dialog.
	 * 
	 * @return the buttons panel of the dialog.
	 */
	protected Component getButtonsPane() {
		final JPanel buttonsPanel = new JPanel(new FlowLayout());
		
		okButton = new JButton("OK", ICON_ACCEPT);
		okButton.setEnabled(false);
		okButton.setToolTipText("Accept");
		okButton.addActionListener(a -> this.onOkButtonClicked());
		
		cancelButton = new JButton("Cancel", ICON_CANCEL); 
		cancelButton.setToolTipText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				InputJDialog.this.dispose();
			}
		});

		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		
		getRootPane().setDefaultButton(okButton);
		InputMap im = okButton.getInputMap();
		im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
		im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
		
		return buttonsPanel;
	}
	
	protected void onOkButtonClicked() {
		canceled = false;
		this.dispose();
	}

	/**
	 * Establishes the dialog key bindings.
	 */
	protected void addKeyBindings() {
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke("ESCAPE"), "closeTheDialog");
		getRootPane().getActionMap().put("closeTheDialog",
			new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					canceled = true;
					InputJDialog.this.dispose();
				}
			});
	}
	
	/**
	 * Centers the dialog in the parent component.
	 */
	protected void centerDialogOnParent() {
		this.pack();
		this.setLocationRelativeTo(parent);
	}
	
	@Override
	public void setVisible(boolean b) {
		pack();
		super.setVisible(b);
	}
	
	public void setIconsVisible(boolean enabled) {
		okButton.setIcon(enabled?ICON_ACCEPT:null);
		cancelButton.setIcon(enabled?ICON_CANCEL:null);
	}
	
	protected void addFocusListener(JTextField tF) {
		tF.addFocusListener(FOCUS_LISTENER);
	}
}
