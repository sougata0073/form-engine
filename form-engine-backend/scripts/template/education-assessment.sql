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
           876200000000000032,
           NOW(),
           NULL,
           'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur quis sem odio. Sed commodo vestibulum leo, sit amet tempus odio consectetur in. Mauris dolor elit, dignissim mollis feugiat maximus, faucibus et eros. Pellentesque venenatis odio nec nunc hendrerit commodo.',
           'Assessment',
           'Assessment',
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
        876200000000000033,
        NOW(),
        NULL,
        NULL,
        0,
        'Name',
        'SHORT_ANSWER',
        TRUE,
        876200000000000032
    ),
    (
        876200000000000034,
        NOW(),
        NULL,
        NULL,
        1,
        'Email',
        'SHORT_ANSWER',
        FALSE,
        876200000000000032
    ),
    (
        876200000000000035,
        NOW(),
        NULL,
        NULL,
        2,
        'Your first question?',
        'MULTIPLE_CHOICE',
        TRUE,
        876200000000000032
    ),
    (
        876200000000000036,
        NOW(),
        NULL,
        NULL,
        3,
        'Your second question?',
        'CHECKBOX',
        TRUE,
        876200000000000032
    ),
    (
        876200000000000037,
        NOW(),
        NULL,
        NULL,
        4,
        'Your third question?',
        'SHORT_ANSWER',
        TRUE,
        876200000000000032
    ),
    (
        876200000000000038,
        NOW(),
        NULL,
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque non risus ipsum. Nullam interdum semper erat, viverra tristique enim efficitur a. Praesent pretium diam enim. Sed orci magna, fermentum in aliquam tristique, dictum ac metus. Maecenas quis eros enim. Mauris ultrices orci mi, vitae tincidunt lorem efficitur a. Aenean pharetra, neque vel facilisis feugiat, eros nunc interdum lorem, vel finibus justo sapien eget ipsum. Aenean in dictum urna. Nullam pulvinar ex nec faucibus feugiat. Proin finibus nisi tristique, suscipit mi ut, maximus turpis. Pellentesque eu pharetra neque, vitae ullamcorper purus. Nullam mattis tellus magna, vitae suscipit dolor vulputate ac. Aenean imperdiet sapien lectus, id viverra neque fringilla nec. Praesent volutpat urna at nunc ullamcorper, id maximus felis suscipit. Mauris tincidunt, ipsum non aliquam malesuada, urna nisi varius dolor, sed imperdiet enim neque ut nulla.',
        5,
        'Based on the text below, your fourth question.',
        'PARAGRAPH',
        FALSE,
        876200000000000032
    );


-- ============================================================
-- MULTIPLE CHOICE
-- ============================================================

INSERT INTO form_template.multiple_choice_templates (
    question_template_id
)
VALUES (
           876200000000000035
       );


INSERT INTO form_template.multiple_choice_option_templates (
    id,
    option,
    order_index,
    multiple_choice_template_question_template_id
)
VALUES
    (
        876200000000000039,
        'Option 1',
        0,
        876200000000000035
    ),
    (
        876200000000000040,
        'Correct answer',
        1,
        876200000000000035
    ),
    (
        876200000000000041,
        'Option 3',
        2,
        876200000000000035
    ),
    (
        876200000000000042,
        'Option 4',
        3,
        876200000000000035
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
           876200000000000036
       );


INSERT INTO form_template.checkbox_option_templates (
    id,
    option,
    order_index,
    checkbox_template_question_template_id
)
VALUES
    (
        876200000000000043,
        'Option 1',
        0,
        876200000000000036
    ),
    (
        876200000000000044,
        'Correct answer 1',
        1,
        876200000000000036
    ),
    (
        876200000000000045,
        'Option 3',
        2,
        876200000000000036
    ),
    (
        876200000000000046,
        'Correct answer 2',
        3,
        876200000000000036
    ),
    (
        876200000000000047,
        'Correct answer 3',
        4,
        876200000000000036
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
        876200000000000033
    ),
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000034
    ),
    (
        '{"errorText": null, "validationId": "SHORT_ANSWER_NONE"}'::jsonb,
        876200000000000037
    );


-- ============================================================
-- PARAGRAPH
-- ============================================================

INSERT INTO form_template.paragraph_templates (
    validation_config,
    question_template_id
)
VALUES (
           '{"errorText": null, "validationId": "PARAGRAPH_NONE"}'::jsonb,
           876200000000000038
       );

COMMIT;