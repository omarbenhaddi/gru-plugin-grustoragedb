/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.plugins.grubusiness.business.notification.INotificationDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.plugins.grustoragedb.service.GruStorageDbPlugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Notification objects stored in SQL database
 */
public final class NotificationDAO implements INotificationDAO
{
    private static final String COLUMN_NOTIFICATION_CONTENT = "notification_content";
    private static final String COLUMN_NOTIFICATION_ID = "id";
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM grustoragedb_notification";
    private static final String SQL_QUERY_FILTER_SELECT_BASE = "SELECT id, notification_content FROM grustoragedb_notification ";
    private static final String SQL_QUERY_FILTER_SELECT_ID_BASE = "SELECT id FROM grustoragedb_notification ";
    private static final String SQL_QUERY_FILTER_WHERE_BASE = " WHERE ";
    private static final String SQL_QUERY_FILTER_WHERE_DEMANDID = " demand_id = ? ";
    private static final String SQL_QUERY_FILTER_WHERE_ID = " id = ? ";
    private static final String SQL_QUERY_FILTER_WHERE_DEMANDTYPEID = " demand_type_id = ? ";
    private static final String SQL_QUERY_FILTER_ORDER = " ORDER BY date DESC, id ASC";
    private static final String SQL_QUERY_FILTER_HAS_BACKOFFICE = " has_backoffice != 0 ";
    private static final String SQL_QUERY_FILTER_NO_BACKOFFICE = " has_backoffice = 0 ";
    private static final String SQL_QUERY_FILTER_HAS_SMS = " has_sms != 0 ";
    private static final String SQL_QUERY_FILTER_NO_SMS = " has_sms = 0 ";
    private static final String SQL_QUERY_FILTER_HAS_CUSTOMER_EMAIL = " has_customer_email != 0 ";
    private static final String SQL_QUERY_FILTER_NO_CUSTOMER_EMAIL = " has_customer_email = 0 ";
    private static final String SQL_QUERY_FILTER_HAS_MYDASHBOARD = " has_mydashboard != 0 ";
    private static final String SQL_QUERY_FILTER_NO_MYDASHBOARD = " has_mydashboard = 0 ";
    private static final String SQL_QUERY_FILTER_HAS_BROADCAST_EMAIL = " has_broadcast_email != 0 ";
    private static final String SQL_QUERY_FILTER_NO_BROADCAST_EMAIL = " has_broadcast_email = 0 ";
    private static final String SQL_QUERY_AND = " AND ";

    private static final String SQL_QUERY_INSERT = "INSERT INTO grustoragedb_notification ( id, demand_id, demand_type_id, date, has_backoffice, has_sms, has_customer_email, has_mydashboard, has_broadcast_email, notification_content ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";
    private static final String SQL_QUERY_DELETE = "DELETE FROM grustoragedb_notification WHERE demand_id = ? AND demand_type_id = ?";

    ObjectMapper _mapper;

    /**
     * Constructor
     */
    public NotificationDAO( )
    {
        super( );
        _mapper = new ObjectMapper( );
        _mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, false );
        _mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        _mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        _mapper.configure( Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> loadByDemand( String strDemandId, String strDemandTypeId )
    {
        NotificationFilter filter = new NotificationFilter( );
        filter.setDemandId( strDemandId );
        filter.setDemandTypeId( strDemandTypeId );

        return loadByFilter( filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> loadByFilter( NotificationFilter notificationFilter )
    {
        String strSQL = getFilterCriteriaClauses( SQL_QUERY_FILTER_SELECT_BASE, notificationFilter );
        List<Notification> listNotifications = new ArrayList<Notification>( );
        DAOUtil daoUtil = new DAOUtil( strSQL, GruStorageDbPlugin.getPlugin( ) );
        addFilterCriteriaValues( daoUtil, notificationFilter );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            String strNotificationJson = daoUtil.getString( COLUMN_NOTIFICATION_CONTENT );
            int nNotificationId = daoUtil.getInt( COLUMN_NOTIFICATION_ID );
            if ( StringUtils.isEmpty( strNotificationJson ) )
            {
                AppLogService.error( "JSON notification is empty for notification " + nNotificationId );
                continue;
            }
            Notification notification;
            try
            {
                notification = _mapper.readValue( strNotificationJson, Notification.class );
                notification.setId( nNotificationId );
                listNotifications.add( notification );
            }
            catch( JsonParseException | JsonMappingException e )
            {
                AppLogService.error( "Error while read JSON of notification " + nNotificationId, e );
            }
            catch( IOException e )
            {
                AppLogService.error( "Error while read JSON of notification " + nNotificationId, e );
            }
        }

        daoUtil.free( );

        return listNotifications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> loadIdsByFilter( NotificationFilter notificationFilter )
    {
        String strSQL = getFilterCriteriaClauses( SQL_QUERY_FILTER_SELECT_ID_BASE, notificationFilter );
        List<String> listIds = new ArrayList<>( );
        DAOUtil daoUtil = new DAOUtil( strSQL, GruStorageDbPlugin.getPlugin( ) );
        addFilterCriteriaValues( daoUtil, notificationFilter );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nNotificationId = daoUtil.getInt( COLUMN_NOTIFICATION_ID );
            String strNotificationId = String.valueOf( nNotificationId );
            listIds.add( strNotificationId );
        }

        daoUtil.free( );

        return listIds;
    }

    /**
     * @param notificationFilter
     * @return the query string
     */
    private String getFilterCriteriaClauses( String strBaseQuery, NotificationFilter notificationFilter )
    {
        StringBuilder sbQuery = new StringBuilder( strBaseQuery );
        boolean hasOneWhere = false;

        // WHERE
        if ( notificationFilter.containsDemandId( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_WHERE_BASE );
            sbQuery.append( SQL_QUERY_FILTER_WHERE_DEMANDID );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsDemandTypeId( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( SQL_QUERY_FILTER_WHERE_DEMANDTYPEID );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsId( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( SQL_QUERY_FILTER_WHERE_ID );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasBackofficeNotification( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( BooleanUtils.toString( notificationFilter.getHasBackofficeNotification( ), SQL_QUERY_FILTER_HAS_BACKOFFICE,
                    SQL_QUERY_FILTER_NO_BACKOFFICE ) );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasSmsNotification( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( BooleanUtils.toString( notificationFilter.getHasSmsNotification( ), SQL_QUERY_FILTER_HAS_SMS, SQL_QUERY_FILTER_NO_SMS ) );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasCustomerEmailNotification( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( BooleanUtils.toString( notificationFilter.getHasCustomerEmailNotification( ), SQL_QUERY_FILTER_HAS_CUSTOMER_EMAIL,
                    SQL_QUERY_FILTER_NO_CUSTOMER_EMAIL ) );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasMyDashboardNotification( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( BooleanUtils.toString( notificationFilter.getHasMyDashboardNotification( ), SQL_QUERY_FILTER_HAS_MYDASHBOARD,
                    SQL_QUERY_FILTER_NO_MYDASHBOARD ) );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasBroadcastEmailNotification( ) )
        {
            sbQuery.append( BooleanUtils.toString( hasOneWhere, SQL_QUERY_AND, SQL_QUERY_FILTER_WHERE_BASE ) );
            sbQuery.append( BooleanUtils.toString( notificationFilter.getHasBroadcastEmailNotification( ), SQL_QUERY_FILTER_HAS_BROADCAST_EMAIL,
                    SQL_QUERY_FILTER_NO_BROADCAST_EMAIL ) );
        }

        // ORDER
        sbQuery.append( SQL_QUERY_FILTER_ORDER );

        return sbQuery.toString( );
    }

    /**
     * @param daoUtil
     * @param notificationFilter
     */
    private void addFilterCriteriaValues( DAOUtil daoUtil, NotificationFilter notificationFilter )
    {
        int nIndex = 1;

        if ( notificationFilter.containsDemandId( ) )
        {
            daoUtil.setString( nIndex++, notificationFilter.getDemandId( ) );
        }
        if ( notificationFilter.containsId( ) )
        {
            daoUtil.setString( nIndex++, notificationFilter.getId( ) );
        }
        if ( notificationFilter.containsDemandTypeId( ) )
        {
            daoUtil.setString( nIndex++, notificationFilter.getDemandTypeId( ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Notification insert( Notification notification )
    {
        int nNotificationId = newPrimaryKey( );
        notification.setId( nNotificationId );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, GruStorageDbPlugin.getPlugin( ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, notification.getId( ) );
        daoUtil.setString( nIndex++, notification.getDemand( ).getId( ) );
        daoUtil.setString( nIndex++, notification.getDemand( ).getTypeId( ) );
        daoUtil.setLong( nIndex++, notification.getDate( ) );
        daoUtil.setInt( nIndex++, BooleanUtils.toInteger( ( notification.getBackofficeNotification( ) != null ), 1, 0 ) );
        daoUtil.setInt( nIndex++, BooleanUtils.toInteger( ( notification.getSmsNotification( ) != null ), 1, 0 ) );
        daoUtil.setInt( nIndex++, BooleanUtils.toInteger( ( notification.getEmailNotification( ) != null ), 1, 0 ) );
        daoUtil.setInt( nIndex++, BooleanUtils.toInteger( ( notification.getMyDashboardNotification( ) != null ), 1, 0 ) );
        daoUtil.setInt( nIndex++, BooleanUtils.toInteger( ( notification.getBroadcastEmail( ) != null && notification.getBroadcastEmail( ).size( ) > 0 ), 1, 0 ) );
        try
        {
            daoUtil.setBytes( nIndex++, _mapper.writeValueAsBytes( notification ) );
            daoUtil.executeUpdate( );
        }
        catch( JsonProcessingException e )
        {
            AppLogService.error( "Error while writing JSON of notification", e );
        }
        daoUtil.free( );

        return notification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByDemand( String strDemandId, String strDemandTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, GruStorageDbPlugin.getPlugin( ) );

        daoUtil.setString( 1, strDemandId );
        daoUtil.setString( 2, strDemandTypeId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Generates a new primary key
     *
     * @return the primary key
     */
    private int newPrimaryKey( )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, GruStorageDbPlugin.getPlugin( ) );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification loadById( String strId )
    {
        NotificationFilter filter = new NotificationFilter( );
        filter.setId( strId );

        List<Notification> listNotifs = loadByFilter( filter );

        if ( listNotifs.size( ) == 1 )
        {
            return listNotifs.get( 0 );
        }
        return null;
    }
}
