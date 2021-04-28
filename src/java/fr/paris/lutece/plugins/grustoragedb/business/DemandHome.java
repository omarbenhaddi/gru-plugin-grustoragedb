package fr.paris.lutece.plugins.grustoragedb.business;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

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
    public static Collection<Demand> getAllDemands()
    {
        return _dao.loadAllDemands();
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
    
    

}

