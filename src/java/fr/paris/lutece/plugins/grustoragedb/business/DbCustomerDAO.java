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
 * This class provides Data Access methods for DbCustomer objects
 */
public final class DbCustomerDAO implements IDbCustomerDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_customer ) FROM grustoragedb_customer";
    private static final String SQL_QUERY_SELECT = "SELECT id_customer, customer_id, customer_email FROM grustoragedb_customer WHERE id_customer = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO grustoragedb_customer ( id_customer, customer_id, customer_email ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM grustoragedb_customer WHERE id_customer = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE grustoragedb_customer SET id_customer = ?, customer_id = ?, customer_email = ? WHERE id_customer = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_customer, customer_id, customer_email FROM grustoragedb_customer";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_customer FROM grustoragedb_customer";

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
    public void insert( DbCustomer customer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        customer.setId( newPrimaryKey( plugin ) );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, customer.getId(  ) );

        daoUtil.setString( nIndex++, customer.getCustomerId(  ) );
        daoUtil.setString( nIndex++, customer.getCustomerEmail(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DbCustomer load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        DbCustomer customer = null;

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            customer = new DbCustomer(  );
            customer.setId( daoUtil.getInt( nIndex++ ) );
            customer.setCustomerId( daoUtil.getString( nIndex++ ) );
            customer.setCustomerEmail( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free(  );

        return customer;
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
    public void store( DbCustomer customer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;
        daoUtil.setInt( nIndex++, customer.getId(  ) );
        daoUtil.setString( nIndex++, customer.getCustomerId(  ) );
        daoUtil.setString( nIndex++, customer.getCustomerEmail(  ) );
        daoUtil.setInt( nIndex, customer.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<DbCustomer> selectCustomersList( Plugin plugin )
    {
        List<DbCustomer> customerList = new ArrayList<DbCustomer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DbCustomer customer = new DbCustomer(  );
            int nIndex = 1;
            customer.setId( daoUtil.getInt( nIndex++ ) );
            customer.setCustomerId( daoUtil.getString( nIndex++ ) );
            customer.setCustomerEmail( daoUtil.getString( nIndex++ ) );

            customerList.add( customer );
        }

        daoUtil.free(  );

        return customerList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdCustomersList( Plugin plugin )
    {
        List<Integer> customerList = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            customerList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return customerList;
    }
}
