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
 * A Alert.
 */
@Entity
@Table(name = "T_ALERT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Alert implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "alert_time", nullable = false)
    private DateTime alert_time;

    @ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.DETACH)
    //@JsonIgnore
    private Incident incident;

    @ManyToOne (fetch=FetchType.EAGER,cascade=CascadeType.DETACH)
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(DateTime alert_time) {
        this.alert_time = alert_time;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Alert alert = (Alert) o;

        if (id != null ? !id.equals(alert.id) : alert.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
    	return id==null? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", alert_time='" + alert_time + "'" +
                '}';
    }
}
