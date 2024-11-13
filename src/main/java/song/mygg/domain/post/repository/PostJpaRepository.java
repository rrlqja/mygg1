package song.mygg.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.mygg.domain.post.entity.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

}
