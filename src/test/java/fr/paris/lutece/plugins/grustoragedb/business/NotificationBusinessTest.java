/*
 * Copyright (c) 2002-2015, Mairie de Paris
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

import fr.paris.lutece.test.LuteceTestCase;


public class NotificationBusinessTest extends LuteceTestCase
{
    private final static int IDDEMAND1 = 1;
    private final static int IDDEMAND2 = 2;
    private final static String JSON1 = "Json1";
    private final static String JSON2 = "Json2";

    public void testBusiness(  )
    {
        // Initialize an object
        DbNotification notification = new DbNotification(  );
        notification.setIdDemand( IDDEMAND1 );
        notification.setJson( JSON1 );

        // Create test
        DbNotificationHome.create( notification );

        DbNotification notificationStored = DbNotificationHome.findByPrimaryKey( notification.getId(  ) );
        assertEquals( notificationStored.getIdDemand(  ), notification.getIdDemand(  ) );
        assertEquals( notificationStored.getJson(  ), notification.getJson(  ) );

        // Update test
        notification.setIdDemand( IDDEMAND2 );
        notification.setJson( JSON2 );
        DbNotificationHome.update( notification );
        notificationStored = DbNotificationHome.findByPrimaryKey( notification.getId(  ) );
        assertEquals( notificationStored.getIdDemand(  ), notification.getIdDemand(  ) );
        assertEquals( notificationStored.getJson(  ), notification.getJson(  ) );

        // List test
        DbNotificationHome.getNotificationsList(  );

        // Delete test
        DbNotificationHome.remove( notification.getId(  ) );
        notificationStored = DbNotificationHome.findByPrimaryKey( notification.getId(  ) );
        assertNull( notificationStored );
    }
}
