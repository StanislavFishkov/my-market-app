package ru.yandex.practicum.mymarket.repository.item.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.yandex.practicum.mymarket.model.Item;

public final class ItemSpecifications {
    private ItemSpecifications() {
        throw new IllegalStateException("Specification class");
    }

    public static Specification<Item> searchInTitleOrDescription(String search) {
        return (root, query, cb) -> {
            if (search == null) return cb.conjunction();

            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }
}