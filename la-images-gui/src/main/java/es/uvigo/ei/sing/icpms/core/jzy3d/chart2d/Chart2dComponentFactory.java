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
package es.uvigo.ei.sing.icpms.core.jzy3d.chart2d;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.chart2d.AxeBox2d;
import org.jzy3d.chart2d.Chart2d;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.align.Valign;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.glu.GLU;

/**
 * This custom {@code Chart2dComponentFactory} is used instead of original class
 * from {@code jzy3d} in order to: use the custom implementation of 
 * {@code View2d} and use a custom {@code AxeBox2d} that provides a better
 * axis drawing. 
 * 
 * @author Hugo López-Fernández
 *
 */
public class Chart2dComponentFactory extends AWTChartComponentFactory {
    @Override
    public IChartComponentFactory getFactory() {
        return this;
    }
    
    @Override
    public Chart newChart(IChartComponentFactory factory, Quality quality, String toolkit){
        return new Chart2d(factory, quality, toolkit);
    }
    
    @Override
    public Chart newChart(Quality quality, Toolkit toolkit) {
        return new Chart2d(getFactory(), quality, toolkit.toString());
    }

    @Override
    public Chart newChart(Quality quality, String toolkit) {
        return new Chart2d(getFactory(), quality, toolkit);
    }

    @Override
    public IAxe newAxe(BoundingBox3d box, View view) {
    	AxeBox2d axe = new AxeBox2d(box) { 
    		@Override
			public void drawAxisTicks(GL gl, GLU glu, Camera cam,
				int direction, Color color, Halign hal, Valign val,
				float tickLength, BoundingBox3d ticksTxtBounds,
				double xpos, double ypos, double zpos, float xdir,
				float ydir, float zdir, double[] ticks
			) {
    			double xlab;
    			double ylab;
    			double zlab;
    			String tickLabel = "";

    			for (int t = 0; t < ticks.length; t++) {
    				// Shift the tick vector along the selected axis
    				// and set the tick length
    				if (spaceTransformer == null) {
    					if (isX(direction)) {
    						xpos = ticks[t];
    						xlab = xpos;
    						ylab = (yrange / tickLength) * ydir + ypos;
    						zlab = (zrange / tickLength) * zdir + zpos;
    						tickLabel = layout.getXTickRenderer().format(xpos);
    					} else if (isY(direction)) {
    						ypos = ticks[t];
    						xlab = (xrange / tickLength) * xdir + xpos;
    						ylab = ypos;
    						zlab = (zrange / tickLength) * zdir + zpos;
    						tickLabel = layout.getYTickRenderer().format(ypos);
    					} else { // (axis==AXE_Z)
    						zpos = ticks[t];
    						xlab = (xrange / tickLength) * xdir + xpos;
    						ylab = (yrange / tickLength) * ydir + ypos;
    						zlab = zpos;
    						tickLabel = layout.getZTickRenderer().format(zpos);
    					}
    				} else {
    					// use space transform shift if we have a space transformer
    					if (isX(direction)) {
    						xpos = spaceTransformer.getX().compute((float) ticks[t]);
    						xlab = xpos;
    						ylab = Math.signum(tickLength * ydir) * (yrange / spaceTransformer.getY().compute(Math.abs(tickLength))) * spaceTransformer.getY().compute(Math.abs(ydir)) + ypos;
    						zlab = Math.signum(tickLength * ydir) * (zrange / spaceTransformer.getZ().compute(Math.abs(tickLength))) * spaceTransformer.getZ().compute(Math.abs(zdir)) + zpos;
    						tickLabel = layout.getXTickRenderer().format(xpos);
    					} else if (isY(direction)) {
    						ypos = spaceTransformer.getY().compute((float) ticks[t]);
    						xlab = Math.signum(tickLength * xdir) * (xrange / spaceTransformer.getX().compute(Math.abs(tickLength))) * spaceTransformer.getX().compute(Math.abs(xdir)) + xpos;
    						ylab = ypos;
    						zlab = Math.signum(tickLength * zdir) * (zrange / spaceTransformer.getZ().compute(Math.abs(tickLength))) * spaceTransformer.getZ().compute(Math.abs(zdir)) + zpos;
    						tickLabel = layout.getYTickRenderer().format(ypos);
    					} else { // (axis==AXE_Z)
    						zpos = spaceTransformer.getZ().compute((float) ticks[t]);
    						xlab = Math.signum(tickLength * xdir) * (xrange / spaceTransformer.getX().compute(Math.abs(tickLength))) * spaceTransformer.getX().compute(Math.abs(xdir)) + xpos;
    						ylab = Math.signum(tickLength * ydir) * (yrange / spaceTransformer.getY().compute(Math.abs(tickLength))) * spaceTransformer.getY().compute(Math.abs(ydir)) + ypos;
    						zlab = zpos;
    						tickLabel = layout.getZTickRenderer().format(zpos);
    					}
    				}
    				Coord3d tickPosition = new Coord3d(xlab, ylab, zlab);

    				if (layout.isTickLineDisplayed()) {
    					if (gl.isGL2()) {
    						if(isY(direction)) {
    							drawTickLine(gl, color, xpos, ypos, zpos, xlab/2, ylab, zlab);
    						} else if(isX(direction)) {
    							drawTickLine(gl, color, xpos, ypos, zpos, xlab, ylab/2, zlab);
    						}
    						//    					drawTickLine(gl, color, xpos, ypos, zpos, xlab, ylab, zlab);
    					} else {
    						// FIXME REWRITE ANDROID
    					}
    				}

    				// Select the alignement of the tick label
    				Halign hAlign = layoutHorizontal(direction, cam, hal, tickPosition);
    				Valign vAlign = layoutVertical(direction, val, zdir);

    				// Draw the text label of the current tick
    				drawAxisTickNumericLabel(gl, glu, direction, cam, color, hAlign, vAlign, ticksTxtBounds, tickLabel, tickPosition);
    			}
    		}

    		@Override
    		public Halign layoutHorizontal(int direction, Camera cam, Halign hal, Coord3d tickPosition) {
    			return Halign.CENTER;
    		}
    	};
    	return axe;
    }

    @Override
    public View newView(Scene scene, ICanvas canvas, Quality quality) {
        return new View2d(getFactory(), scene, canvas, quality);
    }
    
    /* */
    
    public static Chart2d chart() {
        return chart(Quality.Intermediate);
    }
    public static Chart2d chart(Quality quality) {
        return (Chart2d)f.newChart(quality, Toolkit.newt);
    }
    public static Chart2d chart(String toolkit) {
        return (Chart2d)f.newChart(Chart.DEFAULT_QUALITY, toolkit);
    }
    public static Chart2d chart(Quality quality, Toolkit toolkit) {
        return (Chart2d)f.newChart(quality, toolkit);
    }
    public static Chart2d chart(Quality quality, String toolkit) {
        return (Chart2d)f.newChart(quality, toolkit);
    }
    
    static Chart2dComponentFactory f = new Chart2dComponentFactory();
}