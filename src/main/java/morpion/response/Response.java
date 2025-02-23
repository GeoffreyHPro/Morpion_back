package morpion.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Response<T> implements IResponse<T> {

    private T data;
    private String message;

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
