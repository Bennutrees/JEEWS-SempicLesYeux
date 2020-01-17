/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
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
    private PhotoFacade photoDao;
    @EJB
    private SempicRDFStore rdfStore;
    
    @Inject
    @SelectedAlbum
    private Album currentAlbum;
    private List<Part> files;
    
    public PhotoBeans(){
    }
    
    @PostConstruct
    public void init() {
        
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
    
    public String create() throws IOException, SempicModelException {
        if (currentAlbum != null) {
            boolean allFilesUploaded = true;
            for (Part file : files) {
                Photo newPhoto = new Photo();
                newPhoto.setAlbum(currentAlbum);
                try {
                    photoDao.create(newPhoto, file.getInputStream());
                    rdfStore.createPhoto(newPhoto.getId(), currentAlbum.getOwner().getFirstname(), currentAlbum.getOwner().getLastname());
                } catch (SempicModelException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data error"));
                    allFilesUploaded = false;
                } catch (IOException e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could not upload every photo"));
                    allFilesUploaded = false;
                }
            }
            return allFilesUploaded ? "success" : "failure";
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
