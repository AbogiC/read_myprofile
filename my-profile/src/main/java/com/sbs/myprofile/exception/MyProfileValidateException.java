/**
*  Copyright (C) 2017 Yasuyuki Takeo All rights reserved.
*
*  This program is free software: you can redistribute it and/or modify
*  it under the terms of the GNU Lesser General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*  GNU Lesser General Public License for more details.
*/

package com.sbs.myprofile.exception;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

/**
* @author Yasuyuki Takeo
* @author Softbless
*/
public class MyProfileValidateException extends PortalException {

    protected List<String> _errors = null;

    public MyProfileValidateException() {}

    public MyProfileValidateException(List<String> errors) {
        _errors = errors;
    }

    public List<String> getErrors() {
        return _errors;
    }
}