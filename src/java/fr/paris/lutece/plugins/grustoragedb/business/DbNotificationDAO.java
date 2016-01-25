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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for DbNotification objects
 */
public final class DbNotificationDAO implements IDbNotificationDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_notification ) FROM grustoragedb_notification";
    private static final String SQL_QUERY_SELECT = "SELECT id_notification, id_demand, json FROM grustoragedb_notification WHERE id_notification = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO grustoragedb_notification ( id_notification, id_demand, json ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM grustoragedb_notification WHERE id_notification = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE grustoragedb_notification SET id_notification = ?, id_demand = ?, json = ? WHERE id_notification = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_notification, id_demand, json FROM grustoragedb_notification";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_notification FROM grustoragedb_notification";
    private static final String SQL_QUERY_SELECT_BY_DEMAND = "SELECT id_notification, id_demand, json FROM grustoragedb_notification WHERE id_demand = ?";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DbNotification notification, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        notification.setId( newPrimaryKey( plugin ) );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, notification.getId(  ) );

        daoUtil.setInt( nIndex++, notification.getIdDemand(  ) );
        daoUtil.setString( nIndex++, notification.getJson(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DbNotification load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        DbNotification notification = null;

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            notification = new DbNotification(  );
            notification.setId( daoUtil.getInt( nIndex++ ) );
            notification.setIdDemand( daoUtil.getInt( nIndex++ ) );
            notification.setJson( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free(  );

        return notification;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DbNotification notification, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, notification.getId(  ) );
        daoUtil.setInt( nIndex++, notification.getIdDemand(  ) );
        daoUtil.setString( nIndex++, notification.getJson(  ) );
        daoUtil.setInt( nIndex, notification.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DbNotification> selectNotificationsList( Plugin plugin )
    {
        List<DbNotification> notificationList = new ArrayList<DbNotification>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DbNotification notification = new DbNotification(  );
            int nIndex = 1;
            notification.setId( daoUtil.getInt( nIndex++ ) );
            notification.setIdDemand( daoUtil.getInt( nIndex++ ) );
            notification.setJson( daoUtil.getString( nIndex++ ) );

            notificationList.add( notification );
        }

        daoUtil.free(  );

        return notificationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdNotificationsList( Plugin plugin )
    {
        List<Integer> notificationList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            notificationList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return notificationList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DbNotification> selectByDemand( int nDemandId, Plugin plugin )
    {
        List<DbNotification> notificationList = new ArrayList<DbNotification>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_DEMAND, plugin );
        daoUtil.setInt( 1, nDemandId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DbNotification notification = new DbNotification(  );
            int nIndex = 1;
            notification.setId( daoUtil.getInt( nIndex++ ) );
            notification.setIdDemand( daoUtil.getInt( nIndex++ ) );
            notification.setJson( daoUtil.getString( nIndex++ ) );

            notificationList.add( notification );
        }

        daoUtil.free(  );

        return notificationList;
    }
}
