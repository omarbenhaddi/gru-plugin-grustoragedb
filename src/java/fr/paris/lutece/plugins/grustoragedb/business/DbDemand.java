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

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.io.Serializable;



/**
 * This is the business class for the object DbDemand
 */
public class DbDemand implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    private String _strCustomerId;
    @NotEmpty( message = "#i18n{grustoragedb.validation.demand.DemandId.notEmpty}" )
    @Size( max = 50, message = "#i18n{grustoragedb.validation.demand.DemandId.size}" )
    private String _strDemandId;
    @NotEmpty( message = "#i18n{grustoragedb.validation.demand.DemandTypeId.notEmpty}" )
    @Size( max = 50, message = "#i18n{grustoragedb.validation.demand.DemandTypeId.size}" )
    private String _strDemandTypeId;
    private String _strReference;
    private int _nDemandStatus;
    private int _nMaxSteps;
    private int _nCurrentStep;
    private String _strStatusForCustomer;
    private String _strStatusForGRU;
    private long _lFirstNotificationDate; 
    private long _lLastNotificationDate; 

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Id
     *
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the CustomerId
     *
     * @return The CustomerId
     */
    public String getCustomerId(  )
    {
        return _strCustomerId;
    }

    /**
     * Sets the CustomerId
     *
     * @param strCustomerId The CustomerId
     */
    public void setCustomerId( String strCustomerId )
    {
        _strCustomerId = strCustomerId;
    }

    /**
     * Returns the DemandId
     *
     * @return The DemandId
     */
    public String getDemandId(  )
    {
        return _strDemandId;
    }

    /**
     * Sets the DemandId
     *
     * @param strDemandId The DemandId
     */
    public void setDemandId( String strDemandId )
    {
        _strDemandId = strDemandId;
    }

    /**
     * Returns the DemandTypeId
     *
     * @return The DemandTypeId
     */
    public String getDemandTypeId(  )
    {
        return _strDemandTypeId;
    }

    /**
     * Sets the DemandTypeId
     *
     * @param strDemandTypeId The DemandTypeId
     */
    public void setDemandTypeId( String strDemandTypeId )
    {
        _strDemandTypeId = strDemandTypeId;
    }

    /**
     * Returns the Reference
     *
     * @return The Reference
     */
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * Sets the Reference
     *
     * @param strReference The Reference
     */
    public void setReference( String strReference )
    {
        _strReference = strReference;
    }

    /**
     * Returns the DemandState
     *
     * @return The DemandState
     */
    public int getDemandStatus(  )
    {
        return _nDemandStatus;
    }

    /**
     * Sets the DemandState
     *
     * @param nDemandStatus The DemandState
     */
    public void setDemandStatus( int nDemandStatus )
    {
        _nDemandStatus = nDemandStatus;
    }

    /**
     * Returns the MaxSteps
     *
     * @return The MaxSteps
     */
    public int getMaxSteps(  )
    {
        return _nMaxSteps;
    }

    /**
     * Sets the MaxSteps
     *
     * @param nMaxSteps The MaxSteps
     */
    public void setMaxSteps( int nMaxSteps )
    {
        _nMaxSteps = nMaxSteps;
    }

    /**
     * Returns the CurrentStep
     *
     * @return The CurrentStep
     */
    public int getCurrentStep(  )
    {
        return _nCurrentStep;
    }

    /**
     * Sets the CurrentStep
     *
     * @param nCurrentStep The CurrentStep
     */
    public void setCurrentStep( int nCurrentStep )
    {
        _nCurrentStep = nCurrentStep;
    }

    /**
     * Returns the StatusForCustomer
     *
     * @return The StatusForCustomer
     */
    public String getStatusForCustomer(  )
    {
        return _strStatusForCustomer;
    }

    /**
     * Sets the StatusForCustomer
     *
     * @param strStatusForCustomer The StatusForCustomer
     */
    public void setStatusForCustomer( String strStatusForCustomer )
    {
        _strStatusForCustomer = strStatusForCustomer;
    }

    /**
     * Returns the StatusForGRU
     *
     * @return The StatusForGRU
     */
    public String getStatusForGRU(  )
    {
        return _strStatusForGRU;
    }

    /**
     * Sets the StatusForGRU
     *
     * @param strStatusForGRU The StatusForGRU
     */
    public void setStatusForGRU( String strStatusForGRU )
    {
        _strStatusForGRU = strStatusForGRU;
    }
    
      /**
        * Returns the FirstNotificationDate
        * @return The FirstNotificationDate
        */ 
    public long getFirstNotificationDate()
    {
        return _lFirstNotificationDate;
    }
    
       /**
        * Sets the FirstNotificationDate
        * @param lFirstNotificationDate The FirstNotificationDate
        */ 
    public void setFirstNotificationDate( long lFirstNotificationDate )
    {
        _lFirstNotificationDate = lFirstNotificationDate;
    }
    
       /**
        * Returns the LastNotificationDate
        * @return The LastNotificationDate
        */ 
    public long getLastNotificationDate()
    {
        return _lLastNotificationDate;
    }
    
       /**
        * Sets the LastNotificationDate
        * @param lLastNotificationDate The LastNotificationDate
        */ 
    public void setLastNotificationDate( long lLastNotificationDate )
    {
        _lLastNotificationDate = lLastNotificationDate;
    }    
}
