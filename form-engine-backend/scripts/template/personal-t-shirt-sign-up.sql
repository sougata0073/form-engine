BEGIN;

-- ============================================================
-- 1. TEMPLATE
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
           875826790815802842,
           '2026-08-14T18:05:33.039728Z',
           NULL,
           'Enter your name and size to sign up for a T-Shirt.',
           'T-Shirt Sign Up',
           'T-Shirt Sign Up',
           875826790811607954
       );


-- ============================================================
-- 2. QUESTION 1
-- Name
-- SHORT_ANSWER
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
VALUES (
           875826790815802843,
           '2026-08-14T18:05:33.039728Z',
           NULL,
           NULL,
           0,
           'Name',
           'SHORT_ANSWER',
           TRUE,
           875826790815802842
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802843
       );


-- ============================================================
-- 3. QUESTION 2
-- Shirt size
-- MULTIPLE_CHOICE
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
VALUES (
           875826790815802844,
           '2026-08-14T18:05:33.039728Z',
           NULL,
           NULL,
           1,
           'Shirt size',
           'MULTIPLE_CHOICE',
           FALSE,
           875826790815802842
       );

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802844
       );


-- ============================================================
-- SHIRT SIZE OPTIONS
-- ============================================================

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802845,
        'XS',
        0,
        875826790815802844
    ),
    (
        875826790815802846,
        'S',
        1,
        875826790815802844
    ),
    (
        875826790815802847,
        'M',
        2,
        875826790815802844
    ),
    (
        875826790815802848,
        'L',
        3,
        875826790815802844
    ),
    (
        875826790815802849,
        'XL',
        4,
        875826790815802844
    );


-- ============================================================
-- 4. QUESTION 3
-- Other thoughts or comments
-- PARAGRAPH
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
VALUES (
           875826790815802850,
           '2026-08-14T18:05:33.039728Z',
           NULL,
           NULL,
           2,
           'Other thoughts or comments',
           'PARAGRAPH',
           FALSE,
           875826790815802842
       );

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802850
       );


COMMIT;