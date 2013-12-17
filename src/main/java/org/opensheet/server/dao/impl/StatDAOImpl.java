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
package org.opensheet.server.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.map.LinkedMap;
import org.hibernate.SessionFactory;
import org.opensheet.server.dao.AssignmentDAO;
import org.opensheet.server.dao.DepartmentDAO;
import org.opensheet.server.dao.StatDAO;
import org.opensheet.server.dao.UserDAO;
import org.opensheet.shared.model.Assignment;
import org.opensheet.shared.model.Department;
import org.opensheet.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.extjs.gxt.ui.client.data.BaseModel;

@Repository
public class StatDAOImpl implements StatDAO{

	@Autowired	private SessionFactory sessionFactory;
	@Autowired	private AssignmentDAO  assignmentDAO;
	@Autowired	private DepartmentDAO departmentDAO;
	@Autowired	private UserDAO userDAO;
	final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	
	@SuppressWarnings({ "unchecked"})
	@Override
	@Transactional
	public List<Number> getAssignmentSumByMonthByYear(int year, int assignemntId) {
		Map<Integer,Integer> hs =  new HashMap<Integer,Integer>();
		Assignment assignment = assignmentDAO.getAssignmentById(assignemntId);
		List<Object[]> hm = null;
		
		if(assignment.getLevel() == 0){ 
				hm = 	(List<Object[]>) 	sessionFactory.getCurrentSession().createQuery("SELECT sum(h.hour) as hour,MONTH(h.date) as month FROM Hour h WHERE YEAR(h.date) = ? AND assignment=? GROUP by MONTH(h.date)").
				setInteger(0, year).
				setInteger(1, assignment.getId()).
				list();
		}else if(assignment.getLevel() == 1){
					hm = 	(List<Object[]>) 	sessionFactory.getCurrentSession().createQuery("SELECT sum(h.hour) as hour,MONTH(h.date) as month FROM Hour h WHERE YEAR(h.date) = ? AND task=? GROUP by MONTH(h.date)").
					setInteger(0, year).
					setInteger(1, assignment.getId()).
					list();	
		}
		
		for(Object[] n: hm)
			hs.put((Integer)Integer.parseInt(n[1].toString()),(Integer) Integer.parseInt(n[0].toString()));
				
				
		List<Number> al = new ArrayList<Number>(); 
	      for(Integer i =0;i < 12; i++){
	    	  Integer t = i+1;
	    	  if(hs.containsKey(t)){
		    	  al.add((Number) Integer.parseInt(hs.get(t).toString()));  //shitty code
	    	  }else{
	    		  al.add(0);
	    	  }
	      }		
			
		return al;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Number> getAssignmentDepartmentSumByMonthByYear(int year,int assignemntId, int departmentId) {
		Map<Integer,Integer> hs =  new HashMap<Integer,Integer>();
		Assignment assignment = assignmentDAO.getAssignmentById(assignemntId);
		List<Object[]> hm = null;
		
		if(assignment.getLevel() == 0){ 
				hm = 	(List<Object[]>) 	sessionFactory.getCurrentSession().createQuery("SELECT sum(h.hour) as hour,MONTH(h.date) as month FROM Hour h WHERE YEAR(h.date) = ? AND assignment=? AND department=? GROUP by MONTH(h.date)").
				setInteger(0, year).
				setInteger(1, assignment.getId()).
				setInteger(2, departmentId).
				list();
		}else if(assignment.getLevel() == 1){
					hm = 	(List<Object[]>) 	sessionFactory.getCurrentSession().createQuery("SELECT sum(h.hour) as hour,MONTH(h.date) as month FROM Hour h WHERE YEAR(h.date) = ? AND task=? AND department=? GROUP by MONTH(h.date)").
					setInteger(0, year).
					setInteger(1, assignment.getId()).
					setInteger(2, departmentId).
					list();	
		}
		
		for(Object[] n: hm)
			hs.put((Integer)Integer.parseInt(n[1].toString()),(Integer) Integer.parseInt(n[0].toString()));
				
				
		List<Number> al = new ArrayList<Number>(); 
	      for(Integer i =0;i < 12; i++){
	    	  Integer t = i+1;
	    	  if(hs.containsKey(t)){
		    	  al.add((Number) Integer.parseInt(hs.get(t).toString()));  //shitty code
	    	  }else{
	    		  al.add(0);
	    	  }
	      }		
			
		return al;
	}


	@Override
	@Transactional
	public BaseModel getAssignmentQuickDetails(Assignment a) {
		BaseModel answer = new BaseModel();
		
		//yeah? WTF ? but hib doesn't work without session .... strange
		Assignment assignment =  assignmentDAO.getAssignmentById(a.getId());
		
		
		if(assignment != null){
				
			if(assignment.getChildren().size() >=2){
					answer.set("hasChild", true);
				}else{
					answer.set("hasChild", false);
				}
			

			Integer timeline = assignment.getTimeline().getHour();
			if(timeline == null) timeline=0;
			
			Integer hours  = assignmentDAO.getSumHourAssignment(assignment);
			if(hours == null) hours=0;
			
			Integer overtime = timeline - hours;
			
			answer.set("status", assignment.getStatus());
			answer.set("timeline", timeline);
			answer.set("overtime", overtime);
			answer.set("hours",hours);
			answer.set("budget",0);
			answer.set("overbudget",0);

			return answer;
		}
	return null;
		
	}


	@Override
	@Transactional
	public List<BaseModel> getAssignmentStatByPeriod(Date start, Date end,	int assignemntId) {
		Assignment assignment =  assignmentDAO.getAssignmentById(assignemntId);
		
		String query;
		
		if(assignment.getLevel() == 0){
			query = "SELECT SUM(h.hour),MONTH(h.date),YEAR(h.date)  from Hour h where h.date >= ? and h.date <= ? and h.assignment.id =? GROUP BY YEAR(h.date),MONTH(h.date)";
			
		}else{
			query = "SELECT SUM(h.hour),MONTH(h.date),YEAR(h.date)  from Hour h where h.date >= ? and h.date <= ? and h.task.id =? GROUP BY YEAR(h.date),MONTH(h.date)";
			
		}
		
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>) sessionFactory.getCurrentSession().createQuery(query).
				setDate(0, start).
				setDate(1, end).
				setInteger(2, assignemntId).list();
		
		List<BaseModel> answerList = new ArrayList<BaseModel>(list.size());
		
		for(Object[] s: list){
			BaseModel bm = new BaseModel();
			bm.set("hours", s[0]);
			bm.set("month", s[1]);
			bm.set("year", s[2]);
			answerList.add(bm);
		}
		
		return answerList;
	}


	@Override
	@Transactional
	public List<BaseModel> getAssignmentHoursInternalExternalRatesSummThisYear(int assignmentId) {
		
		int upper = 5000;
		List<BaseModel> list = new ArrayList<BaseModel>();
		Random r = new Random();
		
		StringBuffer namesSb = new StringBuffer();
		StringBuffer indexesSb = new StringBuffer();
		
		for(Integer i =0;i < 24; i++){
			BaseModel bm = new BaseModel();
			bm.set("alpha",r.nextInt(upper));
			bm.set("beta",r.nextInt(upper));
			bm.set("gamma",r.nextInt(upper));
			bm.set("alpha_1",r.nextInt(upper));
			bm.set("beta_1",r.nextInt(upper));
			bm.set("gamma_1",r.nextInt(upper));
			
			if(i>=12){
			int tempI  = roundMonth(i);
				bm.set("date",months[tempI]);
			}else{
				bm.set("date",months[i]);
			}
			
			if(i==0){
				
				namesSb.append("alpha_name");
				namesSb.append("@");
				namesSb.append("beta_name");
				namesSb.append("@");
				namesSb.append("gamma_name");
				namesSb.append("@");
				namesSb.append("alpha_name_1");
				namesSb.append("@");
				namesSb.append("beta_name_1");
				namesSb.append("@");
				namesSb.append("gamma_name_1");
				
				
				indexesSb.append("alpha_1");
				indexesSb.append("@");
				indexesSb.append("beta_1");
				indexesSb.append("@");
				indexesSb.append("gamma_1");
				indexesSb.append("@");
				indexesSb.append("alpha_1");
				indexesSb.append("@");
				indexesSb.append("beta_1");
				indexesSb.append("@");
				indexesSb.append("gamma_1");
				
				
				bm.set("indexes", indexesSb.toString());
				bm.set("names", namesSb.toString());

			}
			
			list.add(bm);
		}


		return list;
	}
	
	
	private int roundMonth(int i){
		int answer = i - 12;
		if(answer > 11){
			return roundMonth(answer);
		}else{
			return answer;
		}
	}


	@Override
	public List<BaseModel> getAssignmentHoursMonthThisYear(int assignmentId) {
		StringBuffer namesSb = new StringBuffer();
		StringBuffer indexesSb = new StringBuffer();
		int upper = 5000;
		Random r = new Random();
		
		String[] indexes = {"1","2","3","4","5","6","7"};
		
		List<BaseModel> rows = new ArrayList<BaseModel>();

		
		for(String s: indexes){
			
			BaseModel bm = new BaseModel();
			bm.set("id",s);
			bm.set("name","name_" + s);
			bm.set("hour_"+s,r.nextInt(upper));
			indexesSb.append("hour_"+s+"@");
			namesSb.append(s+"_2011@");
			rows.add(bm);
		}
		
		
		rows.get(0).set("indexes", indexesSb.toString());
		rows.get(0).set("names", namesSb.toString());
		
		
		
		return rows;
	}


	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<BaseModel> getHourReportByUserByPeriodByTypeByBranch(
			Integer branch, Integer type, Date s, Date e) {
		List<Object[]> list = new ArrayList<Object[]>();
		if(type == 9999999 && branch == 9999999){
		list = (List<Object[]>) sessionFactory.getCurrentSession()
		.createQuery("select sum(h.hour),h.user.secondName,h.user.firstName,h.assignment.name," +
				     "sum(h.inratesum),sum(h.extratesum),h.branch.name,h.department.name from Hour h" +
				     " where h.date >= ? and h.date <= ?  group by h.user,h.assignment" +
				     " order by h.user.secondName,h.assignment.name")
				     .setDate(0, s).setDate(1, e).list();
		}else if(type == 9999999){
			list = (List<Object[]>) sessionFactory.getCurrentSession()
					.createQuery("select sum(h.hour),h.user.secondName,h.user.firstName,h.assignment.name," +
							     "sum(h.inratesum),sum(h.extratesum),h.branch.name,h.department.name from Hour h" +
							     " where h.date >= ? and h.date <= ? and h.branch.id=? group by h.user,h.assignment" +
							     " order by h.user.secondName,h.assignment.name")
							     .setDate(0, s).setDate(1, e).setInteger(2, branch).list();
		}else if(branch == 9999999){
			list = (List<Object[]>) sessionFactory.getCurrentSession()
					.createQuery("select sum(h.hour),h.user.secondName,h.user.firstName,h.assignment.name," +
							     "sum(h.inratesum),sum(h.extratesum),h.branch.name,h.department.name from Hour h" +
							     " where h.date >= ? and h.date <= ? and h.assignment.type=? group by h.user,h.assignment" +
							     " order by h.user.secondName,h.assignment.name")
							     .setDate(0, s).setDate(1, e).setInteger(2, type).list();
		}else{
			list = (List<Object[]>) sessionFactory.getCurrentSession()
					.createQuery("select sum(h.hour),h.user.secondName,h.user.firstName,h.assignment.name," +
							     "sum(h.inratesum),sum(h.extratesum),h.branch.name,h.department.name from Hour h" +
							     " where h.date >= ? and h.date <= ? and h.assignment.type=?and h.branch.id=? group by h.user,h.assignment" +
							     " order by h.user.secondName,h.assignment.name")
							     .setDate(0, s).setDate(1, e).setInteger(2, type).setInteger(3, branch).list();
			
			
			
		}
		
		
		
		List<BaseModel> answer  =  new ArrayList<BaseModel>(list.size());
		for(Object[] row: list){
			BaseModel b = new BaseModel();
			b.set("username", row[1] + " " + row[2]);
			b.set("hour", row[0]);
			b.set("assignment", row[3]);
			b.set("inratesum", row[4]);
			b.set("extratesum", row[5]);
			b.set("branch", row[6]);
			b.set("department", row[7]);
			answer.add(b);
		}
		
		
		return answer;
	}


	@Override
	@Transactional
	public Integer getAssignmentDepartmentSum(int assignmentId, int departmentId) {
		Long sumLong = 	(Long) sessionFactory.getCurrentSession().createQuery("SELECT sum(h.hour) as hour " +
				"FROM Hour h WHERE assignment=?" +
				" AND department=?").
				setInteger(0, assignmentId).setInteger(1, departmentId).uniqueResult();
		if(sumLong == null){
			return 0;
		}
		return sumLong.intValue();
	}
	

	@Override
	@Transactional
	public Integer getHourSumByAssignmentAndByDepartmentOnPeriod(
			Assignment assignment, Department department, Calendar starting,
			Calendar ending) {
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.hour) from Hour as h " +
				" where h.assignment=? and h.department=? and h.date >= ? and h.date <= ?")
														  .setEntity(0, assignment)
														  .setEntity(1, department)
														  .setCalendar(2, starting)
														  .setCalendar(3, ending)
														  .uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getHourSumByAssignmentAndByUser(Assignment assignment,User user) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.hour) from Hour as h " +
				" where h." +level+ "=? and h.user=?")
						.setEntity(0, assignment)
						.setEntity(1, user)
						.uniqueResult();
		
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getHourSumByAssignmentAndByUserOnPeriod(
			Assignment assignment, User user, Calendar starting, Calendar ending) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		
		
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.hour) from Hour as h " +
				" where h." +level+ "=? and h.user=? and h.date >= ? and h.date <= ?")
														  .setEntity(0, assignment)
														  .setEntity(1, user)
														  .setCalendar(2, starting)
														  .setCalendar(3, ending)
														  .uniqueResult();
		
		
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getInternalRateSumByAssignmentAndByDepartment(Assignment assignment, Department department) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.inratesum) from Hour as h " +
				" where h." +level+ "=? and h.department=?")
									.setEntity(0, assignment)
									.setEntity(1, department)
									.uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getInternalRateSumByAssignmentAndByDepartmentOnPeriod(Assignment assignment, Department department, Calendar starting,
			Calendar ending) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.inratesum) from Hour as h " +
				" where h." +level+ "=? and h.department=? and h.date >= ? and h.date <= ?")
														  .setEntity(0, assignment)
														  .setEntity(1, department)
														  .setCalendar(2, starting)
														  .setCalendar(3, ending)
														  .uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getExternalRateSumByAssignmentAndByDepartment(
			Assignment assignment, Department department) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.extratesum) from Hour as h " +
				" where h." +level+ "=? and h.department=?")
									.setEntity(0, assignment)
									.setEntity(1, department)
									.uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getExternalRateSumByAssignmentAndByDepartmentOnPeriod(
			Assignment assignment, Department department, Calendar starting,
			Calendar ending) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.extratesum) from Hour as h " +
				" where h." +level+ "=? and h.department=? and h.date >= ? and h.date <= ?")
														  .setEntity(0, assignment)
														  .setEntity(1, department)
														  .setCalendar(2, starting)
														  .setCalendar(3, ending)
														  .uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getInternalRateSumByAssignmentAndByDepartmentAndByUser(
			Assignment assignment, Department department, User user) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.extratesum) from Hour as h " +
				" where h." +level+ "=? and h.department=? and h.user=?")
									.setEntity(0, assignment)
									.setEntity(1, department)
									.setEntity(2, user)
									.uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@Override
	@Transactional
	public Integer getExternalRateSumByAssignmentAndByDepartmentAndByUser(
			Assignment assignment, Department department, User user) {
		String level;
		if(assignment.getLevel() == 0){
			level = "assignment";
		}else{
			level = "task";
		}
		Long answer = (Long) sessionFactory.getCurrentSession().createQuery("SELECT SUM(h.extratesum) from Hour as h " +
				" where h." +level+ "=? and h.department=? and h.user=?")
									.setEntity(0, assignment)
									.setEntity(1, department)
									.setEntity(2, user)
									.uniqueResult();
		if(answer == null){
			return 0;
		}
		return answer.intValue();
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<BaseModel> getHourReportByUserByPeriodByAssignmentByTypeByBranch(Integer assignment,
			Integer branch, Integer type, Date s, Date e) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(h.hour),h.assignment.name,");
		sb.append("sum(h.inratesum),sum(h.extratesum),");
		sb.append("h.user.secondName,h.user.firstName,");
		sb.append("h.branch.name,h.department.name,");
		sb.append("h.user.userRate.internalRate ");
		sb.append(" from Hour h ");
		sb.append(" where h.date >= ? and h.date <= ? ");
		if(branch != 9999999)
			sb.append(" and h.branch.id="+ branch + " ");
		if(type != 9999999)
			sb.append(" and h.assignment.type="+ type + " ");
		if(assignment != 9999999)
			sb.append(" and h.assignment.id="+ assignment + " ");
		
		sb.append(" group by h.user,h.assignment  order by h.user.secondName,h.assignment.name");
		List<Object[]> list = (List<Object[]>) sessionFactory.getCurrentSession()
							.createQuery(sb.toString()).setDate(0, s).setDate(1, e)
							.list();
		
		List<BaseModel> answer  =  new ArrayList<BaseModel>(list.size());
		for(Object[] row: list){
			BaseModel b = new BaseModel();
			
			Integer inratesum = Integer.parseInt(row[2].toString());
			if(inratesum == 0){
				Integer hour = Integer.parseInt(row[0].toString());
				Integer internalRate = Integer.parseInt(row[8].toString());
				b.set("inratesum", hour*internalRate);
			}else{
				b.set("inratesum", row[2]);
			}
			
			
			b.set("username", row[4] + " " + row[5]);
			b.set("hour", row[0]);
			b.set("assignment", row[1]);
			b.set("extratesum", row[3]);
			b.set("branch", row[6]);
			b.set("department", row[7]);
			answer.add(b);
		}
		
		
		return answer;
	}



}


