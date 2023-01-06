package mykeywordnews.mykeynews.repository;

import mykeywordnews.mykeynews.domain.BeingKeyword;

import mykeywordnews.mykeynews.domain.NaverKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeingKeywordRepository extends JpaRepository<BeingKeyword, Long> {

//    List<BeingKeyword> findAllByKeywords();
}
