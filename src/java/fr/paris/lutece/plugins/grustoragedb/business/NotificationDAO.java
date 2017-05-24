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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.notification.BackofficeNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.BroadcastNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.EmailAddress;
import fr.paris.lutece.plugins.grubusiness.business.notification.EmailNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.INotificationDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.MyDashboardNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationFilter;
import fr.paris.lutece.plugins.grubusiness.business.notification.SMSNotification;
import fr.paris.lutece.plugins.grustoragedb.service.GruStorageDbPlugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Notification objects stored in SQL database
 */
public final class NotificationDAO implements INotificationDAO
{
    private static final String COLUMN_ID = "a.id";
    private static final String COLUMN_DATE = "a.date";
    private static final String COLUMN_DEMAND_ID = "a.demand_id";
    private static final String COLUMN_DEMAND_TYPEID = "a.demand_type_id";
    private static final String COLUMN_BACKOFFICE_NOTIFICATION_ID = "b.notification_id";
    private static final String COLUMN_BACKOFFICE_MESSAGE = "b.message";
    private static final String COLUMN_BACKOFFICE_STATUS_TEXT = "b.status_text";
    private static final String COLUMN_SMS_NOTIFICATION_ID = "c.notification_id";
    private static final String COLUMN_SMS_MESSAGE = "c.message";
    private static final String COLUMN_SMS_PHONE_NUMBER = "c.phone_number";
    private static final String COLUMN_CUSTOMER_EMAIL_NOTIFICATION_ID = "d.notification_id";
    private static final String COLUMN_CUSTOMER_EMAIL_SENDER_EMAIL = "d.sender_email";
    private static final String COLUMN_CUSTOMER_EMAIL_SENDER_NAME = "d.sender_name";
    private static final String COLUMN_CUSTOMER_EMAIL_SUBJECT = "d.subject";
    private static final String COLUMN_CUSTOMER_EMAIL_MESSAGE = "d.message";
    private static final String COLUMN_CUSTOMER_EMAIL_RECIPIENTS = "d.recipients";
    private static final String COLUMN_CUSTOMER_EMAIL_COPIES = "d.copies";
    private static final String COLUMN_CUSTOMER_EMAIL_BLIND_COPIES = "d.blind_copies";
    private static final String COLUMN_MYDASHBOARD_NOTIFICATION_ID = "e.notification_id";
    private static final String COLUMN_MYDASHBOARD_STATUS_ID = "e.status_id";
    private static final String COLUMN_MYDASHBOARD_STATUS_TEXT = "e.status_text";
    private static final String COLUMN_MYDASHBOARD_MESSAGE = "e.message";
    private static final String COLUMN_MYDASHBOARD_SUBJECT = "e.subject";
    private static final String COLUMN_MYDASHBOARD_DATA = "e.data";
    private static final String COLUMN_MYDASHBOARD_SENDER_NAME = "e.sender_name";
    private static final String COLUMN_BROADCAST_EMAIL_NOTIFICATION_ID = "f.notification_id";
    private static final String COLUMN_BROADCAST_EMAIL_SENDER_EMAIL = "f.sender_email";
    private static final String COLUMN_BROADCAST_EMAIL_SENDER_NAME = "f.sender_name";
    private static final String COLUMN_BROADCAST_EMAIL_SUBJECT = "f.subject";
    private static final String COLUMN_BROADCAST_EMAIL_MESSAGE = "f.message";
    private static final String COLUMN_BROADCAST_EMAIL_RECIPIENTS = "f.recipients";
    private static final String COLUMN_BROADCAST_EMAIL_COPIES = "f.copies";
    private static final String COLUMN_BROADCAST_EMAIL_BLIND_COPIES = "f.blind_copies";
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id ) FROM grustoragedb_notification";
    private static final String SQL_QUERY_SELECT_BY_DEMAND = "SELECT a.id, a.date, b.notification_id, b.message, b.status_text, c.notification_id, c.message, c.phone_number, "
            + "d.notification_id, d.sender_email, d.sender_name, d.subject, d.message, d.recipients, d.copies, d.blind_copies, "
            + "e.notification_id, e.status_id, e.status_text, e.message, e.subject, e.data, e.sender_name, "
            + "f.notification_id, f.sender_email, f.sender_name, f.subject, f.message, f.recipients, f.copies, f.blind_copies "
            + "FROM grustoragedb_notification a "
            + "LEFT JOIN grustoragedb_notification_backoffice b ON a.id = b.notification_id "
            + "LEFT JOIN grustoragedb_notification_sms c ON a.id = c.notification_id "
            + "LEFT JOIN grustoragedb_notification_customer_email d ON a.id = d.notification_id "
            + "LEFT JOIN grustoragedb_notification_mydashboard e ON a.id = e.notification_id "
            + "LEFT JOIN grustoragedb_notification_broadcast_email f ON a.id = f.notification_id "
            + "WHERE a.demand_id = ? AND a.demand_type_id = ? ORDER BY a.date DESC, a.id ASC";

    private static final String SQL_QUERY_FILTER_SELECT_BASE = "SELECT a.id, a.demand_id, a.demand_type_id, a.date";
    private static final String SQL_QUERY_FILTER_SELECT_BACKOFFICE = ", b.notification_id, b.message, b.status_text";
    private static final String SQL_QUERY_FILTER_SELECT_SMS = ", c.notification_id, c.message, c.phone_number";
    private static final String SQL_QUERY_FILTER_SELECT_EMAIL = ", d.notification_id, d.sender_email, d.sender_name, d.subject, d.message, d.recipients, d.copies, d.blind_copies";
    private static final String SQL_QUERY_FILTER_SELECT_MYDASHBAORD = ", e.notification_id, e.status_id, e.status_text, e.message, e.subject, e.data, e.sender_name";
    private static final String SQL_QUERY_FILTER_SELECT_BROADCAST = ", f.notification_id, f.sender_email, f.sender_name, f.subject, f.message, f.recipients, f.copies, f.blind_copies";
    private static final String SQL_QUERY_FILTER_FROM_BASE = " FROM grustoragedb_notification a ";
    private static final String SQL_QUERY_FILTER_JOIN_BACKOFFICE = " LEFT JOIN grustoragedb_notification_backoffice b ON a.id = b.notification_id ";
    private static final String SQL_QUERY_FILTER_JOIN_SMS = " LEFT JOIN grustoragedb_notification_sms c ON a.id = c.notification_id ";
    private static final String SQL_QUERY_FILTER_JOIN_EMAIL = " LEFT JOIN grustoragedb_notification_customer_email d ON a.id = d.notification_id ";
    private static final String SQL_QUERY_FILTER_JOIN_MYDASHBAORD = " LEFT JOIN grustoragedb_notification_mydashboard e ON a.id = e.notification_id ";
    private static final String SQL_QUERY_FILTER_JOIN_BROADCAST = " LEFT JOIN grustoragedb_notification_broadcast_email f ON a.id = f.notification_id ";
    private static final String SQL_QUERY_FILTER_WHERE_BASE = " WHERE ";
    private static final String SQL_QUERY_FILTER_WHERE_DEMANDID = " a.demand_id = ? ";
    private static final String SQL_QUERY_FILTER_WHERE_DEMANDTYPEID = " a.demand_type_id = ? ";
    private static final String SQL_QUERY_FILTER_ORDER = " ORDER BY a.date DESC, a.id ASC";
    private static final String SQL_QUERY_AND = " AND ";
    private static final String SQL_QUERY_IS_NULL = " IS NULL ";
    private static final String SQL_QUERY_IS_NOT_NULL = " IS NOT NULL ";

    private static final String SQL_QUERY_INSERT = "INSERT INTO grustoragedb_notification ( id, demand_id, demand_type_id, date ) VALUES ( ?, ?, ?, ? );";
    private static final String SQL_QUERY_INSERT_BACKOFFICE = "INSERT INTO grustoragedb_notification_backoffice ( notification_id, message, status_text ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_SMS = "INSERT INTO grustoragedb_notification_sms ( notification_id, message, phone_number ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_CUSTOMER_EMAIL = "INSERT INTO grustoragedb_notification_customer_email ( notification_id, sender_email, sender_name, subject, message, recipients, copies, blind_copies ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_MYDASHBOARD = "INSERT INTO grustoragedb_notification_mydashboard ( notification_id, status_id, status_text, message, subject, data, sender_name ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_BROADCAST_EMAIL = "INSERT INTO grustoragedb_notification_broadcast_email ( notification_id, sender_email, sender_name, subject, message, recipients, copies, blind_copies ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_BROADCAST_EMAIL_MORE_VALUES = ", ( ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE a, b, c, d, e, f FROM grustoragedb_notification a "
            + "LEFT JOIN grustoragedb_notification_backoffice b ON a.id = b.notification_id "
            + "LEFT JOIN grustoragedb_notification_sms c ON a.id = c.notification_id "
            + "LEFT JOIN grustoragedb_notification_customer_email d ON a.id = d.notification_id "
            + "LEFT JOIN grustoragedb_notification_mydashboard e ON a.id = e.notification_id "
            + "LEFT JOIN grustoragedb_notification_broadcast_email f ON a.id = f.notification_id " + "WHERE a.demand_id = ? AND a.demand_type_id = ?";

    // Other constants
    private static final String EMAIL_SEPARATOR = ";";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> loadByDemand( String strDemandId, String strDemandTypeId )
    {
        List<Notification> collectionNotifications = new ArrayList<Notification>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DEMAND, GruStorageDbPlugin.getPlugin( ) );

        int nIndex = 1;

        daoUtil.setString( nIndex++, strDemandId );
        daoUtil.setString( nIndex++, strDemandTypeId );

        daoUtil.executeQuery( );

        // WARNING It is a full request which mean we have ONE line for each grustoragedb_notification_broadcast_email
        // this mean the ORDER is important to avoid duplicate and we must not create a 'new' notification each time
        // BUT the add in collection is done only if notification id are different
        Notification notification = null;
        while ( daoUtil.next( ) )
        {
            int nIdNotifBase = daoUtil.getInt( COLUMN_ID );

            if ( notification == null || notification.getId( ) != nIdNotifBase )
            {
                // new notification
                if ( notification != null )
                {
                    collectionNotifications.add( notification );
                }
                notification = new Notification( );
                notification.setId( nIdNotifBase );
                notification.setDate( daoUtil.getLong( COLUMN_DATE ) );

                Demand demand = new Demand( );
                demand.setId( strDemandId );
                demand.setTypeId( strDemandTypeId );
                notification.setDemand( demand );

                loadBackofficeNotification( daoUtil, notification );
                loadSmsNotification( daoUtil, notification );
                loadCustomerEmailNotification( daoUtil, notification );
                loadMyDashboardNotification( daoUtil, notification );
                loadBroadcastEmailNotification( daoUtil, notification );
            }
            else
            {
                // same notification, just add the grustoragedb_notification_broadcast_email
                loadBroadcastEmailNotification( daoUtil, notification );
            }
        }
        if ( notification != null )
        {
            collectionNotifications.add( notification );
        }

        daoUtil.free( );

        return collectionNotifications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> loadByFilter( NotificationFilter notificationFilter )
    {
        String strSQL = getFilterCriteriaClauses( notificationFilter );
        List<Notification> collectionNotifications = new ArrayList<Notification>( );
        DAOUtil daoUtil = new DAOUtil( strSQL.toString( ), GruStorageDbPlugin.getPlugin( ) );
        addFilterCriteriaValues( daoUtil, notificationFilter );

        daoUtil.executeQuery( );

        // WARNING It is a full request which mean we have ONE line for each grustoragedb_notification_broadcast_email
        // this mean the ORDER is important to avoid duplicate and we must not create a 'new' notification each time
        // BUT the add in collection is done only if notification id are different
        Notification notification = null;
        while ( daoUtil.next( ) )
        {
            int nIdNotifBase = daoUtil.getInt( COLUMN_ID );

            if ( notification == null || notification.getId( ) != nIdNotifBase )
            {
                // new notification
                if ( notification != null )
                {
                    collectionNotifications.add( notification );
                }
                notification = new Notification( );
                notification.setId( nIdNotifBase );
                notification.setDate( daoUtil.getLong( COLUMN_DATE ) );

                Demand demand = new Demand( );
                demand.setId( daoUtil.getString( COLUMN_DEMAND_ID ) );
                demand.setTypeId( daoUtil.getString( COLUMN_DEMAND_TYPEID ) );
                notification.setDemand( demand );

                if ( notificationFilter.containsHasBackofficeNotification( ) )
                {
                    loadBackofficeNotification( daoUtil, notification );
                }
                if ( notificationFilter.containsHasSmsNotification( ) )
                {
                    loadSmsNotification( daoUtil, notification );
                }
                if ( notificationFilter.containsHasCustomerEmailNotification( ) )
                {
                    loadCustomerEmailNotification( daoUtil, notification );
                }
                if ( notificationFilter.containsHasMyDashboardNotification( ) )
                {
                    loadMyDashboardNotification( daoUtil, notification );
                }
                if ( notificationFilter.containsHasBroadcastEmailNotification( ) )
                {
                    loadBroadcastEmailNotification( daoUtil, notification );
                }
            }
            else
                if ( notificationFilter.containsHasBroadcastEmailNotification( ) )
                {
                    // same notification, just add the grustoragedb_notification_broadcast_email
                    loadBroadcastEmailNotification( daoUtil, notification );
                }
        }
        if ( notification != null )
        {
            collectionNotifications.add( notification );
        }

        daoUtil.free( );

        return collectionNotifications;
    }

    /**
     * @param notificationFilter
     * @return the query string
     */
    private String getFilterCriteriaClauses( NotificationFilter notificationFilter )
    {
        StringBuilder sbQuery = new StringBuilder( SQL_QUERY_FILTER_SELECT_BASE );
        boolean hasOneFilter = notificationFilter.containsDemandId( ) || notificationFilter.containsDemandTypeId( );
        boolean hasOneWhere = false;
        // SELECT
        if ( notificationFilter.containsHasBackofficeNotification( ) )
        {
            hasOneFilter = true;
            sbQuery.append( SQL_QUERY_FILTER_SELECT_BACKOFFICE );
        }
        if ( notificationFilter.containsHasSmsNotification( ) )
        {
            hasOneFilter = true;
            sbQuery.append( SQL_QUERY_FILTER_SELECT_SMS );
        }
        if ( notificationFilter.containsHasCustomerEmailNotification( ) )
        {
            hasOneFilter = true;
            sbQuery.append( SQL_QUERY_FILTER_SELECT_EMAIL );
        }
        if ( notificationFilter.containsHasMyDashboardNotification( ) )
        {
            hasOneFilter = true;
            sbQuery.append( SQL_QUERY_FILTER_SELECT_MYDASHBAORD );
        }
        if ( notificationFilter.containsHasBroadcastEmailNotification( ) )
        {
            hasOneFilter = true;
            sbQuery.append( SQL_QUERY_FILTER_SELECT_BROADCAST );
        }

        // FROM
        sbQuery.append( SQL_QUERY_FILTER_FROM_BASE );
        if ( notificationFilter.containsHasBackofficeNotification( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_JOIN_BACKOFFICE );
        }
        if ( notificationFilter.containsHasSmsNotification( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_JOIN_SMS );
        }
        if ( notificationFilter.containsHasCustomerEmailNotification( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_JOIN_EMAIL );
        }
        if ( notificationFilter.containsHasMyDashboardNotification( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_JOIN_MYDASHBAORD );
        }
        if ( notificationFilter.containsHasBroadcastEmailNotification( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_JOIN_BROADCAST );
        }

        // WHERE
        if ( hasOneFilter )
        {
            sbQuery.append( SQL_QUERY_FILTER_WHERE_BASE );
        }

        if ( notificationFilter.containsDemandId( ) )
        {
            sbQuery.append( SQL_QUERY_FILTER_WHERE_DEMANDID );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsDemandTypeId( ) )
        {
            if ( hasOneWhere )
            {
                sbQuery.append( SQL_QUERY_AND );
            }
            sbQuery.append( SQL_QUERY_FILTER_WHERE_DEMANDTYPEID );
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasBackofficeNotification( ) )
        {
            if ( hasOneWhere )
            {
                sbQuery.append( SQL_QUERY_AND );
            }
            sbQuery.append( COLUMN_BACKOFFICE_NOTIFICATION_ID );
            if ( notificationFilter.getHasBackofficeNotification( ) )
            {
                sbQuery.append( SQL_QUERY_IS_NOT_NULL );
            }
            else
            {
                sbQuery.append( SQL_QUERY_IS_NULL );
            }
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasSmsNotification( ) )
        {
            if ( hasOneWhere )
            {
                sbQuery.append( SQL_QUERY_AND );
            }
            sbQuery.append( COLUMN_SMS_NOTIFICATION_ID );
            if ( notificationFilter.getHasSmsNotification( ) )
            {
                sbQuery.append( SQL_QUERY_IS_NOT_NULL );
            }
            else
            {
                sbQuery.append( SQL_QUERY_IS_NULL );
            }
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasCustomerEmailNotification( ) )
        {
            if ( hasOneWhere )
            {
                sbQuery.append( SQL_QUERY_AND );
            }
            sbQuery.append( COLUMN_CUSTOMER_EMAIL_NOTIFICATION_ID );
            if ( notificationFilter.getHasCustomerEmailNotification( ) )
            {
                sbQuery.append( SQL_QUERY_IS_NOT_NULL );
            }
            else
            {
                sbQuery.append( SQL_QUERY_IS_NULL );
            }
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasMyDashboardNotification( ) )
        {
            if ( hasOneWhere )
            {
                sbQuery.append( SQL_QUERY_AND );
            }
            sbQuery.append( COLUMN_MYDASHBOARD_NOTIFICATION_ID );
            if ( notificationFilter.getHasMyDashboardNotification( ) )
            {
                sbQuery.append( SQL_QUERY_IS_NOT_NULL );
            }
            else
            {
                sbQuery.append( SQL_QUERY_IS_NULL );
            }
            hasOneWhere = true;
        }
        if ( notificationFilter.containsHasBroadcastEmailNotification( ) )
        {
            if ( hasOneWhere )
            {
                sbQuery.append( SQL_QUERY_AND );
            }
            sbQuery.append( COLUMN_BROADCAST_EMAIL_NOTIFICATION_ID );
            if ( notificationFilter.getHasBroadcastEmailNotification( ) )
            {
                sbQuery.append( SQL_QUERY_IS_NOT_NULL );
            }
            else
            {
                sbQuery.append( SQL_QUERY_IS_NULL );
            }
            hasOneWhere = true;
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

        daoUtil.executeUpdate( );
        daoUtil.free( );

        insertBackofficeNotification( notification );
        insertSmsNotification( notification );
        insertCustomerEmailNotification( notification );
        insertMyDashboardNotification( notification );
        insertBroadcastEmailNotification( notification );

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
     * Finds the backoffice notification associated to the specified notification and sets it into the specified notification
     * 
     * @param daoUtil
     *            the SQL query manager
     * @param notification
     *            the notification
     */
    private void loadBackofficeNotification( DAOUtil daoUtil, Notification notification )
    {
        int nNotificationId = daoUtil.getInt( COLUMN_BACKOFFICE_NOTIFICATION_ID );

        if ( nNotificationId != 0 )
        {
            BackofficeNotification backofficeNotification = new BackofficeNotification( );

            backofficeNotification.setMessage( daoUtil.getString( COLUMN_BACKOFFICE_MESSAGE ) );
            backofficeNotification.setStatusText( daoUtil.getString( COLUMN_BACKOFFICE_STATUS_TEXT ) );

            notification.setBackofficeNotification( backofficeNotification );
        }
    }

    /**
     * Finds the SMS notification associated to the specified notification and sets it into the specified notification
     * 
     * @param daoUtil
     *            the SQL query manager
     * @param notification
     *            the notification
     */
    private void loadSmsNotification( DAOUtil daoUtil, Notification notification )
    {
        int nNotificationId = daoUtil.getInt( COLUMN_SMS_NOTIFICATION_ID );

        if ( nNotificationId != 0 )
        {
            SMSNotification smsNotification = new SMSNotification( );

            smsNotification.setMessage( daoUtil.getString( COLUMN_SMS_MESSAGE ) );
            smsNotification.setPhoneNumber( daoUtil.getString( COLUMN_SMS_PHONE_NUMBER ) );

            notification.setSmsNotification( smsNotification );
        }
    }

    /**
     * Finds the customer email notification associated to the specified notification and sets it into the specified notification
     * 
     * @param daoUtil
     *            the SQL query manager
     * @param notification
     *            the notification
     */
    private void loadCustomerEmailNotification( DAOUtil daoUtil, Notification notification )
    {
        int nNotificationId = daoUtil.getInt( COLUMN_CUSTOMER_EMAIL_NOTIFICATION_ID );

        if ( nNotificationId != 0 )
        {
            EmailNotification customerEmailNotification = new EmailNotification( );

            customerEmailNotification.setSenderEmail( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_SENDER_EMAIL ) );
            customerEmailNotification.setSenderName( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_SENDER_NAME ) );
            customerEmailNotification.setSubject( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_SUBJECT ) );
            customerEmailNotification.setMessage( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_MESSAGE ) );
            customerEmailNotification.setRecipient( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_RECIPIENTS ) );
            customerEmailNotification.setCc( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_COPIES ) );
            customerEmailNotification.setBcc( daoUtil.getString( COLUMN_CUSTOMER_EMAIL_BLIND_COPIES ) );

            notification.setEmailNotification( customerEmailNotification );
        }
    }

    /**
     * Finds the MyDashboardNotification notification associated to the specified notification and sets it into the specified notification
     * 
     * @param daoUtil
     *            the SQL query manager
     * @param notification
     *            the notification
     */
    private void loadMyDashboardNotification( DAOUtil daoUtil, Notification notification )
    {
        int nNotificationId = daoUtil.getInt( COLUMN_MYDASHBOARD_NOTIFICATION_ID );

        if ( nNotificationId != 0 )
        {
            MyDashboardNotification myDashboardNotification = new MyDashboardNotification( );

            myDashboardNotification.setStatusId( daoUtil.getInt( COLUMN_MYDASHBOARD_STATUS_ID ) );
            myDashboardNotification.setStatusText( daoUtil.getString( COLUMN_MYDASHBOARD_STATUS_TEXT ) );
            myDashboardNotification.setMessage( daoUtil.getString( COLUMN_MYDASHBOARD_MESSAGE ) );
            myDashboardNotification.setSubject( daoUtil.getString( COLUMN_MYDASHBOARD_SUBJECT ) );
            myDashboardNotification.setData( daoUtil.getString( COLUMN_MYDASHBOARD_DATA ) );
            myDashboardNotification.setSenderName( daoUtil.getString( COLUMN_MYDASHBOARD_SENDER_NAME ) );

            notification.setMyDashboardNotification( myDashboardNotification );
        }
    }

    /**
     * Finds one BroadcastEmailNotification notification associated to the specified notification and add it into the specified notification
     * 
     * @param daoUtil
     *            the SQL query manager
     * @param notification
     *            the notification
     */
    private void loadBroadcastEmailNotification( DAOUtil daoUtil, Notification notification )
    {
        int nNotificationId = daoUtil.getInt( COLUMN_BROADCAST_EMAIL_NOTIFICATION_ID );

        if ( nNotificationId != 0 )
        {
            BroadcastNotification broadcastEmailNotification = new BroadcastNotification( );

            broadcastEmailNotification.setSenderEmail( daoUtil.getString( COLUMN_BROADCAST_EMAIL_SENDER_EMAIL ) );
            broadcastEmailNotification.setSenderName( daoUtil.getString( COLUMN_BROADCAST_EMAIL_SENDER_NAME ) );
            broadcastEmailNotification.setSubject( daoUtil.getString( COLUMN_BROADCAST_EMAIL_SUBJECT ) );
            broadcastEmailNotification.setMessage( daoUtil.getString( COLUMN_BROADCAST_EMAIL_MESSAGE ) );
            broadcastEmailNotification.setRecipient( string2Emails( daoUtil.getString( COLUMN_BROADCAST_EMAIL_RECIPIENTS ) ) );
            broadcastEmailNotification.setCc( string2Emails( daoUtil.getString( COLUMN_BROADCAST_EMAIL_COPIES ) ) );
            broadcastEmailNotification.setBcc( string2Emails( daoUtil.getString( COLUMN_BROADCAST_EMAIL_BLIND_COPIES ) ) );

            notification.addBroadcastEmail( broadcastEmailNotification );
        }
    }

    /**
     * Inserts the backoffice notification of the specified notification
     * 
     * @param notification
     *            the notification
     */
    private void insertBackofficeNotification( Notification notification )
    {
        BackofficeNotification backofficeNotification = notification.getBackofficeNotification( );

        if ( backofficeNotification != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_BACKOFFICE, GruStorageDbPlugin.getPlugin( ) );

            int nIndex = 1;

            daoUtil.setInt( nIndex++, notification.getId( ) );
            daoUtil.setString( nIndex++, backofficeNotification.getMessage( ) );
            daoUtil.setString( nIndex++, backofficeNotification.getStatusText( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Inserts the SMS notification of the specified notification
     * 
     * @param notification
     *            the notification
     */
    private void insertSmsNotification( Notification notification )
    {
        SMSNotification smsNotification = notification.getSmsNotification( );

        if ( smsNotification != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_SMS, GruStorageDbPlugin.getPlugin( ) );

            int nIndex = 1;

            daoUtil.setInt( nIndex++, notification.getId( ) );
            daoUtil.setString( nIndex++, smsNotification.getMessage( ) );
            daoUtil.setString( nIndex++, smsNotification.getPhoneNumber( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Inserts the customer email notification of the specified notification
     * 
     * @param notification
     *            the notification
     */
    private void insertCustomerEmailNotification( Notification notification )
    {
        EmailNotification customerEmailNotification = notification.getEmailNotification( );

        if ( customerEmailNotification != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CUSTOMER_EMAIL, GruStorageDbPlugin.getPlugin( ) );

            int nIndex = 1;

            daoUtil.setInt( nIndex++, notification.getId( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getSenderEmail( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getSenderName( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getSubject( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getMessage( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getRecipient( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getCc( ) );
            daoUtil.setString( nIndex++, customerEmailNotification.getCci( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Inserts the MyDasboard notification of the specified notification
     * 
     * @param notification
     *            the notification
     */
    private void insertMyDashboardNotification( Notification notification )
    {

        MyDashboardNotification myDashboardNotification = notification.getMyDashboardNotification( );

        if ( myDashboardNotification != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_MYDASHBOARD, GruStorageDbPlugin.getPlugin( ) );

            int nIndex = 1;

            daoUtil.setInt( nIndex++, notification.getId( ) );
            daoUtil.setInt( nIndex++, myDashboardNotification.getStatusId( ) );
            daoUtil.setString( nIndex++, myDashboardNotification.getStatusText( ) );
            daoUtil.setString( nIndex++, myDashboardNotification.getMessage( ) );
            daoUtil.setString( nIndex++, myDashboardNotification.getSubject( ) );
            daoUtil.setString( nIndex++, myDashboardNotification.getData( ) );
            daoUtil.setString( nIndex++, myDashboardNotification.getSenderName( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Inserts the broadcast email notifications of the specified notification
     * 
     * @param notification
     *            the notification
     */
    private void insertBroadcastEmailNotification( Notification notification )
    {
        List<BroadcastNotification> listBroadcastdEmailNotifications = notification.getBroadcastEmail( );

        if ( ( listBroadcastdEmailNotifications != null ) && !listBroadcastdEmailNotifications.isEmpty( ) )
        {
            StringBuilder sbSqlQuery = new StringBuilder( );

            sbSqlQuery.append( SQL_QUERY_INSERT_BROADCAST_EMAIL );
            for ( int i = 1; i < notification.getBroadcastEmail( ).size( ); i++ )
            {
                sbSqlQuery.append( SQL_QUERY_INSERT_BROADCAST_EMAIL_MORE_VALUES );
            }

            DAOUtil daoUtil = new DAOUtil( sbSqlQuery.toString( ), GruStorageDbPlugin.getPlugin( ) );

            int nIndex = 1;

            for ( BroadcastNotification broadcastEmailNotification : notification.getBroadcastEmail( ) )
            {
                daoUtil.setInt( nIndex++, notification.getId( ) );
                daoUtil.setString( nIndex++, broadcastEmailNotification.getSenderEmail( ) );
                daoUtil.setString( nIndex++, broadcastEmailNotification.getSenderName( ) );
                daoUtil.setString( nIndex++, broadcastEmailNotification.getSubject( ) );
                daoUtil.setString( nIndex++, broadcastEmailNotification.getMessage( ) );
                daoUtil.setString( nIndex++, emails2String( broadcastEmailNotification.getRecipient( ) ) );
                daoUtil.setString( nIndex++, emails2String( broadcastEmailNotification.getCc( ) ) );
                daoUtil.setString( nIndex++, emails2String( broadcastEmailNotification.getBcc( ) ) );
            }

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * Converts a String into a list of EmailAddress objects
     * 
     * @param strEmails
     *            the String representation of emails (semi-colon-separated list)
     * @return the list of AddressEmail objects
     */
    private List<EmailAddress> string2Emails( String strEmails )
    {
        List<EmailAddress> listEmailAddresses = new ArrayList<EmailAddress>( );

        if ( StringUtils.isNotEmpty( strEmails ) )
        {
            List<String> listEmails = Arrays.asList( strEmails.split( EMAIL_SEPARATOR ) );

            for ( String strEmail : listEmails )
            {
                EmailAddress emailAddress = new EmailAddress( );

                emailAddress.setAddress( strEmail );

                listEmailAddresses.add( emailAddress );
            }
        }

        return listEmailAddresses;
    }

    /**
     * Converts a list of EmailAddress objects into a String (semi-colon-separated list)
     * 
     * @param listEmails
     *            the list of AddressEmail objects
     * @return the String representation of emails (semi-colon-separated list)
     */
    private String emails2String( List<EmailAddress> listEmails )
    {
        StringBuilder sbEmails = new StringBuilder( );

        if ( listEmails != null && !listEmails.isEmpty( ) )
        {
            for ( EmailAddress emailAddress : listEmails )
            {
                sbEmails.append( emailAddress.getAddress( ) ).append( EMAIL_SEPARATOR );
            }

            if ( sbEmails.length( ) != 0 )
            {
                sbEmails.deleteCharAt( sbEmails.length( ) - 1 );
            }
        }

        return sbEmails.toString( );
    }
}
