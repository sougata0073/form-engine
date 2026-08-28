BEGIN;

-- ============================================================
-- TEMPLATE
-- ============================================================

INSERT INTO form_template.templates (
    id,
    created_at,
    last_modified_at,
    description,
    name,
    title,
    category_id
)
VALUES (
           876200000000000061,
           NOW(),
           NULL,
           'Please submit feedback regarding the course you have just completed, including feedback on course structure, content, and instructor.',
           'Course Evaluation',
           'Course evaluation',
           875826790815802789
       );


-- ============================================================
-- QUESTION TEMPLATES
-- ============================================================

INSERT INTO form_template.question_templates (
    id,
    created_at,
    last_modified_at,
    description,
    order_index,
    question,
    question_type,
    required,
    template_id
)
VALUES
    (
        876200000000000062,
        NOW(),
        NULL,
        NULL,
        0,
        'Class name',
        'SHORT_ANSWER',
        TRUE,
        876200000000000061
    ),
    (
        876200000000000063,
        NOW(),
        NULL,
        NULL,
        1,
        'Instructor',
        'SHORT_ANSWER',
        TRUE,
        876200000000000061
    ),
    (
        876200000000000064,
        NOW(),
        NULL,
        NULL,
        2,
        'Level of effort',
        'MULTIPLE_CHOICE_GRID',
        FALSE,
        876200000000000061
    ),
    (
        876200000000000065,
        NOW(),
        NULL,
        NULL,
        3,
        'Contribution to learning',
        'MULTIPLE_CHOICE_GRID',
        FALSE,
        876200000000000061
    ),
    (
        876200000000000066,
        NOW(),
        NULL,
        NULL,
        4,
        'Skill and responsiveness of the instructor',
        'MULTIPLE_CHOICE_GRID',
        FALSE,
        876200000000000061
    ),
    (
        876200000000000067,
        NOW(),
        NULL,
        NULL,
        5,
        'Course content',
        'MULTIPLE_CHOICE_GRID',
        FALSE,
        876200000000000061
    ),
    (
        876200000000000068,
        NOW(),
        NULL,
        NULL,
        6,
        'What aspects of this course were most useful or valuable?',
        'PARAGRAPH',
        FALSE,
        876200000000000061
    ),
    (
        876200000000000069,
        NOW(),
        NULL,
        NULL,
        7,
        'How would you improve this course?',
        'PARAGRAPH',
        FALSE,
        876200000000000061
    ),
    (
        876200000000000070,
        NOW(),
        NULL,
        NULL,
        8,
        'Why did you choose this course?',
        'MULTIPLE_CHOICE',
        FALSE,
        876200000000000061
    );


-- ============================================================
-- SHORT ANSWER TEMPLATES
-- ============================================================

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000062
    ),
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000063
    );


-- ============================================================
-- MULTIPLE CHOICE GRID #1
-- Level of effort
-- ============================================================

INSERT INTO form_template.multiple_choice_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           876200000000000064
       );

INSERT INTO form_template.multiple_choice_grid_row_templates (
    id,
    row_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES (
           876200000000000071,
           'Level of effort you put into the course',
           0,
           876200000000000064
       );

INSERT INTO form_template.multiple_choice_grid_column_templates (
    id,
    column_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000072,
        'Poor',
        0,
        876200000000000064
    ),
    (
        876200000000000073,
        'Fair',
        1,
        876200000000000064
    ),
    (
        876200000000000074,
        'Satisfactory',
        2,
        876200000000000064
    ),
    (
        876200000000000075,
        'Very good',
        3,
        876200000000000064
    ),
    (
        876200000000000076,
        'Excellent',
        4,
        876200000000000064
    );


-- ============================================================
-- MULTIPLE CHOICE GRID #2
-- Contribution to learning
-- ============================================================

INSERT INTO form_template.multiple_choice_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           876200000000000065
       );

INSERT INTO form_template.multiple_choice_grid_row_templates (
    id,
    row_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000077,
        'Level of skill/knowledge at start of course',
        0,
        876200000000000065
    ),
    (
        876200000000000078,
        'Level of skill/knowledge at end of course',
        1,
        876200000000000065
    ),
    (
        876200000000000079,
        'Level of skill/knowledge required to complete the course',
        2,
        876200000000000065
    ),
    (
        876200000000000080,
        'Contribution of course to your skill/knowledge',
        3,
        876200000000000065
    );

INSERT INTO form_template.multiple_choice_grid_column_templates (
    id,
    column_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000081,
        'Poor',
        0,
        876200000000000065
    ),
    (
        876200000000000082,
        'Fair',
        1,
        876200000000000065
    ),
    (
        876200000000000083,
        'Satisfactory',
        2,
        876200000000000065
    ),
    (
        876200000000000084,
        'Very good',
        3,
        876200000000000065
    ),
    (
        876200000000000085,
        'Excellent',
        4,
        876200000000000065
    );


-- ============================================================
-- MULTIPLE CHOICE GRID #3
-- Skill and responsiveness of the instructor
-- ============================================================

INSERT INTO form_template.multiple_choice_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           876200000000000066
       );

INSERT INTO form_template.multiple_choice_grid_row_templates (
    id,
    row_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000086,
        'Instructor was an effective lecturer/demonstrator',
        0,
        876200000000000066
    ),
    (
        876200000000000087,
        'Presentations were clear and organized',
        1,
        876200000000000066
    ),
    (
        876200000000000088,
        'Instructor stimulated student interest',
        2,
        876200000000000066
    ),
    (
        876200000000000089,
        'Instructor effectively used time during class periods',
        3,
        876200000000000066
    ),
    (
        876200000000000090,
        'Instructor was available and helpful',
        4,
        876200000000000066
    ),
    (
        876200000000000091,
        'Grading was prompt and had useful feedback',
        5,
        876200000000000066
    );

INSERT INTO form_template.multiple_choice_grid_column_templates (
    id,
    column_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000092,
        'Strongly disagree',
        0,
        876200000000000066
    ),
    (
        876200000000000093,
        'Disagree',
        1,
        876200000000000066
    ),
    (
        876200000000000094,
        'Neutral',
        2,
        876200000000000066
    ),
    (
        876200000000000095,
        'Agree',
        3,
        876200000000000066
    ),
    (
        876200000000000096,
        'Strongly agree',
        4,
        876200000000000066
    );


-- ============================================================
-- MULTIPLE CHOICE GRID #4
-- Course content
-- ============================================================

INSERT INTO form_template.multiple_choice_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           876200000000000067
       );

INSERT INTO form_template.multiple_choice_grid_row_templates (
    id,
    row_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000097,
        'Learning objectives were clear',
        0,
        876200000000000067
    ),
    (
        876200000000000098,
        'Course content was organized and well planned',
        1,
        876200000000000067
    ),
    (
        876200000000000099,
        'Course workload was appropriate',
        2,
        876200000000000067
    ),
    (
        876200000000000100,
        'Course organized to allow all students to participate fully',
        3,
        876200000000000067
    );

INSERT INTO form_template.multiple_choice_grid_column_templates (
    id,
    column_name,
    order_index,
    multiple_choice_grid_template_question_template_id
)
VALUES
    (
        876200000000000101,
        'Strongly disagree',
        0,
        876200000000000067
    ),
    (
        876200000000000102,
        'Disagree',
        1,
        876200000000000067
    ),
    (
        876200000000000103,
        'Neutral',
        2,
        876200000000000067
    ),
    (
        876200000000000104,
        'Agree',
        3,
        876200000000000067
    ),
    (
        876200000000000105,
        'Strongly agree',
        4,
        876200000000000067
    );


-- ============================================================
-- PARAGRAPH QUESTIONS
-- ============================================================

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES
    (
        '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
        876200000000000068
    ),
    (
        '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
        876200000000000069
    );


-- ============================================================
-- MULTIPLE CHOICE QUESTION
-- Why did you choose this course?
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           876200000000000070
       );

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        876200000000000106,
        'Degree requirement',
        0,
        876200000000000070
    ),
    (
        876200000000000107,
        'Time offered',
        1,
        876200000000000070
    ),
    (
        876200000000000108,
        'Interest',
        2,
        876200000000000070
    );


COMMIT;