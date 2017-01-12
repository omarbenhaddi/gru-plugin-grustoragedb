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

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.BackofficeNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.BroadcastNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.DashboardNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.EmailAddress;
import fr.paris.lutece.plugins.grubusiness.business.notification.EmailNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.INotificationDAO;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.grubusiness.business.notification.SMSNotification;
import fr.paris.lutece.test.LuteceTestCase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Test class for the NotificatinoDAO
 *
 */
public class NotificationDAOTest extends LuteceTestCase
{
    private static final String DEMAND_ID_1 = "1";
    private static final String DEMAND_ID_2 = "2";
    private static final String DEMAND_TYPE_ID_1 = "1";
    private static final String DEMAND_TYPE_ID_2 = "2";
    private static final String DEMAND_REFERENCE_1 = "DemandReference1";
    private static final String DEMAND_REFERENCE_2 = "DemandReference2";
    private static final int DEMAND_STATUS_ID_1 = 1;
    private static final int DEMAND_STATUS_ID_2 = 2;
    private static final long NOTIFICATION_DATE_1 = 1L;
    private static final long NOTIFICATION_DATE_2 = 2L;
    private static final long NOTIFICATION_DATE_3 = 3L;
    private static final long NOTIFICATION_DATE_4 = 4L;
    private static final long NOTIFICATION_DATE_5 = 5L;
    private static final long NOTIFICATION_DATE_6 = 6L;
    private static final String BACKOFFICE_NOTIFICATION_MESSAGE_1 = "BackofficeMessage1";
    private static final String BACKOFFICE_NOTIFICATION_STATUS_TEXT_1 = "BackofficeStatusText1";
    private static final String SMS_NOTIFICATION_MESSAGE_1 = "SmsMessage1";
    private static final String SMS_NOTIFICATION_PHONE_NUMBER_1 = "SmsPhoneNumber1";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_SENDER_EMAIL_1 = "CustomerEmailSenderEmail1";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_SENDER_NAME_1 = "CustomerEmailSenderName1";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_SUBJECT_1 = "CustomerEmailSubject1";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_MESSAGE_1 = "CustomerEmailMessage1";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_RECIPIENT_1 = "CustomerEmailRecipient1.1;EmailRecipient1.2";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_COPIES_1 = "CustomerEmailCopies1.1;EmailCopies1.2";
    private static final String CUSTOMER_EMAIL_NOTIFICATION_BLIND_COPIES_1 = "CustomerEmailBlindCopies1.1;EmailBlindCopies1.2";
    private static final int MYDASHBOARD_NOTIFICATION_STATUS_ID_1 = 1;
    private static final String MYDASHBOARD_NOTIFICATION_STATUS_TEXT_1 = "MyDashboardStatusText1";
    private static final String MYDASHBOARD_NOTIFICATION_MESSAGE_1 = "MyDashboardMessage1";
    private static final String MYDASHBOARD_NOTIFICATION_SUBJECT_1 = "MyDashboardSubject1";
    private static final String MYDASHBOARD_NOTIFICATION_DATA_1 = "MyDashboardData1";
    private static final String MYDASHBOARD_NOTIFICATION_SENDER_NAME_1 = "MyDashboardSenderName1";
    private static final String BROADCAST_EMAIL_NOTIFICATION_SENDER_EMAIL_1 = "BroadcastEmailSenderEmail1";
    private static final String BROADCAST_EMAIL_NOTIFICATION_SENDER_NAME_1 = "BroadcastEmailSenderName1";
    private static final String BROADCAST_EMAIL_NOTIFICATION_SUBJECT_1 = "BroadcastEmailSubject1";
    private static final String BROADCAST_EMAIL_NOTIFICATION_MESSAGE_1 = "BroadcastEmailMessage1";
    private static final String EMAIL_ADDRESS_RECIPIENT_1 = "EmailAddressRecipient1";
    private static final String EMAIL_ADDRESS_RECIPIENT_2 = "EmailAddressRecipient2";
    private static final String EMAIL_ADDRESS_COPY_1 = "EmailAddressRecipient1";
    private static final String EMAIL_ADDRESS_COPY_2 = "EmailAddressRecipient2";
    private static final String EMAIL_ADDRESS_BLIND_COPY_1 = "EmailAddressRecipient1";
    private static final String EMAIL_ADDRESS_BLIND_COPY_2 = "EmailAddressRecipient2";
    private final IDemandDAO _demandDAO;
    private final INotificationDAO _notificationDAO;

    /**
     * Constructor
     */
    public NotificationDAOTest(  )
    {
        _notificationDAO = new NotificationDAO(  );
        _demandDAO = new DemandDAO(  );
    }

    /**
     * Test case 1
     */
    public void test1Business(  )
    {
        // Initialize an object
        Demand demand = new Demand(  );
        demand.setId( DEMAND_ID_1 );
        demand.setTypeId( DEMAND_TYPE_ID_1 );
        demand.setReference( DEMAND_REFERENCE_1 );
        demand.setStatusId( DEMAND_STATUS_ID_1 );
        _demandDAO.insert( demand );

        Notification notification = new Notification(  );
        notification.setDemand( demand );
        notification.setNotificationDate( NOTIFICATION_DATE_1 );

        // Create test
        _notificationDAO.insert( notification );

        String strDemandId = notification.getDemand(  ).getId(  );
        String strDemandTypeId = notification.getDemand(  ).getTypeId(  );
        Collection<Notification> collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId,
                strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 1 ) );

        Iterator<Notification> iterator = collectionNotificationStored.iterator(  );
        Notification notificationStored = iterator.next(  );

        assertThat( notificationStored.getDemand(  ).getId(  ), is( notification.getDemand(  ).getId(  ) ) );
        assertThat( notificationStored.getDemand(  ).getTypeId(  ), is( notification.getDemand(  ).getTypeId(  ) ) );

        notification.setNotificationDate( NOTIFICATION_DATE_2 );

        BackofficeNotification backofficeNotification = new BackofficeNotification(  );
        backofficeNotification.setMessage( BACKOFFICE_NOTIFICATION_MESSAGE_1 );
        backofficeNotification.setStatusText( BACKOFFICE_NOTIFICATION_STATUS_TEXT_1 );
        notification.setBackofficeLogging( backofficeNotification );
        _notificationDAO.insert( notification );

        collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId, strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 2 ) );
        iterator = collectionNotificationStored.iterator(  );
        notificationStored = iterator.next(  );

        BackofficeNotification backofficeNotificationStored = notificationStored.getBackofficeLogging(  );
        assertThat( backofficeNotificationStored.getMessage(  ), is( backofficeNotification.getMessage(  ) ) );
        assertThat( backofficeNotificationStored.getStatusText(  ), is( backofficeNotification.getStatusText(  ) ) );

        notification.setNotificationDate( NOTIFICATION_DATE_3 );

        SMSNotification smsNotification = new SMSNotification(  );
        smsNotification.setMessage( SMS_NOTIFICATION_MESSAGE_1 );
        smsNotification.setPhoneNumber( SMS_NOTIFICATION_PHONE_NUMBER_1 );
        notification.setUserSMS( smsNotification );
        _notificationDAO.insert( notification );

        collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId, strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 3 ) );
        iterator = collectionNotificationStored.iterator(  );
        notificationStored = iterator.next(  );

        SMSNotification smsNotificationStored = notificationStored.getUserSMS(  );
        assertThat( smsNotificationStored.getMessage(  ), is( smsNotification.getMessage(  ) ) );
        assertThat( smsNotificationStored.getPhoneNumber(  ), is( smsNotification.getPhoneNumber(  ) ) );

        notification.setNotificationDate( NOTIFICATION_DATE_4 );

        EmailNotification emailNotification = new EmailNotification(  );
        emailNotification.setSenderEmail( CUSTOMER_EMAIL_NOTIFICATION_SENDER_EMAIL_1 );
        emailNotification.setSenderName( CUSTOMER_EMAIL_NOTIFICATION_SENDER_NAME_1 );
        emailNotification.setSubject( CUSTOMER_EMAIL_NOTIFICATION_SUBJECT_1 );
        emailNotification.setMessage( CUSTOMER_EMAIL_NOTIFICATION_MESSAGE_1 );
        emailNotification.setRecipient( CUSTOMER_EMAIL_NOTIFICATION_RECIPIENT_1 );
        emailNotification.setCc( CUSTOMER_EMAIL_NOTIFICATION_COPIES_1 );
        emailNotification.setBcc( CUSTOMER_EMAIL_NOTIFICATION_BLIND_COPIES_1 );
        notification.setUserEmail( emailNotification );
        _notificationDAO.insert( notification );

        collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId, strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 4 ) );
        iterator = collectionNotificationStored.iterator(  );
        notificationStored = iterator.next(  );

        EmailNotification emailNotificationStored = notificationStored.getUserEmail(  );
        assertThat( emailNotificationStored.getSenderEmail(  ), is( emailNotification.getSenderEmail(  ) ) );
        assertThat( emailNotificationStored.getSenderName(  ), is( emailNotification.getSenderName(  ) ) );
        assertThat( emailNotificationStored.getSubject(  ), is( emailNotification.getSubject(  ) ) );
        assertThat( emailNotificationStored.getMessage(  ), is( emailNotification.getMessage(  ) ) );
        assertThat( emailNotificationStored.getRecipient(  ), is( emailNotification.getRecipient(  ) ) );
        assertThat( emailNotificationStored.getCc(  ), is( emailNotification.getCc(  ) ) );
        assertThat( emailNotificationStored.getCci(  ), is( emailNotification.getCci(  ) ) );

        notification.setNotificationDate( NOTIFICATION_DATE_5 );

        DashboardNotification myDashboardNotification = new DashboardNotification(  );
        myDashboardNotification.setStatusId( MYDASHBOARD_NOTIFICATION_STATUS_ID_1 );
        myDashboardNotification.setStatusText( MYDASHBOARD_NOTIFICATION_STATUS_TEXT_1 );
        myDashboardNotification.setMessage( MYDASHBOARD_NOTIFICATION_MESSAGE_1 );
        myDashboardNotification.setSubject( MYDASHBOARD_NOTIFICATION_SUBJECT_1 );
        myDashboardNotification.setData( MYDASHBOARD_NOTIFICATION_DATA_1 );
        myDashboardNotification.setSenderName( MYDASHBOARD_NOTIFICATION_SENDER_NAME_1 );
        notification.setUserDashboard( myDashboardNotification );
        _notificationDAO.insert( notification );

        collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId, strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 5 ) );
        iterator = collectionNotificationStored.iterator(  );
        notificationStored = iterator.next(  );

        DashboardNotification myDashboardNotificationStored = notificationStored.getUserDashboard(  );
        assertThat( myDashboardNotificationStored.getStatusId(  ), is( myDashboardNotification.getStatusId(  ) ) );
        assertThat( myDashboardNotificationStored.getStatusText(  ), is( myDashboardNotification.getStatusText(  ) ) );
        assertThat( myDashboardNotificationStored.getMessage(  ), is( myDashboardNotification.getMessage(  ) ) );
        assertThat( myDashboardNotificationStored.getSubject(  ), is( myDashboardNotification.getSubject(  ) ) );
        assertThat( myDashboardNotificationStored.getData(  ), is( myDashboardNotification.getData(  ) ) );
        assertThat( myDashboardNotificationStored.getSenderName(  ), is( myDashboardNotification.getSenderName(  ) ) );

        notification.setNotificationDate( NOTIFICATION_DATE_6 );

        List<BroadcastNotification> listBroadcastNotifications = new ArrayList<BroadcastNotification>(  );
        BroadcastNotification broadcastNotification = new BroadcastNotification(  );
        broadcastNotification.setSenderEmail( BROADCAST_EMAIL_NOTIFICATION_SENDER_EMAIL_1 );
        broadcastNotification.setSenderName( BROADCAST_EMAIL_NOTIFICATION_SENDER_NAME_1 );
        broadcastNotification.setSubject( BROADCAST_EMAIL_NOTIFICATION_SUBJECT_1 );
        broadcastNotification.setMessage( BROADCAST_EMAIL_NOTIFICATION_MESSAGE_1 );

        List<EmailAddress> listEmailAddresses = new ArrayList<EmailAddress>(  );
        EmailAddress emailAddress = new EmailAddress(  );
        emailAddress.setAddress( EMAIL_ADDRESS_RECIPIENT_1 );
        listEmailAddresses.add( emailAddress );
        emailAddress = new EmailAddress(  );
        emailAddress.setAddress( EMAIL_ADDRESS_RECIPIENT_2 );
        listEmailAddresses.add( emailAddress );
        broadcastNotification.setRecipient( listEmailAddresses );
        listEmailAddresses = new ArrayList<EmailAddress>(  );
        emailAddress = new EmailAddress(  );
        emailAddress.setAddress( EMAIL_ADDRESS_COPY_1 );
        listEmailAddresses.add( emailAddress );
        emailAddress = new EmailAddress(  );
        emailAddress.setAddress( EMAIL_ADDRESS_COPY_2 );
        listEmailAddresses.add( emailAddress );
        broadcastNotification.setCc( listEmailAddresses );
        listEmailAddresses = new ArrayList<EmailAddress>(  );
        emailAddress = new EmailAddress(  );
        emailAddress.setAddress( EMAIL_ADDRESS_BLIND_COPY_1 );
        listEmailAddresses.add( emailAddress );
        emailAddress = new EmailAddress(  );
        emailAddress.setAddress( EMAIL_ADDRESS_BLIND_COPY_2 );
        listEmailAddresses.add( emailAddress );
        broadcastNotification.setBcc( listEmailAddresses );
        listBroadcastNotifications.add( broadcastNotification );
        notification.setBroadcastEmail( listBroadcastNotifications );
        _notificationDAO.insert( notification );

        collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId, strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 6 ) );
        iterator = collectionNotificationStored.iterator(  );
        notificationStored = iterator.next(  );

        List<BroadcastNotification> listBroadcastNotificationsStored = notificationStored.getBroadcastEmail(  );
        assertThat( listBroadcastNotificationsStored.size(  ), is( 1 ) );

        BroadcastNotification broadcastNotificationStored = listBroadcastNotificationsStored.get( 0 );
        assertThat( broadcastNotificationStored.getSenderEmail(  ), is( broadcastNotification.getSenderEmail(  ) ) );
        assertThat( broadcastNotificationStored.getSenderName(  ), is( broadcastNotification.getSenderName(  ) ) );
        assertThat( broadcastNotificationStored.getSubject(  ), is( broadcastNotification.getSubject(  ) ) );
        assertThat( broadcastNotificationStored.getMessage(  ), is( broadcastNotification.getMessage(  ) ) );

        List<EmailAddress> listEmailAddressesStored = broadcastNotificationStored.getRecipient(  );
        assertThat( listEmailAddressesStored.size(  ), is( 2 ) );
        listEmailAddressesStored = broadcastNotificationStored.getCc(  );
        assertThat( listEmailAddressesStored.size(  ), is( 2 ) );
        listEmailAddressesStored = broadcastNotificationStored.getBcc(  );
        assertThat( listEmailAddressesStored.size(  ), is( 2 ) );

        _notificationDAO.deleteByDemand( strDemandId, strDemandTypeId );
        collectionNotificationStored = _notificationDAO.loadByDemand( strDemandId, strDemandTypeId );
        assertThat( collectionNotificationStored.size(  ), is( 0 ) );

        _demandDAO.delete( strDemandId, strDemandTypeId );
    }

    /**
     * Test case 2
     */
    public void test2Business(  )
    {
        // Initialize an object
        Demand demand = new Demand(  );
        demand.setId( DEMAND_ID_1 );
        demand.setTypeId( DEMAND_TYPE_ID_1 );
        demand.setReference( DEMAND_REFERENCE_1 );
        demand.setStatusId( DEMAND_STATUS_ID_1 );
        _demandDAO.insert( demand );

        Demand demand2 = new Demand(  );
        demand2.setId( DEMAND_ID_2 );
        demand2.setTypeId( DEMAND_TYPE_ID_2 );
        demand2.setReference( DEMAND_REFERENCE_2 );
        demand2.setStatusId( DEMAND_STATUS_ID_2 );
        _demandDAO.insert( demand2 );

        Notification notification = new Notification(  );
        notification.setDemand( demand );
        _notificationDAO.insert( notification );

        notification = new Notification(  );
        notification.setDemand( demand2 );
        notification.setNotificationDate( NOTIFICATION_DATE_2 );
        _notificationDAO.insert( notification );

        Collection<Notification> collectionNotificationStored = _notificationDAO.loadByDemand( DEMAND_ID_1,
                DEMAND_TYPE_ID_1 );
        assertThat( collectionNotificationStored.size(  ), is( 1 ) );

        Iterator<Notification> iterator = collectionNotificationStored.iterator(  );
        Notification notificationStored = iterator.next(  );
        assertThat( notificationStored.getNotificationDate(  ), is( NOTIFICATION_DATE_1 ) );

        collectionNotificationStored = _notificationDAO.loadByDemand( DEMAND_ID_2, DEMAND_TYPE_ID_2 );
        assertThat( collectionNotificationStored.size(  ), is( 1 ) );
        iterator = collectionNotificationStored.iterator(  );
        notificationStored = iterator.next(  );
        assertThat( notificationStored.getNotificationDate(  ), is( NOTIFICATION_DATE_2 ) );

        _notificationDAO.deleteByDemand( DEMAND_ID_1, DEMAND_TYPE_ID_1 );
        collectionNotificationStored = _notificationDAO.loadByDemand( DEMAND_ID_1, DEMAND_TYPE_ID_1 );
        assertThat( collectionNotificationStored.size(  ), is( 0 ) );

        _notificationDAO.deleteByDemand( DEMAND_ID_2, DEMAND_TYPE_ID_2 );
        collectionNotificationStored = _notificationDAO.loadByDemand( DEMAND_ID_2, DEMAND_TYPE_ID_2 );
        assertThat( collectionNotificationStored.size(  ), is( 0 ) );

        _demandDAO.delete( DEMAND_ID_1, DEMAND_TYPE_ID_1 );
        _demandDAO.delete( DEMAND_ID_2, DEMAND_TYPE_ID_2 );
    }
}
