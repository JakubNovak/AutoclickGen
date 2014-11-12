/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclickgen;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Jakub Novák
 */
public class AutoClickgenPanel extends JPanel implements PropertyChangeListener {

    private AutoClickgen model;
    private Robot r;

    /**
     * Creates new form AutoClickgenPanel
     *
     * @param model
     */
    public AutoClickgenPanel(AutoClickgen model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
        initComponents();

        try {
            r = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(AutoClickgenPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void performClick(Robot r) {
        model.setActivated(true);

        r.keyPress(KeyEvent.VK_ALT);
        for (int i = 0; i < model.getAltTabs(); i++) {
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_TAB);
        }
        r.keyRelease(KeyEvent.VK_ALT);

        r.mouseMove(model.getXcoord(), model.getYcoord());

        model.activateT(r);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xcoordSpinner = new javax.swing.JSpinner();
        ycoordSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        altTabsSpinner = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        waitForSpinner = new javax.swing.JSpinner();
        timeoutLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        runToggleButton = new javax.swing.JToggleButton();
        actualCoordsLabel = new javax.swing.JLabel();
        solveDiffButton = new javax.swing.JButton();
        checkPatternButton = new javax.swing.JButton();

        xcoordSpinner.setModel(model.getXcoordModel());

        ycoordSpinner.setModel(model.getYcoordModel());

        jLabel1.setText("X coord:");

        jLabel2.setText("Y coord:");

        jLabel3.setText("Alt+Tabs:");

        altTabsSpinner.setModel(model.getAltTabsModel());

        jLabel4.setText("Wait for [ms]:");

        waitForSpinner.setModel(model.getWaitforModel());

        timeoutLabel.setText("0");

        jLabel5.setText("Countdown:");

        runToggleButton.setText("Run");
        runToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runToggleButtonActionPerformed(evt);
            }
        });

        actualCoordsLabel.setText("[x ; y]");
        actualCoordsLabel.setEnabled(false);
        actualCoordsLabel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                actualCoordsLabelMouseMoved(evt);
            }
        });

        solveDiffButton.setText("Solve difference");
        solveDiffButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solveDiffButtonActionPerformed(evt);
            }
        });

        checkPatternButton.setText("Check pattern");
        checkPatternButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPatternButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(xcoordSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(waitForSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(altTabsSpinner))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(ycoordSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(actualCoordsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(runToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(solveDiffButton)
                        .addGap(18, 18, 18)
                        .addComponent(checkPatternButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(waitForSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(xcoordSpinner)))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ycoordSpinner)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(altTabsSpinner)
                    .addComponent(runToggleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(actualCoordsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(solveDiffButton)
                    .addComponent(checkPatternButton))
                .addContainerGap())
        );

        jLabel1.setToolTipText("<html>Either choose x-coordination manualy,<br> or use mouse to point at a target and press C");
        jLabel2.setToolTipText("<html>Either choose y-coordination manualy,<br> or use mouse to point at a target and press C");
        jLabel3.setToolTipText("How many ALT+TABs to perform on first start");
        timeoutLabel.setText(reformatTimeout(model.getWaitFor()));
    }// </editor-fold>//GEN-END:initComponents

    private void runToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runToggleButtonActionPerformed
        if (runToggleButton.isSelected()) {
            performClick(r);
        } else {
            model.setActivated(false);
        }
    }//GEN-LAST:event_runToggleButtonActionPerformed

    private void actualCoordsLabelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actualCoordsLabelMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_actualCoordsLabelMouseMoved

    private void solveDiffButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solveDiffButtonActionPerformed
        model.findDifferences(r);
    }//GEN-LAST:event_solveDiffButtonActionPerformed

    private void checkPatternButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPatternButtonActionPerformed
        // TODO add your handling code here:
        try {
            model.closePopups(r);
        } catch (InterruptedException ex) {
            Logger.getLogger(AutoClickgenPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_checkPatternButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel actualCoordsLabel;
    private javax.swing.JSpinner altTabsSpinner;
    private javax.swing.JButton checkPatternButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JToggleButton runToggleButton;
    private javax.swing.JButton solveDiffButton;
    private javax.swing.JLabel timeoutLabel;
    private javax.swing.JSpinner waitForSpinner;
    private javax.swing.JSpinner xcoordSpinner;
    private javax.swing.JSpinner ycoordSpinner;
    // End of variables declaration//GEN-END:variables

    private String reformatTimeout(long timeout) {
        long second = (timeout / 1000) % 60;
        long minute = (timeout / (1000 * 60)) % 60;
        long millis = timeout % 1000;

        return String.format("%02d:%02d:%03d", minute, second, millis);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case "time":
                timeoutLabel.setText(reformatTimeout(model.getRemainingTime()));
                timeoutLabel.repaint();
                break;
            case "change":
                timeoutLabel.setText(reformatTimeout(model.getWaitFor()));
                timeoutLabel.repaint();
                break;
            case "actualCoords":
                actualCoordsLabel.setText("[" + model.getMousePos().x + " ; " + model.getMousePos().y + "]");
                actualCoordsLabel.repaint();
                break;
            case "mousePosAdded":
                xcoordSpinner.repaint();
                ycoordSpinner.repaint();
        }
    }
}
