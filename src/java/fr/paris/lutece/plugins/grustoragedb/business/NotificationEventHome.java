package fr.paris.lutece.plugins.grustoragedb.business;

import fr.paris.lutece.plugins.grubusiness.business.notification.INotificationEventDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationEvent;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Notification Event object
 */
public final class NotificationEventHome
{

    private static INotificationEventDAO _dao = SpringContextService.getBean( "grustoragedb.NotificationEventDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "grustoragedb" );

    /**
     * Private constructor 
     */
    private NotificationEventHome(  )
    {
    }
    
    /**
     * Find the demand's NotificationEvents
     * 
     * @param strDemandId
     * @param strDemandTypeId
     * @return the NotificationEvent list
     */
    public static List<NotificationEvent> findByDemand( String strDemandId, String strDemandTypeId )
    {
	return _dao.loadByDemand(strDemandId, strDemandTypeId);
    }

    /**
     * Find the demand's NotificationEvents
     * 
     * @param strDemandId
     * @param strDemandTypeId
     * @return the NotificationEvent list
     */
    public static List<NotificationEvent> findByNotification( String strDemandId, String strDemandTypeId, long lNotificationDate )
    {
	return _dao.loadByNotification( strDemandId, strDemandTypeId, lNotificationDate );
    }

    /**
     * Finds a NotificationEvent  with the specified id 
     * 
     * @param strId
     * @return the NotificationEvent
     */
    public static NotificationEvent findById( int nId )
    {
        return _dao.loadById( nId );
    }
    
    /**
     * Find the NotificationEvents according to the filter
     * 
     * @param notificationFilter
     * @return the NotificationEvent list
     */
    public static List<NotificationEvent> findByFilter(NotificationFilter notificationFilter )
    {
        return _dao.loadByFilter( notificationFilter );
    }
}

