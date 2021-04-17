package com.cnt.domein.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity {
private String name;
private Currency localCurrency;
private Set<Country> neighbors;

    public Country() {
        this.neighbors = new HashSet<>();
    }
    @Column(name = "names",nullable = false,unique = true)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id",referencedColumnName="id")
    public Currency getLocalCurrency() {
        return this.localCurrency;
    }

    public void setLocalCurrency(Currency localCurrency) {
        this.localCurrency = localCurrency;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="country_neighbours",
            joinColumns={@JoinColumn(name="countryId")},
            inverseJoinColumns={@JoinColumn(name="neighborsId")})
    public Set<Country> getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(Set<Country> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(Country neighbor){
        this.getNeighbors().add(neighbor);
        neighbor.getNeighbors().add(this);
    }
}
