public class Let {
    private final int partiesAtStart; // Початкова кількість потоків
    private int partiesAwait; // Кількість потоків, які очікують

    public Let(int parties) {
        this.partiesAtStart = parties; // Ініціалізація початкової кількості потоків
        this.partiesAwait = parties; // Початкова кількість потоків, які очікують
    }

    public synchronized void await() throws InterruptedException {
        partiesAwait--; // Зменшення кількості потоків, які очікують
        if(partiesAwait > 0) this.wait(); // Якщо ще є очікуючі потоки, поточний потік переходить у режим очікування

        partiesAwait = partiesAtStart; // Повернення початкової кількості очікуючих потоків
        notifyAll(); // Сповіщення всіх потоків про готовність до продовження виконання
    }
}

