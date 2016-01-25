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

import fr.paris.lutece.plugins.grustoragedb.business.DbCustomer;
import fr.paris.lutece.plugins.grustoragedb.business.DbCustomerHome;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemand;
import fr.paris.lutece.plugins.grustoragedb.business.DbDemandHome;
import fr.paris.lutece.plugins.grustoragedb.business.DbNotification;
import fr.paris.lutece.plugins.grusupply.business.Customer;
import fr.paris.lutece.plugins.grusupply.business.Demand;
import fr.paris.lutece.plugins.grusupply.business.Notification;
import fr.paris.lutece.plugins.grusupply.service.INotificationStorageService;


/**
 * Database Notification Storage Service
 */
public class DatabaseNotificationStorageService implements INotificationStorageService
{
    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Customer customer )
    {
        boolean bCreate = false;
        int nCustomerId = customer.getCustomerId(  );
        DbCustomer dbc = DbCustomerHome.findByPrimaryKey( nCustomerId );

        if ( dbc == null )
        {
            bCreate = true;
            dbc = new DbCustomer(  );
        }

        dbc.setCustomerEmail( customer.getEmail(  ) );
        dbc.setGuid( customer.getGUID(  ) );

        if ( bCreate )
        {
            DbCustomerHome.create( dbc );
        }
        else
        {
            DbCustomerHome.update( dbc );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Demand demand )
    {
        boolean bCreate = false;
        String strDemandId = String.valueOf( demand.getDemandId(  ) );
        String strDemandTypeId = String.valueOf( demand.getDemandIdType(  ) );

        DbDemand dbd = DbDemandHome.findByIdAndType( strDemandId, strDemandTypeId );

        if ( dbd == null )
        {
            bCreate = true;
            dbd = new DbDemand(  );
        }

        // dbd.setCustomerId( ... ); FIXME
        dbd.setDemandId( strDemandId );
        dbd.setDemandTypeId( strDemandTypeId );
        dbd.setDemandState( demand.getDemandState(  ) );
        dbd.setMaxSteps( demand.getDemandMaxStep(  ) );
        dbd.setCurrentStep( demand.getDemandUserCurrentStep(  ) );

        if ( bCreate )
        {
            DbDemandHome.create( dbd );
        }
        else
        {
            DbDemandHome.update( dbd );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Notification notification )
    {
        DbNotification dbn = new DbNotification(  );
        String strDemandId = String.valueOf( notification.getDemandId(  ) );
        String strDemandTypeId = String.valueOf( notification.getDemandIdType(  ) );
        DbDemand dbd = DbDemandHome.findByIdAndType( strDemandId, strDemandTypeId );
        dbn.setIdDemand( dbd.getId(  ) );

        // dbn.setJson( ... );  FIXME
    }
}
