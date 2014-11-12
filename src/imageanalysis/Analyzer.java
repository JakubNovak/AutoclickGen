/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageanalysis;

import gui.ZoomPanel;
import gui.ZoomPanelModel;
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Jakub Novák
 */
public class Analyzer {

    private Mat screen;
    private final int x = 213;
    private final int y = 193;
//    private final int x = 97;
//    private final int y = 193;
    private final Rect left = new Rect(x, y, 376, 462);
    private final Rect right = new Rect(x + 383, y, 376, 462);

    private final Point refPoint = new Point(22, 36);
    private final ZoomPanelModel panelModel;

    public Analyzer() {
        panelModel = new ZoomPanelModel();
    }

    public void showDifferences() {
        showImage(findDifferences());
    }

    private Mat findDifferences() {
        Mat image = ImgTools.getImageFromClipboard();

        // Gets images (both halves)
        Mat leftHalf = image.submat(left);
        Mat rightHalf = image.submat(right);

        // Computes their difference
        Mat diff1 = new Mat();
        Mat diff2 = new Mat();
        Core.subtract(leftHalf, rightHalf, diff1);
        Core.subtract(rightHalf, leftHalf, diff2);

        // Gets sum of both differences (image that highlightes different objects)
        Mat sum = new Mat(diff1.size(), CvType.CV_32F);
        Core.add(diff1, diff2, sum);
        // Normalize
        Core.normalize(sum, sum, 0, 255, Core.NORM_MINMAX);
        sum.convertTo(sum, CvType.CV_8U);

        return sum;

    }

    // Width = 120
    // Height = 160
    public boolean compareRoiAgainstPattern(int xcoord, int ycoord, int width, int height) {
        screen = ImgTools.getImageFromClipboard();

        // Crops roi around chosen mouse point
        Rect roi = new Rect(xcoord - width / 2, ycoord - height / 2, width, height);
        Mat actionButton = screen.submat(roi);

        // Preprocessing
        Imgproc.cvtColor(actionButton, actionButton, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.medianBlur(actionButton, actionButton, 5);

        // Referent pattern
        Mat bonefoodPattern = Patterns.getBonefoodPattern();
//        Imgproc.medianBlur(bonefoodPattern, bonefoodPattern, 5);

        // Match template
        // ... result should be the refPoint
        Mat result = new Mat();
        Imgproc.matchTemplate(actionButton, bonefoodPattern, result, Imgproc.TM_SQDIFF);
        Point p = Core.minMaxLoc(result).minLoc;
//        System.out.println(p.toString());

        return p.equals(refPoint);
    }

    public Point checkForX() {
        screen = ImgTools.getImageFromClipboard();

        Mat image = screen.clone();

        // Preprocessing
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        // Referent pattern
        Mat xPattern = Patterns.getXbuttPattern();

        // Match template
        // ... result should be the refPoint
        Mat result = new Mat();
        Imgproc.matchTemplate(image, xPattern, result, Imgproc.TM_SQDIFF);

        Core.MinMaxLocResult mm = Core.minMaxLoc(result);

        Point p = mm.minLoc;
        double val = mm.minVal;

        if (val < 1000000) {
            p.x += 10;
            p.y += 10;
            return p;
        } else {
            return null;
        }
    }

    private void showImage(Mat image) {
        JFrame frame = new JFrame("Just image");
        ZoomPanel panel = new ZoomPanel(panelModel, 376, 462);
        panel.setZoomable(true);

        frame.add(panel);
        frame.pack();

        frame.setLocation(967, 190);
        frame.setVisible(true);

        panelModel.setImage(ImgTools.toBufferedImage(image));
    }
}
