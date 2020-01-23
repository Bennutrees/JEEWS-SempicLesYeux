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
import java.util.Map;
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
    
    private List<Photo> photos;
    
    private Long albumId;
    
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
    
    public List<Photo> getPhotos() {
        return this.photos;
    }
    
    public Long getAlbumId() {
        return this.albumId;
    }
    
    public void setCurrentAlbum(Album currentAlbum) {
        this.albumId = currentAlbum.getId();
        this.currentAlbum = currentAlbum;
    }
    
    public void setCurrentAlbum(Long albumID) throws ResourceNotFoundException {
        this.currentAlbum = albumDAO.readEager(albumID);
        this.albumId = albumID;
    }
    
    public String create() {        
        try {
            albumDAO.create(currentAlbum);
        } 
        catch (SempicModelUniqueException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Création de l'album impossible"));
            return "failure";
        }
        catch (SempicModelException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return "failure";
        }
        return "success";
    }
    
    public String delete() throws SempicModelException {
        try {
            albumDAO.delete(this.currentAlbum);
            
        } catch (SempicModelUniqueException e) {
            return "failure";
        }
        return "main-page?faces-redirect=true";
    }
    
    public String albumDetails(Long albumID) {
        return "album?faces-redirect=true&albumId="+albumID;
    }
    
    public String newPhoto() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String albumId = params.get("albumId");
        return "create-photo?faces-redirect=true&albumId="+albumId;
    }
}
