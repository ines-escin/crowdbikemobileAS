package br.ufpe.cin.contexto.bikecidadao.cucumber.screens;

public class BaseScreen {

    protected static final String SCREENSHOT_TAG = "invalid-screen";

    public <T extends BaseScreen> T is(Class<T> type) {
        if (type.isInstance(this)) {
            return type.cast(this);
        } else {
            throw new InvalidPageException("Invalid screen type. Expected: " + type.getSimpleName() + ", but got: " + this.getClass().getSimpleName());
        }
    }

    public static class InvalidPageException extends RuntimeException {

        public InvalidPageException(final String message) {
            super(message);
        }
    }
}