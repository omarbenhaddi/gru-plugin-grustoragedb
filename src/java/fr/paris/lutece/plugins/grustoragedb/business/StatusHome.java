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

import fr.paris.lutece.plugins.grustoragedb.service.GruStorageDbPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for Status objects
 */
public final class StatusHome
{
    // Static variable pointed at the DAO instance
    private static IStatusDAO _dao    = SpringContextService.getBean( "grustoragedb.statusDAO" );
    private static Plugin     _plugin = GruStorageDbPlugin.getPlugin( );

    /**
     * Private constructor - this class need not be instantiated
     */
    private StatusHome( )
    {
    }

    /**
     * Create an instance of the status class
     * 
     * @param status
     *            The instance of the Status which contains the informations to store
     * @return The instance of status which has been created with its primary key.
     */
    public static Status create( Status status )
    {
        _dao.insert( status, _plugin );

        return status;
    }

    /**
     * Update of the status which is specified in parameter
     * 
     * @param status
     *            The instance of the Status which contains the data to store
     * @return The instance of the status which has been updated
     */
    public static Status update( Status status )
    {
        _dao.store( status, _plugin );

        return status;
    }

    /**
     * Remove the status whose identifier is specified in parameter
     * 
     * @param nKey
     *            The status Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a status whose identifier is specified in parameter
     * 
     * @param nKey
     *            The status primary key
     * @return an instance of Status
     */
    public static Optional<Status> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }
    
    /**
     * Returns an instance of a status
     * 
     * @param strStatusName
     *            The status name
     * @return an instance of Status
     */
    public static Optional<Status> findByStatusName( String strStatusName )
    {
        return _dao.loadByStatusName( strStatusName, _plugin );
    }

    /**
     * Load the data of all the status objects and returns them as a list
     * 
     * @return the list which contains the data of all the status objects
     */
    public static List<Status> getStatussList( )
    {
        return _dao.selectStatussList( _plugin );
    }

    /**
     * Load the id of all the status objects and returns them as a list
     * 
     * @return the list which contains the id of all the status objects
     */
    public static List<Integer> getIdStatussList( )
    {
        return _dao.selectIdStatussList( _plugin );
    }

    /**
     * Load the data of all the status objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the status objects
     */
    public static ReferenceList getStatussReferenceList( )
    {
        return _dao.selectStatussReferenceList( _plugin );
    }

    /**
     * Load the data of all the avant objects and returns them as a list
     * 
     * @param listIds
     *            liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<Status> getStatussListByIds( List<Integer> listIds )
    {
        return _dao.selectStatussListByIds( _plugin, listIds );
    }

}
