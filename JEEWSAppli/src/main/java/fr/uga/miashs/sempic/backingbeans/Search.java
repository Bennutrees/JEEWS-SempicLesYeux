/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_GSP;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_QUERY;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_UPDATE;
import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

/**
 *
 * @author Edouard
 */
@Named
@RequestScoped
public class Search  {
    
    private final static String ENDPOINT= "http://localhost:3030/sempic/";
    public final static String ENDPOINT_QUERY = ENDPOINT+"sparql"; // SPARQL endpoint
    public final static String ENDPOINT_UPDATE = ENDPOINT+"update"; // SPARQL UPDATE endpoint
    public final static String ENDPOINT_GSP = ENDPOINT+"data"; // Graph Store Protocol
    
    ArrayList<Resource> pictures = new ArrayList<Resource>();
    
    public void searchAll()
    {
        System.out.println("On à cliqué sur searchAllPictures");
        
        /*try ( RDFConnection conn = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP))
        {
            conn.load("sempiconto.owl");
            QueryExecution qe = conn.query("SELECT DISTINCT ?s WHERE {?s ?p ?o}");
            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                Resource subject = qs.getResource("s");
                System.out.println("On à quoi après"+ subject);
            }
            qe.close();
            conn.close();
        }*/
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        //Récupération de toutes les photos
        QueryExecution qe = cnx.query("SELECT DISTINCT ?s WHERE {?s ?p ?o}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("s"));
            System.out.println("On à quoi après"+ qs.getResource("s"));
        }
        cnx.close();

        
    }
    
    public void SearchAllPictures()
    {
     RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        //Récupération de toutes les photos
        QueryExecution qe = cnx.query("SELECT distinct ?picture ?title WHERE { ?picture a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>; <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Title> ?title.}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close();   
    }

    public ArrayList<Resource> getPictures() {
        return pictures;
    }
    
}
