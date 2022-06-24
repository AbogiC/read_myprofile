package com.sbs.myprofile.portlet;

import com.astra.core.elibraryData.model.company;
import com.astra.core.elibraryData.model.employe;
import com.astra.core.elibraryData.service.employeLocalServiceUtil;
import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.sbs.astra.util.AstraUtil;
import com.sbs.myprofile.constants.MyProfileConstants;
import com.sbs.myprofile.constants.MyProfilePortletKeys;
import com.sbs.myprofile.exception.MyProfileValidateException;
import com.sbs.myprofile.util.MyProfileUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Softbless
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.AMDI",
		"com.liferay.portlet.header-portlet-css=/css/my-profile.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=MyProfile",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + MyProfilePortletKeys.MYPROFILE,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class MyProfilePortlet extends MVCPortlet {
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		_log.info("emailAddress: " + themeDisplay.getUser().getEmailAddress());
		
		DynamicQuery employeeQuery = employeLocalServiceUtil.dynamicQuery();
		employeeQuery.add(PropertyFactoryUtil.forName("emailOffice").eq(themeDisplay.getUser().getEmailAddress()));
		employeeQuery.add(PropertyFactoryUtil.forName("rowStatus").eq(true));
				
		List<employe> employees = employeLocalServiceUtil.dynamicQuery(employeeQuery);
		employe employee = null;
		if(employees.size() > 0) {
			employee = employees.get(0);
		} else {
			employee = getNewObject(0);
			employee.setName(themeDisplay.getUser().getFullName());
			employee.setEmailOffice(themeDisplay.getUser().getEmailAddress());
		}
		
		List<company> companies = AstraUtil.getSortedAstraCompanies();
		
		renderRequest.setAttribute("employee", employee);
		renderRequest.setAttribute("companies", companies);
		
		super.render(renderRequest, renderResponse);
	}
	
	@Override
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		try {
			long employeeId = ParamUtil.getLong(actionRequest, "employeeId", 0);
			employe entry = getEmployeeFromRequest(employeeId, actionRequest);
	        
	        String screenName = AstraUtil.getScreenNameFromEmail(entry.getEmailOffice());
	        if(screenName.isEmpty()) {
	        	List<String> errors = new ArrayList<>();
	        	errors.add("employee-officeemail-required");
	        	throw new MyProfileValidateException(errors);
	        }
	        
	        // validate email if different email from user
        	if(!themeDisplay.getUser().getEmailAddress().equalsIgnoreCase(entry.getEmailOffice())) {
	        	DynamicQuery userQuery = UserLocalServiceUtil.dynamicQuery();
	        	Disjunction disjunction = RestrictionsFactoryUtil.disjunction();
	        	disjunction.add(RestrictionsFactoryUtil.eq("screenName", screenName));
	        	disjunction.add(RestrictionsFactoryUtil.eq("emailAddress", entry.getEmailOffice()));
	        	userQuery.add(disjunction);
	        	
	        	List<User> users = UserLocalServiceUtil.dynamicQuery(userQuery);
	        	
	        	if(users.size() > 0) {
	        		List<String> errors = new ArrayList<String>();
		            errors.add("office-email-already-exist");
		            throw new MyProfileValidateException(errors);
	        	}
        	}
        	
        	MyProfileUtil.validate(entry);
	        
	        // Permission check
	        if(employeeId > 0) { //update
	        	employe employee = employeLocalServiceUtil.getEmployee(employeeId);
	        	if(!employee.getEmailOffice().equalsIgnoreCase(themeDisplay.getUser().getEmailAddress())) {
		            List<String> errors = new ArrayList<String>();
		            errors.add("permission-error");
		            throw new MyProfileValidateException(errors);
	        	}
	        		        	
	        	entry.setCreateBy(employee.getCreateBy());	
    			entry.setModifedBy(themeDisplay.getUser().getEmailAddress());
    			entry.setEmployeeStatus(employee.getEmployeeStatus());
    			entry.setRowStatus(employee.getRowStatus());
    			
    			MyProfileUtil.updateEntry(employeeId, entry);
	        } else { //add		        				
				entry.setCreateBy(themeDisplay.getUser().getEmailAddress());
				entry.setModifedBy(themeDisplay.getUser().getEmailAddress());
				entry.setEmployeeStatus(MyProfileConstants.STATUS_ACTIVE);
				entry.setRowStatus(true);
				
				MyProfileUtil.addEntry(entry);
	        }
	        
	        User user = themeDisplay.getUser();
	        user.setScreenName(screenName);
	        user.setEmailAddress(entry.getEmailOffice());
	        UserLocalServiceUtil.updateUser(user);
	        SessionMessages.add(actionRequest, "employee-saved-successfully");
	        
		} catch (MyProfileValidateException e) {
//			StringBuilder errorMessage = new StringBuilder();
            for (String error : e.getErrors()) {
            	_log.info("My Profile error: " + error);
//            	errorMessage.append(error + " ");
                SessionErrors.add(actionRequest, error);
            }
//            actionResponse.setRenderParameter("errorMessage", errorMessage.toString());
            PortalUtil.copyRequestParameters(actionRequest, actionResponse);
            hideDefaultSuccessMessage(actionRequest);
		} catch (Exception e) {			
			e.printStackTrace();
//			StringWriter sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			e.printStackTrace(pw);
//			
//			actionResponse.setRenderParameter("errorMessage", sw.toString());
			SessionErrors.add(actionRequest, Exception.class);
			hideDefaultSuccessMessage(actionRequest);
		}
		
		super.processAction(actionRequest, actionResponse);
	}
	
	public employe getEmployeeFromRequest(
        long primaryKey, PortletRequest request) throws PortletException, MyProfileValidateException {
        ThemeDisplay themeDisplay = (ThemeDisplay) request
            .getAttribute(WebKeys.THEME_DISPLAY);

        // Create or fetch existing data
        employe employee;
        if (primaryKey <= 0) {
            employee = getNewObject(primaryKey);
        } else {
            employee = employeLocalServiceUtil.getEmployee(primaryKey);
        }

        try {
		
			employee.setName(ParamUtil.getString(request, "name"));
			employee.setNik(ParamUtil.getString(request, "nik"));
			employee.setNpk(ParamUtil.getString(request, "npk"));
			employee.setEmailOffice(ParamUtil.getString(request, "emailOfficeInput"));
			employee.setEmailPersonal(ParamUtil.getString(request, "emailPersonal"));
			employee.setPosition(ParamUtil.getString(request, "position"));
			employee.setBirth(getDateTimeFromRequest(request, "birth"));
			employee.setPhone1(ParamUtil.getString(request, "phone1"));
			employee.setPhone2(ParamUtil.getString(request, "phone2"));
			employee.setOfficeExtension(ParamUtil.getString(request, "officeExtension"));
			employee.setGender(ParamUtil.getString(request, "gender"));
			
			employee.setEmployeeStatus(MyProfileConstants.STATUS_ACTIVE);
			
			employee.setCompanyId(ParamUtil.getLong(request, "company"));
						
        } catch (Exception e) {
            _log.error("Errors occur while populating the model",e);
            List<String> error = new ArrayList<>();
            error.add("value-convert-error");
            throw new MyProfileValidateException(error);
        }

        return employee;
    }
	
	private employe getNewObject(long primaryKey) {

        primaryKey = (primaryKey <= 0) ? 0 : CounterLocalServiceUtil.increment();
        return employeLocalServiceUtil.createemploye(primaryKey);
    }
		
	private Date getDateTimeFromRequest(PortletRequest request, String prefix) {
        int Year = ParamUtil.getInteger(request, prefix + "Year");
        int Month = ParamUtil.getInteger(request, prefix + "Month") + 1;
        int Day = ParamUtil.getInteger(request, prefix + "Day");
        int Hour = ParamUtil.getInteger(request, prefix + "Hour");
        int Minute = ParamUtil.getInteger(request, prefix + "Minute");
        int AmPm = ParamUtil.getInteger(request, prefix + "AmPm");

        if (AmPm == Calendar.PM) {
            Hour += 12;
        }

        LocalDateTime ldt;
        try {
            ldt = LocalDateTime.of(Year, Month, Day, Hour, Minute, 0);

        } catch (Exception e) {
            
            _log.error("Unnable get date data. Initialize with current date", e);
            Date in = new Date();
            Instant instant = in.toInstant();
            return Date.from(instant);
        }
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
		
	private static Log _log = LogFactoryUtil.getLog(MyProfilePortlet.class);
}