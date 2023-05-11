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

import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grustoragedb.service.GruStorageDbPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

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

}