package com.jeta.locker.tabs;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTabbedPane;

class TabDropTargetListener implements DropTargetListener {
    private Point prevGlassPt = new Point();
    @Override public void dragEnter(DropTargetDragEvent e) {
        Component c = e.getDropTargetContext().getComponent();
        if (!(c instanceof GhostGlassPane)) {
            return;
        }
        GhostGlassPane glassPane = (GhostGlassPane) c;
        DndTabbedPane tabbedPane = glassPane.tabbedPane;
        Transferable t = e.getTransferable();
        DataFlavor[] f = e.getCurrentDataFlavors();
        if (t.isDataFlavorSupported(f[0]) && tabbedPane.dragTabIndex >= 0) {
            e.acceptDrag(e.getDropAction());
        } else {
            e.rejectDrag();
        }
    }
    @Override public void dragExit(DropTargetEvent e) {
        Component c = e.getDropTargetContext().getComponent();
        System.out.println("DropTargetListener#dragExit: " + c.getName());
    }
    @Override public void dropActionChanged(DropTargetDragEvent e) { /* not needed */ }
    @Override public void dragOver(DropTargetDragEvent e) {
        Component c = e.getDropTargetContext().getComponent();
        if (!(c instanceof GhostGlassPane)) {
            return;
        }
        GhostGlassPane glassPane = (GhostGlassPane) c;
        DndTabbedPane tabbedPane = glassPane.tabbedPane;
        Point glassPt = e.getLocation();
        if (tabbedPane.getTabPlacement() == JTabbedPane.TOP || tabbedPane.getTabPlacement() == JTabbedPane.BOTTOM) {
            tabbedPane.initTargetLeftRightLine(tabbedPane.getTargetTabIndex(glassPt));
        } else {
            tabbedPane.initTargetTopBottomLine(tabbedPane.getTargetTabIndex(glassPt));
        }
        if (tabbedPane.hasGhost) {
            glassPane.setPoint(glassPt);
        }
        if (!prevGlassPt.equals(glassPt)) {
            glassPane.repaint();
        }
        prevGlassPt = glassPt;
        tabbedPane.autoScrollTest(glassPt);
    }
    @Override public void drop(DropTargetDropEvent e) {
        Component c = e.getDropTargetContext().getComponent();
        if (!(c instanceof GhostGlassPane)) {
            return;
        }
        GhostGlassPane glassPane = (GhostGlassPane) c;
        DndTabbedPane tabbedPane = glassPane.tabbedPane;
        Transferable t = e.getTransferable();
        DataFlavor[] f = t.getTransferDataFlavors();
        if (t.isDataFlavorSupported(f[0]) && tabbedPane.dragTabIndex >= 0) {
            tabbedPane.convertTab(tabbedPane.dragTabIndex, tabbedPane.getTargetTabIndex(e.getLocation()));
            e.dropComplete(true);
        } else {
            e.dropComplete(false);
        }
        tabbedPane.dragTabIndex = -1;
        glassPane.setTargetRect(0, 0, 0, 0);
        glassPane.setVisible(false);
        glassPane.setImage(null);
        tabbedPane.repaint();
    }
}
