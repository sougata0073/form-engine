BEGIN;

-- ============================================================
-- PERSONAL CATEGORY ALREADY EXISTS
-- ============================================================
-- 875826790811607954 = Personal


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
           875826790815802791,
           '2026-08-13T20:00:48.344039Z',
           NULL,
           E'Event Address: 123 Your Street Your City, ST 12345\nContact us at (123) 456-7890 or no_reply@example.com',
           'RSVP',
           'Event RSVP',
           875826790811607954
       );


-- ============================================================
-- 2. QUESTION 1
-- Can you attend?
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
           875826790815802792,
           '2026-08-13T20:00:48.344039Z',
           NULL,
           NULL,
           0,
           'Can you attend?',
           'MULTIPLE_CHOICE',
           FALSE,
           875826790815802791
       );


-- ============================================================
-- MULTIPLE CHOICE TEMPLATE FOR QUESTION 1
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802792
       );


-- ============================================================
-- OPTIONS FOR QUESTION 1
-- ============================================================

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802793,
        'Yes,  I''ll be there',
        0,
        875826790815802792
    ),
    (
        875826790815802794,
        'Sorry, can''t make it',
        1,
        875826790815802792
    );


-- ============================================================
-- 3. QUESTION 2
-- What are the names of people attending?
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
           875826790815802795,
           '2026-08-13T20:00:48.344039Z',
           NULL,
           NULL,
           1,
           'What are the names of people attending?',
           'PARAGRAPH',
           FALSE,
           875826790815802791
       );


-- ============================================================
-- PARAGRAPH TEMPLATE FOR QUESTION 2
-- ============================================================

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802795
       );


-- ============================================================
-- 4. QUESTION 3
-- How did you hear about this event?
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
           875826790815802796,
           '2026-08-13T20:00:48.344039Z',
           NULL,
           NULL,
           2,
           'How did you hear about this event?',
           'MULTIPLE_CHOICE',
           FALSE,
           875826790815802791
       );


-- ============================================================
-- MULTIPLE CHOICE TEMPLATE FOR QUESTION 3
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802796
       );


-- ============================================================
-- OPTIONS FOR QUESTION 3
-- ============================================================

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802797,
        'Website',
        0,
        875826790815802796
    ),
    (
        875826790815802798,
        'Friend',
        1,
        875826790815802796
    ),
    (
        875826790815802799,
        'Newsletter',
        2,
        875826790815802796
    ),
    (
        875826790815802800,
        'Advertisement',
        3,
        875826790815802796
    );


-- ============================================================
-- 5. QUESTION 4
-- Comments and/or questions
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
           875826790815802801,
           '2026-08-13T20:00:48.344039Z',
           NULL,
           NULL,
           3,
           'Comments and/or questions',
           'PARAGRAPH',
           FALSE,
           875826790815802791
       );


-- ============================================================
-- PARAGRAPH TEMPLATE FOR QUESTION 4
-- ============================================================

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802801
       );


COMMIT;