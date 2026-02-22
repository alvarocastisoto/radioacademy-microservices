-- V1__init_catalog_tables.sql
-- 1. Tabla Courses
CREATE TABLE courses (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19, 2) NOT NULL,
    -- NUMERIC mapea perfecto con BigDecimal
    hours INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    cover_image VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- 2. Tabla Modules
CREATE TABLE modules (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    order_index INTEGER NOT NULL,
    course_id UUID NOT NULL,
    CONSTRAINT fk_module_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);
-- 3. Tabla Lessons
CREATE TABLE lessons (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    video_url VARCHAR(255),
    pdf_url VARCHAR(255),
    duration INTEGER,
    order_index INTEGER NOT NULL,
    module_id UUID NOT NULL,
    CONSTRAINT fk_lesson_module FOREIGN KEY (module_id) REFERENCES modules (id) ON DELETE CASCADE
);
-- 4. Tabla Lesson Progress
CREATE TABLE lesson_progress (
    id UUID PRIMARY KEY,
    lesson_id UUID NOT NULL,
    user_id UUID NOT NULL,
    -- Solo guardamos la referencia al usuario del auth-service
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_progress_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON DELETE CASCADE,
    CONSTRAINT uq_user_lesson UNIQUE (user_id, lesson_id)
);
-- 5. Tabla Quizzes
CREATE TABLE quizzes (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    module_id UUID NOT NULL UNIQUE,
    -- OneToOne con Module
    CONSTRAINT fk_quiz_module FOREIGN KEY (module_id) REFERENCES modules (id) ON DELETE CASCADE
);
-- 6. Tabla Questions
CREATE TABLE questions (
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    points INTEGER NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    quiz_id UUID NOT NULL,
    CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE CASCADE
);
-- 7. Tabla Options
CREATE TABLE options (
    id UUID PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    question_id UUID NOT NULL,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE
);
-- 8. Tabla Quiz Attempts
CREATE TABLE quiz_attempts (
    id UUID PRIMARY KEY,
    quiz_id UUID NOT NULL,
    user_id UUID NOT NULL,
    -- Solo referencia al usuario
    score DOUBLE PRECISION,
    passed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_attempt_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE CASCADE
);
-- 9. Tabla Quiz Answers
CREATE TABLE quiz_answers (
    id UUID PRIMARY KEY,
    attempt_id UUID,
    question_id UUID,
    selected_option_id UUID,
    is_correct BOOLEAN NOT NULL,
    CONSTRAINT fk_answer_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_option FOREIGN KEY (selected_option_id) REFERENCES options (id) ON DELETE
    SET NULL
);