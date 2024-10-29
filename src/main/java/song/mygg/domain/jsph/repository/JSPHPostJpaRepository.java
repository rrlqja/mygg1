package song.mygg.domain.jsph.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg.domain.jsph.entity.JSPHPost;

import java.util.Optional;

@Repository
public interface JSPHPostJpaRepository extends JpaRepository<JSPHPost, Long> {
    @Query("select a from JSPHPost a where a.id = :id")
    Optional<JSPHPost> findById(@Param("id") Long id);

    @Query("select a from JSPHPost a where a.postId = :postId")
    Optional<JSPHPost> findByPostId(@Param("postId") Long postId);
}
