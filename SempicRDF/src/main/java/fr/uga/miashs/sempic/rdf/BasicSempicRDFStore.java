/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import javax.ejb.Stateless;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

public class BasicSempicRDFStore extends RDFStore {
    
    public final PrefixMapping prefixes;

    public BasicSempicRDFStore() {
        prefixes = PrefixMapping.Factory.create();
        prefixes.withDefaultMappings(PrefixMapping.Standard);
        prefixes.setNsPrefix("sempic", SempicOnto.NS);
    }
    
    public Resource createPhoto(long photoId, long albumId, long ownerId) {
        // create an empty RDF graph
        Model m = ModelFactory.createDefaultModel();
        // create an instance of Photo in Model m
        Resource pRes = m.createResource(Namespaces.getPhotoUri(photoId), SempicOnto.Photo);

        pRes.addLiteral(SempicOnto.albumId, albumId);
        pRes.addLiteral(SempicOnto.ownerId, ownerId);

        saveModel(m);

        return pRes;
    }

    public void deletePhoto(long photoId) {
        // create an instance of Photo in Model m
        Resource pRes = ResourceFactory.createResource(Namespaces.getPhotoUri(photoId));
        deleteResource(pRes);
    }

    /**
     * Query a Photo and retrieve all the direct properties of the photo and if
     * the property are depic, takenIn or takenBy, it also retrieve the labels
     * of the object of these properties
     *
     * @param id
     * @return
     */
    public Resource readPhoto(long id) {
        String pUri = Namespaces.getPhotoUri(id);
        ParameterizedSparqlString pss = new ParameterizedSparqlString(prefixes);
        pss.setBaseUri(Namespaces.photoNS);
        
        pss.setCommandText(
                "CONSTRUCT {"
                        + "?photo ?p ?o ."
                        + "?photo ?p1 ?o1 ."
                        + "?o1 rdfs:label ?o2 ."
                + "} WHERE { "
                        + "?photo ?p ?o ."
                        + "OPTIONAL {"
                        + "?photo ?p1 ?o1 ."
                        + "?o1 rdfs:label ?o2 ."
                        + "FILTER (?p1 IN (<" + SempicOnto.depicts + ">,<" + SempicOnto.takenIn + ">,<" + SempicOnto.takenBy + ">)) "
                        +"}"
                 + "}");
        pss.setIri("photo", pUri);
        
        Model m = cnx.queryConstruct(pss.asQuery());
        return m.getResource(pUri);
    }
}
