package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper {
    Category getCategory(String categoryId);
}
