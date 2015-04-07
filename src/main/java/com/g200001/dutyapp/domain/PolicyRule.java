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
 * A PolicyRule.
 */
@Entity
@Table(name = "T_POLICYRULE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PolicyRule implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "escalate_time")
    private Integer escalate_time;

    @ManyToOne (fetch=FetchType.EAGER, cascade=CascadeType.DETACH)
    @JsonIgnore
    private EscalationPolicy escalationPolicy;

    @ManyToMany (fetch=FetchType.EAGER, cascade=CascadeType.DETACH)
    //@JsonIgnore
    @JoinTable(
            name = "T_POLICYRULE_USER",
            joinColumns = {@JoinColumn(name = "policyrule_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getEscalate_time() {
        return escalate_time;
    }

    public void setEscalate_time(Integer escalate_time) {
        this.escalate_time = escalate_time;
    }

    public EscalationPolicy getEscalationPolicy() {
        return escalationPolicy;
    }

    public void setEscalationPolicy(EscalationPolicy escalationPolicy) {
        this.escalationPolicy = escalationPolicy;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PolicyRule policyRule = (PolicyRule) o;

        if (id != null ? !id.equals(policyRule.id) : policyRule.id != null) return false;

        if (escalate_time != null ? 
        		!escalate_time.equals(policyRule.escalate_time) 
        		: policyRule.escalate_time != null) 
        	return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        //return id.hashCode();
    	return id==null? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "PolicyRule{" +
                "id=" + id +
                ", sequence='" + sequence + "'" +
                ", escalate_time='" + escalate_time + "'" +
                '}';
    }
}
