package com.codeup.bitebook.repositories;


import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.models.RecipePage;
import com.codeup.bitebook.models.RecipeSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class RecipeCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public RecipeCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Recipe> findAllWithFilters(RecipePage recipePage,
                                           RecipeSearchCriteria recipeSearchCriteria){
        CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);
        Predicate predicate = getPredicate(recipeSearchCriteria, recipeRoot);
        criteriaQuery.where(predicate);
        setOrder(recipePage, criteriaQuery, recipeRoot);

        TypedQuery<Recipe> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(recipePage.getPageNumber() * recipePage.getPageSize());
        typedQuery.setMaxResults(recipePage.getPageSize());

        Pageable pageable = getPageable(recipePage);

        long recipesCount = getRecipesCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, recipesCount);
    }

    private Predicate getPredicate(RecipeSearchCriteria recipeSearchCriteria,
                                   Root<Recipe> recipeRoot){
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(recipeSearchCriteria.getTitle())){
            predicates.add(
                    criteriaBuilder.like(recipeRoot.get("title"),
                            "%" + recipeSearchCriteria.getTitle() + "%")
            );
        }
        if(Objects.nonNull(recipeSearchCriteria.getDietary())){
            predicates.add(
                    criteriaBuilder.like(recipeRoot.get("dietary"),
                            "%" + recipeSearchCriteria.getDietary() + "%")
            );
        }
        if(Objects.nonNull(recipeSearchCriteria.getDifficulty())){
            predicates.add(
                    criteriaBuilder.like(recipeRoot.get("difficulty"),
                            "%" + recipeSearchCriteria.getDifficulty() + "%")
            );
        }
        if(Objects.nonNull(recipeSearchCriteria.getRegion())){
            predicates.add(
                    criteriaBuilder.like(recipeRoot.get("region"),
                            "%" + recipeSearchCriteria.getRegion() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(RecipePage recipePage,
                          CriteriaQuery<Recipe> criteriaQuery,
                          Root<Recipe> recipeRoot) {
        if(recipePage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(recipeRoot.get(recipePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(recipeRoot.get(recipePage.getSortBy())));
        }
    }
    private Pageable getPageable(RecipePage recipePage) {
        Sort sort = Sort.by(recipePage.getSortDirection(), recipePage.getSortBy());
        return PageRequest.of(recipePage.getPageNumber(), recipePage.getPageSize(), sort);
    }
    private long getRecipesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Recipe> countRoot = countQuery.from(Recipe.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
