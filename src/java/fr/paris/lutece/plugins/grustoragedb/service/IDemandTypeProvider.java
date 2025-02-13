package fr.paris.lutece.plugins.grustoragedb.service;

import java.util.List;

import fr.paris.lutece.plugins.grubusiness.business.demand.DemandType;

public interface IDemandTypeProvider {
	
	/**
	 * get list of demand types
	 * @return the list of demand types
	 */
	public List<DemandType> getDemandTypes( );

}
