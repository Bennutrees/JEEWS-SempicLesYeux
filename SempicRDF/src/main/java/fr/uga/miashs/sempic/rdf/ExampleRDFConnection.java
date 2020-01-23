/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
import java.util.ArrayList;
import org.apache.jena.query.*;
import org.apache.jena.rdfconnection.*;

public class ExampleRDFConnection {
    
    private final static String ENDPOINT= "http://localhost:3030/sempic/";
    public final static String ENDPOINT_QUERY = ENDPOINT+"sparql"; // SPARQL endpoint
    public final static String ENDPOINT_UPDATE = ENDPOINT+"update"; // SPARQL UPDATE endpoint
    public final static String ENDPOINT_GSP = ENDPOINT+"data"; // Graph Store Protocol
    
    public static void main(String[] args) {
        
        ArrayList l = new ArrayList();
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        //Récupération de toutes les photos
        QueryExecution qe = cnx.query("SELECT DISTINCT ?s WHERE {?s a  <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>}");
        
        //Récupère tout les personnes
       /* QueryExecution qe = cnx.query("SELECT DISTINCT ?Person ?FirstName ?LastName\n" +
                                        "  WHERE{\n" +
                                        "  ?Person a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Person>.\n" +
                                        "  ?Person <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#FirstName> ?FirstName.\n" +
                                        "  ?Person <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#LastName> ?LastName\n" +
                                        "}");*/
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            l.add(qs.getResource("s"));
            System.out.println("Le contenu de la liste : "+ l);
            System.out.println(qs.getResource("s"));
        }

        cnx.close();     
    }
}
