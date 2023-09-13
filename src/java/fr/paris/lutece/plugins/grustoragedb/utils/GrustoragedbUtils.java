/*
 * Copyright (c) 2002-2023, Mairie de Paris
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
package fr.paris.lutece.plugins.grustoragedb.utils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import fr.paris.lutece.plugins.grubusiness.business.web.rs.EnumGenericStatus;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * GrustoragedbUtils
 *
 */
public class GrustoragedbUtils
{
    public static final String PROPERTY_COMPRESS_NOTIFICATION   = "grustoragedb.notification.compress";
    public static final String CHARECTER_REGEXP_FILTER          = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\p{Sm}\\p{Sc}\\s]";
    
    /**
     * Private constructor
     */
    private GrustoragedbUtils ( )
    {
        
    }
    
    /**
     * InitMapper
     */
    public static ObjectMapper initMapper( )
    {
        ObjectMapper mapper = new ObjectMapper( );
        mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, false );
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        mapper.configure( SerializationFeature.WRAP_ROOT_VALUE, false );
        mapper.configure( Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );
        
        return mapper;
    }
    
    /**
     * Return an reference list of GenericStatus (Code = name of enum and Name = label of enum)
     * @return Return an reference list of GenericStatu
     */
    public static ReferenceList getEnumGenericStatusRefList ( )
    {
        ReferenceList refList = new ReferenceList( );
        
        for ( EnumGenericStatus status : EnumGenericStatus.values( ) )
        {
            refList.addItem( status.name( ), status.getLabel( ) );
        }        
        return refList;        
    }
    
}