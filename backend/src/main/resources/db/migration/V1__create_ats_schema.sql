create table users (
    id bigserial primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(255) not null,
    role varchar(32) not null,
    phone varchar(64),
    location varchar(255),
    bio text,
    created_at timestamptz not null
);

create table jobs (
    id bigserial primary key,
    title varchar(255) not null,
    company varchar(255) not null,
    location varchar(255) not null,
    department varchar(255),
    employment_type varchar(64) not null,
    status varchar(32) not null,
    description text not null,
    requirements text,
    min_salary numeric(12,2),
    max_salary numeric(12,2),
    recruiter_id bigint not null references users(id),
    created_at timestamptz not null
);

create table job_stages (
    id bigserial primary key,
    job_id bigint not null references jobs(id) on delete cascade,
    name varchar(120) not null,
    position integer not null,
    unique(job_id, position)
);

create table applications (
    id bigserial primary key,
    job_id bigint not null references jobs(id),
    candidate_id bigint not null references users(id),
    stage_id bigint references job_stages(id),
    status varchar(32) not null,
    resume_path varchar(500),
    cover_letter text,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    unique(job_id, candidate_id)
);

create table application_notes (
    id bigserial primary key,
    application_id bigint not null references applications(id) on delete cascade,
    author_id bigint references users(id),
    body text not null,
    created_at timestamptz not null
);

create index idx_jobs_status_created on jobs(status, created_at desc);
create index idx_applications_candidate on applications(candidate_id, updated_at desc);
create index idx_applications_job_stage on applications(job_id, stage_id);

