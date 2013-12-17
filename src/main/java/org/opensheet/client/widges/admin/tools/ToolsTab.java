package org.opensheet.client.widges.admin.tools;


import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

public class ToolsTab extends TabPanel{

	
	public ToolsTab(){
		
		
		TabItem fixRateTab = new TabItem("Fix Rate's Tab");
		fixRateTab.setHideMode(HideMode.OFFSETS);
		fixRateTab.add(new ExternalRatePanel());
		add(fixRateTab);
	}
	
}