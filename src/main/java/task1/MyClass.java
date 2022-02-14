package task1;

public class MyClass {
    private final Object OBJECT = new Object();
    private volatile char currentChar = 'A';

    //2-ой вариант
    public void printChar(char ch, char ch2) {
        synchronized (OBJECT) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentChar != ch) {
                        OBJECT.wait();
                    }
                    System.out.print(ch);
                    currentChar = ch2;
                    OBJECT.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //1
    public void printA() {
        synchronized (OBJECT) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentChar != 'A') {
                        OBJECT.wait();
                    }
                    System.out.print("A");
                    currentChar = 'B';
                    OBJECT.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (OBJECT) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentChar != 'B') {
                        OBJECT.wait();
                    }
                    System.out.print("B");
                    currentChar = 'C';
                    OBJECT.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (OBJECT) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentChar != 'C') {
                        OBJECT.wait();
                    }
                    System.out.print("C");
                    currentChar = 'A';
                    OBJECT.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
