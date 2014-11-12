/*
 * Copyright (c) 2014, Marcelova Armada
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
package imageanalysis;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author Jakub Novák
 */
public class ImgTools {

    /**
     * Converts image in {@link BufferedImage} format to {@link Mat} format. Mat
     * format is used by OpenCV to perform detecting operations.
     *
     * @param image is the input image.
     * @return image in {@link Mat}
     */
    public static Mat toMat(BufferedImage image) {
        int depth = CvType.CV_8U;
        int channels = image.getSampleModel().getNumBands();
        int type = CvType.makeType(depth, channels);
        Mat output = new Mat(image.getHeight(), image.getWidth(), type);
        output.put(0, 0, toByteArray(image));
        return output;
    }

    /**
     * Converts image from {@link Mat} to {@link BufferedImage} format.
     *
     * @param source is the input image.
     * @return image in {@link BufferedImage}
     */
    public static BufferedImage toBufferedImage(Mat source) {
        Mat m = source.clone();
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else if (m.channels() == 4) {
            type = BufferedImage.TYPE_4BYTE_ABGR;
            // Puts alpha channel at the beginning because:
            // Mat (BGRA) -> BufferedImage (ABGR)
            List<Mat> channels = new ArrayList<>();
            Core.split(m, channels);
            channels.add(0, channels.get(3));
            channels.remove(4);
            Core.merge(channels, m);
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    /**
     * Converts to gray BufferedImage to byte array.
     *
     * @param image is the input image.
     * @return image in byte array
     */
    private static byte[] toByteArray(BufferedImage image) {
        return ((DataBufferByte) image.getData().getDataBuffer()).getData();
    }

    public static BufferedImage gifToBufferedImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
//            Exceptions.printStackTrace(e);
        }
        return image;
    }

    /**
     * @deprecated Doesn't handle transparency.
     * @param path
     * @return
     */
    public static Mat gifToMat(String path) {
        BufferedImage buff = gifToBufferedImage(path);
        if (buff != null) {
            return toMat(buff);
        }
        return null;
    }

    /**
     * Gets image representing screen capture in form of BGR matrix (Mat).
     *
     * @return Mat representation of screen capture is returned.
     */
    public static Mat getImageFromClipboard() {
        try {
            // Captures screen with Robot class from awt
            BufferedImage source = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            // Captured screen is BufferedImage.TYPE_INT_RGB and needs to be changed before conversion to Mat
            BufferedImage res = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g = res.createGraphics();
            g.drawImage(source, null, 0, 0);
            g.dispose();
            return ImgTools.toMat(res);
        } catch (AWTException ex) {
//            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
