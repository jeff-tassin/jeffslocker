package com.jeta.locker.tabs;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class GhostGlassPane extends JPanel {
    private static final AlphaComposite ALPHA = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
    public final DndTabbedPane tabbedPane;
    private final Rectangle lineRect  = new Rectangle();
    private final Color     lineColor = new Color(0, 100, 255);
    private Point location = new Point();
    private transient Optional<BufferedImage> draggingGhostOp;

    protected GhostGlassPane(DndTabbedPane tabbedPane) {
        super();
        this.tabbedPane = tabbedPane;
        setOpaque(false);
        // Bug ID: 6700748 Cursor flickering during D&D when using CellRendererPane with validation
        // http://bugs.java.com/view_bug.do?bug_id=6700748
        //setCursor(null);
    }
    public void setTargetRect(int x, int y, int width, int height) {
        lineRect.setRect(x, y, width, height);
    }
    public void setImage(BufferedImage draggingGhost) {
        this.draggingGhostOp = Optional.ofNullable(draggingGhost);
    }
    public void setPoint(Point location) {
        this.location = location;
    }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(ALPHA);
        if (tabbedPane.isPaintScrollArea && tabbedPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
            g2.setPaint(Color.RED);
            g2.fill(tabbedPane.rBackward);
            g2.fill(tabbedPane.rForward);
        }
        draggingGhostOp.ifPresent(img -> {
            double xx = location.getX() - img.getWidth(this)  / 2d;
            double yy = location.getY() - img.getHeight(this) / 2d;
            g2.drawImage(img, (int) xx, (int) yy, null);
        });
        if (tabbedPane.dragTabIndex >= 0) {
            g2.setPaint(lineColor);
            g2.fill(lineRect);
        }
        g2.dispose();
    }
}
