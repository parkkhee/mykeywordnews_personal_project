package mykeywordnews.mykeynews.repository;

import mykeywordnews.mykeynews.domain.NaverKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<NaverKeyword, Long> {
}
