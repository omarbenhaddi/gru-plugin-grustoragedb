package fr.paris.lutece.plugins.grustoragedb.service;


import fr.paris.lutece.plugins.grustoragedb.business.NotificationEventHome;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * My Daemon
 */
public class NotificationEventDaemon extends Daemon
{
	private static final String PROPERTY_EVENT_NB_DAYS_BEFORE_PURGE = "grustoragedb.daemon.NotificationEventDaemon.purge.nbDaysBefore";

	/**
	 * {@inheritDoc}
	 */  
	@Override
	public void run(  )
	{
		setLastRunLogs( purgeEvents( ) );
	}

	/**
	 * purge events
	 * 
	 * @return the success of the purge
	 */
	private String purgeEvents( ) {

		int nbDaysBeforePurge = AppPropertiesService.getPropertyInt( PROPERTY_EVENT_NB_DAYS_BEFORE_PURGE, 60 );
		
		NotificationEventHome.purge( nbDaysBeforePurge ) ;

		return "Success (purge frequency : " + nbDaysBeforePurge + " days)";
	}
}
