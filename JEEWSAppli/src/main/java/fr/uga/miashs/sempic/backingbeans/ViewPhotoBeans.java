/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.dao.PhotoFacade;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.rdf.SempicRDFStore;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author benjamin
 */
@Named
@ViewScoped
public class ViewPhotoBeans implements Serializable {
    
    @Inject
    private PhotoFacade photoDao;
    @EJB
    private SempicRDFStore rdfStore;
    
    @Inject
    @SelectedPhoto
    private Photo currentPhoto;

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    public void setCurrentPhoto(Photo currentPhoto) {
        this.currentPhoto = currentPhoto;
    }
}
