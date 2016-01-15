package com.jeta.locker.tabs;

import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

class TabDragSourceListener implements DragSourceListener {
    @Override public void dragEnter(DragSourceDragEvent e) {
        e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
    }
    @Override public void dragExit(DragSourceEvent e) {
        e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
        //glassPane.setTargetRect(0, 0, 0, 0);
        //glassPane.setPoint(new Point(-1000, -1000));
        //glassPane.repaint();
    }
    @Override public void dragOver(DragSourceDragEvent e) {
        //Point glassPt = e.getLocation();
        //JComponent glassPane = (JComponent) e.getDragSourceContext();
        //SwingUtilities.convertPointFromScreen(glassPt, glassPane);
        //int targetIdx = getTargetTabIndex(glassPt);
        //if (getTabAreaBounds().contains(glassPt) && targetIdx >= 0 && targetIdx != dragTabIndex && targetIdx != dragTabIndex + 1) {
        //    e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
        //    glassPane.setCursor(DragSource.DefaultMoveDrop);
        //} else {
        //    e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
        //    glassPane.setCursor(DragSource.DefaultMoveNoDrop);
        //}
    }
    @Override public void dragDropEnd(DragSourceDropEvent e) {
        //dragTabIndex = -1;
        //glassPane.setTargetRect(0, 0, 0, 0);
        //glassPane.setVisible(false);
        //glassPane.setImage(null);
    }
    @Override public void dropActionChanged(DragSourceDragEvent e) { /* not needed */ }
}
