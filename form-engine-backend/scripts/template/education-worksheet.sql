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
           876200000000000050,
           NOW(),
           NULL,
           'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
           'Worksheet',
           'Worksheet',
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
        876200000000000051,
        NOW(),
        NULL,
        NULL,
        0,
        'Name',
        'SHORT_ANSWER',
        TRUE,
        876200000000000050
    ),
    (
        876200000000000052,
        NOW(),
        NULL,
        NULL,
        1,
        'Email',
        'SHORT_ANSWER',
        TRUE,
        876200000000000050
    ),
    (
        876200000000000053,
        NOW(),
        NULL,
        NULL,
        2,
        'Question about this topic',
        'MULTIPLE_CHOICE',
        FALSE,
        876200000000000050
    ),
    (
        876200000000000054,
        NOW(),
        NULL,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque non risus ipsum. Nullam interdum semper erat, viverra tristique enim efficitur a. Praesent pretium diam enim. Sed orci magna, fermentum in aliquam tristique, dictum ac metus. Maecenas quis eros enim. Mauris ultrices orci mi, vitae tincidunt lorem efficitur a. Aenean pharetra, neque vel facilisis feugiat, eros nunc interdum lorem, vel finibus justo sapien eget ipsum. Aenean in dictum urna. Nullam pulvinar ex nec faucibus feugiat. Proin finibus nisi tristique, suscipit mi ut, maximus turpis. Pellentesque eu pharetra neque, vitae ullamcorper purus. Nullam mattis tellus magna, vitae suscipit dolor vulputate ac. Aenean imperdiet sapien lectus, id viverra neque fringilla nec. Praesent volutpat urna at nunc ullamcorper, id maximus felis suscipit. Mauris tincidunt, ipsum non aliquam malesuada, urna nisi varius dolor, sed imperdiet enim neque ut nulla.',
        3,
        'Question about below topic',
        'CHECKBOX',
        FALSE,
        876200000000000050
    );


-- ============================================================
-- MULTIPLE CHOICE
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           876200000000000053
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
        876200000000000055,
        'Option 1',
        0,
        876200000000000053
    ),
    (
        876200000000000056,
        'Option 2',
        1,
        876200000000000053
    ),
    (
        876200000000000057,
        'Option 3',
        2,
        876200000000000053
    );


-- ============================================================
-- CHECKBOX
-- ============================================================

INSERT INTO form_template.checkbox_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "CHECKBOX_NONE"}'::jsonb,
           876200000000000054
       );


-- ============================================================
-- CHECKBOX OPTIONS
-- ============================================================

INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES
    (
        876200000000000058,
        'Option 1',
        0,
        876200000000000054
    ),
    (
        876200000000000059,
        'Option 2',
        1,
        876200000000000054
    ),
    (
        876200000000000060,
        'Option 3',
        2,
        876200000000000054
    );


-- ============================================================
-- SHORT ANSWER
-- ============================================================

INSERT INTO form_template.short_answer_templates (
    validation_config,
    question_template_id
)
VALUES
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000051
    ),
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000052
    );


COMMIT;