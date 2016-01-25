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

import fr.paris.lutece.plugins.grustoragedb.business.DbNotification;
import fr.paris.lutece.plugins.grustoragedb.business.DbNotificationHome;
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
 * This class provides the user interface to manage DbNotification features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageNotifications.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "GRUSTORAGEDB_MANAGEMENT" )
public class NotificationJspBean extends ManageGRUDataJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_NOTIFICATIONS = "/admin/plugins/grustoragedb/manage_notifications.html";
    private static final String TEMPLATE_CREATE_NOTIFICATION = "/admin/plugins/grustoragedb/create_notification.html";
    private static final String TEMPLATE_MODIFY_NOTIFICATION = "/admin/plugins/grustoragedb/modify_notification.html";

    // Parameters
    private static final String PARAMETER_ID_NOTIFICATION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_NOTIFICATIONS = "grustoragedb.manage_notifications.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_NOTIFICATION = "grustoragedb.modify_notification.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_NOTIFICATION = "grustoragedb.create_notification.pageTitle";

    // Markers
    private static final String MARK_NOTIFICATION_LIST = "notification_list";
    private static final String MARK_NOTIFICATION = "notification";
    private static final String JSP_MANAGE_NOTIFICATIONS = "jsp/admin/plugins/grustoragedb/ManageNotifications.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_NOTIFICATION = "grustoragedb.message.confirmRemoveNotification";
    private static final String PROPERTY_DEFAULT_LIST_NOTIFICATION_PER_PAGE = "grustoragedb.listNotifications.itemsPerPage";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "grustoragedb.model.entity.notification.attribute.";

    // Views
    private static final String VIEW_MANAGE_NOTIFICATIONS = "manageNotifications";
    private static final String VIEW_CREATE_NOTIFICATION = "createNotification";
    private static final String VIEW_MODIFY_NOTIFICATION = "modifyNotification";

    // Actions
    private static final String ACTION_CREATE_NOTIFICATION = "createNotification";
    private static final String ACTION_MODIFY_NOTIFICATION = "modifyNotification";
    private static final String ACTION_REMOVE_NOTIFICATION = "removeNotification";
    private static final String ACTION_CONFIRM_REMOVE_NOTIFICATION = "confirmRemoveNotification";

    // Infos
    private static final String INFO_NOTIFICATION_CREATED = "grustoragedb.info.notification.created";
    private static final String INFO_NOTIFICATION_UPDATED = "grustoragedb.info.notification.updated";
    private static final String INFO_NOTIFICATION_REMOVED = "grustoragedb.info.notification.removed";

    // Session variable to store working values
    private DbNotification _notification;

    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_NOTIFICATIONS, defaultView = true )
    public String getManageNotifications( HttpServletRequest request )
    {
        _notification = null;

        List<DbNotification> listNotifications = DbNotificationHome.getNotificationsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_NOTIFICATION_LIST, listNotifications,
                JSP_MANAGE_NOTIFICATIONS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_NOTIFICATIONS, TEMPLATE_MANAGE_NOTIFICATIONS, model );
    }

    /**
     * Returns the form to create a notification
     *
     * @param request The Http request
     * @return the html code of the notification form
     */
    @View( VIEW_CREATE_NOTIFICATION )
    public String getCreateNotification( HttpServletRequest request )
    {
        _notification = ( _notification != null ) ? _notification : new DbNotification(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_NOTIFICATION, _notification );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_NOTIFICATION, TEMPLATE_CREATE_NOTIFICATION, model );
    }

    /**
     * Process the data capture form of a new notification
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_NOTIFICATION )
    public String doCreateNotification( HttpServletRequest request )
    {
        populate( _notification, request );

        // Check constraints
        if ( !validateBean( _notification, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_NOTIFICATION );
        }

        DbNotificationHome.create( _notification );
        addInfo( INFO_NOTIFICATION_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_NOTIFICATIONS );
    }

    /**
     * Manages the removal form of a notification whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_NOTIFICATION )
    public String getConfirmRemoveNotification( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_NOTIFICATION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_NOTIFICATION ) );
        url.addParameter( PARAMETER_ID_NOTIFICATION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_NOTIFICATION,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a notification
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage notifications
     */
    @Action( ACTION_REMOVE_NOTIFICATION )
    public String doRemoveNotification( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_NOTIFICATION ) );
        DbNotificationHome.remove( nId );
        addInfo( INFO_NOTIFICATION_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_NOTIFICATIONS );
    }

    /**
     * Returns the form to update info about a notification
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_NOTIFICATION )
    public String getModifyNotification( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_NOTIFICATION ) );

        if ( ( _notification == null ) || ( _notification.getId(  ) != nId ) )
        {
            _notification = DbNotificationHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_NOTIFICATION, _notification );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_NOTIFICATION, TEMPLATE_MODIFY_NOTIFICATION, model );
    }

    /**
     * Process the change form of a notification
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_NOTIFICATION )
    public String doModifyNotification( HttpServletRequest request )
    {
        populate( _notification, request );

        // Check constraints
        if ( !validateBean( _notification, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_NOTIFICATION, PARAMETER_ID_NOTIFICATION, _notification.getId(  ) );
        }

        DbNotificationHome.update( _notification );
        addInfo( INFO_NOTIFICATION_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_NOTIFICATIONS );
    }
}
