package nbcc.resto.controller.api.result;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultHandler {

    public static <T> ResponseEntity<Result<T>> handleResult(Result<T> result, HttpStatus successStatus) {
        return handleResult(result, successStatus, HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<Result<T>> handleResult(Result<T> result, HttpStatus successStatus, HttpStatus emptyStatus) {

        if (result.isError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result instanceof ValidatedResult<?> validatedResult && validatedResult.isInvalid()) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, emptyStatus);
        }

        if (result.isSuccessful()) {
            return new ResponseEntity<>(result, successStatus);
        }

        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }
}