package com.mozhotels.srit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.mozhotels.srit.domain.enumeration.GActivityArea;

/**
 * A InstanceActivity.
 */
@Entity
@Table(name = "instance_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceactivity")
public class InstanceActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_activity_name", nullable = false)
    private String instanceActivityName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private GActivityArea area;

    @Lob
    @Column(name = "photo_principal")
    private byte[] photoPrincipal;

    @Column(name = "photo_principal_content_type")    
    private String photoPrincipalContentType;

    @ManyToOne
    private InstanceActivityType instanceActivityType;

    @ManyToOne
    private InstanceTur instanceTur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceActivityName() {
        return instanceActivityName;
    }

    public void setInstanceActivityName(String instanceActivityName) {
        this.instanceActivityName = instanceActivityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GActivityArea getArea() {
        return area;
    }

    public void setArea(GActivityArea area) {
        this.area = area;
    }

    public byte[] getPhotoPrincipal() {
        return photoPrincipal;
    }

    public void setPhotoPrincipal(byte[] photoPrincipal) {
        this.photoPrincipal = photoPrincipal;
    }

    public String getPhotoPrincipalContentType() {
        return photoPrincipalContentType;
    }

    public void setPhotoPrincipalContentType(String photoPrincipalContentType) {
        this.photoPrincipalContentType = photoPrincipalContentType;
    }

    public InstanceActivityType getInstanceActivityType() {
        return instanceActivityType;
    }

    public void setInstanceActivityType(InstanceActivityType instanceActivityType) {
        this.instanceActivityType = instanceActivityType;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceActivity instanceActivity = (InstanceActivity) o;
        if(instanceActivity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceActivity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceActivity{" +
            "id=" + id +
            ", instanceActivityName='" + instanceActivityName + "'" +
            ", description='" + description + "'" +
            ", area='" + area + "'" +
            ", photoPrincipal='" + photoPrincipal + "'" +
            ", photoPrincipalContentType='" + photoPrincipalContentType + "'" +
            '}';
    }
}
