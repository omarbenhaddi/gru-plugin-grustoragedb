package fr.paris.lutece.plugins.grustoragedb.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.plugins.grustoragedb.business.NotificationHome;
import fr.paris.lutece.plugins.grustoragedb.service.JsonGeneration;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;

@Controller( controllerJsp = "ManageNotification.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "DEMAND_MANAGEMENT" )
public class NotificationJspBean extends AbstractManageDemandJspBean {
	
    // Templates
    private static final String TEMPLATE_MANAGE_NOTIFICATION = "/admin/plugins/grustoragedb/manage_notification.html";
    
    private static final String MARK_NOTIFICATION_LIST = "notification_list";
    private static final String MARK_DEMAND_TYPE_ID_LIST = "demand_type_id_list";    
    private static final String MARK_DEMAND_TYPE_ID = "demand_type_id";
    
    private static final String JSP_MANAGE_NOTIFICATIONS = "jsp/admin/plugins/grustoragedb/ManageNotification.jsp";
    
    
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_NOTIFICATION = "grustoragedb.manage_notification.pageTitle";

    // Views
    private static final String VIEW_MANAGE_NOTIFICATION = "manageNotification";

    //Parameters
    private static final String PARAMETER_DEMAND_ID = "demand_id";
    private static final String PARAMETER_DEMAND_TYPE_ID = "demand_type_id";
    private static final String PARAMETER_NOTIFICATION_DATE = "notification_date";
    private static final String PARAMETER_START_DATE = "start_date";
    private static final String PARAMETER_END_DATE = "end_date";
    
    // instance variables
    private ReferenceList _listDemandTypeId ;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_NOTIFICATION, defaultView = true )
    public String getManageNotification( HttpServletRequest request) 
    {
    	List<Notification> listNotification = new ArrayList<>();
        
        if ( _listDemandTypeId == null )
        {
            _listDemandTypeId = NotificationHome.getDemandTypeIds( );
        }
                
        NotificationFilter filter = new NotificationFilter( );
        long lNotificationDate = -1;
        
    	if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_DEMAND_ID ) ) )
        {
            filter.setDemandId( request.getParameter( PARAMETER_DEMAND_ID ) );
        }
        
        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_DEMAND_TYPE_ID ) ) )
        {
            filter.setDemandTypeId( request.getParameter( PARAMETER_DEMAND_TYPE_ID ) );
        }
        
        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_NOTIFICATION_DATE ) ) )
        {
            lNotificationDate = Long.parseLong( request.getParameter( PARAMETER_NOTIFICATION_DATE ) );
        }
        
        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_START_DATE ) ) )
        {
            String strStartDate = request.getParameter( PARAMETER_START_DATE );
            Timestamp tStartDate = DateUtil.formatTimestamp( strStartDate, getLocale( ) );
            if ( tStartDate != null )
            {
                filter.setStartDate( tStartDate.getTime( ) );
            }
        }
        
        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_END_DATE ) ) )
        {
            String strEndDate = request.getParameter( PARAMETER_END_DATE );
            Timestamp tEndDate = DateUtil.formatTimestamp( strEndDate, getLocale( ) );
            if ( tEndDate != null )
            {
                filter.setEndDate( tEndDate.getTime( ) );
            }
        }
        
        if ( filter.containsDemandId( ) && filter.containsDemandTypeId( ) && lNotificationDate > 0 )
        {
            listNotification = NotificationHome.findByNotification( filter.getDemandId( ), filter.getDemandTypeId( ), lNotificationDate );
        }
        else if ( filter.containsDemandId( ) || filter.containsDemandTypeId( ) || filter.containsStartDate( ) || filter.containsEndDate( ) )
        {
            listNotification = NotificationHome.findByFilter( filter );
        }
    	
        if ( !StringUtils.isEmpty( request.getParameter( "generate_json" ) ) )
        {
            JsonGeneration.generateJson( listNotification );
            //reinit
            listNotification =  new ArrayList<Notification>( );
        }
            
    	Map<String, Object> model = getPaginatedListModel(request, MARK_NOTIFICATION_LIST, listNotification, JSP_MANAGE_NOTIFICATIONS );
        
        model.put( MARK_DEMAND_TYPE_ID_LIST, _listDemandTypeId );
        model.put( MARK_DEMAND_TYPE_ID, request.getParameter( PARAMETER_DEMAND_TYPE_ID ) );
        
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_NOTIFICATION, TEMPLATE_MANAGE_NOTIFICATION, model );
    }
}