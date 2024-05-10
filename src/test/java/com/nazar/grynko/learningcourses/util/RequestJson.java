package com.nazar.grynko.learningcourses.util;

public interface RequestJson {

    String EMPTY_BODY = "{}";

    String LESSON_CORRECT = """
                {
                    "title": "Learn Python in 30 Days",
                    "description": "In-depth course for beginners covering syntax fundamentals, data structures, functions, and modules.",
                    "number": 2,
                    "isFinished": false,
                    "maxMark": 10,
                    "successMark": 5
                }
                """;

    String LESSON_INCORRECT = """
            {
                "title": "Python Basics",
                "description": "This lesson covers the basics of Python syntax, including variables, data types, and operators.",
                "number": "10",
                "isFinished": false,
                "maxMark": -1,
                "successMark": "1000000"
            }
            """;

    String SIGN_IN = """
            {
                "login": "test.user@gmail.com",
                "password": "test.password"
            }
            """;

    String SIGN_UP = """
            {
                "login": "test.user@gmail.com",
                "password": "test.password",
                "firstName": "Nazar",
                "lastName": "Grynko"
            }
            """;

}
