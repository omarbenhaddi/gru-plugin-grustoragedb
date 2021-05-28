/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.grustoragedb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.paris.lutece.plugins.grubusiness.business.notification.BroadcastNotification;
import fr.paris.lutece.plugins.grubusiness.business.notification.EmailAddress;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seboo
 */
public class JsonGeneration {
    
    private static final String FAKE_MAIL = "fake_mail@paris.fr";
    private static final String FAKE_MAIL_CC = "fakecc_mail@paris.fr";
    private static final String FAKE_NAME = "fake_name";
    private static final String FAKE_PHONE = "0102030405";
    
    public static void generateJson( List<Notification> list )
    {
        String dir ="/home/seboo/DEV/sites/GRU/site4esb/target/out/";
        
        Path path = Paths.get( dir );
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            System.out.println( ex.getLocalizedMessage ( ) );
        }
    
        int i=1;
        ObjectMapper mapper = new ObjectMapper( );
        mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, true );
        mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        
        for (Notification notif : list)
        {
            anonymize( notif );
            try {
                String strJSON = mapper.writeValueAsString( notif );
                strJSON = "{ \"notification\" : " + strJSON + " }";
                writeFile( dir + i +".json", strJSON );
                i++;
            } catch (JsonProcessingException ex) {
                System.out.println( ex.getLocalizedMessage ( ) );
            }
        }
    }
    
    private static void anonymize( Notification notif )
    {
        
        // customer
        if ( notif.getDemand( ) != null && notif.getDemand( ).getCustomer( ) != null)
        {
            notif.getDemand().getCustomer( ).setEmail(FAKE_MAIL);
            notif.getDemand().getCustomer( ).setFamilyname(FAKE_NAME);
            notif.getDemand().getCustomer( ).setBirthDate("");
            notif.getDemand().getCustomer( ).setFixedPhoneNumber(FAKE_PHONE);
            notif.getDemand().getCustomer( ).setMobilePhone(FAKE_PHONE);
        }
        
        // mail
        if ( notif.getEmailNotification()!= null )
        {
            notif.getEmailNotification().setRecipient(FAKE_MAIL);
            notif.getEmailNotification().setCc(FAKE_MAIL_CC);
            notif.getEmailNotification().setBcc("");
        }
        
        // sms
        if ( notif.getSmsNotification()!= null )
        {
            notif.getSmsNotification().setPhoneNumber(FAKE_PHONE);
        }
        
        // broadcast
        if ( notif.getBroadcastEmail( )!= null && notif.getBroadcastEmail( ).size() > 0 )
        {
            for ( BroadcastNotification broadcastNotif : notif.getBroadcastEmail( ) )
            {
                broadcastNotif.setRecipient( EmailAddress.buildEmailAddresses( FAKE_MAIL, FAKE_MAIL_CC ) );
                broadcastNotif.setCc( EmailAddress.buildEmailAddresses( FAKE_MAIL, FAKE_MAIL_CC ) );
                broadcastNotif.setBcc( new ArrayList<>( ) ) ;
            }
        }
        
        // mydashboard
        
        // backoffice
    }
    
    
    private static void writeFile( String filePath, String fileContent ) 
    {
        try (PrintWriter output = new PrintWriter( filePath, "UTF-8")) {
            output.print( fileContent );
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println( ex.getLocalizedMessage ( ) );
        }
    }
}
