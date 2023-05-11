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

package fr.paris.lutece.plugins.grustoragedb.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides Data Access methods for Status objects
 */
public final class StatusDAO implements IStatusDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT                = "SELECT id_status, status_name, label, color_code FROM grustoragedb_status WHERE id_status = ?";
    private static final String SQL_QUERY_INSERT                = "INSERT INTO grustoragedb_status ( status_name, label, color_code ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE                = "DELETE FROM grustoragedb_status WHERE id_status = ? ";
    private static final String SQL_QUERY_UPDATE                = "UPDATE grustoragedb_status SET status_name = ?, label = ?, color_code = ? WHERE id_status = ?";
    private static final String SQL_QUERY_SELECTALL             = "SELECT id_status, status_name, label, color_code FROM grustoragedb_status";
    private static final String SQL_QUERY_SELECTALL_ID          = "SELECT id_status FROM grustoragedb_status";
    private static final String SQL_QUERY_SELECTALL_BY_IDS      = "SELECT id_status, status_name, label, color_code FROM grustoragedb_status WHERE id_status IN (  ";
    private static final String SQL_QUERY_SELECT_BY_STATUS_NAME = "SELECT id_status, status_name, label, color_code FROM grustoragedb_status WHERE ucase(status_name) = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Status status, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, status.getStatusName( ) );
            daoUtil.setString( nIndex++, status.getLabel( ) );
            daoUtil.setString( nIndex++, status.getColorCode( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                status.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<Status> load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            Status status = null;

            if ( daoUtil.next( ) )
            {
                status = new Status( );
                int nIndex = 1;

                status.setId( daoUtil.getInt( nIndex++ ) );
                status.setStatusName( daoUtil.getString( nIndex++ ) );
                status.setLabel( daoUtil.getString( nIndex++ ) );
                status.setColorCode( daoUtil.getString( nIndex ) );
            }

            return Optional.ofNullable( status );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Status status, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setString( nIndex++, status.getStatusName( ) );
            daoUtil.setString( nIndex++, status.getLabel( ) );
            daoUtil.setString( nIndex++, status.getColorCode( ) );
            daoUtil.setInt( nIndex, status.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Status> selectStatussList( Plugin plugin )
    {
        List<Status> statusList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Status status = new Status( );
                int nIndex = 1;

                status.setId( daoUtil.getInt( nIndex++ ) );
                status.setStatusName( daoUtil.getString( nIndex++ ) );
                status.setLabel( daoUtil.getString( nIndex++ ) );
                status.setColorCode( daoUtil.getString( nIndex ) );

                statusList.add( status );
            }

            return statusList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdStatussList( Plugin plugin )
    {
        List<Integer> statusList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                statusList.add( daoUtil.getInt( 1 ) );
            }

            return statusList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectStatussReferenceList( Plugin plugin )
    {
        ReferenceList statusList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                statusList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

            return statusList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Status> selectStatussListByIds( Plugin plugin, List<Integer> listIds )
    {
        List<Status> statusList = new ArrayList<>( );

        StringBuilder builder = new StringBuilder( );

        if ( !listIds.isEmpty( ) )
        {
            for ( int i = 0; i < listIds.size( ); i++ )
            {
                builder.append( "?," );
            }

            String placeHolders = builder.deleteCharAt( builder.length( ) - 1 ).toString( );
            String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";

            try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
            {
                int index = 1;
                for ( Integer n : listIds )
                {
                    daoUtil.setInt( index++, n );
                }

                daoUtil.executeQuery( );
                while ( daoUtil.next( ) )
                {
                    Status status = new Status( );
                    int nIndex = 1;

                    status.setId( daoUtil.getInt( nIndex++ ) );
                    status.setStatusName( daoUtil.getString( nIndex++ ) );
                    status.setLabel( daoUtil.getString( nIndex++ ) );
                    status.setColorCode( daoUtil.getString( nIndex ) );

                    statusList.add( status );
                }

                daoUtil.free( );

            }
        }
        return statusList;

    }

    @Override
    public Optional<Status> loadByStatusName( String strStatusName, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_STATUS_NAME, plugin ) )
        {
            daoUtil.setString( 1, strStatusName.toLowerCase( ) );
            daoUtil.executeQuery( );
            Status status = null;

            if ( daoUtil.next( ) )
            {
                status = new Status( );
                int nIndex = 1;

                status.setId( daoUtil.getInt( nIndex++ ) );
                status.setStatusName( daoUtil.getString( nIndex++ ) );
                status.setLabel( daoUtil.getString( nIndex++ ) );
                status.setColorCode( daoUtil.getString( nIndex ) );
            }

            return Optional.ofNullable( status );
        }
    }
}
