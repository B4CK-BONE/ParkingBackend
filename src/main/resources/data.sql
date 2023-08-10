INSERT INTO cat.user (address, car, email, phone, role, room_idx)
VALUES ('101', '12가1234', 'slknalsdknalkn@naver.com', '01012341234', 2, 1);
INSERT INTO cat.user (address, car, email, phone, role, room_idx)
VALUES ('102', '31다4245', 'qezxc@naver.com', '01011111111', 1, 1);
INSERT INTO cat.user (address, car, email, phone, role, room_idx)
VALUES ('103', '15다1345', 'aqerdfc@naver.com', '01021111111', DEFAULT, 1);
INSERT INTO cat.user (address, car, email, phone, role, room_idx)
VALUES ('104', '11다1345', 'aqe123rdfc@naver.com', '01021111211', DEFAULT, DEFAULT);

INSERT INTO cat.room (admin_idx)
VALUES (1);

INSERT INTO cat.parking_lot (bottom, direction, left_side, right_side, room_idx, slot, top)
VALUES ('a', 'a', 'a', 'a', 1, 1, 'a');
INSERT INTO cat.parking_lot (bottom, direction, left_side, right_side, room_idx, slot, top)
VALUES ('a', 'a', 'a', 'a', 1, 2, 'a');
INSERT INTO cat.parking_lot (bottom, direction, left_side, right_side, room_idx, slot, top)
VALUES ('a', 'a', 'a', 'a', 1, 3, 'a');