BEGIN;

-- ============================================================
-- TEMPLATE
-- Category: Education
-- Category ID: 875826790815802790
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
           876200000000000026,
           NOW(),
           NULL,
           'Before you leave class today, answer the following questions.',
           'Exit Ticket',
           'Exit Ticket',
           875826790815802790
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
        876200000000000027,
        NOW(),
        NULL,
        NULL,
        0,
        'Name',
        'SHORT_ANSWER',
        TRUE,
        876200000000000026
    ),
    (
        876200000000000028,
        NOW(),
        NULL,
        NULL,
        1,
        'Email',
        'SHORT_ANSWER',
        FALSE,
        876200000000000026
    ),
    (
        876200000000000029,
        NOW(),
        NULL,
        NULL,
        2,
        'What''s one important thing you learned in class today?',
        'PARAGRAPH',
        FALSE,
        876200000000000026
    ),
    (
        876200000000000030,
        NOW(),
        NULL,
        NULL,
        3,
        'Did you feel prepared for today''s lesson? Why or why not?',
        'PARAGRAPH',
        FALSE,
        876200000000000026
    ),
    (
        876200000000000031,
        NOW(),
        NULL,
        NULL,
        4,
        'What would help make today''s lesson more effective?',
        'PARAGRAPH',
        FALSE,
        876200000000000026
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
        876200000000000027
    ),
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000028
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
        876200000000000029
    ),
    (
        '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
        876200000000000030
    ),
    (
        '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
        876200000000000031
    );

COMMIT;