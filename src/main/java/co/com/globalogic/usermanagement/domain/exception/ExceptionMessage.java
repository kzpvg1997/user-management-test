package co.com.globalogic.usermanagement.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    SERVER_ERROR("UME-0001","Internal server error"),
    BAD_REQUEST("UME-0002","Error in body request"),
    DB_SEARCH_ERROR("UME-0003","Error Searching in Data Base"),
    DB_SAVE_ERROR("UME-0004","Error Saving/Updating in Data Base"),
    USER_ALREADY_EXIST("UME-0005","The user is already registered"),
    INVALID_TOKEN("UME-0006","The token is invalid");

    private final String code;
    private final String detail;
}
