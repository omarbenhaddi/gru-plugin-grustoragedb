package fr.paris.lutece.plugins.grustoragedb.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.plugins.grustoragedb.business.DemandHome;
import fr.paris.lutece.plugins.grustoragedb.business.NotificationHome;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;

@Controller( controllerJsp = "ManageDemand.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "DEMAND_MANAGEMENT" )
public class DemandJspBean extends AbstractManageDemandJspBean {
	
    // Templates
    private static final String TEMPLATE_MANAGE_DEMAND = "/admin/plugins/grustoragedb/manage_demand.html";
    
    // Markers
    private static final String MARK_DEMAND_LIST = "demand_list";
    private static final String MARK_DEMAND_TYPE_ID_LIST = "demand_type_id_list";    
    private static final String MARK_DEMAND_ID = "demand_id";
    private static final String MARK_DEMAND_TYPE_ID = "demand_type_id";
    private static final String MARK_START_DATE = "start_date";
    private static final String MARK_END_DATE = "end_date";
    
    // JSP
    private static final String JSP_MANAGE_DEMANDS = "jsp/admin/plugins/grustoragedb/ManageDemand.jsp";
        
    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DEMAND = "grustoragedb.manage_demand.pageTitle";

    // Views
    private static final String VIEW_MANAGE_DEMAND = "manageDemand";

    //Parameters
    private static final String PARAMETER_DEMAND_ID = "demand_id";
    private static final String PARAMETER_DEMAND_TYPE_ID = "demand_type_id";
    private static final String PARAMETER_START_DATE = "start_date";
    private static final String PARAMETER_END_DATE = "end_date";
    
    // instance variables
    private ReferenceList _listDemandTypeId ;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEMAND, defaultView = true )
    public String getManageDemand( HttpServletRequest request) 
    {
    	List<Demand> listDemand = new ArrayList<>( );
        
        if ( _listDemandTypeId == null )
        {
            _listDemandTypeId = NotificationHome.getDemandTypeIds( );
        }
        
        NotificationFilter filter = new NotificationFilter( );
        
    	if ( !StringUtils.isEmpty(request.getParameter( PARAMETER_DEMAND_ID ) ) )
        {
            filter.setDemandId( request.getParameter( PARAMETER_DEMAND_ID ) );
        }
        
        if ( !StringUtils.isEmpty(request.getParameter( PARAMETER_DEMAND_TYPE_ID ) ) )
        {
            filter.setDemandTypeId( request.getParameter( PARAMETER_DEMAND_TYPE_ID ) );
        }
        
        String strStartDate = request.getParameter( PARAMETER_START_DATE );
        if ( !StringUtils.isEmpty( strStartDate ) )
        {
            Timestamp tStartDate = DateUtil.formatTimestamp( strStartDate, getLocale( ) );
            if ( tStartDate != null )
            {
                filter.setStartDate( tStartDate.getTime( ) );
            }
        }
        
        String strEndDate = request.getParameter( PARAMETER_END_DATE );
        if ( !StringUtils.isEmpty( strEndDate ) )
        {
            Timestamp tEndDate = DateUtil.formatTimestamp( strEndDate, getLocale( ) );
            if ( tEndDate != null )
            {
                filter.setEndDate( tEndDate.getTime( ) );
            }
        }
        
        if ( filter.containsDemandId( ) || filter.containsDemandTypeId( ) || filter.containsStartDate( ) || filter.containsEndDate( ) )
        {
            // search demands
            listDemand = (List<Demand>) DemandHome.searchByFilter( filter );
        }
    	
    	Map<String, Object> model = getPaginatedListModel(request, MARK_DEMAND_LIST, listDemand, JSP_MANAGE_DEMANDS );
        
        model.put( MARK_DEMAND_TYPE_ID_LIST, _listDemandTypeId );
        model.put( MARK_DEMAND_ID, request.getParameter( PARAMETER_DEMAND_TYPE_ID ) );
        model.put( MARK_DEMAND_TYPE_ID, request.getParameter( PARAMETER_DEMAND_ID ) );
        model.put( MARK_START_DATE, request.getParameter( PARAMETER_START_DATE ) );
        model.put( MARK_END_DATE, request.getParameter( PARAMETER_END_DATE ) );
        
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEMAND, TEMPLATE_MANAGE_DEMAND, model );
    }
}