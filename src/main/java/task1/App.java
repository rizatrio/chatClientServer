package task1;

public class App {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();

        new Thread(myClass::printA).start();
        new Thread(myClass::printB).start();
        new Thread(myClass::printC).start();

        //2-ой вариант
//        new Thread(() -> myClass.printChar('A', 'B')).start();
//        new Thread(() -> myClass.printChar('B', 'C')).start();
//        new Thread(() -> myClass.printChar('C', 'A')).start();
    }
}
