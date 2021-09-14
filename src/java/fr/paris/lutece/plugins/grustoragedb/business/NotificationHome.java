package fr.paris.lutece.plugins.grustoragedb.business;

import fr.paris.lutece.plugins.grubusiness.business.notification.INotificationDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Project objects
 */
public final class NotificationHome
{
    // Static variable pointed at the DAO instance
    private static INotificationDAO _dao = SpringContextService.getBean( "grustoragedb.NotificationDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "grustoragedb" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private NotificationHome(  )
    {
    }

    /**
     * Find the demand's notifications
     * 
     * @param strDemandId
     * @param strDemandTypeId
     * @return the notification list
     */
    public static List<Notification> findByDemand( String strDemandId, String strDemandTypeId )
    {
	return _dao.loadByDemand(strDemandId, strDemandTypeId);
    }
    
    
    /**
     * Find the notification by demand id's and date
     * 
     * @param strDemandId
     * @param strDemandTypeId
     * @param lDate
     * @return the notification list
     */
    public static List<Notification> findByNotification( String strDemandId, String strDemandTypeId, long lDate )
    {
	return _dao.loadByDemandAndDate(strDemandId, strDemandTypeId, lDate);
    }

    /**
     * Finds a notification  with the specified id 
     * 
     * @param strId
     * @return the notification
     */
    public static Notification findById( String strId )
    {
        return _dao.loadById(strId);
    }

    /**
     * search notifications by filter
     * 
     * @param filter
     * @return the notification list 
     */
    public static List<Notification> findByFilter( NotificationFilter filter )
    {
        return _dao.loadByFilter( filter );
    }

    /**
     * Find the notifications according to the filter
     * 
     * @param notificationFilter
     * @return the notification list
     */
    public static List<Notification> getAll(NotificationFilter notificationFilter )
    {
        return _dao.loadByFilter( notificationFilter );
    }

    /**
     * Get distinct demand type ids list
     * 
     * @return the  list
     */
    public static ReferenceList getDemandTypeIds( )
    {
        List<String> strList = _dao.loadDistinctDemandTypeIds( );
        
        ReferenceList refList = new ReferenceList( );
        for (String strId : strList )
        {
            refList.addItem(strId, strId);
        }
        
        return refList;
    }
}

