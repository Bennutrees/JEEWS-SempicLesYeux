/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.ResourceNotFoundException;
import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.List;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityGraph;
import javax.persistence.Query;

/**
 *
 * @author benjamin
 */
@Stateful
public class AlbumFacade extends AbstractJpaFacade<Long,Album> {
    
    public AlbumFacade() {
        super(Album.class);
    }
    
    @Override
    public Long create(Album album) throws SempicModelException {
        return super.create(album);
    }
    
    @Override
    public void deleteById(Long albumID) throws SempicModelException {
        try {
            super.deleteById(albumID);
        } catch (SempicModelUniqueException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }
    
    public Album readEager(long id) {
        EntityGraph entityGraph = this.getEntityManager().getEntityGraph("graph.Album.title-owner");
        return (Album) getEntityManager().createQuery("SELECT album FROM Album album WHERE album.id=:id")
            .setParameter("id", id)
            .setHint("javax.persistence.fetchgraph", entityGraph)
            .getSingleResult();
    }
    
    @Override
    public List<Album> findAll() {
        EntityGraph entityGraph = this.getEntityManager().getEntityGraph("graph.Album.title-owner");
        return getEntityManager().createQuery(this.findAllQuery())
            .setHint("javax.persistence.fetchgraph", entityGraph)
            .getResultList();
    }
    
    public List<Album> findByOwner(SempicUser owner) {
        Query q = getEntityManager().createNamedQuery("findOwnerAlbums");
        q.setParameter("owner", owner);
        return q.getResultList();
    }
}
