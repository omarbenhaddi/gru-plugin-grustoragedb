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
package fr.paris.lutece.plugins.grustoragedb.service.search;

import fr.paris.lutece.plugins.gru.business.customer.CustomerHome;
import fr.paris.lutece.plugins.gru.service.search.CustomerResult;
import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;


/**
 * SearchService
 */
public final class SearchService
{
    private static final String PATH_INDEX = "WEB-INF/plugins/grustoragedb/indexes";
    private static final String FIELD_CUSTOMER_INFOS = "customer";
    private static final String FIELD_ID = "id";
    private static final String FIELD_FIRSTNAME = "firstname";
    private static final String FIELD_LASTNAME = "lastname";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PHONE = "phone";

    /** Private constructor */
    private SearchService()
    {
    }

    public static String indexAll( )
    {
        StringBuilder sbLogs = new StringBuilder();
        List<Customer> list = CustomerHome.getCustomersList();
        try
        {
            Directory dir = FSDirectory.open( getIndexPath(  ) );
            Analyzer analyzer = new StandardAnalyzer( Version.LUCENE_4_9 );
            IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_4_9, analyzer );
            boolean bCreate = true;

            if ( bCreate )
            {
                iwc.setOpenMode( OpenMode.CREATE );
            }
            else
            {
                iwc.setOpenMode( OpenMode.CREATE_OR_APPEND );
            }

            IndexWriter writer = new IndexWriter( dir, iwc );
            for( Customer customer  : list )
            {
                index( writer, customer );
            }
            sbLogs.append( "Indexed customers : " + list.size() );
            writer.close(  );
        }
        catch ( IOException ex )
        {
            AppLogService.error( "Error indexing customer : " + ex.getMessage(  ), ex );
        }
        return sbLogs.toString();
    }
    
    
    /**
     * Index a customer
     * @param customer The customer to index
     */
    public static void indexCustomer( Customer customer )
    {
        try
        {
            Directory dir = FSDirectory.open( getIndexPath(  ) );
            Analyzer analyzer = new StandardAnalyzer( Version.LUCENE_4_9 );
            IndexWriterConfig iwc = new IndexWriterConfig( Version.LUCENE_4_9, analyzer );
            boolean bCreate = true;

            if ( bCreate )
            {
                iwc.setOpenMode( OpenMode.CREATE );
            }
            else
            {
                iwc.setOpenMode( OpenMode.CREATE_OR_APPEND );
            }

            IndexWriter writer = new IndexWriter( dir, iwc );
            index( writer, customer );
            writer.close(  );
        }
        catch ( IOException ex )
        {
            AppLogService.error( "Error indexing customer : " + ex.getMessage(  ), ex );
        }
    }

    /**
     * Search for a customer
     * @param strQuery The search query
     * @return A list of results
     */
    public static List<CustomerResult> searchCustomer( String strQuery )
    {
        List<CustomerResult> list = new ArrayList<CustomerResult>(  );

        try
        {
            IndexReader reader = DirectoryReader.open( FSDirectory.open( getIndexPath(  ) ) );
            IndexSearcher searcher = new IndexSearcher( reader );
            Analyzer analyzer = new StandardAnalyzer( Version.LUCENE_4_9 );

            QueryParser parser = new QueryParser( Version.LUCENE_4_9, FIELD_CUSTOMER_INFOS, analyzer );
            parser.setDefaultOperator( QueryParser.Operator.AND );
            Query query = parser.parse( strQuery );
            TopDocs results = searcher.search( query, 10 );
            ScoreDoc[] hits = results.scoreDocs;

            for ( ScoreDoc hit : hits )
            {
                Document doc = searcher.doc( hit.doc );
                CustomerResult customer = new CustomerResult(  );
                customer.setFirstname( doc.get( FIELD_FIRSTNAME ) );
                customer.setLastname( doc.get( FIELD_LASTNAME ) );
                customer.setEmail( doc.get( FIELD_EMAIL ) );
                customer.setMobilePhone( doc.get( FIELD_PHONE ) );
                customer.setId( Integer.parseInt( doc.get( FIELD_ID ) ) );
                list.add( customer );
            }

            reader.close(  );
        }
        catch ( IOException ex )
        {
            AppLogService.error( "Error searching customer : " + ex.getMessage(  ), ex );
        }
        catch ( ParseException ex )
        {
            AppLogService.error( "Error searching customer : " + ex.getMessage(  ), ex );
        }

        return list;
    }

    /**
     * Index a customer
     * @param writer The index writer
     * @param customer The customer
     * @throws IOException  If an error occurs
     */
    private static void index( IndexWriter writer, Customer customer )
        throws IOException
    {
        Document doc = new Document(  );
        StringBuilder sbCustomerInfos = new StringBuilder(  );
        sbCustomerInfos.append( customer.getFirstname(  ) ).append( " " ).append( customer.getLastname(  ) );

        Field fielIdname = new StringField( FIELD_ID, "" + customer.getId(), Field.Store.YES );
        doc.add( fielIdname );

        Field fielFirstname = new StringField( FIELD_FIRSTNAME, customer.getFirstname(  ), Field.Store.YES );
        doc.add( fielFirstname );

        Field fielLastname = new StringField( FIELD_LASTNAME, customer.getLastname(  ), Field.Store.YES );
        doc.add( fielLastname );

        if ( customer.getEmail(  ) != null )
        {
            Field fieldEmail = new StringField( FIELD_EMAIL, customer.getEmail(  ), Field.Store.YES );
            doc.add( fieldEmail );
            sbCustomerInfos.append( " " ).append( customer.getEmail(  ) );
        }

        if ( customer.getMobilePhone() != null )
        {
            Field fieldPhone = new StringField( FIELD_PHONE, customer.getMobilePhone(  ), Field.Store.YES );
            doc.add( fieldPhone );
            sbCustomerInfos.append( " " ).append( customer.getMobilePhone(  ) );
        }

        Field fieldCustomer = new TextField( FIELD_CUSTOMER_INFOS, sbCustomerInfos.toString(  ), Field.Store.YES );
        doc.add( fieldCustomer );

        if ( writer.getConfig(  ).getOpenMode(  ) == OpenMode.CREATE )
        {
            writer.addDocument( doc );
        }
        else
        {
            writer.updateDocument( new Term( FIELD_CUSTOMER_INFOS, sbCustomerInfos.toString(  ) ), doc );
        }
    }

    /**
     * Returns the index PATH
     * @return The index path
     */
    private static File getIndexPath(  )
    {
        String strIndexPath = AppPathService.getAbsolutePathFromRelativePath( PATH_INDEX );

        return Paths.get( strIndexPath ).toFile(  );
    }
}
