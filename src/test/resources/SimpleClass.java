class SimpleClass {
    public void foo() {
        bar();
    }

    private int bar() {
        return outer(0);
    }
}