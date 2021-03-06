/*Copyright (c) 2016-2017 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/

package com.hrdb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.hrdb.Employee;
import com.hrdb.Vacation;
import com.hrdb.service.EmployeeService;


/**
 * Controller object for domain model class Employee.
 * @see Employee
 */
@RestController("hrdb.EmployeeController")
@Api(value = "/hrdb/Employee", description = "Exposes APIs to work with Employee resource.")
@RequestMapping("/hrdb/Employee")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
	@Qualifier("hrdb.EmployeeService")
	private EmployeeService employeeService;

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = "Creates a new Employee instance.")
	public Employee createEmployee(@RequestBody Employee employee) {
		LOGGER.debug("Create Employee with information: {}" , employee);

		employee = employeeService.create(employee);
		LOGGER.debug("Created Employee with information: {}" , employee);

	    return employee;
	}


    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the Employee instance associated with the given id.")
    public Employee getEmployee(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Getting Employee with id: {}" , id);

        Employee foundEmployee = employeeService.getById(id);
        LOGGER.debug("Employee details with id: {}" , foundEmployee);

        return foundEmployee;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates the Employee instance associated with the given id.")
    public Employee editEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) throws EntityNotFoundException {
        LOGGER.debug("Editing Employee with id: {}" , employee.getEmpId());

        employee.setEmpId(id);
        employee = employeeService.update(employee);
        LOGGER.debug("Employee details with id: {}" , employee);

        return employee;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes the Employee instance associated with the given id.")
    public boolean deleteEmployee(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Deleting Employee with id: {}" , id);

        Employee deletedEmployee = employeeService.delete(id);

        return deletedEmployee != null;
    }

    /**
     * @deprecated Use {@link #findEmployees(String, Pageable)} instead.
     */
    @Deprecated
    @RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Returns the list of Employee instances matching the search criteria.")
	public Page<Employee> findEmployees( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
		LOGGER.debug("Rendering Employees list");
		return employeeService.findAll(queryFilters, pageable);
	}

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Returns the list of Employee instances matching the search criteria.")
    public Page<Employee> findEmployees(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Employees list");
    	return employeeService.findAll(query, pageable);
    }

    @RequestMapping(value = "/export/{exportType}", method = RequestMethod.GET, produces = "application/octet-stream")
    @ApiOperation(value = "Returns downloadable file for the data.")
    public Downloadable exportEmployees(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return employeeService.export(exportType, query, pageable);
    }

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ApiOperation(value = "Returns the total count of Employee instances.")
	public Long countEmployees( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting Employees");
		return employeeService.count(query);
	}

    @RequestMapping(value="/{id:.+}/employeesForManagerId", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the employeesForManagerId instance associated with the given id.")
    public Page<Employee> findAssociatedEmployeesForManagerId(@PathVariable("id") Integer id, Pageable pageable) {

        LOGGER.debug("Fetching all associated employeesForManagerId");
        return employeeService.findAssociatedEmployeesForManagerId(id, pageable);
    }

    @RequestMapping(value="/{id:.+}/vacations", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the vacations instance associated with the given id.")
    public Page<Vacation> findAssociatedVacations(@PathVariable("id") Integer id, Pageable pageable) {

        LOGGER.debug("Fetching all associated vacations");
        return employeeService.findAssociatedVacations(id, pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service EmployeeService instance
	 */
	protected void setEmployeeService(EmployeeService service) {
		this.employeeService = service;
	}

}

