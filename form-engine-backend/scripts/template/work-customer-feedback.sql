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
           876200000000000001,
           NOW(),
           NULL,
           'We would love to hear your thoughts or feedback on how we can improve your experience!',
           'Customer Feedback',
           'Customer Feedback',
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
        876200000000000002,
        NOW(),
        NULL,
        NULL,
        0,
        'Feedback Type',
        'MULTIPLE_CHOICE',
        FALSE,
        876200000000000001
    ),
    (
        876200000000000007,
        NOW(),
        NULL,
        NULL,
        1,
        'Feedback',
        'PARAGRAPH',
        TRUE,
        876200000000000001
    ),
    (
        876200000000000008,
        NOW(),
        NULL,
        NULL,
        2,
        'Suggestions for improvement',
        'PARAGRAPH',
        FALSE,
        876200000000000001
    ),
    (
        876200000000000009,
        NOW(),
        NULL,
        NULL,
        3,
        'Name',
        'SHORT_ANSWER',
        FALSE,
        876200000000000001
    ),
    (
        876200000000000010,
        NOW(),
        NULL,
        NULL,
        4,
        'Email',
        'SHORT_ANSWER',
        FALSE,
        876200000000000001
    );


-- ============================================================
-- MULTIPLE CHOICE
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           876200000000000002
       );


-- ============================================================
-- MULTIPLE CHOICE OPTIONS
-- ============================================================

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        876200000000000003,
        'Comments',
        0,
        876200000000000002
    ),
    (
        876200000000000004,
        'Questions',
        1,
        876200000000000002
    ),
    (
        876200000000000005,
        'Bug Reports',
        2,
        876200000000000002
    ),
    (
        876200000000000006,
        'Feature Request',
        3,
        876200000000000002
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
        876200000000000007
    ),
    (
        '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
        876200000000000008
    );


-- ============================================================
-- SHORT ANSWER QUESTIONS
-- ============================================================

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000009
    ),
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_TEXT_EMAIL"}'::jsonb,
        876200000000000010
    );

COMMIT;