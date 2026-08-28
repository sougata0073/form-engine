BEGIN;

-- ============================================================
-- PERSONAL CATEGORY
-- Existing category:
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
           875826790815802802,
           '2026-08-13T20:20:56.823002Z',
           NULL,
           E'We need to get together to talk about some things - when do you have time to meet?\n\nLet''s meet at 123 Your Street Your City, ST 12345',
           'Find a Time',
           'Find a Time',
           875826790811607954
       );


-- ============================================================
-- 2. QUESTION 1
-- What times are you available?
-- TICK_BOX_GRID
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
           875826790815802803,
           '2026-08-13T20:20:56.823002Z',
           NULL,
           NULL,
           0,
           'What times are you available?',
           'TICK_BOX_GRID',
           FALSE,
           875826790815802802
       );


-- ============================================================
-- TICK BOX GRID TEMPLATE
-- ============================================================

INSERT INTO form_template.tick_box_grid_templates (
    each_row_required,
    question_template_id
)
VALUES (
           FALSE,
           875826790815802803
       );


-- ============================================================
-- ROWS
-- ============================================================

INSERT INTO form_template.tick_box_grid_row_templates (
    id,
    row_name,
    order_index,
    tick_box_grid_template_question_template_id
)
VALUES
    (
        875826790815802804,
        'Monday',
        0,
        875826790815802803
    ),
    (
        875826790815802805,
        'Tuesday',
        1,
        875826790815802803
    ),
    (
        875826790815802806,
        'Wednesday',
        2,
        875826790815802803
    ),
    (
        875826790815802807,
        'Thursday',
        3,
        875826790815802803
    ),
    (
        875826790815802808,
        'Friday',
        4,
        875826790815802803
    );


-- ============================================================
-- COLUMNS
-- ============================================================

INSERT INTO form_template.tick_box_grid_column_templates (
    id,
    column_name,
    order_index,
    tick_box_grid_template_question_template_id
)
VALUES
    (
        875826790815802809,
        'Morning',
        0,
        875826790815802803
    ),
    (
        875826790815802810,
        'Midday',
        1,
        875826790815802803
    ),
    (
        875826790815802811,
        'Afternoon',
        2,
        875826790815802803
    ),
    (
        875826790815802812,
        'Evening',
        3,
        875826790815802803
    );


-- ============================================================
-- 3. QUESTION 2
-- Items to discuss?
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
           875826790815802813,
           '2026-08-13T20:20:56.823002Z',
           NULL,
           NULL,
           1,
           'Items to discuss?',
           'PARAGRAPH',
           FALSE,
           875826790815802802
       );


-- Paragraph template

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802813
       );


-- ============================================================
-- 4. QUESTION 3
-- Allergies or dietary restrictions?
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
           875826790815802814,
           '2026-08-13T20:20:56.823002Z',
           NULL,
           NULL,
           2,
           'Allergies or dietary restrictions?',
           'MULTIPLE_CHOICE',
           FALSE,
           875826790815802802
       );


-- Multiple choice template

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802814
       );


-- ============================================================
-- OPTIONS
-- ============================================================

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802815,
        'Vegeterian',
        0,
        875826790815802814
    ),
    (
        875826790815802816,
        'Vegan',
        1,
        875826790815802814
    ),
    (
        875826790815802817,
        'Kosher',
        2,
        875826790815802814
    ),
    (
        875826790815802818,
        'Halal',
        3,
        875826790815802814
    ),
    (
        875826790815802819,
        'Gluten-free',
        4,
        875826790815802814
    ),
    (
        875826790815802820,
        'None',
        5,
        875826790815802814
    );


-- ============================================================
-- 5. QUESTION 4
-- Any other comments and/or questions?
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
           875826790815802821,
           '2026-08-13T20:20:56.823002Z',
           NULL,
           NULL,
           3,
           'Any other comments and/or questions?',
           'PARAGRAPH',
           FALSE,
           875826790815802802
       );


-- Paragraph template

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802821
       );


COMMIT;