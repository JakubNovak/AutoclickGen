/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageanalysis;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Jakub Novák
 */
public class Patterns {

    private static final String bonefood = System.getProperty("user.dir") + "\\src\\images\\bonefood.jpg";
    private static final String okbutt = System.getProperty("user.dir") + "\\src\\images\\okbutt.jpg";
    private static final String xbutt = System.getProperty("user.dir") + "\\src\\images\\x.png";

    private Patterns() {
    }

    public static Mat getBonefoodPattern() {
//        BufferedImage im = null;
//        try {
//            im = ImageIO.read(new File(bonefood));
//        } catch (IOException ex) {
//            Logger.getLogger(Patterns.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return ImgTools.toMat(im, true);
//        return Imgcodecs.imread(bonefood, Imgcodecs.IMREAD_ANYCOLOR);
        return Imgcodecs.imread(bonefood, Imgcodecs.IMREAD_GRAYSCALE);
    }

    public static Mat getOkbuttPattern() {
        return Imgcodecs.imread(okbutt);
    }

    public static Mat getXbuttPattern() {
        return Imgcodecs.imread(xbutt, Imgcodecs.IMREAD_GRAYSCALE);
    }
}
