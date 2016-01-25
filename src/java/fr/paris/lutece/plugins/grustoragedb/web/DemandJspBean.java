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

import fr.paris.lutece.plugins.grustoragedb.business.DbDemand;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemandHome;
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
 * This class provides the user interface to manage DbDemand features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDemands.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "GRUSTORAGEDB_MANAGEMENT" )
public class DemandJspBean extends ManageGRUDataJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_DEMANDS = "/admin/plugins/grustoragedb/manage_demands.html";
    private static final String TEMPLATE_CREATE_DEMAND = "/admin/plugins/grustoragedb/create_demand.html";
    private static final String TEMPLATE_MODIFY_DEMAND = "/admin/plugins/grustoragedb/modify_demand.html";

    // Parameters
    private static final String PARAMETER_ID_DEMAND = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DEMANDS = "grustoragedb.manage_demands.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DEMAND = "grustoragedb.modify_demand.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DEMAND = "grustoragedb.create_demand.pageTitle";

    // Markers
    private static final String MARK_DEMAND_LIST = "demand_list";
    private static final String MARK_DEMAND = "demand";
    private static final String JSP_MANAGE_DEMANDS = "jsp/admin/plugins/grustoragedb/ManageDemands.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DEMAND = "grustoragedb.message.confirmRemoveDemand";
    private static final String PROPERTY_DEFAULT_LIST_DEMAND_PER_PAGE = "grustoragedb.listDemands.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "grustoragedb.model.entity.demand.attribute.";

    // Views
    private static final String VIEW_MANAGE_DEMANDS = "manageDemands";
    private static final String VIEW_CREATE_DEMAND = "createDemand";
    private static final String VIEW_MODIFY_DEMAND = "modifyDemand";

    // Actions
    private static final String ACTION_CREATE_DEMAND = "createDemand";
    private static final String ACTION_MODIFY_DEMAND = "modifyDemand";
    private static final String ACTION_REMOVE_DEMAND = "removeDemand";
    private static final String ACTION_CONFIRM_REMOVE_DEMAND = "confirmRemoveDemand";

    // Infos
    private static final String INFO_DEMAND_CREATED = "grustoragedb.info.demand.created";
    private static final String INFO_DEMAND_UPDATED = "grustoragedb.info.demand.updated";
    private static final String INFO_DEMAND_REMOVED = "grustoragedb.info.demand.removed";

    // Session variable to store working values
    private DbDemand _demand;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEMANDS, defaultView = true )
    public String getManageDemands( HttpServletRequest request )
    {
        _demand = null;

        List<DbDemand> listDemands = DbDemandHome.getDemandsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DEMAND_LIST, listDemands, JSP_MANAGE_DEMANDS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEMANDS, TEMPLATE_MANAGE_DEMANDS, model );
    }

    /**
     * Returns the form to create a demand
     *
     * @param request The Http request
     * @return the html code of the demand form
     */
    @View( VIEW_CREATE_DEMAND )
    public String getCreateDemand( HttpServletRequest request )
    {
        _demand = ( _demand != null ) ? _demand : new DbDemand(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DEMAND, _demand );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DEMAND, TEMPLATE_CREATE_DEMAND, model );
    }

    /**
     * Process the data capture form of a new demand
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DEMAND )
    public String doCreateDemand( HttpServletRequest request )
    {
        populate( _demand, request );

        // Check constraints
        if ( !validateBean( _demand, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DEMAND );
        }

        DbDemandHome.create( _demand );
        addInfo( INFO_DEMAND_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DEMANDS );
    }

    /**
     * Manages the removal form of a demand whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DEMAND )
    public String getConfirmRemoveDemand( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEMAND ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DEMAND ) );
        url.addParameter( PARAMETER_ID_DEMAND, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DEMAND,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a demand
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage demands
     */
    @Action( ACTION_REMOVE_DEMAND )
    public String doRemoveDemand( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEMAND ) );
        DbDemandHome.remove( nId );
        addInfo( INFO_DEMAND_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DEMANDS );
    }

    /**
     * Returns the form to update info about a demand
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DEMAND )
    public String getModifyDemand( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEMAND ) );

        if ( ( _demand == null ) || ( _demand.getId(  ) != nId ) )
        {
            _demand = DbDemandHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_DEMAND, _demand );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DEMAND, TEMPLATE_MODIFY_DEMAND, model );
    }

    /**
     * Process the change form of a demand
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DEMAND )
    public String doModifyDemand( HttpServletRequest request )
    {
        populate( _demand, request );

        // Check constraints
        if ( !validateBean( _demand, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DEMAND, PARAMETER_ID_DEMAND, _demand.getId(  ) );
        }

        DbDemandHome.update( _demand );
        addInfo( INFO_DEMAND_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DEMANDS );
    }
}
