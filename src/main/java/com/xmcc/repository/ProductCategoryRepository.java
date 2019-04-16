package com.xmcc.repository;

import com.xmcc.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
//泛型：实体类名称 主键类型
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
}
