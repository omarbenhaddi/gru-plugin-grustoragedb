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


public class DemandBusinessTest extends LuteceTestCase
{
    private final static String IDCUSTOMER1 = "1";
    private final static String IDCUSTOMER2 = "2";
    private final static String DEMANDID1 = "DemandId1";
    private final static String DEMANDID2 = "DemandId2";
    private final static String DEMANDTYPEID1 = "DemandTypeId1";
    private final static String DEMANDTYPEID2 = "DemandTypeId2";
    private final static int DEMANDSTATE1 = 1;
    private final static int DEMANDSTATE2 = 2;
    private final static int MAXSTEPS1 = 1;
    private final static int MAXSTEPS2 = 2;
    private final static int CURRENTSTEP1 = 1;
    private final static int CURRENTSTEP2 = 2;

    public void testBusiness(  )
    {
        // Initialize an object
        DbDemand demand = new DbDemand(  );
        demand.setCustomerId( IDCUSTOMER1 );
        demand.setDemandId( DEMANDID1 );
        demand.setDemandTypeId( DEMANDTYPEID1 );
        demand.setDemandStatus( DEMANDSTATE1 );
        demand.setMaxSteps( MAXSTEPS1 );
        demand.setCurrentStep( CURRENTSTEP1 );

        // Create test
        DbDemandHome.create( demand );

        DbDemand demandStored = DbDemandHome.findByPrimaryKey( demand.getId(  ) );
        assertEquals( demandStored.getCustomerId(  ), demand.getCustomerId(  ) );
        assertEquals( demandStored.getDemandId(  ), demand.getDemandId(  ) );
        assertEquals( demandStored.getDemandTypeId(  ), demand.getDemandTypeId(  ) );
        assertEquals( demandStored.getDemandStatus(  ), demand.getDemandStatus(  ) );
        assertEquals( demandStored.getMaxSteps(  ), demand.getMaxSteps(  ) );
        assertEquals( demandStored.getCurrentStep(  ), demand.getCurrentStep(  ) );

        // Update test
        demand.setCustomerId( IDCUSTOMER2 );
        demand.setDemandId( DEMANDID2 );
        demand.setDemandTypeId( DEMANDTYPEID2 );
        demand.setDemandStatus( DEMANDSTATE2 );
        demand.setMaxSteps( MAXSTEPS2 );
        demand.setCurrentStep( CURRENTSTEP2 );
        DbDemandHome.update( demand );
        demandStored = DbDemandHome.findByPrimaryKey( demand.getId(  ) );
        assertEquals( demandStored.getCustomerId(  ), demand.getCustomerId(  ) );
        assertEquals( demandStored.getDemandId(  ), demand.getDemandId(  ) );
        assertEquals( demandStored.getDemandTypeId(  ), demand.getDemandTypeId(  ) );
        assertEquals( demandStored.getDemandStatus(  ), demand.getDemandStatus(  ) );
        assertEquals( demandStored.getMaxSteps(  ), demand.getMaxSteps(  ) );
        assertEquals( demandStored.getCurrentStep(  ), demand.getCurrentStep(  ) );

        // List test
        DbDemandHome.getDemandsList(  );

        // Delete test
        DbDemandHome.remove( demand.getId(  ) );
        demandStored = DbDemandHome.findByPrimaryKey( demand.getId(  ) );
        assertNull( demandStored );
    }
}
