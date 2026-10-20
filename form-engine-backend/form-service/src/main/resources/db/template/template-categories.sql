INSERT INTO form_template.template_categories (id, name)
VALUES (875826790811607954, 'Personal'),
       (875826790815802789, 'Work'),
       (875826790815802790, 'Education') ON CONFLICT (id) DO NOTHING;