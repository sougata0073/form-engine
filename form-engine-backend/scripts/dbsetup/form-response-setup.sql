create sequence if not exists public.question_response_sequence
    as bigint
    start with 10000000000
    increment by 1
    minvalue 10000000000
    maxvalue 9223372036854775807;

create or replace function public.date_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.date_responses (question_response_id, question_id, date, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'date')::timestamptz,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (date, question_id)
                        do update
                        set response_count = public.date_responses.response_count + excluded.response_count
                    returning public.date_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.checkbox_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.checkbox_responses (question_response_id, question_id, option_id, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'optionId')::bigint,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (option_id, question_id)
                        do update
                        set response_count = public.checkbox_responses.response_count + excluded.response_count
                    returning public.checkbox_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.date_time_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.date_time_responses (question_response_id, question_id, date_time, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'dateTime')::timestamptz,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (date_time, question_id)
                        do update
                        set response_count = public.date_time_responses.response_count + excluded.response_count
                    returning public.date_time_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.dropdown_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.dropdown_responses (question_response_id, question_id, option_id, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'optionId')::bigint,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (option_id, question_id)
                        do update
                        set response_count = public.dropdown_responses.response_count + excluded.response_count
                    returning public.dropdown_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.duration_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.duration_responses (question_response_id, question_id, hours, minutes, seconds,
                                                           response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'hours')::integer,
                            (v_response ->> 'minutes')::integer, (v_response ->> 'seconds')::integer,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (hours, minutes, seconds, question_id)
                        do update
                        set response_count = public.duration_responses.response_count + excluded.response_count
                    returning public.duration_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.file_upload_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.file_upload_responses (question_response_id, question_id, file_url, file_name,
                                                              file_mime_type, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'fileUrl')::text,
                            (v_response ->> 'fileName')::text, (v_response ->> 'fileMimeType')::text,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (file_url, file_name, file_mime_type, question_id)
                        do update
                        set response_count = public.file_upload_responses.response_count + excluded.response_count
                    returning public.file_upload_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.linear_scale_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.linear_scale_responses (question_response_id, question_id, scale, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'scale')::integer,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (scale, question_id)
                        do update
                        set response_count = public.linear_scale_responses.response_count + excluded.response_count
                    returning public.linear_scale_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.multiple_choice_grid_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.multiple_choice_grid_responses (question_response_id, question_id, row_id, column_id, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'rowId')::bigint,
                            (v_response ->> 'columnId')::bigint, (v_response ->> 'responseCount')::bigint)
                    on conflict (row_id, column_id, question_id)
                        do update
                        set response_count = public.multiple_choice_grid_responses.response_count +
                                             excluded.response_count
                    returning public.multiple_choice_grid_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.multiple_choice_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.multiple_choice_responses (question_response_id, question_id, option_id, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'optionId')::bigint,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (option_id, question_id)
                        do update
                        set response_count = public.multiple_choice_responses.response_count + excluded.response_count
                    returning public.multiple_choice_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.paragraph_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.paragraph_responses (question_response_id, question_id, text, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'text')::text,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (text, question_id)
                        do update
                        set response_count = public.paragraph_responses.response_count + excluded.response_count
                    returning public.paragraph_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.rating_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.rating_responses (question_response_id, question_id, rating, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'rating')::integer,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (rating, question_id)
                        do update
                        set response_count = public.rating_responses.response_count + excluded.response_count
                    returning public.rating_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.short_answer_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.short_answer_responses (question_response_id, question_id, text, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'text')::text,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (text, question_id)
                        do update
                        set response_count = public.short_answer_responses.response_count + excluded.response_count
                    returning public.short_answer_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.tick_box_grid_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.tick_box_grid_responses (question_response_id, question_id, row_id, column_id, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'rowId')::bigint,
                            (v_response ->> 'columnId')::bigint, (v_response ->> 'responseCount')::bigint)
                    on conflict (row_id, column_id, question_id)
                        do update
                        set response_count = public.tick_box_grid_responses.response_count + excluded.response_count
                    returning public.tick_box_grid_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.time_responses_increment_or_create(p_batch_responses jsonb) returns void
    language plpgsql as
$$
declare
    v_batch                        jsonb;
    v_response                     jsonb;
    v_question_id                  bigint;
    v_question_response_id         bigint;
    v_updated_question_response_id bigint;
begin

    for v_batch in select jsonb_array_elements(p_batch_responses -> 'batches')
        loop

            v_question_id := (v_batch ->> 'questionId')::bigint;

            for v_response in select jsonb_array_elements(v_batch -> 'responses')
                loop

                    v_question_response_id := (v_response ->> 'questionResponseId')::bigint;

                    insert into public.question_responses (id, question_response_summary_question_id)
                    values (v_question_response_id, v_question_id);

                    insert into public.time_responses (question_response_id, question_id, time, response_count)
                    values (v_question_response_id, v_question_id, (v_response ->> 'time')::timestamptz,
                            (v_response ->> 'responseCount')::bigint)
                    on conflict (time, question_id)
                        do update
                        set response_count = public.time_responses.response_count + excluded.response_count
                    returning public.time_responses.question_response_id
                        into v_updated_question_response_id;

                    if v_updated_question_response_id != v_question_response_id then
                        delete from public.question_responses qr where qr.id = v_question_response_id;
                    end if;

                    insert into public.question_response_form_response_individual (question_response_id, form_response_individual_id)
                    select v_updated_question_response_id, form_response_id::uuid
                    from jsonb_array_elements_text(v_response -> 'formResponseIds') as form_response_id;

                end loop;

        end loop;

end;
$$;

create or replace function public.save_form_response_summaries (
    p_form_response_counts jsonb,
    p_question_response_counts jsonb,
    p_form_response_infos jsonb
) returns void language plpgsql as $$
begin
    insert into public.form_response_summaries (form_id, response_count)
    select (value ->> 'formId')::uuid, (value ->> 'responseCount')::bigint
    from jsonb_array_elements(p_form_response_counts -> 'counts')
    on conflict (form_id)
        do update
        set response_count = public.form_response_summaries.response_count + excluded.response_count;

    insert into public.question_response_summaries (question_id, response_count, form_response_summary_form_id)
    select (value ->> 'questionId')::bigint, (value ->> 'responseCount')::bigint, (value ->> 'formId')::uuid
    from jsonb_array_elements(p_question_response_counts -> 'counts')
    on conflict (question_id) do update
        set response_count = public.question_response_summaries.response_count + excluded.response_count;

    insert into public.form_response_individuals (form_response_id, form_response_summary_form_id, user_id)
    select (value ->> 'formResponseId')::uuid, (value ->> 'formId')::uuid, (value ->> 'userId')::uuid
    from jsonb_array_elements(p_form_response_infos -> 'formResponseInfos');

end;
$$;