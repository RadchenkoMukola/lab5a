import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Army implements Runnable {
    private static final AtomicBoolean finished = new AtomicBoolean(false); // Флаг для відстеження завершення процесу
    private static final List<Boolean> finishedPart = new ArrayList<>(); // Список для відстеження завершення кожної частини

    private final int[] recruits; // Масив рекрутів
    private final Let let; // Об'єкт типу Let для синхронізації

    private final int partIndex; // Індекс поточної частини
    private final int leftIndex; // Ліва межа діапазону
    private final int rightIndex; // Права межа діапазону

    public Army(int[] recruits, Let let, int partIndex, int leftIndex, int rightIndex) {
        this.recruits = recruits;
        this.let = let;
        this.partIndex = partIndex;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    public void run() {
        while (!finished.get()) {
            boolean currentPartFinished = finishedPart.get(partIndex); // Перевірка, чи поточна частина завершила обробку
            if (!currentPartFinished) {
                if (partIndex == 0) System.out.println(Arrays.toString(recruits)); // Виведення масиву рекрутів для першої частини
                boolean isFormatted = true;
                for (int i = leftIndex; i < rightIndex - 1; i++) {
                    if (recruits[i] != recruits[i+1]){
                        if(recruits[i] == 1)
                            recruits[i] = 0;
                        else
                            recruits[i] = 1;
                        isFormatted = false;
                    }
                }
                if(isFormatted) finish(); // Якщо діапазон однорідний, викликається метод finish
            }
            try {
                let.await(); // Очікування дозволу від об'єкта Let
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void finish() {
        finishedPart.set(partIndex, true); // Позначаємо поточну частину як завершену
        for (boolean part: finishedPart)
            if (!part) return; // Перевірка, чи всі частини завершили обробку
        finished.set(true); // Якщо всі частини завершили обробку, встановлюємо флаг завершення процесу
    }

    public static void fillFinishedArray(int numberOfParts) {
        for (int i = 0; i < numberOfParts; i++) finishedPart.add(false); // Ініціалізація списку для відстеження завершення кожної частини
    }
}

