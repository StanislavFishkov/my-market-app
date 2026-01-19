package ru.yandex.practicum.mymarket.utils;

import java.util.ArrayList;
import java.util.List;

public final class PartitionUtils {

    private PartitionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> List<List<T>> chunkList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i += chunkSize) {
            int end = Math.min(size, i + chunkSize);
            chunks.add(new ArrayList<>(list.subList(i, end)));
        }
        return chunks;
    }

    public static <T> List<List<T>> chunkAndPad(List<T> list, int chunkSize, T emptyElement) {
        List<List<T>> rows = PartitionUtils.chunkList(list, chunkSize);

        for (List<T> row : rows) {
            while (row.size() < chunkSize) {
                row.add(emptyElement);
            }
        }
        return rows;
    }
}