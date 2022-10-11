package fr.paris.lutece.plugins.grustoragedb.web;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationEvent;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.plugins.grustoragedb.business.NotificationEventHome;
import fr.paris.lutece.plugins.grustoragedb.business.NotificationHome;
import fr.paris.lutece.plugins.grustoragedb.service.DemandTypeService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.sql.Timestamp;
import org.apache.commons.lang3.StringUtils;

@Controller( controllerJsp = "ManageNotificationEvent.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "DEMAND_MANAGEMENT" )
public class NotificationEventJspBean extends AbstractManageDemandJspBean<Integer, NotificationEvent> {
	
    // Templates
    private static final String TEMPLATE_MANAGE_EVENT = "/admin/plugins/grustoragedb/manage_notification_event.html";
    
    private static final String MARK_EVENT_LIST = "notification_event_list";
    private static final String MARK_DEMAND_TYPE_ID_LIST = "demand_type_id_list";    
    private static final String MARK_DEMAND_ID = "demand_id";
    private static final String MARK_DEMAND_TYPE_ID = "demand_type_id";
    private static final String MARK_START_DATE = "start_date";
    private static final String MARK_END_DATE = "end_date";
    
    private static final String JSP_MANAGE_EVENTS = "jsp/admin/plugins/grustoragedb/ManageNotificationEvent.jsp";
    
    
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_EVENT = "grustoragedb.manage_event.pageTitle";

    // Views
    private static final String VIEW_MANAGE_EVENT = "manageNotification";

    //Parameters
    private static final String PARAMETER_DEMAND_ID = "demand_id";
    private static final String PARAMETER_DEMAND_TYPE_ID = "demand_type_id";
    private static final String PARAMETER_NOTIFICATION_DATE = "notification_date";
    private static final String PARAMETER_START_DATE = "start_date";
    private static final String PARAMETER_END_DATE = "end_date";   
    private static final String PARAMETER_STATUS = "event_status";   
    
    // instance variables
    private ReferenceList _listDemandTypeId ;
    private List<Integer>  _listNotificationEventId = new ArrayList<>( );
    private NotificationFilter _currentFilter;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_EVENT, defaultView = true )
    public String getManageNotificationEvent( HttpServletRequest request) 
    {
    	long lNotificationDate = -1;
        
        if ( _listDemandTypeId == null )
        {
        	_listDemandTypeId = DemandTypeService.instance( ).getDemandTypesReferenceList( );
        }
        
        // initial call (no pagination)
    	if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) == null || _listNotificationEventId.isEmpty( ))
    	{
    		// new search...
	    	_currentFilter = new NotificationFilter( );
        
	    	if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_DEMAND_ID ) ) )
	        {
	    		_currentFilter.setDemandId( request.getParameter( PARAMETER_DEMAND_ID ) );
	        }
	        
	        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_DEMAND_TYPE_ID ) ) )
	        {
	        	_currentFilter.setDemandTypeId( request.getParameter( PARAMETER_DEMAND_TYPE_ID ) );
	        }
	        
	        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_NOTIFICATION_DATE ) ) )
	        {
	            lNotificationDate = Long.parseLong( request.getParameter( PARAMETER_NOTIFICATION_DATE ) );
	        }
	        
	        
	        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_START_DATE ) ) )
	        {
	            String strStartDate = request.getParameter( PARAMETER_START_DATE );
	            Date dStartDate = DateUtil.parseIsoDate( strStartDate );
	            if ( dStartDate != null )
	            {
	            	_currentFilter.setStartDate( dStartDate.getTime( ) );
	            }
	        }
	        
	        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_END_DATE ) ) )
	        {
	            String strEndDate = request.getParameter( PARAMETER_END_DATE );
	            Date dEndDate = DateUtil.parseIsoDate( strEndDate );            
	            if ( dEndDate != null )
	            {
	            	_currentFilter.setEndDate( dEndDate.getTime( ) );
	            }
	        }
	        
	        if ( !StringUtils.isEmpty( request.getParameter( PARAMETER_STATUS ) ) )
	        {
	        	_currentFilter.setEventStatus( request.getParameter( PARAMETER_STATUS ) );
	        }
	        
	        
	        // search
	        if ( _currentFilter.containsDemandId( ) && _currentFilter.containsDemandTypeId( ) && lNotificationDate > 0 )
	        {
	        	List<NotificationEvent> listNotificationEvent =  NotificationEventHome.findByNotification( _currentFilter.getDemandId( ), _currentFilter.getDemandTypeId( ), lNotificationDate );
	            _listNotificationEventId = new ArrayList<>();
	            if ( listNotificationEvent != null )
	            {
	            	for ( NotificationEvent event : listNotificationEvent )
	            	{
	            		_listNotificationEventId.add( event.getId( ) );
	            	}
	            }
	        }
	        else if ( _currentFilter.containsDemandId( ) || _currentFilter.containsDemandTypeId( ) || _currentFilter.containsStartDate( ) || _currentFilter.containsEndDate( ) )
	        {
	            _listNotificationEventId =  NotificationEventHome.findIdsByFilter( _currentFilter );
	        }
    	}
    	
        
    	Map<String, Object> model = getPaginatedListModel(request, MARK_EVENT_LIST, _listNotificationEventId, JSP_MANAGE_EVENTS );
        
        model.put( MARK_DEMAND_TYPE_ID_LIST, _listDemandTypeId );
        if ( !StringUtils.isEmpty( _currentFilter.getDemandId( ) ) )
        {
        	model.put( MARK_DEMAND_ID, _currentFilter.getDemandId( ) );
        }
        if ( !StringUtils.isBlank( _currentFilter.getDemandTypeId( ) ) )
        {
        	model.put( MARK_DEMAND_TYPE_ID, _currentFilter.getDemandTypeId( ) );
        }
        if ( _currentFilter.getStartDate( ) > 0 ) 
        {
        	model.put( MARK_START_DATE, new Date( _currentFilter.getStartDate( ) ) );
        }
        if ( _currentFilter.getEndDate( ) > 0 ) 
        {
        	model.put( MARK_END_DATE, new Date( _currentFilter.getEndDate( ) ) );
        }
        
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_EVENT, TEMPLATE_MANAGE_EVENT, model );
    }

	@Override
	List<NotificationEvent> getItemsFromIds(List<Integer> listIds) {
		List<NotificationEvent> listNotificationEvent = NotificationEventHome.getByIds( listIds );
		
		// keep original order
		return listNotificationEvent.stream( )
         	    .sorted( Comparator.comparingInt( notif -> listIds.indexOf( notif.getId( ) ) ) )
         	    .collect( Collectors.toList( ) );
	}
}