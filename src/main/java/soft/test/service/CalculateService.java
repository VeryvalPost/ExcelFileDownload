package soft.test.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

@Service
public class CalculateService {

    public int getResultByParameter(List<Integer> data, int n) {
        if (data == null || data.isEmpty() || data.size() < n || n <= 0) {
            throw new RuntimeException("Недопустимое значение n или пустой список");
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
        int pivot = arr[right];
        int i = left;

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