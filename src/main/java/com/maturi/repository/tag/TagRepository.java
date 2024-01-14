package com.maturi.repository.tag;

import com.maturi.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
  public Tag findByName(String name);
}
