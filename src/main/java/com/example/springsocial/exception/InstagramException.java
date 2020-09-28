package com.example.springsocial.exception;

public class InstagramException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private final String message;

        public InstagramException(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
}
