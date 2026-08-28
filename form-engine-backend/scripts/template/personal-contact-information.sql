BEGIN;

-- ============================================================
-- PERSONAL CATEGORY
-- 875826790811607954 = Personal
-- ============================================================


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
           875826790815802822,
           '2026-08-13T20:25:24.530946Z',
           NULL,
           '',
           'Contact Information',
           'Contact information',
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
           875826790815802823,
           '2026-08-13T20:25:24.530946Z',
           NULL,
           NULL,
           0,
           'Name',
           'SHORT_ANSWER',
           TRUE,
           875826790815802822
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802823
       );


-- ============================================================
-- 3. QUESTION 2
-- Email
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
           875826790815802824,
           '2026-08-13T20:25:24.530946Z',
           NULL,
           NULL,
           1,
           'Email',
           'SHORT_ANSWER',
           TRUE,
           875826790815802822
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802824
       );


-- ============================================================
-- 4. QUESTION 3
-- Address
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
           875826790815802825,
           '2026-08-13T20:25:24.530946Z',
           NULL,
           NULL,
           2,
           'Address',
           'PARAGRAPH',
           TRUE,
           875826790815802822
       );


INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802825
       );


-- ============================================================
-- 5. QUESTION 4
-- Phone number
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
           875826790815802826,
           '2026-08-13T20:25:24.530946Z',
           NULL,
           NULL,
           3,
           'Phone number',
           'SHORT_ANSWER',
           FALSE,
           875826790815802822
       );


INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802826
       );


-- ============================================================
-- 6. QUESTION 5
-- Comment
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
           875826790815802827,
           '2026-08-13T20:25:24.530946Z',
           NULL,
           NULL,
           4,
           'Comment',
           'PARAGRAPH',
           FALSE,
           875826790815802822
       );


INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802827
       );


COMMIT;