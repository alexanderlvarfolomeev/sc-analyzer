import java.util.Optional;

public class PreMain {
}

public class MainWithError extends PreMain {

    public MainWithError() {
        super();
        int x = 1, y = 0;
        int z;
    }
    public static void doSMTH() {
        Optional.of(true).ifPresent(System.out::println);
    }
    public static void main(String[] args) {
        new MainWithError().doSMTH();
        System.out.println("Hello world!");
    }
}
