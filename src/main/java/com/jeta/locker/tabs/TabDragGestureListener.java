package com.jeta.locker.tabs;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.InvalidDnDOperationException;

class TabDragGestureListener implements DragGestureListener {
    @Override public void dragGestureRecognized(DragGestureEvent e) {
        Component c = e.getComponent();
        if (!(c instanceof DndTabbedPane)) {
            return;
        }
        DndTabbedPane tabbedPane = (DndTabbedPane) c;
        if (tabbedPane.getTabCount() <= 1) {
            return;
        }
        Point tabPt = e.getDragOrigin();
        tabbedPane.dragTabIndex = tabbedPane.indexAtLocation(tabPt.x, tabPt.y);
        //"disabled tab problem".
        if (tabbedPane.dragTabIndex < 0 || !tabbedPane.isEnabledAt(tabbedPane.dragTabIndex)) {
            return;
        }
        tabbedPane.initGlassPane(e.getDragOrigin());
        try {
            e.startDrag(DragSource.DefaultMoveDrop, new TabTransferable(c), new TabDragSourceListener());
        } catch (InvalidDnDOperationException idoe) {
            idoe.printStackTrace();
        }
    }
}
