package com.g200001.dutyapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Service.
 */
@Entity
@Table(name = "T_SERVICE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Service implements Serializable {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "service_name")
    private String service_name;

    @Column(name = "api_key")
    private String api_key;

    @Column(name = "service_type")
    private Integer service_type;

    @Column(name = "is_deleted")
    private Boolean is_deleted;

    @ManyToOne
    private EscalationPolicy escalationPolicy;

	@OneToMany(mappedBy = "service")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Incident> incidents = new HashSet<>();
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public Integer getService_type() {
        return service_type;
    }

    public void setService_type(Integer service_type) {
        this.service_type = service_type;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public EscalationPolicy getEscalationPolicy() {
        return escalationPolicy;
    }

    public void setEscalationPolicy(EscalationPolicy escalationPolicy) {
        this.escalationPolicy = escalationPolicy;
    }
	
	public Set<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(Set<Incident> incidents) {
        this.incidents = incidents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Service service = (Service) o;

        if (id != null ? !id.equals(service.id) : service.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", user_id='" + user_id + "'" +
                ", service_name='" + service_name + "'" +
                ", api_key='" + api_key + "'" +
                ", service_type='" + service_type + "'" +
                ", is_deleted='" + is_deleted + "'" +
                '}';
    }
}
