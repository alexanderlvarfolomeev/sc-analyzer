import java.util.Optional;

public class PreMain {
}

public class Main extends PreMain {

    public Main() {
        super();
        int x = 1, y = 0;
        int z;
    }
    public static void doSMTH() {
        Optional.of(true).ifPresent(System.out::println);
    }
    public static void main(String[] args) {
        new Main().doSMTH();
        System.out.println("Hello world!");
    }
}
