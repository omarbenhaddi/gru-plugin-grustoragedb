/*
 * Copyright (c) 2002-2023, City of Paris
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
package fr.paris.lutece.plugins.grustoragedb.business.listener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;


import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.notification.EnumNotificationType;
import fr.paris.lutece.plugins.grubusiness.business.notification.INotificationListener;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grustoragedb.business.DemandHome;
import fr.paris.lutece.plugins.grustoragedb.business.NotificationContent;
import fr.paris.lutece.plugins.grustoragedb.business.NotificationContentHome;
import fr.paris.lutece.plugins.grustoragedb.business.Status;
import fr.paris.lutece.plugins.grustoragedb.business.StatusHome;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;

/**
 * 
 * Listener to Store notification content
 *
 */
public class ContentNotificationListener implements INotificationListener
{
    private static final String PROPERTY_COMPRESS_NOTIFICATION   = "grustoragedb.notification.compress";
    private static final String CHARECTER_REGEXP_FILTER          = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\p{Sm}\\p{Sc}\\s]";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateNotification( Notification notification )
    {

        try
        {
            ObjectMapper mapperr = initMapper( );
            Demand demand = notification.getDemand( );
            
            if ( notification.getSmsNotification( ) != null )
            {
                storeNotificationContent( notification, EnumNotificationType.SMS, mapperr.writeValueAsString( notification.getSmsNotification( ) ) );
            }

            if ( notification.getBackofficeNotification( ) != null )
            {
                storeNotificationContent( notification, EnumNotificationType.BACKOFFICE, mapperr.writeValueAsString( notification.getBackofficeNotification( ) ) );
            }

            if ( CollectionUtils.isNotEmpty( notification.getBroadcastEmail( ) ) )
            {
                storeNotificationContent( notification, EnumNotificationType.BROADCAST_EMAIL, mapperr.writeValueAsString( notification.getBroadcastEmail( ) ) );
            }

            if ( notification.getMyDashboardNotification( ) != null )
            {
                storeNotificationContent( notification, EnumNotificationType.MYDASHBOARD, mapperr.writeValueAsString( notification.getMyDashboardNotification( ) ) );
                demand.setStatusMyDashboard( getStatusNotification( notification ) );
            }

            if ( notification.getEmailNotification( ) != null )
            {
                storeNotificationContent( notification, EnumNotificationType.CUSTOMER_EMAIL, mapperr.writeValueAsString( notification.getEmailNotification( ) ) );
            }
            
            demand.setModifyDate( new Date( ).getTime( ) );
            demand.setRead( false );
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateNotification( Notification notification )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDeleteDemand( String strDemandId, String strDemandTypeId )
    {
        // Do nothing
    }

    /**
     * Store notification content
     * 
     * @param nNotificationId
     * @param notificationType
     * @param strNotificationContent
     * @throws IOException
     */
    private void storeNotificationContent( Notification notification, EnumNotificationType notificationType, String strNotificationContent ) throws IOException
    {
        strNotificationContent = strNotificationContent.replaceAll( CHARECTER_REGEXP_FILTER, "" );

        byte[] bytes;

        if ( AppPropertiesService.getPropertyBoolean( PROPERTY_COMPRESS_NOTIFICATION, false ) )
        {
            bytes = StringUtil.compress( strNotificationContent );
        } else
        {
            bytes = strNotificationContent.getBytes( StandardCharsets.UTF_8 );
        }

        NotificationContent notificationContent = new NotificationContent( );
        notificationContent.setIdNotification( notification.getId( ) );
        notificationContent.setNotificationType( notificationType.name( ) );
        notificationContent.setStatus( getStatusNotification( notification ) );
        notificationContent.setContent( bytes );

        NotificationContentHome.create( notificationContent );
    }
    
    /**
     * Get status for mydashboard notification
     * @param notification
     */
    private String getStatusNotification ( Notification notification )
    {
        if (  notification.getMyDashboardNotification( ) != null )
        {
            if ( notification.getMyDashboardNotification( ).getStatusId( ) != 1 
                    && notification.getMyDashboardNotification( ).getStatusId( ) != 0 )
            {
                Optional<Status> status = StatusHome.findByStatusName( notification.getMyDashboardNotification( ).getStatusText( ) );
                
                if( status.isPresent( ) )
                {
                    
                    return status.get( ).getLabel( );
                }
                else
                {
                    return notification.getMyDashboardNotification( ).getStatusText( );
                }
            }
            else
            {
                return notification.getMyDashboardNotification( ).getStatusText( );
            }
        }
        return null;
    }
    
    /**
     * InitMapper
     */
    private static ObjectMapper initMapper( )
    {
        ObjectMapper mapper = new ObjectMapper( );
        mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, false );
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        mapper.configure( Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );
        
        return mapper;
    }
}
