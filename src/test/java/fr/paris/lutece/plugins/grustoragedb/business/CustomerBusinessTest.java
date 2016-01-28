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


public class CustomerBusinessTest extends LuteceTestCase
{
    private final static String CUSTOMERID1 = "CustomerId1";
    private final static String CUSTOMERID2 = "CustomerId2";
    private final static String CUSTOMEREMAIL1 = "CustomerEmail1";
    private final static String CUSTOMEREMAIL2 = "CustomerEmail2";

    public void testBusiness(  )
    {
        // Initialize an object
        DbCustomer customer = new DbCustomer(  );
        customer.setCustomerId( CUSTOMERID1 );
        customer.setCustomerEmail( CUSTOMEREMAIL1 );

        // Create test
        DbCustomerHome.create( customer );

        DbCustomer customerStored = DbCustomerHome.findByPrimaryKey( customer.getId(  ) );
        assertEquals( customerStored.getCustomerId(  ), customer.getCustomerId(  ) );
        assertEquals( customerStored.getCustomerEmail(  ), customer.getCustomerEmail(  ) );

        // Update test
        customer.setCustomerId( CUSTOMERID2 );
        customer.setCustomerEmail( CUSTOMEREMAIL2 );
        DbCustomerHome.update( customer );
        customerStored = DbCustomerHome.findByPrimaryKey( customer.getId(  ) );
        assertEquals( customerStored.getCustomerId(  ), customer.getCustomerId(  ) );
        assertEquals( customerStored.getCustomerEmail(  ), customer.getCustomerEmail(  ) );

        // List test
        DbCustomerHome.getCustomersList(  );

        // Delete test
        DbCustomerHome.remove( customer.getId(  ) );
        customerStored = DbCustomerHome.findByPrimaryKey( customer.getId(  ) );
        assertNull( customerStored );
    }
}
