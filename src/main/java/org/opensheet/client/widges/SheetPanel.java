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
package org.opensheet.client.widges;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.opensheet.client.dto.UserDTO;
import org.opensheet.client.dto.grid.HourFolder;
import org.opensheet.client.l10n.OpensheetConstants;
import org.opensheet.client.l10n.OpensheetMessages;
import org.opensheet.client.mvc.events.EventBus;
import org.opensheet.client.mvc.events.SheetPanelEvent;
import org.opensheet.client.services.AssignmentService;
import org.opensheet.client.services.AssignmentServiceAsync;
import org.opensheet.client.services.HourService;
import org.opensheet.client.services.HourServiceAsync;
import org.opensheet.client.services.SystemService;
import org.opensheet.client.services.SystemServiceAsync;
import org.opensheet.client.utils.MyTreeGridView;
import org.opensheet.client.utils.Resources;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid.ClicksToEdit;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.Joint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class SheetPanel extends ContentPanel {

	private UserSwitchMenu userSwitchMenu;
	private FootBarSheetPanel footBarSheetPanel;
	TreeStore<ModelData> store;
	ListStore<BeanModel> userStore = new ListStore<BeanModel>();
	ListLoader<?> userloader;
	List<Integer> disabledAssignments;
	EditorTreeGrid<ModelData> tree;
	HashMap<Integer, Boolean> holidays;
	private MonthHelper monthHelper = new MonthHelper();
	private UserDTO userDTO;
	private UserDTO currentUserDTO;
	private List<ColumnConfig> columns;
	private Boolean timesheetInputMode;
	private Date timesheetInputBorderDate;

	Text textDaysInMonth;
	Button thisMonthButton;
	ComboBox<BeanModel> usersComboBox;
	Date date = new Date();
	final private HourServiceAsync hourService = GWT.create(HourService.class);
	final private AssignmentServiceAsync assignmentService = GWT
			.create(AssignmentService.class);
	private SystemServiceAsync systemService = GWT.create(SystemService.class);
	private OpensheetConstants myConstants = (OpensheetConstants) GWT
			.create(OpensheetConstants.class);
	private OpensheetMessages myMessages = (OpensheetMessages) GWT
			.create(OpensheetMessages.class);

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public SheetPanel() {

		addListener(Events.Attach, new Listener() {
			@Override
			public void handleEvent(BaseEvent be) {

				getHolidays(date);
				
				
			}
		});

		// hmmm ?
		userDTO = (UserDTO) Registry.get("userCurrent");
		currentUserDTO = (UserDTO) Registry.get("userCurrent");
		getTimesheetEditMode();
		store = new TreeStore<ModelData>();

		columns = new ArrayList<ColumnConfig>();

		ColumnConfig type = new ColumnConfig("type", myConstants.type(), 51);
		type.setFixed(true);
		type.setSortable(false);
		type.setMenuDisabled(true);
		type.setAlignment(HorizontalAlignment.LEFT);
		type.setRenderer(new GridCellRenderer<ModelData>() {
			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {

				String itype = "";

				if (Integer.parseInt((String) model.get(property).toString()) == 0) {
					itype = "cache";
				} else if (Integer.parseInt((String) model.get(property)
						.toString()) == 1) {
					itype = "filetypes";
				} else if (Integer.parseInt((String) model.get(property)
						.toString()) == 2) {
					itype = "kteatime";
				} else if (Integer.parseInt((String) model.get(property)
						.toString()) == 3) {
					itype = "kuickshow";
				} else if (Integer.parseInt((String) model.get(property)
						.toString()) == 4) {
					itype = "alarm_bell";
				} else if (Integer.parseInt((String) model.get(property)
						.toString()) == 5) {
					itype = "sum";
				}

				return "<img src=\"resources/images/nuvola/16x16/apps/" + itype
						+ ".png\" />";

			}
		});

		columns.add(type);

		ColumnConfig name = new ColumnConfig("name", myConstants.assignment(),
				320);
		name.setFixed(true);
		name.setMenuDisabled(true);
		name.setStyle("padding-left:0px;");
		name.setFixed(true);
		name.setSortable(false);
		name.setAlignment(HorizontalAlignment.LEFT);
		name.setRenderer(new TreeGridCellRenderer<ModelData>() {

			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				config.css = "x-treegrid-column";
				assert grid instanceof TreeGrid : "TreeGridCellRenderer can only be used in a TreeGrid";
				TreeGrid tree = (TreeGrid) grid;
				TreeStore ts = tree.getTreeStore();
				int level = ts.getDepth(model);
				String id = getId(tree, model, property, rowIndex, colIndex);
				String text = getText(tree, model, property, rowIndex, colIndex);
				AbstractImagePrototype icon = calculateIconStyle(tree, model,
						property, rowIndex, colIndex);
				Joint j = calcualteJoint(tree, model, property, rowIndex,
						colIndex);

				return tree.getTreeView().getTemplate(model, id, text, icon,
						false, j, level - 1);

			}

		});

		columns.add(name);

		ColumnConfig sum = new ColumnConfig("summ", "∑", 39);
		sum.setAlignment(HorizontalAlignment.CENTER);
		sum.setFixed(true);
		columns.add(sum);

		NumberField nf = new NumberField();
		nf.setAllowDecimals(false);
		nf.setPropertyEditorType(Integer.class);
		CellEditor ce = new CellEditor(nf);

		Integer i = 32;

		for (Integer day = 1; day < i; day++) {
			final Integer thisDay = day;

			ColumnConfig dayColumnConfig = new ColumnConfig(day.toString(),
					day.toString(), 22);

			dayColumnConfig.setEditor(ce);
			dayColumnConfig.setMenuDisabled(true);
			dayColumnConfig.setSortable(false);
			dayColumnConfig.setFixed(true);
			dayColumnConfig.setRenderer(new GridCellRenderer<ModelData>() {
				@Override
				public Object render(ModelData model, String property,
						ColumnData config, int rowIndex, int colIndex,
						ListStore<ModelData> store, Grid<ModelData> grid) {

					if (holidays.size() >= thisDay) {
						if (holidays.get(thisDay) == true) {
							config.css = "x-treegrid-column-holiday";
						}else{
							config.css = "x-treegrid-column-noholiday";
						}
					} else if (holidays.size() < thisDay) {
						config.css = "x-treegrid-column-outoforder";
					}
					if (model.get(property) == null) {
						return "";
					} else {
						if (Integer.parseInt((String) model.get(property)
								.toString()) > 8) {
							return "<span style='color: red'>"
									+ model.get(property) + "</span>";
						} else {
							return "<span style='color: black'>"
									+ model.get(property) + "</span>";
						}
					}
				}
			});

			columns.add(dayColumnConfig);
		}

		ColumnModel cm = new ColumnModel(columns);

		TextField<String> text = new TextField<String>();
		text.setAllowBlank(false);

		thisMonthButton = new Button();
		thisMonthButton.setWidth(100);
		thisMonthButton.setText(monthHelper.get(date.getMonth()) + " "
				+ (date.getYear() + 1900));

		Button privMonthButton = new Button();
		privMonthButton.setIconStyle("privMonthButton");
		privMonthButton.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				date.setMonth(date.getMonth() - 1);
				thisMonthButton.setText(monthHelper.get(date.getMonth()) + " "
						+ (date.getYear() + 1900));
				getHolidays(date);
			}

		});

		Button nextMonthButton = new Button();
		nextMonthButton.setIconStyle("nextMonthButton");
		nextMonthButton.addListener(Events.Select, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {

				date.setMonth(date.getMonth() + 1);
				thisMonthButton.setText(monthHelper.get(date.getMonth()) + " "
						+ (date.getYear() + 1900));
				getHolidays(date);
			}

		});

		Button xlsExportButton = new Button();
		xlsExportButton.setText("Excel Export");
		xlsExportButton.setIcon(Resources.ICONS.table());
		xlsExportButton.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				Integer getYear = date.getYear() + 1900;
				Window.Location.assign(GWT.getHostPageBaseURL().toString()
						+ "timesheettoxls.htm?user_id=" + userDTO.getId()
						+ "&month=" + date.getMonth() + "&year=" + getYear);

			}

		});

		Button printButton = new Button();
		printButton.setText("Print Me");
		printButton.setIconStyle("icon-printer");
		printButton.addListener(Events.Select, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				Integer getYear = date.getYear() + 1900;

				String winUrl = GWT.getHostPageBaseURL()
						+ "timesheettoprinter.htm?user_id=" + userDTO.getId()
						+ "&month=" + date.getMonth() + "&year=" + getYear;
				String winName = "Print Page";
				openNewWindow(winName, winUrl);
			}
		});

		Text textUserName = new Text();
		textUserName.setText(currentUserDTO.getfullName());
		textUserName.setStateId("icon-user");
		textUserName.setWidth(200);

		Text textBR = new Text();
		textBR.setText("BR : 100%");
		textBR.setWidth(60);

		Text textFTEE = new Text();
		textFTEE.setText("FTEE : 100%");
		textFTEE.setWidth(80);

		textDaysInMonth = new Text();
		textDaysInMonth.setWidth(140);

		ContentPanel cp = new ContentPanel();
		cp.setBodyBorder(false);
		cp.setButtonAlign(HorizontalAlignment.CENTER);
		cp.setLayout(new FitLayout());
		cp.setFrame(true);
		cp.setSize(1200, 350);

		tree = new EditorTreeGrid<ModelData>(store, cm);
		tree.setClicksToEdit(ClicksToEdit.ONE);
		tree.setBorders(true);
		tree.setAutoWidth(false);
		tree.setLoadMask(true);
		tree.setColumnLines(true);
		tree.getStyle().setLeafIcon(IconHelper.createStyle("icon-music"));
		tree.setSize(1200, 400);
		tree.setAutoExpandColumn("name");
		tree.setTrackMouseOver(false);
		tree.addListener(Events.BeforeEdit,
				new Listener<GridEvent<BaseModel>>() {
					public void handleEvent(GridEvent<BaseModel> be) {
						Integer assignmentId = Integer.parseInt((String) be
								.getRecord().get("index").toString());
						if (holidays.size() < Integer.parseInt((String) be
								.getProperty())) {
							be.setCancelled(true);
						} else if (tree.getTreeStore()
								.getModelState(be.getModel()).getChildCount() > 0) {
							be.setCancelled(true);
						} else if (be.getRecord().get("type").toString()
								.equals("4")) {
							be.setCancelled(true);
						} else if (be.getRecord().get("type").toString()
								.equals("5")) {
							be.setCancelled(true);

						} else if (currentUserDTO.equals(userDTO)
								&& timesheetInputMode == true
								&& currentUserDTO.getPermission().getAdmin() != true
								&& (new Date().getMonth() - date.getMonth()) == 1
								&& disabledAssignments.contains(assignmentId) != true) {
							/**
							 * ADD new year fix
							 * 
							 */
							int results = timesheetInputBorderDate
									.compareTo(date);
							if (results < 0) {
								be.setCancelled(true);
							}

						} else if (currentUserDTO.getPermission().getAdmin() != true
								&& date.getMonth() != new Date().getMonth()) {
							be.setCancelled(true);

						} else if (disabledAssignments.contains(assignmentId)) {
							be.setCancelled(true);
						}
						/**
						 * Check, what hour is in assignments
						 * 
						 */

						Integer thisHour = null;
						if (be.getValue() != null) {
							thisHour = Integer.parseInt(be.getValue()
									.toString());
						}
						Boolean leaf = Boolean.parseBoolean(be.getRecord()
								.get("leaf").toString());
						Integer type = Integer.parseInt(be.getRecord()
								.get("type").toString());
						Integer day = Integer.parseInt((String) be
								.getProperty().toString());
						String sum = be.getRecord().get("summ").toString();
						Date actionDate = new Date();
						actionDate.setYear(date.getYear());
						actionDate.setMonth(date.getMonth());
						actionDate.setDate(day);
						footBarSheetPanel.updateData(userDTO, actionDate,
								assignmentId, thisHour, sum, leaf, type);

					}
				});

		tree.addListener(Events.AfterEdit,
				new Listener<GridEvent<BaseModel>>() {
					public void handleEvent(GridEvent<BaseModel> be) {
						if (be.getStartValue() != be.getValue()) {
							Integer newValue = 0;
							Integer oldValue = 0;

							if (be.getValue() != null) {
								newValue = Integer.parseInt((String) be
										.getValue().toString());
							}

							if (be.getStartValue() != null) {
								oldValue = Integer.parseInt((String) be
										.getStartValue().toString());
							}

							Integer assignmentId = Integer.parseInt((String) be
									.getRecord().get("index").toString());
							Integer day = Integer.parseInt((String) be
									.getProperty().toString());

							Date actionDate = new Date();
							actionDate.setYear(date.getYear());
							actionDate.setMonth(date.getMonth());
							actionDate.setDate(day);

							hourService.setOrUpdateHour(userDTO, actionDate,
									oldValue, newValue, assignmentId,
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											MessageBox.alert("Cannot add Hour",
													caught.getMessage(), null);
										}

										@Override
										public void onSuccess(Void result) {

											Info.display(
													"Hours Has Been Added",
													"Succesfull");

										}
									});
						}

					}
				});

		/*
		 * Event for UserSwitch
		 */
		EventBus.get().addListener(SheetPanelEvent.UserChanged,
				new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent e) {
						userDTO.setId(Integer.parseInt((String) userSwitchMenu
								.getSelectedUser().getId().toString()));
						populateTimesheet();
					}
				});

		

		cp.add(tree);

		
		
		cp.setStyleName("border-collapse");
		
		
		
		
		cp.setHeaderVisible(false);
		cp.addButton(new Button(myConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {

						populateTimesheet();
					}
				}));

		ToolBar tb = new ToolBar();
		userSwitchMenu = new UserSwitchMenu(currentUserDTO);
		userSwitchMenu.disable();
		if (currentUserDTO.getPermission().getAdmin() == true
				|| currentUserDTO.getPermission().getDm() == true) {
			userSwitchMenu.enable();
		}
		tb.add(userSwitchMenu);
		tb.add(new SeparatorToolItem());
		tb.add(privMonthButton);
		tb.add(thisMonthButton);
		tb.add(nextMonthButton);
		tb.add(new SeparatorToolItem());
		tb.add(xlsExportButton);
		tb.add(new SeparatorToolItem());
		tb.add(printButton);
		tb.add(new SeparatorToolItem());
		tb.add(textUserName);
		tb.add(new SeparatorToolItem());
		tb.add(textBR);
		tb.add(new SeparatorToolItem());
		tb.add(textFTEE);
		tb.add(new SeparatorToolItem());
		tb.add(textDaysInMonth);
		tb.add(new SeparatorToolItem());

		add(tb);
		add(cp);
		setHeaderVisible(false);
		footBarSheetPanel = new FootBarSheetPanel();
		add(footBarSheetPanel);

	}

	private void populateTimesheet() {
		tree.mask("Loading Data");
		final HourServiceAsync hourService = GWT.create(HourService.class);
		hourService.getHours(userDTO, date, new AsyncCallback<BaseTreeModel>() {
			public void onFailure(Throwable caught) {
				MessageBox.alert("Cannot get Hours", caught.getMessage(), null);
			}

			public void onSuccess(BaseTreeModel model) {
				if (model == null) {
					MessageBox.alert("Cannot get Hours",
							"server answer is null", null);
				} else {
					store.removeAll();
					store.add(model.getChildren(), true);
					store.sort(myConstants.type(), SortDir.ASC);
					tree.unmask();
				}
			}
		});
	}

	private void getHolidays(Date date) {
		final HourServiceAsync hourService = GWT.create(HourService.class);
		hourService.getHolidays(date,
				new AsyncCallback<HashMap<Integer, Boolean>>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert("Cannot get holidays",
								caught.getMessage(), null);
					}

					@Override
					public void onSuccess(HashMap<Integer, Boolean> holidaysHM) {
						holidays = holidaysHM;
						Integer count = 0;
						for (Integer i = 1; i <= holidays.size(); i++) {
							if (holidays.get(i) == false) {
								count++;
							}
						}
						textDaysInMonth.setText(myMessages
								.this_month_we_have_param_work_days_and_param_work_hours(
										count.toString(),
										new Integer(count * 8).toString()));
						getDisabledAssignments(userDTO);
					}
				});
	}

	private void getDisabledAssignments(UserDTO userDTO) {
		assignmentService.getDisabledAssignment(userDTO, date,
				new AsyncCallback<List<Integer>>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert("Cannot get Disabled Assignments",
								caught.getMessage(), null);
					}

					@Override
					public void onSuccess(List<Integer> result) {
						disabledAssignments = result;
						populateTimesheet();
					}
				});

	}

	public static void openNewWindow(String name, String url) {
		com.google.gwt.user.client.Window.open(url, name.replace(" ", "_"),
				"menubar=no," + "location=false," + "resizable=yes,"
						+ "scrollbars=yes," + "status=no," + "dependent=true");
	}

	private void getTimesheetEditMode() {
		systemService
				.getTimeSheetInputMode(new AsyncCallback<HashMap<Boolean, Date>>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.info("Error", caught.getMessage(), null);
					}

					@Override
					public void onSuccess(HashMap<Boolean, Date> result) {
						if (result.containsKey(true)) {
							timesheetInputMode = true;
							timesheetInputBorderDate = result.get(true);
						} else {
							timesheetInputMode = false;
						}
					}
				});
	}
}

class MonthHelper {
	private OpensheetConstants myConstants = (OpensheetConstants) GWT
			.create(OpensheetConstants.class);

	public String get(Integer number) {
		String answer = "";
		switch (number) {
		case 0: {
			answer = myConstants.January();
			break;
		}
		case 1: {
			answer = myConstants.February();
			break;
		}

		case 2: {
			answer = myConstants.March();
			break;
		}
		case 3: {
			answer = myConstants.April();
			break;
		}

		case 4: {
			answer = myConstants.May();
			break;
		}
		case 5: {
			answer = myConstants.June();
			break;
		}
		case 6: {
			answer = myConstants.July();
			break;
		}
		case 7: {
			answer = myConstants.August();
			break;
		}
		case 8: {
			answer = myConstants.September();
			break;
		}
		case 9: {
			answer = myConstants.October();
			break;
		}
		case 10: {
			answer = myConstants.November();
			break;
		}
		case 11: {
			answer = myConstants.December();
			break;
		}
		case 12: {
			answer = myConstants.January();
			break;
		}
		}

		return answer;
	}

}
