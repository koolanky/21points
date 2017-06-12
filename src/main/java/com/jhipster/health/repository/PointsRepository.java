package com.jhipster.health.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jhipster.health.domain.Points;

/**
 * Spring Data JPA repository for the Points entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointsRepository extends JpaRepository<Points,Long> {

    @Query("select points from Points points where points.user.login = ?#{principal.username} order by points.date desc")
    //List<Points> findByUserIsCurrentUser();
    
    Page<Points> findByUserIsCurrentUser(Pageable pageable);
    
    List<Points> findAllByDateBetween(LocalDate firstDate,LocalDate secondDate);
    
    Page<Points> findAllByOrderByDateDesc(Pageable pageable);
}
