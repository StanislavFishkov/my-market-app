package ru.yandex.practicum.mymarket.repository.item.criteria;

import org.springframework.data.relational.core.query.Criteria;

public final class ItemCriteria {
    private ItemCriteria() {
        throw new IllegalStateException("Criteria class");
    }

    public static Criteria searchInTitleOrDescription(String search) {
        if (search == null) return Criteria.empty();

        String pattern = "%" + search.toLowerCase() + "%";
        return Criteria.where("title").like(pattern).ignoreCase(true)
                .or(Criteria.where("description").like(pattern).ignoreCase(true));
    }
}