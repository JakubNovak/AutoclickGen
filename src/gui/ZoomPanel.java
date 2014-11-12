/*
 * Copyright (c) 2013, Marcelova Armada
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 *
 * - Neither the name of the OBBB project nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 *
 * @author Jakub Novák
 */
public class ZoomPanel extends JPanel implements MouseWheelListener, PropertyChangeListener {

    private final ZoomPanelModel model;
    private int w, h;
    private Rectangle bounds;
    private boolean isResizeable = false;
    private double resize = 1.0;
    // Zooming variables
    private boolean isZoomable = false;
    protected double zoom = 1.0;
    private double percentage = 0.1;

    public ZoomPanel(ZoomPanelModel model, int width, int height) {
        this.model = model;
        w = width;
        h = height;
        setPreferredSize(new Dimension(w, h));
        this.model.addPropertyChangeListener(this);
    }

    public ZoomPanel(ZoomPanelModel model, BufferedImage image) {
        this.model = model;
        this.model.setImage(image);
        w = image.getWidth();
        h = image.getHeight();
        setPreferredSize(new Dimension(w, h));
        this.model.addPropertyChangeListener(this);
    }

    public void setZoomable(boolean isZoomable) {
        this.isZoomable = isZoomable;
        if (isZoomable) {
            this.addMouseWheelListener(this);
        } else {
            this.removeMouseWheelListener(this);
        }
    }

    public void setResizeable(boolean isResizeable) {
        this.isResizeable = isResizeable;
    }

    private double getProperResizeConstant() {
        double imh = model.getImage().getHeight();
        double imw = model.getImage().getWidth();
        return Math.min(h / imh, w / imw);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        if (isZoomable) {
            g2D.scale(zoom, zoom);
        }

        if (model.getImage() != null) {
            if (isResizeable) {
                resize = getProperResizeConstant();
                g2D.scale(resize, resize);
            }
            g2D.drawImage(model.getImage(), 0, 0, this);
        }

        if (isResizeable) {
            bounds = getBounds();
            w = (int) bounds.getWidth();
            h = (int) bounds.getHeight();
        }
    }

    // **** Zooming methods ************************************************* //
    /**
     * Sets the zooming precentage.
     *
     * @param zoomPercentage is the percentage of 100 that will be used as a
     * scaling factor for zooming.
     */
    public void setZoomPercentage(int zoomPercentage) {
        percentage = ((double) zoomPercentage) / 100;
    }

    /**
     * Resets zooming to 1.
     */
    public void originalSize() {
        zoom = 1;
    }

    /**
     * Zooms in.
     */
    public void zoomIn() {
        zoom += percentage;
    }

    /**
     * Zooms out.
     */
    public void zoomOut() {
        zoom -= percentage;

        if (zoom < percentage) {
            if (percentage > 1.0) {
                zoom = 1.0;
            } else {
                zoomIn();
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (isZoomable) {
            int i = e.getWheelRotation();
            if (i > 0) {
                zoomOut();
            } else if (i < 0) {
                zoomIn();
            }
            repaint();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("image")) {
            this.revalidate();
            this.repaint();
        }
    }
}
