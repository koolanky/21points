package com.jhipster.health.repository;

import com.jhipster.health.domain.Preferences;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Preferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferencesRepository extends JpaRepository<Preferences,Long> {

	Optional<Preferences> findOneByUserLogin(String login);
}
