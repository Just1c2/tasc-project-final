package vn.tass.microservice.model.constants;

public interface WAREHOUSE {
    class STATUS{
        public static final int CREATED = 1;
        public static final int FAIL = 2;
        public static final int SUCCESS = 3;
        public static final int ROLLBACK = 4;
    }
}
