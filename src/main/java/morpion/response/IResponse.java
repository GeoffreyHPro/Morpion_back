package morpion.response;

public interface IResponse<T> {
    T getData();
    String getMessage();
}
