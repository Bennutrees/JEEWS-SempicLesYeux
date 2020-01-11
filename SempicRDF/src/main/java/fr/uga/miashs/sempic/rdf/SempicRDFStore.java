/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import java.util.*;
import javax.ejb.Stateless;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.syntax.Template;
import org.apache.jena.vocabulary.*;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Stateless
public class SempicRDFStore extends BasicSempicRDFStore {

    /**
     * Delete the triple <picture,p,o> from the triple store
     * and also the resource o if it is a blank node
     * @param picture
     * @param p
     * @param o 
     */
    public void deleteAnnotation(Resource picture, Property p, Resource o) {
        /*if (!picture.getURI().startsWith(Namespaces.photoNS)) {
            return;
        }*/  
        cnx.begin(ReadWrite.WRITE);
        // can add a clause that check that p is a subproperty of depicts
        if (o.isAnon()) {
            cnx.update("DELETE WHERE { "
                    + "<" + picture.getURI() + "> <" + p.getURI() + "> ?o . "
                    + "?o <" + RDFS.label + "> \"" + o.getProperty(RDFS.label).getString() + "\" ."
                    + "?o ?p ?x}");
        } else {
            cnx.update("DELETE DATA { <" + picture.getURI() + "> <" + p.getURI() + "> <" + o.getURI() + "> } ");
        }
        cnx.commit();
        if (picture.getModel() != null) {
            picture.getModel().removeAll(picture, p, o);
        }
    }
    
    /**
     * delete all the triple with subject picture and property p
     * @param picture
     * @param p 
     */
    public void deleteAnnotation(Resource picture, Property p) {
        /*if (!picture.getURI().startsWith(Namespaces.photoNS)) {
            return;
        }*/
        cnx.update("DELETE  WHERE { <" + picture.getURI() + "> <" + p.getURI() + "> ?o } ");
        if (picture.getModel() != null) {
            picture.removeAll(p);
        }
    }


    /**
     * Add annotation to the picture i.e. triple <picture,p,o> is added to the triple store.
     * if o has properties, hey are also added.
     * @param picture
     * @param p
     * @param o 
     */
    public void addAnnotation(Resource picture, Property p, Resource o) {
        if (o==null) return;
        /*if (!picture.getURI().startsWith(Namespaces.photoNS)) {
            return;
        }*/
        Model m = ModelFactory.createDefaultModel();
        if (o.getModel()!=null) {
             m.add(o.listProperties());
        }
        m.add(picture, p, o);
        cnx.begin(ReadWrite.WRITE);
        cnx.load(m);
        cnx.commit();
        picture.getModel().add(m);
    }
    
    public void setAnnotation(Resource picture, Property annotProp, Resource r) {
        deleteAnnotation(picture,annotProp);
        addAnnotation(picture, annotProp, r);
    }
    
    /*
    Can be simplified
    */
    public List<Resource> searchPhotos(List<Resource> types, List<Resource> depicts, Resource takenBy, Resource takenIn, List<Resource> restrictionDepicted, long ownerId) {
        
        if (depicts==null) depicts=Collections.emptyList();
        if (types==null) types=Collections.emptyList();
        if (restrictionDepicted==null) restrictionDepicted=Collections.emptyList();
        StringBuilder query = new StringBuilder();
        query.append("CONSTRUCT {?p a <"+SempicOnto.Photo+">} "
                + "WHERE {"
                + "?p a <"+SempicOnto.Photo+"> .");
                if (takenBy !=null) {
                    query.append("?p <"+SempicOnto.takenBy+"> <"+takenBy+">.");
                }
                if (takenIn !=null) {
                    query.append("?p <"+SempicOnto.takenIn+"> <"+takenIn+">.");
                }
                types.forEach(t -> {query.append("?p a <"+t+">.");});
                
                depicts.forEach(t -> {
                    if (t.isAnon())
                        query.append("?p <"+SempicOnto.depicts+"> ?d. ?d a <"+t.getPropertyResourceValue(RDF.type)+"> .");
                    else
                        query.append("?p <"+SempicOnto.depicts+"> <"+t+"> .");
                });
                restrictionDepicted.forEach( r -> {query.append("?p <"+SempicOnto.depicts+"> [ <"+r.getPropertyResourceValue(OWL.onProperty)+"> <"+r.getPropertyResourceValue(OWL.hasValue)+">] .");});
                if (ownerId!=-1) {
                    query.append("?p <"+SempicOnto.ownerId+"> "+ownerId+" .");
                }
               query.append("}");
        //System.out.println(query);  
        return cnx.queryConstruct(query.toString()).listSubjects().toList();
    }
    
    /**
     * Retrieve the instances that contain the string labelContent in one of their labels
     * @param type the type of instances
     * @param labelContent the content of the instance labels
     * @param instanciatedProperty if not null the instance has to be object of this given property
     * @param ownerId if not -1 the subject (photo) of the instanciatedProperty has to be owned by the given user
     * @return 
     */
    public List<Resource> lookupInstances(Resource type, String labelContent, Resource instanciatedProperty, long ownerId) {
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX text: <http://jena.apache.org/text#> "
                + "CONSTRUCT {?s rdfs:label ?l} "
                + "WHERE { ?s rdfs:label ?l.";
        if (type!=null)
                query+= "?s a <"+type.getURI()+"> .";
        if (instanciatedProperty!=null) {
                query+= "[ <"+instanciatedProperty.getURI()+"> ?s";
                if (ownerId != -1) {
                    query+="; <"+SempicOnto.ownerId+"> "+ownerId;
                }
                query+="] .";
        }
        if (labelContent!=null && !"".equals(labelContent)) {
            query+= "FILTER regex(?l, \""+labelContent.toLowerCase()+"\", \"i\")";//"FILTER (STRSTARTS(LCASE(?l),\""+labelContent.toLowerCase()+"\")) .";
            query+= "(?s) text:query (rdfs:label \""+labelContent.toLowerCase()+"*\") .";
            
        }
        
        query+= "FILTER (ISIRI(?s)) .";
        query+="}";
        //System.out.println(query);
        Model m = cnx.queryConstruct(query);
        List<Resource> res =  m.listSubjects().toList();
        return res;
    }
    
     public List<Resource> lookupInstances(Resource type, String prefix) {
         return lookupInstances(type,prefix,null,-1);
     }
    
    public List<Resource> createAnonInstances(Resource type, String prefix, Resource instanciatedProperty, long ownerId) {
        StringBuilder query = new StringBuilder(
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX text: <http://jena.apache.org/text#> "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "CONSTRUCT {_:i rdfs:label ?l ."
                + "_:i a ?c"
                + "} "
                + "WHERE { "
                + "?c a owl:Class . "
                +"{"
                + "?c rdfs:label ?cl . "
                + "VALUES ?det { \"a\"@en \"un(e)\"@fr \"unas/unos\"@es} ."
                + "FILTER (lang(?det)=lang(?cl)) ."
                + "BIND (CONCAT(?det,\"  \",?cl) AS ?l) ."
                + "} UNION { "
                + "FILTER (lang(?cl)=\"\") ."
                + "BIND (CONCAT(\"A  \",?cl) AS ?l) ."
                + "}");
        if (instanciatedProperty!=null) {
                query.append("FILTER (EXISTS {[ <").append(instanciatedProperty.getURI()).append("> ?s");
                
                if (ownerId != -1) {
                    query.append("; <"+SempicOnto.ownerId+"> "+ownerId);
                }
                query.append("] . ?s a ?c}) .");
                
        }
        if (type!=null)
                query.append( "?c rdfs:subClassOf <"+type.getURI()+"> .");
        if (prefix!=null && !"".equals(prefix)) {
            query.append("FILTER regex(?l, \""+prefix.toLowerCase()+"\", \"i\")");
        }
        
        query.append("}");
        //System.out.println(query);
        Model m = cnx.queryConstruct(query.toString());
        return m.listSubjects().toList();
    }
    
    
    public List<Resource> getInstanciatedPictureTypes(long ownerId) {
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "CONSTRUCT {?s rdfs:label ?l} "
                + "WHERE {?s rdfs:label ?l . "
                + "?s rdfs:subClassOf <"+SempicOnto.Photo+"> ."
                + "[ a ?s ";
        if (ownerId!=-1) {
            query+="; <"+SempicOnto.ownerId+"> "+ownerId;
        }
        query+="] }";
        //System.out.println(query);
        Model m = cnx.queryConstruct(query);
        return m.listSubjects().toList();
    }
    
    
    public List<Resource> getInstancesAndTypes(String prefix, Resource type, Resource instanciatedProperty,long ownerId) {
        List<Resource> res = this.lookupInstances(type, prefix, instanciatedProperty,ownerId);
        res.addAll(this.createAnonInstances(type, prefix, instanciatedProperty,ownerId));
        return res;
    }
    
    public List<Resource> getInstancesAndTypes(String prefix, Resource type) {
        return getInstancesAndTypes(prefix,type,null,-1);
    }
   
    /*
    return all hasValue property restrictions that can be 
    built from objects of depicts property
    */
    public List<Resource> getDepictedPropertyRestrictions(String prefix, long ownerId) {
        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                        + "PREFIX text: <http://jena.apache.org/text#> " 
                        + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                        + "CONSTRUCT { " +
                            "  _:x a owl:Restriction . " +
                            "  _:x owl:onProperty ?p ." +
                            "  _:x owl:hasValue ?v ." +
                            "  _:x rdfs:label ?l" +
                            " } WHERE {" +
                            "  [ <"+SempicOnto.depicts+"> ?smt";
        if (ownerId!=-1) {
            query+="; <"+SempicOnto.ownerId+"> "+ownerId;
        }
        query+="].";
               query+=      "  ?smt ?p ?v." +
                            "  ?p rdfs:label ?pl." +
                            "  ?v rdfs:label ?vl." +
                            "  BIND (CONCAT(?pl,\" \",?vl) AS ?l)."+
                            "  FILTER (LANG(?pl)=LANG(?vl))." ;//+
                            //"  FILTER (STRSTARTS(STR(?p),\""+SempicNamespaces.APP_ONTO+"\")) ." +
                            //"  FILTER (?p != <"+SempicNamespaces.APP_ONTO+"isDepictedIn>)";
                            if (prefix !=null && !"".equals(prefix)) {
                                query+= "FILTER (regex(LCASE(?l),\""+prefix.toLowerCase()+"\",\"i\"))";
                                //query+= "?s text:query (rdfs:label \""+prefix.toLowerCase()+"*\") .";
                            }
                            query+="}";
        //System.out.println(query);
        Model m = cnx.queryConstruct(query);
        return m.listSubjects().toList();
    }

    
}
