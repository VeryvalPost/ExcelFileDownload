package soft.test.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class CalculateService {

    private static final Random RANDOM = new Random();

    public int getResultByParameter(List<Integer> data, int n) {
        if (data == null || data.isEmpty() || data.size() < n || n <= 0) {
            throw new RuntimeException("Недопустимое значение n или пустой список");
        }

        if (data.contains(null)) {
            throw new IllegalArgumentException("Список содержит null элементы");
        }

        Integer[] array = data.toArray(new Integer[0]);
        return quickSelect(array, 0, array.length - 1, n - 1);
    }

    private int quickSelect(Integer[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }

        int pivotIndex = partition(arr, left, right);

        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return quickSelect(arr, left, pivotIndex - 1, k);
        } else {
            return quickSelect(arr, pivotIndex + 1, right, k);
        }
    }

    private int partition(Integer[] arr, int left, int right) {
        int index = RANDOM.nextInt(right - left + 1) + left;
        int pivot = arr[index];
        int i = left;
        swap(arr, index, right);

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }

        swap(arr, i, right);
        return i;
    }

    private void swap(Integer[] arr, int i, int j) {
        Integer temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}