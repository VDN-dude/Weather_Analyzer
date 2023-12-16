create table if not exists weather (
    id serial primary key,
    status varchar(8) not null,
    temp_c double precision not null,
    wind_mph double precision not null,
    pressure_mb double precision not null,
    humidity double precision not null,
    condition varchar(50) not null,
    location varchar(50) not null,
    date timestamp not null,
    updated_at timestamp not null
);