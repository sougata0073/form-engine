create index if not exists idx_email_auth_provider
    on users(email, auth_provider);