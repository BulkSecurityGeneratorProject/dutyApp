package com.g200001.dutyapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g200001.dutyapp.domain.util.CustomDateTimeDeserializer;
import com.g200001.dutyapp.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Incident.
 */
@Entity
@Table(name = "T_INCIDENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Incident implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "create_time", nullable = false)
    private DateTime create_time;

    @Column(name = "state")
    private Integer state;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "ack_time", nullable = false)
    private DateTime ack_time;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "resolve_time", nullable = false)
    private DateTime resolve_time;

    @Column(name = "description")
    private String description;

    @Column(name = "detail")
    private String detail;

    @Column(name = "incident_no")
    private Long incident_no;

    @ManyToOne(fetch=FetchType.EAGER,optional=true,cascade=CascadeType.PERSIST)
    //@ManyToOne
    private Service service;

    @OneToMany(mappedBy = "incident")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Alert> alerts = new HashSet<>();

    @ManyToOne
    private User ack_user;

    @ManyToOne
    private User resolve_user;

    @ManyToOne
    private User assign_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(DateTime create_time) {
        this.create_time = create_time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public DateTime getAck_time() {
        return ack_time;
    }

    public void setAck_time(DateTime ack_time) {
        this.ack_time = ack_time;
    }

    public DateTime getResolve_time() {
        return resolve_time;
    }

    public void setResolve_time(DateTime resolve_time) {
        this.resolve_time = resolve_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getIncident_no() {
        return incident_no;
    }

    public void setIncident_no(Long incident_no) {
        this.incident_no = incident_no;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Set<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(Set<Alert> alerts) {
        this.alerts = alerts;
    }

    public User getAck_user() {
        return ack_user;
    }

    public void setAck_user(User user) {
        this.ack_user = user;
    }

    public User getResolve_user() {
        return resolve_user;
    }

    public void setResolve_user(User user) {
        this.resolve_user = user;
    }

    public User getAssign_user() {
        return assign_user;
    }

    public void setAssign_user(User assign_user) {
        this.assign_user = assign_user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Incident incident = (Incident) o;

        if (id != null ? !id.equals(incident.id) : incident.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id=" + id +
                ", create_time='" + create_time + "'" +
                ", state='" + state + "'" +
                ", ack_time='" + ack_time + "'" +
                ", resolve_time='" + resolve_time + "'" +
                ", description='" + description + "'" +
                ", detail='" + detail + "'" +
                ", incident_no='" + incident_no + "'" +
                '}';
    }
}
