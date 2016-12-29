/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandDAO;
import fr.paris.lutece.test.LuteceTestCase;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Iterator;


/**
 * Test class for the DemandDAO
 *
 */
public class DemandDAOTest extends LuteceTestCase
{
    private static final String CUSTOMER_ID_1 = "CustomerId1";
    private static final String CUSTOMER_ID_2 = "CustomerId1";
    private static final String DEMAND_ID_1 = "DemandId1";
    private static final String DEMAND_ID_2 = "DemandId2";
    private static final String DEMAND_TYPE_ID_1 = "DemandTypeId1";
    private static final String DEMAND_TYPE_ID_2 = "DemandTypeId2";
    private static final String DEMAND_REFERENCE_1 = "DemandReference1";
    private static final String DEMAND_REFERENCE_2 = "DemandReference2";
    private static final long DEMAND_CREATION_DATE_1 = 1L;
    private static final long DEMAND_CREATION_DATE_2 = 2L;
    private static final long DEMAND_CLOSURE_DATE_1 = 1L;
    private static final long DEMAND_CLOSURE_DATE_2 = 2L;
    private static final int DEMAND_MAX_STEPS_1 = 1;
    private static final int DEMAND_MAX_STEPS_2 = 2;
    private static final int DEMAND_CURRENT_STEP_1 = 1;
    private static final int DEMAND_CURRENT_STEP_2 = 2;
    private final IDemandDAO _demandDao;

    /**
     * Constructor
     */
    public DemandDAOTest(  )
    {
        _demandDao = new DemandDAO(  );
    }

    /**
     * Test case
     */
    public void testBusiness(  )
    {
        // Create test
        Demand demand = new Demand(  );
        demand.setId( DEMAND_ID_1 );
        demand.setTypeId( DEMAND_TYPE_ID_1 );
        demand.setReference( DEMAND_REFERENCE_1 );
        demand.setStatusId( Demand.STATUS_INPROGRESS );
        demand.setCustomerId( CUSTOMER_ID_1 );
        demand.setCreationDate( DEMAND_CREATION_DATE_1 );
        demand.setClosureDate( DEMAND_CLOSURE_DATE_1 );
        demand.setMaxSteps( DEMAND_MAX_STEPS_1 );
        demand.setCurrentStep( DEMAND_CURRENT_STEP_1 );

        _demandDao.insert( demand );

        Demand demandStored = _demandDao.load( demand.getId(  ), demand.getTypeId(  ) );
        assertThat( demandStored.getId(  ), is( demand.getId(  ) ) );
        assertThat( demandStored.getTypeId(  ), is( demand.getTypeId(  ) ) );
        assertThat( demandStored.getReference(  ), is( demand.getReference(  ) ) );
        assertThat( demandStored.getStatusId(  ), is( demand.getStatusId(  ) ) );
        assertThat( demandStored.getCustomerId(  ), is( demand.getCustomerId(  ) ) );
        assertThat( demandStored.getCreationDate(  ), is( demand.getCreationDate(  ) ) );
        assertThat( demandStored.getClosureDate(  ), is( demand.getClosureDate(  ) ) );
        assertThat( demandStored.getMaxSteps(  ), is( demand.getMaxSteps(  ) ) );
        assertThat( demandStored.getCurrentStep(  ), is( demand.getCurrentStep(  ) ) );

        // Update test
        demand.setId( DEMAND_ID_2 );
        demand.setTypeId( DEMAND_TYPE_ID_2 );
        demand.setReference( DEMAND_REFERENCE_2 );
        demand.setStatusId( Demand.STATUS_CLOSED );
        demand.setCustomerId( CUSTOMER_ID_2 );
        demand.setCreationDate( DEMAND_CREATION_DATE_2 );
        demand.setClosureDate( DEMAND_CLOSURE_DATE_2 );
        demand.setMaxSteps( DEMAND_MAX_STEPS_2 );
        demand.setCurrentStep( DEMAND_CURRENT_STEP_2 );

        _demandDao.store( demand );

        demandStored = _demandDao.load( demand.getId(  ), demand.getTypeId(  ) );
        assertThat( demandStored, is( nullValue(  ) ) );

        demand.setId( DEMAND_ID_1 );
        demand.setTypeId( DEMAND_TYPE_ID_1 );
        _demandDao.store( demand );

        demandStored = _demandDao.load( demand.getId(  ), demand.getTypeId(  ) );
        assertThat( demandStored.getId(  ), is( demand.getId(  ) ) );
        assertThat( demandStored.getTypeId(  ), is( demand.getTypeId(  ) ) );
        assertThat( demandStored.getReference(  ), is( not( demand.getReference(  ) ) ) );
        assertThat( demandStored.getStatusId(  ), is( demand.getStatusId(  ) ) );
        assertThat( demandStored.getCustomerId(  ), is( demand.getCustomerId(  ) ) );
        assertThat( demandStored.getCreationDate(  ), is( not( demand.getCreationDate(  ) ) ) );
        assertThat( demandStored.getClosureDate(  ), is( demand.getClosureDate(  ) ) );
        assertThat( demandStored.getMaxSteps(  ), is( not( demand.getMaxSteps(  ) ) ) );
        assertThat( demandStored.getCurrentStep(  ), is( demand.getCurrentStep(  ) ) );

        // List test
        Collection<Demand> collectionDemands = _demandDao.loadByCustomerId( CUSTOMER_ID_1 );
        assertThat( collectionDemands.size(  ), is( 1 ) );

        Iterator<Demand> iteratorDemand = collectionDemands.iterator(  );
        demandStored = iteratorDemand.next(  );
        assertThat( demandStored.getId(  ), is( DEMAND_ID_1 ) );
        assertThat( demandStored.getTypeId(  ), is( DEMAND_TYPE_ID_1 ) );

        // Delete test
        _demandDao.delete( DEMAND_ID_1, DEMAND_TYPE_ID_1 );
        demandStored = _demandDao.load( DEMAND_ID_1, DEMAND_TYPE_ID_1 );
        assertThat( demandStored, is( nullValue(  ) ) );
    }
}
