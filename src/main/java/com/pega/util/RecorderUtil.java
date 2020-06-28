

package com.pega.util;

import org.monte.media.*;
import org.monte.media.math.*;
import org.monte.screenrecorder.*;

import java.awt.*;
import java.io.*;

public class RecorderUtil extends ScreenRecorder {
    String COPYRIGHT;
    private static final String VERSION = "$Id: RecorderUtil.java 125139 2015-02-22 15:23:22Z SachinVellanki $";
    private String name;
    private static RecorderUtil screenRecorder;

    public RecorderUtil(final GraphicsConfiguration cfg, final Rectangle captureArea, final Format fileFormat, final Format screenFormat, final Format mouseFormat, final Format audioFormat, final File movieFolder, final String name) throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
        this.name = name;
    }

    protected File createMovieFile(final Format fileFormat) throws IOException {
        if (!this.movieFolder.exists()) {
            this.movieFolder.mkdirs();
        } else if (!this.movieFolder.isDirectory()) {
            throw new IOException("\"" + this.movieFolder + "\" is not a directory.");
        }
        return new File(this.movieFolder, this.name + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(final String dirPath, final String fileName) throws IOException, AWTException {
        final File file = new File(dirPath);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = screenSize.width;
        final int height = screenSize.height;
        final Rectangle captureSize = new Rectangle(0, 0, width, height);
        final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        (RecorderUtil.screenRecorder = new RecorderUtil(gc, captureSize, new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.FILE, FormatKeys.MimeTypeKey, "video/avi"), new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, "tscc", VideoFormatKeys.CompressorNameKey, "tscc", VideoFormatKeys.DepthKey, 24, FormatKeys.FrameRateKey, Rational.valueOf(15.0), VideoFormatKeys.QualityKey, 1.0f, FormatKeys.KeyFrameIntervalKey, 900), new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.EncodingKey, "black", FormatKeys.FrameRateKey, Rational.valueOf(30.0)), null, file, fileName)).start();
    }

    public static void stopRecording() throws Exception {
        RecorderUtil.screenRecorder.stop();
    }
}
