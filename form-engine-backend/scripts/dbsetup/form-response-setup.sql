create sequence if not exists public.question_response_sequence
    as bigint
    start with 10000000000
    increment by 1
    minvalue 10000000000
    maxvalue 9223372036854775807;

create
    or replace function public.date_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_date
               timestamptz;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_date
                := (v_response ->> 'date')::timestamptz;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.date_responses (question_response_id, question_id, date, response_count)
            values (v_generated_question_response_id, v_question_id, v_date, p_increment_by)
            on conflict (date, question_id)
                do update
                set response_count = public.date_responses.response_count + excluded.response_count
            returning public.date_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.checkbox_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_option_id
               bigint;
    v_option_ids
               bigint[];
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;

            select array_agg(value::BIGINT)
            into v_option_ids
            from jsonb_array_elements_text(v_response -> 'responseOptionIds');

            foreach v_option_id in array v_option_ids
                loop

                    v_generated_question_response_id := nextval('question_response_sequence');

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_generated_question_response_id, v_question_id);

                    insert into public.checkbox_responses (question_response_id, question_id, option_id, response_count)
                    values (v_generated_question_response_id, v_question_id, v_option_id, p_increment_by)
                    on conflict (option_id, question_id)
                        do update
                        set response_count = public.checkbox_responses.response_count + excluded.response_count
                    returning public.checkbox_responses.question_response_id
                        into v_question_response_id;

                    if
                        v_question_response_id != v_generated_question_response_id then

                        delete
                        from public.question_responses qr
                        where qr.id = v_generated_question_response_id;

                    end if;

                    v_form_response_individual_question_response_ids
                        := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

                end loop;

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;

end;
$$;

create
    or replace function public.date_time_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_date_time
               timestamptz;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_date_time
                := (v_response ->> 'dateTime')::timestamptz;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.date_time_responses (question_response_id, question_id, date_time, response_count)
            values (v_generated_question_response_id, v_question_id, v_date_time, p_increment_by)
            on conflict (date_time, question_id)
                do update
                set response_count = public.date_time_responses.response_count + excluded.response_count
            returning public.date_time_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.dropdown_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_option_id
               bigint;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_option_id
                := (v_response ->> 'responseOptionId')::bigint;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.dropdown_responses (question_response_id, question_id, option_id, response_count)
            values (v_generated_question_response_id, v_question_id, v_option_id, p_increment_by)
            on conflict (option_id, question_id)
                do update
                set response_count = public.dropdown_responses.response_count + excluded.response_count
            returning public.dropdown_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.duration_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_hours
               integer;
    v_minutes
               integer;
    v_seconds
               integer;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_hours
                := (v_response ->> 'hours')::integer;
            v_minutes
                := (v_response ->> 'minutes')::integer;
            v_seconds
                := (v_response ->> 'seconds')::integer;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.duration_responses (question_response_id, question_id, hours, minutes, seconds,
                                                   response_count)
            values (v_generated_question_response_id, v_question_id, v_hours, v_minutes, v_seconds, p_increment_by)
            on conflict (hours, minutes, seconds, question_id)
                do update
                set response_count = public.duration_responses.response_count + excluded.response_count
            returning public.duration_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.file_upload_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_file_url
               text;
    v_file_name
               text;
    v_file_mime_type
               text;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_file_url
                := (v_response ->> 'fileUrl')::text;
            v_file_name
                := (v_response ->> 'fileName')::text;
            v_file_mime_type
                := (v_response ->> 'fileMimeType')::text;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.file_upload_responses (question_response_id, question_id, file_url, file_name,
                                                      file_mime_type,
                                                      response_count)
            values (v_generated_question_response_id, v_question_id, v_file_url, v_file_name, v_file_mime_type,
                    p_increment_by)
            on conflict (file_url, file_name, file_mime_type, question_id)
                do update
                set response_count = public.file_upload_responses.response_count + excluded.response_count
            returning public.file_upload_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.linear_scale_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_scale
               integer;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_scale
                := (v_response ->> 'scale')::integer;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.linear_scale_responses (question_response_id, question_id, scale, response_count)
            values (v_generated_question_response_id, v_question_id, v_scale, p_increment_by)
            on conflict (scale, question_id)
                do update
                set response_count = public.linear_scale_responses.response_count + excluded.response_count
            returning public.linear_scale_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.multiple_choice_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_option_id
               bigint;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_option_id
                := (v_response ->> 'responseOptionId')::bigint;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.multiple_choice_responses (question_response_id, question_id, option_id, response_count)
            values (v_generated_question_response_id, v_question_id, v_option_id, p_increment_by)
            on conflict (option_id, question_id)
                do update
                set response_count = public.multiple_choice_responses.response_count + excluded.response_count
            returning public.multiple_choice_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.paragraph_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_text
               text;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_text
                := (v_response ->> 'text')::text;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.paragraph_responses (question_response_id, question_id, text, response_count)
            values (v_generated_question_response_id, v_question_id, v_text, p_increment_by)
            on conflict (text, question_id)
                do update
                set response_count = public.paragraph_responses.response_count + excluded.response_count
            returning public.paragraph_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.rating_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_rating
               integer;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_rating
                := (v_response ->> 'rating')::integer;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.rating_responses (question_response_id, question_id, rating, response_count)
            values (v_generated_question_response_id, v_question_id, v_rating, p_increment_by)
            on conflict (rating, question_id)
                do update
                set response_count = public.rating_responses.response_count + excluded.response_count
            returning public.rating_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.short_answer_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_text
               text;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_text
                := (v_response ->> 'text')::text;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.short_answer_responses (question_response_id, question_id, text, response_count)
            values (v_generated_question_response_id, v_question_id, v_text, p_increment_by)
            on conflict (text, question_id)
                do update
                set response_count = public.short_answer_responses.response_count + excluded.response_count
            returning public.short_answer_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.time_responses_increment_or_create(
    p_data jsonb,
    p_form_response_id uuid,
    p_increment_by bigint
) returns void
    language plpgsql as
$$
declare
    v_response jsonb;
    v_question_id
               bigint;
    v_time
               timestamptz;
    v_question_response_id
               bigint;
    v_generated_question_response_id
               bigint;
    v_form_response_individual_question_response_ids
               bigint[] := Array []::bigint[];
begin
    for v_response in
        select jsonb_array_elements(p_data -> 'responses')
        loop
            v_question_id := (v_response ->> 'questionId')::bigint;
            v_time
                := (v_response ->> 'time')::timestamptz;
            v_generated_question_response_id
                := nextval('question_response_sequence');

            insert into public.question_responses (id, question_response_summary_question_id)
            values (v_generated_question_response_id, v_question_id);

            insert into public.time_responses (question_response_id, question_id, time, response_count)
            values (v_generated_question_response_id, v_question_id, v_time, p_increment_by)
            on conflict (time, question_id)
                do update
                set response_count = public.time_responses.response_count + excluded.response_count
            returning public.time_responses.question_response_id
                into v_question_response_id;

            if
                v_question_response_id != v_generated_question_response_id then

                delete
                from public.question_responses qr
                where qr.id = v_generated_question_response_id;

            end if;

            v_form_response_individual_question_response_ids
                := array_append(v_form_response_individual_question_response_ids, v_question_response_id);

        end loop;

    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
    select question_response_id, p_form_response_id
    from unnest(v_form_response_individual_question_response_ids) as question_response_id;
end;
$$;

create
    or replace function public.save_form_response_summary(
    p_form_id uuid,
    p_form_response_id uuid,
    p_increment_form_response_count_by bigint,
    p_increment_question_response_count_by bigint,
    p_response_question_ids jsonb
) returns void
    language plpgsql as
$$
begin
    insert into public.form_response_summaries (form_id, response_count)
    values (p_form_id, p_increment_form_response_count_by)
    on conflict (form_id)
        do update
        set response_count = public.form_response_summaries.response_count + excluded.response_count;

    insert into public.form_response_individuals (form_response_id)
    values (p_form_response_id);

    insert into public.question_response_summaries (question_id, response_count, form_response_summary_form_id)
    select value::bigint, p_increment_question_response_count_by, p_form_id
    from jsonb_array_elements_text(p_response_question_ids -> 'questionIds')
    on conflict (question_id) do update
        set response_count = public.question_response_summaries.response_count + excluded.response_count;
end;
$$;