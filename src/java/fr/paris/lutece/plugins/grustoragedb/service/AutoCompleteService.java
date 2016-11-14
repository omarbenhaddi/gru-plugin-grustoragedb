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

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grustoragedb.service.search.SearchService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;


/**
 * AutoCompleteService
 */
public final class AutoCompleteService
{
    private static final int INDENT = 4;

    /** Private constructor */
    private AutoCompleteService(  )
    {
    }

    /**
     * Returns a json string for autocomplete purpose
     * @param strQuery The query
     * @return The JSON
     */
    public static String getJson( String strQuery )
    {
        String[] terms = strQuery.split( " " );
        StringBuilder sbSearchQuery = new StringBuilder(  );

        for ( int i = 0; i < terms.length; i++ )
        {
            if ( i > 0 )
            {
                sbSearchQuery.append( ' ' );
            }

            sbSearchQuery.append( terms[i] ).append( '*' );
        }

        List<Customer> listCustomers = SearchService.searchCustomer( sbSearchQuery.toString(  ) );
        JSONObject json = new JSONObject(  );

        JSONArray jsonAutocomplete = new JSONArray(  );

        for ( Customer customer : listCustomers )
        {
            JSONObject jsonItem = new JSONObject(  );
            JSONObject jsonItemContent = new JSONObject(  );

            jsonItemContent.accumulate( "first_name", customer.getFirstname(  ) );
            jsonItemContent.accumulate( "last_name", customer.getLastname(  ) );
            jsonItem.accumulate( "item", jsonItemContent );
            jsonAutocomplete.add( jsonItem );
        }

        json.accumulate( "autocomplete", jsonAutocomplete );

        return json.toString( INDENT );
    }
}
