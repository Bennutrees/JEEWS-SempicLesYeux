/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.ResourceNotFoundException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.AlbumFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.qualifiers.SelectedUser;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author benjamin
 */
@Named
@ViewScoped
public class AlbumBeans implements Serializable {
    
    @Inject
    private AlbumFacade albumDAO;
    
    @Inject
    @SelectedUser
    private SempicUser selectedUser;
    
    private Album currentAlbum;
    
    public AlbumBeans() {
        
    }
    
    @PostConstruct
    public void init() {
        currentAlbum = new Album();
        currentAlbum.setOwner(selectedUser);
    }

    public Album getCurrentAlbum() {
        return currentAlbum;
    }
    
    public List<Album> getAlbumsByOwner(SempicUser user) {
        return this.albumDAO.findByOwner(user);
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }
    
    public void setCurrentAlbum(Long albumID) throws ResourceNotFoundException {
        this.currentAlbum = albumDAO.readEager(albumID);
    }
    
    public String create() {        
        try {
            albumDAO.create(currentAlbum);
        } 
        catch (SempicModelUniqueException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("un utilisateur avec cette adresse mail existe déjà"));
            return "failure";
        }
        catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        return "success";
    }
    
    public String delete(Long albumID) throws SempicModelException {
        try {
            albumDAO.deleteById(albumID);
        } catch (SempicModelUniqueException e) {
            return "failure";
        }
        return "main-page?faces-redirect=true";
    }
    
    public String albumDetails(Long albumID) {
        return "album?faces-redirect=true&albumID="+albumID;
    }
    
    public List<Photo> getPhotos() {
        return null;
    }
}
