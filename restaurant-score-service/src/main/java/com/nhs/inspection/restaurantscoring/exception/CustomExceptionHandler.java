package com.nhs.inspection.restaurantscoring.exception;

import com.nhs.inspection.restaurantscoring.constants.ErrorConstants;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class CustomExceptionHandler {

    public void handleSqlRepositoryExceptions(Exception ex) {
        String message = "Error while accessing database";
        if (ex instanceof DataAccessException) {
            // BadSqlGrammarException is implementation of DataAccessException
            if (ex instanceof BadSqlGrammarException)
                throwInternalException(ex, ErrorConstants.DatabaseErrorCode.DB_QUERY_EXECUTION_FAILED.name(), message, HttpStatus.INTERNAL_SERVER_ERROR);
            if (ex instanceof DataAccessResourceFailureException)
                throwInternalException(ex, ErrorConstants.DatabaseErrorCode.DB_CONNECTION_TIME_OUT.name(), message, HttpStatus.SERVICE_UNAVAILABLE);
            // All other DataAccessException implementations will be assigned connectivity error code by default. More implementations of DataAccessException can be handled if need be.
            throwInternalException(ex, ErrorConstants.DatabaseErrorCode.DB_ACCESS_ERROR.name(), message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //For RowMapper and Unable to connect to database/table exceptions
        else if (ex instanceof SQLException) {
            if (ex instanceof SQLTimeoutException)
                throwInternalException(ex, ErrorConstants.DatabaseErrorCode.DB_SQL_ERROR.name(), message, HttpStatus.SERVICE_UNAVAILABLE);
            throwInternalException(ex, ErrorConstants.DatabaseErrorCode.DB_SQL_ERROR.name(), message, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            throwInternalException(ex, "Internal Unknown Error", message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected void throwInternalException(Exception ex, String code, String message, HttpStatus httpStatus) {
        throw new CustomRuntimeException(code, message, Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC"))), httpStatus, ex);
    }
}
