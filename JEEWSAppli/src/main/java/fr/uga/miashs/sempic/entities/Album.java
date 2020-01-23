/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author benjamin
 */
@NamedQueries({
    @NamedQuery(
        name = "findOwnerAlbums",
        query = "SELECT DISTINCT album FROM Album album WHERE album.owner=:owner"
    )
})
@NamedEntityGraph(
  name = "graph.Album.title-owner",
  attributeNodes = {
    @NamedAttributeNode("title"),
    @NamedAttributeNode("owner"),
  }
)
@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@Entity
@Table(name = "albums", uniqueConstraints = {
    @UniqueConstraint(name = "UniqueAlbumNameForUser", columnNames = {"title", "owner_id"})
})
public class Album implements Serializable {
    public final static String PREFIX_ALBUM="/albums/";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    @Column(name = "title")
    @NotBlank
    private String title;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "album", cascade = CascadeType.REMOVE)
    @Column(name = "photos")
    private Set<Photo> photos;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @NotNull
    private SempicUser owner;
    
    public long getId() {
        return id;
    }
    
    
    @XmlID
    @XmlAttribute(name="id")
    public String getIdUri() {
        return PREFIX_ALBUM + this.getId();
    }

    public String getTitle() {
        return title;
    }
    
    public Set<Photo> getPhotos() {
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
    
    public void setPhotos(Set<Photo> photos) {
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
