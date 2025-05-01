package soft.test.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PriorityQueue;

@Service
public class CalculateService {

    public int getResultByParameter(PriorityQueue<Integer> data, int n) {


        if (data == null || data.isEmpty() || data.size()< n) {
            throw new RuntimeException("Количество элементов в массиве меньше чем n, или массив пустой");
        }
        PriorityQueue<Integer> tempQueue = new PriorityQueue<>(data);

        int result = 0;
        for (int i = 0; i < n; i++) {
            result = tempQueue.poll();
        }

        return result;
    }

}
