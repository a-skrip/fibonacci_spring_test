package ru.skriplex.springhotelapplication.exception;

public class HotelIsExistException extends RuntimeException {
    public HotelIsExistException(String message) {
        super(message);
    }
}
