insert into users (id, balance, name, surname)
values (1, 30500.00, 'Yuriy', 'Osmolovskiy');

insert into users (id, balance, name, surname)
values (2, 8500.00, 'Ivan', 'Ivanov');

insert into users (id, balance, name, surname)
values (3, 600.00, 'Petr', 'Petrov');

insert into operations (id, date_of_operation, operation_type, sum, user_id)
values (100, '2022-11-20', 'PUT_MONEY', 300.0, 1);