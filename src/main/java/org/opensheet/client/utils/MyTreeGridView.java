/*******************************************************************************
 * Copyright (c) 2012 Dmitry Tikhomirov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Dmitry Tikhomirov - initial API and implementation
 ******************************************************************************/
package org.opensheet.client.utils;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnHeader;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridView;

/**
 * @author Vadim_Kolesnikov
 */
public class MyTreeGridView extends TreeGridView {

	private int headsOffset = 0;

	public int getHeadsOffset() {
		return headsOffset;
	}

	public void setHeadsOffset(int headsOffset) {
		this.headsOffset = headsOffset;
	}

	@Override
	protected ColumnHeader newColumnHeader() {
		header = new ColumnHeader(grid, cm) {

			@SuppressWarnings("unchecked")
			@Override
			protected ComponentEvent createColumnEvent(ColumnHeader header, int column, Menu menu) {
				GridEvent<ModelData> event = (GridEvent<ModelData>) MyTreeGridView.this.createComponentEvent(null);
				event.setColIndex(column);
				event.setMenu(menu);
				return event;
			}

			@Override
			protected Menu getContextMenu(int column) {
				return createContextMenu(column);
			}

			@Override
			protected void onColumnSplitterMoved(int colIndex, int width) {
				super.onColumnSplitterMoved(colIndex, width);
				MyTreeGridView.this.onColumnSplitterMoved(colIndex, width);
			}

			@Override
			protected void onHeaderClick(ComponentEvent ce, int column) {
				super.onHeaderClick(ce, column);
				MyTreeGridView.this.onHeaderClick(grid, column);
			}

			@Override
			protected void onKeyDown(ComponentEvent ce, int index) {
				ce.cancelBubble();
				if (grid.getSelectionModel() instanceof CellSelectionModel<?>) {
					CellSelectionModel<?> csm = (CellSelectionModel<?>) grid.getSelectionModel();
					csm.selectCell(0, index);
				} else {
					grid.getSelectionModel().select(0, false);
				}
			}

			@Override
			// was need override this method.
			public void updateTotalWidth(int offset, int width) {
				if (offset != -1) {
					table.getElement().getParentElement().getStyle().setPropertyPx("width", ++offset);
				}
				width += headsOffset; // in some tables headsOffset == cm.getColumnCount();
				table.getElement().getStyle().setProperty("width", (width) + "px");
			}
		};
		header.setSplitterWidth(splitterWidth);
		header.setMinColumnWidth(grid.getMinColumnWidth());

		return header;
	}
}
