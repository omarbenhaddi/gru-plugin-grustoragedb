/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.grustoragedb.web;

import fr.paris.lutece.plugins.grustoragedb.business.DbCustomer;
import fr.paris.lutece.plugins.grustoragedb.business.DbCustomerHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage DbCustomer features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCustomers.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "GRUSTORAGEDB_MANAGEMENT" )
public class CustomerJspBean extends ManageGRUDataJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CUSTOMERS = "/admin/plugins/grustoragedb/manage_customers.html";
    private static final String TEMPLATE_CREATE_CUSTOMER = "/admin/plugins/grustoragedb/create_customer.html";
    private static final String TEMPLATE_MODIFY_CUSTOMER = "/admin/plugins/grustoragedb/modify_customer.html";

    // Parameters
    private static final String PARAMETER_ID_CUSTOMER = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CUSTOMERS = "grustoragedb.manage_customers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CUSTOMER = "grustoragedb.modify_customer.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CUSTOMER = "grustoragedb.create_customer.pageTitle";

    // Markers
    private static final String MARK_CUSTOMER_LIST = "customer_list";
    private static final String MARK_CUSTOMER = "customer";
    private static final String JSP_MANAGE_CUSTOMERS = "jsp/admin/plugins/grustoragedb/ManageCustomers.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CUSTOMER = "grustoragedb.message.confirmRemoveCustomer";
    private static final String PROPERTY_DEFAULT_LIST_CUSTOMER_PER_PAGE = "grustoragedb.listCustomers.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "grustoragedb.model.entity.customer.attribute.";

    // Views
    private static final String VIEW_MANAGE_CUSTOMERS = "manageCustomers";
    private static final String VIEW_CREATE_CUSTOMER = "createCustomer";
    private static final String VIEW_MODIFY_CUSTOMER = "modifyCustomer";

    // Actions
    private static final String ACTION_CREATE_CUSTOMER = "createCustomer";
    private static final String ACTION_MODIFY_CUSTOMER = "modifyCustomer";
    private static final String ACTION_REMOVE_CUSTOMER = "removeCustomer";
    private static final String ACTION_CONFIRM_REMOVE_CUSTOMER = "confirmRemoveCustomer";

    // Infos
    private static final String INFO_CUSTOMER_CREATED = "grustoragedb.info.customer.created";
    private static final String INFO_CUSTOMER_UPDATED = "grustoragedb.info.customer.updated";
    private static final String INFO_CUSTOMER_REMOVED = "grustoragedb.info.customer.removed";

    // Session variable to store working values
    private DbCustomer _customer;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CUSTOMERS, defaultView = true )
    public String getManageCustomers( HttpServletRequest request )
    {
        _customer = null;

        List<DbCustomer> listCustomers = DbCustomerHome.getCustomersList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CUSTOMER_LIST, listCustomers,
                JSP_MANAGE_CUSTOMERS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CUSTOMERS, TEMPLATE_MANAGE_CUSTOMERS, model );
    }

    /**
     * Returns the form to create a customer
     *
     * @param request The Http request
     * @return the html code of the customer form
     */
    @View( VIEW_CREATE_CUSTOMER )
    public String getCreateCustomer( HttpServletRequest request )
    {
        _customer = ( _customer != null ) ? _customer : new DbCustomer(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CUSTOMER, _customer );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CUSTOMER, TEMPLATE_CREATE_CUSTOMER, model );
    }

    /**
     * Process the data capture form of a new customer
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CUSTOMER )
    public String doCreateCustomer( HttpServletRequest request )
    {
        populate( _customer, request );

        // Check constraints
        if ( !validateBean( _customer, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CUSTOMER );
        }

        DbCustomerHome.create( _customer );
        addInfo( INFO_CUSTOMER_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CUSTOMERS );
    }

    /**
     * Manages the removal form of a customer whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CUSTOMER )
    public String getConfirmRemoveCustomer( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CUSTOMER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CUSTOMER ) );
        url.addParameter( PARAMETER_ID_CUSTOMER, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CUSTOMER,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a customer
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage customers
     */
    @Action( ACTION_REMOVE_CUSTOMER )
    public String doRemoveCustomer( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CUSTOMER ) );
        DbCustomerHome.remove( nId );
        addInfo( INFO_CUSTOMER_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CUSTOMERS );
    }

    /**
     * Returns the form to update info about a customer
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CUSTOMER )
    public String getModifyCustomer( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CUSTOMER ) );

        if ( ( _customer == null ) || ( _customer.getId(  ) != nId ) )
        {
            _customer = DbCustomerHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CUSTOMER, _customer );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CUSTOMER, TEMPLATE_MODIFY_CUSTOMER, model );
    }

    /**
     * Process the change form of a customer
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CUSTOMER )
    public String doModifyCustomer( HttpServletRequest request )
    {
        populate( _customer, request );

        // Check constraints
        if ( !validateBean( _customer, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CUSTOMER, PARAMETER_ID_CUSTOMER, _customer.getId(  ) );
        }

        DbCustomerHome.update( _customer );
        addInfo( INFO_CUSTOMER_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CUSTOMERS );
    }
}
