/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoclickgen;

import imageanalysis.Analyzer;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.opencv.core.Core;
import org.opencv.core.Point;

/**
 *
 * @author Jakub Novák
 */
public class AutoClickgen extends JFrame implements KeyListener {

    private boolean activated = false;
    private Thread t;
    private Thread mousePosThread;
    private Timer timer;
    private long remainingTime;
    private java.awt.Point mousePos;
    private Random random;
    private Analyzer analyzer;
    private SpinnerNumberModel xcoordModel;
    private SpinnerNumberModel ycoordModel;
    private SpinnerNumberModel altTabsModel;
    private SpinnerNumberModel waitforModel;
    private PropertyChangeSupport propListener;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
//    static {
//        try {
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // used for tests. This library in classpath only
//        } catch (UnsatisfiedLinkError e) {
//            try {
//                NativeUtils.loadLibraryFromJar(System.getProperty("user.dir") + "/dist/lib/" + Core.NATIVE_LIBRARY_NAME + ".dll"); // during runtime. .DLL within .JAR
//            } catch (IOException e1) {
//                throw new RuntimeException(e1);
//            }
//        }
//    }

    public static void main(String args[]) {
        new AutoClickgen().start();
    }

    public void initialize() {
        propListener = new PropertyChangeSupport(this);
        xcoordModel = new SpinnerNumberModel(468, 0, 1365, 1);
        ycoordModel = new SpinnerNumberModel(578, 0, 767, 1);
        altTabsModel = new SpinnerNumberModel(1, 0, 10, 1);
        waitforModel = new SpinnerNumberModel(300000, 0, 600000, 1000);
        waitforModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                propListener.firePropertyChange("change", -1, getWaitFor());
            }
        });
        random = new Random();
        analyzer = new Analyzer();
    }

    private void start() {
        initialize();

        try {
            JFrame f = new JFrame("Auto Clickgen");
            f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    setActivated(false);
                    dispose();
                    System.exit(0);
                }
            });

            f.setVisible(true);
//            f.setLocation(100, 50);
            f.setLocation(1000, 5);

            f.add(new AutoClickgenPanel(this));
            f.pack();

            f.addKeyListener(this);

            // New thread to look for mouse position
            checkMousePosition();
        } catch (HeadlessException e) {
        }
    }

//    private synchronized boolean checkImageArea(Robot r) {
//
//    }
    public void findDifferences(Robot r) {
//        analyzer.performClicksOnTargets(r);
        analyzer.showDifferences();
    }

    public synchronized void closePopups(Robot r) throws InterruptedException {
        // Analyze
        Point p = analyzer.checkForX();

        if (p != null) {
            r.mouseMove((int) p.x, (int) p.y);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);

            // Move mouse somewhere else
            r.mouseMove(200, 300);
            wait(5000);

            closePopups(r);
        } else {
            System.out.println("Neni co zavrit");
        }
    }

    private synchronized void evaluate(Robot r) throws InterruptedException {
        r.mouseMove(getXcoord(), getYcoord());

        boolean ok = analyzer.compareRoiAgainstPattern(getXcoord(), getYcoord(), 120, 160);

        if (ok) {
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);

            // 5 min
            runTimeout();
            wait(getWaitFor());
            wait(2000 + ((random.nextInt(8) + 2) * 1000));
        } else {
            // Reload page
            r.mouseMove(150, 40);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);

            // Wait 20 s
            wait(20000);

            // Moves the whole page up
            r.keyPress(KeyEvent.VK_HOME);
            r.keyRelease(KeyEvent.VK_HOME);
            wait(2000);

            // Hit PLAY button
            r.mouseMove(370, 160);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
            wait(10000);

            // Moves the whole page down
            r.keyPress(KeyEvent.VK_DOWN);
            r.keyRelease(KeyEvent.VK_DOWN);

            // Close every other pop-up window
            closePopups(r);

            // Hit DOG button
            r.mouseMove(380, 290);
            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);
            wait(3000);

            // Checks again
            evaluate(r);
        }
    }

    private synchronized void runTimeout() {
        timer = new Timer();
        setRemainingTime(getWaitFor());

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setRemainingTime(getRemainingTime() - 1);
                if (getRemainingTime() < 0) {
                    restartTimeout();
                }
            }
        }, 0, 1);
    }

    private synchronized void restartTimeout() {
        timer.cancel();
        propListener.firePropertyChange("change", -1, getWaitFor());
    }

    public Thread getT() {
        return t;
    }

    public void activateT(final Robot r) {

        if (getT() == null) {
            t = new Thread() {
                @Override
                public void run() {
                    while (getActivated()) {
                        try {
                            evaluate(r);
                        } catch (InterruptedException ex) {
                            System.out.println("Error");
                        }
                    }
                }
            };
            t.start();
        }
    }

    private void checkMousePosition() {
        mousePosThread = new Thread() {
            @Override
            public void run() {
                while (!getActivated()) {
                    PointerInfo info = MouseInfo.getPointerInfo();
                    mousePos = info.getLocation();
                    propListener.firePropertyChange("actualCoords", null, mousePos);
                }
            }
        };
        mousePosThread.start();
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        if (!activated) {
            if (getT() != null) {
                try {
                    getT().join(2000);
                    t = null;
                } catch (InterruptedException ex) {
                    System.out.println("Error");
                }
            }
            if (timer != null) {
                restartTimeout();
            }

            // Starts thread checking mouse position
            checkMousePosition();
        } else {
            if (mousePosThread != null) {
                try {
                    mousePosThread.join(2000);
                    mousePosThread = null;
                } catch (InterruptedException ex) {
                    System.out.println("Error");
                }
            }
        }
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        long old = getRemainingTime();
        this.remainingTime = remainingTime;
        propListener.firePropertyChange("time", old, remainingTime);
    }

    public java.awt.Point getMousePos() {
        return mousePos;
    }

    // Get models
    public SpinnerNumberModel getXcoordModel() {
        return xcoordModel;
    }

    public SpinnerNumberModel getYcoordModel() {
        return ycoordModel;
    }

    public SpinnerNumberModel getAltTabsModel() {
        return altTabsModel;
    }

    public SpinnerNumberModel getWaitforModel() {
        return waitforModel;
    }

    // Get values
    public long getWaitFor() {
        return getWaitforModel().getNumber().longValue();
    }

    public int getXcoord() {
        return getXcoordModel().getNumber().intValue();
    }

    public int getYcoord() {
        return getYcoordModel().getNumber().intValue();
    }

    public int getAltTabs() {
        return getAltTabsModel().getNumber().intValue();
    }

    // Property change support
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propListener.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propListener.removePropertyChangeListener(listener);
    }

    // Key listener
    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println(e.getKeyChar());
        if (e.getKeyCode() == KeyEvent.VK_C && !getActivated()) {
            getXcoordModel().setValue(getMousePos().x);
            getYcoordModel().setValue(getMousePos().y);
            propListener.firePropertyChange("mousePosAdded", 0, 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
