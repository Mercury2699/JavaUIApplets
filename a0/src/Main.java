public class Main {
    public static void main(String[] args) {
        new Main();
    }

    Main() {
        System.out.println("=== Hello World! ===");
        SystemInfo info = new SystemInfo();
        System.out.print(info.toString());
    }
}