package org.api.persistence;

import java.net.URL;

import org.api.persistence.model.LinkEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends CrudRepository<LinkEntity, String> {

  @Query("SELECT l FROM link l WHERE mainLink=:mainLink")
  public LinkEntity findLinkByMainLink(@Param("mainLink") URL mainLink);

  @Query("SELECT l FROM link l WHERE shortenLink=:shortenLink")
  public LinkEntity findLinkByShortenLink(@Param("shortenLink") String shortenLink);
}
