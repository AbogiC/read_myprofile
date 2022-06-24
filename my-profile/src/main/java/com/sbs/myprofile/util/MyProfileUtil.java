package com.sbs.myprofile.util;

import com.astra.core.elibraryData.model.employe;
import com.astra.core.elibraryData.service.employeLocalServiceUtil;
import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;
import com.sbs.astra.util.AstraUtil;
import com.sbs.myprofile.exception.MyProfileValidateException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyProfileUtil {
    public static void validate(employe employee) throws PortalException {
    	List<String> errors = new ArrayList<>();
		// Field employeeName
    	if (Validator.isNull(employee.getName())) {
    		errors.add("employee-employeename-required");
    	}    	
    	 
        if (Validator.isNull(employee.getNik())) {
            errors.add("employee-nik-required");
        } else if(!AstraUtil.isValidNIK(employee.getNik())) {
        	errors.add("employee-nik-is-invalid");
        }
    	   
        if (Validator.isNull(employee.getNpk())) {
            errors.add("employee-npk-required");
        }
    	
        if (Validator.isNull(employee.getEmailOffice())) {
            errors.add("employee-officeemail-required");
        } else if(!AstraUtil.isValidEmailDomain(employee.getEmailOffice())) {
        	errors.add("employee-officeemail-domain-is-invalid");
        }
    	
       
    	 
        if (Validator.isNull(employee.getBirth())) {
            errors.add("employee-birthday-required");
        }	
    	   
        if (Validator.isNull(employee.getPhone1())) {
            errors.add("employee-mobilephone1-required");
        }
   
        if (Validator.isNull(employee.getGender())) {
            errors.add("employee-gender-required");
        }

//    	        if (!StringUtils.isNotEmpty(field)) {
//    	            errors.add("employee-usertype-required");
//    	        }

        if (Validator.isNull(employee.getEmployeeStatus())) {
            errors.add("employee-employeestatus-required");
        }    	    
        
        if (employee.getCompanyId() == 0) {
            errors.add("employee-company-required");
        } 
        
        if(!errors.isEmpty()) {
        	throw new MyProfileValidateException(errors);
        }        
    }
    
    public static employe addEntry(employe entry)
            throws PortalException {

        long id = CounterLocalServiceUtil.increment(employe.class.getName());

        employe newEntry = employeLocalServiceUtil.createemploye(id);

        Date now = new Date();
        newEntry.setCompanyId(entry.getCompanyId());
        newEntry.setCreateDate(now);
        newEntry.setModifedDate(now);

        newEntry.setName(entry.getName());
        newEntry.setNik(entry.getNik());
        newEntry.setNpk(entry.getNpk());
        newEntry.setEmailOffice(entry.getEmailOffice());
        newEntry.setEmailPersonal(entry.getEmailPersonal());
        newEntry.setPosition(entry.getPosition());
        newEntry.setBirth(entry.getBirth());
        newEntry.setPhone1(entry.getPhone1());
        newEntry.setPhone2(entry.getPhone2());
        newEntry.setOfficeExtension(entry.getOfficeExtension());
        newEntry.setGender(entry.getGender());
        newEntry.setUkuranJaket(entry.getUkuranJaket());
//        newEntry.setUserType(entry.getUserType());
        newEntry.setEmployeeStatus(entry.getEmployeeStatus());
        newEntry.setGolonganKerjaId(entry.getGolonganKerjaId());
        newEntry.setMasaKerjaId(entry.getMasaKerjaId());
        newEntry.setCompanyId(entry.getCompanyId());
        newEntry.setCreateBy(entry.getCreateBy());
        newEntry.setModifedBy(entry.getModifedBy());
        newEntry.setRowStatus(entry.getRowStatus());

        return employeLocalServiceUtil.addemploye(newEntry);
    }
    
    public static employe updateEntry(long primaryKey, employe entry)
            throws PortalException {

        employe updateEntry = employeLocalServiceUtil.getemploye(primaryKey);

        Date now = new Date();
        updateEntry.setCompanyId(entry.getCompanyId());
        updateEntry.setCreateDate(entry.getCreateDate());
        updateEntry.setModifedDate(now);

        updateEntry.setId(entry.getId());
        updateEntry.setName(entry.getName());
        updateEntry.setNik(entry.getNik());
        updateEntry.setNpk(entry.getNpk());
        updateEntry.setEmailOffice(entry.getEmailOffice());
        updateEntry.setEmailPersonal(entry.getEmailPersonal());
        updateEntry.setPosition(entry.getPosition());
        updateEntry.setBirth(entry.getBirth());
        updateEntry.setPhone1(entry.getPhone1());
        updateEntry.setPhone2(entry.getPhone2());
        updateEntry.setOfficeExtension(entry.getOfficeExtension());
        updateEntry.setGender(entry.getGender());
        updateEntry.setUkuranJaket(entry.getUkuranJaket());
//        updateEntry.setUserType(entry.getUserType());
        updateEntry.setEmployeeStatus(entry.getEmployeeStatus());
        updateEntry.setGolonganKerjaId(entry.getGolonganKerjaId());
        updateEntry.setMasaKerjaId(entry.getMasaKerjaId());
        updateEntry.setCompanyId(entry.getCompanyId());
        updateEntry.setCreateBy(entry.getCreateBy());
        updateEntry.setModifedBy(entry.getModifedBy());
        updateEntry.setRowStatus(entry.getRowStatus());

        employeLocalServiceUtil.updateemploye(updateEntry);
        
        return updateEntry;
    }
}
