package com.g200001.dutyapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BillAccount.
 */
@Entity
@Table(name = "T_BILLACCOUNT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BillAccount implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "company_name")
    private String company_name;

    @OneToMany(mappedBy = "billAccount")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Service> services = new HashSet<>();
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillAccount billAccount = (BillAccount) o;

        if (id != null ? !id.equals(billAccount.id) : billAccount.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
    	return id==null? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "BillAccount{" +
                "id=" + id +
                ", company_name='" + company_name + "'" +
                '}';
    }
}
