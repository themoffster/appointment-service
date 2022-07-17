create table appointment
(
    id         varchar(36) primary key,
    patient_id varchar(100) not null,
    datetime   timestamptz  not null,
    notes      varchar
);
