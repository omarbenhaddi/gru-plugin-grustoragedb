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

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandDAO;
import fr.paris.lutece.plugins.grustoragedb.service.GruStorageDbPlugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class provides Data Access methods for Demand objects stored in SQL database
 */
public final class DemandDAO implements IDemandDAO
{
    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE_ID = "type_id";
    private static final String COLUMN_REFERENCE = "reference";
    private static final String COLUMN_STATUS_ID = "status_id";
    private static final String COLUMN_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_CREATION_DATE = "creation_date";
    private static final String COLUMN_CLOSURE_DATE = "closure_date";
    private static final String COLUMN_MAX_STEPS = "max_steps";
    private static final String COLUMN_CURRENT_STEP = "current_step";

    // SQL queries
    private static final String SQL_QUERY_DEMAND_ALL_FIELDS = "id, type_id, reference, status_id, customer_id, creation_date, closure_date, max_steps, current_step";
    private static final String SQL_QUERY_DEMAND_SELECT = "SELECT " + SQL_QUERY_DEMAND_ALL_FIELDS + " FROM grustoragedb_demand WHERE id = ? AND type_id = ?";
    private static final String SQL_QUERY_DEMAND_SELECT_ALL = "SELECT " + SQL_QUERY_DEMAND_ALL_FIELDS + " FROM grustoragedb_demand";
    private static final String SQL_QUERY_DEMAND_INSERT = "INSERT INTO grustoragedb_demand ( " + SQL_QUERY_DEMAND_ALL_FIELDS
            + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DEMAND_UPDATE = "UPDATE grustoragedb_demand SET status_id = ?, customer_id = ?, closure_date = ?, current_step = ? WHERE id = ? AND type_id = ?";
    private static final String SQL_QUERY_DEMAND_DELETE = "DELETE FROM grustoragedb_demand WHERE id = ? AND type_id = ? ";
    private static final String SQL_QUERY_DEMAND_SELECT_BY_CUSTOMER_ID = "SELECT " + SQL_QUERY_DEMAND_ALL_FIELDS
            + " FROM grustoragedb_demand WHERE customer_id = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public Demand load( String strDemandId, String strDemandTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DEMAND_SELECT, GruStorageDbPlugin.getPlugin( ) );

        daoUtil.setString( 1, strDemandId );
        daoUtil.setString( 2, strDemandTypeId );
        daoUtil.executeQuery( );

        Demand demand = null;

        if ( daoUtil.next( ) )
        {
            demand = dao2Demand( daoUtil );
        }

        daoUtil.free( );

        return demand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Demand> loadByCustomerId( String strCustomerId )
    {
        Collection<Demand> collectionDemands = new ArrayList<Demand>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DEMAND_SELECT_BY_CUSTOMER_ID, GruStorageDbPlugin.getPlugin( ) );

        daoUtil.setString( 1, strCustomerId );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            collectionDemands.add( dao2Demand( daoUtil ) );
        }

        daoUtil.free( );

        return collectionDemands;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Demand> loadAllDemands( )
    {
        Collection<Demand> collectionDemands = new ArrayList<Demand>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DEMAND_SELECT_ALL, GruStorageDbPlugin.getPlugin( ) );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            collectionDemands.add( dao2Demand( daoUtil ) );
        }

        daoUtil.free( );

        return collectionDemands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Demand insert( Demand demand )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DEMAND_INSERT, GruStorageDbPlugin.getPlugin( ) );

        int nIndex = 1;

        daoUtil.setString( nIndex++, demand.getId( ) );
        daoUtil.setString( nIndex++, demand.getTypeId( ) );
        daoUtil.setString( nIndex++, demand.getReference( ) );
        daoUtil.setInt( nIndex++, demand.getStatusId( ) );
        daoUtil.setString( nIndex++, demand.getCustomer( ).getId( ) );
        daoUtil.setLong( nIndex++, demand.getCreationDate( ) );
        daoUtil.setLong( nIndex++, demand.getClosureDate( ) );
        daoUtil.setInt( nIndex++, demand.getMaxSteps( ) );
        daoUtil.setInt( nIndex++, demand.getCurrentStep( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return demand;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Demand store( Demand demand )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DEMAND_UPDATE, GruStorageDbPlugin.getPlugin( ) );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, demand.getStatusId( ) );
        daoUtil.setString( nIndex++, demand.getCustomer( ).getId( ) );
        daoUtil.setLong( nIndex++, demand.getClosureDate( ) );
        daoUtil.setInt( nIndex++, demand.getCurrentStep( ) );
        daoUtil.setString( nIndex++, demand.getId( ) );
        daoUtil.setString( nIndex++, demand.getTypeId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );

        return demand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strDemandId, String strDemandTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DEMAND_DELETE, GruStorageDbPlugin.getPlugin( ) );

        daoUtil.setString( 1, strDemandId );
        daoUtil.setString( 2, strDemandTypeId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Converts data from DAO to a Demand object
     * 
     * @param daoUtil
     *            the DAO containing the data to convert
     * @return a Demand object
     */
    private Demand dao2Demand( DAOUtil daoUtil )
    {
        Demand demand = new Demand( );

        demand.setId( daoUtil.getString( COLUMN_ID ) );
        demand.setTypeId( daoUtil.getString( COLUMN_TYPE_ID ) );
        demand.setStatusId( daoUtil.getInt( COLUMN_STATUS_ID ) );

        Customer customer = new Customer( );
        customer.setId( daoUtil.getString( COLUMN_CUSTOMER_ID ) );
        demand.setCustomer( customer );

        demand.setReference( daoUtil.getString( COLUMN_REFERENCE ) );
        demand.setCreationDate( daoUtil.getLong( COLUMN_CREATION_DATE ) );
        demand.setClosureDate( daoUtil.getLong( COLUMN_CLOSURE_DATE ) );
        demand.setMaxSteps( daoUtil.getInt( COLUMN_MAX_STEPS ) );
        demand.setCurrentStep( daoUtil.getInt( COLUMN_CURRENT_STEP ) );

        return demand;
    }

}
