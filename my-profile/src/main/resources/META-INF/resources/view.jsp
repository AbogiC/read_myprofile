<%@ include file="init.jsp" %>

<%
employe employee = (employe) request.getAttribute("employee");
List<company> companies = (List<company>) request.getAttribute("companies");

%>

<style>
.my-profile-form .birthday.lfr-input-time {
	display: none;
}
</style>

<portlet:actionURL var="updateEmployeeURL" />

<div class="container-fluid-1280">
<aui:form cssClass="my-profile-form" action="<%= updateEmployeeURL %>">
	<aui:model-context bean="<%= employee %>" model="<%= employe.class %>" />	
	<aui:input name="employeeId" type="hidden" value="<%= employee.getId() %>" />
	
	<c:if test="<%= employee.getId() == 0 %>">
		<div class="alert alert-danger">
			<liferay-ui:message key="employee-data-not-exists" />
		</div>	
	</c:if>
		
	<liferay-ui:success key="employee-saved-successfully" message="employee-saved-successfully" />
	
	<liferay-ui:error key="employee-employeename-required" message="employee-employeename-required" />
	<liferay-ui:error key="employee-nik-required" message="employee-nik-required" />
	<liferay-ui:error key="employee-nik-is-invalid" message="employee-nik-is-invalid" />
	<liferay-ui:error key="employee-npk-required" message="employee-npk-required" />
	<liferay-ui:error key="employee-officeemail-required" message="employee-officeemail-required" />
	<liferay-ui:error key="employee-officeemail-is-invalid" message="employee-officeemail-is-invalid" />
	<liferay-ui:error key="employee-officeemail-domain-is-invalid" message="employee-officeemail-domain-is-invalid" />
	<liferay-ui:error key="employee-personalemail-required" message="employee-personalemail-required" />
	<liferay-ui:error key="employee-birthday-required" message="employee-birthday-required" />
	<liferay-ui:error key="employee-mobilephone1-required" message="employee-mobilephone1-required" />
	<liferay-ui:error key="employee-gender-required" message="employee-gender-required" />
	<liferay-ui:error key="employee-company-required" message="employee-company-required" />
	<liferay-ui:error key="permission-error" message="permission-error" />
	<liferay-ui:error exception="<%= Exception.class %>" message="errors-please-contact-administrator" />
	
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>employeeName">
				<liferay-ui:message key="employee-name" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="name" label="">
				<aui:validator name="required" />
			</aui:input>
		</aui:col>
	</aui:row>
	
	<liferay-ui:error key="employee-nik-required" message="employee-nik-required" />	
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>nik">
				<liferay-ui:message key="nik" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="nik" label="">
				<aui:validator name="required"/>
			</aui:input>
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-npk-required" message="employee-npk-required" />	
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>npk">
				<liferay-ui:message key="npk" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="npk" label="">
				<aui:validator name="required" />
			</aui:input>
		</aui:col>
	</aui:row>
		
	<liferay-ui:error key="employee-amdicompanyid-required" message="employee-amdicompanyid-required" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>company">
				<liferay-ui:message key="company" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">	
			<aui:select name="company" label="" value="<%= employee.getCompanyId() %>">
				<% for(company c : companies) { %>
					<aui:option value="<%= c.getId() %>"><%= c.getName() %></aui:option>
				<% } %>
			</aui:select>
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>companyCode">
				<liferay-ui:message key="company-code" />
			</label>
		</aui:col>
		<aui:col width="50">	
			<aui:input name="companyCode" type="text" disabled="true" label="" />
		</aui:col>
	</aui:row>
	
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>companyGroup">
				<liferay-ui:message key="company-group" />
			</label>
		</aui:col>
		<aui:col width="50">		
			<aui:input name="companyGroup" type="text" disabled="true" label="" />
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-officeemail-required" message="employee-officeemail-required" />
	<liferay-ui:error key="office-email-already-exist" message="office-email-already-exist" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>officeEmail">
				<liferay-ui:message key="office-email" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="emailOfficeInput" label="" type="hidden" value="<%= employee.getEmailOffice() %>" />
			<aui:input name="emailOffice" label="" disabled="true">
				<aui:validator name="required" />
			</aui:input>
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-personalemail-required" message="employee-personalemail-required" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>personalEmail">
				<liferay-ui:message key="personal-email" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="emailPersonal" label="">
				<aui:validator name="email" />
			</aui:input>
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-position-required" message="employee-position-required" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>position">
				<liferay-ui:message key="position" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="position" label="" />
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-dateofbirth-required" message="employee-dateofbirth-required" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>dateOfBirth">
				<liferay-ui:message key="date-of-birth" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input cssClass="birthday" name="birth" label="">
				<aui:validator name="required" />
			</aui:input>
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-mobilephone1-required" message="employee-mobilephone1-required" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>mobilePhone1">
				<liferay-ui:message key="mobile-phone1" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="phone1" label="">
				<aui:validator name="required" />
			</aui:input>
		</aui:col>	
	</aui:row>
	
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>mobilePhone2">
				<liferay-ui:message key="mobile-phone2" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="phone2" label="" />
		</aui:col>
	</aui:row>
			
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>officeExtension">
				<liferay-ui:message key="office-extension" />
			</label>
		</aui:col>
		<aui:col width="50">
			<aui:input name="officeExtension" label=""/>
		</aui:col>
	</aui:row>		
	
	<liferay-ui:error key="employee-gender-required" message="employee-gender-required" />
	<aui:row>
		<aui:col width="40" cssClass="form-group">
			<label class="form-control" for="<portlet:namespace/>gender">
				<liferay-ui:message key="gender" />
				<liferay-ui:icon icon="asterisk" />
			</label>
		</aui:col>
		<aui:col width="50">	
			<aui:select name="gender" label="">
				<aui:option value="<%= MyProfileConstants.GENDER_MALE %>">
					<liferay-ui:message key="male" />
				</aui:option>
				<aui:option value="<%= MyProfileConstants.GENDER_FEMALE %>">
					<liferay-ui:message key="female" />	
				</aui:option>		
			</aui:select>
		</aui:col>
	</aui:row>		
	
	<aui:button-row cssClass="text-center" >
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>
</div>

<script>
	var <portlet:namespace />company = new Object();
	<% for(company c : companies) { %>
	<portlet:namespace />company[<%= c.getId() %>] = {
			"code" : "<%= c.getCode() %>",
			"group" : "<%= c.getNameGroup() %>"
	};	
	<% } %>
	
	function <portlet:namespace />setCompany(elemen) {
		$('#<portlet:namespace />companyCode').val(<portlet:namespace />company[elemen.val()].code);
		$('#<portlet:namespace />companyGroup').val(<portlet:namespace />company[elemen.val()].group);
	}
	
	$(document).ready(function() {
		<portlet:namespace />setCompany($('#<portlet:namespace />company'));
		$('#<portlet:namespace />company').on('change', function() {
			<portlet:namespace />setCompany($(this));
		});
	});
</script>