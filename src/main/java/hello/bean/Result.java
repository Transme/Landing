package hello.bean;

public abstract class Result<T> {
    public enum ResultState{
        OK("ok"),
        FAIL("fail");

        private String state;
        public String getState() {
            return state;
        }
        ResultState(String state) {
            this.state = state;
        }
    }

    ResultState state;
    String message;
    T data;

    protected Result(ResultState state, String message, T data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public ResultState getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
