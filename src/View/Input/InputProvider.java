package View.Input;

public enum InputProvider {
    Up("w"),
    Down("s"),
    Left("a"),
    Right("d"),
    CastAbility("e"),
    Wait("q");

    private String key;

    InputProvider(String key) {
        this.key = key;
    }

    // getter method
    public String GetKey() {
        return this.key;
    }

    public String getRegex() {
        String regex = "";
        for (InputProvider inp : InputProvider.values()) {
            regex += inp.GetKey();
        }
        return regex;
    }

    public static InputProvider FindByKey(String key) {
        for (InputProvider v : values()) {
            if (v.key.equals(key)) {
                return v;
            }
        }
        return null;
    }
}
