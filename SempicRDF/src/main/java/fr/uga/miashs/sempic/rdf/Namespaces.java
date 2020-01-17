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
public class Namespaces {
    
    public final static String photoNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Picture/";
    public final static String placeNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Place/";
    public final static String landscapeNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Landscape/";
    public final static String eventNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Event/";
    public final static String personNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Person/";
    public final static String objectNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Objects/";
    public final static String animalNS = "http://www.semanticweb.org/edouard/ontologies/2020/0/projetOntology.owl#Animal/";
    
    public static String getPhotoUri(long photoId) {
        return photoNS+photoId;
    }
    
    public static String getPlaceUri(String placeName) {
        return placeNS+placeName;
    }
    
    public static String getLandscapeUri(String landscapeName) {
        return landscapeNS+landscapeName;
    }
    
    public static String getEventUri(String eventName) {
        return eventNS+eventName;
    }
    
    public static String getPersonUri(String firstName, String lastName) {
        return personNS+firstName+lastName;
    }
    
    public static String getObjectUri(String objectName) {
        return objectNS+objectName;
    }
    
    public static String getAnimalUri(String animalName) {
        return animalNS+animalName;
    }
}
