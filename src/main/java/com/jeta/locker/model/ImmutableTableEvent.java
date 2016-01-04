package com.jeta.locker.model;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class ImmutableTableEvent extends TableModelEvent {
	public ImmutableTableEvent( TableModel model) {
		super(model);
	}
}
