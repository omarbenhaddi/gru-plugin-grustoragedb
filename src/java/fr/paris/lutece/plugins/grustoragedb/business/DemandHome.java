package fr.paris.lutece.plugins.grustoragedb.business;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Project objects
 */
public final class DemandHome
{
    // Static variable pointed at the DAO instance
    private static IDemandDAO _dao = SpringContextService.getBean( "grustoragedb.DemandDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "grustoragedb" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DemandHome(  )
    {
    }
    
    /**
     * Finds all the demands 
     * @return all the demands. An empty collection is returned if no demands has been found.
     */
    public static List<Demand> getByIds( List<Integer> listIds)
    {
        return _dao.loadByIds( listIds);
    }
 
    /**
     * search demands by filter
     * 
     * @param strKey
     * @return 
     */
    public static List<Integer> searchIdsByFilter( NotificationFilter filter )
    {
        return _dao.loadIdsByFilter( filter );
    }
 
    /**
     * search demands by filter
     * 
     * @param strKey
     * @return 
     */
    public static Collection<Demand> searchByFilter( NotificationFilter filter )
    {
        return _dao.loadByFilter( filter );
    }
 

    /**
     * Finds a demand with the specified id and type id
     * @param nKey The project primary key
     * @return an instance of Project
     */
    public static Demand findByPrimaryKey( String strKey, String strDemandTypeId )
    {
        return _dao.load(strKey, strDemandTypeId);
    }
    
    /**
     * Finds the demands associated to the specified customer id
     * @return the list which contains the id of all the project objects
     */
    public static Collection<Demand> getDemandIdCustomer(String strCustomerId )
    {
        return _dao.loadByCustomerId(strCustomerId);
    }
    
    /**
     * Load demand ids ordered by date notification
     * @param strCustomerId
     * @param strNotificationType
     * @param strIdDemandType (Optional can be null)
     * @return The list of demand ids
     */
    public static List<Integer> getIdsByCustomerIdAndDemandTypeId( String strCustomerId,  String strNotificationType, String strIdDemandType  )
    {
        return _dao.loadIdsByCustomerIdAndIdDemandType( strCustomerId, strNotificationType, strIdDemandType );
    }
    
    /**
     * Load demand ids by status
     * @param strCustomerId
     * @param listStatus
     * @param strNotificationType
     * @param strIdDemandType
     * @return The list of demand ids
     */
    public static List<Integer> getIdsByStatus( String strCustomerId, List<String> listStatus, String strNotificationType, String strIdDemandType )
    {
        return _dao.loadIdsByStatus( strCustomerId, listStatus, strNotificationType, strIdDemandType );
    }
    
    /**
     * Updates a demand
     * 
     * @param demand
     *            the demand to update
     * @return the updated demand
     */
    public static Demand update( Demand demand )
    {
        return _dao.store( demand );
    }
}
