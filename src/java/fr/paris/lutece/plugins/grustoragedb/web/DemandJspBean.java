package fr.paris.lutece.plugins.grustoragedb.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.plugins.grustoragedb.business.DemandHome;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.date.DateUtil;
import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;

@Controller( controllerJsp = "ManageDemand.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/", right = "DEMAND_MANAGEMENT" )
public class DemandJspBean extends AbstractManageDemandJspBean {
	
	// Templates
    private static final String TEMPLATE_MANAGE_DEMAND = "/admin/plugins/grustoragedb/manage_demand.html";
    
    private static final String MARK_DEMAND_LIST = "demand_list";
    
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
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEMAND, defaultView = true )
    public String getManageDemand( HttpServletRequest request) 
    {
    	List<Demand> listDemand = new ArrayList<>();
        
        NotificationFilter filter = new NotificationFilter( );
        
    	if ( !StringUtils.isEmpty(request.getParameter( PARAMETER_DEMAND_ID ) ) )
        {
            filter.setDemandId( request.getParameter( PARAMETER_DEMAND_ID ) );
        }
        
        if ( !StringUtils.isEmpty(request.getParameter( PARAMETER_DEMAND_TYPE_ID ) ) )
        {
            filter.setDemandTypeId( request.getParameter( PARAMETER_DEMAND_TYPE_ID ) );
        }
        
        if ( !StringUtils.isEmpty(request.getParameter( PARAMETER_START_DATE ) ) )
        {
            String strStartDate = request.getParameter( PARAMETER_START_DATE );
            Timestamp tStartDate = DateUtil.formatTimestamp( strStartDate, getLocale( ) );
            if ( tStartDate != null )
            {
                filter.setStartDate( tStartDate.getTime( ) );
            }
        }
        
        if ( !StringUtils.isEmpty(request.getParameter( PARAMETER_END_DATE ) ) )
        {
            String strEndDate = request.getParameter( PARAMETER_END_DATE );
            Timestamp tEndDate = DateUtil.formatTimestamp( strEndDate, getLocale( ) );
            if ( tEndDate != null )
            {
                filter.setEndDate( tEndDate.getTime( ) );
            }
        }
        
        if ( filter.containsDemandId( ) || filter.containsDemandTypeId( ) || filter.containsStartDate( ) || filter.containsEndDate( ) )
        {
            listDemand = (List<Demand>) DemandHome.searchByFilter( filter );
        }
    	
    	Map<String, Object> model = getPaginatedListModel(request, MARK_DEMAND_LIST, listDemand, JSP_MANAGE_DEMANDS );
        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEMAND, TEMPLATE_MANAGE_DEMAND, model );
    }
}