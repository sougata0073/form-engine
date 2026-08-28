BEGIN;

-- ============================================================
-- TEMPLATE
-- Category: Work
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
           875826790815802920,
           '2026-08-14T18:36:23.278309Z',
           NULL,
           'Please submit the times you need to take off work and the type of leave you are taking.',
           'Time Off Request',
           'Time off request',
           (
               SELECT id
               FROM form_template.template_categories
               WHERE name = 'Work'
           )
       );


-- ============================================================
-- QUESTION 1
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
           875826790815802921,
           '2026-08-14T18:36:23.278309Z',
           NULL,
           NULL,
           0,
           'Name',
           'SHORT_ANSWER',
           TRUE,
           875826790815802920
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802921
       );


-- ============================================================
-- QUESTION 2
-- Leave date(s)
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
           875826790815802922,
           '2026-08-14T18:36:23.278309Z',
           NULL,
           NULL,
           1,
           'Leave date(s)',
           'SHORT_ANSWER',
           TRUE,
           875826790815802920
       );

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
           875826790815802922
       );


-- ============================================================
-- QUESTION 3
-- AM/PM/All day
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
           875826790815802923,
           '2026-08-14T18:36:23.278309Z',
           NULL,
           NULL,
           2,
           'AM/PM/All day',
           'MULTIPLE_CHOICE',
           TRUE,
           875826790815802920
       );

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802923
       );

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802924,
        'AM',
        0,
        875826790815802923
    ),
    (
        875826790815802925,
        'PM',
        1,
        875826790815802923
    ),
    (
        875826790815802926,
        'Full day',
        2,
        875826790815802923
    );


-- ============================================================
-- QUESTION 4
-- Type of leave
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
           875826790815802927,
           '2026-08-14T18:36:23.278309Z',
           NULL,
           'Description if needed. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum.',
           3,
           'Type of leave',
           'MULTIPLE_CHOICE',
           TRUE,
           875826790815802920
       );

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           875826790815802927
       );

INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        875826790815802928,
        'Sick leave (Illness or Injury)',
        0,
        875826790815802927
    ),
    (
        875826790815802929,
        'Bereavement leave (Immediate Family)',
        1,
        875826790815802927
    ),
    (
        875826790815802930,
        'Bereavement leave (Other)',
        2,
        875826790815802927
    ),
    (
        875826790815802931,
        'Personal leave',
        3,
        875826790815802927
    ),
    (
        875826790815802932,
        'Jury duty or legal leave',
        4,
        875826790815802927
    ),
    (
        875826790815802933,
        'Emergency leave',
        5,
        875826790815802927
    ),
    (
        875826790815802934,
        'Temporary leave',
        6,
        875826790815802927
    ),
    (
        875826790815802935,
        'Leave without pay',
        7,
        875826790815802927
    );


-- ============================================================
-- QUESTION 5
-- Reason for leave
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
           875826790815802936,
           '2026-08-14T18:36:23.278309Z',
           NULL,
           NULL,
           4,
           'Reason for leave',
           'PARAGRAPH',
           FALSE,
           875826790815802920
       );

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           875826790815802936
       );


COMMIT;