/*
 * Copyright (c) 2002-2024, Mairie de Paris
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
package fr.paris.lutece.plugins.grustoragedb.rs.service;

import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.paris.lutece.plugins.grustoragedb.business.Status;
import fr.paris.lutece.plugins.grustoragedb.business.StatusHome;
import fr.paris.lutece.plugins.grustoragedb.utils.GrustoragedbConstants;
import fr.paris.lutece.plugins.grustoragedb.utils.GrustoragedbUtils;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

/**
 * 
 * StatusRest
 *
 */
@Path( RestConstants.BASE_PATH + GrustoragedbConstants.PATH_STATUS_REST )
public class StatusRest
{
    
    /**
     * Gets list status
     * @return list status
     */
    @GET( )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getListStatus( @HeaderParam( GrustoragedbConstants.CONSTANT_X_API_KEY ) String strApiKey )
    {
        if( GrustoragedbConstants.PROPERTY_STATUS_REST_API_KEY.equals( strApiKey ) )
        {
            return Response.status( Response.Status.OK )
                    .entity( JsonUtil.buildJsonResponse( new JsonResponse( StatusHome.getStatusList( ) ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.UNAUTHORIZED )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( GrustoragedbConstants.API_KEY_ERROR ) ) )
                .build( );
    }

    /**
     * Get status by id
     * @param nId
     * @return status by id
     */
    @GET
    @Path( GrustoragedbConstants.PATH_ID )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getStatus( @HeaderParam( GrustoragedbConstants.CONSTANT_X_API_KEY ) String strApiKey,
            @PathParam( GrustoragedbConstants.PATH_PARAM_ID ) int nId )
    {
        if( GrustoragedbConstants.PROPERTY_STATUS_REST_API_KEY.equals( strApiKey ) )
        {
            Optional<Status> status = StatusHome.findByPrimaryKey( nId );
    
            if ( status.isPresent( ) )
            {      
                return Response.status( Response.Status.OK )
                        .entity( JsonUtil.buildJsonResponse( new JsonResponse( status.get( ) ) ) )
                        .build( );
            }
    
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.getReasonPhrase( ) ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.UNAUTHORIZED )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( GrustoragedbConstants.API_KEY_ERROR ) ) )
                .build( );
    }

    /**
     * Create status
     * @param strStatus
     * @return created status
     */
    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response doCreateStatus( @HeaderParam( GrustoragedbConstants.CONSTANT_X_API_KEY ) String strApiKey, String strStatus )
    {
        if( GrustoragedbConstants.PROPERTY_STATUS_REST_API_KEY.equals( strApiKey ) )
        {
            try
            {
                Status status = GrustoragedbUtils.initMapper( ).readValue( strStatus, Status.class );
    
                if ( isStatusCompleted( status ) )
                {
                    StatusHome.create( status );
                    return Response.status( Response.Status.CREATED )
                            .entity( JsonUtil.buildJsonResponse( new JsonResponse( status ) ) )
                            .build( );
                }
                return Response.status( Response.Status.NOT_ACCEPTABLE )
                        .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_ACCEPTABLE.name( ), GrustoragedbConstants.ALL_REQUIRED ) ) )
                        .build( );
    
            } catch ( JsonProcessingException e )
            {
                return Response.status( Response.Status.BAD_REQUEST )
                        .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.getReasonPhrase( ) ) ) )
                        .build( );
            }
        }
        
        return Response.status( Response.Status.UNAUTHORIZED )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( GrustoragedbConstants.API_KEY_ERROR ) ) )
                .build( );
    }

    /**
     * Modify status
     * @param strStatus
     * @return modified status
     */
    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response doModifyStatus( @HeaderParam( GrustoragedbConstants.CONSTANT_X_API_KEY ) String strApiKey, String strStatus )
    {
        if( GrustoragedbConstants.PROPERTY_STATUS_REST_API_KEY.equals( strApiKey ) )
        {
            try
            {
                Status status = GrustoragedbUtils.initMapper( ).readValue( strStatus, Status.class );
    
                if ( isStatusCompleted( status ) && StatusHome.findByPrimaryKey( status.getId( ) ).isPresent( ) )
                {
                    StatusHome.update( status );
                    return Response.status( Response.Status.OK )
                            .entity( JsonUtil.buildJsonResponse( new JsonResponse( status ) ) )
                            .build( );
                }
                return Response.status( Response.Status.NOT_MODIFIED )
                        .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_MODIFIED.name( ), GrustoragedbConstants.ALL_REQUIRED ) ) )
                        .build( );
    
            } catch ( JsonProcessingException e )
            {
                return Response.status( Response.Status.BAD_REQUEST )
                        .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.getReasonPhrase( ) ) ) )
                        .build( );
            }
        }
        
        return Response.status( Response.Status.UNAUTHORIZED )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( GrustoragedbConstants.API_KEY_ERROR ) ) )
                .build( );
    }

    /**
     * Delete status
     * @param nId
     * @return ok if is deleted
     */
    @DELETE
    @Path( GrustoragedbConstants.PATH_ID )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response doDeleteStatus( @HeaderParam( GrustoragedbConstants.CONSTANT_X_API_KEY ) String strApiKey,
            @PathParam( GrustoragedbConstants.PATH_PARAM_ID ) int nId )
    {
        if( GrustoragedbConstants.PROPERTY_STATUS_REST_API_KEY.equals( strApiKey ) )
        {
            if ( StatusHome.findByPrimaryKey( nId ).isPresent( ) )
            {
                StatusHome.remove( nId );
                return Response.status( Response.Status.OK )
                        .entity( JsonUtil.buildJsonResponse( new JsonResponse( Response.Status.OK ) ) )
                        .build( );
            }
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.getReasonPhrase( ) ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.UNAUTHORIZED )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( GrustoragedbConstants.API_KEY_ERROR ) ) )
                .build( );      
    }
    
    /**
     * Gets list status
     * @return list status
     */
    @GET( )
    @Path( GrustoragedbConstants.PATH_GENERIC )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getListGenericStatus( @HeaderParam( GrustoragedbConstants.CONSTANT_X_API_KEY ) String strApiKey )
    {
        if( GrustoragedbConstants.PROPERTY_STATUS_REST_API_KEY.equals( strApiKey ) )
        {
            return Response.status( Response.Status.OK )
                    .entity( JsonUtil.buildJsonResponse( new JsonResponse( GrustoragedbUtils.getEnumGenericStatusRefList( ) ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.UNAUTHORIZED )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( GrustoragedbConstants.API_KEY_ERROR ) ) )
                .build( );       
    }

    /**
     * True if status is completed
     * 
     * @param status
     * @return true if status is completed
     */
    private boolean isStatusCompleted( Status status )
    {
        return status != null && StringUtils.isNotEmpty( status.getStatus( ) ) && StringUtils.isNotEmpty( status.getCodeStatus( ) ) 
                && StringUtils.isNotEmpty( status.getLabelColorCode( ) ) && StringUtils.isNotEmpty( status.getBannerColorCode( ) );
    }

}
