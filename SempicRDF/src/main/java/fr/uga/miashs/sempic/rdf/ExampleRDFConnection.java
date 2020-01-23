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
        //  <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>}");
        
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#> \n"+
                "SELECT DISTINCT ?picture ?Appareil\n" +
"  WHERE {\n" +
"    ?picture a projet:Picture;\n" +
"        projet:Taken_By ?Appareil.\n" +
"  \n" +
"  	?Appareil a projet:Camera\n" +
"}");

        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            l.add(qs.getResource("picture"));
            System.out.println("Le contenu de la liste : "+ l);
            System.out.println(qs.getResource("picture"));
        }

        cnx.close();     
    }
}
