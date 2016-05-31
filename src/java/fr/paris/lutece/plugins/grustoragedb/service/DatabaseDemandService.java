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
package fr.paris.lutece.plugins.grustoragedb.service;

import fr.paris.lutece.plugins.gru.business.demand.BackOfficeLogging;
import fr.paris.lutece.plugins.gru.business.demand.BaseDemand;
import fr.paris.lutece.plugins.gru.business.demand.Demand;
import fr.paris.lutece.plugins.gru.business.demand.Notification;
import fr.paris.lutece.plugins.gru.business.demand.UserDashboard;
import fr.paris.lutece.plugins.gru.service.demand.IDemandService;
import fr.paris.lutece.plugins.gru.service.demand.NotificationService;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemand;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemandHome;
import fr.paris.lutece.plugins.grustoragedb.business.DbNotification;
import fr.paris.lutece.plugins.grustoragedb.business.DbNotificationHome;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;


/**
 * DatabaseDemandService
 */
public class DatabaseDemandService implements IDemandService
{
    /**
     * {@inheritDoc }
     */
    @Override
    public Demand getDemand( String strDemandId, String strDemandTypeId, AdminUser user )
    {
        DbDemand dbd = DbDemandHome.findByIdAndType( strDemandId, strDemandTypeId );
        Demand demand = new Demand(  );
        demand.setId( strDemandId );
        demand.setDemandTypeId( strDemandTypeId );
        demand.setStatus( dbd.getDemandStatus(  ) );
        demand.setReference( dbd.getReference(  ) );
        

        List<DbNotification> listDbNotifications = DbNotificationHome.findByDemand( dbd.getId(  ) );

        long lFirstDate = 0;
        long lLastDate = 0;
        
        for ( DbNotification dbn : listDbNotifications )
        {
            Notification notification = NotificationService.parseJSON( dbn.getJson(  ) );
            demand.addNotification( notification );
            if( lFirstDate == 0 )
            {
                lFirstDate = notification.getTimestamp();
            }
            lLastDate = notification.getTimestamp();
                    
            UserDashboard ud = notification.getUserDashboard();
            if( ( ud != null ) && StringUtils.isNotBlank( ud.getStatusText() ))
            {
                demand.setCustomerStatus( ud.getStatusText() );
            }
            
            BackOfficeLogging bol = notification.getBackOfficeLogging();
            if( ( bol != null ) && StringUtils.isNotBlank( bol.getStatusText() ))
            {
                demand.setAgentStatus( bol.getStatusText() );
            }
            
        }
        
        
        if( demand.getStatus() == Demand.STATUS_CLOSED )
        {
            demand.setTimeOpenedInMs( lLastDate - lFirstDate );
        }
        else
        {
            demand.setTimeOpenedInMs( now() - lFirstDate );
        }
            
        
        return demand;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<BaseDemand> getDemands( String strCustomerId, AdminUser user )
    {
        List<DbDemand> listDemands = DbDemandHome.findByCustomer( strCustomerId );
        List<BaseDemand> listBaseDemands = new ArrayList<BaseDemand>(  );

        for ( DbDemand dbd : listDemands )
        {
            BaseDemand demand = new BaseDemand(  );
            demand.setId( dbd.getDemandId(  ) );
            demand.setDemandTypeId( dbd.getDemandTypeId(  ) );
            demand.setReference( dbd.getReference(  ) );
            demand.setStatus( dbd.getDemandStatus(  ) );
            if( demand.getStatus() == Demand.STATUS_CLOSED )
            {
                demand.setTimeOpenedInMs( dbd.getLastNotificationDate() - dbd.getFirstNotificationDate() );
            }
            else
            {
                demand.setTimeOpenedInMs( now() - dbd.getFirstNotificationDate() );
            }
            
            listBaseDemands.add( demand );
        }

        return listBaseDemands;
    }
    
    /**
     * Return the current time in ms
     * @return the current time
     */
    private long now()
    {
        return ( new Date() ).getTime();
    }
}
