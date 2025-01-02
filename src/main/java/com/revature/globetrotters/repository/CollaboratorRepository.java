package com.revature.globetrotters.repository;

import com.revature.globetrotters.entity.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Collaborator.CollaboratorId> {
    @Query("""
            SELECT c
            FROM Collaborator c
            WHERE c.id.travelPlanId = :travelPlanId""")
    List<Collaborator> findAllByPlanId(@Param("travelPlanId") Integer travelPlanId);
}
