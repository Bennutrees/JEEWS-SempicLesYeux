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
import fr.uga.miashs.sempic.dao.PhotoFacade;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.qualifiers.SelectedAlbum;
import fr.uga.miashs.sempic.qualifiers.SelectedPhoto;
import fr.uga.miashs.sempic.rdf.SempicRDFStore;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private AlbumFacade albumDAO;
    
    @Inject
    private PhotoFacade photoDao;
    @EJB
    private SempicRDFStore rdfStore;
    
    private Album currentAlbum;
    private List<Part> files;
    
    private Part image;
    
    private String albumId;
    
    public PhotoBeans(){
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    public String getAlbumId() {
        return this.albumId;
    }
    
    public void setAlbumId(String value) {
        this.albumId = value;
    }
    
    public Part getImage() {
        return image;
    }
    
    public void setImage(Part image) {
        this.image = image;
    }

    public List<Part> getFiles() {
        return files;
    }

    public void setFiles(List<Part> files) {
        this.files = files;
    }

    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }
    
    public void setCurrentAlbum(Long albumID) throws ResourceNotFoundException {
        this.currentAlbum = this.albumDAO.readEager(albumID);
    }
    
    public String create() throws IOException, SempicModelException {
        if (currentAlbum != null) {
            System.out.println("fr.uga.miashs.sempic.backingbeans.PhotoBeans.create()");
            System.out.println("fr.uga.miashs.sempic.backingbeans.PhotoBeans.create()");
            System.out.println("fr.uga.miashs.sempic.backingbeans.PhotoBeans.create()");
            /*boolean allFilesUploaded = true;
            for (Part file : files) {
                Photo newPhoto = new Photo();
                newPhoto.setAlbum(currentAlbum);
                try {
                    photoDao.create(newPhoto, file.getInputStream());
                    rdfStore.createPhoto(newPhoto.getId(), currentAlbum.getId(), currentAlbum.getOwner().getId());
                } catch (SempicModelException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data error"));
                    allFilesUploaded = false;
                } catch (IOException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not upload every photo"));
                    allFilesUploaded = false;
                }
            }
            return allFilesUploaded ? "success" : "failure";*/
            Photo photo = new Photo();
            photo.setAlbum(currentAlbum);
            if(photoDao == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Missing photo Dao"));
                return "failure";
            }
            photoDao.create(photo, image.getInputStream());
            System.out.println(image.getInputStream());
            if(rdfStore == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Missing rdf Store"));
                return "failure";
            }
            rdfStore.createPhoto(photo.getId(), currentAlbum.getId(), currentAlbum.getOwner().getId());
            return "success";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Missing Album Id"));
            return "failure";
        }
        
    }
    
    public String delete(Long id) throws SempicModelException  {
        try {
            photoDao.deleteById(id);
            rdfStore.deletePhoto(id);
        } catch (SempicModelUniqueException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data error"));
            return "failure";
        }
        return "album?faces-redirect=true";
    }
}
