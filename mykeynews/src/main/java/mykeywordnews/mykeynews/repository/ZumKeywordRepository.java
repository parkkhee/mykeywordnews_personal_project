package mykeywordnews.mykeynews.repository;

import mykeywordnews.mykeynews.domain.NaverKeyword;
import mykeywordnews.mykeynews.domain.ZumKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZumKeywordRepository extends JpaRepository<ZumKeyword, Long> {
}
