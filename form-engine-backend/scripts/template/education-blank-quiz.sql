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
           876200000000000012,
           NOW(),
           NULL,
           '',
           'Blank Quiz',
           'Blank Quiz',
           875826790815802790
       );


-- ============================================================
-- QUESTION
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
           876200000000000013,
           NOW(),
           NULL,
           NULL,
           0,
           'Untitled Question',
           'MULTIPLE_CHOICE',
           FALSE,
           876200000000000012
       );


-- ============================================================
-- MULTIPLE CHOICE QUESTION
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           876200000000000013
       );


-- ============================================================
-- MULTIPLE CHOICE OPTION
-- ============================================================

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES (
           876200000000000014,
           'Option 1',
           0,
           876200000000000013
       );

COMMIT;