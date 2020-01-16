/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.PhotoFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.qualifiers.SelectedAlbum;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author benjamin
 */
public class ViewAlbumBeans {
        
    @Inject
    private PhotoFacade photoDao;
    @Inject
    private PhotoBeans photoBeans;
    
    @Inject
    @SelectedAlbum
    private Album currentAlbum;
    
    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    public PhotoBeans getPhotoBeans() {
        return photoBeans;
    }
    
    public List<Photo> getPhotos() {
        try {
            return photoDao.findPhotosOfAlbum(currentAlbum);
        } catch (SempicModelException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
        return Collections.emptyList();
    }
}
