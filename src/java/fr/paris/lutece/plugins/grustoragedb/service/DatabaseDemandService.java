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

import fr.paris.lutece.plugins.gru.business.demand.BaseDemand;
import fr.paris.lutece.plugins.gru.business.demand.Demand;
import fr.paris.lutece.plugins.gru.business.demand.Notification;
import fr.paris.lutece.plugins.gru.service.demand.IDemandService;
import fr.paris.lutece.plugins.gru.service.demand.NotificationService;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemand;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemandHome;
import fr.paris.lutece.plugins.grustoragedb.business.DbNotification;
import fr.paris.lutece.plugins.grustoragedb.business.DbNotificationHome;
import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.ArrayList;
import java.util.List;


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
        demand.setStatus( dbd.getDemandState(  ) );
        demand.setReference( dbd.getReference(  ) );

        List<DbNotification> listDbNotifications = DbNotificationHome.findByDemand( dbd.getId(  ) );

        for ( DbNotification dbn : listDbNotifications )
        {
            Notification notification = NotificationService.parseJSON( dbn.getJson(  ) );
            demand.addNotification( notification );
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
            demand.setStatus( dbd.getDemandState(  ) );
            listBaseDemands.add( demand );
        }

        return listBaseDemands;
    }
}
