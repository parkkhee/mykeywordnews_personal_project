package mykeywordnews.mykeynews.repository;

import mykeywordnews.mykeynews.domain.NateKeyword;
import mykeywordnews.mykeynews.domain.RealtimeKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtimeKeywordRepository extends JpaRepository<RealtimeKeyword, Long> {

    RealtimeKeyword findByRtkeywordNo(Long rtkeywordId);
}
