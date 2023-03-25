package com.maturi.repository.article.tag;

import com.maturi.entity.article.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
  public Tag findByName(String name);
}
