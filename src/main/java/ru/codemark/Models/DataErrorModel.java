package ru.codemark.Models;

import java.util.List;
import java.util.Objects;

public class DataErrorModel {
    private boolean success;
    private List<String> errors;

    public DataErrorModel(boolean success, List<String> errors) {
        this.success = success;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataErrorModel that = (DataErrorModel) o;
        return success == that.success &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, errors);
    }
}
