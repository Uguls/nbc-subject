package nbc.nbcsubject.domain.user.exception;

import lombok.Getter;
import nbc.nbcsubject.common.exception.BaseException;
import nbc.nbcsubject.common.response.ResponseCode;

@Getter
public class UserException extends BaseException {
    private final ResponseCode responseCode;

    public UserException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
