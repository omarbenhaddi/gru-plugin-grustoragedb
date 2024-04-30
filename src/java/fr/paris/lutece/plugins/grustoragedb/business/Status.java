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

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
/**
 * This is the business class for the object Status
 */ 
public class Status implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    @NotEmpty( message = "#i18n{grusupply.validation.status.Status.notEmpty}" )
    private String _strStatus;
    
    private String _strCodeStatus;
    
    private String _strLabelColorCode;
    
    private String _strBannerColorCode;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
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
     * @return the _strStatus
     */
    public String getStatus( )
    {
        return _strStatus;
    }

    /**
     * @param strStatus the _strStatus to set
     */
    public void setStatus( String strStatus )
    {
        this._strStatus = strStatus;
    }

    /**
     * @return the _strCodeStatus
     */
    public String getCodeStatus( )
    {
        return _strCodeStatus;
    }

    /**
     * @param strCodeStatus the _strCodeStatus to set
     */
    public void setCodeStatus( String strCodeStatus )
    {
        this._strCodeStatus = strCodeStatus;
    }

    /**
     * @return the _strLabelColorCode
     */
    public String getLabelColorCode( )
    {
        return _strLabelColorCode;
    }

    /**
     * @param strLabelColorCode the _strLabelColorCode to set
     */
    public void setLabelColorCode( String strLabelColorCode )
    {
        this._strLabelColorCode = strLabelColorCode;
    }

    /**
     * @return the _strBannerColorCode
     */
    public String getBannerColorCode( )
    {
        return _strBannerColorCode;
    }

    /**
     * @param strBannerColorCode the _strBannerColorCode to set
     */
    public void setBannerColorCode( String strBannerColorCode )
    {
        this._strBannerColorCode = strBannerColorCode;
    }

}