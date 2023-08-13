INSERT INTO cat.users (email,  role, room_idx)
VALUES ('slknalsdknalkn@naver.com',  2, 1);
INSERT INTO cat.users (email,  role, room_idx)
VALUES ('qezxc@naver.com',  1, 1);
INSERT INTO cat.users (email, role, room_idx)
VALUES ('aqerdfc@naver.com',  DEFAULT, 1);
INSERT INTO cat.users (email, role, room_idx)
VALUES ('aqe123rdfc@naver.com', DEFAULT, DEFAULT);

INSERT INTO cat.user_info (address, car, kakao, phone, report_count) VALUES (null, null, null, null, null);
INSERT INTO cat.user_info (address, car, kakao, phone, report_count) VALUES (null, null, null, null, null);
INSERT INTO cat.user_info (address, car, kakao, phone, report_count) VALUES (null, null, null, null, null);
INSERT INTO cat.user_info (address, car, kakao, phone, report_count) VALUES (null, null, null, null, null);

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