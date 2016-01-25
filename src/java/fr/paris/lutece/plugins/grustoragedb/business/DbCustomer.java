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

import org.hibernate.validator.constraints.*;

import java.io.Serializable;

import javax.validation.constraints.*;


/**
 * This is the business class for the object DbCustomer
 */
public class DbCustomer implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    @NotEmpty( message = "#i18n{grustoragedb.validation.customer.CustomerId.notEmpty}" )
    @Size( max = 50, message = "#i18n{grustoragedb.validation.customer.CustomerId.size}" )
    private String _strCustomerId;
    @Size( max = 50, message = "#i18n{grustoragedb.validation.customer.Guid.size}" )
    private String _strGuid;
    @Email( message = "#i18n{portal.validation.message.email}" )
    @Size( max = 255, message = "#i18n{grustoragedb.validation.customer.CustomerEmail.size}" )
    private String _strCustomerEmail;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the CustomerId
     * @return The CustomerId
     */
    public String getCustomerId(  )
    {
        return _strCustomerId;
    }

    /**
     * Sets the CustomerId
     * @param strCustomerId The CustomerId
     */
    public void setCustomerId( String strCustomerId )
    {
        _strCustomerId = strCustomerId;
    }

    /**
     * Returns the Guid
     * @return The Guid
     */
    public String getGuid(  )
    {
        return _strGuid;
    }

    /**
     * Sets the Guid
     * @param strGuid The Guid
     */
    public void setGuid( String strGuid )
    {
        _strGuid = strGuid;
    }

    /**
     * Returns the CustomerEmail
     * @return The CustomerEmail
     */
    public String getCustomerEmail(  )
    {
        return _strCustomerEmail;
    }

    /**
     * Sets the CustomerEmail
     * @param strCustomerEmail The CustomerEmail
     */
    public void setCustomerEmail( String strCustomerEmail )
    {
        _strCustomerEmail = strCustomerEmail;
    }
}
