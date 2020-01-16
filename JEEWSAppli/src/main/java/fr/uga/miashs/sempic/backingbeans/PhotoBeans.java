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
import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author adem
 */
@Named
@ViewScoped
public class PhotoBeans implements Serializable{
    
    @Inject
    @SelectedAlbum
    private Album album;
    
    @Inject
    private PhotoFacade service;
    
    private Part photoPart;
    
    private Photo photo;
    
    public PhotoBeans(){
    }
    
    
    
    public Album getAlbum() {
        return this.album;
    }
    
    public Part getPhotoPart() {
        return this.photoPart;
    }
    
    public Photo getPhoto() {
        return this.photo;
    }
    
    public void setAlbum(Album album) {
        this.album = album;
    }
    
    public void setPhotoPart(Part part) {
        this.photoPart = part;
    }
    
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
    
    public String create() throws IOException, SempicModelException {
        this.photo = new Photo();
        this.photo.setAlbum(this.album);
        
        this.service.create(this.photo,this.photoPart.getInputStream());
        
        return "album?faces-redirect=true;";
        
    }
    
}
