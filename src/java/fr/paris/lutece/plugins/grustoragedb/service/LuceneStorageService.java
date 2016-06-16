package fr.paris.lutece.plugins.grustoragedb.service;



import fr.paris.lutece.plugins.grustoragedb.service.search.SearchService;

import fr.paris.lutece.plugins.grusupply.business.Customer;
import fr.paris.lutece.portal.service.spring.SpringContextService;


public class LuceneStorageService {

	private static final String BEAN_SERVICE = "grustoragedb.luceneStorageService";  
      private static LuceneStorageService _singleton;
	    
	    public static LuceneStorageService instance(  )
	    {
	        if ( _singleton == null )
	        {
	            _singleton =  SpringContextService.getBean( BEAN_SERVICE );       
	          
	        }
	        return _singleton;
	    }
	    

	public void store(Customer user) {		
		
		fr.paris.lutece.plugins.gru.business.customer.Customer customer = new fr.paris.lutece.plugins.gru.business.customer.Customer();
		customer.setFirstname(user.getFirstName());
		customer.setLastname(user.getName());
		customer.setId(user.getCustomerId());
		customer.setEmail(user.getEmail());
		customer.setFixedPhoneNumber(user.getFixedTelephoneNumber());
		customer.setMobilePhone(user.getTelephoneNumber());
		SearchService.indexCustomer(customer);        
		
	}    

}
