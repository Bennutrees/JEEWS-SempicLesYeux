/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.Photo;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @author benjamin
 */
@Stateful
public class PhotoFacade extends AbstractJpaFacade<Long,Photo>{
    
    @Inject
    private PhotoStorage photoStorage;
    
    public PhotoFacade() {
        super(Photo.class);
    }
    
    public void create(Photo photo, InputStream donnee) throws SempicModelException {
        super.create(photo);
        try {
            photoStorage.savePicture(Paths.get(String.valueOf(photo.getAlbum().getId()), String.valueOf(photo.getId())), donnee);
        } catch (SempicException e) {
            throw new SempicModelException("Photo could not be uploaded");
        }
    }
    
    @Override
    public void delete(Photo photo) throws SempicModelException {
        super.delete(photo);
        try {
            photoStorage.deletePicture(Paths.get(String.valueOf(photo.getAlbum().getId()), String.valueOf(photo.getId())));
        }catch (SempicException e) {
            Logger.getLogger(PhotoFacade.class.getName()).log(Level.INFO, null, e);
        }
    }
    
    public List<Photo> findPhotosOfAlbum(Album album) throws SempicModelException {
        Query q = getEntityManager().createNamedQuery("findPhotosByAlbum");
        q.setParameter("album", album);
        return q.getResultList();
    }
    
    public Photo findPhotoById(Long id) {
        Query q = getEntityManager().createNamedQuery("findPhotoById");
        q.setParameter("id", id);
        return (Photo) q.getSingleResult();
    }
}
