package org.api.persistence;

import java.util.List;

import org.api.persistence.model.VisitEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends CrudRepository<VisitEntity, Long> {

  @Query("SELECT v FROM visit v WHERE link_id=:linkid")
  public List<VisitEntity> findVisitByLink(@Param("linkid") String linkid);
}
