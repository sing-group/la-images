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
package es.uvigo.ei.sing.icpms.gui.aibench;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import es.uvigo.ei.sing.icpms.core.gui.licensing.IconAttributionsPanel;

public class AiBenchIconAttributionsPanel extends IconAttributionsPanel {
	private static final long serialVersionUID = 1L;
	
	private final static ImageIcon ICON_FILE =
		new ImageIcon(AiBenchIconAttributionsPanel.class.getResource("icons/file.png"));
	
	private static AuthorAttribution[] ATTRIBUTIONS = new AuthorAttribution[] {
			new AuthorAttribution(
				"useiconic.com",
				"the Noun Project Collection.", 
				new AttributedIcon[] {
					/*
					 * Load Data/Analysis in AIBench's toolbar.
					 */
					new AttributedIcon(ICON_FILE,"File"),
				}
			)
		};
	@Override
	protected List<AuthorAttribution> getAttributions() {
		List<AuthorAttribution> attributions = new LinkedList<AuthorAttribution>();
		attributions.addAll(super.getAttributions());
		attributions.addAll(Arrays.asList(ATTRIBUTIONS));
		return attributions;
	}

}
