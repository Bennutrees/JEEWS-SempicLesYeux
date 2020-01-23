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
    
    public void SearchSelfies(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("SELECT distinct ?picture ?subject WHERE { ?picture a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>; <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Author> ?subject; <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Subject> ?subject}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureAlone(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("SELECT DISTINCT ?picture (count(?who) as ?count)\n" +
        "  WHERE {\n" +
        "    ?picture a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>;\n" +
        "             <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Who> ?who.\n" +
        "  }\n" +
        "group BY ?picture\n" +
        "having(?count = 1)");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureGroup(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("SELECT DISTINCT ?picture (count(?who) as ?count)\n" +
        "  WHERE {\n" +
        "    ?picture a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>;\n" +
        "             <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Who> ?who.\n" +
        "  }\n" +
        "group BY ?picture\n" +
        "having(?count != 1)");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureFriend(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT DISTINCT ?picture\n" +
        "  WHERE {\n" +
        "    ?picture a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>;\n" +
        "        <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Who> ?person;\n" +
        "        <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Who> ?personFriend.\n" +
        "  \n" +
        "    ?person a ?personType.\n" +
        "    ?personFriend a ?personType.\n" +
        "    ?personType rdfs:subClassOf <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Person>.\n" +
        "  \n" +
        "    ?person <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Friend> ?personFriend.\n" +
        "    ?personFriend <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Friend> ?person.\n" +
"}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureColleague(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT distinct ?picture ?person ?colleague\n" +
"  WHERE {\n" +
"     ?picture a <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture>;\n" +
"              <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Who> ?person;\n" +
"  			  <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Who> ?colleague.\n" +
"  \n" +
"  	?person <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Colleague> ?personColleague.\n" +
"  	?person <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Works> ?personJob.\n" +
"  \n" +
"  	?personJob <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Colleague> ?colleague.\n" +
"    \n" +
"  }");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureColleagueAndFriend(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#> SELECT DISTINCT ?picture ?person ?personFriend ?personCollege ?colleague\n" +
"  WHERE {\n" +
"    ?picture a projet:Picture;\n" +
"        projet:Who ?person;\n" +
"        projet:Who ?personCollege;\n" +
"        projet:Who ?personFriend;\n" +
"  		projet:Who ?colleague.\n" +
"  \n" +
"    ?person a ?personType.\n" +
"    ?personFriend a ?personType.\n" +
"    ?personType rdfs:subClassOf projet:Person.\n" +
"  \n" +
"    ?person projet:Friend ?personFriend.\n" +
"    ?personFriend projet:Friend ?person.\n" +
"  \n" +
"  	?personCollege projet:Colleague ?personColleague.\n" +
"  	?personCollege projet:Works ?personJob.\n" +
"  \n" +
"  	?personJob projet:Colleague ?colleague.\n" +
"}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureArtiste(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#> \n"+
                " SELECT DISTINCT ?picture ?author ?authorJob\n" +
"  WHERE {\n" +
"    ?picture a projet:Picture;\n" +
"        projet:Author ?author.\n" +
"  \n" +
"    ?author projet:Works ?authorJob.\n" +
"    ?authorJob a projet:Photographer\n" +
"}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureBrotherOrSister(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#> \n"+
                "SELECT DISTINCT ?picture ?person1 ?person2\n" +
"  WHERE {\n" +
"    ?picture a projet:Picture;\n" +
"        projet:Who ?person1;\n" +
"        projet:Who ?person2.\n" +
"  \n" +
"  		?person1 projet:hasSister ?person2.\n" +
"        ?person2 projet:hasBrother ?person1.\n" +
"}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    
    public void SearchPictureCamera(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#> \n"+
                "SELECT DISTINCT ?picture ?Appareil\n" +
"  WHERE {\n" +
"    ?picture a projet:Picture;\n" +
"        projet:Taken_By ?Appareil.\n" +
"  \n" +
"  	?Appareil a projet:Camera\n" +
"}");
        System.out.println("Le contenu de qe : "+qe.execSelect());
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            pictures.add(qs.getResource("picture"));
            System.out.println("On à quoi après"+ qs.getResource("picture"));
        }
        cnx.close(); 
    }
    public void SearchPictureSamsungOnePersonDuringConcert(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        QueryExecution qe = cnx.query("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> prefix projet: <http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#> \n"+
                "SELECT DISTINCT ?picture (count(?who) as ?count)\n" +
"  WHERE {\n" +
"    ?picture a projet:Picture;\n" +
"            projet:Taken_By ?Appareil;\n" +
"            projet:Who ?who;\n" +
"  			projet:Event ?Event;\n" +
"    	    projet:Where ?Lieux.\n" +
"  \n" +
"        ?Appareil a projet:Samsung.\n" +
"        ?Event a projet:Musical.\n" +
"        ?Lieux a projet:Region.\n" +
"  }\n" +
"group BY ?picture\n" +
"having(?count != 1)");
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
