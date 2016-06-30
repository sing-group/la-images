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
package es.uvigo.ei.sing.icpms.core.gui.licensing;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import es.uvigo.ei.sing.icpms.core.gui.views.components.RotationButtonsPanel;

/**
 * A panel that shows icon attributions information.
 * 
 * @author hlfernandez
 *
 */
public class IconAttributionsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Font JLABEL 	= UIManager.getFont("Label.font");
	private static final Font PLAIN 	= JLABEL.deriveFont(Font.PLAIN);
	private static final Font BOLD 		= JLABEL.deriveFont(Font.BOLD);
	private static final Font ITALIC 	= PLAIN.deriveFont(Font.ITALIC);
	
	private final static ImageIcon ICON_ARROW =
		new ImageIcon(IconAttributionsPanel.class.getResource("icons/arrow.png"));
	private final static ImageIcon ICON_VIEW =
		new ImageIcon(IconAttributionsPanel.class.getResource("icons/view.png"));
	
	private AuthorAttribution[] attributions = new AuthorAttribution[] {
		new AuthorAttribution(
			"Aleks",
			"the Noun Project Collection.", 
			new AttributedIcon[] {
				/*
				 * Rotate left, right, down and up arrows in RotationButtonsPanel.
				 */
				new AttributedIcon(
					RotationButtonsPanel.ICON_UP,
					"Rotate"
				),
			}
		),
		new AuthorAttribution(
			"Arthur Shlain",
			"the Noun Project Collection.", 
			new AttributedIcon[] {
				/*
				 * Reset button in RotationButtonsPanel.
				 */
				new AttributedIcon(
					RotationButtonsPanel.ICON_RESET,
					"Target"
				),
			}
		),	
		new AuthorAttribution(
			"Molly Bramlet",
			"the Noun Project Collection.", 
			new AttributedIcon[] {
				/*
				 * Arrow used to create color map range icon in 
				 * ElementDatasetViewer menu.
				 */
				new AttributedIcon(
					ICON_ARROW,
					"Arrow"
				),
			}
		),		
		new AuthorAttribution(
			"Felipe Santana",
			"the Noun Project Collection.", 
			new AttributedIcon[] {
				/*
				 * Arrow used to create color map range icon in 
				 * ElementDatasetViewer menu.
				 */
				new AttributedIcon(
					ICON_VIEW,
					"View"
				),
			}
		)		
	};

	/**
	 * Constructs a new {@code IconAttributionsPanel}. 
	 */
	public IconAttributionsPanel() {
		init();
	}

	private void init() {
		this.add(getAttributionsPanel());
	}
	
	private JPanel getAttributionsPanel() {
		List<AuthorAttribution> attributions = getAttributions();
		
		JPanel attributionsPanel = new JPanel(
			new GridLayout(attributions.size(), 1));
		attributions.forEach(l -> {
			attributionsPanel.add(l.getPanel());
		});
		return attributionsPanel;
	}
	
	protected List<AuthorAttribution> getAttributions() {
		return Arrays.asList(attributions);
	}

	protected static class AuthorAttribution {
		
		private AttributedIcon[] icons;
		private String author;
		private String affiliation;

		public AuthorAttribution(String author, String affiliation, 
			AttributedIcon[] icons
		) {
			this.author = author;
			this.affiliation = affiliation;
			this.icons = icons;
		}
		
		public Component getPanel() {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			for(int i = 0; i < icons.length; i++) {
				panel.add(icons[i].getPanel());
				addSeparator(panel, i);
			}
			panel.add(new FontJLabel("by", PLAIN));
			panel.add(new FontJLabel(author, ITALIC));
			panel.add(new FontJLabel("from" + affiliation, PLAIN));
			return panel;
		}

		private void addSeparator(JPanel panel, int i) {
			if(i == icons.length - 1) {
				panel.add(new FontJLabel(icons.length == 1? "symbol" : "symbols", PLAIN));
			} else if (i == icons.length - 2) {
				panel.add(new FontJLabel("and", PLAIN));
			} else {
				panel.add(new FontJLabel(",", PLAIN));
			}
		}
	}
	
	private static class FontJLabel extends JLabel {
		private static final long serialVersionUID = 1L;

		public FontJLabel(String label, Font font) {
			super(label);
			this.setFont(font);
		}

	}

	protected static class AttributedIcon {
		
		private ImageIcon icon;
		private String name;

		public AttributedIcon(ImageIcon icon, String name) {
			this.icon = icon;
			this.name = name;
		}
		
		public Component getPanel() {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel.add(new FontJLabel(name, BOLD));
			panel.add(new JLabel(icon));
			return panel;
		}
	}
}
