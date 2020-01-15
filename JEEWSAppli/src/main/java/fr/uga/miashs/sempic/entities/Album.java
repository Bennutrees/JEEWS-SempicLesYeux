/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author benjamin
 */
@Entity
@Table(name = "albums", uniqueConstraints = {
    @UniqueConstraint(name = "UniqueAlbumNameForUser", columnNames = {"title", "owner_id"})
})
public class Album implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    @Column(name = "title")
    @NotBlank
    private String title;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "album")
    @Column(name = "photos")
    private List<Photo> photos;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @NotNull
    private SempicUser owner;
    
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    
    public List<Photo> getPhotos() {
        return photos;
    }

    public SempicUser getOwner() {
        return owner;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setOwner(SempicUser owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Album other = (Album) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
