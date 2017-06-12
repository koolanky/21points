package com.jhipster.health.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.jhipster.health.domain.enumeration.Units;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "preferences")
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 10)
    @Max(value = 21)
    @Column(name = "weeklyGoals", nullable = false)
    private Integer weeklyGoals;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weightUnits", nullable = false)
    private Units weightUnits;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeeklyGoals() {
        return weeklyGoals;
    }

    public Preferences weeklyGoals(Integer weeklyGoals) {
        this.weeklyGoals = weeklyGoals;
        return this;
    }

    public void setWeeklyGoals(Integer weeklyGoals) {
        this.weeklyGoals = weeklyGoals;
    }

    public Units getWeightUnits() {
        return weightUnits;
    }

    public Preferences weightUnits(Units weightUnits) {
        this.weightUnits = weightUnits;
        return this;
    }

    public void setWeightUnits(Units weightUnits) {
        this.weightUnits = weightUnits;
    }

    public User getUser() {
        return user;
    }

    public Preferences user(User user) {
        this.user = user;
        return this;
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
        Preferences preferences = (Preferences) o;
        if (preferences.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferences.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + getId() +
            ", weeklyGoals='" + getWeeklyGoals() + "'" +
            ", weightUnits='" + getWeightUnits() + "'" +
            "}";
    }
}
