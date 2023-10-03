/*
 * Copyright (c) 2002-2023, Mairie de Paris
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
 
package fr.paris.lutece.plugins.grustoragedb.business;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.web.rs.EnumGenericStatus;
import fr.paris.lutece.plugins.grustoragedb.service.GruStorageDbPlugin;
import fr.paris.lutece.plugins.grustoragedb.utils.GrustoragedbUtils;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class provides instances management methods (create, find, ...) for NotificationContent objects
 */

public final class NotificationContentHome
{

    // Static variable pointed at the DAO instance

    private static INotificationContentDAO _dao = ( INotificationContentDAO ) SpringContextService.getBean( "grustoragedb.notificationContentDAO" );


    /**
     * Private constructor - this class need not be instantiated
     */

    private NotificationContentHome(  )
    {
    }

    /**
     * Create an instance of the notificationContent class
     * @param notificationContent The instance of the NotificationContent which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of notificationContent which has been created with its primary key.
     */

    public static NotificationContent create( NotificationContent notificationContent )
    {
        _dao.insert( notificationContent, GruStorageDbPlugin.getPlugin( ) );

        return notificationContent;
    }
    
    /**
     * Create an instance of the notificationContent class
     * @param notificationContent The instance of the NotificationContent which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of notificationContent which has been created with its primary key.
     */

    public static List<NotificationContent>  create( Notification notification )
    {
        List<NotificationContent> listNotificationContent = getListNotificationContent ( notification );
        for( NotificationContent content : listNotificationContent )
        {
            _dao.insert( content, GruStorageDbPlugin.getPlugin( ) );
        }

        return listNotificationContent;
    }


    /**
     * Update of the notificationContent which is specified in parameter
     * @param notificationContent The instance of the NotificationContent which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  notificationContent which has been updated
     */

    public static NotificationContent update( NotificationContent notificationContent )
    {
        _dao.store( notificationContent, GruStorageDbPlugin.getPlugin( ) );

        return notificationContent;
    }


    /**
     * Remove the notificationContent whose identifier is specified in parameter
     * @param nNotificationContentId The notificationContent Id
     * @param plugin the Plugin
     */


    public static void remove( int nNotificationContentId )
    {
        _dao.delete( nNotificationContentId, GruStorageDbPlugin.getPlugin( ) );
    }


    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a notificationContent whose identifier is specified in parameter
     * @param nKey The notificationContent primary key
     * @param plugin the Plugin
     * @return an instance of NotificationContent
     */

    public static NotificationContent findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, GruStorageDbPlugin.getPlugin( ));
    }


    /**
     * Load the data of all the notificationContent objects and returns them in form of a list
     * @return the list which contains the data of all the notificationContent objects
     */

    public static List<NotificationContent> getNotificationContentsList( )
    {
        return _dao.selectNotificationContentsList( GruStorageDbPlugin.getPlugin( ) );
    }
    
    /**
     * Load the data by id notification and returns them in form of a list
     * @param nIdNotification the id notification
     * @return the list which contains the data by id notification
     */

    public static List<NotificationContent> getNotificationContentsByIdNotification( int nIdNotification )
    {
        return _dao.selectNotificationContentsByIdNotification( nIdNotification, GruStorageDbPlugin.getPlugin( ) );
    }
    
    
    /**
     * Load the data by id notification and notification type
     * @param nIdNotification the id notification
     * @return the list which contains the data by id notification
     */

    public static List<NotificationContent> getNotificationContentsByIdAndTypeNotification( int nIdNotification, List<EnumNotificationType> listNotificationType )
    {
        return _dao.selectNotificationContentsByIdAndTypeNotification( nIdNotification, listNotificationType, GruStorageDbPlugin.getPlugin( ) );
    }
    
    
    private static List<NotificationContent> getListNotificationContent( Notification notification )
    {
        List<NotificationContent> listNotificationContent = new ArrayList< >();

        try
        {
            ObjectMapper mapperr = GrustoragedbUtils.initMapper( );
            Demand demand = notification.getDemand( );
            
            if ( notification.getSmsNotification( ) != null )
            {
                listNotificationContent.add( initNotificationContent( notification, EnumNotificationType.SMS, mapperr.writeValueAsString( notification.getSmsNotification( ) ) ) );
            }

            if ( notification.getBackofficeNotification( ) != null )
            {
                listNotificationContent.add( initNotificationContent( notification, EnumNotificationType.BACKOFFICE, mapperr.writeValueAsString( notification.getBackofficeNotification( ) ) ) );
            }

            if ( CollectionUtils.isNotEmpty( notification.getBroadcastEmail( ) ) )
            {
                listNotificationContent.add( initNotificationContent( notification, EnumNotificationType.BROADCAST_EMAIL, mapperr.writeValueAsString( notification.getBroadcastEmail( ) ) ) );
            }

            if ( notification.getMyDashboardNotification( ) != null )
            {
                demand.setStatusId( getStatusNotification( notification ) );
                listNotificationContent.add( initNotificationContent( notification, EnumNotificationType.MYDASHBOARD, mapperr.writeValueAsString( notification.getMyDashboardNotification( ) ) ) );
            }

            if ( notification.getEmailNotification( ) != null )
            {
                listNotificationContent.add( initNotificationContent( notification, EnumNotificationType.CUSTOMER_EMAIL, mapperr.writeValueAsString( notification.getEmailNotification( ) ) ) );
            }
            demand.setModifyDate( new Date( ).getTime( ) );
            DemandHome.update( demand );
            
        }
        catch ( JsonProcessingException e )
        {
            AppLogService.error( "Error while writing JSON of notification", e );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Error while compressing or writing JSON of notification", e );
        }      
        
        return listNotificationContent;
    }
    
    
    /**
     * Init Notification content
     * 
     * @param nNotificationId
     * @param notificationType
     * @param strNotificationContent
     * @throws IOException
     */
    private static NotificationContent initNotificationContent( Notification notification, EnumNotificationType notificationType, String strNotificationContent ) throws IOException
    {
        strNotificationContent = strNotificationContent.replaceAll( GrustoragedbUtils.CHARECTER_REGEXP_FILTER, "" );

        byte[] bytes;

        if ( AppPropertiesService.getPropertyBoolean( GrustoragedbUtils.PROPERTY_COMPRESS_NOTIFICATION, false ) )
        {
            bytes = StringUtil.compress( strNotificationContent );
        } else
        {
            bytes = strNotificationContent.getBytes( StandardCharsets.UTF_8 );
        }

        NotificationContent notificationContent = new NotificationContent( );
        notificationContent.setIdNotification( notification.getId( ) );
        notificationContent.setNotificationType( notificationType.name( ) );
        notificationContent.setStatusId( getStatusNotification( notification ) );
        notificationContent.setContent( bytes );

        return notificationContent;
    }
    
    /**
     * Get status for mydashboard notification
     * @param notification
     */
    private static Integer getStatusNotification ( Notification notification )
    {
        if (  notification.getMyDashboardNotification( ) != null )
        {           
            if( EnumGenericStatus.existStatus( notification.getMyDashboardNotification( ).getStatusId( ) ) ) 
            {
                return notification.getMyDashboardNotification( ).getStatusId( );
            } 
            else
            {
                Optional<Status> status = StatusHome.findByStatus( notification.getMyDashboardNotification( ).getStatusText( ) );
                if( status.isPresent( ) )
                {
                    EnumGenericStatus genericStatus =  EnumGenericStatus.valueOf( status.get( ).getCodeStatus( ) );
                    if( genericStatus != null )
                    {
                        return genericStatus.getStatusId( );
                    }
                }
                return -1;
            }
        }
        return null;
    }

}