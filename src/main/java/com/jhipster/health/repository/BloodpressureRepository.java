package com.jhipster.health.repository;

import com.jhipster.health.domain.Bloodpressure;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Bloodpressure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BloodpressureRepository extends JpaRepository<Bloodpressure,Long> {

    @Query("select bloodpressure from Bloodpressure bloodpressure where bloodpressure.user.login = ?#{principal.username} order by bloodpressure.date desc")
    //List<Bloodpressure> findByUserIsCurrentUser();
    
    List<Bloodpressure> findAllByTimestampBetweenOrderByTimestampDesc(LocalDate	firstDate, LocalDate secondDate);
    
    List<Bloodpressure> findAllByTimestampBetweenAndUserLoginOrderByTimestampDesc(LocalDate firstDate, LocalDate secondDate, String login);


}
