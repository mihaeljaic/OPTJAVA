package hr.fer.zemris.optjava.dz10.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IndexComparator<T> implements Comparator<Integer> {
    private final List<T> items;
    private final Comparator<T> comparator;

    public IndexComparator(List<T> items, Comparator<T> comparator) {
        this.items = new ArrayList<>(items);
        this.comparator = comparator;
    }

    public List<Integer> createIndexedList() {
        List<Integer> indexes = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            indexes.add(i);
        }

        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2) {
        return comparator.compare(items.get(index1), items.get(index2));
    }
}
