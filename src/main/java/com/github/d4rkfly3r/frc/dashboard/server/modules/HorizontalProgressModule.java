package com.github.d4rkfly3r.frc.dashboard.server.modules;

import com.github.d4rkfly3r.frc.dashboard.api.Module;
import com.github.d4rkfly3r.frc.dashboard.server.Styles;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joshua on 3/14/2016.
 * Project: FRC-Dashboard-Server
 */
@Module(name = "Progress Module")
public class HorizontalProgressModule extends JProgressBar {

    private static final String DISABLED_PERCENT_STRING = " --- ";

    {
        System.out.println(Styles.getOrDef(".lols|color2", 0x1869A6));
    }

    private Color progressBarColor = new Color(Integer.decode(Styles.getOrDef(".lols|color", 0x1869A6)));
    private Color gradientEndingColor = new Color(0xc0c0c0);
    private Color borderColor = new Color(0x736a60);
    private Color disabledBorderColor = new Color(0xbebebe);
    private Color progressTextColor = new Color(0x000000);
    private Font progressTextFont = new Font("Arial", Font.PLAIN, 20);

    private boolean percentStringVisible = true;
    private boolean progressCenterText = true;

    private static final Composite opaque = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    private static final Composite medTransparen = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
    private static final Composite transparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f);
    private static final Composite veryTransparent = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);

    private static GradientPaint gradient;

    private int oldWidth;
    private int oldHeight;

    private int displayWidth;
    private int displayHeight;

    private int insets[] = new int[4];
    private static final int TOP_INSET = 0;
    private static final int LEFT_INSET = 1;
    private static final int BOTTOM_INSET = 2;
    private static final int RIGHT_INSET = 3;

    private static final int PREFERRED_PERCENT_STRING_MARGIN_WIDTH = 3;

    private String maxPercentString;

    public HorizontalProgressModule() {
        setDisplaySize(300, 50);
        setSize(300, 50);
        setMinimum(0);
        setMaximum(100);
        setValue(40);
    }

    public void updateGraphics() {
        update(getGraphics());
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = displayWidth != 0 ? displayWidth - 1 : getWidth() - 1;
        int h = displayHeight != 0 ? displayHeight - 1 : getHeight() - 1;

        int x = insets[LEFT_INSET];
        int y = insets[TOP_INSET];
        w -= (insets[RIGHT_INSET] << 1);
        h -= (insets[BOTTOM_INSET] << 1);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Clean background
        if (isOpaque()) {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        g2d.translate(x, y);


        // Control Border
        g2d.setColor(isEnabled() ? borderColor : disabledBorderColor);
        g2d.drawLine(1, 0, w - 1, 0);
        g2d.drawLine(1, h, w - 1, h);
        g2d.drawLine(0, 1, 0, h - 1);
        g2d.drawLine(w, 1, w, h - 1);

        // Fill in the progress
        int min = getMinimum();
        int max = getMaximum();
        int total = max - min;
        float dx = (float) (w - 2) / (float) total;
        int value = getValue();
        int progress = 0;
        if (value == max) {
            progress = w - 1;
        } else {
            progress = (int) (dx * getValue());
        }

        // A gradient over the progress fill
        g2d.setColor(gradientEndingColor);
        g2d.fillRect(1, 1, w - 1, h - 1);

        g2d.setColor(progressBarColor);
        g2d.fillRect(1, 1, progress, h - 1);

        if (percentStringVisible) {
            FontMetrics fm = g.getFontMetrics();
            int stringW;
            int stringH;

            if (isEnabled()) {
                int p = getValue();
                String percent = Integer.toString(p, 10) + "%";

                if (maxPercentString == null) {
                    maxPercentString = Integer.toString(getMaximum(), 10) + "%";
                }
                stringW = fm.stringWidth(maxPercentString);
                stringH = ((h - fm.getHeight()) / 2) + fm.getAscent();

                g2d.setColor(progressTextColor);
                g2d.setFont(progressTextFont);
                if (progressCenterText) {
                    g2d.drawString(percent, (w / 2) - (stringW / 2), stringH);
                } else {
                    g2d.drawString(percent, w - stringW, stringH);
                }
            } else {
                stringW = fm.stringWidth(DISABLED_PERCENT_STRING);
                stringH = ((h - fm.getHeight()) / 2) + fm.getAscent();

                g2d.setColor(progressTextColor);
                g2d.setFont(progressTextFont);
                if (progressCenterText) {
                    g2d.drawString(DISABLED_PERCENT_STRING, (w / 2) - (stringW / 2), stringH);
                } else {
                    g2d.drawString(DISABLED_PERCENT_STRING, w - stringW, stringH);
                }
            }
            w -= (stringW + PREFERRED_PERCENT_STRING_MARGIN_WIDTH);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
    }

    @Override
    public void validate() {
        int w = getWidth();
        int h = getHeight();

        super.validate();
        if (oldWidth != w || oldHeight != h) {
            oldWidth = w;
            oldHeight = h;
            gradient = null;
        }
    }

    public HorizontalProgressModule setInsetValues(int top, int left, int bottom, int right) {
        insets[TOP_INSET] = top;
        insets[LEFT_INSET] = left;
        insets[BOTTOM_INSET] = bottom;
        insets[RIGHT_INSET] = right;
        return this;
    }

    public int[] getInsetValues() {
        return insets;
    }

    public HorizontalProgressModule setPercentStringVisible(boolean percentStringVisible) {
        this.percentStringVisible = percentStringVisible;
        return this;
    }

    public boolean isPercentStringVisible() {
        return percentStringVisible;
    }

    public HorizontalProgressModule setMaximumValue(int n) {
        super.setMaximum(n);
        maxPercentString = Integer.toString(n, 10) + "%";
        return this;
    }

    public int getMaximum() {
        return super.getMaximum();
    }

    public HorizontalProgressModule setDisplaySize(int width, int height) {
        displayWidth = width;
        displayHeight = height;
        return this;
    }

    public Color getProgressBarColor() {
        return progressBarColor;
    }

    public HorizontalProgressModule setProgressBarColor(Color progressBarColor) {
        this.progressBarColor = progressBarColor;
        return this;
    }

    public Color getGradientEndingColor() {
        return gradientEndingColor;
    }

    public HorizontalProgressModule setGradientEndingColor(Color gradientEndingColor) {
        this.gradientEndingColor = gradientEndingColor;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public HorizontalProgressModule setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getDisabledBorderColor() {
        return disabledBorderColor;
    }

    public HorizontalProgressModule setDisabledBorderColor(Color disabledBorderColor) {
        this.disabledBorderColor = disabledBorderColor;
        return this;
    }

    public Color getProgressTextColor() {
        return progressTextColor;
    }

    public HorizontalProgressModule setProgressTextColor(Color progressTextColor) {
        this.progressTextColor = progressTextColor;
        return this;
    }

    public Font getProgressTextFont() {
        return progressTextFont;
    }

    public HorizontalProgressModule setProgressTextFont(Font progressTextFont) {
        this.progressTextFont = progressTextFont;
        return this;
    }

    public boolean isProgressCenterText() {
        return progressCenterText;
    }

    public HorizontalProgressModule setProgressCenterText(boolean progressCenterText) {
        this.progressCenterText = progressCenterText;
        return this;
    }
}