package com.mozhotels.srit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.LPictureType;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "picture")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "picture_name", nullable = false)
    private String pictureName;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")    
    private String pictureContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LPictureType type;

    @ManyToOne
    private Province province;

    @ManyToOne
    private InstanceTur instanceTur;

    @ManyToOne
    private InstanceRoomType instanceRoomType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public LPictureType getType() {
        return type;
    }

    public void setType(LPictureType type) {
        this.type = type;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    public InstanceRoomType getInstanceRoomType() {
        return instanceRoomType;
    }

    public void setInstanceRoomType(InstanceRoomType instanceRoomType) {
        this.instanceRoomType = instanceRoomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Picture picture = (Picture) o;
        if(picture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Picture{" +
            "id=" + id +
            ", pictureName='" + pictureName + "'" +
            ", description='" + description + "'" +
            ", picture='" + picture + "'" +
            ", pictureContentType='" + pictureContentType + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
