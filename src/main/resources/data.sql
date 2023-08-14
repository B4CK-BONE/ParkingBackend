INSERT INTO cat.room (admin_idx)
VALUES (1);

INSERT INTO cat.parking_lot (bottom, direction, left_side, right_side, room_idx, slot, top)
VALUES ('a', 'a', 'a', 'a', 1, 1, 'a');
INSERT INTO cat.parking_lot (bottom, direction, left_side, right_side, room_idx, slot, top)
VALUES ('a', 'a', 'a', 'a', 1, 2, 'a');
INSERT INTO cat.parking_lot (bottom, direction, left_side, right_side, room_idx, slot, top)
VALUES ('a', 'a', 'a', 'a', 1, 3, 'a');
DROP TABLE  if exists User;
create table User(idx int NOT NULL primary key auto_increment, room_idx int null, email varchar(255) not null, pw text not null ,role int null default 0, refresh_token text null);